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
      class_option :timestamps, :type => :boolean, :default => true
      class_option :parent,     :type => :string, :desc => "The parent class for the generated model"
      class_option :singleton,  :type => :boolean

      def create_model_file
        template 'Model.java', File.join(java_root, models_package.gsub(/\./, "/"), class_path, "#{class_name}.java")
      end

      def create_rest_service_file
        template 'RestService.java', File.join(java_root, restservices_package.gsub(/\./, "/"), class_path, "#{controller_class_name}RestService.java")
      end

      def create_view_files
        template 'View.java', File.join(java_root, views_package.gsub(/\./, "/"), class_path, "#{class_name}View.java")
        template 'View.ui.xml', File.join(java_root, views_package.gsub(/\./, "/"), class_path, "#{class_name}View.ui.xml")
        template 'ViewImpl.java', File.join(java_root, views_package.gsub(/\./, "/"), class_path, "#{class_name}ViewImpl.java")
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
            factory.sub! /interface\s+ActivityFactory\s+{/, "interface ActivityFactory {\n  @Named(\"#{table_name}\") Activity create(#{places_package}.#{class_name}Place place);"
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
            content.sub! /#{application_name}PlaceHistoryMapper\(\)\s*{/, "#{application_name}PlaceHistoryMapper(){\n        register(\"#{options[:singleton] ? singular_table_name : table_name}\", new #{places_package}.#{class_name}PlaceTokenizer());"
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
            content.sub! /super\(\s*sessionManager\s*\)\s*;/, "super(sessionManager);\n        addButton(\"#{table_name.underscore.humanize}\").addClickHandler(new ClickHandler() {\n            public void onClick(ClickEvent event) {\n                placeController.goTo(new #{places_package}.#{class_name}Place(RestfulActionEnum.INDEX));\n            }\n        });"
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
