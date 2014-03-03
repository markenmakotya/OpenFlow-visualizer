require "pre_topology"

class DLink
	def initialize
		@dpids = []
		@hosts = []
	end

	def add_dpid dpid
		if @dpids.index(dpid) == nil
			@dpids << dpid
		end
	end
	
	def delete_dpid dpid
		if @dpids.index(dpid) != nil
			@dpids -= [ dpid ]
		end
	end

	def get_dpids
		return @dpids
	end


	def renew_dpids dpid
		@dpids = dpid
	end

	def add_host mac
		if @hosts.index(mac) == nil
			@hosts << mac
		end
	end
	
	def delete_host mac
		if @hosts.index(mac) != nil
			@hosts -= [ mac ]
		end
	end

	def get_hosts
		return @hosts
	end

end



class Topology
@@link_host = Hash.new(0)
@@link_port = Hash.new(0)
@@link_porthost = Hash.new("none")
@@db = {}
@@switch_list = []
@@dpid_link = Hash.new do | hash, dpid |
	hash[ dpid ] = DLink.new
end
@@pretopology = PreTopology.new
@@linkdata = {}
@@prelinkdata = {}
@@newmod = Hash.new("none")
@@flowdata = {}
@@preflowdata = {}

	def initialize

	end



	def renew
		renew_or = 0
		@@switch_list.length.times do |i|
			if @@dpid_link[@@switch_list[i]].get_dpids.sort != @@pretopology.get_dpids(@@switch_list[i]).sort
				renew_or = 1
			end
		end
		if renew_or == 1
			@@db = @@pretopology.get_db
			@@switch_list.length.times do |i|
				@@dpid_link[@@switch_list[i]].renew_dpids(@@pretopology.get_dpids(@@switch_list[i]))
			end
			puts "@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
			puts "@@@@   topology renew   @@@@"
			puts "@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
		end

		@@pretopology.delete

		return renew_or
	end



	def add dpid, port, from_dpid, from_port
		@@pretopology.add dpid, port, from_dpid, from_port
	end


	
	def get_links dpid, ports
		links = []
		ports.times do |i|
			if @@db[[dpid, i]] != nil
				links << @@db[[dpid, i]][0]
			end
		end
		return links
	end



	def get_linkport dpid, ports, dstdpid
		linkport = 0
		ports.times do |i|
			if @@db[[dpid, i]] != nil
				if @@db[[dpid, i]][0] == dstdpid
					linkport = i
				end
			end
		end
		return linkport
	end



	def get_link dpid, port
		link = nil
		if @@db[[dpid, port]] != nil
			link = @@db[[dpid, port]]
		end
		return link
	end


	def switch
		return @@switch_list
	end

	def add_switch dpid
		@@switch_list << dpid
	end
	
	def delete_switch dpid
		@@switch_list -= [ dpid ]
	end


	def add_host dpid, mac
		@@dpid_link[ dpid ].add_host mac
	end
	
	def get_hosts dpid
		return @@dpid_link[ dpid ].get_hosts
	end


	def add_linkhost mac, dpid
		if @@link_host[ mac ] == 0
			@@link_host[ mac ] = dpid
		end
	end

	def add_linkporthost mac, dpid, port
		if @@link_porthost[[dpid, port]] == "none"
			@@link_porthost[[dpid, port]] = mac.to_s
		end
	end

	def get_linkporthost dpid, port
		if @@link_porthost[[dpid, port]] != "none"
			return @@link_porthost[[dpid, port]]
		else
			return "none"
		end
	end

	def linkhost mac
		if @@link_host[ mac ] != 0
			return @@link_host[ mac ]
		else
			return 0
		end
	end

	def add_linkport mac, port
		if @@link_port[ mac ] == 0
			@@link_port[ mac ] = port
		end
	end

	def linkhostport mac
		if @@link_port[ mac ] != 0
			return @@link_port[ mac ]
		else
			return 0
		end
	end

	def get_dpids dpid
		return @@dpid_link[dpid].get_dpids
	end



	def show_topology
		@@switch_list.length.times do |i|
			if @@dpid_link[@@switch_list[i]].get_hosts != []
				puts "#{@@dpid_link[@@switch_list[i]].get_hosts.join(",")}-->#{@@switch_list[i].to_hex}"
			end
			puts "#{@@switch_list[i].to_hex}-->#{@@dpid_link[@@switch_list[i]].get_dpids.join(",")}"
		end
	end



	def send_topology
		topo = ""
