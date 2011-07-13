/**
 * 
 */
package de.mkristian.gwt.rails;



public abstract class RestfulPlaceTokenizer<P extends RestfulPlace> {
    
    protected static class Token {
        public final String identifier;
        public final RestfulAction action;
        Token(RestfulAction action){
            this(null, action);
        }

        Token(String identifier, RestfulAction action){
            this.identifier = identifier;
            this.action = action;
        }
    }
    
    private static final String SEPARATOR = "/";
    
    protected RestfulAction toRestfulAction(String action){
        return RestfulActionEnum.toRestfulAction(action);
    }
    
    protected Token toSingletonToken(String token){
        if(token.endsWith("/")){
            token = token.substring(0, token.length() - 1);
        }
        return new Token(toRestfulAction(token));
    }

    protected Token toToken(String token){
        if(token.endsWith("/")){
            token = token.substring(0, token.length() - 1);
        }
        String[] parts = token.split(SEPARATOR);
        switch(parts.length){
            case 2:
                return new Token(parts[0], toRestfulAction(parts[1]));
            case 1:
                if(RestfulActionEnum.NEW.token().equals(token)){
                    return new Token(toRestfulAction(token));
                }
                else {
                    if( token.length() == 0){
                        return new Token(RestfulActionEnum.LIST);
                    }
                    else{
                        return new Token(token, RestfulActionEnum.SHOW);
                    }
                }
            default: 
                throw new RuntimeException("unknown token: " + token);
        }
        
    }
    
    public String getToken(P place){
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
}