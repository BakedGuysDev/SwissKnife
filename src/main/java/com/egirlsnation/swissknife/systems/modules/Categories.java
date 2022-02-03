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

public class Categories {
    public static final Category Entity = new Category("entity", new ItemStack(Material.ZOMBIE_HEAD));
    public static final Category Illegals = new Category("illegals", new ItemStack(Material.BEDROCK));
    public static final Category Player = new Category("player", new ItemStack(Material.PLAYER_HEAD));
    public static final Category World = new Category("world", new ItemStack(Material.GRASS_BLOCK));
    public static final Category Misc = new Category("misc", new ItemStack(Material.POPPY));
    public static final Category EgirlsNation = new Category("egirls-nation", new ItemStack(Material.BLAZE_POWDER));
    public static final Category Database = new Category("database", new ItemStack(Material.PAPER));

    public static boolean REGISTERING;

    public static void init(){
        REGISTERING = true;

        //Note to self: Registration order decides config order because yes
        Modules.registerCategory(Illegals);
        Modules.registerCategory(Entity);
        Modules.registerCategory(Player);
        Modules.registerCategory(World);
        Modules.registerCategory(Database);
        Modules.registerCategory(Misc);
        Modules.registerCategory(EgirlsNation);

        REGISTERING = false;
    }
}
