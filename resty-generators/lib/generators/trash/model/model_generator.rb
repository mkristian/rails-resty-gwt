module Rails
  module Generators
    class ModelGenerator < NamedBase #metagenerator
      argument :attributes, :type => :array, :default => [], :banner => "field:type field:type"
      hook_for :orm, :required => true

      hook_for :resty, :type => :boolean, :default => true do |controller|
        invoke controller, [class_name]
      end
    end
  end
end
