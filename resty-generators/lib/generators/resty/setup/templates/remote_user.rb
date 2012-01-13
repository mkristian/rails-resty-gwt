class RemoteUser < ActiveResource::Base
  self.site = Rails.application.config.respond_to?(:remote_service_url) ? Rails.application.config.remote_service_url : "http://localhost:3000"
  self.element_name = "user"
  self.headers['X-SERVICE-TOKEN'] =  Rails.application.config.respond_to?(:remote_service_token) ? Rails.application.config.remote_service_token : 'be happy'
end
