require 'rails/generators/resource_helpers'
require 'generators/resty/base'
module Resty
  module Generators
    class ModelGenerator < Base

      source_root File.expand_path('../../templates', __FILE__)
      
      argument :attributes, :type => :array, :default => [], :banner => "field:type field:type"

      if defined? ::Ixtlan::ModifiedBy
        class_option :modified_by, :type => :boolean
      end
      class_option :timestamps, :type => :boolean, :default => true
      class_option :parent,     :type => :string, :desc => "The parent class for the generated model"

      def create_model_file
        template 'Model.java', File.join(java_root, models_package.gsub(/\./, "/"), class_path, "#{class_name}.java")
      end

    end
  end
end
