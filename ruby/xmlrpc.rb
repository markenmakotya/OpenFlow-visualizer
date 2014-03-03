require "xmlrpc/server"

class XmlRpc
	def initialize
		@s = XMLRPC::Server.new(8000)
	end

	def start topology
		@s.add_handler("topology_request") do |a|
			linkdata = ""
			flowdata = ""
			topo = topology.send_topology

			l_keys = topology.send_linkdata1
			l_values = topology.send_linkdata2
			l_values.length.times do |i|
				linkdata << "L#{l_keys[i][1].to_s}-#{l_keys[i][0].to_s}->#{l_values[i].to_s}> "
			end

			f_keys = topology.send_flowdata1
			f_values = topology.send_flowdata2
			f_values.length.times do |i|
				if f_values[i] > 0
					flowdata << "L#{f_keys[i][0].to_s}-#{f_keys[i][1].to_s}-matchstart#{f_keys[i][2].to_s}matchend->#{f_values[i].to_s}> "
				end
			end

			{"topology" => topo, "linkdata" => linkdata, "flowdata" => flowdata}
		end

		@s.add_handler("route_change_request") do |a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,z|

			change_mod_data = Hash.new("none")
			change_mod_data["dl_src"] = a
			change_mod_data["dl_dst"] = b
			change_mod_data["dl_type"] = c
			change_mod_data["nw_src"] = d
			change_mod_data["nw_dst"] = e
			change_mod_data["nw_proto"] = f
			change_mod_data["nw_tos"] = g
			change_mod_data["tp_src"] = h
			change_mod_data["tp_dst"] = i
			change_mod_data["route"] = j

			change_mod_data["dl_vlan"] = k
			change_mod_data["dl_vlan_pcp"] = l
			change_mod_data["port_id"] = m
			change_mod_data["idle_timeout"] = n
			change_mod_data["cookie"] = o
			change_mod_data["hard_timeout"] = p
			change_mod_data["priority"] = q
			change_mod_data["buffer_id"] = r
			change_mod_data["out_port"] = s
			change_mod_data["send_flow_rem"] = t
			change_mod_data["check_overlap"] = u
			change_mod_data["emerg"] = v
			change_mod_data["Forward"] = w
			change_mod_data["ModifyField"] = x
			change_mod_data["make"] = z
			change_mod_data["or_route"] = "none"

			routeok = topology.recv_change_moddata change_mod_data
		end

		@s.set_default_handler do |name, *args|
			raise XMLRPC::FaultException.new(-99, "Method #{name} missing" +" or wrong number of parameters!")
		end

		@s.serve
	end
end
