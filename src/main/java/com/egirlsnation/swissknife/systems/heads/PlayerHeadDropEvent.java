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

package com.egirlsnation.swissknife.systems.heads;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerHeadDropEvent extends Event{

    private final Player killer;
    private final Player victim;
    private final ItemStack head;
    private final List<String> headLore;
    private final String broadcastedMessage;

    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerHeadDropEvent(Player killer, Player victim, ItemStack head, List<String> headLore, String broadcastedMessage){
        this.killer = killer;
        this.victim = victim;
        this.head = head;
        this.headLore = headLore;
        this.broadcastedMessage = broadcastedMessage;
    }

    @Override
    public HandlerList getHandlers(){
        return HANDLERS;
    }

    public Player getKiller(){
        return killer;
    }

    public Player getVictim(){
        return victim;
    }

    public ItemStack getHead(){
        return head;
    }

    public List<String> getHeadLore(){
        return headLore;
    }

    public String getBroadcastedMessage(){
        return broadcastedMessage;
    }

}
