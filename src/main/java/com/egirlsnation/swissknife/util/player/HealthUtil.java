package com.egirlsnation.swissknife.util.player;

import com.egirlsnation.swissknife.SwissKnife;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class HealthUtil {

    public final void killPlayer(Player player, SwissKnife swissKnife){
        final EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, Float.MAX_VALUE);
        swissKnife.getPluginManager().callEvent(ede);
        ede.getEntity().setLastDamageCause(ede);
        player.setHealth(0);
    }
}
