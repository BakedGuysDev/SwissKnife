package com.egirlsnation.swissknife.commands;

import com.egirlsnation.swissknife.swissKnife;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class kill implements CommandExecutor {

    private swissKnife swissknife = swissKnife.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            if(swissknife.users.get(((Player) sender).getUniqueId()) == null){
                ((Player) sender).setHealth(0);
                swissknife.users.put(((Player) sender).getUniqueId(), System.currentTimeMillis());
            }else if(swissknife.users.get(((Player) sender).getUniqueId()) != null && swissknife.users.get(((Player) sender).getUniqueId()) + (swissknife.killCooldown * 1000) <= System.currentTimeMillis()){
                ((Player) sender).setHealth(0);
            }else{
                sender.sendMessage(swissKnife.killCooldownMsg);
            }
            return true;
        }else if(!(sender instanceof Player)){
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }
        return false;
    }
}
