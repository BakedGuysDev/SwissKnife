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

package com.egirlsnation.swissknife.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DropExcessEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private ItemStack droppedItem;
    private Inventory inventory;

    public DropExcessEvent(Player player, ItemStack droppedItem, Inventory inventory) {
        this.player = player;
        this.droppedItem = droppedItem;
        this.inventory = inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemDropped() {
        return droppedItem;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
