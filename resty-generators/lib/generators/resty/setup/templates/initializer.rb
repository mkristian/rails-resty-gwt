require 'resty/child_path'

Rails.application.config.middleware.use Resty::ChildPath, '<%= application_name.underscore %>'
