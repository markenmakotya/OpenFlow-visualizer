require "switch"
require "topology"
require "dicstra"
require "xmlrpc"

class SHController < Controller
	periodic_timer_event :send_lldp, 2
	periodic_timer_event :topology_renew, 2
	periodic_timer_event :send_traffic, 1

#	add_timer_event :age_fdb, 5, :periodic
	UINT16_MAX = ( 1 << 16 ) - 1
	OFPFW_ALL = ( 1 << 22 ) - 1
	ETH_ETHTYPE_LLDP = 0x88cc
	INITIAL_DISCOVERY_PERIOD = 5


	def start
		@@trema_start = 0
		@switches = {}
		@topology = Topology.new
		@dicstra = Hash.new do | hash, macsa |
			hash[ macsa ] = Dicstra.new
		end
		@flowmods = []
	end



	def switch_ready dpid
		add_switch dpid
#		add_flow_for_receiving_lldp dpid
#		add_flow_for_discarding_every_other_packet dpid
		send_message dpid, FeaturesRequest.new
		@topology.add_switch dpid
	end



	def switch_disconnected dpid
		ports( dpid ).each do | each |
			@switches[ dpid ].delete each
		end
		delete_switch dpid
		@topology.delete_switch dpid
	end



	def features_reply dpid, message
		message.ports.each do | each |
			@switches[ dpid ].add TopologyPort.new( each )
		end
	end



	def stats_reply dpid, message
		if message.stats[0].to_s.index("port_no") != nil
#			@topology.link_data(dpid.to_i, message.stats[0].to_s.slice(message.stats[0].to_s.index("port_no")+9..message.stats[0].to_s.index("\n",message.stats[0].to_s.index("port_no")+9)-1).to_i, message.stats[0].to_s.slice(message.stats[0].to_s.index("rx_bytes")+10..message.stats[0].to_s.index("\n",message.stats[0].to_s.index("rx_bytes")+10)-1).to_i)
#puts message.stats[0]
			@topology.link_data(dpid.to_i, message.stats[0].port_no.to_i, message.stats[0].rx_bytes.to_i, message.stats[0].tx_bytes.to_i)
		else
#puts "@"
#			puts "#{dpid.to_i}"
#			puts "#{message.stats[0].actions[0].port_number.to_i}"
#			puts "#{message.stats[0].match.to_s}"
#			puts "#{message.stats[0].byte_count.to_i}"
#puts "@@@@@@@@"
#puts message.stats[0]
#puts "::::"
#puts message.stats[0].match.to_s
#puts "@@@@@@@@"
			if message.stats[0].actions[0] != nil
#puts "#{dpid}-#{message.stats[0].actions[0].port_number}"
			if message.stats[0].actions[0].port_number.to_i == 65532
			aa = 10
				aa.times do |a|
				@topology.flow_data(dpid.to_i, a.to_i, message.stats[0].match.to_s, message.stats[0].byte_count.to_i, message.stats[0].match.dl_src.to_s)
				end
			elsif message.stats[0].actions[0].port_number.to_i == 65531

			elsif message.stats[0].actions[0].port_number.to_i == 65528
				@topology.flow_data(dpid.to_i, message.stats[0].actions[0].port_number.to_i, message.stats[0].match.to_s, message.stats[0].byte_count.to_i, message.stats[0].match.dl_src.to_s)
			else
				@topology.flow_data(dpid.to_i, message.stats[0].actions[0].port_number.to_i, message.stats[0].match.to_s, message.stats[0].byte_count.to_i, message.stats[0].match.dl_src.to_s)
			end
			end
		end
	end



	def packet_in dpid, message
		if @@trema_start > 1
			if "lldp" == message.data.strip.split(/\s*,\s*/)[0]
				@topology.add dpid, message.in_port, message.data.strip.split(/\s*,\s*/)[1].to_i, message.data.strip.split(/\s*,\s*/)[2]
			else
				info "---------------------------------------"
#start_time = Time.now.instance_eval { '%s.%03d' % [strftime('%Y%m%d%H%M%S'), (ctime.usec).round] }
#puts Time.now.instance_eval { '%s.%03d' % [strftime('%Y%m%d%H%M%S'), (ctime.usec).round] }
ctime = Time.now
start_time = ctime.usec
puts ctime.usec
				macsa = message.macsa
				macda = message.macda

				dicstra = @dicstra[ macsa ]

#				info "received a packet_in #{ dpid.to_hex }"
#				info "#{macsa} to #{macda}"
				if @topology.get_link(dpid, message.in_port) != nil
