package <%= managed_package %>;

import <%= gwt_rails_package %>.RestfulAction;

public abstract class RestfulPlace<T> extends ActivityPlace {

    public final RestfulAction action;

    private T resource;

    public RestfulPlace(RestfulAction restfulAction) {
        this.action = restfulAction;
    }

    public void setResource(T resource) {
        if (this.resource == null) {
            this.resource = resource;
        }
    }

    public T getResource() {
        return resource;
    }
}