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

package com.egirlsnation.swissknife.systems.modules.player;

import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class BedrockFloorDisabler extends Module {
    public BedrockFloorDisabler() {
        super(Categories.Player, "bedrock-floor-disabler", "Prevents players going bellow the bedrock floor");
    }


    private final SettingGroup sgOverworld = settings.createGroup("overworld");

    private final Setting<Boolean> owEnable = sgOverworld.add(new BoolSetting.Builder()
            .name("enabled")
            .description("If the plugin should prevent players from going bellow the bedrock floor in the overworld")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> owRepair = sgOverworld.add(new BoolSetting.Builder()
            .name("repair")
            .description("If the plugin should repair the bedrock floor when player falls through")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> owDepth = sgOverworld.add(new IntSetting.Builder()
            .name("depth")
            .description("The Y coordinate of the lowest bedrock block")
            .defaultValue(0)
            .build()
    );

    private final Setting<Boolean> owAlertPlayers = sgOverworld.add(new BoolSetting.Builder()
            .name("alert-players")
            .description("If the plugin should tell the player he can't go bellow the bedrock")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> owMessage = sgOverworld.add(new StringSetting.Builder()
            .name("message")
            .description("The message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "You cannot go bellow the bedrock floor")
            .build()
    );

    private final Setting<Boolean> owLog = sgOverworld.add(new BoolSetting.Builder()
            .name("log")
            .description("If the plugin should log when player attempts to go bellow the bedrock floor")
            .defaultValue(true)
            .build()
    );


    private final SettingGroup sgNether = settings.createGroup("nether");

    private final Setting<Boolean> netherEnable = sgNether.add(new BoolSetting.Builder()
            .name("enabled")
            .description("If the plugin should prevent players from going bellow the bedrock floor in the nether")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> netherRepair = sgNether.add(new BoolSetting.Builder()
            .name("repair")
            .description("If the plugin should repair the bedrock floor when player falls through")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> netherDepth = sgNether.add(new IntSetting.Builder()
            .name("depth")
            .description("The Y coordinate of the lowest bedrock block")
            .defaultValue(0)
            .build()
    );

    private final Setting<Boolean> netherAlertPlayers = sgNether.add(new BoolSetting.Builder()
            .name("alert-players")
            .description("If the plugin should tell the player he can't go bellow the bedrock")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> netherMessage = sgNether.add(new StringSetting.Builder()
            .name("message")
            .description("The message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "You cannot go bellow the bedrock floor")
            .build()
    );

    private final Setting<Boolean> netherLog = sgNether.add(new BoolSetting.Builder()
            .name("log")
            .description("If the plugin should log when player attempts to go bellow the bedrock floor")
            .defaultValue(true)
            .build()
    );


    @EventHandler
    public void PlayerMove(PlayerMoveEvent e) {
        if(!isEnabled()) return;

        World.Environment env = e.getTo().getWorld().getEnvironment();

        if(owEnable.get() && env.equals(World.Environment.NORMAL) && e.getTo().getY() < owDepth.get()){
            if(owRepair.get()){
                e.getPlayer().getWorld().getBlockAt(e.getTo().getBlockX(), owDepth.get(), e.getTo().getBlockZ()).setType(Material.BEDROCK);
            }
            e.setTo(e.getFrom().add(0, 2, 0));
            if(owAlertPlayers.get()){
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('§', owMessage.get()));
            }
            if(owLog.get()){
                info("Player " + e.getPlayer().getName() + " attempted to go bellow the overworld bedrock at: " + LocationUtil.getLocationString(e.getFrom()));
            }
            return;
        }

        if(netherEnable.get() && env.equals(World.Environment.NETHER) && e.getTo().getY() < netherDepth.get()){
            if(netherRepair.get()){
                e.getPlayer().getWorld().getBlockAt(e.getTo().getBlockX(), netherDepth.get(), e.getTo().getBlockZ()).setType(Material.BEDROCK);
            }
            e.setTo(e.getFrom().add(0, 2, 0));
            if(netherAlertPlayers.get()){
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('§', netherMessage.get()));
            }
            if(netherLog.get()){
                info("Player " + e.getPlayer().getName() + " attempted to go bellow the nether bedrock at: " + LocationUtil.getLocationString(e.getFrom()));
            }
        }
    }
}
