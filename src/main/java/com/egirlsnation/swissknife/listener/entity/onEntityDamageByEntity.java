/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.listener.entity;

import com.egirlsnation.swissknife.util.CombatCheck;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;

public class onEntityDamageByEntity implements Listener {

    private final CombatCheck combatCheck = new CombatCheck();

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            if (e.getEntity().getPassengers().contains(player)) {
                e.setCancelled(true);
                return;
            }
            if (preventHighDmg) {
                if (player.isOp()) return;
                if (e.getDamage() > highDmgThreshold) {
                    e.setCancelled(true);
                    player.damage(e.getDamage());
                    if(kickOnHighDamage){
                        player.kickPlayer("Oi cunt, what the bloody hell");
                    }
                }
            }
        }

        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        EntityDamageEvent.DamageCause cause = e.getCause();
        Entity damager = e.getDamager();


        //Checking if the damage is caused by an explosion from tnt or end crystal (Anchor check in
        if (cause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            if (!damager.getType().equals(EntityType.ENDER_CRYSTAL) && !damager.getType().equals(EntityType.PRIMED_TNT) && !damager.getType().equals(EntityType.SNOWBALL))
                return;
            combatCheck.addToCombatMap(player);
            return;
        }

        //Checking if the damage
        if (cause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || cause.equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            if (!(damager instanceof Player)) return;
            combatCheck.addToCombatMap(player);
            return;
        }

        if (cause.equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            Projectile projectile = (Projectile) damager;
            if (!(projectile.getShooter() instanceof Player)) return;
            combatCheck.addToCombatMap(player);
        }


    }
}
