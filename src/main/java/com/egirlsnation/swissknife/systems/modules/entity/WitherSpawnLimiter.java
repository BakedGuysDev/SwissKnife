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

package com.egirlsnation.swissknife.systems.modules.entity;

import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class WitherSpawnLimiter extends Module {

    public WitherSpawnLimiter() {
        super(Categories.Entity,"wither-spawn-limiter", "Limits spawning withers at spawn");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> radius = sgGeneral.add(new IntSetting.Builder()
            .name("radius")
            .description("The spawn radius in which players can't spawn withers")
            .defaultValue(3000)
            .build()
    );

    private final Setting<Boolean> alertPlayers = sgGeneral.add(new BoolSetting.Builder()
            .name("alert-players")
            .description("Whether to send message to players when attempting to spawn withers")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder()
            .name("message")
            .description("The message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "You cannot spawn withers this close to spawn")
            .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log cancelled wither spawn attempts")
            .defaultValue(false)
            .build()
    );

    @EventHandler
    private void EntitySpawn(CreatureSpawnEvent e){
        if(!isEnabled()) return;

        if(e.getEntityType() == EntityType.WITHER){
            if(LocationUtil.isInSpawnRadius(e.getLocation().getX(),e.getLocation().getZ(), radius.get())){
                e.setCancelled(true);
                if(!alertPlayers.get()) return;
                for(Entity entity : e.getEntity().getNearbyEntities(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ())){
                    if(entity instanceof Player){
                        sendMessage((Player) entity, ChatColor.translateAlternateColorCodes('§', message.get()));
                    }
                }
                if(log.get()){
                    info("Cancelled wither spawn at: " + LocationUtil.getLocationString(e.getLocation()));
                }
            }
        }
    }
}
