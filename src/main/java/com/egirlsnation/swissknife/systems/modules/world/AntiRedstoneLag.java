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

package com.egirlsnation.swissknife.systems.modules.world;

import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.server.ServerUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class AntiRedstoneLag extends Module {
    public AntiRedstoneLag(){
        super(Categories.World, "anti-redstone-lag", "Disables redstone when tps drops bellow configured value");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> tpsThreshold = sgGeneral.add(new IntSetting.Builder()
            .name("tps-threshold")
            .description("The tps to disable redstone at")
            .defaultValue(18)
            .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("log")
            .description("If the plugin should log")
            .defaultValue(false)
            .build()
    );

    private final Setting<Integer> logDelay = sgGeneral.add(new IntSetting.Builder()
            .name("log-delay")
            .description("If tps is under threshold how often should the plugin log (in seconds)")
            .defaultValue(30)
            .build()
    );

    private long lastLog = 0;



    @EventHandler
    private void redstoneEvent(BlockRedstoneEvent e){
        if(!isEnabled()) return;
        if(ServerUtil.getTps()[0] < tpsThreshold.get()){
            if(e.getBlock().getType().equals(Material.OBSERVER)){
                e.setNewCurrent(0);
            }else{
                e.setNewCurrent(e.getOldCurrent());
            }
            if(log.get()){
                if(System.currentTimeMillis() - lastLog >= logDelay.get() ){
                    info("Disabled all redstone because tps is under " + tpsThreshold.get());
                    lastLog = System.currentTimeMillis();
                }
            }
        }else{
            e.setNewCurrent(e.getNewCurrent());
        }
    }

    @EventHandler
    private void pistonExtend(BlockPistonExtendEvent e){
        if(!isEnabled()) return;
        if(ServerUtil.getTps()[0] < tpsThreshold.get()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void pistonExtend(BlockPistonRetractEvent e){
        if(!isEnabled()) return;
        if(ServerUtil.getTps()[0] < tpsThreshold.get()){
            e.setCancelled(true);
        }
    }


}
