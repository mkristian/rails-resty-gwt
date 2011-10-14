class Authentication < ActiveResource::Base
  self.site = Rails.application.config.respond_to?(:remote_sso_url) ? Rails.application.config.remote_sso_url : "http://localhost:3000"
  self.headers['X-SERVICE-TOKEN'] = Rails.application.config.respond_to?(:remote_sso_token) ? Rails.application.config.remote_sso_token : 'be happy'
end
