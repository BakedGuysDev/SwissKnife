package com.egirlsnation.swissknife.listener.player;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.util.CombatCheck;
import com.egirlsnation.swissknife.util.cooldownManager.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class onLeave implements Listener {

    private final CooldownManager cooldownManager = new CooldownManager();
    private final CombatCheck combatCheck = new CombatCheck();

    private final SwissKnife plugin;
    public onLeave(SwissKnife plugin){ this.plugin = plugin; }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent e){
        cooldownManager.removePlayer(e.getPlayer());
        combatCheck.removePlayer(e.getPlayer());

        if(plugin.SQL.isConnected()){
            plugin.sqlQuery.updateValues(e.getPlayer());

            if(plugin.sqlQuery.isShitlisted(e.getPlayer())){
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "The coords of " + e.getPlayer().getDisplayName() + ChatColor.GREEN + " are " + getFormattedCoords(e.getPlayer().getLocation()));
                    }
                },40);
            }
        }

    }

    private String getFormattedCoords(Location loc){
        int x = (int) loc.getX();
        int y = (int) loc.getY();
        int z = (int) loc.getZ();

        return "X: " + x + " Y: " + y + " Z: " + z;
    }
}
