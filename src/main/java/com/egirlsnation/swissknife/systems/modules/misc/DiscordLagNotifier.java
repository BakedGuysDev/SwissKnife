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

package com.egirlsnation.swissknife.systems.modules.misc;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.OldConfig;
import com.egirlsnation.swissknife.utils.discord.DiscordWebhook;
import com.egirlsnation.swissknife.utils.entity.player.RankUtil;
import com.egirlsnation.swissknife.utils.server.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DiscordLagNotifier extends Module {
    public DiscordLagNotifier(){
        super(Categories.Misc, "discord-lag-notifier", "Uses discord webhooks to notify you about lag");
    }

    private final SettingGroup sgWebhook = settings.createGroup("webhook");

    private final Setting<String> webhookUrl = sgWebhook.add(new StringSetting.Builder()
            .name("webhook-url")
            .description("The discord webhook url")
            .defaultValue("https://discord.com/api/example/1234567890/123456-asdf-56789-qwertz-12345-yxcvb")
            .build()
    );

    private final Setting<String> webhookName = sgWebhook.add(new StringSetting.Builder()
            .name("webhook-name")
            .description("The name that will be displayed")
            .defaultValue("SwissKnife TPS Alert")
            .build()
    );

    private final Setting<String> webhookAvatarUrl = sgWebhook.add(new StringSetting.Builder()
            .name("avatar-url")
            .description("Link to an image that should be set as the image of the webook")
            .defaultValue("https://cdn1.iconfinder.com/data/icons/ui-6/502/alert-512.png")
            .build()
    );

    private final Setting<List<String>> roleIds = sgWebhook.add(new StringListSetting.Builder()
            .name("ping-role-ids")
            .description("Roles to ping. Leave empty to disable")
            .defaultValue(Arrays.asList("719274790795346031"))
            .build()
    );

    private final SettingGroup sgNotifier = settings.createGroup("notifier");

    private final Setting<Integer> repeatTime = sgNotifier.add(new IntSetting.Builder()
            .name("repeat-time")
            .description("How often should the plugin check the tps (in ticks)")
            .defaultValue(1200)
            .min(20)
            .build()
    );

    private final Setting<Integer> recheckDelay = sgNotifier.add(new IntSetting.Builder()
            .name("recheck-delay")
            .description("After how many ticks to recheck the tps in case the tps drop was a lag spike")
            .defaultValue(300)
            .min(20)
            .build()
    );

    private final Setting<Integer> loadDelay = sgNotifier.add(new IntSetting.Builder()
            .name("load-delay")
            .description("After how many ticks to start the check task when module is enabled (Tps may drop when plugins are being loaded)")
            .defaultValue(1200)
            .min(20)
            .build()
    );

    private final Setting<Integer> tpsThreshold = sgNotifier.add(new IntSetting.Builder()
            .name("tps-threshold")
            .description("How often should the plugin check the tps (in ticks)")
            .defaultValue(18)
            .range(1, 19)
            .build()
    );

    private final Setting<Boolean> listOnlinePlayers = sgNotifier.add(new BoolSetting.Builder()
            .name("list-online-players")
            .description("If the online players should be shown on the embed")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> listLowPtPlayers = sgNotifier.add(new BoolSetting.Builder()
            .name("list-low-pt-players")
            .description("If low playtime players should be shown on the embed")
            .defaultValue(false)
            .build()
    );

    private final Setting<Integer> lowPtThreshold = sgNotifier.add(new IntSetting.Builder()
            .name("low-pt-threshold")
            .description("Until how many hours of playtime is player a low pt one")
            .defaultValue(10)
            .min(1)
            .build()
    );

    private BukkitTask tpsCheck = null;


    @Override
    public void onEnable(){
        tpsCheck = Bukkit.getScheduler().runTaskTimer(SwissKnife.INSTANCE, () -> {
            if(ServerUtil.getTps()[0] <= tpsThreshold.get()){
                Bukkit.getScheduler().runTaskLater(SwissKnife.INSTANCE, () -> {
                    double[] tps = ServerUtil.getTps();
                    if(tps[0] <= tpsThreshold.get()){
                        tpsNotify(tps);
                    }
                }, recheckDelay.get());
            }
        }, loadDelay.get(), repeatTime.get());
    }

    @Override
    public void onDisable(){
        if(tpsCheck != null){
            tpsCheck.cancel();
        }
    }

    private void tpsNotify(double[] tps){
        Collection<? extends Player> onlinePlayers = null;
        if(listOnlinePlayers.get()){
            onlinePlayers = Bukkit.getOnlinePlayers();
        }

        List<String> namesUnderPt = null;
        if(listLowPtPlayers.get()){
            namesUnderPt = RankUtil.getOnlinePlayerNamesUnderPlaytime(lowPtThreshold.get());
        }

        int playercount = Bukkit.getServer().getOnlinePlayers().size();
        int maxSlots = Bukkit.getServer().getMaxPlayers();
        List<String> finalNamesUnderPt = namesUnderPt;
        Collection<? extends Player> finalOnlinePlayers = onlinePlayers;
        Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
            try {
                postNotif(tps, playercount, maxSlots, finalOnlinePlayers, finalNamesUnderPt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void postNotif(double[] tps, int playercount, int maxSlots, Collection<? extends Player> onlinePlayers, List<String> lowPtNames) throws IOException{
        DiscordWebhook webhook = new DiscordWebhook(webhookUrl.get());
        if(webhookName.get().isBlank()){
            webhook.setUsername(webhookName.getDefaultValue());
        }else{
            webhook.setUsername(webhookName.get());
        }

        if(!webhookAvatarUrl.get().isBlank()){
            webhook.setAvatarUrl(OldConfig.instance.webhookAvatarURL);
        }
        if(!roleIds.get().isEmpty()){
            String pings = "";
            for(String id : roleIds.get()){
                pings = pings + " <@&" + id +">";
            }
            webhook.setContent(pings);
        }

        String tpsString = Arrays.toString(tps);
        tpsString = tpsString.substring(1, tpsString.length() - 1);

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject()
                .setTitle(":warning: Low TPS")
                .setDescription("Please keep in mind that this feature is still in testing.")
                .addField("Players online", playercount + "/" + maxSlots, false);
        if(tps.length == 3){
            embed.addField("TPS (1m, 5m, 15m)", tpsString, false );
        }else if(tps.length == 4){
            embed.addField("TPS (5s, 1m, 5m, 15m)", tpsString, false);
        }else{
            embed.addField("TPS (unknown time intervals)", tpsString, false);
        }

        if(onlinePlayers != null){
            if(onlinePlayers.isEmpty()){
                embed.addField("Players", "No players online", false);
            }else{
                embed.addField("Players", onlinePlayers.stream().map(HumanEntity::getName).collect(Collectors.joining(", ")), false );
            }
        }
        if(lowPtNames != null){
            if(lowPtNames.isEmpty()){
                embed.addField("Low Playtime Players", "No low playtime players online", true);
            }else{
                embed.addField("Low Playtime Players", lowPtNames.stream().map(Objects::toString).collect(Collectors.joining(", ")), false );
            }
        }
        embed.setColor(Color.red);
        webhook.addEmbed(embed);
        webhook.execute();
    }
}
