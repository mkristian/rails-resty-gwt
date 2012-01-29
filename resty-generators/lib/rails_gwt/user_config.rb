module RailsGwt
  class UserConfigFactory

    def [](login)
      UserConfig.new(login.to_sym, config[login.to_sym])
    end

    def add(login, user_config)
      config[login.to_sym] = user_config
    end

    def logins
      config.keys
    end

    private

    def config
      @config ||= {}
    end
  end

  class UserConfig

    attr_reader :login, :name, :menu, :resource, :action

    def initialize(login, config)
      @user_config = config
      raise "unknown login #{login}" unless @user_config
      @login = login.to_s
      @name = @user_config[:name]
      @menu = @user_config[:menu]
    end
    
    def resources
      @user_config.collect { |k,v| k.to_s if v.is_a? Hash }.delete_if { |i| i.nil? }.sort!.collect { |i| i.to_sym }
    end

    def resource=(resource)
      @resource = resource.to_s.pluralize
      @config = @user_config[resource]
    end

    def content
      c = @config[@action][:content] || []
      c = [c] unless c.is_a? Array
      c
    end

    def buttons
      c = @config[@action][:buttons] || []
      c = [c] unless c.is_a? Array
      c
    end

    def action_buttons
      c = @config[@action][:action_buttons] || []
      c = [c] unless c.is_a? Array
      c
    end

    def action=(action)
      @action = @config[action] ? action : nil
      @action
    end

    def mode
      @config[:mode] || :page
    end

    def resource_id
      if id = @config[:resource_id]
        self.class_eval(id)
      else
        1
      end
    end

  end
end
