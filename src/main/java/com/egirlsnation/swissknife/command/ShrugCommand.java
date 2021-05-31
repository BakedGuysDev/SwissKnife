package com.egirlsnation.swissknife.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShrugCommand implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED+"You must be a player to execute this command.");
        }else {
            Player p = (Player) sender;
            StringBuilder arg = new StringBuilder();
            if (args != null) {
                for (String a : args) {
                    arg.append(a).append(" ");
                }
            }

            p.chat(arg + " ¯\\_(ツ)_/¯");
        }
        return true;
    }
}