#					puts "#{@topology.get_link(dpid, message.in_port)[0].to_i.to_hex},#{@topology.get_link(dpid, message.in_port)[1].to_i} --> #{dpid.to_hex},#{message.in_port}"
				else
#					puts "host --> #{dpid.to_hex},#{message.in_port}"
					@topology.add_linkhost macsa, dpid
					@topology.add_linkport macsa, message.in_port
					@topology.add_linkporthost macsa, dpid, message.in_port
					@topology.add_host dpid, macsa
				end

				if @topology.linkhost(macsa) == dpid
					dicstra.make_own_topology @topology, @switches, dpid
				end

				pathable = 1
				dicstra.no_path_topology.length.times do |i|
					if dicstra.no_path_topology[i][0].to_i == dpid && dicstra.no_path_topology[i][1].to_i == message.in_port
						pathable = 0
					end
				end

				if pathable == 1
					destdpid = @topology.linkhost( macda )
					if destdpid != 0
						path = []
						path = dicstra.search_link(dpid, destdpid)
						match = Match.new( :dl_src => message.macsa, :dl_dst => message.macda )
						flow_mod destdpid, match, @topology.linkhostport( macda )
						packet_out destdpid, message, @topology.linkhostport( macda )

						path.length.times do |i|
							p = path.shift
							flow_mod p[0], match, p[1]
						end
					else	
						flood dpid, message
#						info "broad cast"
					end
				else
#					puts "get error!!!"
				end
ctime = Time.now
end_time = ctime.usec
puts ctime.usec
#end_time = Time.now.instance_eval { '%s.%03d' % [strftime('%Y%m%d%H%M%S'), (ctime.usec).round] }
#puts Time.now.instance_eval { '%s.%03d' % [strftime('%Y%m%d%H%M%S'), (ctime.usec).round] }
puts end_time.to_f - start_time.to_f
			end
		end
	end



	def flow_removed dpid, message
		info "flow removed"
	end



  ##############################################################################
  private
  ##############################################################################

	def add_switch dpid
		info "New Switch (datapath_id = #{ dpid.to_hex })"
		@switches[ dpid ] = TopologySwitch.new( dpid )
	end



	def delete_switch dpid
		@switches.delete dpid
	end



	def ports dpid
		if @switches[ dpid ]
			return @switches[ dpid ].ports
		end
	end



	def send_lldp
		@topology.switch.length.times do |i|
			ports(@topology.switch[i]).length.times do |f|
				packet_data = "lldp, #{@topology.switch[i]}, #{f}"
				send_packet_out(
					@topology.switch[i],
					:data => packet_data,
					:actions =>  [ActionOutput.new( f )],
					:zero_padding => true
				)
			end
		end
	end

=begin

	def add_flow_for_receiving_lldp dpid
		send_flow_mod_add(
			dpid,
			:priority => UINT16_MAX,
			:hard_timeout => INITIAL_DISCOVERY_PERIOD,
			:match => Match.new( :dl_type => 0 ),
			:actions => ActionOutput.new( :port => OFPP_CONTROLLER )
		)
	end



	def add_flow_for_discarding_every_other_packet dpid
		send_flow_mod_add(
			dpid,
			:priority => UINT16_MAX - 1,
			:hard_timeout => INITIAL_DISCOVERY_PERIOD,
			:match => Match.new( :wildcards => OFPFW_ALL )
		)
	end

