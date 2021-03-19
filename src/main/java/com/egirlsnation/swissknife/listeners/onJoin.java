package com.egirlsnation.swissknife.listeners;

import com.egirlsnation.swissknife.swissKnife;
import com.google.common.base.CharMatcher;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

import static com.egirlsnation.swissknife.service.illegalsPatchService.*;
import static com.egirlsnation.swissknife.service.respawnLocationService.randomLocation;
import static com.egirlsnation.swissknife.swissKnife.allDisabled;


public class onJoin implements Listener {

    public swissKnife plugin;
    public onJoin(swissKnife instance){
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoinEvent(PlayerJoinEvent e){
       Player player = e.getPlayer();
       UUID playerUUID = player.getUniqueId();

       if(plugin.SQL.isConnected()){
           plugin.sqlService.createPlayer(player);
       }


        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String playerName = player.getName();
        if(player.getGameMode().equals(GameMode.CREATIVE)){
            String commandFly = "fly " + playerName + " disable";
            Bukkit.dispatchCommand(console, commandFly);
        }

        long pt = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
        if(!offlinePlayer.hasPlayedBefore()){
            e.setJoinMessage(ChatColor.BLUE + e.getPlayer().getName() + ChatColor.GOLD + " joined for the first time. Give them a welcome retards.");
            player.teleport(randomLocation(player.getWorld()));
        }

        if((pt >= 2160000) && !(player.hasPermission("egirls.rank.vet"))){
            String command = "lp user " + playerName + " parent add veteran";
            Bukkit.dispatchCommand(console, command);
            String broadcast = playerName + " reached MidFag!";
            Bukkit.dispatchCommand(console, broadcast);
        }
        if((pt >= 17280000) && !(player.hasPermission("egirls.rank.oldfag"))){
            String command = "lp user " + playerName + " parent add oldfag";
            Bukkit.dispatchCommand(console, command);
            String broadcast = playerName + " reached OldFag!";
            Bukkit.dispatchCommand(console, broadcast);
        }

        ItemStack[] inv = player.getInventory().getContents();
        ItemStack i = new ItemStack(Material.NETHERITE_SWORD);
        ItemStack i2 = new ItemStack(Material.DIAMOND_SWORD);
        for(ItemStack item:inv){
            if(item != null){
                if(!allDisabled){
                    if(item.getType() == Material.TOTEM_OF_UNDYING){
                        if(item.getAmount() > 2){
                            item.setAmount(2);
                        }
                    }
                }

                if(echantLevelCheck(item)){
                    item.setAmount(0);
                    e.getPlayer().sendMessage(ChatColor.RED + "No overenchanted items for you m8.");
                }
                if(illegalItemLoreCheck(item)){
                    item.setAmount(0);
                    e.getPlayer().sendMessage(ChatColor.RED + "Next time .peek first m8.");
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
                                player.sendMessage(ChatColor.RED + "Your book contained non-ascii characters and was removed.");
                                item.setAmount(0);
                                Bukkit.getLogger().warning("User " + player.getName() + " joined with a book with non-ascii characters at coords: "
                                        + player.getLocation());
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
