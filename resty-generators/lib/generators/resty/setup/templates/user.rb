class User
  include ActiveModel::Serializers::JSON
  include ActiveModel::Serializers::Xml
  
  attr_accessor :login, :name, :groups

  def attributes
    {'login' => login, 'name' => name, 'groups' => groups.collect { |g| g.attributes } }
  end

  def initialize(attributes = {})
    @login = attributes['login']
    @name = attributes['name']
    @groups = (attributes['groups'] || []).collect {|g| Group.new g }
  end

  def self.authenticate(login, password)
    if login.size > 0 && password == "behappy"
      u = User.new
      u.login = login
      u.name = login.humanize
      u.groups = [Group.new('name' => login)]
      u
    end
  end
end
