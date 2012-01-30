package de.mkristian.gwt.rails.views;

import de.mkristian.gwt.rails.places.RestfulActionEnum;

public class ModelButton<T> extends GeneralModelButton<T, RestfulActionEnum> {
    public ModelButton(RestfulActionEnum action, T model){
        super(action, model);
    }
}