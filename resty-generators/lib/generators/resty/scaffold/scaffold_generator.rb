require 'rails/generators/resource_helpers'
require 'generators/resty/base'
module Resty
  module Generators
    class ScaffoldGenerator < Base
      include Rails::Generators::ResourceHelpers

      source_root File.expand_path('../../templates', __FILE__)
      
      argument :attributes, :type => :array, :default => [], :banner => "field:type field:type"

      class_option :timestamps,  :type => :boolean, :default => true
      class_option :read_only,   :type => :boolean, :default => false
      class_option :modified_by, :type => :boolean, :default => false
      class_option :singleton,   :type => :boolean, :default => false
      class_option :parent,      :type => :string, :desc => "The parent class for the generated model"

      def create_model_file
        template 'Model.java', File.join(java_root, models_package.gsub(/\./, "/"), class_path, "#{class_name}.java")
      end

      def create_cache_file
        unless options[:singleton]
          template 'Cache.java', File.join(java_root, caches_package.gsub(/\./, "/"), class_path, "#{class_name.pluralize}Cache.java")
        end
      end

      def create_event_files
        template 'Event.java', File.join(java_root, events_package.gsub(/\./, "/"), class_path, "#{class_name}Event.java")
        template 'EventHandler.java', File.join(java_root, events_package.gsub(/\./, "/"), class_path, "#{class_name}EventHandler.java")
      end

      def create_rest_service_file
        template 'RestService.java', File.join(java_root, restservices_package.gsub(/\./, "/"), class_path, "#{controller_class_name}RestService.java")
      end

      def create_view_files
        template 'View.java', File.join(java_root, views_package.gsub(/\./, "/"), class_path, "#{class_name}View.java")
        template 'View.ui.xml', File.join(java_root, views_package.gsub(/\./, "/"), class_path, "#{class_name}View.ui.xml")
        template 'ViewImpl.java', File.join(java_root, views_package.gsub(/\./, "/"), class_path, "#{class_name}ViewImpl.java")
      end

      def create_editor_files
        template 'Editor.java', File.join(java_root, editors_package.gsub(/\./, "/"), class_path, "#{class_name}Editor.java")
        template 'Editor.ui.xml', File.join(java_root, editors_package.gsub(/\./, "/"), class_path, "#{class_name}Editor.ui.xml")
      end

      def create_place_files
        template 'Place.java', File.join(java_root, places_package.gsub(/\./, "/"), class_path, "#{class_name}Place.java")
        template 'PlaceTokenizer.java', File.join(java_root, places_package.gsub(/\./, "/"), class_path, "#{class_name}PlaceTokenizer.java")
      end

      def create_activity_file
        template 'Activity.java', File.join(java_root, activities_package.gsub(/\./, "/"), class_path, "#{class_name}Activity.java")
      end

      def add_to_activity_factory
        factory_file = File.join(java_root, managed_package.gsub(/\./, "/"), class_path, "ActivityFactory.java")
        if File.exists?(factory_file)
          factory = File.read(factory_file)
          if factory =~ /@Named\(.#{table_name}.\)/
            log 'keep', factory_file
          else
            factory.sub! /interface\s+ActivityFactory\s+\{/, "interface ActivityFactory {\n  @Named(\"#{table_name}\") Activity create(#{places_package}.#{class_name}Place place);"
            File.open(factory_file, 'w') { |f| f.print factory }
            log "added to", factory_file
          end
        end
      end

      def add_to_place_histroy_mapper
        file = File.join(java_root, managed_package.gsub(/\./, "/"), class_path, "#{application_name}PlaceHistoryMapper.java")
        if File.exists?(file)
          content = File.read(file)
          if content =~ /#{class_name}PlaceTokenizer/
            log 'keep', file
          else
            content.sub! /public\s+#{application_name}PlaceHistoryMapper.(.*).\s*\{/ do |m|
              "public #{application_name}PlaceHistoryMapper(#{$1}){\n        register(\"#{table_name}\", new #{places_package}.#{class_name}PlaceTokenizer());"
end
            File.open(file, 'w') { |f| f.print content }
            log "added to", file
          end
        end
      end

      def add_to_menu_panel
        file = File.join(java_root, managed_package.gsub(/\./, "/"), class_path, "#{application_name}MenuPanel.java")
        if File.exists?(file)
          content = File.read(file)
          if content =~ /#{class_name}Place\(RestfulActionEnum/
            log 'keep', file
          else
            # TODO non session case !!!
            t_name = options[:singleton] ? singular_table_name : table_name
            content.sub! /super\(\s*sessionManager\s*\)\s*;/, "super(sessionManager);\n        addButton(\"#{t_name.underscore.humanize}\", new #{places_package}.#{class_name}Place(RestfulActionEnum.#{options[:singleton] ? 'SHOW' : 'INDEX'}));\n            }\n        });"
            content.sub! /super\(\s*\)\s*;/, "super();\n        addButton(\"#{t_name.underscore.humanize}\", new #{places_package}.#{class_name}Place(RestfulActionEnum.#{options[:singleton] ? 'SHOW' : 'INDEX'}));\n            }\n        });"
            File.open(file, 'w') { |f| f.print content }
            log "added to", file
          end
        end
      end

      def add_to_module
        file = File.join(java_root, managed_package.gsub(/\./, "/"), class_path, "#{application_name}Module.java")
        if File.exists?(file)
          content = File.read(file)
          if content =~ /#{class_name.pluralize}RestService.class/
            log 'keep', file
          else content =~ /super.configure\(\);/
            content.sub! /super.configure\(\);/, "super.configure();\n        bind(#{restservices_package}.#{class_name.pluralize}RestService.class).toProvider(#{class_name.pluralize}RestServiceProvider.class);"

            content.sub! /new GinFactoryModuleBuilder\(\)/, "new GinFactoryModuleBuilder()\n            .implement(Activity.class, Names.named(\"#{table_name}\"), #{activities_package}.#{class_name}Activity.class)"

            content.sub! /^}/, <<-EOF

    @Singleton
    public static class #{class_name.pluralize}RestServiceProvider implements Provider<#{restservices_package}.#{class_name.pluralize}RestService> {
        private final #{restservices_package}.#{class_name.pluralize}RestService service = GWT.create(#{restservices_package}.#{class_name.pluralize}RestService.class);
        public #{restservices_package}.#{class_name.pluralize}RestService get() {
            return service;
        }
    }
}
EOF
            File.open(file, 'w') { |f| f.print content }
            log "added to", file
          end
        end
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
