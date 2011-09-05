package de.mkristian.gwt.rails.session;

import java.util.Set;

import org.fusesource.restygwt.client.Json;
import org.fusesource.restygwt.client.Json.Style;

@Json(style = Style.RAILS)
public class Permission {
    public String resource;
    public boolean deny;
    public Set<Action> actions;
}
