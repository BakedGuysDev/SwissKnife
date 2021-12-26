/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.systems.modules;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Category {
    public final String name;
    public final ItemStack icon;

    public Category(String name, ItemStack icon){
        this.name = name;
        this.icon = icon == null ? new ItemStack(Material.GRASS_BLOCK) : icon;
    }
}
