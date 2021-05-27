package com.egirlsnation.swissknife.event.player;

import com.egirlsnation.swissknife.util.IllegalItemHandler;
import com.egirlsnation.swissknife.util.player.GamemodeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class onJoin implements Listener {

    private final GamemodeUtil gamemodeUtil = new GamemodeUtil();
    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        gamemodeUtil.ensureFlyDisable(player);

        if(!player.hasPlayedBefore()) return;
        for(ItemStack item : player.getInventory().getContents()){
            illegalItemHandler.handleIllegals(item, player);
        }

    }
}
