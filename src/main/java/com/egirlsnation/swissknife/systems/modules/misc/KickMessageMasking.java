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

package com.egirlsnation.swissknife.systems.modules.misc;

import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.settings.StringSetting;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class KickMessageMasking extends Module {
    public KickMessageMasking(){
        super(Categories.Misc, "kick-message-masking", "Masks kick messages to your specified one (SwissKnife kick messages override)");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> kickMsg = sgGeneral.add(new StringSetting.Builder()
            .name("message")
            .description("The message to display (supports color codes)")
            .defaultValue(ChatColor.GOLD +  "Lost connection to the server")
            .build()
    );

    @EventHandler
    private void playerQuit(PlayerQuitEvent e){
        if(!isEnabled()) return;

        if(e.getReason().equals(PlayerQuitEvent.QuitReason.KICKED)){
            if(e.getQuitMessage() == null || e.getQuitMessage().isBlank()){
                if(e.getQuitMessage().startsWith("Swissknife ")){
                    e.setQuitMessage(e.getQuitMessage().replaceAll("Swissknife ", ""));
                    return;
                }
                e.setQuitMessage(kickMsg.get());
            }

        }
    }
}
