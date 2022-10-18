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

package com.egirlsnation.swissknife;

import com.egirlsnation.swissknife.systems.Systems;
import com.egirlsnation.swissknife.systems.commands.Commands;
import com.egirlsnation.swissknife.systems.config.Config;
import com.egirlsnation.swissknife.systems.internal.SwissPlayerRegistry;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Modules;
import com.egirlsnation.swissknife.systems.modules.egirls.DraconiteItems;
import com.egirlsnation.swissknife.utils.SwissLogger;
import com.egirlsnation.swissknife.utils.server.ServerUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SwissKnife extends JavaPlugin {

    public static SwissKnife INSTANCE;
    public static SwissLogger swissLogger;

    @Override
    public void onEnable() {
        if(INSTANCE == null){
            INSTANCE = this;
        }

        if(swissLogger == null){
            swissLogger = new SwissLogger();
        }

        swissLogger.info("Initializing SwissKnife");

        Systems.addPreLoadTask(() -> {
            Bukkit.getPluginManager().registerEvents(new SwissPlayerRegistry(), this);
        });

        Systems.addPostLoadTask(() -> {
            //MySQL.get().initDatabase();
            Modules.get().get(DraconiteItems.class).registerRecipes();
            if(!Config.get().disableMetrics){
                new Metrics(this, 14670);
                swissLogger.info("Metrics loaded. Thank you :)");
            }
        });

        ServerUtil.init();

        Categories.init();

        Systems.init();

        Modules.get().sortModules();

        Systems.load();

        Modules.get().loadDbDependant();
        Commands.get().loadDbDependant();
    }

    @Override
    public void onDisable() {
        Systems.save();
        swissLogger.info("Swiss Knife plugin disabled.");
    }

}
