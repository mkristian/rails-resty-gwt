require 'rails'

module Resty
  class RestyRailtie < Rails::Railtie

    config.generators do
      require 'rails/generators'      
      require 'rails/generators/rails/controller/controller_generator'
      #require 'rails/generators/erb/scaffold/scaffold_generator'
      Rails::Generators::ControllerGenerator.hook_for :resty, :type => :boolean, :default => true do |controller|
        invoke controller, [ class_name, actions ]
      end
      #Erb::Generators::ScaffoldGenerator.source_paths.insert(0, File.expand_path('../../generators/ixtlan/templates', __FILE__))
    end
  end
end
