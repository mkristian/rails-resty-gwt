/**
 * 
 */
package de.mkristian.gwt.rails;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Singleton;

@Singleton
public class Notice extends Label {
	public Notice(){
		setStyleName("notice");
	}
	
	public void setText(String msg){
		super.setText(msg);
		setVisible(msg != null);
	}
}