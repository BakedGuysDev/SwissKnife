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

package com.egirlsnation.swissknife.listener.player;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.event.PlayerPlaceCrystalEvent;
import com.egirlsnation.swissknife.util.IllegalItemHandler;
import com.egirlsnation.swissknife.util.customItem.CustomItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

import static com.egirlsnation.swissknife.SwissKnife.Config.enablePickaxe;

public class onPlayerInteract implements Listener {

    private final SwissKnife plugin;
    public onPlayerInteract(SwissKnife plugin){ this.plugin = plugin; }

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();
    private final CustomItemHandler customItemHandler = new CustomItemHandler();

    @EventHandler
    private void onPlayerInteractEvent(PlayerInteractEvent e){

        if(e.getClickedBlock() != null && illegalItemHandler.isSpawnEgg(e.getItem())){
            e.getPlayer().sendMessage(ChatColor.RED + "You cannot do this m8");
            e.getItem().setAmount(0);
            e.setCancelled(true);
        }

        if(e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if(customItemHandler.isDraconiteSword(e.getItem())){
                if(customItemHandler.getDisabledPlayersList().contains(e.getPlayer().getUniqueId())){
                    return;
                }
                customItemHandler.handleSwordAbility(e.getPlayer(), e.getHand());
                return;
            }else if(customItemHandler.isDraconiteAxe(e.getItem())){
                if(customItemHandler.getDisabledPlayersList().contains(e.getPlayer().getUniqueId())){
                    return;
                }
                customItemHandler.handleAxeAbility(e.getPlayer(), e.getHand(), plugin);
                return;
            }else if(customItemHandler.isDraconiteCrystal(e.getItem())){
                if(customItemHandler.getDisabledPlayersList().contains(e.getPlayer().getUniqueId())){
                    return;
                }
                customItemHandler.handleCrystalAbility(e.getPlayer(), e.getHand(), plugin);
                return;
            }
        }

        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if(customItemHandler.isDraconitePickaxe(e.getItem()) && enablePickaxe) {
                if (customItemHandler.getDisabledPlayersList().contains(e.getPlayer().getUniqueId())) {
                    return;
                }
                customItemHandler.handlePickaxeAbility(e.getPlayer(), e.getHand(), plugin);
                return;
            }
        }

        if(!Action.RIGHT_CLICK_BLOCK.equals(e.getAction())) return;
        if(e.getClickedBlock().getType().equals(Material.OBSIDIAN) || e.getClickedBlock().getType().equals(Material.BEDROCK) || e.getClickedBlock().getType().equals(Material.CRYING_OBSIDIAN)){
            if(e.getMaterial().equals(Material.END_CRYSTAL)){
                Bukkit.getScheduler().runTask(plugin, () -> {
                    List<Entity> entities = e.getPlayer().getNearbyEntities(4, 4, 4);

                    for(Entity entity : entities){
                        if(EntityType.ENDER_CRYSTAL.equals(entity.getType())){
                            EnderCrystal crystal = (EnderCrystal) entity;
                            Block belowCrystal = crystal.getLocation().getBlock().getRelative(BlockFace.DOWN);

                            if(e.getClickedBlock().equals(belowCrystal)){
                                PlayerPlaceCrystalEvent playerPlaceCrystalEvent = new PlayerPlaceCrystalEvent(e.getPlayer(), crystal, e.getItem());
                                Bukkit.getPluginManager().callEvent(playerPlaceCrystalEvent);
                                if(playerPlaceCrystalEvent.isCancelled()){
                                    e.setCancelled(true);
                                }
                                break;
                            }
                        }
                    }
                });
            }
        }
    }
}
