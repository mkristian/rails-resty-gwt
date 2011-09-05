/**
 *
 */
package de.mkristian.gwt.rails.places;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;


public class RestfulPlaceHistoryMapper implements PlaceHistoryMapper {

    private static final String SEPARATOR = RestfulPlaceTokenizer.SEPARATOR;

    private final Map<String, RestfulPlaceTokenizer<?>> map = new HashMap<String, RestfulPlaceTokenizer<?>>();

    protected void register(String key, RestfulPlaceTokenizer<?> tokenizer) {
        this.map.put(key, tokenizer);
    }

    public Place getPlace(String token) {
        if (!token.endsWith(SEPARATOR)) {
            token += SEPARATOR;
        }
        int index = token.indexOf(SEPARATOR);
        RestfulPlaceTokenizer<?> tokenizer = this.map.get(token.substring(0,
                index));
        if (tokenizer == null) {
            return null;
        } else {
            return tokenizer.getPlace(token.substring(index + 1));
        }
    }

    @SuppressWarnings("unchecked")
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