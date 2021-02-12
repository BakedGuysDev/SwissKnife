package com.egirlsnation.swissknife.listeners;

import com.google.common.base.CharMatcher;
import org.bukkit.*;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

import static com.egirlsnation.swissknife.service.respawnLocationService.randomLocation;


public class onJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        long pt = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
        if(!offlinePlayer.hasPlayedBefore()){
            e.setJoinMessage(ChatColor.BLUE + e.getPlayer().getName() + ChatColor.GOLD + " joined for the first time. Give them a welcome retards.");
            player.teleport(randomLocation(player.getWorld()));
        }

        if((pt >= 2160000) && !(player.hasPermission("egirls.rank.vet"))){
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String playerName = player.getName();
            String command = "lp user " + playerName + " parent add veteran";
            Bukkit.dispatchCommand(console, command);
            String broadcast = playerName + " reached MidFag!";
            Bukkit.dispatchCommand(console, broadcast);
        }
        if((pt >= 17280000) && !(player.hasPermission("egirls.rank.oldfag"))){
            String playerName = player.getName();
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String command = "lp user " + playerName + " parent add oldfag";
            Bukkit.dispatchCommand(console, command);
            String broadcast = playerName + " reached OldFag!";
            Bukkit.dispatchCommand(console, broadcast);
        }

        ItemStack[] inv = player.getInventory().getContents();
        ItemStack i = new ItemStack(Material.NETHERITE_SWORD);
        ItemStack i2 = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack[] echest = player.getEnderChest().getContents();
        for(ItemStack item:inv){
            if(item != null){
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
                                player.sendMessage(ChatColor.RED + "Your book contained non-ascii characters and was removed.");
                                item.setAmount(0);
                                Bukkit.getLogger().warning("User " + player.getName() + " joined with a book with non-ascii characters at coords: "
                                        + player.getLocation());
                            }
                        }
                    }
                }

                if(item.getType().equals(i.getType()) || item.getType().equals(i2.getType())){
                    if(item.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)){
                        if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) >= 20){
                            item.setAmount(0);
                            e.getPlayer().sendMessage(ChatColor.RED + "No 32ks for you m8.");
                        }
                    }
                    if(item.getItemMeta() != null){
                        if(item.getItemMeta().hasLore()){
                            if(item.getItemMeta().getLore() != null){
                                if(item.getItemMeta().getLore().contains("§9§lBig Dick Energy X")){
                                    e.getPlayer().sendMessage(ChatColor.RED + "No illegal effect item for you m8.");
                                    item.setAmount(0);
                                }
                            }
                        }
                    }
                }
            }
        }

        /*
        if(player.isOp()){
             String playerName = player.getName();
             if((playerName != "Lerbiq") || (playerName != "killmlana2020")){
                 ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                 String command = "deop " + playerName;
                 Bukkit.dispatchCommand(console, command);
             }
         }
        */


    }
}
