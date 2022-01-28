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

import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.OldConfig;
import com.egirlsnation.swissknife.utils.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatTweaks extends Module {
    public ChatTweaks() {
        super(Categories.Misc, "chat-tweaks", "Adds minor features to chat");
    }

    @EventHandler
    public void PlayerChat(AsyncPlayerChatEvent e){
        if(!isEnabled()) return;
        if(OldConfig.instance.greentext && e.getPlayer().hasPermission("swissknife.chat.greentext") && e.getMessage().charAt(0) == '>'){
            if(e.getPlayer().getName().equals("Lerbiq")){
                e.setMessage(ChatColor.LIGHT_PURPLE + e.getMessage());
            }
            e.setMessage(ChatColor.GREEN + e.getMessage());
        }

        if(OldConfig.instance.coordsEnabled){
            e.setMessage(e.getMessage().replaceAll(OldConfig.instance.coordsPlaceholder, StringUtil.getCoordsPlaceholderFormatted(e.getPlayer())));
        }
    }
}
