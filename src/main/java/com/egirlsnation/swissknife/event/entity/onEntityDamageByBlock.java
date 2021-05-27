package com.egirlsnation.swissknife.event.entity;

import com.egirlsnation.swissknife.util.CombatCheck;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class onEntityDamageByBlock implements Listener {

    private final CombatCheck combatCheck = new CombatCheck();

    @EventHandler
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        EntityDamageEvent.DamageCause cause = e.getCause();

        if(cause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)){
            combatCheck.addToCombatMap(player);
        }
    }
}
