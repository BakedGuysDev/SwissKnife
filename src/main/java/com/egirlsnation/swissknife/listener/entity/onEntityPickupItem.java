package com.egirlsnation.swissknife.listener.entity;

import com.egirlsnation.swissknife.util.IllegalItemHandler;
import com.egirlsnation.swissknife.util.customItem.AnniversaryItemHanlder;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class onEntityPickupItem implements Listener {

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();
    private final AnniversaryItemHanlder anniversaryItemHanlder = new AnniversaryItemHanlder();

    @EventHandler
    private void onEntityPickupItemEvent(EntityPickupItemEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if(illegalItemHandler.handleIllegals(e.getItem(), player)){
            e.setCancelled(true);
        }

        if(anniversaryItemHanlder.isTpaToken(e.getItem().getItemStack())){
            e.setCancelled(true);
            e.getItem().remove();
            final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String command = "eco give " + player.getName() + " 1";
            Bukkit.dispatchCommand(console, command);
        }
    }
}
