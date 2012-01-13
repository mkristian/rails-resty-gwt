package <%= models_package %>;
<% if options[:remote_users] -%>

import java.util.Collections;
import java.util.List;
<% end -%>

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import org.fusesource.restygwt.client.Json;
import org.fusesource.restygwt.client.Json.Style;

import <%= gwt_rails_package %>.models.IsUser;

@Json(style = Style.RAILS)
public class User implements IsUser {

  private String login;

  private String name;
<% if options[:remote_users] -%>

  public final List<Application> applications;
<% end -%>

  @JsonCreator
  public User(@JsonProperty("login") String login,
          @JsonProperty("name") String name<% if options[:remote_users] -%>, 
          @JsonProperty("applications") List<Application> applications<% end -%>){
    this.login = login;
    this.name = name;
<% if options[:remote_users] -%>
    this.applications = applications == null ? null : Collections.unmodifiableList(applications);
<% end -%>
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
