package com.egirlsnation.swissknife.listeners;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class onEntityDamage implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDamageEvent e) {
        LivingEntity pet = (LivingEntity) e.getEntity();
        if (pet.getHealth() - e.getDamage() <= 0) { //Bukkit.broadcastMessage("pet has died");
            if ((pet instanceof Tameable) && (pet instanceof Sittable)) { //Bukkit.broadcastMessage("pet is sittable");
                if ((((Tameable) pet).isTamed())) { //Bukkit.broadcastMessage("pet's is tamed");
                    if (((Tameable) pet).getOwner() instanceof Player) {
                        Player p = (Player) ((Tameable) pet).getOwner();
                        if (p != null) {
                            //Bukkit.broadcastMessage("pet's owner is online");
                            if ((p.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING))) { //Bukkit.broadcastMessage("owner has totem in offhand");
                                p.getInventory().getItemInOffHand().setAmount((p.getInventory().getItemInOffHand().getAmount() - 1));
                                p.playEffect(EntityEffect.TOTEM_RESURRECT);
                                p.sendMessage(ChatColor.AQUA + "Your pet used a Totem.");
                                e.setCancelled(true);
                                pet.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 900, 2));
                                pet.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 1));
                                pet.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 2));
                            }
                        }
                    }
                }
            }
        }
    }
}
