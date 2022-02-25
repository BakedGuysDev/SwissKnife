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

package com.egirlsnation.swissknife.systems.modules.entity;

import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.entity.player.SwissPlayer;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PetTotems extends Module {
    public PetTotems(){
        super(Categories.Entity, "pet-totems", "Allows pets to use totems of their owner");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(!isEnabled()) return;

        if(!(e.getEntity() instanceof Tameable)) return;

        Tameable pet = (Tameable) e.getEntity();
        if((pet.getHealth() - e.getFinalDamage()) > 0) return;
        if(!pet.isTamed()) return;
        if(!(pet.getOwner() instanceof Player)) return;
        if(pet.getOwner() == null) return;

        Player p = (Player) pet.getOwner();
        SwissPlayer swissPlayer = SwissPlayer.getSwissPlayer(p);
        if(!swissPlayer.hasFeatureEnabled(SwissPlayer.SwissFeature.PET_TOTEMS)) return;
        if(!p.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) return;
        e.setCancelled(true);

        pet.playEffect(EntityEffect.TOTEM_RESURRECT);
        p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
        p.sendMessage(ChatColor.AQUA + "Your pet, " + getPetName(pet) + ", used a totem.");
        e.setCancelled(true);

        pet.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 900, 2));
        pet.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 1));
        pet.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 2));
    }

    public String getPetName(Tameable pet){
        if(pet.getCustomName() == null){
            return pet.getName();
        }
        return pet.getCustomName();
    }
}
