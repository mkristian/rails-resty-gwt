/**
 * 
 */
package de.mkristian.gwt.rails;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum RestfulActionEnum implements RestfulAction{
    NEW("new", false), SHOW("", true), EDIT("edit", false), INDEX("", true), DESTROY("", false);
    
    private final String token;
    
    private final boolean viewOnly;
    
    static Map<String, RestfulActionEnum> map = new HashMap<String, RestfulActionEnum>();
    static {
        for(RestfulActionEnum s : EnumSet.allOf(RestfulActionEnum.class)) {
             map.put(s.name(), s);
        }
    }
    
    private RestfulActionEnum(String token, boolean viewOnly){
        this.token = token;
        this.viewOnly = viewOnly;
    }
    
    public static RestfulAction toRestfulAction(String name){
        RestfulActionEnum action = map.get(name.toUpperCase());
        if (action == null){
            return SHOW;
        }
        else {
            return action;
        }
    }
    
    public String token(){
        return token;
    }

    public boolean viewOnly() {
        return viewOnly;
    }
}