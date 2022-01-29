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

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.OldConfig;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CombatCheck extends Module {
    public CombatCheck() {
        super(Categories.Player, "combat-check", "Combat check for various situations");
    }

    private static final Map<UUID, Long> combatMap = new HashMap<>();
    private static final Map<UUID, Long> elytraMap = new HashMap<>();


    //TODO: Config options


    @EventHandler
    private void onEntityDamageByBlockEvent(EntityDamageByBlockEvent e){
        if(!isEnabled()) return;
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        EntityDamageEvent.DamageCause cause = e.getCause();

        if(e.getDamager() == null){
            SwissKnife.swissLogger.debug("Damager null");
            return;
        }

        //TODO: Improve anchor check
        if(cause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) && e.getDamager().getType().equals(Material.RESPAWN_ANCHOR)){
            addToCombatMap(player);
        }
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if(!isEnabled()) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        EntityDamageEvent.DamageCause cause = e.getCause();
        Entity damager = e.getDamager();


        //TODO: Improve crystal and tnt check
        //Checking if the damage is caused by an explosion from tnt or end crystal
        if (cause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            if (!damager.getType().equals(EntityType.ENDER_CRYSTAL) && !damager.getType().equals(EntityType.PRIMED_TNT) && !damager.getType().equals(EntityType.SNOWBALL))
                return;
            addToCombatMap(player);
            return;
        }

        //Checking if the damage is caused by player melee attacks
        if (cause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || cause.equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            if (!(damager instanceof Player)) return;
            addToCombatMap(player);
            addToElytraMap(player);
            return;
        }

        if (cause.equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            Projectile projectile = (Projectile) damager;
            if (!(projectile.getShooter() instanceof Player)) return;
            addToCombatMap(player);
            addToElytraMap(player);
        }

    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e){
        if(!isEnabled()) return;
        combatMap.remove(e.getPlayer().getUniqueId());
        elytraMap.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e){
        if(!isEnabled()) return;
        combatMap.remove(e.getEntity().getUniqueId());
        elytraMap.remove(e.getEntity().getUniqueId());
    }


    public void addToCombatMap(Player player){
        combatMap.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void addToElytraMap(Player player){
        elytraMap.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public boolean isInCombat(Player player){
        UUID playerUUID = player.getUniqueId();
        if(!combatMap.containsKey(playerUUID)) return false;

        long timeDifference = System.currentTimeMillis() - combatMap.get(playerUUID);
        if(timeDifference >= OldConfig.instance.combatTimeout){
            combatMap.remove(playerUUID);
            return false;
        }else{
            return true;
        }
    }

    public boolean hasElytraDisabled(Player player){
        UUID playerUUID = player.getUniqueId();
        if(!elytraMap.containsKey(playerUUID)) return false;

        long timeDifference = System.currentTimeMillis() - elytraMap.get(playerUUID);
        if(timeDifference >= OldConfig.instance.elytraTimeout){
            elytraMap.remove(playerUUID);
            return false;
        }else{
            return true;
        }
    }

    public long getRemainingCombatTime(Player player){
        UUID playerUUID = player.getUniqueId();
        if(!combatMap.containsKey(playerUUID)) return 0;

        long timeDifference = System.currentTimeMillis() - combatMap.get(playerUUID);
        if(timeDifference >= OldConfig.instance.combatTimeout) return 0;

        return TimeUnit.MILLISECONDS.toSeconds(OldConfig.instance.combatTimeout -timeDifference);
    }

    public long getRemainingElytraTime(Player player){
        UUID playerUUID = player.getUniqueId();
        if(!elytraMap.containsKey(playerUUID)) return 0;

        long timeDifference = System.currentTimeMillis() - elytraMap.get(playerUUID);
        if(timeDifference >= OldConfig.instance.elytraTimeout) return 0;

        return TimeUnit.MILLISECONDS.toSeconds(OldConfig.instance.elytraTimeout -timeDifference);
    }


}
