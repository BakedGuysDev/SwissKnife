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

public class IntSetting extends Setting<Integer>{
    public final int min, max;

    private IntSetting(String name, String description, int defaultValue, Consumer<Integer> onChanged, Consumer<Setting<Integer>> onModuleActivated, int min, int max) {
        super(name, description, defaultValue, onChanged, onModuleActivated);

        this.min = min;
        this.max = max;
    }

    @Override
    protected Integer parseImpl(String str) {
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    protected boolean isValueValid(Integer value) {
        return value >= min && value <= max;
    }

    public static class Builder extends SettingBuilder<Builder, Integer, IntSetting> {
        private int min = Integer.MIN_VALUE, max = Integer.MAX_VALUE;

        public Builder() {
            super(0);
        }

        public Builder min(int min) {
            this.min = min;
            return this;
        }

        public Builder max(int max) {
            this.max = max;
            return this;
        }

        public Builder range(int min, int max) {
            this.min = Math.min(min, max);
            this.max = Math.max(min, max);
            return this;
        }

        @Override
        public IntSetting build() {
            return new IntSetting(name, description, defaultValue, onChanged, onModuleActivated, min, max);
        }
    }
}
