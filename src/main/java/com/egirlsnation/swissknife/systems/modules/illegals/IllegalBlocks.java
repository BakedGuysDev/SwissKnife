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

package com.egirlsnation.swissknife.systems.modules.illegals;

import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
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

    private final Setting<Boolean> alertPlayers = sgGeneral.add(new BoolSetting.Builder()
            .name("alert-players")
            .description("Illegal item found. This incident will be reported")
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

        //TODO: Check if this rewrite actually works
        //TODO: Check if it allows a bypass with spoofing a different block being held
        String materialString1 = String.valueOf(illegalBlocks.get().stream().filter(materialString -> e.getBlock().getType().equals(Material.getMaterial(materialString))).findFirst());

        if(materialString1 != null){
            //Additional check if the block was held to allow filling out end portals
            if(e.getItemInHand().getType().equals(Material.getMaterial(materialString1))){
                e.setCancelled(true);
                if(alertPlayers.get()){
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message.get()));
                }
                if(log.get()){
                    info("Prevented " + e.getPlayer().getName() + " from placing an illegal block ( " + materialString1 + " )" );
                }
            }
        }

        /*
        OldConfig.instance.illegalBlockList.forEach(block -> {
            if(e.getBlock().getType().equals(Material.getMaterial(block))){

                if(e.getItemInHand().getType().equals(Material.getMaterial(block))){
                    e.setCancelled(true);

                }
            }
        });
        */
    }
}
