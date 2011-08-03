class Session
  include ActiveModel::Serializers::JSON
  include ActiveModel::Serializers::Xml

  attr_accessor :permissions, :user

  def self.create(params = {})
    if Rails.application.config.respond_to? :remote_sso_url
      begin
        a = Authentication.create(:login => params[:login],
                                  :password => params[:password])
        result = new
        user = User.new
        user.login = a.login
        user.name = a.name
        user.groups = a.groups
        result.user = user
        result
      rescue ActiveResource::ResourceNotFound
        nil
      end
    else
      user = User.authenticate(params[:login], params[:password])
      if user
        result = new
        result.user = user
        result
      end
    end
  end

  def idle_session_timeout
    Rails.application.config.idle_session_timeout
  end

  def attributes
    {'idle_session_timeout' => idle_session_timeout, 'permissions' => permissions, 'user' => user}
  end

  def id
    ""
  end
end
