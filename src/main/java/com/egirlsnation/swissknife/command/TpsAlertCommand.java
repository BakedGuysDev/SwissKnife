/*
 * This file is part of the SwissKnife plugin distibution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.command;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.util.ServerUtils;
import com.egirlsnation.swissknife.util.discord.DiscordHandler;
import com.egirlsnation.swissknife.util.player.RankUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;

public class TpsAlertCommand implements CommandExecutor {

    private final SwissKnife plugin;

    public TpsAlertCommand(SwissKnife plugin) {
        this.plugin = plugin;
    }

    private final ServerUtils serverUtils = new ServerUtils();
    private final DiscordHandler discordHandler = new DiscordHandler();
    private final RankUtil rankUtil = new RankUtil();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(ChatColor.RED + "This command is console only");
        }
        List<Double> tps = serverUtils.getTPS();
        List<String> rankNames = null;
        if (listOnlinePlayers) {
            rankNames = rankUtil.getOnlinePlayerRankList();
        }
        List<String> namesUnderPt = null;
        if (listLowPtPlayers) {
            namesUnderPt = rankUtil.getOnlinePlayerNamesUnderPlaytime(lowPtThreshold);
        }

        List<String> finalRankNames = rankNames;
        List<String> finalNamesUnderPt = namesUnderPt;
        int playercount = Bukkit.getServer().getOnlinePlayers().size();
        int maxSlots = Bukkit.getServer().getMaxPlayers();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                discordHandler.postDiscordTPSNotif(tps, playercount, maxSlots, finalRankNames, finalNamesUnderPt);
                Bukkit.getLogger().info("Webhook posted successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return true;
    }
}
