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

package com.egirlsnation.swissknife.systems.modules.illegals;

import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.settings.StringSetting;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.IllegalItemsUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AntiSpawnEggs extends Module {
    public AntiSpawnEggs() {
        super(Categories.Illegals,"anti-spawn-eggs", "Prevents players from using spawn eggs");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> opBypass = sgGeneral.add(new BoolSetting.Builder()
            .name("op-bypass")
            .description("Whether to allow operators to bypass the module checks")
            .defaultValue(false)
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
            .defaultValue(ChatColor.RED + "Illegal item found. This incident will be reported")
            .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log when preventing player the use of a spawn egg")
            .defaultValue(false)
            .build()
    );

    @EventHandler
    private void onBlockDispenseEvent(BlockDispenseEvent e){
        if(!isEnabled()) return;
        if(IllegalItemsUtil.isSpawnEgg(e.getItem())){
            e.setItem(IllegalItemsUtil.getReplacementItem());
        }
    }

    @EventHandler
    private void onPlayerInteractEvent(PlayerInteractEvent e) {
        if(!isEnabled()) return;
        if (e.getClickedBlock() != null && IllegalItemsUtil.isSpawnEgg(e.getItem())) {
            e.getItem().setAmount(0);
            e.setCancelled(true);
            if(alertPlayers.get()){
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message.get()));
            }
            if(log.get()){
                info("Prevented " + e.getPlayer().getName() + " from using a spawn egg");
            }
        }
    }
}
