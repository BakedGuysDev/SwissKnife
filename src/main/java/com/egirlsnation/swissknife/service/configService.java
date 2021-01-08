package com.egirlsnation.swissknife.service;

import com.egirlsnation.swissknife.swissKnife;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class configService {

    private static swissKnife swissknife = swissKnife.getInstance();
    static String message;
    static Boolean enabled;

    public static String getMessages(String path){
        try{
            message = ChatColor.translateAlternateColorCodes('&', swissknife.getConfig().getString(path));
        }catch(Exception e){
            Bukkit.getLogger().severe("Something went wrong while fetching a message from swissKnife config.");
            e.printStackTrace();
        }
        return message;
    }

    public static Boolean getEnabled(String path){
        try{
            enabled = swissknife.getConfig().getBoolean(path);
        }catch(Exception e){
            Bukkit.getLogger().severe("Something went wrong while fetching a message from swissKnife config.");
            e.printStackTrace();
        }
        return enabled;
    }
}
