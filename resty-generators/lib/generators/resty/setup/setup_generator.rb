require 'generators/resty/base'
module Resty
  module Generators
    class SetupGenerator < Base

      source_root File.expand_path('../templates', __FILE__)

      arguments.clear # clear name argument from NamedBase
      
      argument :gwt_module_name, :type => :string, :required => true

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
        template 'EntryPoint.java', File.join(java_root, base_package.gsub(/\./, "/"), "#{application_name}.java")
      end

      def create_initializers
        template 'initializer.rb', File.join('config', 'initializers', 'resty.rb')
        template 'monkey_patch.rb', File.join('config', 'initializers', 'resty_monkey_patch.rb')
      end

      def create_html
        template 'page.html', File.join('public', "#{application_name.underscore}.html")
      end

      def base_package
        name + ".client"
      end
    end
  end
end
