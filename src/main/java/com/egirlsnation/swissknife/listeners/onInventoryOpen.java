package com.egirlsnation.swissknife.listeners;

import com.google.common.base.CharMatcher;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class onInventoryOpen implements Listener {




    @EventHandler
    public void InventoryOpenEvent(InventoryOpenEvent e){
        ItemStack[] inv = e.getInventory().getContents();
        ItemStack i = new ItemStack(Material.NETHERITE_SWORD);
        ItemStack i2 = new ItemStack(Material.DIAMOND_SWORD);
        for(ItemStack item:inv){
            if(item != null){
                if(item.getType().equals(i.getType()) || item.getType().equals(i2.getType())){
                    if(item.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)){
                        if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) >= 20){
                            item.setAmount(0);
                            e.getPlayer().sendMessage(ChatColor.RED + "No 32ks for you m8.");
                        }
                    }
                    if (item.getItemMeta() != null){
                        if(item.getItemMeta().hasLore()){
                            if(item.getItemMeta().getLore().contains("§9§lBig Dick Energy X")){
                                e.getPlayer().sendMessage(ChatColor.RED + "No illegal effect item for you m8.");
                                item.setAmount(0);
                            }
                        }
                    }
                }
                if(item.getType() == Material.TOTEM_OF_UNDYING){
                    if(item.getAmount() > 2){
                        item.setAmount(2);
                    }
                }
                if(item.getType() == Material.WRITTEN_BOOK || item.getType() == Material.WRITABLE_BOOK){
                    BookMeta meta = (BookMeta) item.getItemMeta();

                    if(meta != null){
                        List<String> previousText = meta.getPages();
                        for(String string : previousText){
                            if(!CharMatcher.ascii().matchesAllOf(string)){
                                e.getPlayer().sendMessage(ChatColor.RED + "Your book contained non-ascii characters and was removed.");
                                Bukkit.getLogger().warning("User " + e.getPlayer().getName() + " opened an inventory with book/s with non-ascii characters at coords: "
                                        + e.getPlayer().getLocation());
                                item.setAmount(0);
                            }
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
