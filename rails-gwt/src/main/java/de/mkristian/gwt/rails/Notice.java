/**
 *
 */
package de.mkristian.gwt.rails;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Singleton;

@Singleton
public class Notice extends PopupPanel {
    Label notice = new Label();

 public Notice(){
  setStyleName("notice");
  setWidget(notice);
        setVisible(false);
 }

 public void setText(String msg){
  notice.setText(msg);
  if(msg == null){
            setVisible(false);
      hide();
  }
  else {
            setVisible(true);
      show();
  }
 }
}