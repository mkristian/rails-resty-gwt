class SessionsController < ApplicationController

  skip_before_filter :authorization

  skip_before_filter :check_session, :only => :destroy

  prepend_after_filter :reset_session, :only => :destroy

  public

  def create
    auth = params[:authentication] || params
    method = Rails.application.config.respond_to?(:remote_sso_url) ? :create_remote : :create
    @session = Session.send(method, auth[:login] || auth[:email], 
                              auth[:password])
    
    if @session.valid?
      current_user(@session.user)
      @session.idle_session_timeout = Rails.application.config.idle_session_timeout
      @session.permissions = guard.permissions(groups_for_current_user)

      # TODO make html login
      respond_to do |format|
        format.html { render :text => "authorized - but nothing further is implemented" }
        format.xml  { render :xml => @session.to_xml }
        format.json  { render :json => @session.to_json }
      end
    else
      head :unauthorized
    end
  end

  def reset_password
    authentication = params[:authentication] || []
    user = User.reset_password(authentication[:email] || authentication[:login])

    if user

      # for the log
      @session = user
      
      head :ok
    else
      head :not_found
    end
  end

  def destroy
    # for the log
    @session = current_user

    # reset session happens in the after filter which allows for 
    # audit log with username which happens in another after filter
    head :ok
  end
end
