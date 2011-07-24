require 'rails'

module Resty
  
  module TimeFormat

    def as_json(options = nil)
      strftime('%Y-%m-%dT%H:%M:%S.') + ("%06d" % usec) + strftime('%z')
    end
    
    def as_html(options = nil)
      strftime('%Y-%m-%d %H:%M:%S.') + ("%06d" % usec)
    end

  end

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
      ActiveRecord::Base.include_root_in_json = false
      # TODO there migt be a way to tell ALL ActiveModel:
      #ActiveModel::Base.include_root_in_json = false

      # get the time/date format right ;-) and match it with resty
      class DateTime
        include TimeFormat
      end
      class ActiveSupport::TimeWithZone
        include TimeFormat
      end
      class Date
        include TimeFormat
      end
      class Time
        include TimeFormat
      end
    end
  end
end
