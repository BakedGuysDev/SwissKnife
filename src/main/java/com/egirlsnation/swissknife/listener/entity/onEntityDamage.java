package com.egirlsnation.swissknife.listener.entity;

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
    private void EntityDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Item){
            Item item = (Item) e.getEntity();
            if(!item.getItemStack().getType().equals(Material.PLAYER_HEAD)) return;
            if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)){
                e.setCancelled(true);
                return;
            }
        }

        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity pet = (LivingEntity) e.getEntity();
            if (pet.getHealth() - e.getDamage() <= 0) {
                if ((pet instanceof Tameable) && (pet instanceof Sittable)) {
                    if ((((Tameable) pet).isTamed())) {
                        if (((Tameable) pet).getOwner() instanceof Player) {
                            Player p = (Player) ((Tameable) pet).getOwner();
                            if (p != null) {
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
}
