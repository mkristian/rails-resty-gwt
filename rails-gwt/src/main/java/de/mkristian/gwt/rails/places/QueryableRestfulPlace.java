package de.mkristian.gwt.rails.places;


public abstract class QueryableRestfulPlace<T, S> extends  RestfulPlace<T, S> {

    public final String query;
    
    public QueryableRestfulPlace(String query, String name) {
        super(RestfulActionEnum.INDEX, name);
        this.query = query;
    }

    public QueryableRestfulPlace(int id, RestfulAction restfulAction,
            String name) {
        super(id, restfulAction, name);
        this.query = null;
    }

    public QueryableRestfulPlace(int id, T model,
            RestfulAction restfulAction, String name) {
        super(id, model, restfulAction, name);
        this.query = null;
    }

    public QueryableRestfulPlace(RestfulAction restfulAction, String name) {
        super(restfulAction, name);
        this.query = null;
    }

    public QueryableRestfulPlace(String id, RestfulAction restfulAction,
            String name) {
        super(id, restfulAction, name);
        this.query = null;
    }

    public QueryableRestfulPlace(T model, RestfulAction restfulAction,
            String name) {
        super(model, restfulAction, name);
        this.query = null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((query == null) ? 0 : query.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        QueryableRestfulPlace<?, ?> other = (QueryableRestfulPlace<?,?>) obj;
        if (query == null) {
            if (other.query != null)
                return false;
        } else if (!query.equals(other.query))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "Place[action=" + action + ", id=" + id + ", model="
                + model + ", resourceName=" + resourceName + ", query=" + query + "]";
    }

}