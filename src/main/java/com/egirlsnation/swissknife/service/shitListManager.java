package com.egirlsnation.swissknife.service;


import com.egirlsnation.swissknife.swissKnife;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class shitListManager {

    private swissKnife plugin = swissKnife.getInstance();

    public FileConfiguration shitListConfig;
    public File shitListFile;

    public void setup(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        shitListFile = new File(plugin.getDataFolder(), "shitlist.yml");

        if(!shitListFile.exists()){
            try{
                shitListFile.createNewFile();
            }catch (IOException e){
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create the shitlist.yml file!");
            }
        }

        shitListConfig = YamlConfiguration.loadConfiguration(shitListFile);
        Bukkit.getLogger().info(ChatColor.GREEN + "The shitlist.yml file was created.");
    }

    public FileConfiguration getShitListConfig(){
        return shitListConfig;
    }

    public void saveShitList(){
        try{
            shitListConfig.save(shitListFile);
            Bukkit.getLogger().info(ChatColor.GREEN + "shitlist.yml has been saved.");
        }catch (IOException e){
            Bukkit.getLogger().severe(ChatColor.RED + "Could not save the shitlist.yml file.");
        }
    }

    public void reloadShitList(){
        shitListConfig = YamlConfiguration.loadConfiguration(shitListFile);
        Bukkit.getLogger().info(ChatColor.GREEN + "shitlist.yml has been reloaded.");
    }
}
