package <%= models_package %>;

import org.fusesource.restygwt.client.Json;
import org.fusesource.restygwt.client.Json.Style;

@Json(style = Style.RAILS)
public class User {

    public String login;

    public String name;
}