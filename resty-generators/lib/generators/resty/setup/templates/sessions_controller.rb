class SessionsController < ApplicationController

  skip_before_filter :authorization

  prepend_after_filter :reset_session, :only => :destroy

  public

  def create
    @session = Session.create(params[:session])

    if @session
      current_user @session.user
      @session.permissions = guard.permissions(self)

      # TODO make all formats available
      # TODO make html login
      render :json => @session.to_json(:excludes => :groups)
    else
      head :not_found
    end
  end

  def destroy
    # reset session happens in the after filter which allows for 
    # audit log with username which happens in another after filter
    head :ok
  end
end
