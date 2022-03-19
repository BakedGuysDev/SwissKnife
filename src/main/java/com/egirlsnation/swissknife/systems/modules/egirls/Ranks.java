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

package com.egirlsnation.swissknife.systems.modules.egirls;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.hooks.EssentialsHook;
import com.egirlsnation.swissknife.systems.hooks.Hooks;
import com.egirlsnation.swissknife.systems.hooks.LuckPermsHook;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.entity.player.RankUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.TimeUnit;

public class Ranks extends Module {
    public Ranks() {
        super(Categories.EgirlsNation, "ranks", "Egirls Nation rank system");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> checkPeriodically = sgGeneral.add(new BoolSetting.Builder()
            .name("periodic-check")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> period = sgGeneral.add(new IntSetting.Builder()
            .name("period")
            .defaultValue(10)
            .range(5,120)
            .build()
    );

    private final SettingGroup sgRequirements = settings.createGroup("requirements");

    public final Setting<Integer> newfagHours = sgRequirements.add(new IntSetting.Builder()
            .name("newfag-hours")
            .defaultValue(48)
            .build()
    );

    public final Setting<Integer> midfagHours = sgRequirements.add(new IntSetting.Builder()
            .name("midfag-hours")
            .defaultValue(168)
            .build()
    );

    public final Setting<Integer> oldfagHours = sgRequirements.add(new IntSetting.Builder()
            .name("oldfag-hours")
            .defaultValue(432)
            .build()
    );

    public final Setting<Integer> elderfagHours = sgRequirements.add(new IntSetting.Builder()
            .name("elderfag-hours")
            .defaultValue(1200)
            .build()
    );

    public final Setting<Integer> elderfagVotes = sgRequirements.add(new IntSetting.Builder()
            .name("elderfag-votes")
            .defaultValue(100)
            .build()
    );

    public final Setting<Integer> boomerfagHours = sgRequirements.add(new IntSetting.Builder()
            .name("boomerfag-hours")
            .defaultValue(2400)
            .build()
    );

    public final Setting<Integer> boomerfagVotes = sgRequirements.add(new IntSetting.Builder()
            .name("boomerfag-votes")
            .defaultValue(300)
            .build()
    );

    @Override
    public void onEnable(){
        if(!isEnabled()) return;
        if(!checkPeriodically.get()) return;

        Bukkit.getScheduler().runTaskTimer(SwissKnife.INSTANCE, () -> {
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.hasPlayedBefore()){
                    RankUtil.promoteIfEligible(p);
                }
            }
        }, 6000, period.get() * 60 * 20);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e){
        if(!isEnabled()) return;
        if(!e.getPlayer().hasPlayedBefore()) return;
        RankUtil.promoteIfEligible(e.getPlayer());
        if((System.currentTimeMillis() - e.getPlayer().getLastLogin()) > TimeUnit.DAYS.toMillis(1) && Hooks.get().isActive(LuckPermsHook.class) && Hooks.get().isActive(EssentialsHook.class)){
            if(Hooks.get().get(LuckPermsHook.class).isElderFag(e.getPlayer())){
                Hooks.get().get(EssentialsHook.class).addTpaTokens(e.getPlayer(), 2);
                sendMessage(e.getPlayer(), ChatColor.GREEN + "2 TPA tokens were added to your account :)");
            }
        }
    }
}
