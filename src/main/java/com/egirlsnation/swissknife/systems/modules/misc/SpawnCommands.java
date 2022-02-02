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

package com.egirlsnation.swissknife.systems.modules.misc;

import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

public class SpawnCommands extends Module {
    public SpawnCommands(){
        super(Categories.Misc, "spawn-commands", "Restricts the usage of configured commands at spawn");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<String>> commandList = sgGeneral.add(new StringListSetting.Builder()
            .name("commands-list")
            .description("List of commands that will be disabled at spawn")
            .defaultValue(Arrays.asList("tpa", "tpahere", "tpayes", "tpaccept", "tpaaccept", "tpno", "tpano", "tpdeny", "tpadeny", "tpyes"))
            .build()
    );

    private final Setting<Integer> radius = sgGeneral.add(new IntSetting.Builder()
            .name("spawn-radius")
            .description("The radius in which the commands should be disabled")
            .defaultValue(2000)
            .min(1)
            .build()
    );

    private final Setting<Boolean> alertPlayers = sgGeneral.add(new BoolSetting.Builder()
            .name("alert-players")
            .description("If the plugin should alert players when it prevents a command")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder()
            .name("message")
            .description("The message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "You need to be further from spawn to use this command (%radius% blocks).")
            .build()
    );

    @EventHandler
    private void PlayerCommandPreProcess(PlayerCommandPreprocessEvent e){
        if(!isEnabled()) return;
        if(LocationUtil.isInSpawnRadius(e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getZ(), radius.get())){
            for(String command : commandList.get()){
                if(e.getMessage().toLowerCase().startsWith("/" + command.toLowerCase())){
                    e.setCancelled(true);
                    if(alertPlayers.get()){
                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('§', message.get().replaceAll("%radius%", radius.get().toString())));
                    }
                }
            }
        }
    }


}
