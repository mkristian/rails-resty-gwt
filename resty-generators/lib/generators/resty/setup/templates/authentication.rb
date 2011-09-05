class Authentication < ActiveResource::Base
  self.site = Rails.application.config.remote_sso_url if Rails.application.config.respond_to? :remote_sso_url
end
