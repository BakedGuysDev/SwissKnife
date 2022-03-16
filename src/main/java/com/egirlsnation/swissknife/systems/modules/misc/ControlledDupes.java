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

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class ControlledDupes extends Module {
    public ControlledDupes(){
        super(Categories.Misc, "controlled-dupes", "Adds back some dupes but you can control them this time");
    }

    private final SettingGroup sgCraftingDupe = settings.createGroup("crafting-dupe");

    private final Setting<Boolean> craftingEnabled = sgCraftingDupe.add(new BoolSetting.Builder()
            .name("enabled")
            .description("If the crafting dupe from 1.12 should be enabled")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> craftingStacking = sgCraftingDupe.add(new BoolSetting.Builder()
            .name("stacking")
            .description("If the crafting dupe should stack the items")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> craftingMaxStack = sgCraftingDupe.add(new IntSetting.Builder()
            .name("max-stack")
            .description("If stacking, how many items should be in the stack")
            .defaultValue(64)
            .range(2, 64)
            .build()
    );

    private final Setting<List<String>> craftingBlacklist = sgCraftingDupe.add(new StringListSetting.Builder()
            .name("blacklist")
            .description("Item materials that cannot be duped at all (supports regex)")
            .defaultValue(Arrays.asList(".*SHULKER_BOX$", Material.PLAYER_HEAD.toString()))
            .build()
    );

    List<UUID> startedCrafting = new ArrayList<>(1);

    @EventHandler
    private void PrepareCraftItem(PrepareItemCraftEvent e){
        if(!isEnabled()) return;

        if(craftingEnabled.get()){
            for(HumanEntity viewer : e.getViewers()){
                if(!(viewer instanceof Player)) continue;
                startedCrafting.add(viewer.getUniqueId());
            }
        }
    }

    @EventHandler
    private void PickUpEvent(EntityPickupItemEvent e){
        if(!isEnabled()) return;

        if(craftingEnabled.get()){
            if(!(e.getEntity() instanceof Player)) return;
            if(!startedCrafting.contains(e.getEntity().getUniqueId())) return;
            Player player = (Player) e.getEntity();
            for(String material : craftingBlacklist.get()){
                if(e.getItem().getItemStack().getType().toString().matches(material)) return;
            }

            Bukkit.getScheduler().runTaskLater(SwissKnife.INSTANCE, () -> {
                if(player.getInventory().getItemInMainHand().isSimilar(e.getItem().getItemStack())){
                    player.getInventory().getItemInMainHand().setAmount(craftingMaxStack.get());
                    startedCrafting.remove(player.getUniqueId());
                }
            }, 5);

        }
    }

    @EventHandler
    private void PlayerQuit(PlayerQuitEvent e){
        startedCrafting.remove(e.getPlayer().getUniqueId());
    }


}
