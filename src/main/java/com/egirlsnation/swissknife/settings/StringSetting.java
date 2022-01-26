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

import java.util.function.Consumer;

public class StringSetting extends Setting<String>{

    public StringSetting(String name, String description, String defaultValue, Consumer<String> onChanged, Consumer<Setting<String>> onModuleActivated) {
        super(name, description, defaultValue, onChanged, onModuleActivated);

        value = defaultValue;
    }

    @Override
    protected String parseImpl(String str) {
        return str;
    }

    @Override
    public void reset(boolean callbacks) {
        value = defaultValue;
        if (callbacks) changed();
    }

    @Override
    protected boolean isValueValid(String value) {
        return true;
    }

    public static class Builder extends SettingBuilder<Builder, String, StringSetting> {
        public Builder() {
            super(null);
        }

        @Override
        public StringSetting build() {
            return new StringSetting(name, description, defaultValue, onChanged, onModuleActivated);
        }
    }
}
