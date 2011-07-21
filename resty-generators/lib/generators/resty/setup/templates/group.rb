class Group

  attr_accessor :name

  def initialize(attributes = {})
    @name = attributes['name']
  end

  def attributes
    { 'name' => name }
  end
end
