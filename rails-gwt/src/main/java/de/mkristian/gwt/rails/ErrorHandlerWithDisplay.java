package de.mkristian.gwt.rails;

import com.google.gwt.user.client.ui.AcceptsOneWidget;


public class ErrorHandlerWithDisplay extends ErrorHandler{
    private AcceptsOneWidget display;

    public ErrorHandlerWithDisplay( Notice notice ) {
        super( notice );
    }
    
    public AcceptsOneWidget getDisplay(){
        return this.display;
    }
    
    public void setDisplay( AcceptsOneWidget display ){
        this.display = display;
    }
    
}