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

package com.egirlsnation.swissknife.systems.modules.illegals;

import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;
import java.util.List;

public class IllegalBlocks extends Module {
    public IllegalBlocks() {
        super(Categories.Illegals, "illegal-blocks", "Prevents players from placing illegal blocks");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<String>> illegalBlocks = sgGeneral.add(new StringListSetting.Builder()
            .name("illegal-block")
            .description("List of blocks to blacklist")
            .defaultValue(Arrays.asList("BEDROCK", "END_PORTAL_FRAME", "BARRIER", "STRUCTURE_BLOCK", "STRUCTURE_VOID"))
            .build()
    );

    private final Setting<Boolean> bypass = sgGeneral.add(new BoolSetting.Builder()
            .name("bypass")
            .description("If the check can be bypassed by permissions")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> alertPlayers = sgGeneral.add(new BoolSetting.Builder()
            .name("alert-players")
            .description("If the plugin should alert players when placing illegal block")
            .defaultValue(false)
            .build()
    );

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder()
            .name("message")
            .description("The message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "Illegal block found. This incident will be reported")
            .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log when player tries to place an illegal block")
            .defaultValue(false)
            .build()
    );


    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e){
        if(!isEnabled()) return;

        if(e.getPlayer().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }
        //TODO: Check if it allows a bypass with spoofing a different block being held

        for(String string : illegalBlocks.get()){
            if(e.getBlock().getType().equals(Material.getMaterial(string))){
                if(e.getItemInHand().getType().equals(Material.getMaterial(string))){
                    e.setCancelled(true);
                    e.getItemInHand().setAmount(0);
                    if(alertPlayers.get()){
                        sendMessage(e.getPlayer(), ChatColor.translateAlternateColorCodes('§', message.get()));
                    }
                    if(log.get()){
                        info("Prevented " + e.getPlayer().getName() + " from placing an illegal block ( " + string + " ) at: " + LocationUtil.getLocationString(e.getBlock().getLocation()));
                    }
                }
            }
        }

    }
}
