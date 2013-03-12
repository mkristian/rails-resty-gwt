package de.mkristian.ixtlan.gwt.audits;

import static de.mkristian.gwt.rails.places.RestfulActionEnum.SHOW;

import java.util.List;

import com.google.gwt.place.shared.PlaceController;

import de.mkristian.gwt.rails.views.ReadOnlyListViewImpl;

public abstract class AbstractAuditListViewImpl
            extends ReadOnlyListViewImpl<Audit> {

    public AbstractAuditListViewImpl(String title, PlaceController places) {
        super(title, places);
    }

    public void reset(List<Audit> models) {
        list.removeAllRows();
        list.setText(0, 0, "Id");
        list.setText(0, 1, "Login");
        list.setText(0, 2, "Message");
        list.getRowFormatter().addStyleName(0, "gwt-rails-model-list-header");
        if (models != null) {
            int row = 1;
            for(Audit model: models){
                setRow(row, model);
                row++;
            }
        }
    }

    private void setRow(int row, Audit model) {
        list.setText(row, 0, model.getId() + "");
        list.setText(row, 1, model.getLogin() + "");
        list.setText(row, 2, model.getMessage() + "");
    
        list.setWidget(row, 3, newButton(SHOW, model));
    }

}