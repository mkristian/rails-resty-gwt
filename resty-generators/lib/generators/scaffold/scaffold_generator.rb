# use this extended copy of Rails::Generators::ScaffoldGenerator
# since the search path is the following:
# ["scaffold:scaffold", "rails:scaffold", "scaffold"]
# which allows to override orignal generator
# adding a  Rails::Generators::ScaffoldGenerator.hook_for did not work
# since it looses the ORM config somehow
require  'rails/generators/rails/resource/resource_generator'
module Rails
  module Generators
    class ScaffoldGenerator < ResourceGenerator #metagenerator

      remove_hook_for :resource_controller
      remove_class_option :actions
    
      hook_for :scaffold_controller, :required => true, :in => :rails
      hook_for :stylesheets, :in => :rails

      hook_for :resty, :type => :boolean, :default => true do |controller|
        invoke controller, [class_name]
      end
    end
  end
end
