package com.egirlsnation.swissknife.listener.block;

import com.egirlsnation.swissknife.util.IllegalItemHandler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class onBlockDispense implements Listener {

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();

    @EventHandler
    private void onBlockDispenseEvent(BlockDispenseEvent e){

        if(illegalItemHandler.isOverEnchanted(e.getItem())) {
            if(e.getItem().getType().equals(Material.NETHERITE_SWORD)) return;
            if(e.getItem().getType().equals(Material.NETHERITE_AXE)) return;
            e.setItem(illegalItemHandler.getIllegalItemReplacement());
            return;
        }

        if(illegalItemHandler.isSpawnEgg(e.getItem())){
            e.setItem(illegalItemHandler.getIllegalItemReplacement());
            return;
        }
    }
}
