class Application
  include ActiveModel::Serializers::JSON
  include ActiveModel::Serializers::Xml

  attr_accessor :name, :url

  def initialize(attributes = {})
    @name = attributes['name']
    @url = attributes['url']
  end

  def attributes
    { 'name' => name, 'url' => url }
  end
end
