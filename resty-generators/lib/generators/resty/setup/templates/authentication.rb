class Authentication < ActiveResource::Base
  self.site = Rails.application.config.remote_sso_url
end
