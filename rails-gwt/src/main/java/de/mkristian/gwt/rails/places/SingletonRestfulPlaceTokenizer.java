package de.mkristian.gwt.rails.places;


public abstract class SingletonRestfulPlaceTokenizer<P extends RestfulPlace<?, ?>> extends RestfulPlaceTokenizer<P> {

    public P getPlace(String token) {
        return newRestfulPlace( toSingletonToken(token).action );
    }

    protected RestfulPlaceTokenizer.Token toSingletonToken(String token){
        if(token.endsWith(SEPARATOR)){
            token = token.substring(0, token.length() - 1);
        }
        RestfulAction action = toRestfulAction(token);
        if(action == null){
            action = RestfulActionEnum.SHOW;
        }
        return new Token(action);
    }

    @Override
    protected P newRestfulPlace(int id, RestfulAction action) {
        return null;
    }
}