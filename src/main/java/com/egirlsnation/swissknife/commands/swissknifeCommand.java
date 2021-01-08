package com.egirlsnation.swissknife.commands;

import com.egirlsnation.swissknife.swissKnife;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import java.util.UUID;

public class swissknifeCommand implements CommandExecutor {

    private swissKnife swissknife = swissKnife.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 0){
         sender.sendMessage(ChatColor.DARK_RED + "You need to use an argument in the command.");
         return false;
        }else if(args[0].equalsIgnoreCase("reload")){
            if(!sender.hasPermission("egirls.command.reload")){
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permissions to execute this command.");
                return true;
            }else{
                swissknife.reloadConfig();
                swissknife.saveDefaultConfig();
                sender.sendMessage(swissKnife.reloadMessage);
                return true;
            }
        }else if(args[0].equalsIgnoreCase("togglepickup")){
            if(!(sender instanceof Player)){
                sender.sendMessage("You need to be a player to execute this command.");
            }else {
                if (!sender.hasPermission("egirls.command.pickup")) {
                    sender.sendMessage(ChatColor.DARK_RED + "You do not have permissions to execute this command.");
                    return true;
                } else {
                    UUID playerUUID = ((Player) sender).getUniqueId();
                    if(swissKnife.itemPickUpUsers.get(playerUUID) == null){
                        swissKnife.itemPickUpUsers.put(playerUUID, true);
                        sender.sendMessage(ChatColor.AQUA + "Item pickup was toggled off for you.");
                        return true;
                    }else{
                        swissKnife.itemPickUpUsers.remove(playerUUID);
                        sender.sendMessage(ChatColor.AQUA + "Item pickup was toggled on for you.");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
