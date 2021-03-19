package com.egirlsnation.swissknife.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class onShulkerClose implements Listener {

    @EventHandler
    public void onInventoryCloseEvent (InventoryCloseEvent e){
        Inventory inv = e.getInventory();
        inv.getType();
        if(inv.getType() == InventoryType.SHULKER_BOX){
            for(ItemStack item :inv.getContents()){
                if(item != null){
                    if(item.getAmount() > item.getMaxStackSize()){
                        item.setAmount(item.getMaxStackSize());
                    }
                }
            }
        }

    }
}
