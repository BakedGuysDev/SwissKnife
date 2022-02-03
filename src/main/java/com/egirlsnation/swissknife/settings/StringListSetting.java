/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GPL-3.0 License.
 *
 * You should have received a copy of the GPL-3.0
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

package com.egirlsnation.swissknife.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class StringListSetting extends Setting<List<String>>{
    public String newText = "";

    public StringListSetting(String name, String description, List<String> defaultValue, Consumer<List<String>> onChanged, Consumer<Setting<List<String>>> onModuleActivated) {
        super(name, description, defaultValue, onChanged, onModuleActivated);
    }

    @Override
    protected List<String> parseImpl(String str) {
        return Arrays.asList(str.split(","));
    }

    @Override
    protected boolean isValueValid(List<String> value) {
        return true;
    }

    @Override
    public void reset(boolean callbacks) {
        value = new ArrayList<>(defaultValue);
        if (callbacks) changed();
    }

    public static class Builder extends SettingBuilder<Builder, List<String>, StringListSetting> {
        public Builder() {
            super(new ArrayList<>(0));
        }

        public Builder defaultValue(String... defaults) {
            return defaultValue(defaults != null ? Arrays.asList(defaults) : new ArrayList<>());
        }

        @Override
        public StringListSetting build() {
            return new StringListSetting(name, description, defaultValue, onChanged, onModuleActivated);
        }
    }
}
