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

      def events_package
        @events_package ||= base_package + ".events"
      end

      def editors_package
        @editors_package ||= base_package + ".editors"
      end

      def activities_package
        @activities_package ||= base_package + ".activities"
      end

      def gwt_rails_package
        'de.mkristian.gwt.rails'
      end

      def gwt_rails_session_package
        @gwt_rails_session_package ||= gwt_rails_package + '.session'
      end

      def restservices_package
        @restservices_package ||= base_package + ".restservices"
      end

      def caches_package
        @caches_package ||= base_package + ".caches"
      end

      def action_map
        @action_map ||= {'index' => :get_all, 'show' => :get_single, 'create' => :post, 'update' => :put, 'destroy' => :delete}
      end

      def type_map
        @type_map ||= {:integer => 'int', :boolean => 'boolean', :string => 'String', :float => 'double', :date => 'java.util.Date', :datetime => 'java.util.Date', :number => 'long', :fixnum => 'long', :text => 'String', :password => 'String'}
      end

      def type_widget_map
        @type_widget_map ||= {:integer => 'r:IntegerBox', :boolean => 'g:CheckBox', :float => 'r:DoubleBox', :date => 'd:DateBox', :datetime => 'd:DateBox', :number => 'r:LongBox', :fixnum => 'r:LongBox', :text => 'g:TextArea', :string => 'g:TextBox', :password => 'g:PasswordTextBox', :float => 'r:DoubleBox'}
      end

     def type_widget_prefix_map
        @type_widget_prefix_map ||= {:integer => 'r', :boolean => 'g', :float => 'r', :date => 'd', :datetime => 'd', :number => 'r', :fixnum => 'r', :text => 'g', :string => 'g', :passord => 'g', :float => 'r'}
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
