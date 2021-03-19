package com.egirlsnation.swissknife.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;

public class onPlayerChat implements Listener{

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSendMessage(AsyncPlayerChatEvent e){

        Random rng = new Random();

        if(e.getMessage().toLowerCase().contains("nigger")){
            int randomNum = rng.nextInt(100);
            if(randomNum > 80){
                e.setMessage("I suck dicks and I love it. Please let me suck your dick.");
            }
        }else if(e.getMessage().toLowerCase().contains("nigga")){
            int randomNum = rng.nextInt(100);
            if(randomNum > 80){
                e.setMessage(e.getMessage().replaceAll("(?i)nigga", "nice guy"));
            }
        }
    }
}
