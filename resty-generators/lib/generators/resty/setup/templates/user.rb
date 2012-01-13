class User<% if options[:remote_users] -%> < ActiveRecord::Base

  attr_accessor :groups, :applications

  validates :login, :presence => true

  record_timestamps = false
<% else -%>

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
<% end -%>

  def self.authenticate(login, password)
    result = User.new
    if password.blank?
      result.log = "no password given with login: #{login}"
    elsif login.blank?
      result.log = "no login given"
    elsif password == "behappy"
      result.login = login
      result.name = login.humanize
<% if options[:remote_users] -%>
      result.id = 0
<% end -%>
      result.groups = [Group.new('name' => login)]
<% if options[:remote_users] -%>
      result.applications = []
<% end -%>
    else
      result.log = "wrong password for login: #{login}"
    end
    result
  end

  def self.reset_password(login)
    result = User.new(:login => login)
    begin
      Authentication.post(:reset_password, :login => login)
    rescue ActiveResource::ResourceNotFound
      result.log = "User(#{login}) not found"
    end
    result
  end

  def log=(msg)
    @log = msg
  end

  def to_log
    if @log
      @log
    else
      "User(#{id ? (id.to_s + ':') : ''}#{login})"
    end
  end

<% if options[:remote_users] -%>
  unless respond_to? :old_as_json
    alias :old_as_json :as_json
    def as_json(options = nil)
      options = { :methods => [ :applications ] } unless options
      old_as_json(options)
    end
  end
<% else -%>
  def valid?(ignore)
    @log.nil?
  end

  def new_record?
    false
  end
  alias :destroyed? :new_record?
<% end -%>

end
