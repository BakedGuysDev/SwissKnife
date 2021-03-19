package com.egirlsnation.swissknife.listeners;

import com.google.common.base.CharMatcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static com.egirlsnation.swissknife.service.illegalsPatchService.*;
import static com.egirlsnation.swissknife.swissKnife.allDisabled;

public class onInventoryOpen implements Listener {




    @EventHandler
    public void InventoryOpenEvent(InventoryOpenEvent e){
        ItemStack[] inv = e.getInventory().getContents();
        ItemStack i = new ItemStack(Material.NETHERITE_SWORD);
        ItemStack i2 = new ItemStack(Material.DIAMOND_SWORD);
        for(ItemStack item:inv){
            if(item != null){
                if(echantLevelCheck(item)){
                    item.setAmount(0);
                    e.getPlayer().sendMessage(ChatColor.RED + "No overenchanted items for you m8.");
                }
                if(illegalItemLoreCheck(item)){
                    item.setAmount(0);
                    e.getPlayer().sendMessage(ChatColor.RED + "Next time .peek first m8.");
                }


                if(!allDisabled){
                    if(item.getType() == Material.TOTEM_OF_UNDYING){
                        if(item.getAmount() > 2){
                            item.setAmount(2);
                        }
                    }
                }

                if(!allDisabled){
                    if(item.getType().equals(Material.END_PORTAL_FRAME)){
                        item.setAmount(0);
                    }
                }

                ItemMeta newMeta = ancientWeaponReduce(item);
                if(newMeta != null){
                    item.setItemMeta(newMeta);
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
