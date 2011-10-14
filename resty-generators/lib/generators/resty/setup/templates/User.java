package <%= models_package %>;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import org.fusesource.restygwt.client.Json;
import org.fusesource.restygwt.client.Json.Style;

import <%= gwt_rails_package %>.models.IsUser;

@Json(style = Style.RAILS)
public class User implements IsUser {

  private String login;

  private String name;

  @JsonCreator
  public User(@JsonProperty("login") String login,
          @JsonProperty("name") String name){
    this.login = login;
    this.name = name;
  }
  
  public String getLogin(){
    return login;
  }

  public String getName(){
    return name;
  }

  public int hashCode(){
      return login.hashCode();
  }

  public boolean equals(Object other){
    return (other instanceof User) && 
        ((User)other).login == login;
  }

}
