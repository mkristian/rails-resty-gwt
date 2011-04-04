require 'action_dispatch/http/request'
require 'active_support/core_ext/hash/indifferent_access'

module ActionDispatch
  class ParamsParser

    alias :call_old :call
    def call(env)
      request = Request.new(env)
      mime_type = content_type_from_legacy_post_data_format_header(env) ||
          request.content_mime_type

      case mime_type
      when Mime::JSON
        data = ActiveSupport::JSON.decode(request.body)
        request.body.rewind if request.body.respond_to?(:rewind)
        data = {:_json => data} unless data.is_a?(Hash)
        env["action_dispatch.request.request_parameters"] = {:json => data}.with_indifferent_access
        
        @app.call(env)
      else
        call_old(env)
      end
    end
  end
end

module ActionController
  class Base
    def from_java(map)
      result = map.dup
      map.each do |k,v|
        result.delete(k)
        result[k.underscore] = v.is_a?(Hash) ? from_java(v) : v
      end
      result
    end
  end
end

::ActionController::Base.send(:before_filter) do
  json = params.delete(:json)
  if json
    params[params[:controller].singularize] = from_java(json)
  end
  true
end
