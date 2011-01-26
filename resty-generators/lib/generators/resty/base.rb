require 'rails/generators/resource_helpers'
module Resty
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
          raise "no gwt module found" unless fullpath
          fullpath.sub(/#{java_root}./, '').sub(/.gwt.xml$/, '').gsub(/[\/\\]/, '.') + ".client"
        end
    end

    def models_base_package
      @models_base_package ||= base_package + ".models"
    end

    def controllers_base_package
      @controllers_base_package ||= base_package + ".controllers"
    end

    def action_map
      @action_map ||= {'index' => :get_all, 'show' => :get_single, 'create' => :post, 'update' => :put, 'destroy' => :delete}
    end

    def type_map
      @type_map ||= {:integer => 'int', :boolean => 'bool', :string => 'String', :float => 'Double', :date => 'java.util.Date', :datetime => 'java.util.Date', :number => 'long', :fixnum => 'long'}
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
