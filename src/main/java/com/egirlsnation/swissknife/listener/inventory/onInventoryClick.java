package com.egirlsnation.swissknife.listener.inventory;

import com.egirlsnation.swissknife.util.IllegalItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class onInventoryClick implements Listener {

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();

    @EventHandler
    private void InventoryClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)) return;

        List<ItemStack> items = new ArrayList<>();
        items.add(e.getCursor());
        items.add(e.getCurrentItem());

        for(ItemStack item : items){
            illegalItemHandler.handleIllegals(item, (Player) e.getWhoClicked());
        }
    }
}
