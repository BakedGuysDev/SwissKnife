package com.egirlsnation.swissknife;

import com.egirlsnation.swissknife.commands.*;
import com.egirlsnation.swissknife.listeners.*;
import com.egirlsnation.swissknife.service.*;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class swissKnife extends JavaPlugin{

    public com.egirlsnation.swissknife.service.shitListManager shitListManager;

    public static Map<UUID, Long> users;
    public static Map<UUID, Boolean> itemPickUpUsers;
    public static Map<UUID, Boolean> swimmingList;
    public static String killCooldownMsg;
    public static long killCooldown;
    public static swissKnife instance;

    //Config options
    public static String reloadMessage;
    public static Boolean headsAncientOnly;
    public static Boolean jihadsEnabled;
    public static Boolean patch32kEnabled;
    public static Boolean DragonPatchEnabled;

    public static  Map<String, String> shitlist = new HashMap<String, String>();

    public shitListManager slManager;

    @Override
    public void onEnable(){
        instance = this;
        saveDefaultConfig();
        loadShitListManager();
        if(slManager.getShitListConfig().contains("data")){
            restoreShitList();
        }



        users = new ConcurrentHashMap<UUID, Long>();
        itemPickUpUsers = new ConcurrentHashMap<UUID, Boolean>();
        swimmingList = new ConcurrentHashMap<UUID, Boolean>();



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
        if(!shitlist.isEmpty()){
            saveShitList();
        }
        getLogger().info("Swiss Knife plugin disabled.");
    }


    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new onJoin(), this);
        if(getConfig().getBoolean("Jihads.Enabled")){
            getServer().getPluginManager().registerEvents(new onSnowballHit(), this);
        }
        getServer().getPluginManager().registerEvents(new onPlayerDeath(), this);
        if(getConfig().getBoolean("Patches.DragonSpawnGlitchEnabled")){
            getServer().getPluginManager().registerEvents(new onPlaceBlock(), this);
        }
        getServer().getPluginManager().registerEvents(new onEntityPickUpItem(), this);
        if(getConfig().getBoolean("Patches.32kEnabled")){
            getServer().getPluginManager().registerEvents(new onInventoryOpen(), this);
            getServer().getPluginManager().registerEvents(new onInventoryClick(), this);
        }
        getServer().getPluginManager().registerEvents(new onEntityPortalExit(), this);
        getServer().getPluginManager().registerEvents(new onEntitySpawn(), this);
        getServer().getPluginManager().registerEvents(new onSwitchItem(), this);
        getServer().getPluginManager().registerEvents(new onShulkerClose(), this);
        getServer().getPluginManager().registerEvents(new onSwapHandsItem(), this);
        getServer().getPluginManager().registerEvents(new onRespawn(), this);
        getServer().getPluginManager().registerEvents(new onBookClose(), this);
        getServer().getPluginManager().registerEvents(new onGamemodeSwitch(), this);
    }

    public void registerCommands(){
        this.getCommand("shrug").setExecutor(new shrug());
        this.getCommand("playtime").setExecutor(new playtime());
        this.getCommand("kill").setExecutor(new kill());
        this.getCommand("swissknife").setExecutor(new swissknifeCommand());
        this.getCommand("swissknife").setTabCompleter(new swissknifeTabComplete());
        this.getCommand("ping").setExecutor(new ping());
    }


    public static swissKnife getInstance(){
        return instance;
    }


    public void loadShitListManager(){
        slManager = new shitListManager();
        slManager.setup();
        slManager.saveShitList();
        slManager.reloadShitList();
    }

    public void saveShitList(){
        getLogger().warning(shitlist.toString());
        slManager.getShitListConfig().set("data", null);
        for(Map.Entry<String, String> entry : shitlist.entrySet()){
            slManager.getShitListConfig().set("data." + entry.getKey(), entry.getValue());
        }
        slManager.saveShitList();
    }

    public void restoreShitList(){
        slManager.getShitListConfig().getConfigurationSection("data").getKeys(false).forEach(key ->{
            String content = slManager.getShitListConfig().get("data." + key).toString();
            shitlist.put(key, content);
        });
    }


}
