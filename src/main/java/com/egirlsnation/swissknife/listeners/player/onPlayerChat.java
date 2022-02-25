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

package com.egirlsnation.swissknife.listeners.player;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.utils.StringUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;

public class onPlayerChat implements Listener {

    private final SwissKnife plugin;
    public onPlayerChat(SwissKnife plugin){
        this.plugin = plugin;
    }

    private final Random rng = new Random();
    private final StringUtil stringUtils = new StringUtil();

    @EventHandler
    public void PlayerChat(AsyncPlayerChatEvent e){
        /*
        if(plugin.SQL.isConnected()){
            if(OldConfig.instance.enableShitlist && OldConfig.instance.swapWordsRandomly && plugin.playerStatsDriver.isShitlisted(e.getPlayer())){
                if((rng.nextInt(100) + 1) > OldConfig.instance.replaceChance) return;
                String[] words = e.getMessage().split(" ");
                words[rng.nextInt(words.length)] = OldConfig.instance.replacementWords.get(rng.nextInt(OldConfig.instance.replacementWords.size()));
                StringBuilder sb = new StringBuilder();
                for(String word : words){
                    sb.append(word);
                }
                e.setMessage(sb.toString());
            }
        }

        if(OldConfig.instance.greentext && e.getPlayer().hasPermission("swissknife.chat.greentext") && e.getMessage().charAt(0) == '>'){
            if(e.getPlayer().getName().equals("Lerbiq")){
                e.setMessage(ChatColor.LIGHT_PURPLE + e.getMessage());
            }
            e.setMessage(ChatColor.GREEN + e.getMessage());
        }

        if(OldConfig.instance.coordsEnabled){
            //e.setMessage(e.getMessage().replaceAll(OldConfig.instance.coordsPlaceholder, stringUtils.getCoordsPlaceholderFormatted(e.getPlayer())));
        }
         */
    }


}
