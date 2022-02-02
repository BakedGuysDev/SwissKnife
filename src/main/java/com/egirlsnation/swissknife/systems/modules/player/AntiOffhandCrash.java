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

package com.egirlsnation.swissknife.systems.modules.player;

import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.HashMap;

public class AntiOffhandCrash extends Module {
    public AntiOffhandCrash(){
        super(Categories.Player, "anti-offhand-crash", "Prevents players from doing the offhand crash");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
            .name("delay")
            .description("The hand swap delay in milliseconds")
            .defaultValue(200)
            .range(0, 120)
            .build()
    );

    private final Setting<Boolean> kick = sgGeneral.add(new BoolSetting.Builder()
            .name("kick")
            .description("If the plugin should kick the player when he does the offhand crash")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> kickMessage = sgGeneral.add(new StringSetting.Builder()
            .name("message")
            .description("The kick message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "Offhand crash? Or should I say offhand kick?")
            .build()
    );

    private final HashMap<Player, Long> handSwapDelay = new HashMap<>();

    @EventHandler
    private void SwapHandItems(PlayerSwapHandItemsEvent e){
        if(!isEnabled()) return;
        if(!handSwapDelay.containsKey(e.getPlayer())){
            handSwapDelay.put(e.getPlayer(), 0L);
        }
        if(System.currentTimeMillis() < handSwapDelay.get(e.getPlayer())){
            e.setCancelled(true);
            if(kick.get()){
                e.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('§', kickMessage.get()));
            }
        }else{
            handSwapDelay.put(e.getPlayer(), System.currentTimeMillis() + delay.get());
        }
    }
}
