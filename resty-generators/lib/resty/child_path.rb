module Resty
  class ChildPath
    def initialize(app, rootpath)
      @app = app
      @rootpath = rootpath
    end

    def call(env)
      is_json = (env['CONTENT_TYPE'] || env['HTTP_ACCEPT']) =~ /^application\/json/
      is_child = false
      ['REQUEST_PATH','PATH_INFO','REQUEST_URI','SCRIPT_NAME'].each do |key|
        if(is_json || env[key] =~ /\.json([?].*)?$/)
          is_json = true
          value = env[key]
          if value
            is_child = is_child || value =~ /^\/#{@rootpath}\//
            value.gsub!(/^\/#{@rootpath}\//, "/")
          end
        end
      end
      status, headers, response = @app.call(env)
      if is_child && headers['Location']
        uri = URI(headers['Location']) 
        uri.path.gsub!(/^\//, "/#{@rootpath}/")
        headers['Location'] = uri.to_s
      end
      headers['Content-Type'] = 'application/json' if is_json
      [status, headers, response]
    end
  end
end
