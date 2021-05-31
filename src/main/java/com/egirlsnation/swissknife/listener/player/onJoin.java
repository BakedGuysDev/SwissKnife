package com.egirlsnation.swissknife.listener.player;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.util.IllegalItemHandler;
import com.egirlsnation.swissknife.util.player.GamemodeUtil;
import com.egirlsnation.swissknife.util.player.RankUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class onJoin implements Listener {

    private final SwissKnife plugin;
    public onJoin(SwissKnife plugin){ this.plugin = plugin; }

    private final GamemodeUtil gamemodeUtil = new GamemodeUtil();
    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();
    private final RankUtil rankUtil = new RankUtil();

    @EventHandler(priority = EventPriority.HIGH)
    private void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                gamemodeUtil.ensureFlyDisable(player);
            }
        }, 20);

        if(!player.hasPlayedBefore()) return;

        Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                for(ItemStack item : player.getInventory().getContents()){
                    illegalItemHandler.handleIllegals(item, player);
                }
                rankUtil.promoteIfEligible(player);
            }
        });



    }
}
