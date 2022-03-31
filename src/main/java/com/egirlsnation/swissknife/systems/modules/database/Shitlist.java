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

package com.egirlsnation.swissknife.systems.modules.database;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.systems.sql.MySQL;
import com.egirlsnation.swissknife.utils.StringUtil;
import com.egirlsnation.swissknife.utils.entity.player.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class Shitlist extends Module {
    public Shitlist(){
        super(Categories.Database, "shitlist", "Basically list for naughty players. Restricts some actions for them");
    }

    private final SettingGroup sgDisableCommands = settings.createGroup("commands");
    private final SettingGroup sgSwapWords = settings.createGroup("chat");
    private final SettingGroup sgLeakCoords = settings.createGroup("coords");

    private final Setting<Boolean> disableCommands = sgDisableCommands.add(new BoolSetting.Builder()
            .name("disable-commands")
            .description("If shitlisted players should be prevented from doing certain commands")
            .defaultValue(true)
            .build()
    );

    private final Setting<List<String>> disabledCommands = sgDisableCommands.add(new StringListSetting.Builder()
            .name("disabled-commands")
            .description("List of commands to prevent shitlisted players from executing (without slash)")
            .defaultValue(Arrays.asList("tpa", "tpahere", "tpayes", "tpaccept", "tpaaccept", "tpno", "tpano", "tpdeny", "tpadeny", "tpyes"))
            .build()
    );

    private final Setting<Boolean> cmdAlertPlayers = sgDisableCommands.add(new BoolSetting.Builder()
            .name("alert-players")
            .description("If the plugin should alert player when they're shitlisted and a a blacklisted command")
            .defaultValue(true)
            .build()
    );

    private final Setting<String> cmdMessage = sgDisableCommands.add(new StringSetting.Builder()
            .name("message")
            .description("The message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "You are shitlisted and can't do this command.")
            .build()
    );

    private final Setting<Boolean> cmdLog = sgDisableCommands.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log when shitlisted player tries to execute a blacklisted command")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> swapWords = sgSwapWords.add(new BoolSetting.Builder()
            .name("swap-words")
            .description("If the plugin should swap words randomly when shitlisted player sends a chat message (with chance)")
            .defaultValue(true)
            .build()
    );

    private final Setting<List<String>> wordsList = sgSwapWords.add(new StringListSetting.Builder()
            .name("words-list")
            .description("Words to replace in the words in message with")
            .defaultValue(Arrays.asList("badger", ". Anyway I love dicks.", "titty", "pickle", "canoodle", "mitten", "doodlesack"))
            .build()
    );

    private final Setting<Integer> replaceChance = sgSwapWords.add(new IntSetting.Builder()
            .name("swap-chance")
            .description("Chance to replace word in message (in percent)")
            .defaultValue(20)
            .range(1,100)
            .build()
    );

    private final Setting<Boolean> leakCoords = sgLeakCoords.add(new BoolSetting.Builder()
            .name("leak-coords")
            .description("If the plugin should announce coords of shitlisted players when they leave the server")
            .defaultValue(false)
            .build()
    );


    private final List<UUID> onlineShitlist = new ArrayList<>();
    private final Random rng = new Random();


    @Override
    public void onEnable(){
        if(!MySQL.get().isConnected()){
            warn("Disabling... This module depends on the MySQL database, which is not connected.");
            toggle();
        }
    }

    @EventHandler
    private void playerJoin(PlayerJoinEvent e){
        if(!isEnabled()) return;
        UUID uuid = e.getPlayer().getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
           if(MySQL.get().getPlayerStatsDriver().isShitlisted(uuid)){
               onlineShitlist.add(uuid);
           }
        });
    }

    @EventHandler
    private void playerQuit(PlayerQuitEvent e){
        if(!isEnabled()) return;

        if(leakCoords.get() && onlineShitlist.contains(e.getPlayer().getUniqueId())){
            Location location = e.getPlayer().getLocation();
            String displayname = e.getPlayer().getDisplayName();
            Bukkit.getScheduler().runTaskLater(SwissKnife.INSTANCE, () -> Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "The coords of " + displayname + ChatColor.GREEN + " are " + StringUtil.getFormattedCoords(location)),40);
        }

        onlineShitlist.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    private void commandPreProcessor(PlayerCommandPreprocessEvent e){
        if(!isEnabled()) return;
        if(!disableCommands.get()) return;
        if(onlineShitlist.contains(e.getPlayer().getUniqueId())){
            for(String command : disabledCommands.get()){
                if(e.getMessage().toLowerCase().startsWith("/" + command.toLowerCase())){
                    e.setCancelled(true);
                    if(cmdAlertPlayers.get()){
                        sendMessage(e.getPlayer(), ChatColor.translateAlternateColorCodes('§', cmdMessage.get()));
                    }
                    if(cmdLog.get()){
                        info("Shitlisted player " + e.getPlayer().getName() + " tried to execute /" + command);
                    }
                    break;
                }
            }
        }
    }

    @EventHandler
    private void playerChat(AsyncPlayerChatEvent e){
        if(!isEnabled()) return;
        if(!swapWords.get()) return;
        if(onlineShitlist.contains(e.getPlayer().getUniqueId())){
            if((rng.nextInt(100) + 1) > replaceChance.get()) return;
            String[] words = e.getMessage().split(" ");
            words[rng.nextInt(words.length)] = wordsList.get().get(rng.nextInt(wordsList.get().size()));
            StringBuilder sb = new StringBuilder();
            for(String word : words){
                sb.append(word);
            }
            e.setMessage(sb.toString());
        }

    }

    public void addToShitlist(Player player){
        onlineShitlist.add(player.getUniqueId());
        PlayerInfo info = new PlayerInfo(player, true);

        Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
           if(MySQL.get().getPlayerStatsDriver().exists(player.getName())){
               MySQL.get().getPlayerStatsDriver().addToShitlist(info);
           }else{
               MySQL.get().getPlayerStatsDriver().createPlayer(info);
           }
        });
    }

    public boolean removeFromShitlist(Player player){
        if(!onlineShitlist.contains(player.getUniqueId())){
            return false;
        }
        onlineShitlist.remove(player.getUniqueId());
        final boolean[] playerExists = {false};
        MySQL.get().getPlayerStatsDriver().existsAsync(player.getName(), exists -> playerExists[0] = exists); //TODO: Test
        if(!playerExists[0]){
            MySQL.get().getPlayerStatsDriver().createPlayerAsync(player, false);
        }else{
            MySQL.get().getPlayerStatsDriver().removeFromShitlistAsync(player);
        }
        return true;
    }

}
