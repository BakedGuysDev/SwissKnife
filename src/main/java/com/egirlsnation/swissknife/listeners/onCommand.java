package com.egirlsnation.swissknife.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;

public class onCommand implements Listener {
    private String[] commands = {"tpa", "tpahere", "tpayes", "tpaccept", "tpaaccept", "tpno", "tpano", "tpdeny", "tpadeny"};

    @EventHandler
    public void CommandPreProcessor (PlayerCommandPreprocessEvent e){
        for(String command : commands){
            if(e.getMessage().toLowerCase().startsWith("/"+ command.toLowerCase())){
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "You're on the shitlist for lagging. If you want to appeal msg Lerbiq on discord.");
            }
        }
    }
}
