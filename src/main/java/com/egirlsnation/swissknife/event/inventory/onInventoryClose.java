package com.egirlsnation.swissknife.event.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class onInventoryClose implements Listener {

    @EventHandler
    public void InventoryClose(InventoryCloseEvent e){
        if(e.getInventory().getType() != InventoryType.SHULKER_BOX) return;

        for(ItemStack item : e.getInventory().getContents()){
            if(item.getAmount() > item.getMaxStackSize()){
                item.setAmount(item.getMaxStackSize());
            }
        }
    }
}
