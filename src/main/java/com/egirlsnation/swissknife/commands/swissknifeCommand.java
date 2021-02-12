package com.egirlsnation.swissknife.commands;

import com.egirlsnation.swissknife.swissKnife;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import java.util.UUID;

import static com.egirlsnation.swissknife.service.shitListService.addToShitList;
import static com.egirlsnation.swissknife.service.shitListService.removeFromShitList;

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
        }else if(args[0].equalsIgnoreCase("toggleswim")){
            if(!(sender instanceof Player)){
                sender.sendMessage("You need to be a player to execute this command.");
                return true;
            }else{
                if(!sender.hasPermission("egirls.command.swim")){
                    sender.sendMessage(ChatColor.RED + "You dont have enough permissions.");
                    return true;
                }else{
                    UUID playerUUID = ((Player) sender).getUniqueId();
                    if(swissKnife.swimmingList.get(playerUUID) == null){
                        swissKnife.swimmingList.put(playerUUID, true);
                        sender.sendMessage(ChatColor.AQUA + "Enjoy swimming animation xd");
                        ((Player) sender).setSwimming(true);
                        return true;
                    }else{
                        swissKnife.swimmingList.remove(playerUUID);
                        sender.sendMessage(ChatColor.AQUA + "Swimming was toggled off for you.");
                        ((Player) sender).setSwimming(false);
                        return true;
                    }
                }
            }

        }else if(args[0].equalsIgnoreCase("shitlist")){
            if(sender instanceof Player){
                if(!sender.isOp()){
                    sender.sendMessage(ChatColor.RED + "You don't have permission to do this command.");
                }else{
                    if(args[1] == null){
                        sender.sendMessage(ChatColor.RED + "You need to provide what you want to do. Add/remove");
                    }else if(args[1].equalsIgnoreCase("add")){
                        if(args[2] == null){
                            sender.sendMessage(ChatColor.RED + "You need to provide a playername.");
                        }else{
                            Player player = Bukkit.getServer().getPlayer(args[2]);
                            if(player != null){
                                if(args[3].equalsIgnoreCase("tpa")){
                                    sender.sendMessage(ChatColor.GREEN + "Player " + player.getDisplayName() + ChatColor.GREEN + " was added to shitlist with tpa");
                                    sender.sendMessage(addToShitList(player, "tpa"));
                                }else if(args[3].equalsIgnoreCase("coords")){
                                    sender.sendMessage(ChatColor.GREEN + "Player " + player.getDisplayName() + ChatColor.GREEN + " was added to shitlist with coords");
                                    sender.sendMessage(addToShitList(player, "coords"));
                                }else if(args[3].equalsIgnoreCase("both")){
                                    sender.sendMessage(ChatColor.GREEN + "Player " + player.getDisplayName() + ChatColor.GREEN + " was added to shitlist with coords and tpa");
                                    sender.sendMessage(addToShitList(player, "both"));
                                }else{
                                    sender.sendMessage(ChatColor.RED + "Only valid arguments are tpa/coords/both");
                                }
                            }else{
                                sender.sendMessage(ChatColor.RED + "Player not found.");
                            }
                        }
                    }else if(args[1].equalsIgnoreCase("remove")){
                        if(args[2] == null){
                            sender.sendMessage(ChatColor.RED + "You need to provide a playername.");
                        }else{
                            Player player = Bukkit.getServer().getPlayer(args[2]);
                            if(player != null){
                                if(args[3].equalsIgnoreCase("tpa")){
                                    sender.sendMessage(ChatColor.GREEN + "Player " + player.getDisplayName() + ChatColor.GREEN + " was removed from shitlist with tpa");
                                    sender.sendMessage(removeFromShitList(player, "tpa"));
                                }else if(args[3].equalsIgnoreCase("coords")){
                                    sender.sendMessage(ChatColor.GREEN + "Player " + player.getDisplayName() + ChatColor.GREEN + " was removed from shitlist with coords");
                                    sender.sendMessage(removeFromShitList(player, "coords"));
                                }else if(args[3].equalsIgnoreCase("both")){
                                    sender.sendMessage(ChatColor.GREEN + "Player " + player.getDisplayName() + ChatColor.GREEN + " was removed from shitlist with coords and tpa");
                                    sender.sendMessage(removeFromShitList(player, "both"));
                                }else{
                                    sender.sendMessage(ChatColor.RED + "Only valid arguments are tpa/coords/both");
                                }
                            }else{
                                sender.sendMessage(ChatColor.RED + "Player not found.");
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
