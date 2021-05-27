package com.egirlsnation.swissknife.event.inventory;

import com.egirlsnation.swissknife.util.IllegalItemHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class onInventoryOpen implements Listener {

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();
    @EventHandler
    public void InventoryOpen(InventoryOpenEvent e){
        for(ItemStack item : e.getInventory().getContents()){
            illegalItemHandler.handleIllegals(item);
        }
    }
}
