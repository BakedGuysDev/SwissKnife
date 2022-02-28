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

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnarchyPvpLimiter extends Module {

    //TODO: Test

    public AnarchyPvpLimiter(){
        super(Categories.Player, "anarchy-pvp-limiter", "Applies limits to certain anarchy pvp mechanics");
    }

    private final SettingGroup sgCrystal = settings.createGroup("crystals");

    private final Setting<Boolean> limitCrystals = sgCrystal.add(new BoolSetting.Builder()
            .name("enabled")
            .description("If the plugin should limit crystal break speed")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> crystalDelay = sgCrystal.add(new IntSetting.Builder()
            .name("delay")
            .description("How many miliseconds before breaking another crystal")
            .defaultValue(200)
            .build()
    );

    private final Setting<Boolean> preventInstaBreak = sgCrystal.add(new BoolSetting.Builder()
            .name("prevent-insta-break")
            .description("Prevents placing and destroying crystals at the same tick")
            .defaultValue(true)
            .build()
    );


    private final SettingGroup sgAnchor = settings.createGroup("anchors");

    private final Setting<Boolean> limitAnchors = sgAnchor.add(new BoolSetting.Builder()
            .name("enabled")
            .description("If the plugin should limit how fast can player explode anchor")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> anchorDelay = sgAnchor.add(new IntSetting.Builder()
            .name("delay")
            .description("How many miliseconds before exploding another anchor")
            .defaultValue(200)
            .build()
    );

    private final SettingGroup sgBed = settings.createGroup("beds");

    private final Setting<Boolean> limitBeds = sgBed.add(new BoolSetting.Builder()
            .name("enabled")
            .description("If the plugin should limit how fast can player explode beds")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> bedDelay = sgBed.add(new IntSetting.Builder()
            .name("delay")
            .description("How many miliseconds before exploding another bed")
            .defaultValue(200)
            .build()
    );


    private final static Map<UUID, Long> crystalMap = new HashMap<>();
    private final static Map<UUID, Long> anchorMap = new HashMap<>();
    private final static Map<UUID, Long> bedMap = new HashMap<>();

    @EventHandler
    private void playerDamageEntity(EntityDamageByEntityEvent e){
        if(!isEnabled()) return;
        if(!limitCrystals.get()) return;
        if(!e.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)) return;
        if(!(e.getDamager() instanceof Player)) return;
        if(preventInstaBreak.get() && e.getEntity().getTicksLived() < 1){
            e.setCancelled(true);
            return;
        }
        Player player = (Player) e.getDamager();

        UUID uuid = player.getUniqueId();
        if(!crystalMap.containsKey(uuid)){
            crystalMap.put(uuid, System.currentTimeMillis());
        }else{
            long timeLeft = System.currentTimeMillis() - crystalMap.get(uuid);
            if(timeLeft < crystalDelay.get()){
                e.setCancelled(true);
            }else{
                crystalMap.put(uuid, System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    private void playerInteract(PlayerInteractEvent e){
        if(!isEnabled()) return;
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(e.getClickedBlock() == null) return;

        if(limitAnchors.get() && e.getClickedBlock().getType().equals(Material.RESPAWN_ANCHOR) && !e.getClickedBlock().getWorld().getEnvironment().equals(World.Environment.NETHER)){
            Player player = e.getPlayer();
            UUID uuid = player.getUniqueId();

            RespawnAnchor anchor = (RespawnAnchor) e.getClickedBlock().getBlockData();
            if(canAnchorExplode(anchor, e.getHand(), player)){
                if(!anchorMap.containsKey(uuid)){
                    anchorMap.put(uuid, System.currentTimeMillis());
                }else{
                    long timeLeft = System.currentTimeMillis() - anchorMap.get(uuid);
                    if(timeLeft < anchorDelay.get()){
                        e.setCancelled(true);
                        SwissKnife.swissLogger.debug("prevented anchor");
                    }else{
                        anchorMap.put(uuid, System.currentTimeMillis());
                    }
                }
            }
        }


        if(limitBeds.get()){
            if(!ItemUtil.isBed(e.getClickedBlock())) return;
            if(e.getClickedBlock().getWorld().getEnvironment().equals(World.Environment.NORMAL)) return;

            Player player = (Player) e.getPlayer();
            UUID uuid = player.getUniqueId();

            if(!bedMap.containsKey(uuid)){
                bedMap.put(uuid, System.currentTimeMillis());
            }else{
                long timeLeft = System.currentTimeMillis() - bedMap.get(uuid);
                if(timeLeft < bedDelay.get()){
                    e.setCancelled(true);
                }else{
                    bedMap.put(uuid, System.currentTimeMillis());
                }
            }
        }
    }

    private boolean canAnchorExplode(RespawnAnchor anchor, EquipmentSlot hand, Player player){
        if(anchor.getCharges() == 0) return false;
        if(anchor.getCharges() == 4) return true;
        if(player.getInventory().getItem(hand) == null) return true;
        return !player.getInventory().getItem(hand).getType().equals(Material.GLOWSTONE);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e){
        crystalMap.remove(e.getPlayer().getUniqueId());
        anchorMap.remove(e.getPlayer().getUniqueId());
        bedMap.remove(e.getPlayer().getUniqueId());
    }
}
