package com.egirlsnation.swissknife.command;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.util.SpawnRadiusManager;
import com.egirlsnation.swissknife.util.cooldownManager.CommandType;
import com.egirlsnation.swissknife.util.cooldownManager.CooldownManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.concurrent.TimeUnit;

import static com.egirlsnation.swissknife.SwissKnife.Config.spawnRadius;

public class CommandPreProcessor  implements Listener {

    private final SwissKnife plugin;
    public CommandPreProcessor(SwissKnife plugin){ this.plugin = plugin; }

    private final CooldownManager cooldownManager = new CooldownManager();
    private final SpawnRadiusManager radiusManager = new SpawnRadiusManager();

    private final String[] commands = {"tpa", "tpahere", "tpayes", "tpaccept", "tpaaccept", "tpno", "tpano", "tpdeny", "tpadeny", "tpyes"};

    @EventHandler
    public void CommandPreProcessorEvent(PlayerCommandPreprocessEvent e){

        if(e.getMessage().toLowerCase().startsWith("/afk")){
            CommandType type = CommandType.AFK;
            Player player = e.getPlayer();

            // Getting time left for this player for this command
            long timeLeft = System.currentTimeMillis() - cooldownManager.getCommandInfo(player.getUniqueId(), type).getCooldown();

            if(player.hasPermission("swissknife.cooldown.bypass")){
                return;
            }else if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownManager.DEFAULT_COOLDOWN){
                cooldownManager.setCooldown(player.getUniqueId(), System.currentTimeMillis(), type);
                return;
            }
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You gotta wait before doing that m8");
            return;
        }

        if(e.getMessage().toLowerCase().startsWith("/me")){
            CommandType type = CommandType.ME;
            Player player = e.getPlayer();

            // Getting time left for this player for this command
            long timeLeft = System.currentTimeMillis() - cooldownManager.getCommandInfo(player.getUniqueId(), type).getCooldown();

            if(player.hasPermission("swissknife.cooldown.bypass")){
                return;
            }else if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownManager.DEFAULT_COOLDOWN){
                cooldownManager.setCooldown(player.getUniqueId(), System.currentTimeMillis(), type);
                return;
            }
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You gotta wait before doing that m8");
            return;
        }

        if(radiusManager.isInRadius(e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getZ(), spawnRadius)){
            for(String command : commands){
                if(e.getMessage().toLowerCase().startsWith("/" + command.toLowerCase())){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "You need to be further from spawn in order to do this command.");
                }
            }
        }

        if(!plugin.SQL.isConnected()){
            return;
        }

        if(plugin.sqlQuery.isShitlisted(e.getPlayer())){
            for(String command : commands){
                if(e.getMessage().toLowerCase().startsWith("/" + command.toLowerCase())){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "You are shitlisted and can't do this command.");
                }
            }
        }
    }
}
