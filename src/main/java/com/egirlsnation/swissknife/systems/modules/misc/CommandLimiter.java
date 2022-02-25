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

import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CommandLimiter extends Module {
    public CommandLimiter(){
        super(Categories.Misc, "command-limiter", "Puts cooldowns on commands from other plugins");
    }

    private final SettingGroup sgEssentials = settings.createGroup("essentials");

    private final Setting<Boolean> limitMe = sgEssentials.add(new BoolSetting.Builder()
            .name("me-command")
            .description("If the plugin should limit the /me command")
            .defaultValue(true)
            .build()
    );
    private final Setting<Integer> meCooldown = sgEssentials.add(new IntSetting.Builder()
            .name("me-cooldown")
            .description("Cooldown in seconds")
            .defaultValue(30)
            .build()
    );

    private final Setting<Boolean> limitAfk = sgEssentials.add(new BoolSetting.Builder()
            .name("afk-command")
            .description("If the plugin should limit the /afk command")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> afkCooldown = sgEssentials.add(new IntSetting.Builder()
            .name("afk-cooldown")
            .description("Cooldown in seconds")
            .defaultValue(30)
            .build()
    );

    private final Map<UUID, Long> afkCooldownMap = new HashMap<>(1);
    private final Map<UUID, Long> meCooldownMap = new HashMap<>(1);

    @EventHandler
    private void onCommand(PlayerCommandPreprocessEvent e){
        ForeignCommand command;
        if(e.getMessage().toLowerCase().startsWith("/me")){
            command = ForeignCommand.ME;
        }else if(e.getMessage().toLowerCase().startsWith("/afk")){
            command = ForeignCommand.AFK;
        }else{
            return;
        }

        if(isOnCooldown(e.getPlayer().getUniqueId(), command)){
            e.setCancelled(true);
            sendMessage(e.getPlayer(), ChatColor.RED + "You need to wait " + getPlayerCooldown(e.getPlayer().getUniqueId(), command) + " seconds before doing this command again");
        }else{
            setCooldown(e.getPlayer().getUniqueId(), command);
        }
    }

    private void setCooldown(UUID uuid, ForeignCommand command){
        getCooldownMap(command).put(uuid, System.currentTimeMillis());
    }

    public boolean isOnCooldown(UUID uuid, ForeignCommand command){
        Map<UUID, Long> cooldownMap = getCooldownMap(command);
        if(cooldownMap.get(uuid) == null) return false;
        long timeLeft = System.currentTimeMillis() - cooldownMap.get(uuid);

        return TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= getCooldown(command);
    }

    public long getPlayerCooldown(UUID uuid, ForeignCommand command){
        Map<UUID, Long> cooldownMap = getCooldownMap(command);
        if(cooldownMap.get(uuid) == null) return 0;
        long timeLeft = System.currentTimeMillis() - cooldownMap.get(uuid);
        return TimeUnit.MILLISECONDS.toSeconds(timeLeft);
    }

    private Map<UUID, Long> getCooldownMap(ForeignCommand command){
        if(command.equals(ForeignCommand.ME)){
            return meCooldownMap;
        }else if(command.equals(ForeignCommand.AFK)){
            return afkCooldownMap;
        }
        return new HashMap<>(0);
    }

    private int getCooldown(ForeignCommand command){
        if(command.equals(ForeignCommand.ME)){
            return meCooldown.get();
        }else if(command.equals(ForeignCommand.AFK)){
            return meCooldown.get();
        }
        return 0;
    }

    private enum ForeignCommand {
        ME,
        AFK
    }
}
