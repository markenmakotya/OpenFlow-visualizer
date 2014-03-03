class PreDLink
	def initialize
		@predpids = []
	end

	def add_dpid dpid
		if @predpids.index(dpid) == nil
			@predpids << dpid
		end
	end
	
	def delete_dpid dpid
		if @predpids.index(dpid) != nil
			@predpids -= [ dpid ]
		end
	end

	def get_dpids
		return @predpids
	end


end



class PreTopology

	def initialize
		@predb = {}
		@predpid_link = Hash.new do | hash, dpid |
			hash[ dpid ] = DLink.new
		end
	end


	def delete
		@predb = {}
		@predpid_link = Hash.new do | hash, dpid |
			hash[ dpid ] = DLink.new
		end
	end


	def add dpid, port, from_dpid, from_port
		to = [dpid, port]
		from = [from_dpid, from_port]
		@predb[ to ] = from
		@predpid_link[ dpid ].add_dpid from_dpid
	end



	def get_db
		return @predb
	end



	def get_dpids dpid
		return @predpid_link[ dpid ].get_dpids
	end

end

