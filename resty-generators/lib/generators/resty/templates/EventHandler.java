package <%= events_package %>;

import <%= models_package %>.<%= class_name %>;

import <%= gwt_rails_package %>.events.ModelEventHandler;

public interface <%= class_name %>EventHandler extends ModelEventHandler<<%= class_name %>> {
}