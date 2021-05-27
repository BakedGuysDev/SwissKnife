package com.egirlsnation.swissknife.event.player;

import com.egirlsnation.swissknife.util.IllegalItemHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class onPlayerInteract implements Listener {

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e){

        if(e.getClickedBlock() != null && illegalItemHandler.handleIfSpawnEgg(e.getItem())){
            e.getPlayer().sendMessage("You cannot do this m8");
            ItemStack paper = new ItemStack(Material.PAPER);
            ItemMeta meta = paper.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "I fucked ya mom");
            paper.setItemMeta(meta);
            e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), paper);
            e.setCancelled(true);
        }
    }
}
