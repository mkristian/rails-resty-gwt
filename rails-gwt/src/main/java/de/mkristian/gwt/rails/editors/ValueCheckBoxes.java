package de.mkristian.gwt.rails.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SimpleKeyProvider;

public class ValueCheckBoxes<T> extends Composite
        implements
            IsEditor<TakesValueEditor<List<T>>>,
            TakesValue<List<T>>,
            HasValueChangeHandlers<List<T>> {

    private final List<T> values = new ArrayList<T>();
    private final Map<Object, Integer> valueKeyToIndex = new HashMap<Object, Integer>();
    private final Renderer<T> renderer;
    private final ProvidesKey<T> keyProvider;

    private TakesValueEditor<List<T>> editor;
    private List<T> selectedValues = new LinkedList<T>();
    private ValueChangeHandler<Boolean> handler;
    private boolean enabled;

    public ValueCheckBoxes(Renderer<T> renderer) {
        this(renderer, new SimpleKeyProvider<T>());
    }

    static class KeyedCheckBox extends CheckBox {

        private final Object key;

        KeyedCheckBox(String label, Object key) {
            super(label);
            this.key = key;
        }
        Object getKey() {
            return key;
        }
    }

    public ValueCheckBoxes(Renderer<T> renderer, ProvidesKey<T> keyProvider) {
        this.keyProvider = keyProvider;
        this.renderer = renderer;
        initWidget(new FlowPanel());

        handler = new ValueChangeHandler<Boolean>() {

            public void onValueChange(ValueChangeEvent<Boolean> event) {
                Object key = ((KeyedCheckBox) event.getSource()).getKey();
                T value = values.get(valueKeyToIndex.get(key));
                if (event.getValue()) {
                    int index = selectedValues.indexOf(value);
                    if (index < 0) {
                        selectedValues.add(value);
                    }
                } else {
                    selectedValues.remove(value);
                }
            }
        };
    }

    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<List<T>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * Returns a {@link TakesValueEditor} backed by the ValueCheckBoxes.
     */
    public TakesValueEditor<List<T>> asEditor() {
        if (editor == null) {
            editor = TakesValueEditor.of(this);
        }
        return editor;
    }

    public List<T> getValue() {
        return Collections.unmodifiableList(selectedValues);
    }

    public void setAcceptableValues(Collection<T> newValues) {
        values.clear();
        valueKeyToIndex.clear();
        FlowPanel flowPanel = getFlowPanel();
        flowPanel.clear();

        for (T nextNewValue : newValues) {
            addValue(nextNewValue);
        }

        updateFlowPanel();
        setEnabled(enabled);
    }

    /**
     * Set the value and display it in the select element. Add the value to the
     * acceptable set if it is not already there.
     */
    public void setValue(List<T> values) {
        setValue(values, false);
    }

    public void setValue(List<T> values, boolean fireEvents) {
        if (values == null
                || values == this.selectedValues
                || (this.selectedValues != null && this.selectedValues
                        .equals(values))) {
            return;
        }

        List<T> before = this.selectedValues;
        this.selectedValues = values;

        updateFlowPanel();

        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this, before, values);
        }
    }

    private void addValue(T value) {
        Object key = keyProvider.getKey(value);
        if (valueKeyToIndex.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate value: " + value);
        }

        valueKeyToIndex.put(key, values.size());
        values.add(value);

        KeyedCheckBox box = new KeyedCheckBox(renderer.render(value), key);
        box.addValueChangeHandler(handler);
        getFlowPanel().add(box);
        assert values.size() == getFlowPanel().getWidgetCount();
    }

    private FlowPanel getFlowPanel() {
        return (FlowPanel) getWidget();
    }

    private void updateFlowPanel() {
        List<T> newSelected = new ArrayList<T>(this.selectedValues);
        newSelected.removeAll(this.values);

        for (T value : newSelected) {
            addValue(value);
        }

        int index = 0;
        for (T value : values) {
            ((CheckBox) getFlowPanel().getWidget(index++))
                    .setValue(selectedValues.contains(value));
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        for (int index = 0; index < getFlowPanel().getWidgetCount(); index++) {
            ((CheckBox) getFlowPanel().getWidget(index)).setEnabled(enabled);
        }
    }
}
