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

package com.egirlsnation.swissknife.systems.modules;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Category {
    public final String name;
    public final ItemStack icon;
    private final int nameHash;

    public Category(String name, ItemStack icon){
        this.name = name;
        this.nameHash = name.hashCode();
        this.icon = icon == null ? new ItemStack(Material.GRASS_BLOCK) : icon;
    }

    public Category(String name){
        this(name, null);
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return nameHash == category.nameHash;
    }

    @Override
    public int hashCode(){
        return nameHash;
    }
}
