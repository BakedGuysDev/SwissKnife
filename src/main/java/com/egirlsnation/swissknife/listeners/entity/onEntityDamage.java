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

package com.egirlsnation.swissknife.listeners.entity;

import com.egirlsnation.swissknife.utils.OldConfig;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class onEntityDamage implements Listener {

    @EventHandler
    private void EntityDamage(EntityDamageEvent e) {
        //Heads
        if (e.getEntity() instanceof Item) {
            Item item = (Item) e.getEntity();
            if (!item.getItemStack().getType().equals(Material.PLAYER_HEAD)) return;
            if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                e.setCancelled(true);
                return;
            }
        }

        if(OldConfig.instance.fixDragonDeath && e.getEntity().getType().equals(EntityType.ENDER_DRAGON)){
            if((e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))){
                LivingEntity dragon = (LivingEntity) e.getEntity();
                if(dragon.getHealth() > OldConfig.instance.dragonHealth) return;
                e.setCancelled(true);
            }
        }

        //Pet totems
        if(OldConfig.instance.petsUseTotems){
            if (!(e.getEntity() instanceof Tameable)) return;
            Tameable pet = (Tameable) e.getEntity();
            if ((pet.getHealth() - e.getDamage()) > 0) return;
            if (!pet.isTamed()) return;
            if (!(pet.getOwner() instanceof Player)) return;
            if (pet.getOwner() == null) return;
            Player p = (Player) pet.getOwner();
            if (!p.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) return;
            e.setCancelled(true);
            pet.playEffect(EntityEffect.TOTEM_RESURRECT);
            p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
            p.sendMessage(ChatColor.AQUA + "Your pet, " + getPetName(pet) + ", used a totem." );
            e.setCancelled(true);
            pet.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 900, 2));
            pet.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 1));
            pet.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 2));
        }


    }

    public String getPetName(Tameable pet){
        if(pet.getCustomName() == null){
           return pet.getName();
        }
        return pet.getCustomName();
    }
}

