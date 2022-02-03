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

import java.util.function.Consumer;

public class DoubleSetting extends Setting<Double>{
    public final double min, max;

    private DoubleSetting(String name, String description, double defaultValue, Consumer<Double> onChanged, Consumer<Setting<Double>> onModuleActivated, double min, double max) {
        super(name, description, defaultValue, onChanged, onModuleActivated);

        this.min = min;
        this.max = max;
    }

    @Override
    protected Double parseImpl(String str) {
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    protected boolean isValueValid(Double value) {
        return value >= min && value <= max;
    }

    public static class Builder extends SettingBuilder<Builder, Double, DoubleSetting> {
        public double min = Double.NEGATIVE_INFINITY, max = Double.POSITIVE_INFINITY;

        public Builder() {
            super(0D);
        }

        public Builder defaultValue(double defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder min(double min) {
            this.min = min;
            return this;
        }

        public Builder max(double max) {
            this.max = max;
            return this;
        }

        public Builder range(double min, double max) {
            this.min = Math.min(min, max);
            this.max = Math.max(min, max);
            return this;
        }

        public DoubleSetting build() {
            return new DoubleSetting(name, description, defaultValue, onChanged, onModuleActivated, min, max);
        }
    }

}
