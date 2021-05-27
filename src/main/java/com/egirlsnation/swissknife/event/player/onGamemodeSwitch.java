package com.egirlsnation.swissknife.event.player;

import com.egirlsnation.swissknife.util.CombatCheck;
import com.egirlsnation.swissknife.util.player.GamemodeUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class onGamemodeSwitch implements Listener {

    private final GamemodeUtil gamemodeUtil = new GamemodeUtil();
    private final CombatCheck combatCheck = new CombatCheck();


    @EventHandler
    public void onGamemodeSwitchEvent (PlayerGameModeChangeEvent e){
        Player player = e.getPlayer();

        if(e.getNewGameMode().equals(GameMode.CREATIVE) && !player.hasPermission("swissknife.bypass.combat") && combatCheck.isInCombat(player)){
            player.sendMessage(ChatColor.RED + "You cannot do that command in combat. Time remaining: " + combatCheck.getRemainingTime(player) + " seconds");
            e.setCancelled(true);
        }
        gamemodeUtil.ensureFlyDisable(player);

    }
}
