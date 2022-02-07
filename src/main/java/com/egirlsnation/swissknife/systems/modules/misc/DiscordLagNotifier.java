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

import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;

import java.util.Arrays;
import java.util.List;

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

    








}
