package <%= views_package %>;

import java.util.ArrayList;

import <%= gwt_rails_package %>.ColumnDefinition;
import <%= models_package %>.<%= class_name %>;

@SuppressWarnings("serial")
public class <%= class_name.pluralize %>ColumnDefinitionsImpl extends 
    ArrayList<ColumnDefinition<<%= class_name %>>> {
  
  protected <%= class_name.pluralize %>ColumnDefinitionsImpl() {
    
    this.add(new ColumnDefinition<<%= class_name %>>() {
      public void render(<%= class_name %> c, StringBuilder sb) {        
        sb.append("<div id='" + c.id + "'>" + "TODO" + "</div>");
      }

      public boolean isClickable() {
        return true;
      }
    });
  }
}