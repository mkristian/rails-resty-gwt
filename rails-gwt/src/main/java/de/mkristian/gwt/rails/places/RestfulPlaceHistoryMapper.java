/**
 *
 */
package de.mkristian.gwt.rails.places;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import static de.mkristian.gwt.rails.places.RestfulPlaceTokenizer.SEPARATOR;
import static de.mkristian.gwt.rails.places.QueryableRestfulPlaceTokenizer.QUERY_SEPARATOR;

public class RestfulPlaceHistoryMapper implements PlaceHistoryMapper {

    private final Map<String, RestfulPlaceTokenizer<?>> map = new HashMap<String, RestfulPlaceTokenizer<?>>();

    protected void register(String key, RestfulPlaceTokenizer<?> tokenizer) {
        this.map.put(key, tokenizer);
    }

    public Place getPlace(String token) {
        // name => ""
        // name/ => ""
        // name$query => $query
        // name/something => "something"
        // name/something$query => something$query
        final String key;
        final String subtoken;
        int queryStart = token.indexOf(QUERY_SEPARATOR);
        if(!token.contains(SEPARATOR)){
            if(queryStart > 0){
                key = token.substring(0, queryStart);
            }
            else {
                key = token;
            }
            subtoken = token.substring(key.length());
        }
        else {
            key = token.substring(0, token.indexOf(SEPARATOR));
            subtoken = token.substring(key.length() + 1);
        }
        RestfulPlaceTokenizer<?> tokenizer = this.map.get(key);
        if (tokenizer == null) {
            return null;
        } else {
            return tokenizer.getPlace(subtoken);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getToken(Place place) {
        RestfulPlaceTokenizer tokenizer = this.map
                .get(((RestfulPlace) place).resourceName);
        if (tokenizer == null) {
            return null;
        } else {
            return tokenizer.getToken((RestfulPlace) place);
        }
    }
}