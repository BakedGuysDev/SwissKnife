package com.egirlsnation.swissknife.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class playtime implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED+"You must be a player to execute this command.");
        }else {
            if(args.length == 0){
                Player player = (Player) sender;
                long ptTicks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);

                if((ptTicks >= 2160000) && !(player.hasPermission("egirls.rank.vet"))){
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    String playerName = player.getName();
                    String command = "lp user " + playerName + " parent add veteran";
                    Bukkit.dispatchCommand(console, command);
                    String broadcast = player.getDisplayName() + ChatColor.GOLD + " reached MidFag!";
                    Bukkit.dispatchCommand(console, broadcast);
                }
                if((ptTicks >= 17280000) && !(player.hasPermission("egirls.rank.oldfag"))){
                    String playerName = player.getName();
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    String command = "lp user " + playerName + " parent add oldfag";
                    Bukkit.dispatchCommand(console, command);
                    String broadcast = player.getDisplayName() + ChatColor.GOLD + " reached OldFag!";
                    Bukkit.dispatchCommand(console, broadcast);
                }

                long pt = ptTicks / 20;

                long day = (int) TimeUnit.SECONDS.toDays(pt);
                long hours = TimeUnit.SECONDS.toHours(pt) - (day *24);
                long minute = TimeUnit.SECONDS.toMinutes(pt) - (TimeUnit.SECONDS.toHours(pt)* 60);
                long second = TimeUnit.SECONDS.toSeconds(pt) - (TimeUnit.SECONDS.toMinutes(pt) *60);

                player.sendMessage(ChatColor.GOLD + "Your playtime is: " + day + " days, " + hours + " hours " + minute + " minutes " + second + " seconds");
            }else if(args.length >= 1){
                Player target  = Bukkit.getPlayer(args[0]);
                long ptTicks = target.getStatistic(Statistic.PLAY_ONE_MINUTE);

                if((ptTicks >= 2160000) && !(target.hasPermission("egirls.rank.vet"))){
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    String playerName = target.getName();
                    String command = "lp user " + playerName + " parent add veteran";
                    Bukkit.dispatchCommand(console, command);
                    String broadcast = target.getDisplayName() + ChatColor.GOLD + " reached MidFag!";
                    Bukkit.dispatchCommand(console, broadcast);
                }
                if((ptTicks >= 17280000) && !(target.hasPermission("egirls.rank.oldfag"))){
                    String playerName = target.getName();
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    String command = "lp user " + playerName + " parent add oldfag";
                    Bukkit.dispatchCommand(console, command);
                    String broadcast = target.getDisplayName() + ChatColor.GOLD + " reached OldFag!";
                    Bukkit.dispatchCommand(console, broadcast);
                }

                long pt = ptTicks / 20;

                long day = (int) TimeUnit.SECONDS.toDays(pt);
                long hours = TimeUnit.SECONDS.toHours(pt) - (day *24);
                long minute = TimeUnit.SECONDS.toMinutes(pt) - (TimeUnit.SECONDS.toHours(pt)* 60);
                long second = TimeUnit.SECONDS.toSeconds(pt) - (TimeUnit.SECONDS.toMinutes(pt) *60);

                sender.sendMessage(ChatColor.GOLD + "Playtime of " + target.getDisplayName()+ ChatColor.GOLD + " is: " + day + " days, " + hours + " hours " + minute + " minutes " + second + " seconds");
            }
        }
        return true;
    }
}
