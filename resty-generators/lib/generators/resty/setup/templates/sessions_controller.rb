class SessionsController < ApplicationController

  skip_before_filter :authorization

  prepend_after_filter :reset_session, :only => :destroy

  public

  def create
    @session = Session.create(params[:authentication] || params)

    if @session
      current_user @session.user
      @session.permissions = guard.permissions(self)

      # TODO make html login
      respond_to do |format|
        format.html { render :text => "authorized - but nothing further is implemented" }
        format.xml  { render :xml => @session.to_xml }
        format.json  { render :json => @session.to_json }
      end
    else
      head :not_found
    end
  end

  def reset_password
    warn "not implemented"
    head :ok
  end

  def destroy
    # reset session happens in the after filter which allows for 
    # audit log with username which happens in another after filter
    head :ok
  end
end
