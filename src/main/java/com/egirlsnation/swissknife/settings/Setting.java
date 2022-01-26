/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.settings;

import com.egirlsnation.swissknife.utils.StringUtil;
import com.egirlsnation.swissknife.utils.misc.IGetter;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class Setting<T> implements IGetter<T> {
    public final String name, title, description;

    protected final T defaultValue;
    protected T value;

    private final Consumer<T> onChanged;
    public final Consumer<Setting<T>> onModuleActivated;

    public Module module;

    public Setting(String name, String description, T defaultValue, Consumer<T> onChanged, Consumer<Setting<T>> onModuleActivated){
        this.name = name;
        this.title = StringUtil.nameToTitle(name);
        this.description = description;
        this.defaultValue = defaultValue;
        reset(false);
        this.onChanged = onChanged;
        this.onModuleActivated = onModuleActivated;
    }

    @Override
    public T get(){
        return value;
    }

    public boolean set(T value){
        if(!isValueValid(value)) return false;
        this.value = value;
        changed();
        return true;
    }

    public void reset(boolean callbacks){
        value = defaultValue;
        if(callbacks) changed();
    }

    public void reset(){
        reset(true);
    }

    public T getDefaultValue(){
        return defaultValue;
    }

    public boolean parse(String str){
        T newValue = parseImpl(str);

        if(newValue != null){
            if(isValueValid(newValue)){
                value = newValue;
                changed();
            }
        }

        return newValue != null;
    }

    public void changed(){
        if (onChanged != null) onChanged.accept(value);
    }

    public void onActivated(){
        if(onModuleActivated != null) onModuleActivated.accept(this);
    }

    protected abstract  T parseImpl(String str);

    protected abstract boolean isValueValid(T value);

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Setting<?> setting = (Setting<?>) o;
        return Objects.equals(name, setting.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public abstract static class SettingBuilder<B, V, S> {
        protected String name = "undefined", description = "";
        protected V defaultValue;
        protected Consumer<V> onChanged;
        protected Consumer<Setting<V>> onModuleActivated;

        protected SettingBuilder(V defaultValue) {
            this.defaultValue = defaultValue;
        }

        public B name(String name) {
            this.name = name;
            return (B) this;
        }

        public B description(String description) {
            this.description = description;
            return (B) this;
        }

        public B defaultValue(V defaultValue) {
            this.defaultValue = defaultValue;
            return (B) this;
        }

        public B onChanged(Consumer<V> onChanged) {
            this.onChanged = onChanged;
            return (B) this;
        }

        public B onModuleActivated(Consumer<Setting<V>> onModuleActivated) {
            this.onModuleActivated = onModuleActivated;
            return (B) this;
        }

        public abstract S build();
    }

}
