/*
 *
 *  * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 *  * Copyright (c) 2021 Egirls Nation Development
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the MIT License.
 *  *
 *  * You should have received a copy of the MIT
 *  License along with this program.  If not, see
 *  <https://opensource.org/licenses/MIT>.
 *
 */

package com.egirlsnation.swissknife.listener.player;

import com.egirlsnation.swissknife.SwissKnife;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;

public class onPlayerChat implements Listener {

    private final SwissKnife plugin;
    public onPlayerChat(SwissKnife plugin){
        this.plugin = plugin;
    }

    private final Random rng = new Random();

    @EventHandler
    public void PlayerChat(AsyncPlayerChatEvent e){
        if(enableShitlist && swapWordsRandomly && plugin.sqlQuery.isShitlisted(e.getPlayer())){
            if((rng.nextInt(100) + 1) > replaceChance) return;
            String[] words = e.getMessage().split(" ");
            words[rng.nextInt(words.length)] = replacementWords.get(rng.nextInt(replacementWords.size()));
            StringBuilder sb = new StringBuilder();
            for(String word : words){
                sb.append(word);
            }
            e.setMessage(sb.toString());
        }
    }
}