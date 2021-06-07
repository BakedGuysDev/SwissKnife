package com.egirlsnation.swissknife.command;

import com.egirlsnation.swissknife.util.player.RankUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankCommand implements CommandExecutor {

    private final RankUtil rankUtil = new RankUtil();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        // Console with no args
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "You can't do this command m8.");
            return true;
        }

        Player player = (Player) sender;
        rankUtil.promoteIfEligible(player);


        return true;
    }

}