#		topo.concat(@@switch_list.length)
		@@switch_list.length.times do |i|
			if @@dpid_link[@@switch_list[i]].get_hosts != []
				topo.concat("SW#{@@switch_list[i]}-,#{@@dpid_link[@@switch_list[i]].get_dpids.join(",")},#{@@dpid_link[@@switch_list[i]].get_hosts.join(",")}, ")
			else
				topo.concat("SW#{@@switch_list[i]}-,#{@@dpid_link[@@switch_list[i]].get_dpids.join(",")}, ")
			end
		end
		return topo
	end



	def link_data dpid, port, data, n_data
		if get_link(dpid, port) != nil
			dest = get_link(dpid, port)[0]
			if @@prelinkdata[[dpid, dest]] == nil
				@@prelinkdata[[dpid, dest]] = data
			else
				@@linkdata[[dpid, dest]] = data - @@prelinkdata[[dpid, dest]]
				@@prelinkdata[[dpid, dest]] = data
			end
		else
			if data > 0
				dest = get_linkporthost dpid, port
				if dest != "none"
					if @@prelinkdata[[dpid, dest]] == nil
						@@prelinkdata[[dpid, dest]] = data
					else
						@@linkdata[[dpid, dest]] = data - @@prelinkdata[[dpid, dest]]
						@@prelinkdata[[dpid, dest]] = data
					end
				end
			end
		end

		if n_data > 0
			dest = get_linkporthost dpid, port
			if dest != "none"
				if @@prelinkdata[[dest, dpid]] == nil
					@@prelinkdata[[dest, dpid]] = n_data
				else
					@@linkdata[[dest, dpid]] = n_data - @@prelinkdata[[dest, dpid]]
					@@prelinkdata[[dest, dpid]] = n_data
				end
			end
		end
	end


	def send_linkdata1
		return @@linkdata.keys
	end


	def send_linkdata2
		return @@linkdata.values
	end


	def recv_change_moddata change_mod_data
		route_ok = 0

		if change_mod_data["route"].index("-") != nil
			routes = change_mod_data["route"].split("-")

			routes.length.times do |i|
				routes[i] = routes[i].to_i
				if(@@switch_list.index(routes[i]) == nil)
					route_ok = 1
				end	
			end

			if(route_ok==0)
				((routes.length)-1).times do |i|
					if(@@dpid_link[routes[i]].get_dpids.index(routes[i+1]) == nil)
						route_ok = 1
					end
				end
			end

			if(route_ok==0)
				change_mod_data["route"] = routes
				change_mod_data["or_route"] = "-"
				@@newmod = change_mod_data
			end

		elsif change_mod_data["route"].index("*") != nil
			routes = change_mod_data["route"].split("*")
			routes.length.times do |i|
				routes[i] = routes[i].to_i
				if(@@switch_list.index(routes[i]) == nil)
					route_ok = 1
				end	
			end

			if(route_ok==0)
				change_mod_data["route"] = routes
				change_mod_data["or_route"] = "*"
				@@newmod = change_mod_data

			end

		elsif change_mod_data["route"].index(",") != nil
			routes = change_mod_data["route"].split(",")

			routes.length.times do |i|
				routes[i] = routes[i].to_i
				if(@@switch_list.index(routes[i]) == nil)
					route_ok = 1
				end	
			end

			if(route_ok==0)
				change_mod_data["route"] = routes
				change_mod_data["or_route"] = ","
				@@newmod = change_mod_data

			end

		else change_mod_data["route"].length == 1
			#puts "@@@@@@"
			routes = change_mod_data["route"].to_i
			if(@@switch_list.index(routes) == nil)
				route_ok = 1
			end	

			if(route_ok==0)
				change_mod_data["route"] = routes
				change_mod_data["or_route"] = "one"
				@@newmod = change_mod_data
				#puts "fafasfafsa@@@@@@"
			end
		end

		return route_ok
	end



	def flow_data dpid, port, match, data, mac_src
		if get_link(dpid, port) != nil
			dest = get_link(dpid, port)[0]
			if @@preflowdata[[dpid, dest, match]] == nil
				@@preflowdata[[dpid, dest, match]] = data
			else
				@@flowdata[[dpid, dest, match]] = data - @@preflowdata[[dpid, dest, match]]
				@@preflowdata[[dpid, dest, match]] = data
			end
		else
			if data > 0
				dest = get_linkporthost dpid, port
				if dest != "none"
					if @@preflowdata[[dpid, dest, match]] == nil
						@@preflowdata[[dpid, dest, match]] = data
					else
						@@flowdata[[dpid, dest, match]] = data - @@preflowdata[[dpid, dest, match]]
						@@preflowdata[[dpid, dest, match]] = data
					end
				end
			end
		end
		if data > 0
			mac_src = Mac.new(mac_src)
			if linkhost(mac_src) == dpid
				dest = mac_src
				if @@preflowdata[[dest, dpid, match]] == nil
					@@preflowdata[[dest, dpid, match]] = data
				else
					@@flowdata[[dest, dpid, match]] = data - @@preflowdata[[dest, dpid, match]]
					@@preflowdata[[dest, dpid, match]] = data
				end
			end
		end
	end


	def send_flowdata1
		return @@flowdata.keys
	end


	def send_flowdata2
		return @@flowdata.values
	end



	def check_new_mod
		return @@newmod
	end

	def delete_new_mod
		@@newmod = Hash.new("none")
	end
end

