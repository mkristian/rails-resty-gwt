module Resty
  class ChildPath
    def initialize(app, rootpath)
      @app = app
      @rootpath = rootpath
    end

    def call(env)
      ['REQUEST_PATH','PATH_INFO','REQUEST_URI','SCRIPT_NAME'].each do |key|
        if(env[key] =~ /\.json([?].*)?$/)
          env[key].gsub!(/^\/#{@rootpath}\//, "/")
        end
      end
      @app.call(env)
    end

  end
end
