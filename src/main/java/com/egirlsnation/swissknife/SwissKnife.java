package com.egirlsnation.swissknife;

import com.egirlsnation.swissknife.command.*;
import com.egirlsnation.swissknife.listener.block.onBlockDispense;
import com.egirlsnation.swissknife.listener.block.onBlockPlace;
import com.egirlsnation.swissknife.listener.entity.*;
import com.egirlsnation.swissknife.listener.inventory.onInventoryClick;
import com.egirlsnation.swissknife.listener.inventory.onInventoryClose;
import com.egirlsnation.swissknife.listener.inventory.onInventoryOpen;
import com.egirlsnation.swissknife.listener.player.*;
import com.egirlsnation.swissknife.sql.MySQL;
import com.egirlsnation.swissknife.sql.SqlQuery;
import com.egirlsnation.swissknife.util.player.PingUtil;
import me.affanhaq.keeper.Keeper;
import me.affanhaq.keeper.data.ConfigFile;
import me.affanhaq.keeper.data.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class SwissKnife extends JavaPlugin {

    private final PluginManager pluginManager = Bukkit.getPluginManager();
    public static final Logger LOGGER = Bukkit.getLogger();

    public MySQL SQL;
    public SqlQuery sqlQuery;

    private final PingUtil pingUtil = new PingUtil();

    @Override
    public void onEnable(){
        LOGGER.info("Loading config handler.");
        new Keeper(this).register(new Config()).load();

        registerEvents();
        registerCommands();

        initSQL();


    }

    @Override
    public void onDisable(){
        SQL.disconnect();
        getLogger().info(ChatColor.GREEN + "Swiss Knife plugin disabled.");
    }

    private void registerEvents(){
        pluginManager.registerEvents(new CommandPreProcessor(this), this);
        LOGGER.info(ChatColor.AQUA + "Registering block events");
        pluginManager.registerEvents(new onBlockDispense(), this);
        pluginManager.registerEvents(new onBlockPlace(), this);

        LOGGER.info(ChatColor.AQUA + "Registering entity events");
        pluginManager.registerEvents(new onEntityChangeBlock(), this);
        pluginManager.registerEvents(new onEntityChangeBlock(), this);
        pluginManager.registerEvents(new onEntityDamage(), this );
        pluginManager.registerEvents(new onEntityDamageByBlock(), this);
        pluginManager.registerEvents(new onEntityDamageByEntity(), this);
        pluginManager.registerEvents(new onEntityDeath(), this);
        pluginManager.registerEvents(new onEntityPickupItem(), this);
        pluginManager.registerEvents(new onEntityPortalTeleport(), this);
        pluginManager.registerEvents(new onCreatureSpawn(), this);
        pluginManager.registerEvents(new onProjectileHit(), this);

        LOGGER.info(ChatColor.AQUA + "Registering inventory events");
        pluginManager.registerEvents(new onInventoryClick(), this);
        pluginManager.registerEvents(new onInventoryClose(), this);
        pluginManager.registerEvents(new onInventoryOpen(), this);

        LOGGER.info(ChatColor.AQUA + "Registering player events");
        pluginManager.registerEvents(new onGamemodeSwitch(this), this);
        pluginManager.registerEvents(new onJoin(this), this);
        pluginManager.registerEvents(new onLeave(this), this);
        pluginManager.registerEvents(new onPlayerDeath(), this);
        pluginManager.registerEvents(new onPlayerInteract(this), this);
        pluginManager.registerEvents(new onPlayerInteractEntity(), this);
        pluginManager.registerEvents(new onRespawn(), this);
        pluginManager.registerEvents(new onPlayerPlaceCrystal(), this);
        pluginManager.registerEvents(new onSwapHandItems(), this);
        pluginManager.registerEvents(new EnderCrystalListeners(), this);
    }

    private void registerCommands(){
        LOGGER.info("Registering commands.");
        this.getCommand("kill").setExecutor(new KillCommand(this));
        this.getCommand("ping").setExecutor(new PingCommand());
        this.getCommand("playtime").setExecutor(new PlaytimeCommand(this));
        this.getCommand("shitlist").setExecutor(new ShitListCommand(this));
        this.getCommand("shrug").setExecutor(new ShrugCommand());
        this.getCommand("rank").setExecutor(new RankCommand());
        this.getCommand("monkey").setExecutor(new MonkeyCommand());
    }

    private void initSQL(){
        LOGGER.info(ChatColor.AQUA + "Starting up SQL driver.");

        this.SQL = new MySQL();
        this.sqlQuery = new SqlQuery(this);

        if(Config.databaseName.equals("name") && Config.databaseUsername.equals("username") && Config.databasePassword.equals("password")){
            LOGGER.warning("Default SQL config values detected. SQL driver won't be initiated.");
            return;
        }

        try{
            SQL.connect();
        } catch (SQLException | ClassNotFoundException throwables) {
            LOGGER.severe("Something went wrong while initiating SQL\nStack trace will follow.");
            throwables.printStackTrace();
        }

        if(SQL.isConnected()){
            LOGGER.info(ChatColor.GREEN + "Sucessfully connected to SwissKnife database.");
            sqlQuery.createStatsTable();
            //sqlQuery.createPingTable();
        }

        LOGGER.info(ChatColor.GREEN + "Finished SQL initialization.");

    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    private void startPingLogTask(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> pingUtil.uploadPingMap(pingUtil.getAllPings(), SQL, sqlQuery),6000,12000);
    }

    @ConfigFile("config.yml")
    public static class Config{

        @ConfigValue("combatCheck.timeout")
        public static long combatTimeout = 20000;

        @ConfigValue("jihads.enabled")
        public static boolean jihadsEnabled = true;

        @ConfigValue("jihads.limitRadius")
        public static boolean limitJihadRadius = true;

        @ConfigValue("jihads.radius")
        public static int jihadsRadius = 10000;

        @ConfigValue("jihads.power")
        public static double jihadsPower = 6.0;

        @ConfigValue("illegals.maxEnchant")
        public static int maxEnchantLevel = 100;

        @ConfigValue("illegals.checkLores")
        public static List<String> illegalLoreList = Arrays.asList("§9§lBig Dick Energy X", "§cCurse of Simping");

        @ConfigValue("illegals.maxTotemStack")
        public static int maxTotemStack = 2;

        @ConfigValue("illegals.illegalBlockList")
        public static List<String> illegalBlockList = Arrays.asList("BEDROCK", "END_PORTAL_FRAME", "BARRIER", "STRUCTURE_BLOCK", "STRUCTURE_VOID");

        @ConfigValue("patches.limitVehiclesInChunk")
        public static boolean limitVehicles = true;

        @ConfigValue("patches.maxVehicleInChunk")
        public static int vehicleLimitChunk = 26;

        @ConfigValue("illegals.enable1kPicks")
        public static boolean enable1kPicks = false;

        @ConfigValue("radius.spawnTeleport")
        public static int spawnRadius = 2000;

        @ConfigValue("ranks.midfagHours")
        public static int midfagHours = 48;

        @ConfigValue("ranks.oldfagHours")
        public static int oldfagHours = 408;

        @ConfigValue("ranks.elderfagHours")
        public static int elderfagHours = 2400;

        @ConfigValue("rank.elderfagVotes")
        public static int elderfagVotes = 300;

        @ConfigValue("sql.host")
        public static String databaseHost = "172.18.0.1";

        @ConfigValue("sql.port")
        public static String databasePort = "3306";

        @ConfigValue("sql.dbName")
        public static String databaseName = "name";

        @ConfigValue("sql.dbUserName")
        public static String databaseUsername = "username";

        @ConfigValue("sql.dbPassword")
        public static String databasePassword = "password";

        @ConfigValue("misc.mainWorldName")
        public static String mainWorldName = "world";

        @ConfigValue("misc.netherWorldName")
        public static String netherWorldName = "world_nether";

        @ConfigValue("misc.endWorldName")
        public static String endWorldName = "world_the_end";

        @ConfigValue("misc.enableAnniversaryItems")
        public static boolean anniversaryItems = true;
    }
}
