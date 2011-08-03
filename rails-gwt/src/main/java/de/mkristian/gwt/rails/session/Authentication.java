package de.mkristian.gwt.rails.session;

import org.fusesource.restygwt.client.Json;
import org.fusesource.restygwt.client.Json.Style;

@Json(style = Style.RAILS)
public class Authentication {

    protected String login;
    
    protected String password;
    
    public Authentication(){
        this(null, null);
    }
    
    public Authentication(String login) {
        this(login, null);
    }

    public Authentication(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