=end

	def check_new_mod
		new_mod = @topology.check_new_mod
		if(new_mod.length != 0)
			newmod = Hash.new
			newmod[:dl_src] = Mac.new(new_mod["dl_src"]) if(new_mod["dl_src"] != "none") 
			newmod[:dl_dst] = Mac.new(new_mod["dl_dst"]) if(new_mod["dl_dst"] != "none") 
			newmod[:dl_type] = new_mod["dl_type"].to_i if(new_mod["dl_type"] != "none") 
			newmod[:nw_src] = IP.new(new_mod["nw_src"]) if(new_mod["nw_src"] != "none") 
			newmod[:nw_dst] = IP.new(new_mod["nw_dst"]) if(new_mod["nw_dst"] != "none") 
			newmod[:nw_proto] = new_mod["nw_proto"].to_i if(new_mod["nw_proto"] != "none") 
			newmod[:nw_tos] = new_mod["nw_tos"].to_i if(new_mod["nw_tos"] != "none") 
			newmod[:tp_src] = new_mod["tp_src"].to_i if(new_mod["tp_src"] != "none") 
			newmod[:tp_dst] = new_mod["tp_dst"].to_i if(new_mod["tp_dst"] != "none")
			newmod[:dl_vlan] = new_mod["dl_vlan"].to_i if(new_mod["dl_vlan"] != "none") 
			newmod[:dl_vlan_pcp] = new_mod["dl_vlan_pcp"].to_i if(new_mod["dl_vlan_pcp"] != "none") 
			newmod[:port_id] = new_mod["port_id"].to_i if(new_mod["port_id"] != "none")
			newmod_match = Match.new(newmod)

			modhash = Hash.new
			modhash[:match] = newmod_match
			modhash[:idle_timeout] = new_mod["idle_timeout"].to_i if(new_mod["idle_timeout"] != "none") 
			modhash[:cookie] = new_mod["cookie"].to_i if(new_mod["cookie"] != "none") 
			modhash[:hard_timeout] = new_mod["hard_timeout"].to_i if(new_mod["hard_timeout"] != "none")
			modhash[:priority] = new_mod["priority"].to_i if(new_mod["priority"] != "none") 
			modhash[:buffer_id] = new_mod["buffer_id"].to_i if(new_mod["buffer_id"] != "none") 
			modhash[:out_port] = new_mod["out_port"].to_i if(new_mod["out_port"] != "none")
			modhash[:send_flow_rem] = Boolean.new_mod["send_flow_rem"] if(new_mod["send_flow_rem"] != "none") 
			modhash[:check_overlap] = Boolean.new_mod["check_overlap"] if(new_mod["check_overlap"] != "none") 
			modhash[:emerg] = Boolean.new_mod["emerg"] if(new_mod["emerg"] != "none")

			newmod[:Forward] = new_mod["Forward"]

			newmod[:ModifyField] = new_mod["ModifyField"].to_s
			if newmod[:ModifyField][0..3] == "none"
#				modify = nil
			elsif newmod[:ModifyField][0..4] == "dlsrc"
				modify = ActionSetDlSrc.new( newmod[:ModifyField][6..newmod[:ModifyField].length-2] )
			elsif newmod[:ModifyField][0..4] == "dldst"
				modify = ActionSetDlDst.new( newmod[:ModifyField][6..newmod[:ModifyField].length-2] )
			elsif newmod[:ModifyField][0..4] == "nwsrc"
				modify = ActionSetNwSrc.new( newmod[:ModifyField][6..newmod[:ModifyField].length-2] )
			elsif newmod[:ModifyField][0..4] == "nwdst"
				modify = ActionSetNwDst.new( newmod[:ModifyField][6..newmod[:ModifyField].length-2] )
			elsif newmod[:ModifyField][0..4] == "tpsrc"
				modify = ActionSetTpSrc.new( newmod[:ModifyField][6..newmod[:ModifyField].length-2].to_i )
			elsif newmod[:ModifyField][0..4] == "tpdst"
				modify = ActionSetTpDst.new( newmod[:ModifyField][6..newmod[:ModifyField].length-2].to_i )
			end
#			puts newmod_match

			routes = new_mod["route"]
			if new_mod["or_route"] == "-"
				(routes.length-1).times do |i|
					linkport = @topology.get_linkport(routes[i].to_i, ports(routes[i]).length, routes[i+1].to_i)
					if modify == nil
					modhash[:actions] = SendOutPort.new( linkport )
					else
					modhash[:actions] = [SendOutPort.new( linkport ),modify]
					end
					new_flow_mod routes[i], newmod_match, modhash
				end

				linkport = @topology.linkhostport new_mod["dl_dst"]
					if modify == nil
					modhash[:actions] = SendOutPort.new( linkport )
					else
					modhash[:actions] = [SendOutPort.new( linkport ),modify]
					end
				new_flow_mod routes[(routes.length-1)], newmod_match, modhash

				@topology.delete_new_mod
			end
			if new_mod["or_route"] == "*"
#puts "1"
				sr = @topology.linkhost(newmod[:dl_src])
				ds = @topology.linkhost(newmod[:dl_dst])

				dicstra = @dicstra[ sr ]
				dicstra.make_own_topology @topology, @switches, sr

				pre = sr
				(routes.length).times do |i|
#puts "2-----#{routes[i]}"
					if routes[i].to_i != sr && routes[i].to_i != ds
						routess = routes[i].to_i
#puts "3"
						dicstra = @dicstra[ pre ]
						dicstra.make_own_topology @topology, @switches, pre

						path = dicstra.search_link(pre, routess)

						path.length.times do |i|
							p = path.shift
#puts "#{p[0]}--#{@topology.get_link(p[0],p[1])}   sr=#{sr} ds=#{ds} @@#{routess}"
							modhash[:actions] = SendOutPort.new( p[1] )
							new_flow_mod p[0], newmod_match, modhash
						end
						pre = routess
