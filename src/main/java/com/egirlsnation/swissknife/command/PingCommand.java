package com.egirlsnation.swissknife.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.egirlsnation.swissknife.util.StringUtils.formatPing;

public class PingCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[Pong] Choochoo motherfucker");
            return true;
        }
        if (args.length == 0) {
            Player player = (Player) sender;
            int ping = player.getPing();
            if (ping == 0) {
                sender.sendMessage(ChatColor.RED + "Something went wrong while getting your ping or your ping is 0 ¯\\_(ツ)_/¯");
            } else {
                sender.sendMessage(ChatColor.AQUA + "Your ping is" + formatPing(ping) + ChatColor.AQUA + " ms");
            }
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not online.");
            return true;
        }
        int ping = target.getPing();
        if (ping == 0) {
            sender.sendMessage(ChatColor.RED + "Something went wrong while getting ping of " + target.getDisplayName() + " or their ping is 0 ¯\\_(ツ)_/¯");
        } else {
            sender.sendMessage(ChatColor.AQUA + "Your ping is" + formatPing(ping) + ChatColor.AQUA + " ms");
        }
        return true;
    }
}
