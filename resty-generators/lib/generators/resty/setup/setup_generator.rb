require 'generators/resty/base'
module Resty
  module Generators
    class SetupGenerator < Base

      source_root File.expand_path('../templates', __FILE__)

      arguments.clear # clear name argument from NamedBase
      
      argument :gwt_module_name, :type => :string, :required => true

      class_option :session, :type => :boolean, :default => false
     
      def name
        gwt_module_name
      end

      def create_module_file
        template 'module.gwt.xml', File.join(java_root, name.gsub(/\./, "/"), "#{application_name.underscore}.gwt.xml")
      end

      def create_maven_file
        template 'Mavenfile', File.join("Mavenfile")
      end

      def create_entry_point_file
        template 'EntryPoint.java', File.join(java_root, base_package.gsub(/\./, "/"), "#{application_name}EntryPoint.java")
      end

      def create_managed_files
        path = managed_package.gsub(/\./, "/")
        template 'PlaceHistoryMapper.java', 
                        File.join(java_root, path, 
                                  "#{application_name}PlaceHistoryMapper.java")
        template 'GinModule.java', 
                        File.join(java_root, path, 
                                  "#{application_name}Module.java")
        template 'ActivityFactory.java', 
                        File.join(java_root, path, 
                                  "ActivityFactory.java")
      end

      def create_scaffolded_files
        path = base_package.gsub(/\./, "/")
        template 'ActivityPlace.java', 
                        File.join(java_root, path, 
                                  "ActivityPlace.java")
        template 'ActivityPlaceActivityMapper.java', 
                        File.join(java_root, path, 
                                  "ActivityPlaceActivityMapper.java")
        if options[:session]
          template 'SessionActivityPlaceActivityMapper.java', 
                        File.join(java_root, path, 
                                  "SessionActivityPlaceActivityMapper.java")
          template 'BreadCrumbsPanel.java', 
                        File.join(java_root, path, 
                                  "BreadCrumbsPanel.java")          
        end
      end

      def create_session_files
        if options[:session]
          template 'LoginActivity.java',
                        File.join(java_root, activities_package.gsub(/\./, "/"),
                                  "LoginActivity.java")
          template 'User.java',
                        File.join(java_root, models_package.gsub(/\./, "/"),
                                  "User.java")
          template 'LoginPlace.java',
                        File.join(java_root, places_package.gsub(/\./, "/"),
                                  "LoginPlace.java")
          template 'SessionRestService.java',
                        File.join(java_root, restservices_package.gsub(/\./, "/"),
                                  "SessionRestService.java")
          template 'LoginViewImpl.java',
                        File.join(java_root, views_package.gsub(/\./, "/"),
                                  "LoginViewImpl.java")
          template 'LoginView.ui.xml',
                        File.join(java_root, views_package.gsub(/\./, "/"),
                                  "LoginView.ui.xml")
        end
      end

      def create_initializers
        template 'monkey_patch.rb', File.join('config', 'initializers', 'resty_monkey_patch.rb')
      end

      def create_html
        template 'page.html', File.join('public', "#{application_name.underscore}.html")
        template 'gwt.css', File.join('public', 'stylesheets', "#{application_name.underscore}.css")
      end

      def create_web_xml
        template 'web.xml', File.join('public', 'WEB-INF', 'web.xml')
      end

      def base_package
        name + ".client"
      end

    end
  end
end
