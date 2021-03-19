package com.egirlsnation.swissknife.listeners;

import com.egirlsnation.swissknife.service.radiusManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static com.egirlsnation.swissknife.swissKnife.shitlist;

public class onCommand implements Listener {
    private String[] commands = {"tpa", "tpahere", "tpayes", "tpaccept", "tpaaccept", "tpno", "tpano", "tpdeny", "tpadeny"};
    radiusManager radiusManager = new radiusManager();

    @EventHandler
    public void CommandPreProcessor (PlayerCommandPreprocessEvent e){

        if(shitlist.containsKey(e.getPlayer().getUniqueId().toString())){
            for(String command : commands){
                if(e.getMessage().toLowerCase().startsWith("/"+ command.toLowerCase())){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "You're on the shitlist so you can't do this. If you want to appeal msg Lerbiq on discord.");
                }
            }
        }else if(!radiusManager.isInRadius(e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getZ(), 2000)){
            for(String command : commands){
                if(e.getMessage().toLowerCase().startsWith("/"+ command.toLowerCase())){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "You need to be further from spawn in order to be able to use tpa.");
                }
            }
        }
    }
}
