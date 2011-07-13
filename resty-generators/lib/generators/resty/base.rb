require 'rails/generators/named_base'
module Resty
  module Generators
    class Base < Rails::Generators::NamedBase

      protected

      def application_name
        @application_name ||= Rails.application.class.to_s.gsub(/::/,'').sub(/Application$/, '')
      end

      def java_root
        @java_root ||= File.join('src', 'main', 'java')
      end

      def base_package
        @base_package ||= 
          begin
            fullpath = find_gwt_xml(java_root)
            raise "no gwt module found - maybe run 'rails g resty:setup'" unless fullpath
            fullpath.sub(/#{java_root}./, '').sub(/[a-zA-Z0-9_]+.gwt.xml$/, '').gsub(/[\/\\]/, '.') + "client"
          end
      end

      def managed_package
        @managed_package ||= base_package + ".managed"
      end

      def models_package
        @models_package ||= base_package + ".models"
      end

      def views_package
        @views_package ||= base_package + ".views"
      end

      def places_package
        @places_package ||= base_package + ".places"
      end

      def activities_package
        @activities_package ||= base_package + ".activities"
      end

      def gwt_rails_package
        'de.mkristian.gwt.rails'
      end

      def rest_services_package
        @rest_services_package ||= base_package + ".restservices"
      end

      def action_map
        @action_map ||= {'index' => :get_all, 'show' => :get_single, 'create' => :post, 'update' => :put, 'destroy' => :delete}
      end

      def type_map
        @type_map ||= {:integer => 'int', :boolean => 'bool', :string => 'String', :float => 'double', :date => 'java.util.Date', :datetime => 'java.util.Date', :number => 'long', :fixnum => 'long'}
      end

      def type_conversion_map
        @type_conversion_map ||= {:integer => 'Integer.parseInt', :boolean => 'Boolean.parseBoolean', :float => 'Double.parseDouble', :date => 'TODO', :datetime => 'TODO', :number => 'Long.parseLong', :fixnum => 'Long.parseLong'}
      end

      def find_gwt_xml(basedir)
        Dir[File.join(basedir, "*")].each do |path|
          if File.directory?(path)
            result = find_gwt_xml(path)
            return result if result
          elsif File.file?(path)
            return path if path =~ /.gwt.xml$/
          end
        end
        nil
      end
    end
  end
end
