/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.systems.modules.player;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.player.GamemodeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class RestrictedCreativeAddon extends Module {
    public RestrictedCreativeAddon() {
        super("restricted-creative-addon", "Additional checks for RestrictedCreative to make it safe-ish");
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e){
        if(!e.getPlayer().isOp() && e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent e){
        if(!Action.RIGHT_CLICK_BLOCK.equals(e.getAction())) return;
        if(e.getMaterial().equals(Material.TNT_MINECART) && e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && !e.getPlayer().hasPermission("swissknife.bypass.creative")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onGamemodeSwitchEvent (PlayerGameModeChangeEvent e){
        Player player = e.getPlayer();

        //TODO: Combat check
        if(e.getNewGameMode().equals(GameMode.CREATIVE)){
            if(!player.hasPermission("swissknife.bypass.combat") && combatCheckHandler.isInCombat(player)){
                player.sendMessage(ChatColor.RED + "You cannot do that command in combat. Time remaining: " + combatCheckHandler.getRemainingTime(player) + " seconds");
                e.setCancelled(true);
            }
        }else{
            GamemodeUtil.removeClickedItem(player);
        }

        Bukkit.getScheduler().runTaskLater(SwissKnife.getPlugin(SwissKnife.class), () -> GamemodeUtil.ensureFlyDisable(player), 10);
    }
}
