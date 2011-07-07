require 'rails/generators/resource_helpers'
require 'generators/resty/base'
module Resty
  module Generators
    class ControllerGenerator < Base
      include Rails::Generators::ResourceHelpers

      source_root File.expand_path('../../templates', __FILE__)
      
      argument :actions, :type => :array, :default => [], :banner => "action action"

      def create_rest_service_file
        template 'RestService.java', File.join(java_root, rest_services_base_package.gsub(/\./, "/"), class_path, "#{controller_class_name}RestService.java")
      end

    end
  end
end
