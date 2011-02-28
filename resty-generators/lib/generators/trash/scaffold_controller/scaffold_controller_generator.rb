require 'rails/generators/resource_helpers'
require 'rails/generators/resource_helpers'

module Resty
  module Generators
    class ScaffoldControllerGenerator < ::Rails::Generators::NamedBase
      include ::Rails::Generators::ResourceHelpers

      source_root File.expand_path('../../templates..............', __FILE__)
  
      check_class_collision :suffix => "Controller"

      class_option :orm, :banner => "NAME", :type => :string, :required => true,
                         :desc => "ORM to generate the controller for"
      
#      def create_controller_files
#        template 'controller.rb', File.join('app/controllers', class_path, "#{controller_file_name}_controller.rb")
#      end

      hook_for :template_engine, :test_framework, :as => :scaffold

      # Invoke the helper using the controller name (pluralized)
      hook_for :helper, :as => :scaffold, :in => :rails do |invoked|
        invoke invoked, [ controller_name ]
      end
    end
  end
end
