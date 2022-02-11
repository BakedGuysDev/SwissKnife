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

package com.egirlsnation.swissknife.systems.modules.player;

import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.settings.StringSetting;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;

public class BetterRespawn extends Module {
    public BetterRespawn(){
        super(Categories.Player, "better-respawn", "Implements better respawning mechanics");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> worldName = sgGeneral.add(new StringSetting.Builder()
            .name("default-world-name")
            .description("The name of the world players should respawn in")
            .defaultValue("world")
            .build()
    );

    private final Setting<Integer> spawnRadius = sgGeneral.add(new IntSetting.Builder()
            .name("spawn-radius")
            .description("The radius from spawn where players can respawn")
            .defaultValue(255)
            .build()
    );

    private final Setting<Integer> worldHeight = sgGeneral.add(new IntSetting.Builder()
            .name("highest-world-y")
            .description("The highest Y coordinate of the build limit")
            .defaultValue(255)
            .build()
    );

    private final Setting<Integer> worldDepth = sgGeneral.add(new IntSetting.Builder()
            .name("lowest-world-y")
            .description("The lowest Y coordinate of the build limit")
            .defaultValue(1)
            .build()
    );

    @EventHandler
    private void playerRespawn(PlayerRespawnEvent e){
        if(e.getPlayer().getBedSpawnLocation() != null) return;
        e.setRespawnLocation(LocationUtil.getRandomSpawnLocation(Bukkit.getWorld(worldName.get()), spawnRadius.get(), worldDepth.get(), worldHeight.get()));
    }
}
