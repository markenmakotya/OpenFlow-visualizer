class Dicstra

	def initialize
		@db = {}
		@dest = {}
		@no_path = []
	end


	def make_own_topology topology, switches, dpid
		@db = {}
		@dest = {}
		@no_path = []
		dpid_pathed = []
		dpid_pathed << dpid
		dpid_fin = []
		search topology, switches, dpid, dpid_pathed, dpid_fin
	end



	def search topology, switches, dpid, dpid_pathed, dpid_fin
		switches[ dpid ].ports.length.times do |i|
			if topology.get_link(dpid, i) != nil
				if dpid_pathed.index(topology.get_link(dpid, i)[0]) == nil && dpid_fin.index(topology.get_link(dpid, i)[0]) == nil
					@db[[dpid, i]] = topology.get_link(dpid, i)
					@dest[@db[[dpid, i]][0]] = [dpid, i]
					dpid_pathed << @db[[dpid, i]][0]
				else
					if @no_path.index(topology.get_link(dpid, i)) == nil
						@no_path << [topology.get_link(dpid, i)[0],topology.get_link(dpid, i)[1]]
					end
				end
			end
		end
		dpid_fin << dpid
		dpid_pathed.shift
		if dpid_pathed[0] != nil
			search topology, switches, dpid_pathed[0], dpid_pathed, dpid_fin
		end
	end


	def search_link dpid, enddpid
		path = []
		while( @dest[enddpid] != nil )
			prepath = @dest[enddpid]
			path << prepath
			enddpid = prepath[0]
		end
		return path
	end


	def show_own_topology
		puts @db.to_a
	end



	def send_own_topology
		return @db.keys
	end



	def no_path_topology
		return @no_path
	end
end

