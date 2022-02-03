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

import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.StringUtil;
import com.egirlsnation.swissknife.utils.misc.IGetter;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.comments.CommentType;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class Setting<Object> implements IGetter<Object> {
    public final String name, title, description;

    protected final Object defaultValue;
    protected Object value;

    private final Consumer<Object> onChanged;
    public final Consumer<Setting<Object>> onModuleActivated;

    public Module module;

    public Setting(String name, String description, Object defaultValue, Consumer<Object> onChanged, Consumer<Setting<Object>> onModuleActivated){
        this.name = name;
        this.title = StringUtil.nameToTitle(name);
        this.description = description;
        this.defaultValue = defaultValue;
        reset(false);
        this.onChanged = onChanged;
        this.onModuleActivated = onModuleActivated;
    }

    public Object get(){
        return value;
    }

    public boolean set(Object value){
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

    public Object getDefaultValue(){
        return defaultValue;
    }

    public boolean parse(String str){
        Object newValue = parseImpl(str);

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

    protected abstract  Object parseImpl(String str);

    protected abstract boolean isValueValid(Object value);

    public void writeToConfig(YamlFile file, Module module, SettingGroup sg, ConfigurationSection section){
        section.set(name, get());

        String path = module.category.name + "." + module.name + "." + sg.name + "." + name;
        //TODO: Either better way or fix comment overspill in simpleyaml lib
        //Very very very very bad way of preventing comments overspilling into values
        if((name+ ": " + get()).length() >= 76){
            file.setComment(path, description, CommentType.BLOCK);
        }else{
            file.setComment(path, description, CommentType.SIDE);
        }
    }

    @Override
    public String toString() {
        return value.toString();
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
