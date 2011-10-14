require 'rails/generators/resource_helpers'
require 'generators/resty/base'
module Resty
  module Generators
    class ModelGenerator < Base

      source_root File.expand_path('../../templates', __FILE__)
      
      argument :attributes, :type => :array, :default => [], :banner => "field:type field:type"

      class_option :timestamps, :type => :boolean, :default => true
      class_option :read_only, :type => :boolean, :default => false
      class_option :modified_by, :type => :boolean, :default => false
      class_option :singleton, :type => :boolean, :default => false
      class_option :parent,     :type => :string, :desc => "The parent class for the generated model"

      def create_model_file
        template 'Model.java', File.join(java_root, models_package.gsub(/\./, "/"), class_path, "#{class_name}.java")
      end

      def create_event_files
        template 'Event.java', File.join(java_root, events_package.gsub(/\./, "/"), class_path, "#{class_name}Event.java")
        template 'EventHandler.java', File.join(java_root, events_package.gsub(/\./, "/"), class_path, "#{class_name}EventHandler.java")
      end

      def create_rest_service_file
        template 'RestService.java', File.join(java_root, restservices_package.gsub(/\./, "/"), class_path, "#{controller_class_name}RestService.java")
      end

      def controller_class_name
        @controller_class_name ||= class_name
      end

      def actions
        @actions ||= 
          begin
            keys = action_map.keys
            if options[:singleton]
              keys.delete('index')
              keys.delete('create')
              keys.delete('destroy')
            end
            if options[:read_only]
              keys.delete('update')
              keys.delete('create')
              keys.delete('destroy')
            end
            keys
          end
      end
    end
  end
end
