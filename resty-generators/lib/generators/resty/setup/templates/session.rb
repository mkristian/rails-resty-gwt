require 'ixtlan/guard/abstract_session'

class Session < Ixtlan::Guard::AbstractSession
  include ActiveModel::Serializers::JSON
  include ActiveModel::Serializers::Xml

  def self.authenticate(login, password)
    User.authenticate(login, password)
  end

  def self.authenticate_remote(login, password)
    begin
      auth = Authentication.create(:login => login, :password => password)
      user = User.new
      user.login = auth.login
      user.name = auth.name
      user.groups = auth.groups
      user
    rescue ActiveResource::ResourceNotFound
      result = User.new
      result.log = "access denied #{login}" # error message
      result
    end
  end
end
