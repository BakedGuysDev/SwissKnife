package com.egirlsnation.swissknife.util.player;

import org.bukkit.entity.Player;

public class GamemodeUtil {

    public void ensureFlyDisable(Player player){
        if(!player.hasPermission("swissknife.bypass.fly")){
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }
}
