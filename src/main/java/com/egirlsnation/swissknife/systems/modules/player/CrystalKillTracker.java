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

package com.egirlsnation.swissknife.systems.modules.player;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.egirlsnation.swissknife.events.PlayerCrystalKillEvent;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.Statistic;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class CrystalKillTracker extends Module {

    public CrystalKillTracker() {
        super(Categories.Player, "crystal-kill-tracker", "Tracks kills with ender crystals and adds those into statistics");
    }

    private final Map<Player, EntityDamageEvent.DamageCause> lastDmgCause = new HashMap<>();
    private final Map<Player, Entity> lastAttacker = new HashMap<>();
    private final Map<EnderCrystal, Player> crystalExploder = new HashMap<>();


    @EventHandler
    private void onTick(ServerTickEndEvent e){
        crystalExploder.clear();
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent e){
        if(!isEnabled()) return;
        if(!(e.getEntity() instanceof  Player)) return;
        lastDmgCause.put((Player) e.getEntity(), e.getCause());
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        if(!isEnabled()) return;
        if(e.getEntity() instanceof EnderCrystal && e.getDamager() instanceof Player){
            crystalExploder.put((EnderCrystal) e.getEntity(), (Player) e.getDamager());
        }

        // Damaging players
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();

        lastDmgCause.put(player, e.getCause());
        lastAttacker.put(player, e.getDamager());

        /*
        if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)){

            if(e.getDamager() instanceof EnderCrystal){
                if(player.isDead()) return;
                Player exploder = crystalExploder.remove((EnderCrystal) e.getDamager());
                if(combatCheck.getElytraMap().containsKey(exploder.getUniqueId())){
                    combatCheck.getElytraMap().get(exploder.getUniqueId()).cancel();
                    exploder.setGliding(false);
                }
                if(combatCheck.getElytraMap().containsKey(player.getUniqueId())){
                    combatCheck.getElytraMap().get(player.getUniqueId()).cancel();
                    exploder.setGliding(false);
                }
                BukkitTask taskExploder = Bukkit.getScheduler().runTaskLater(plugin, () -> combatCheck.getElytraMap().remove(exploder.getUniqueId()), 100);
                BukkitTask taskVictim = Bukkit.getScheduler().runTaskLater(plugin, () -> combatCheck.getElytraMap().remove(player.getUniqueId()), 100);
                combatCheck.getElytraMap().put(exploder.getUniqueId(), taskExploder);
                combatCheck.getElytraMap().put(player.getUniqueId(), taskVictim);
            }
        }
         */
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e){
        lastDmgCause.remove(e.getPlayer());
        lastAttacker.remove(e.getPlayer());
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e){
        if(!isEnabled()) return;
        Player player = e.getEntity();

        EntityDamageEvent.DamageCause cause = lastDmgCause.get(player);
        Entity attacker = lastAttacker.get(player);

        if(cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
            Player exploder = null;

            if(attacker instanceof EnderCrystal){
                exploder = crystalExploder.remove(attacker);
            }

            if(exploder != null){
                exploder.setStatistic(Statistic.PLAYER_KILLS, (exploder.getStatistic(Statistic.PLAYER_KILLS) + 1));

                PlayerCrystalKillEvent event = new PlayerCrystalKillEvent(exploder, player, attacker);
            }
        }
    }
}
