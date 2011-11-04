package <%= activities_package %>;
<% if !options[:singleton] || attributes.detect { |a| a.type == :belongs_to} -%>

import java.util.List;
<% end -%>

import <%= events_package %>.<%= class_name %>Event;
<% if !options[:singleton] && !options[:read_only] -%>
import <%= events_package %>.<%= class_name %>EventHandler;
<% end -%>
<% if !options[:singleton] && !options[:read_only] -%>
import <%= caches_package %>.<%= class_name.pluralize %>Cache;
<% end -%>
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
import <%= models_package %>.<%= attribute.name.classify %>;
<% end -%>
<% end -%>
import <%= models_package %>.<%= class_name %>;
import <%= places_package %>.<%= class_name %>Place;
<% for attribute in attributes -%>
<% if attribute.type == :belongs_to -%>
import <%= caches_package %>.<%= attribute.name.classify.to_s.pluralize %>Cache;
import <%= events_package %>.<%= attribute.name.classify %>Event;
import <%= events_package %>.<%= attribute.name.classify %>EventHandler;
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
<% if !options[:singleton] && !options[:read_only] -%>
import <%= gwt_rails_package %>.events.ModelEvent;
<% end -%>
import <%= gwt_rails_package %>.events.ModelEvent.Action;
<% unless options[:singleton] -%>
import <%= gwt_rails_package %>.places.RestfulActionEnum;
<% end -%>

public class <%= class_name %>Activity extends AbstractActivity implements <%= class_name %>View.Presenter{

<% if !options[:singleton] -%>
    private final <%= class_name %>Place place;
<% end -%>
    private final <%= class_name.pluralize %>RestService service;
    private final Notice notice;
    private final PlaceController placeController;
    private final <%= class_name %>View view;
<% if !options[:singleton] && !options[:read_only] -%>
    private final <%= class_name.pluralize %>Cache cache;
<% end -%>
<% attributes.select { |a| a.type == :belongs_to }.each do |attribute| -%>
    private final <%= attribute.name.classify.to_s.pluralize %>Cache <%= attribute.name.pluralize %>Cache;
<% end -%>
    private EventBus eventBus;
    
    @Inject
    public <%= class_name %>Activity(@Assisted <%= class_name %>Place place, final Notice notice, final <%= class_name %>View view,
            <%= class_name.pluralize %>RestService service, PlaceController placeController<% if !options[:singleton] && !options[:read_only] -%>,
            <%= class_name.pluralize %>Cache cache<% for attribute in attributes -%>
<% end -%>
<% if attribute.type == :belongs_to -%>
, <%= attribute.name.classify.to_s.pluralize %>Cache <%= attribute.name.pluralize %>Cache<% end -%><% end -%>) {
<% if !options[:singleton] -%>
        this.place = place;
<% end -%>
        this.notice = notice;
        this.view = view;
        this.service = service;
        this.placeController = placeController;
<% if !options[:singleton] && !options[:read_only] -%>
        this.cache = cache;
<% end -%>
<% attributes.select { |a| a.type == :belongs_to }.each do |attribute| -%>
        this.<%= attribute.name.pluralize %>Cache = <%= attribute.name.pluralize %>Cache;
<% end -%>

        notice.hide();
        view.setup(this, place.action);
<% for attribute in attributes -%>
<% if attribute.type == :belongsss_to -%>
    
        view.reset<%= attribute.name.classify.to_s.pluralize %>(null);
        <%= attribute.name %>RestService.index(new MethodCallback<List<<%= attribute.name.classify %>>>() {
            
            public void onSuccess(Method method, List<<%= attribute.name.classify %>> response) {
                view.reset<%= attribute.name.classify.to_s.pluralize %>(response);
            }
            
            public void onFailure(Method method, Throwable exception) {
                notice.error("failed to load <%= attribute.name.pluralize %>");
            }
        });
<% end -%>
<% end -%>
    }

    public void start(AcceptsOneWidget display, EventBus eventBus) {
        this.eventBus = eventBus;
<% attributes.select { |a| a.type == :belongs_to }.each do |attribute| -%>

        this.eventBus.addHandler(<%= attribute.name.classify %>Event.TYPE, new <%= attribute.name.classify %>EventHandler() {
            
            public void onModelEvent(ModelEvent<<%= attribute.name.classify %>> event) {
                if(event.getModels() != null) {
                    view.reset<%= attribute.name.classify.to_s.pluralize %>(event.getModels());
                }
            }
        });
        view.reset<%= attribute.name.classify.to_s.pluralize %>(<%= attribute.name.pluralize %>Cache.getOrLoadModels());
<% end -%>
<% if !options[:singleton] && !options[:read_only]-%>

        this.eventBus.addHandler(<%= class_name %>Event.TYPE, new <%= class_name %>EventHandler() {

            public void onModelEvent(ModelEvent<<%= class_name %>> event) {
                notice.finishLoading();
                if (event.getModels() != null) {
                    view.reset(event.getModels());
                } else if (event.getModel() == null) {
                    // TODO maybe error message ?
                    notice.error("error loading list of <%= class_name.underscore.humanize %>");
                }
            }
        });
<% end -%>

        display.setWidget(view.asWidget());

<% if options[:singleton] -%>
        load();
<% else -%>
        switch(RestfulActionEnum.valueOf(place.action)){
<% unless options[:read_only] -%>
            case EDIT:
<% end -%>
            case SHOW:
                load(place.id);
                break;
<% unless options[:read_only] -%>
            case NEW:
                view.edit(new <%= class_name %>());
                break;
<% end -%>
            case INDEX:
            default:
                load();
                break;
        }
<% end -%>
    }

