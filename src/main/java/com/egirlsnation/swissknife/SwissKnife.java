package com.egirlsnation.swissknife;

import com.egirlsnation.swissknife.command.KillCommand;
import com.egirlsnation.swissknife.event.entity.onEntityDamageByBlock;
import com.egirlsnation.swissknife.event.entity.onEntityDamageByEntity;
import com.egirlsnation.swissknife.event.player.onLeave;
import com.egirlsnation.swissknife.event.player.onJoin;
import com.egirlsnation.swissknife.hooks.PluginManagerEvents;
import me.affanhaq.keeper.Keeper;
import me.affanhaq.keeper.data.ConfigFile;
import me.affanhaq.keeper.data.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class SwissKnife extends JavaPlugin {

    private final PluginManager pluginManager = Bukkit.getPluginManager();
    public final Logger LOGGER = Bukkit.getLogger();

    @Override
    public void onEnable(){
        LOGGER.info("Loading config handler.");
        new Keeper(this).register(new Config()).load();

        registerEvents();
        registerCommands();

    }

    @Override
    public void onDisable(){

    }

    private void registerEvents(){
        LOGGER.info(ChatColor.AQUA + "Registering events");
        pluginManager.registerEvents(new onJoin(), this);
        pluginManager.registerEvents(new PluginManagerEvents(this), this);
        pluginManager.registerEvents(new onEntityDamageByEntity(), this);
        pluginManager.registerEvents(new onEntityDamageByBlock(), this);
        pluginManager.registerEvents(new onLeave(), this);
    }

    private void registerCommands(){
        LOGGER.info("Registering commands.");
        this.getCommand("kill").setExecutor(new KillCommand(this));
    }

    public PluginManager getPluginManager() {
        return pluginManager;
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

        @ConfigValue("radius.spawnTeleport")
        public static int spawnRadius = 2000;

        @ConfigValue("misc.mainWorldName")
        public static String mainWorldName = "world";

        @ConfigValue("misc.netherWorldName")
        public static String netherWorldName = "world_nether";

        @ConfigValue("misc.endWorldName")
        public static String endWorldName = "world_the_end";
    }
}
