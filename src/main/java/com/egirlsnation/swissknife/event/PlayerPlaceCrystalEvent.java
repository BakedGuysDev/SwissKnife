/*
 * This file is part of the SwissKnife plugin distibution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.event;

import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerPlaceCrystalEvent extends Event {

    private final Player player;
    private final EnderCrystal enderCrystal;
    private final ItemStack crystalItem;

    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerPlaceCrystalEvent(Player player, EnderCrystal enderCrystal, ItemStack crystalItem){
        this.player = player;
        this.enderCrystal = enderCrystal;
        this.crystalItem = crystalItem;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }

    public Player getPlayer(){
        return player;
    }

    public EnderCrystal getCrystal(){
        return enderCrystal;
    }

    public ItemStack getCrystalItem(){
        return crystalItem;
    }

    public Location getLocation(){
        return enderCrystal.getLocation();
    }
}
