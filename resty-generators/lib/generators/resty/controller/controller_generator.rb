require 'rails/generators/resource_helpers'
require 'generators/resty/base'
module Resty
  module Generators
    class ControllerGenerator < Base
      include Rails::Generators::ResourceHelpers

      source_root File.expand_path('../../templates', __FILE__)
      
      argument :actions, :type => :array, :default => [], :banner => "action action"

      def create_controller_file
        template 'Controller.java', File.join(java_root, controllers_base_package.gsub(/\./, "/"), class_path, "#{controller_class_name}Controller.java")
      end

    end
  end
end
