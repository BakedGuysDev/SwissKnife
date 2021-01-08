package com.egirlsnation.swissknife.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;


public class onJoin implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        long pt = player.getStatistic(Statistic.PLAY_ONE_MINUTE);

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
