package com.egirlsnation.swissknife.listener.inventory;

import com.egirlsnation.swissknife.util.IllegalItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class onInventoryClick implements Listener {

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();

    @EventHandler
    private void InventoryClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)) return;

        if(e.getClickedInventory() == null) return;
        ItemStack[] items = e.getClickedInventory().getContents();

        for(ItemStack item : items){
            if(illegalItemHandler.handleIllegals(item, (Player) e.getWhoClicked())){
                e.setCancelled(true);
                return;
            }
        }
    }
}
