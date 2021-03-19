package com.egirlsnation.swissknife;

import com.egirlsnation.swissknife.commands.*;
import com.egirlsnation.swissknife.listeners.*;
import com.egirlsnation.swissknife.service.configService;
import com.egirlsnation.swissknife.service.shitListManager;
import com.egirlsnation.swissknife.service.swissknifeTabComplete;
import com.egirlsnation.swissknife.sql.mysqlService;
import com.egirlsnation.swissknife.sql.sqlQueryService;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class swissKnife extends JavaPlugin{

    public com.egirlsnation.swissknife.service.shitListManager shitListManager;

    public final onJoin onJoinListener = new onJoin(this);
    public final onQuit onQuitListener = new onQuit(this);

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
    public static Boolean allDisabled;

    public static  Map<String, String> shitlist = new HashMap<String, String>();

    public shitListManager slManager;

    public mysqlService SQL;
    public sqlQueryService sqlService;



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
        allDisabled = configService.getEnabled("Patches.allDisabled");

        registerCommands();
        registerEvents();

        this.SQL = new mysqlService();
        this.sqlService = new sqlQueryService(this);



        try {
            SQL.connect();
        } catch (ClassNotFoundException e) {
            getLogger().severe("Something went massively wrong while connecting to the database.");
            getLogger().severe("Something went massively wrong while connecting to the database.");
            getLogger().severe("Something went massively wrong while connecting to the database.");
            getLogger().severe("Massive stacktrace may follow.");
            e.printStackTrace();
        } catch (SQLException throwables) {
            getLogger().severe("Something went massively wrong while connecting to the database.");
            getLogger().severe("Something went massively wrong while connecting to the database.");
            getLogger().severe("Something went massively wrong while connecting to the database.");
            getLogger().severe("Massive stacktrace may follow.");
            throwables.printStackTrace();
        }

        if(SQL.isConnected()){
            getLogger().info(ChatColor.GREEN + "Successfully connected to the database!");
            sqlService.createTable();
        }

        getLogger().info("Swiss Knife plugin by Lerbiq, Killmlana and StoneTrench enabled.");
    }

    @Override
    public void onDisable(){
        if(!shitlist.isEmpty()){
            saveShitList();
        }
        SQL.disconnect();
        getLogger().info("Swiss Knife plugin disabled.");
    }


    public void registerEvents(){
        getServer().getPluginManager().registerEvents(onJoinListener, this);
        getServer().getPluginManager().registerEvents(onQuitListener, this);
        /*if(getConfig().getBoolean("Jihads.Enabled")){
            getServer().getPluginManager().registerEvents(new onSnowballHit(), this);
        }*/
        getServer().getPluginManager().registerEvents(new onSnowballHit(), this);
        getServer().getPluginManager().registerEvents(new onPlayerDeath(), this);
        if(getConfig().getBoolean("Patches.DragonSpawnGlitchEnabled")){
            getServer().getPluginManager().registerEvents(new onPlaceBlock(), this);
        }
        getServer().getPluginManager().registerEvents(new onEntityPickUpItem(), this);
        if(getConfig().getBoolean("Patches.32kEnabled")){
            getServer().getPluginManager().registerEvents(new onInventoryOpen(), this);
            getServer().getPluginManager().registerEvents(new onInventoryClick(), this);
        }
        getServer().getPluginManager().registerEvents(new entityPortalTeleportEvent(), this);
        getServer().getPluginManager().registerEvents(new onEntitySpawn(), this);
        getServer().getPluginManager().registerEvents(new onShulkerClose(), this);
        getServer().getPluginManager().registerEvents(new onSwapHandsItem(), this);
        getServer().getPluginManager().registerEvents(new onRespawn(), this);
        getServer().getPluginManager().registerEvents(new onBookClose(), this);
        getServer().getPluginManager().registerEvents(new onGamemodeSwitch(), this);
        getServer().getPluginManager().registerEvents(new onPlayerTeleport(), this);
        getServer().getPluginManager().registerEvents(new onPlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new onBlockDispense(), this);
        getServer().getPluginManager().registerEvents(new onPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new onCommand(), this);
        getServer().getPluginManager().registerEvents(new onVehicleCreate(), this);
        getServer().getPluginManager().registerEvents(new onEntityDamage(), this);
        //getServer().getPluginManager().registerEvents(new onPistonMove(), this);
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
