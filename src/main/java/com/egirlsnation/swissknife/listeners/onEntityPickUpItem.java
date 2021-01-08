package com.egirlsnation.swissknife.listeners;

import com.egirlsnation.swissknife.swissKnife;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class onEntityPickUpItem implements Listener {
    private swissKnife swissknife = swissKnife.getInstance();
    Player player;
    UUID playerUUID;

    @EventHandler
    public void onEntityPickUpItem(EntityPickupItemEvent e){
        if(e.getEntity() instanceof Player){
            player = ((Player) e.getEntity()).getPlayer();
            playerUUID = player.getUniqueId();
            if(swissKnife.itemPickUpUsers.get(playerUUID) != null) {
                if (swissKnife.itemPickUpUsers.get(playerUUID)) {
                    e.setCancelled(true);
                }
            }
            ItemStack  itemStack = e.getItem().getItemStack();
            ItemStack i = new ItemStack(Material.NETHERITE_SWORD);
            if(itemStack != null){
                if(itemStack.getType().equals(i.getType())){
                    if(itemStack.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)){
                        if(itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL) >= 20){
                            player.getPlayer().sendMessage(ChatColor.RED + "No 32ks for you m8.");
                            e.getItem().remove();
                            e.setCancelled(true);
                        }
                    }
                }
            }

        }
    }
}
