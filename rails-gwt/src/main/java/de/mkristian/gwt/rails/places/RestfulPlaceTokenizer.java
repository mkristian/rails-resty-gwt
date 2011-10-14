/**
 *
 */
package de.mkristian.gwt.rails.places;

import com.google.gwt.place.shared.PlaceTokenizer;

public abstract class RestfulPlaceTokenizer<P extends RestfulPlace<?>> implements PlaceTokenizer<P> {

    protected static class Token {
        public final int id;
        public final String identifier;
        public final RestfulAction action;
        Token(RestfulAction action){
            this(null, action);
        }

        Token(String identifier, RestfulAction action){
            if(identifier == null){
                this.identifier = null;
                this.id = 0;
            }
            else {
                int id;
                try {
                    id = Integer.parseInt(identifier);
                }
                catch(NumberFormatException e){
                    id = 0;
                }
                this.identifier = id == 0? null : "" + id;
                this.id = id;
            }
            this.action = action;
        }
    }

    protected static final String SEPARATOR = "/";

    protected RestfulAction toRestfulAction(String action){
        return RestfulActionEnum.toRestfulAction(action);
    }

    protected Token toSingletonToken(String token){
        if(token.endsWith("/")){
            token = token.substring(0, token.length() - 1);
        }
        RestfulAction action = toRestfulAction(token);
        if(action == null){
            action = RestfulActionEnum.SHOW;
        }
        return new Token(action);
    }

    protected Token toToken(String token){
        if(token.endsWith("/")){
            token = token.substring(0, token.length() - 1);
        }
        String[] parts = token.split(SEPARATOR);
        switch(parts.length){
            case 2:
                RestfulAction action = toRestfulAction(parts[1]);
                if (action != null) {
                    return new Token(parts[0], action);
                }
                else {
                    token = parts[0];
                }
            case 1:
                if(RestfulActionEnum.NEW.token().equals(token)){
                    return new Token(toRestfulAction(token));
                }
                else {
                    if( token.length() == 0){
                        return new Token(RestfulActionEnum.INDEX);
                    }
                    else{
                        return new Token(token, RestfulActionEnum.SHOW);
                    }
                }
            default:
                throw new RuntimeException("unknown token: " + token);
        }

    }

    public String getTokenWithoutName(P place){
        if(place.id != 0){
            if(place.action.token().length() > 0){
                return place.id + SEPARATOR + place.action.token();
            }
            else {
                return place.id + "";
            }
        }
        else {
            return place.action.token();
        }
    }

    public String getToken(P place){
        return place.resourceName + SEPARATOR + getTokenWithoutName(place);
    }

}