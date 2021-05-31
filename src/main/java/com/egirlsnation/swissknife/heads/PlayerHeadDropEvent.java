package com.egirlsnation.swissknife.heads;

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
