require 'rails/generators/resource_helpers'
require 'generators/resty/base'
module Resty
  module Generators
    class ScaffoldGenerator < Base
      include Rails::Generators::ResourceHelpers

      source_root File.expand_path('../../templates', __FILE__)
      
      argument :attributes, :type => :array, :default => [], :banner => "field:type field:type"

      if defined? ::Ixtlan::ModifiedBy
        class_option :modified_by, :type => :boolean
      end
      class_option :timestamps, :type => :boolean
      class_option :parent,     :type => :string, :desc => "The parent class for the generated model"
      class_option :singleton,  :type => :boolean

      def create_model_file
        template 'Model.java', File.join(java_root, models_base_package.gsub(/\./, "/"), class_path, "#{class_name}.java")
      end

      def create_controller_file
        template 'Controller.java', File.join(java_root, controllers_base_package.gsub(/\./, "/"), class_path, "#{controller_class_name}Controller.java")
      end

      def actions
        if options[:singleton]
          keys = action_map.keys
          keys.delete('index')
          keys.delete('create')
          keys.delete('destroy')
          keys
        else
          action_map.keys
        end
      end
    end
  end
end
