require 'generators/resty/base'
module Resty
  module Generators
    class SetupGenerator < Base

      source_root File.expand_path('../templates', __FILE__)

      arguments.clear # clear name argument from NamedBase
      
      argument :gwt_module_name, :type => :string, :required => true

      class_option :session, :type => :boolean, :default => false
     
      class_option :menu, :type => :boolean, :default => false
     
      def name
        gwt_module_name
      end

      def create_module_file
        template 'module.gwt.xml', File.join(java_root, name.gsub(/\./, "/"), "#{application_name}.gwt.xml")
      end

      def create_maven_file
        template 'Mavenfile', "Mavenfile"
        unless File.read(".gitignore") =~ /^target/
          File.open(".gitignore", "a") { |f| f.puts "target/" }
        end
        unless File.read(".gitignore") =~ /^*pom/
          File.open(".gitignore", "a") { |f| f.puts "*pom" }
        end
      end

      def create_entry_point_file
        template 'EntryPoint.java', File.join(java_root, base_package.gsub(/\./, "/"), "#{application_name}EntryPoint.java")
      end

      def create_managed_files
        path = managed_package.gsub(/\./, "/")
        template 'PlaceHistoryMapper.java', 
                        File.join(java_root, path, 
                                  "#{application_name}PlaceHistoryMapper.java")
        template 'GinModule.java', 
                        File.join(java_root, path, 
                                  "#{application_name}Module.java")
        template 'ActivityFactory.java', 
                        File.join(java_root, path, 
                                  "ActivityFactory.java")
        if options[:menu]
          template 'MenuPanel.java', 
                        File.join(java_root, path, 
                                  "#{application_name}MenuPanel.java")
        end
      end

      def create_scaffolded_files
        path = base_package.gsub(/\./, "/")
        #template 'ActivityPlace.java', 
        #                File.join(java_root, path, 
        #                          "ActivityPlace.java")
        template 'ActivityPlaceActivityMapper.java', 
                        File.join(java_root, path, 
                                  "ActivityPlaceActivityMapper.java")
        if options[:session]
          template 'SessionActivityPlaceActivityMapper.java', 
                        File.join(java_root, path, 
                                  "SessionActivityPlaceActivityMapper.java")
          template 'BreadCrumbsPanel.java', 
                        File.join(java_root, path, 
                                  "BreadCrumbsPanel.java")          
        end
      end

      def create_session_files
        if options[:session]
          template 'LoginActivity.java',
                        File.join(java_root, activities_package.gsub(/\./, "/"),
                                  "LoginActivity.java")
          template 'User.java',
                        File.join(java_root, models_package.gsub(/\./, "/"),
                                  "User.java")
          template 'LoginPlace.java',
                        File.join(java_root, places_package.gsub(/\./, "/"),
                                  "LoginPlace.java")
          template 'SessionRestService.java',
                        File.join(java_root, restservices_package.gsub(/\./, "/"),
                                  "SessionRestService.java")
          template 'LoginViewImpl.java',
                        File.join(java_root, views_package.gsub(/\./, "/"),
                                  "LoginViewImpl.java")
          template 'LoginView.ui.xml',
                        File.join(java_root, views_package.gsub(/\./, "/"),
                                  "LoginView.ui.xml")
        end
      end

      def create_html
        template 'page.html', File.join('public', "#{application_name}.html")
        template 'gwt.css', File.join('public', 'stylesheets', "#{application_name.underscore}.css")
      end

      def create_web_xml
        template 'web.xml', File.join('public', 'WEB-INF', 'web.xml')
        template 'gitignore', File.join('public', 'WEB-INF', '.gitignore')
      end

      def add_gems
        gem 'ixtlan-core'
      end

      def create_rails_session_files
        if options[:session]
          template 'sessions_controller.rb', File.join('app', 'controllers', "sessions_controller.rb")
          file = File.join('config', 'environments', "development.rb")
          development = File.read(file)
          changed = false
          unless development =~ /config.remote_sso_url/
            changed = true
            development.sub! /^end\s*$/, <<ENV

  if ENV['SSO'] == 'true' || ENV['SSO'] == ''
    config.remote_sso_url = "http://localhost:3000"
  end
end
ENV
          end
          if changed
            File.open(file, 'w') { |f| f.print development }
            log "changed", file
          else
            log "unchanged", file
          end
          file = File.join('app', 'controllers', "application_controller.rb")
          app_controller = File.read(file)
          changed = false
          unless app_controller =~ /def\s+current_user/
            changed = true
            app_controller.sub! /^end\s*$/, <<SESSION

  protected

  def current_user(user = nil)
    session['user'] = user if user
    session['user']
  end
end
SESSION
          end
          unless app_controller =~ /def\s+csrf/
            changed = true
            app_controller.sub! /^end\s*$/, <<SESSION

  private

  after_filter :csrf

  def csrf
    response.header['X-CSRF-Token'] = form_authenticity_token if current_user
  end
end
SESSION
          end
          if changed
            File.open(file, 'w') { |f| f.print app_controller }
            log "changed", file
          else
            log "unchanged", file
          end
          template 'authentication.rb', File.join('app', 'models', "authentication.rb")
          template 'group.rb', File.join('app', 'models', "group.rb")          
          template 'session.rb', File.join('app', 'models', "session.rb")
          template 'user.rb', File.join('app', 'models', "user.rb")
          route <<ROUTE
resource :session do
    member do
      post :reset_password
    end
  end
ROUTE
          gem 'ixtlan-session-timeout'
          gem 'ixtlan-guard'
          # needs to be in Gemfile to have jruby find the gem
          gem "jruby-openssl", "~> 0.7.4", :platforms => :jruby
        end
      end
      
      def base_package
        name + ".client"
      end

    end
  end
end
