require 'rails'

# TODO decide where to put it here or in after_initialize
# DateTime and Time do not work - no usec
class Date
  def as_json(options = nil)
    strftime('%Y-%m-%dT0:0:0.000')
  end
end
# class DateTime
#   def as_json(options = nil)
#     strftime('%Y-%m-%dT%H:%M:%S.') + ("%06d" % usec) + strftime('%z')
#   end
# end
class ActiveSupport::TimeWithZone
  def as_json(options = nil)
    strftime('%Y-%m-%dT%H:%M:%S.') + ("%06d" % usec) + strftime('%z')
  end
end
# class Time
#   def as_json(options = nil)
#     strftime('%Y-%m-%dT%H:%M:%S.') + ("%06d" % usec) + strftime('%z')
#   end
# end

module Resty
  class RestyRailtie < Rails::Railtie

    gmethod = config.respond_to?(:generators) ? :generators : :app_generators
    config.send(gmethod) do
      require 'rails/generators'      
      require 'rails/generators/rails/controller/controller_generator'
      #require 'rails/generators/erb/scaffold/scaffold_generator'
      Rails::Generators::ControllerGenerator.hook_for :resty, :type => :boolean, :default => true do |controller|
        invoke controller, [ class_name, actions ]
      end
    end

    config.after_initialize do
      # get the time/date format right ;-) and match it with resty
      class Date
        def as_json(options = nil)
          strftime('%Y-%m-%dT0:0:0.000%z')
        end
      end
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
