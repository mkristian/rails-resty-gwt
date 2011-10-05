package <%= activities_package %>;
<% unless options[:singleton] -%>

import java.util.List;
<% end -%>

import <%= events_package %>.<%= class_name %>Event;
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
import <%= models_package %>.<%= attribute.name.classify %>;
<% end -%>
<% end -%>
import <%= models_package %>.<%= class_name %>;
import <%= places_package %>.<%= class_name %>Place;
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
import <%= restservices_package %>.<%= attribute.name.classify.to_s.pluralize %>RestService;
<% end -%>
<% end -%>
import <%= restservices_package %>.<%= class_name.pluralize %>RestService;
import <%= views_package %>.<%= class_name %>View;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import <%= gwt_rails_package %>.Notice;
import <%= gwt_rails_package %>.events.ModelEvent.Action;
<% unless options[:singleton] -%>
import <%= gwt_rails_package %>.places.RestfulActionEnum;
<% end -%>

public class <%= class_name %>Activity extends AbstractActivity implements <%= class_name %>View.Presenter{

    private final <%= class_name %>Place place;
    private final <%= class_name.pluralize %>RestService service;
    private final Notice notice;
    private final PlaceController placeController;
    private final <%= class_name %>View view;
    private EventBus eventBus;
    
    @Inject
    public <%= class_name %>Activity(@Assisted <%= class_name %>Place place, final Notice notice, final <%= class_name %>View view,
            <%= class_name.pluralize %>RestService service, PlaceController placeController<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
, <%= attribute.name.classify.to_s.pluralize %>RestService <%= attribute.name %>RestService<% end -%><% end -%>) {
        this.place = place;
        this.notice = notice;
        this.view = view;
        this.service = service;
        this.placeController = placeController;
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
    
        view.reset<%= attribute.name.classify.to_s.pluralize %>(null);
        <%= attribute.name %>RestService.index(new MethodCallback<List<<%= attribute.name.classify %>>>() {
            
            public void onSuccess(Method method, List<<%= attribute.name.classify %>> response) {
                view.reset<%= attribute.name.classify.to_s.pluralize %>(response);
            }
            
            public void onFailure(Method method, Throwable exception) {
                notice.setText("failed to load <%= attribute.name.pluralize %>");
            }
        });
<% end -%>
<% end -%>
    }

    public void start(AcceptsOneWidget display, EventBus eventBus) {
        this.eventBus = eventBus;
        display.setWidget(view.asWidget());
        view.setPresenter(this);
<% if options[:singleton] -%>
        load();
<% else -%>
        switch(RestfulActionEnum.valueOf(place.action.name())){
            case EDIT: 
            case SHOW:
                load(place.model == null ? place.id : place.model.getId());
                break;
            case NEW:
                notice.setText(null);
                view.edit(new <%= class_name %>());
                break;
            case INDEX:
            default:
                load();
                break;
        }
<% end -%>
        view.reset(place.action);
    }

    public void goTo(Place place) {
        placeController.goTo(place);
    }
<% unless options[:singleton] -%>

    public void load(){  
        view.setEnabled(false);
        service.index(new MethodCallback<List<<%= class_name %>>>() {

            public void onFailure(Method method, Throwable exception) {
                notice.setText("error loading list of <%= class_name.underscore.humanize %>: "
                        + exception.getMessage());
                view.reset(place.action);
            }

            public void onSuccess(Method method, List<<%= class_name %>> response) {
                eventBus.fireEvent(new <%= class_name %>Event(response, Action.LOAD));
                notice.setText(null);
                view.reset(response);
                view.reset(place.action);
            }
        });
        if(!notice.isVisible()){
            notice.setText("loading list of <%= class_name.underscore.humanize %> . . .");
        }
    }
<% end -%>
<% unless options[:singleton] -%>

    public void create() {
        <%= class_name %> model = view.flush();
        view.setEnabled(false);
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
        <%= attribute.name.camelcase %> <%= attribute.name %> = model.skip<%= attribute.name.camelcase %>();
<% end -%>
<% end -%>
        service.create(model, new MethodCallback<<%= class_name %>>() {

            public void onFailure(Method method, Throwable exception) {
                notice.setText("error creating <%= class_name.underscore.humanize %>: "
                        + exception.getMessage());
                view.reset(place.action);
            }

            public void onSuccess(Method method, <%= class_name %> response) {
                eventBus.fireEvent(new <%= class_name %>Event(response, Action.CREATE));
                notice.setText(null);
                view.addToList(response);
                goTo(new <%= class_name %>Place(response.getId(), RestfulActionEnum.EDIT));
            }
        });
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
        model.set<%= attribute.name.camelcase %>(<%= attribute.name %>);
<% end -%>
<% end -%>
        notice.setText("creating <%= class_name.underscore.humanize %> . . .");
    }
<% end -%>

    public void load(<% unless options[:singleton] -%>int id<% end -%>) {
        view.setEnabled(false);
        service.show(<% unless options[:singleton] -%>id, <% end -%>new MethodCallback<<%= class_name %>>() {

            public void onFailure(Method method, Throwable exception) {
                notice.setText("error loading <%= class_name.underscore.humanize %>: "
                        + exception.getMessage());
                view.reset(place.action);
            }

            public void onSuccess(Method method, <%= class_name %> response) {
                eventBus.fireEvent(new <%= class_name %>Event(response, Action.LOAD));
                notice.setText(null);
                view.edit(response);
                view.reset(place.action);
            }
        });
        if(!notice.isVisible()){
            notice.setText("loading <%= class_name.underscore.humanize %> . . .");
        }
    }

    public void save() {
        <%= class_name %> model = view.flush();
        view.setEnabled(false);
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
        <%= attribute.name.camelcase %> <%= attribute.name %> = model.skip<%= attribute.name.camelcase %>();
<% end -%>
<% end -%>
        service.update(model, new MethodCallback<<%= class_name %>>() {

            public void onFailure(Method method, Throwable exception) {
                notice.setText("error saving <%= class_name.underscore.humanize %>: "
                        + exception.getMessage());
                view.reset(place.action);
            }

            public void onSuccess(Method method, <%= class_name %> response) {
                eventBus.fireEvent(new <%= class_name %>Event(response, Action.UPDATE));
                notice.setText(null);
<% unless options[:singleton] -%>
                view.updateInList(response);
<% end -%>
                view.edit(response);
                view.reset(place.action);
            }
        });
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
        model.set<%= attribute.name.camelcase %>(<%= attribute.name %>);
<% end -%>
<% end -%>
        notice.setText("saving <%= class_name.underscore.humanize %> . . .");
    }
<% unless options[:singleton] -%>

    public void delete(final <%= class_name %> model){
        view.setEnabled(false);
        service.destroy(model, new MethodCallback<Void>() {

            public void onFailure(Method method, Throwable exception) {
                notice.setText("error deleting <%= class_name.underscore.humanize %>: "
                        + exception.getMessage());
                view.reset(place.action);
            }

            public void onSuccess(Method method, Void response) {
                eventBus.fireEvent(new <%= class_name %>Event(model, Action.DESTROY));
                notice.setText(null);
                view.removeFromList(model);
                <%= class_name %>Place next = new <%= class_name %>Place(RestfulActionEnum.INDEX);
                if(placeController.getWhere().equals(next)){
                    view.reset(place.action);
                }
                else{
                    goTo(next);
                }
            }
        });
        notice.setText("deleting <%= class_name.underscore.humanize %> . . .");
    }
<% end -%>
}