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

package com.egirlsnation.swissknife.listeners.player;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.api.IllegalItemHandler;
import com.egirlsnation.swissknife.events.PlayerPlaceCrystalEvent;
import com.egirlsnation.swissknife.systems.handlers.customItems.CustomItemHandler;
import com.egirlsnation.swissknife.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class onPlayerInteract implements Listener {

    private final SwissKnife plugin;
    public onPlayerInteract(SwissKnife plugin){ this.plugin = plugin; }

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();
    private final CustomItemHandler customItemHandler = new CustomItemHandler();

    private final static Map<UUID, Long> crystalMap = new HashMap<>();

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

        if(e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.DEBUG_STICK)){
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 20, 0));

        }

        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if(customItemHandler.isDraconitePickaxe(e.getItem()) && Config.instance.enablePickaxe) {
                if (customItemHandler.getDisabledPlayersList().contains(e.getPlayer().getUniqueId())) {
                    return;
                }
                customItemHandler.handlePickaxeAbility(e.getPlayer(), e.getHand(), plugin);
                return;
            }
        }

        if(!Action.RIGHT_CLICK_BLOCK.equals(e.getAction())) return;
        if(e.getMaterial().equals(Material.TNT_MINECART) && e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && !e.getPlayer().hasPermission("swissknife.bypass.creative")){
            e.setCancelled(true);
            return;
        }
        if(e.getClickedBlock().getType().equals(Material.OBSIDIAN) || e.getClickedBlock().getType().equals(Material.BEDROCK) || e.getClickedBlock().getType().equals(Material.CRYING_OBSIDIAN)){
            if(e.getMaterial().equals(Material.END_CRYSTAL)){

                if(Config.instance.limitCrystalPlacementSpeed){
                    UUID uuid = e.getPlayer().getUniqueId();
                    if(!crystalMap.containsKey(uuid)){
                        crystalMap.put(uuid, System.currentTimeMillis());
                    }else{
                        long timeLeft = System.currentTimeMillis() - crystalMap.get(uuid);
                        if(timeLeft < Config.instance.crystalDelay){
                            e.setCancelled(true);
                            return;
                        }else{
                            crystalMap.put(uuid, System.currentTimeMillis());
                        }
                    }
                }

                Bukkit.getScheduler().runTask(plugin, () -> {
                    List<Entity> entities = e.getPlayer().getNearbyEntities(3, 3, 3);

                    for(Entity entity : entities){
                        if(EntityType.ENDER_CRYSTAL.equals(entity.getType())){
                            EnderCrystal crystal = (EnderCrystal) entity;
                            Block belowCrystal = crystal.getLocation().getBlock().getRelative(BlockFace.DOWN);

                            if(e.getClickedBlock().equals(belowCrystal)){
                                Bukkit.getPluginManager().callEvent(new PlayerPlaceCrystalEvent(e.getPlayer(), crystal, e.getItem()));
                                break;
                            }
                        }
                    }
                });
            }
        }
    }
}
