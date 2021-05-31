package com.egirlsnation.swissknife.command;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.util.cooldownManager.CommandType;
import com.egirlsnation.swissknife.util.cooldownManager.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class PlaytimeCommand implements CommandExecutor {

    private final SwissKnife plugin;
    public PlaytimeCommand(SwissKnife plugin){ this.plugin = plugin; }

    private final CooldownManager cooldownManager = new CooldownManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "You cannot execute this command as console!");
            return true;
        }

        if(args.length == 0){
            Player player = (Player) sender;
            long ptTicks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
            player.sendMessage(ChatColor.AQUA + "Your playtime is " + formatPlayTime(ptTicks));
            return true;
        }
        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if(target != null){
            long ptTicks = target.getStatistic(Statistic.PLAY_ONE_MINUTE);
            sender.sendMessage(ChatColor.AQUA + "Playtime of" + targetName + " is " + formatPlayTime(ptTicks));
            return true;
        }

        if(handleCooldown((Player) sender)){
            return true;
        }

        if(!plugin.SQL.isConnected()){
            sender.sendMessage(ChatColor.RED + "Couldn't get playtime of " + targetName);
            return true;
        }
        if(!plugin.sqlQuery.exists(targetName)){
            sender.sendMessage(ChatColor.RED + "Couldn't get playtime of " + targetName + ".\nThis can mean that the player isn't in the database or isn't online.");
            return true;
        }
        long ptTicks  = plugin.sqlQuery.getPlaytime(targetName);
        sender.sendMessage(ChatColor.RED + "Playtime of " + targetName + " is " + formatPlayTime(ptTicks));

        return true;
    }


    private String formatPlayTime(long ticks){
        long pt = ticks / 20;

        long day = (int) TimeUnit.SECONDS.toDays(pt);
        long hours = TimeUnit.SECONDS.toHours(pt) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(pt) - (TimeUnit.SECONDS.toHours(pt)* 60);
        long second = TimeUnit.SECONDS.toSeconds(pt) - (TimeUnit.SECONDS.toMinutes(pt) *60);

        return day + " days, " + hours + " hours " + minute + " minutes " + second + " seconds";
    }

    private boolean handleCooldown(Player player){
        CommandType type = CommandType.PLAYTIME;

        // Getting time left for this player for this command
        long timeLeft = System.currentTimeMillis() - cooldownManager.getCommandInfo(player.getUniqueId(), type).getCooldown();

        if(player.hasPermission("swissknife.cooldown.bypass")){
            return false;
        }else if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownManager.DEFAULT_COOLDOWN){
            cooldownManager.setCooldown(player.getUniqueId(), System.currentTimeMillis(), type);
            return false;
        }
        player.sendMessage(ChatColor.RED + "You gotta wait before doing that again m8");
        return true;
    }
}
