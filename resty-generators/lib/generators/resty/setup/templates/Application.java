package <%= models_package %>;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import org.fusesource.restygwt.client.Json;
import org.fusesource.restygwt.client.Json.Style;

@Json(style = Style.RAILS)
public class Application {

  private final String name;

  private final String url;

  @JsonCreator
  public Application(@JsonProperty("name") String name, 
          @JsonProperty("url") String url){
    this.url = url;
    this.name = name;
  }

  public String getName(){
    return name;
  }

  public String getUrl(){
    return url;
  }

}
