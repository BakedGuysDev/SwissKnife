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

package com.egirlsnation.swissknife.systems.modules;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Categories {
    public static final Category Entity = new Category("Entity", new ItemStack(Material.ZOMBIE_HEAD));
    public static final Category Illegals = new Category("Illegals", new ItemStack(Material.BEDROCK));
    public static final Category Player = new Category("Player", new ItemStack(Material.PLAYER_HEAD));
    public static final Category World = new Category("World", new ItemStack(Material.GRASS_BLOCK));
    public static final Category Misc = new Category("Misc", new ItemStack(Material.POPPY));
    public static final Category EgirlsNation = new Category("EgirlsNation", new ItemStack(Material.BLAZE_POWDER));

    public static boolean REGISTERING;

    public static void init(){
        REGISTERING = true;

        Modules.registerCategory(Entity);
        Modules.registerCategory(Illegals);
        Modules.registerCategory(Player);
        Modules.registerCategory(World);
        Modules.registerCategory(Misc);
        Modules.registerCategory(EgirlsNation);

        REGISTERING = false;
    }
}