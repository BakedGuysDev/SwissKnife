package com.egirlsnation.swissknife.listeners;

import com.google.common.base.CharMatcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class onInventoryClick implements Listener {

    @EventHandler
    public void onHopper(InventoryClickEvent e){
        if(e.getClickedInventory() != null){
            ItemStack[] inv = e.getClickedInventory().getContents();
            ItemStack i = new ItemStack(Material.NETHERITE_SWORD);
            ItemStack i2 = new ItemStack(Material.DIAMOND_SWORD);
            ItemStack i3 = new ItemStack(Material.TOTEM_OF_UNDYING);
            ItemStack bedrock = new ItemStack(Material.BEDROCK);
            for(ItemStack item:inv){
                if(item != null){

                    if(item.getItemMeta() instanceof BlockStateMeta){
                        BlockStateMeta im = (BlockStateMeta) item.getItemMeta();
                        if(im.getBlockState() instanceof ShulkerBox){
                            ShulkerBox shulkerBox = (ShulkerBox) im.getBlockState();
                            ItemStack[] shulkerInv = shulkerBox.getInventory().getContents();
                            for(ItemStack shulkItem:shulkerInv){
                                if(shulkItem != null){
                                    if(shulkItem.getType() == Material.WRITTEN_BOOK || shulkItem.getType() == Material.WRITABLE_BOOK){
                                        BookMeta meta = (BookMeta) shulkItem.getItemMeta();

                                        if(meta != null){
                                            String author = meta.getAuthor();
                                            if(author != null){
                                                if(author.equals("rittenhouseincs") || author.equals("apes")){
                                                    ItemStack bookItemStack = new ItemStack(Material.WRITTEN_BOOK);
                                                    BookMeta newBookMeta = (BookMeta) bookItemStack.getItemMeta();
                                                    newBookMeta.setPages("Stop right there, criminal scum! Nobody breaks the law on my watch! I'm confiscating your stolen goods. Now pay your fine or it's off to jail.");
                                                    newBookMeta.setAuthor("OblivionGuard");
                                                    newBookMeta.setTitle(meta.getTitle());
                                                    bookItemStack.setItemMeta(newBookMeta);
                                                    e.setCurrentItem(bookItemStack);
                                                    Bukkit.getLogger().warning("Book written by apes or ritten detected in shulker. Canceling the event and terminating the shulker.");
                                                    Bukkit.getLogger().warning("Check the playerdata of: " + e.getWhoClicked().getName());
                                                    e.setCancelled(true);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if(item.getType().equals(bedrock.getType())){
                        item.setAmount(0);
                    }else if(item.getType().equals(i.getType()) || item.getType().equals(i2.getType())){
                        if(item.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)){
                            if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) >= 20){
                                item.setAmount(0);
                                e.setCancelled(true);
                            }
                        }
                    }
                    if(item.getItemMeta() != null){
                        if(item.hasItemMeta()){
                            if(item.getItemMeta().hasLore()){
                                if(item.getItemMeta().getLore().contains("§9§lBig Dick Energy X")){
                                    item.setAmount(0);
                                    Bukkit.getLogger().warning("User " + e.getWhoClicked().getName() + " clicked a book with non-ascii characters at coords: "
                                            + e.getWhoClicked().getLocation());
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }
                    if(item.getType() == Material.TOTEM_OF_UNDYING){
                        if(item.getAmount() > 2){
                            item.setAmount(2);
                            e.setCancelled(true);
                        }
                    }
                    if(item.getType() == Material.WRITTEN_BOOK || item.getType() == Material.WRITABLE_BOOK){
                        BookMeta meta = (BookMeta) item.getItemMeta();

                        if(meta != null){
                            List<String> previousText = meta.getPages();
                            for(String string : previousText){
                                if(!CharMatcher.ascii().matchesAllOf(string)){
                                    item.setAmount(0);
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
