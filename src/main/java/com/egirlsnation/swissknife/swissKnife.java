package com.egirlsnation.swissknife;

import com.egirlsnation.swissknife.commands.*;
import com.egirlsnation.swissknife.listeners.*;
import com.egirlsnation.swissknife.service.*;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class swissKnife extends JavaPlugin{

    public static Map<UUID, Long> users;
    public static Map<UUID, Boolean> itemPickUpUsers;
    public static String killCooldownMsg;
    public static long killCooldown;
    public static swissKnife instance;

    //Config options
    public static String reloadMessage;
    public static Boolean headsAncientOnly;
    public static Boolean jihadsEnabled;
    public static Boolean patch32kEnabled;
    public static Boolean DragonPatchEnabled;

    @Override
    public void onEnable(){
        instance = this;
        saveDefaultConfig();


        users = new ConcurrentHashMap<UUID, Long>();
        itemPickUpUsers = new ConcurrentHashMap<UUID, Boolean>();


        reloadMessage = configService.getMessages("reload-message");
        headsAncientOnly = configService.getEnabled("Heads.ancientWeaponsOnly");
        jihadsEnabled = configService.getEnabled("Jihads.Enabled");
        DragonPatchEnabled = configService.getEnabled("Patches.DragonSpawnGlitchEnabled");
        patch32kEnabled = configService.getEnabled("Patches.32kEnabled");

        registerCommands();
        registerEvents();

        getLogger().info("Swiss Knife plugin by Lerbiq, Killmlana and StoneTrench enabled.");
    }

    @Override
    public void onDisable(){
        getLogger().info("Swiss Knife plugin disabled.");
    }


    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new onJoin(), this);
        if(getConfig().getBoolean("Jihads.Enabled")){
            getServer().getPluginManager().registerEvents(new onSnowballHit(), this);
        }
        getServer().getPluginManager().registerEvents(new onPlayerDeath(), this);
        if(getConfig().getBoolean("Patches.DragonSpawnGlitchEnabled")){
            getServer().getPluginManager().registerEvents(new onObsidianPlace(), this);
        }
        getServer().getPluginManager().registerEvents(new onEntityPickUpItem(), this);
        if(getConfig().getBoolean("Patches.32kEnabled")){
            getServer().getPluginManager().registerEvents(new onInventoryOpen(), this);
            getServer().getPluginManager().registerEvents(new onInventoryClick(), this);
        }
    }

    public void registerCommands(){
        this.getCommand("shrug").setExecutor(new shrug());
        this.getCommand("playtime").setExecutor(new playtime());
        this.getCommand("kill").setExecutor(new kill());
        this.getCommand("swissknife").setExecutor(new swissknifeCommand());
    }

    public static swissKnife getInstance(){
        return instance;
    }




}
