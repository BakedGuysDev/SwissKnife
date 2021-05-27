package com.egirlsnation.swissknife.event.player;

import com.egirlsnation.swissknife.util.CombatCheck;
import com.egirlsnation.swissknife.util.cooldownManager.CooldownManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class onLeave implements Listener {

    private final CooldownManager cooldownManager = new CooldownManager();
    private final CombatCheck combatCheck = new CombatCheck();

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        cooldownManager.removePlayer(e.getPlayer());
        combatCheck.removePlayer(e.getPlayer());
    }
}
