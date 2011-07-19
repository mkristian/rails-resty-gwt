package de.mkristian.gwt.rails.session;

public class Authentication {

    protected String login;
    
    protected String password;
    
    public Authentication(){
        this(null, null);
    }
    
    public Authentication(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
