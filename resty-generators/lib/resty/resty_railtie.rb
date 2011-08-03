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
    end

    config.after_initialize do
      # get the time/date format right ;-) and match it with resty
      class DateTime
        def as_json(options = nil)
          strftime('%Y-%m-%dT%H:%M:%S.') + ("%06d" % usec) + strftime('%z')
        end
      end
      class ActiveSupport::TimeWithZone
        def as_json(options = nil)
          strftime('%Y-%m-%dT%H:%M:%S.') + ("%06d" % usec) + strftime('%z')
        end
      end
      class Time
        def as_json(options = nil)
          strftime('%Y-%m-%dT%H:%M:%S.') + ("%06d" % usec) + strftime('%z')
        end
      end
    end
  end
end
