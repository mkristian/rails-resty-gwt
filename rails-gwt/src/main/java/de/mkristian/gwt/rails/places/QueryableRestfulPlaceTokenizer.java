package de.mkristian.gwt.rails.places;


public abstract class QueryableRestfulPlaceTokenizer<P extends QueryableRestfulPlace<?,?>> 
                            extends RestfulPlaceTokenizer<P> {
    
    public static final String QUERY_SEPARATOR = "$";
    
    public P getPlace(String token) {
        if(token.contains(QUERY_SEPARATOR)){
            return newRestfulPlace(token.substring(token.indexOf(QUERY_SEPARATOR) + 1).replaceFirst("/$", ""));
        }
        Token t = toToken(token);
        if(t.identifier == null){
            return newRestfulPlace(t.action);
        }
        else {
            return newRestfulPlace(t.id, t.action);
        }
    }

    
    abstract protected P newRestfulPlace(String query); 
    
    @Override
    public String getToken(P place) {
        String token = super.getToken(place);
        if (place.query != null) {
            return token + QUERY_SEPARATOR + place.query;
        }
        else {
            return token;
        }
    }
    
}