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

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerCrystalKillEvent extends Event {
    private final Player killer;
    private final Player victim;
    private final Entity endCrystal;

    private final static HandlerList HANDLERS = new HandlerList();

    public PlayerCrystalKillEvent(Player killer, Player victim, Entity endCrystal){
        this.killer = killer;
        this.victim = victim;
        this.endCrystal = endCrystal;
    }

    public Player getKiller(){
        return killer;
    }

    public Player getVictim(){
        return victim;
    }

    public Entity getEndCrystal(){
        return endCrystal;
    }

    @Override
    public @NotNull HandlerList getHandlers(){
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
}
