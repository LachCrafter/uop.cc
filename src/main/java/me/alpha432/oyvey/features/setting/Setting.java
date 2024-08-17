package me.alpha432.oyvey.features.setting;

import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.features.Feature;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Predicate;

public class Setting<T> {
    private final String name;
    private final T defaultValue;
    private T value;
    private T plannedValue;
    private T min;
    private T max;
    private final boolean hasRestriction;
    private Predicate<T> visibility;
    private final String description;
    private Feature feature;

    public Setting(String name, T defaultValue) {
        this(name, defaultValue, "", null, null, null, false);
    }

    public Setting(String name, T defaultValue, String description) {
        this(name, defaultValue, description, null, null, null, false);
    }

    public Setting(String name, T defaultValue, T min, T max, String description) {
        this(name, defaultValue, description, min, max, null, true);
    }

    public Setting(String name, T defaultValue, T min, T max) {
        this(name, defaultValue, "", min, max, null, true);
    }

    public Setting(String name, T defaultValue, T min, T max, Predicate<T> visibility, String description) {
        this(name, defaultValue, description, min, max, visibility, true);
    }

    public Setting(String name, T defaultValue, T min, T max, Predicate<T> visibility) {
        this(name, defaultValue, "", min, max, visibility, true);
    }

    public Setting(String name, T defaultValue, Predicate<T> visibility) {
        this(name, defaultValue, "", null, null, visibility, false);
    }

    private Setting(String name, T defaultValue, String description, T min, T max, Predicate<T> visibility, boolean hasRestriction) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.plannedValue = defaultValue;
        this.min = min;
        this.max = max;
        this.description = description;
        this.visibility = visibility;
        this.hasRestriction = hasRestriction;
    }

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }

    public T getPlannedValue() {
        return this.plannedValue;
    }

    public T getMin() {
        return this.min;
    }

    public T getMax() {
        return this.max;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public String getDescription() {
        return this.description != null ? this.description : "";
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public boolean hasRestriction() {
        return this.hasRestriction;
    }

    public boolean isVisible() {
        return this.visibility == null || this.visibility.test(this.getValue());
    }

    // Setter methods
    public void setValue(T value) {
        setPlannedValue(value);
        if (this.hasRestriction) {
            enforceBounds();
        }
        if (postEvent()) {
            this.value = this.plannedValue;
        } else {
            this.plannedValue = this.value;
        }
    }

    public void setValueNoEvent(T value) {
        setPlannedValue(value);
        if (this.hasRestriction) {
            enforceBounds();
        }
        this.value = this.plannedValue;
    }

    public void setPlannedValue(T value) {
        this.plannedValue = value;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public void setMax(T max) {
        this.max = max;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public void setVisibility(Predicate<T> visibility) {
        this.visibility = visibility;
    }

    private void enforceBounds() {
        if (((Number) this.min).floatValue() > ((Number) this.plannedValue).floatValue()) {
            this.plannedValue = this.min;
        }
        if (((Number) this.max).floatValue() < ((Number) this.plannedValue).floatValue()) {
            this.plannedValue = this.max;
        }
    }

    private boolean postEvent() {
        ClientEvent event = new ClientEvent(this);
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }

    public int getEnum(String input) {
        for (int i = 0; i < this.value.getClass().getEnumConstants().length; i++) {
            Enum<?> e = (Enum<?>) this.value.getClass().getEnumConstants()[i];
            if (e.name().equalsIgnoreCase(input)) {
                return i;
            }
        }
        return -1;
    }

    public void setEnumValue(String value) {
        for (Enum<?> e : (Enum<?>[]) this.value.getClass().getEnumConstants()) {
            if (e.name().equalsIgnoreCase(value)) {
                this.value = (T) e;
                break;
            }
        }
    }

    public String currentEnumName() {
        return EnumConverter.getProperName((Enum<?>) this.value);
    }

    public int currentEnum() {
        return EnumConverter.currentEnum((Enum<?>) this.value);
    }

    public void increaseEnum() {
        this.plannedValue = (T) EnumConverter.increaseEnum((Enum<?>) this.value);
        if (postEvent()) {
            this.value = this.plannedValue;
        } else {
            this.plannedValue = this.value;
        }
    }

    public void increaseEnumNoEvent() {
        this.value = (T) EnumConverter.increaseEnum((Enum<?>) this.value);
    }

    public String getType() {
        return isEnumSetting() ? "Enum" : getClassName(this.defaultValue);
    }

    public <V> String getClassName(V value) {
        return value.getClass().getSimpleName();
    }

    public String getValueAsString() {
        return this.value.toString();
    }

    public boolean isNumberSetting() {
        return this.value instanceof Number;
    }

    public boolean isEnumSetting() {
        return !isNumberSetting() && !(this.value instanceof String) && !(this.value instanceof Bind) && !(this.value instanceof Character) && !(this.value instanceof Boolean);
    }

    public boolean isStringSetting() {
        return this.value instanceof String;
    }
}
