package <%= activities_package %>;


import <%= models_package %>.<%= class_name %>;
import <%= places_package %>.<%= class_name %>Place;
import <%= restservices_package %>.<%= class_name.pluralize %>RestService;
import <%= views_package %>.<%= class_name %>View;

import <%= gwt_rails_package %>.Notice;
import <%= gwt_rails_package %>.RestfulActionEnum;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class <%= class_name %>Activity extends AbstractActivity implements <%= class_name %>View.Presenter{

    private final <%= class_name %>Place place;
    private final <%= class_name.pluralize %>RestService service;
    private final Notice notice;
    private final PlaceController placeController;
    private final <%= class_name %>View view;
    
    @Inject
    public <%= class_name %>Activity(@Assisted <%= class_name %>Place place, Notice notice, <%= class_name %>View view,
            <%= class_name.pluralize %>RestService service, PlaceController placeController) {
	this.place = place;
        this.notice = notice;
        this.view = view;
        this.service = service;
        this.placeController = placeController;
    }

    public void start(AcceptsOneWidget display, EventBus eventBus) {
        display.setWidget(view.asWidget());
        view.setPresenter(this);
<% if options[:singleton] -%>
	load();
<% else -%>
        switch(RestfulActionEnum.valueOf(place.action.name())){
            case EDIT: 
            case SHOW:
                load(place.id);
                break;
            case LIST:
                //TODO
            default:
            case NEW: 
                notice.setText(null);
                view.reset(new <%= class_name %>());
                break;
        }
<% end -%>
        view.reset(place.action);
    }

    public void goTo(Place place) {
        placeController.goTo(place);
    }

    public void load(<% unless options[:singleton] -%>int id<% end -%>) {
        view.setEnabled(false);
        service.show(<% unless options[:singleton] -%>id, <% end -%>new MethodCallback<<%= class_name %>>() {

            public void onFailure(Method method, Throwable exception) {
                notice.setText("error loading <%= class_name.humanize %>: "
                        + exception.getMessage());
            }

            public void onSuccess(Method method, <%= class_name %> response) {
                view.reset(response);
                notice.setText(null);
                view.reset(place.action);
            }
        });
        if(!notice.isVisible()){
            notice.setText("loading <%= class_name.humanize %> . . .");
        }
    }
<% unless options[:singleton] -%>
    public void create() {
        <%= class_name %> model = view.retrieve<%= class_name %>();
        view.setEnabled(false);
        service.create(model, new MethodCallback<<%= class_name %>>() {

            public void onFailure(Method method, Throwable exception) {
                notice.setText("error creating <%= class_name.humanize %>: "
                        + exception.getMessage());
            }

            public void onSuccess(Method method, <%= class_name %> response) {
                goTo(new <%= class_name %>Place(response.id, 
                        RestfulActionEnum.EDIT));
            }
        });
        notice.setText("creating <%= class_name.humanize %> . . .");                
    }

    public void delete() {
        <%= class_name %> model = view.retrieve<%= class_name %>();
        view.setEnabled(false);
        service.destroy(model, new MethodCallback<Void>() {

            public void onFailure(Method method, Throwable exception) {
                notice.setText("error deleting <%= class_name.humanize %>: "
                        + exception.getMessage());
            }

            public void onSuccess(Method method, Void response) {
                goTo(new <%= class_name %>Place(RestfulActionEnum.LIST));
            }
        });
        notice.setText("deleting <%= class_name.humanize %> . . .");                
    }
<% end -%>
    public void save() {
        <%= class_name %> model = view.retrieve<%= class_name %>();
        view.setEnabled(false);
        service.update(model, new MethodCallback<<%= class_name %>>() {

            public void onFailure(Method method, Throwable exception) {
                notice.setText("error loading <%= class_name.humanize %>: "
                        + exception.getMessage());
            }

            public void onSuccess(Method method, <%= class_name %> response) {
                goTo(new <%= class_name %>Place(<% unless options[:singleton] -%>response.id, 
                        <% end -%>RestfulActionEnum.EDIT));
            }
        });
        notice.setText("saving <%= class_name.humanize %> . . .");        
    }
}