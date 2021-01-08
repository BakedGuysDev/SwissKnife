package com.egirlsnation.swissknife.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class onInventoryOpen implements Listener {




    @EventHandler
    public void InventoryOpenEvent(InventoryOpenEvent e){
        ItemStack[] inv = e.getInventory().getContents();
        ItemStack i = new ItemStack(Material.NETHERITE_SWORD);
        for(ItemStack item:inv){
            if(item != null){
                if(item.getType().equals(i.getType())){
                    if(item.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)){
                        if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) >= 20){
                            item.setAmount(0);
                            e.getPlayer().sendMessage(ChatColor.RED + "No 32ks for you m8.");
                        }
                    }
                }
            }
        }
    }

    /*public ItemStack getItemStack(){
        ItemStack itemStack = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }*/
}
