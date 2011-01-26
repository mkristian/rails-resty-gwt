require 'rails/generators/resource_helpers'
require 'generators/resty/base'
module Resty
  class ScaffoldGenerator < Base
    include Rails::Generators::ResourceHelpers

    source_root File.expand_path('../../templates', __FILE__)
  
    argument :attributes, :type => :array, :default => [], :banner => "field:type field:type"

    class_option :timestamps, :type => :boolean
    class_option :parent,     :type => :string, :desc => "The parent class for the generated model"

    def create_model_file
      template 'model.java', File.join(java_root, models_base_package.gsub(/\./, "/"), class_path, "#{class_name}.java")
    end

    def create_controller_file
      template 'controller.java', File.join(java_root, controllers_base_package.gsub(/\./, "/"), class_path, "#{controller_class_name}Controller.java")
    end

    def actions
      action_map.keys
    end
  end
end
