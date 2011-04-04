require 'resty/child_path'

Rails.application.config.middleware.use Resty::ChildPath, '<%= application_name.underscore %>'

ActiveRecord::Base.include_root_in_json = false

# get the time/date format right ;-)
class DateTime
  def as_json(options = nil)
    strftime('%Y-%m-%dT%H:%M:%S.%s%z')
  end
end
class ActiveSupport::TimeWithZone
  def as_json(options = nil)
    strftime('%Y-%m-%dT%H:%M:%S.%s%z')
  end
end
class Date
  def as_json(options = nil)
    strftime('%Y-%m-%dT%H:%M:%S.%s%z')
  end
end

class Time
  def as_json(options = nil)
    strftime('%Y-%m-%dT%H:%M:%S.%s%z')
  end
end