#						linkport = @topology.get_linkport(routes[i].to_i, ports(routes[i]).length, routes[i+1].to_i)
#						modhash[:actions] = SendOutPort.new( p[1] )
#						new_flow_mod p[0], newmod_match, modhash
			
					end
				end
#puts "4"
				dicstra = @dicstra[ pre ]
				dicstra.make_own_topology @topology, @switches, pre
				path = dicstra.search_link(pre, ds)
#puts "pre=#{pre}"
				path.length.times do |i|
					p = path.shift
#puts "#{p[0]}--#{@topology.get_link(p[0],p[1])}   sr=#{sr} ds=#{ds}"
					modhash[:actions] = SendOutPort.new( p[1] )
					new_flow_mod p[0], newmod_match, modhash
				end
#puts "5"

#				linkport = @topology.linkhostport new_mod["dl_dst"]
				modhash[:actions] = SendOutPort.new( @topology.linkhostport( newmod[:dl_dst] ) )
				new_flow_mod ds, newmod_match, modhash

				@topology.delete_new_mod
			end
			if new_mod["or_route"] == ","
				routes.length.times do |i|
					if new_mod["Forward"] != "none"				
						if modify == nil
							if new_mod["Forward"] == "IN_PORT"
								modhash[:actions] = ActionOutput.new( OFPP_IN_PORT )
							elsif new_mod["Forward"] == "ALL"
								modhash[:actions] = ActionOutput.new( OFPP_ALL )
							elsif new_mod["Forward"] == "FLOOD"
								modhash[:actions] = ActionOutput.new( OFPP_FLOOD )
							elsif new_mod["Forward"] == "CONTROLLER"
								modhash[:actions] = ActionOutput.new( OFPP_CONTROLLER )
							else new_mod["Forward"] == "LOCAL"
								modhash[:actions] = ActionOutput.new( OFPP_LOCAL )
							end
							new_flow_mod routes.to_i, newmod_match, modhash
						else
							if new_mod["Forward"] == "IN_PORT"
								modhash[:actions] = [modify,ActionOutput.new( OFPP_IN_PORT )]
							elsif new_mod["Forward"] == "ALL"
								modhash[:actions] = [modify,ActionOutput.new( OFPP_ALL )]
							elsif new_mod["Forward"] == "FLOOD"
								modhash[:actions] = [modify,ActionOutput.new( OFPP_FLOOD )]
							elsif new_mod["Forward"] == "CONTROLLER"
								modhash[:actions] = [modify,ActionOutput.new( OFPP_CONTROLLER )]
							else new_mod["Forward"] == "LOCAL"
								modhash[:actions] = [modify,ActionOutput.new( OFPP_LOCAL )]
							end
							new_flow_mod routes.to_i, newmod_match, modhash
						end
					else
						if modify == nil
							new_no_flow_mod routes.to_i, newmod_match, modhash
						else
							modhash[:actions] = modify
							new_flow_mod routes.to_i, newmod_match, modhash
						end
						
					end
				end
				@topology.delete_new_mod
			end
			if new_mod["or_route"] == "one"
				if new_mod["Forward"] != "none"				
					if modify == nil
						if new_mod["Forward"] == "IN_PORT"
							modhash[:actions] = ActionOutput.new( OFPP_IN_PORT )
						elsif new_mod["Forward"] == "ALL"
							modhash[:actions] = ActionOutput.new( OFPP_ALL )
						elsif new_mod["Forward"] == "FLOOD"
							modhash[:actions] = ActionOutput.new( OFPP_FLOOD )
						elsif new_mod["Forward"] == "CONTROLLER"
							modhash[:actions] = ActionOutput.new( OFPP_CONTROLLER )
						else new_mod["Forward"] == "LOCAL"
							modhash[:actions] = ActionOutput.new( OFPP_LOCAL )
						end
						new_flow_mod routes.to_i, newmod_match, modhash
					else
						if new_mod["Forward"] == "IN_PORT"
							modhash[:actions] = [modify,ActionOutput.new( OFPP_IN_PORT )]
						elsif new_mod["Forward"] == "ALL"
							modhash[:actions] = [modify,ActionOutput.new( OFPP_ALL )]
						elsif new_mod["Forward"] == "FLOOD"
							modhash[:actions] = [modify,ActionOutput.new( OFPP_FLOOD )]
						elsif new_mod["Forward"] == "CONTROLLER"
							modhash[:actions] = [modify,ActionOutput.new( OFPP_CONTROLLER )]
						else new_mod["Forward"] == "LOCAL"
							modhash[:actions] = [modify,ActionOutput.new( OFPP_LOCAL )]
						end
						new_flow_mod routes.to_i, newmod_match, modhash
					end
				else
						if modify == nil
							new_no_flow_mod routes.to_i, newmod_match, modhash
						else

							modhash[:actions] = modify

							new_flow_mod routes.to_i, newmod_match, modhash

						end
				end
				@topology.delete_new_mod

			end
		end
	end



	def new_flow_mod dpid, newmod_match, options

		if @flowmods != nil
			overwrite = 0
			@flowmods.length.times do |k|
				if newmod_match.compare(@flowmods[k]) == true
					overwrite = 1
					send_flow_mod_modify(
						dpid,
						options
					)
				end
			end
			if overwrite == 0
					send_flow_mod_add(
						dpid,
			#			:match => newmatch,
						options
			#			:actions => SendOutPort.new( out_port )  #ActionOutput.new( out_port )
					)
				@flowmods << newmod_match
			end
		end
