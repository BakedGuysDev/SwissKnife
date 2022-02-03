/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GPL-3.0 License.
 *
 * You should have received a copy of the GPL-3.0
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

package com.egirlsnation.swissknife.systems.commands;

import com.egirlsnation.swissknife.utils.entity.player.RankUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RefreshRankCommand implements CommandExecutor {

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
