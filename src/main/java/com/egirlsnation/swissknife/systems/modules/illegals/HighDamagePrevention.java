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
import com.egirlsnation.swissknife.utils.entity.player.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HighDamagePrevention extends Module {
    public HighDamagePrevention(){
        super(Categories.Illegals, "high-damage-prevention", "Ancient 32k prevention since I wasn't sure if my 32k check is secure");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> threshold = sgGeneral.add(new IntSetting.Builder()
            .name("threshold")
            .description("The damage at which to cancel")
            .defaultValue(1000)
            .min(1)
            .build()
    );

    private final Setting<Boolean> redirect = sgGeneral.add(new BoolSetting.Builder()
            .name("redirect")
            .description("If the plugin should redirect the damage to the player who dealt it")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> kill = sgGeneral.add(new BoolSetting.Builder()
            .name("kill")
            .description("If the player should get killed when he bypasses the damage limit")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> kick = sgGeneral.add(new BoolSetting.Builder()
            .name("kick")
            .description("Kick the player if he deals damage over the threshold")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> kickMessage = sgGeneral.add(new StringSetting.Builder()
            .name("kick-message")
            .description("The kick message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "Ayo, what the hell")
            .build()
    );

    private final Setting<Boolean> bypass = sgGeneral.add(new BoolSetting.Builder()
            .name("bypass")
            .description("If the check can be bypassed by permissions")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log when player deals damage over the threshold")
            .defaultValue(false)
            .build()
    );

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        if(!isEnabled()) return;

        if(e.getDamager() instanceof Player){
            if(e.getDamager().hasPermission("swissknife.bypass.illegals") && bypass.get()){
                return;
            }

            Player player = (Player) e.getDamager();

            if(e.getDamage() > threshold.get()){
                e.setCancelled(true);
                if(redirect.get()){
                    player.damage(e.getFinalDamage());
                }
                if(kill.get()){
                    PlayerUtil.killPlayer(player);
                }
                if(kick.get()){
                    PlayerUtil.kickPlayer(player, ChatColor.translateAlternateColorCodes('§', kickMessage.get()));
                }
                if(log.get()){
                    info("Player " + player.getName() + " dealt damage over threshold");
                }
            }

        }
    }
}
