package com.egirlsnation.swissknife.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class onInventoryClick implements Listener {

    @EventHandler
    public void onHopper(InventoryClickEvent e){
        ItemStack[] inv = e.getInventory().getContents();
        ItemStack i = new ItemStack(Material.NETHERITE_SWORD);
        ItemStack bedrock = new ItemStack(Material.BEDROCK);
        for(ItemStack item:inv){
            if(item != null){
                if(item.getType().equals(bedrock.getType())){
                    item.setAmount(0);
                }else if(item.getType().equals(i.getType())){
                    if(item.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)){
                        if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) >= 20){
                            item.setAmount(0);
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
