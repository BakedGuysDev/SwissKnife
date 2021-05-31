package com.egirlsnation.swissknife.listener.player;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.util.CombatCheck;
import com.egirlsnation.swissknife.util.player.GamemodeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class onGamemodeSwitch implements Listener {

    private final SwissKnife plugin;
    public onGamemodeSwitch(SwissKnife plugin){ this.plugin = plugin; }

    private final GamemodeUtil gamemodeUtil = new GamemodeUtil();
    private final CombatCheck combatCheck = new CombatCheck();


    @EventHandler
    private void onGamemodeSwitchEvent (PlayerGameModeChangeEvent e){
        Player player = e.getPlayer();

        if(e.getNewGameMode().equals(GameMode.CREATIVE)){
            if(player.hasPermission("swissknife.bypass.combat")) return;
            if(combatCheck.isInCombat(player)){
                player.sendMessage(ChatColor.RED + "You cannot do that command in combat. Time remaining: " + combatCheck.getRemainingTime(player) + " seconds");
                e.setCancelled(true);
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                gamemodeUtil.ensureFlyDisable(player);
            }
        }, 10);

    }
}
