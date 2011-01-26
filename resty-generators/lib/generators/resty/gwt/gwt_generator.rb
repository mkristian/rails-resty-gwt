require 'generators/resty/base'
module Resty
  class GwtGenerator < Base

    source_root File.expand_path('../../templates', __FILE__)

    arguments.clear # clear name argument from NamedBase
      
    def name # set alias so NamedBase uses the model as its name
      "gwt_module_name"
    end

    def create_module_file
      template 'module.gwt.xml', File.join(java_root, name.gsub(/\./, "/"), "#{application_name.underscore}.gwt.xml")
    end

    def create_maven_file
      template 'maven.rb', File.join("maven.rb")
    end

    def create_entry_point_file
      template 'entry_point.java', File.join(java_root, base_package.gsub(/\./, "/"), "#{application_name}.java")
    end

    def base_package
      name + ".client"
    end
  end
end
