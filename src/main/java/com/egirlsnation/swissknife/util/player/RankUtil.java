package com.egirlsnation.swissknife.util.player;

import com.egirlsnation.swissknife.hooks.votingPlugin.UserUtils;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;

public class RankUtil {

    private final UserUtils userUtils = new UserUtils();

    public void promoteIfEligible(@NotNull Player player){

        if(!player.hasPlayedBefore()) return;

        //Name "PLAY_ONE_MINUTE" is missleading. It's actually in ticks.
        int pt = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20 / 60 / 60;
        final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        if(pt >= (midfagHours * 240) && !player.hasPermission("egirls.rank.vet")){ //Hours to ticks
            String command = "lp user " + player.getName() + " parent add veteran";
            Bukkit.dispatchCommand(console, command);
            Bukkit.getServer().broadcastMessage(player.getDisplayName() + ChatColor.GREEN + " reached " + ChatColor.YELLOW + "MidFag" + ChatColor.GREEN + "!");
        }

        if(pt >= (oldfagHours * 240) && !player.hasPermission("egirls.rank.oldfag")){ //Hours to ticks
            String command = "lp user " + player.getName() + " parent add oldfag";
            Bukkit.dispatchCommand(console, command);
            Bukkit.getServer().broadcastMessage(player.getDisplayName() + ChatColor.GREEN + " reached " + ChatColor.RED + "OldFag" + ChatColor.GREEN + "!");
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 100, 0);
        }

        if(userUtils.getUserManager() == null) return;
        if(pt >= (elderfagHours * 240) && userUtils.getVotes(player) >= 300 && !player.hasPermission("egirls.rank.legend")){ //Hours to ticks
            String command = "lp user " + player.getName() + " parent add legend";
            Bukkit.dispatchCommand(console, command);
            Bukkit.getServer().broadcastMessage(player.getDisplayName() + ChatColor.GREEN + " reached " + ChatColor.AQUA + "ElderFag" + ChatColor.GREEN + "!");
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 100, 0);
            }
        }
    }
}
