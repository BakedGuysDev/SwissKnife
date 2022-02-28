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

import com.egirlsnation.swissknife.systems.modules.Modules;
import com.egirlsnation.swissknife.systems.modules.misc.DiscordLagNotifier;
import com.egirlsnation.swissknife.utils.server.ServerUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpsAlertTestCommand extends Command {

    public TpsAlertTestCommand(){
        super("tps-alert-test");
    }

    //TODO: Improve

    @Override
    public void handleCommand(CommandSender sender, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(!player.isOp()){
                sendMessage(sender, ChatColor.RED + "You don't have enough permission to do this command");
            }
        }
        Modules.get().get(DiscordLagNotifier.class).tpsNotify(ServerUtil.getTps());
    }
}
