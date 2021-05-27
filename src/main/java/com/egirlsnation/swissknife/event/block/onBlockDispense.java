package com.egirlsnation.swissknife.event.block;

import com.egirlsnation.swissknife.util.IllegalItemHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class onBlockDispense implements Listener {

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();

    @EventHandler
    public void onBlockDispenseEvent(BlockDispenseEvent e){

        if(illegalItemHandler.handleIfSpawnEgg(e.getItem())) {
            ItemStack paper = new ItemStack(Material.PAPER);
            ItemMeta meta = paper.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "I fucked ya mom");
            paper.setItemMeta(meta);
            e.setItem(paper);
            return;
        }

        if(illegalItemHandler.isOverEnchanted(e.getItem())){
            e.setItem(illegalItemHandler.getIllegalItemReplacement());
            return;
        }
    }
}
