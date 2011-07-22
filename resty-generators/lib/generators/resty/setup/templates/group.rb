class Group
  include ActiveModel::Serializers::JSON
  include ActiveModel::Serializers::Xml

  attr_accessor :name

  def initialize(attributes = {})
    @name = attributes['name']
  end

  def attributes
    { 'name' => name }
  end
end
