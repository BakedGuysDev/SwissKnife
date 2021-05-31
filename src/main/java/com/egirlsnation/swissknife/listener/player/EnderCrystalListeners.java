package com.egirlsnation.swissknife.listener.player;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.egirlsnation.swissknife.heads.HeadsHandler;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class EnderCrystalListeners implements Listener {

    private final Map<Player, EntityDamageEvent.DamageCause> lastDmgCause = new HashMap<>();
    private final Map<Player, Entity> lastAttacker = new HashMap<>();
    private final Map<EnderCrystal, Player> crystalExploder = new HashMap<>();

    private final HeadsHandler headsHandler = new HeadsHandler();


    @EventHandler
    private void onTick(ServerTickEndEvent e){
        crystalExploder.clear();
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent e){
        if(!(e.getEntity() instanceof  Player)) return;
        Player player = (Player) e.getEntity();

        lastDmgCause.put(player, e.getCause());
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof EnderCrystal && e.getDamager() instanceof Player){
            crystalExploder.put((EnderCrystal) e.getEntity(), (Player) e.getDamager());
        }

        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();

        lastDmgCause.put(player, e.getCause());
        lastAttacker.put(player, e.getDamager());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e){
        lastDmgCause.remove(e.getPlayer());
        lastAttacker.remove(e.getPlayer());
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e){
        Player player = e.getEntity();

        EntityDamageEvent.DamageCause cause = lastDmgCause.get(player);
        Entity attacker = lastAttacker.get(player);

        if(cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
            Player exploder = null;

            if(attacker instanceof EnderCrystal){
                exploder = crystalExploder.remove(attacker);
            }

            if(exploder != null){
                //Player died by an end crystal. Exploder is who killed him. e.getEntity() is who died.

                if(exploder.equals(player)) return;

                Bukkit.getLogger().info("Adding one kill to player");
                exploder.setStatistic(Statistic.PLAYER_KILLS, (exploder.getStatistic(Statistic.PLAYER_KILLS) + 1));

                if(attacker.getCustomName() == null) return;
                if(attacker.getCustomName().equals("Draconite Crystal")){
                    Bukkit.getLogger().info("Crystal was Draconite");
                    headsHandler.dropHeadIfLucky(player, exploder);
                }
            }
        }
    }
}
