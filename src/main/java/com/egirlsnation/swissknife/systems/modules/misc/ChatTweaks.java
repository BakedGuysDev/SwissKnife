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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

public class ChatTweaks extends Module {
    public ChatTweaks() {
        super(Categories.Misc, "chat-tweaks", "Adds minor features to chat");
    }

    private final SettingGroup sgGreenText = settings.createGroup("green-text");
    private final SettingGroup sgCoords = settings.createGroup("coords-placeholder");

    private final Setting<Boolean> greenTextEnabled = sgGreenText.add(new BoolSetting.Builder()
            .name("enabled")
            .description("If green-text should be enabled")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> greenTextPermission = sgGreenText.add(new BoolSetting.Builder()
            .name("use-permission")
            .description("If users need to have permission to use green-text (swissknife.chat.greentext)")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> coordsEnabled = sgCoords.add(new BoolSetting.Builder()
            .name("enabled")
            .description("If the [coords] placeholder should be enabled")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> coordsReplace = sgCoords.add(new StringSetting.Builder()
            .name("replace-string")
            .description("What to replace the placeholder with (supports color codes)")
            .defaultValue("§f%player_world% §eX:§f%player_x% §eY:§f%player_y% §eZ:&f%player_z%")
            .build()
    );

    private final Setting<Boolean> enableCoordCommands = sgCoords.add(new BoolSetting.Builder()
            .name("replace-in-commands")
            .description("Whether to replace [coords] in commands")
            .defaultValue(true)
            .build()
    );

    private final Setting<List<String>> coordsCommands = sgCoords.add(new StringListSetting.Builder()
            .name("commands")
            .description("In which commands to replace [coords] (type without slash)")
            .defaultValue(Arrays.asList("w", "whisper", "msg", "r", "message", "reply"))
            .build()
    );

    private final Setting<Boolean> coordsPermission = sgCoords.add(new BoolSetting.Builder()
            .name("use-permission")
            .description("If users need to have permission to use [coords] placeholder (swissknife.chat.coords)")
            .defaultValue(false)
            .build()
    );

    @EventHandler
    public void PlayerChat(AsyncPlayerChatEvent e){
        if(!isEnabled()) return;

        if(greenTextEnabled.get() && (e.getPlayer().hasPermission("swissknife.chat.greentext") || !greenTextPermission.get()) && e.getMessage().charAt(0) == '>'){
            if(e.getPlayer().getName().equals("Lerbiq")){
                e.setMessage(ChatColor.LIGHT_PURPLE + e.getMessage());
            }else{
                e.setMessage(ChatColor.GREEN + e.getMessage());
            }
        }

        if(coordsEnabled.get()){
            if(e.getPlayer().hasPermission("swissknife.chat.coords") || !coordsPermission.get()){
                e.setMessage(e.getMessage().replaceAll("(?i)(\\[coords\\])", getCoordsPlaceholderFormatted(e.getPlayer())));
            }
        }
    }

    @EventHandler
    private void commandPreProcessor(PlayerCommandPreprocessEvent e){
        if(!isEnabled()) return;
        if(enableCoordCommands.get()){
            for(String command : coordsCommands.get()){
                if(e.getMessage().toLowerCase().startsWith("/" + command)){
                    e.setMessage(e.getMessage().replaceAll("(?i)(\\[coords\\])", getCoordsPlaceholderFormatted(e.getPlayer())));
                    break;
                }
            }

        }
    }

    public String getCoordsPlaceholderFormatted(Player player){

        String res = ChatColor.translateAlternateColorCodes('§', coordsReplace.get().replaceAll("%player_world%", player.getWorld().getName())
                .replaceAll("%player_x%", (int) player.getLocation().getX() + "")
                .replaceAll("%player_y%", (int) player.getLocation().getY() + "")
                .replaceAll("%player_z%", (int) player.getLocation().getZ() + "")
        );

        return res;
    }
}
