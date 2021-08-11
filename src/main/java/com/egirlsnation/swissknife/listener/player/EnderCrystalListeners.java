/*
 * This file is part of the SwissKnife plugin distibution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Meteor Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License, however this file
 * is licensed under the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * You should have received a copy of the MIT and GNU General Public
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>
 * and <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.egirlsnation.swissknife.listener.player;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.egirlsnation.swissknife.heads.HeadsHandler;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

// SwissKnife : Rename to EnderCrystalListeners from DeathMessageListener
public class EnderCrystalListeners implements Listener {

    private final Map<Player, EntityDamageEvent.DamageCause> lastDmgCause = new HashMap<>();
    private final Map<Player, Entity> lastAttacker = new HashMap<>();
    private final Map<EnderCrystal, Player> crystalExploder = new HashMap<>();

    private final HeadsHandler headsHandler = new HeadsHandler(); // SwissKnife


    @EventHandler
    private void onTick(ServerTickEndEvent e){
        crystalExploder.clear();
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent e){
        if(!(e.getEntity() instanceof  Player)) return;
        Player player = (Player) e.getEntity();

        lastDmgCause.put(player, e.getCause());
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof EnderCrystal && e.getDamager() instanceof Player){
            crystalExploder.put((EnderCrystal) e.getEntity(), (Player) e.getDamager());
        }

        // Damaging players
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();

        lastDmgCause.put(player, e.getCause());
        lastAttacker.put(player, e.getDamager());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e){
        lastDmgCause.remove(e.getPlayer());
        lastAttacker.remove(e.getPlayer());
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e){
        Player player = e.getEntity();

        EntityDamageEvent.DamageCause cause = lastDmgCause.get(player);
        Entity attacker = lastAttacker.get(player);

        // Killed by entity explosion
        if(cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
            Player exploder = null;
            // SwissKnife: Removed
            // String with = null;

            if(attacker instanceof EnderCrystal){
                exploder = crystalExploder.remove(attacker);
                // SwissKnife: Removed
                // with = ChatColor.GREEN + "End Crystal";
            }

            // Original: 'if (exploder != null) event.setDeathMessage(String.format("%s%s %swas nuked by %s%s %swith %s", ChatColor.GREEN, player.getName(), ChatColor.RED, ChatColor.GREEN, exploder.getName(), ChatColor.RED, with));
            if(exploder != null){
                // SwissKnife start

                //Player died by an end crystal. Exploder is who killed him. e.getEntity() is who died.

                if(exploder.equals(player)) return;

                Bukkit.getLogger().info("Adding one kill to player");
                exploder.setStatistic(Statistic.PLAYER_KILLS, (exploder.getStatistic(Statistic.PLAYER_KILLS) + 1));

                if(attacker.getCustomName() == null) return;
                if(attacker.getCustomName().equals("Draconite Crystal")){
                    Bukkit.getLogger().info("Crystal was Draconite");
                    headsHandler.dropHeadIfLucky(player, exploder);
                }
                //SwissKnife end
            }

            // SwissKnife: Removed
            // @EventHandler
            //    private void onBroadcastMessage(BroadcastMessageEvent event) {
            //        if (event.getMessage().endsWith("thebestplugin remove broadcast")) event.setCancelled(true);
            //    }
        }
    }
}
