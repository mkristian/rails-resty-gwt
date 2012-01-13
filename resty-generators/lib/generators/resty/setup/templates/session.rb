require 'ixtlan/guard/abstract_session'
<% if options[:remote_users] -%>
require 'heartbeat'
<% end -%>

class Session < Ixtlan::Guard::AbstractSession
  include ActiveModel::Serializers::JSON
  include ActiveModel::Serializers::Xml

  def self.authenticate(login, password)
    User.authenticate(login, password)
  end

  def self.authenticate_remote(login, password)
    begin
      auth = Authentication.create(:login => login, :password => password)
<% if options[:remote_users] -%>
      user = User.find_by_login(auth.login)
      if user.nil?
        heart = Heartbeat.new
        heart.beat User
        user = User.find_by_login(auth.login)
        raise "user #{auth.login} not found" unless user
      end
<% else -%>
      user = User.new
      user.login = auth.login
<% end -%>
      user.name = auth.name
      user.groups = auth.groups
<% if options[:remote_users] -%>
      user.applications = auth.applications
<% end -%>
      user
    rescue ActiveResource::ResourceNotFound
      result = User.new
      result.log = "access denied #{login}" # error message
      result
    rescue ActiveResource::UnauthorizedAccess
      result = User.new
      result.log = "access denied #{login}" # error message
      result
    end
  end
end