#		puts "new_sent_mod #{dpid.to_hex}--#{out_port}"
	end

	def new_no_flow_mod dpid, newmod_match, options
		send_flow_mod_add(
			dpid,
#			:match => newmatch
			options
		)
		if @flowmods != nil
			overwrite = 0
			@flowmods.length.times do |k|
				if newmod_match.compare(@flowmods[k]) == true
					send_flow_mod_modify(
						dpid,
				#		:match => newmatch
						options
					)
					overwrite = 1
				end
			end
			if overwrite == 0
					send_flow_mod_add(
						dpid,
				#		:match => newmatch
						options
					)
				@flowmods << newmod_match
			end
		end
		#puts "new_sent_mod #{dpid.to_hex}--#{out_port}"
	end

	def flow_mod dpid, match, out_port
		modhash = Hash.new
		modhash[:match] = match
		modhash[:actions] = ActionOutput.new( out_port )
		send_flow_mod_add(
			dpid,
#			:priority => UINT16_MAX-5,
#			:hard_timeout => 3,
#			:match => ExactMatch.from( message ),
#			:check_overlap => true,
#			:match => match,
#			:actions => ActionOutput.new( out_port )
			modhash
		)
		puts "sent mod #{dpid.to_hex}--#{out_port}"
		if @flowmods != nil
			overwrite = 0
			@flowmods.length.times do |k|
				if match.compare(@flowmods[k]) == true
					overwrite = 1
				end
			end
			if overwrite == 0
				@flowmods << match
			end
		end
	end


	def ofpp_flow_mod dpid, message
		send_flow_mod_add(
			dpid,
			:hard_timeout => 1,
			:match => Match.new( :dl_src => message.macsa, :dl_dst => message.macda ),
			:actions => ActionOutput.new( OFPP_FLOOD )
		)
		puts "sent floodmod #{dpid.to_hex}"
	end


	def packet_out dpid, message, out_port
		send_packet_out(
			dpid,
			:packet_in => message,
			:actions => ActionOutput.new( out_port )
		)
	end



	def flood dpid, message
		packet_out dpid, message, OFPP_FLOOD
#		ofpp_flow_mod dpid, message
	end



	def topology_renew
		renew_or = @topology.renew
		if @@trema_start < 2	
			@@trema_start = @@trema_start + 1
		elsif @@trema_start == 2
			puts "@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
			puts "@@@@    trema start!    @@@@"
			puts "@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
			@@trema_start = 3

			xmlrpc = Thread.new do
				periodic_timer_event :check_new_mod, 1
				@xmlrpc = XmlRpc.new
				@xmlrpc.start @topology
			end
		else
			if renew_or == 1
				@topology.switch.length.times do |i|
					send_flow_mod_delete @topology.switch[i]
				end
				puts "@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				puts "@@@@  delete all mods!  @@@@"
				puts "@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
			end
		end
#		show_switches
#		show_topology
	end




	def send_traffic
#puts "@@@@@@@@@@@@@@@@@@@@@@@@@@#{@flowmods}"
		@topology.switch.length.times do |i|
			if @flowmods != nil
				@flowmods.length.times do |k|
					send_message i+1, FlowStatsRequest.new(:match => @flowmods[k])
#puts "@ #{k}"
				end
			end
			ports(@topology.switch[i]).length.times do |f|
				send_message @topology.switch[i], PortStatsRequest.new( :port_no => f)
#puts "#{@topology.switch[i].to_hex} #{f}   %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
			end
		end
	end




	def show_switches
		info "---------------------------------------"
		@topology.switch.length.times do |i|
			info "switch = #{ @topology.switch[i].to_hex }"
#			puts ports( @topology.switch[i] )
		end
	end




	def show_topology
		info "---------------------------------------"
		@topology.show_topology
	end

end

