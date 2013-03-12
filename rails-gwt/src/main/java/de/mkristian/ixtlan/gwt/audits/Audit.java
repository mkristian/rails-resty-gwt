package de.mkristian.ixtlan.gwt.audits;


import java.util.Date;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.fusesource.restygwt.client.Json;
import org.fusesource.restygwt.client.Json.Style;

import de.mkristian.gwt.rails.models.HasToDisplay;
import de.mkristian.gwt.rails.models.Identifiable;

@Json(style = Style.RAILS)
public class Audit implements HasToDisplay, Identifiable {

  public final int id;

  @Json(name = "created_at")
  private final Date createdAt;

  private String login;

  private String message;

  public Audit(){
    this(0, null);
  }
  
  @JsonCreator
  public Audit(@JsonProperty("id") int id, 
          @JsonProperty("createdAt") Date createdAt){
    this.id = id;
    this.createdAt = createdAt;
  }

  public int getId(){
    return id;
  }

  public Date getCreatedAt(){
    return createdAt;
  }

  public String getLogin(){
    return login;
  }

  public void setLogin(String value){
    login = value;
  }

  public String getMessage(){
    return message;
  }

  public void setMessage(String value){
    message = value;
  }

  public int hashCode(){
    return id;
  }

  public boolean equals(Object other){
    return (other instanceof Audit) && 
        ((Audit)other).id == id;
  }

  public String toDisplay() {
    return login;
  }
}
