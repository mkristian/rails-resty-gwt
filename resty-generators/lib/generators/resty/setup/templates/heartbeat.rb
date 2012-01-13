require 'ixtlan/core/heartbeat'
class Heartbeat < Ixtlan::Core::Heartbeat

  def initialize
    super
    register(User, RemoteUser)
  end

end
