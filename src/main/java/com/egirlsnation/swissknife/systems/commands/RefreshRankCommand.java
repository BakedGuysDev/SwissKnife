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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RefreshRankCommand extends Command {


    public RefreshRankCommand(){
        super("refresh-rank");
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args){
        if(!(sender instanceof Player)){
            sendMessage(sender, ChatColor.RED + "You can't do this command m8.");
            return;
        }
        boolean promoted = RankUtil.promoteIfEligible((Player) sender);
        if(promoted){
            sendMessage(sender, ChatColor.GREEN + "You've been promoted!");
        }else{
            sendMessage(sender, ChatColor.GOLD + "Didn't find any rank you could be promoted to.");
        }
    }
}
