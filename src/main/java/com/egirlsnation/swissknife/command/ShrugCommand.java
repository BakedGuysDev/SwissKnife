package com.egirlsnation.swissknife.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShrugCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED+"You must be a player to execute this command.");
            return true;
        }else {
            Player p = (Player) sender;
            String arg = "";
            if (args != null) {
                String[] var10 = args;
                int var9 = args.length;

                for(int var8 = 0; var8 < var9; ++var8) {
                    String a = var10[var8];
                    arg = arg + a + " ";
                }
            }

            p.chat(arg + " ¯\\_(ツ)_/¯");
            return true;
        }
    }
}