    public void goTo(Place place) {
        placeController.goTo(place);
    }
<% unless options[:singleton] -%>

    public void load(){
<% if options[:read_only] -%>
        service.index(new MethodCallback<List<<%= class_name %>>>() {

            public void onFailure(Method method, Throwable exception) {
                notice.finishLoading();
                notice.error("error loading list of <%= class_name.underscore.humanize %>", exception);
            }

            public void onSuccess(Method method, List<<%= class_name %>> response) {
                notice.finishLoading();
                eventBus.fireEvent(new <%= class_name %>Event(response, Action.LOAD));
                view.reset(response);
            }
        });
        notice.loading();
<% else -%>
        List<<%= class_name %>> models = cache.getOrLoadModels();
        if (models != null){
            view.reset(models);
        }
        else {
            // loading the event callback fills the resets the models
            notice.loading();
        }
<% end -%>
    }
<% end -%>
<% if !options[:singleton] && !options[:read_only] -%>

    public void create() {
        <%= class_name %> model = view.flush();
        service.create(model<% if attributes.detect{|a| a.type == :belongs_to} -%>.minimalClone()<% end -%>, new MethodCallback<<%= class_name %>>() {

            public void onFailure(Method method, Throwable exception) {
                notice.finishLoading();
                notice.error("error creating <%= class_name.underscore.humanize %>", exception);
            }

            public void onSuccess(Method method, <%= class_name %> response) {
                notice.finishLoading();
                eventBus.fireEvent(new <%= class_name %>Event(response, Action.CREATE));
                goTo(new <%= class_name %>Place(response.getId(), RestfulActionEnum.EDIT));
            }
        });
        notice.loading();
    }
<% end -%>

    public void load(<% unless options[:singleton] -%>int id<% end -%>) {
<% if !options[:singleton] && !options[:read_only] -%>
        <%= class_name %> model = cache.getModel(id);
        view.edit(model);
<% elsif options[:read_only] -%>
        view.edit(new <%= class_name %>()); // clear the form
<% end -%>
<% if !options[:singleton] && !options[:read_only] && (options[:timestamps] || options[:modified_by]) -%>
        if (model == null || model.get<% if options[:timestamps] -%>CreatedAt()<% else -%>ModifiedBy()<% end -%> == null) {
<% indent = '    ' -%>
<% else -%>
<% indent = '' -%>
<% end -%>
<%= indent %>        service.show(<% unless options[:singleton] -%>id, <% end -%>new MethodCallback<<%= class_name %>>() {

<%= indent %>            public void onFailure(Method method, Throwable exception) {
<%= indent %>                notice.finishLoading();
<%= indent %>                notice.error("error loading <%= class_name.underscore.humanize %>", exception);
<%= indent %>            }

<%= indent %>            public void onSuccess(Method method, <%= class_name %> response) {
<%= indent %>                notice.finishLoading();
<%= indent %>                eventBus.fireEvent(new <%= class_name %>Event(response, Action.LOAD));
<%= indent %>                view.edit(response);
<%= indent %>            }
<%= indent %>        });
<%= indent %>        notice.loading();
<% if !options[:singleton] && !options[:read_only] && (options[:timestamps] || options[:modified_by]) -%>
        }
<% end -%>
    }
<% unless options[:read_only] -%>

    public void save() {
        <%= class_name %> model = view.flush();
        service.update(model<% if attributes.detect{|a| a.type == :belongs_to} -%>.minimalClone()<% end -%>, new MethodCallback<<%= class_name %>>() {

            public void onFailure(Method method, Throwable exception) {
                notice.finishLoading();
                notice.error("error saving <%= class_name.underscore.humanize %>", exception);
            }

            public void onSuccess(Method method, <%= class_name %> response) {
                notice.finishLoading();
                eventBus.fireEvent(new <%= class_name %>Event(response, Action.UPDATE));
                view.edit(response);
            }
        });
        notice.loading();
    }
<% unless options[:singleton] -%>

    public void delete(final <%= class_name %> model){
        service.destroy(model, new MethodCallback<Void>() {

            public void onFailure(Method method, Throwable exception) {
                notice.finishLoading();
                notice.error("error deleting <%= class_name.underscore.humanize %>", exception);
            }

            public void onSuccess(Method method, Void response) {
                notice.finishLoading();
                eventBus.fireEvent(new <%= class_name %>Event(model, Action.DESTROY));
                <%= class_name %>Place next = new <%= class_name %>Place(RestfulActionEnum.INDEX);
                if(placeController.getWhere().equals(next)){
                    view.removeFromList(model);
                }
                else{
                    goTo(next);
                }
            }
        });
        notice.loading();
    }
<% end -%>
<% end -%>
}