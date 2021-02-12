package com.egirlsnation.swissknife.service;

import com.egirlsnation.swissknife.swissKnife;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class swissknifeTabComplete implements TabCompleter {
    private static swissKnife swissknife = swissKnife.getInstance();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("swissknife") && args.length ==1){
            if(sender instanceof Player){

                List<String> list = new ArrayList<>();
                list.add("togglepickup");
                list.add("shitlist");
                list.add("toggleswim");
                return list;
            }
        }
        if(cmd.getName().equalsIgnoreCase("swissknife") && args.length ==2 && args[0].equalsIgnoreCase("shitlist")){
            if(sender instanceof Player){
                List<String> list = new ArrayList<>();
                list.add("add");
                list.add("remove");
                return list;
            }
        }

        /*if(cmd.getName().equalsIgnoreCase("swissknife") && args.length ==3 && args[0].equalsIgnoreCase("add")){
            if(sender instanceof Player){
                List<String> playerList = new ArrayList<>();

                return playerList;
            }
        }*/

        return null;
    }
}
