package com.egirlsnation.swissknife.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.egirlsnation.swissknife.service.pingService.getPing;

public class ping implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED+"You must be a player to execute this command.");
        }else{
            if(args.length == 0){
                Player player = (Player) sender;
                int ping = getPing(player);
                if(ping == 0){
                    sender.sendMessage(ChatColor.YELLOW+"["+ChatColor.GOLD+"Ping"+ChatColor.YELLOW+"] Something went wrong.");
                }else{
                    sender.sendMessage(ChatColor.YELLOW+"["+ChatColor.GOLD+"Ping"+ChatColor.YELLOW+"] Your ping is " + ping + "ms." );
                }
                return true;
            }else if (args.length >= 1){
                Player target  = Bukkit.getPlayer(args[0]);
                if(target == null){sender.sendMessage(ChatColor.YELLOW+"["+ChatColor.GOLD+"Ping"+ChatColor.YELLOW+"] Player not found."); return true;}
                else{
                    int ping = getPing(target);
                    if(ping == 0){
                        sender.sendMessage(ChatColor.YELLOW+"["+ChatColor.GOLD+"Ping"+ChatColor.YELLOW+"] Something went wrong.");
                    }else{
                        sender.sendMessage(ChatColor.YELLOW+"["+ChatColor.GOLD+"Ping"+ChatColor.YELLOW+"] Ping of "+ target.getDisplayName() + ChatColor.YELLOW+  " is " + ping + "ms.");
                    }
                    return true;
                }

            }
        }
        return true;
    }
}
