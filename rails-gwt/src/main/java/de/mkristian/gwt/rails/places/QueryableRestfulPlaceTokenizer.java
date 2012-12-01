package de.mkristian.gwt.rails.places;


public abstract class QueryableRestfulPlaceTokenizer<P extends QueryableRestfulPlace<?,?>> 
                            extends RestfulPlaceTokenizer<P> {
    
    public static final String QUERY_SEPARATOR = "$";
    
    public P getPlace(String token) {
        String query = null;
        int index = token.indexOf(QUERY_SEPARATOR);
        if(index > -1){
            query = token.substring(index + 1);
            token = token.substring(0, index);
        }
        Token t = toToken(token);
        if(t.identifier == null){
            return newRestfulPlace( t.action, query );
        }
        else {
            return newRestfulPlace( t.id, t.action, query );
        }
    }

    @Override
    protected P newRestfulPlace(RestfulAction action) {
        throw new RuntimeException( "bug" );
    }

    @Override
    protected P newRestfulPlace(int id, RestfulAction action) {
        throw new RuntimeException( "bug" );
    }

    abstract protected P newRestfulPlace( RestfulAction action, String query );
    
    abstract protected P newRestfulPlace( int id, RestfulAction action, String query ); 
    
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