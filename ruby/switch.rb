
class TopologyPort
	def initialize port
		@port = port
	end


	def up?
		@port.up?
	end


	def down
	end
end


class TopologySwitch
	attr_reader :ports


	def initialize datapath_id
		@datapath_id = datapath_id
		@ports = []
	end



	def add port
		@ports << port
	end


	def delete port
		port.down
		@ports -= [ port ]
	end


  def find port
    candidate = nil
    @ports.each do | each |
      if port.name.nil?
        return each if each.number == port.number
      else
        return each if each.name == port.name
        if each.number == port.number
          candidate = each
        end
      end
    end
    candidate
  end
end


