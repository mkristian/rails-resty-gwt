require 'rails_gwt/user_config'

Capybara.default_driver = :selenium
Capybara.automatic_reload = false
Capybara.default_wait_time = 10
#Capybara.register_driver :selenium do |app|
#  Capybara::Selenium::Driver.new(app, :resynchronize => true)
#end

#sqlite3 can have only one connection at the time
class ActiveRecord::Base
  mattr_accessor :shared_connection
  @@shared_connection = nil

  def self.connection
    @@shared_connection || retrieve_connection
  end
end
ActiveRecord::Base.shared_connection = ActiveRecord::Base.connection

module RailsGwt
  module DSL
    include Capybara::DSL

    def login_session(username, use_last_pwd = false)
      puts "login user: '#{username}'"
      if use_last_pwd
        line = File.read('log/test.log').gsub(/\r/, '').split("\n").grep(/new password :/).last
        pwd = line.gsub(/.*new password : (.*)/ , '\1') if line
      end
      if pwd.blank?
        fill_in 'username', :with => username
        click_on('password reset')
        page.should have_content('new password was sent to your email address')

        pwd = File.read('log/test.log').gsub(/\r/, '').split("\n").grep(/new password :/).last.gsub(/.*new password : (.*)/ , '\1')
      end
      fill_in 'login', :with => username
      fill_in 'password', :with => pwd
      click_on('login')

      should_have_no_loading

      yield

      click_on('logout')
      page.should have_content('password reset')
    end

    def should_have_no_loading
      page.should have_xpath('//div[@class = "gwt-rails-loading" and @style = "display: none;"]')
    end

    def dump_page
      puts page.html.gsub(/></, ">\n<")
    end

    def should_have_button(name)
      find(:xpath, "//button[text()='#{name}' and not(@style='display: none;')]")
    end

    def should_have_no_button(name)
      find(:xpath, "//button[text()='#{name}' and @style='display: none;']")
    end

    def should_have_menu_items(*items)
      found = []
      all(:xpath, "//div[@class='gwt-rails-menu']/button").each do |b|
        found << b.text if b[:style].blank?
      end
      found.sort!.should == items.flatten.sort
    end

    def should_have_no_model_display
      find(:xpath, "//div[@class='gwt-rails-model' and @style='display: none;']")
    end

    def should_have_model_list_display
      find(:xpath, "//div[@class='gwt-rails-model-list' and not(table/@style='display: none;')]")
    end

    def should_have_model_display
      find(:xpath, "//div[@class='gwt-rails-model' and not(@style='display: none;')]")
    end

    def should_have_no_model_list_display
      find(:xpath, "//div[@class='gwt-rails-model-list' and table/@style='display: none;']")
    end

    def should_have_action_button(name)
      find(:xpath, "//div[@class='gwt-rails-model']//div[@class='gwt-rails-buttons']/button[text()='#{name}' and not(@style='display: none;')]")
    end

    def should_have_no_action_button(name)
      find(:xpath, "//div[@class='gwt-rails-buttons']/button[text()='#{name}' and @style='display: none;']")
    end

    def within_page(path, mode = :page, &block)
      puts "visit page: #{path}"
      visit "#{path}"
      should_have_no_loading
      
      case mode
      when :index
        should_have_no_model_display
        should_have_model_list_display
      when :page
        should_have_model_display
        should_have_no_model_list_display
      when :singleton
        should_have_model_display
      end

      block.call if block
    end

    def should_have_no_action_buttons
      should_have_action_buttons
    end

    def should_have_action_buttons(*buttons)
      buttons = buttons.flatten
      found = []
      all(:xpath, "//div[@class='gwt-rails-model']//div[@class='gwt-rails-buttons']/button").each do |b|
        found << b.text unless b.text.blank?
      end
      (found || bottons).each do |button|
        if buttons.member? button
          should_have_action_button(button) unless button.blank?
        else
          should_have_no_action_button(button) unless button.blank?
        end
      end 
    end

    def should_have_no_buttons
      should_have_buttons
    end

    def should_have_buttons(*buttons)
      buttons = buttons.flatten
      found = []
      all(:xpath, "//div[contains(@class, 'gwt-rails-display')]/div[@class='gwt-rails-buttons']/button").each do |b|
        text = b.text
        found << text unless text.blank?
      end
      (found || buttons).each do |button|
        if buttons.member? button
          should_have_button(button)
        else
          should_have_no_button(button)
        end
      end
    end

    def visit_resource(config, &block)
      puts "----- resource: '#{config.resource}'"
      page.should have_content("Welcome #{config.name}")
      page.should have_content('logout')

      should_have_menu_items(config.menu)
      
      config.action = :new
      if config.action
        within_page("/Users.html##{config.resource}/new", config.mode) do
          should_have_action_buttons(config.action_buttons)
          should_have_buttons(config.buttons)
          write_page(config.action)
        end
      end

      config.action = :edit
      if config.action
        id = config.mode == :singleton ? '' : "/#{config.resource_id}"
        within_page("/Users.html##{config.resource}#{id}/edit", config.mode) do
          should_have_action_buttons(config.action_buttons)
          should_have_buttons(config.buttons)
          write_page(config.action)
        end
      end
      
      config.action = :show
      if config.action      
        id = config.mode == :singleton ? '' : "/#{config.resource_id}"
        within_page("/Users.html##{config.resource}#{id}", config.mode) do
          should_have_action_buttons(config.action_buttons)
          should_have_buttons(config.buttons)
          write_page(config.action)
        end
      end

      config.action = :index
      if config.action
        within_page("/Users.html##{config.resource}", :index) do
          should_have_action_buttons(config.action_buttons)
          should_have_buttons(config.buttons)
          write_page(config.action)
        end

        # list
        config.content.each do |item|
          page.should have_content(item)
        end
        
        # no user list after search
        #      fill_in 'search', :with => 'pipapo'
        #      page.should have_no_content('root@example.com')
        
      end

      block.call(config) if block
    end

    def write_page(name)
      if defined? DIR
        File.open("#{DIR}/#{name}.html", 'w') do |f|
          f.puts page.html
        end
      end
    end
  end
end
