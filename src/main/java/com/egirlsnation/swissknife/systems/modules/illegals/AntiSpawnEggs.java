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

import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.settings.StringSetting;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.entity.player.SwissPlayer;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AntiSpawnEggs extends Module {
    public AntiSpawnEggs() {
        super(Categories.Illegals,"anti-spawn-eggs", "Prevents players from using spawn eggs");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> bypass = sgGeneral.add(new BoolSetting.Builder()
            .name("bypass")
            .description("If the check can be bypassed by permissions")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> alertPlayers = sgGeneral.add(new BoolSetting.Builder()
            .name("alert-players")
            .description("If the plugin should alert player when they try to use a spawn egg")
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
        if(ItemUtil.isSpawnEgg(e.getItem())){
            e.setItem(ItemUtil.getReplacementItem());
        }
    }

    @EventHandler
    private void onPlayerInteractEvent(PlayerInteractEvent e) {
        if(!isEnabled()) return;
        if (e.getClickedBlock() != null && ItemUtil.isSpawnEgg(e.getItem())) {
            if(e.getPlayer().hasPermission("swissknife.bypass.illegals") && bypass.get()){
                return;
            }
            e.getItem().setAmount(0);
            e.setCancelled(true);
            if(alertPlayers.get() && SwissPlayer.getSwissPlayer(e.getPlayer()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                sendMessage(e.getPlayer(), ChatColor.translateAlternateColorCodes('§', message.get()));
            }
            if(log.get()){
                info("Prevented " + e.getPlayer().getName() + " from using a spawn egg");
            }
        }
    }
}
