package <%= places_package %>;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import <%= gwt_rails_package %>.RestfulPlaceTokenizer;

@Prefix("<% if options[:singleton] -%><%= singular_table_name %><% else -%><%= table_name %><% end -%>") 
public class <%= class_name %>PlaceTokenizer extends RestfulPlaceTokenizer<<%= class_name %>Place> 
    implements PlaceTokenizer<<%= class_name %>Place> {
    
    public <%= class_name %>Place getPlace(String token) {
<% if options[:singleton] -%>
	return new <%= class_name %>Place(toSingletonToken(token).action);
<% else -%>
        Token t = toToken(token);
        if(t.identifier == null){
            return new <%= class_name %>Place(t.action);
        }
        else {
            return new <%= class_name %>Place(t.identifier, t.action);
        }
<% end -%>
    }
}
