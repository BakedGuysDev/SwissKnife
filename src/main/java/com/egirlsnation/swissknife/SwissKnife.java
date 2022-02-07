/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
* License along with this program.  If not, see
* <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife;

import com.egirlsnation.swissknife.listeners.DebugStickDupe;
import com.egirlsnation.swissknife.listeners.block.onBlockDispense;
import com.egirlsnation.swissknife.listeners.block.onBlockPlace;
import com.egirlsnation.swissknife.listeners.entity.*;
import com.egirlsnation.swissknife.listeners.inventory.onCraftItemEvent;
import com.egirlsnation.swissknife.listeners.inventory.onInventoryClick;
import com.egirlsnation.swissknife.listeners.inventory.onInventoryClose;
import com.egirlsnation.swissknife.listeners.inventory.onInventoryOpen;
import com.egirlsnation.swissknife.listeners.player.*;
import com.egirlsnation.swissknife.systems.commands.*;
import com.egirlsnation.swissknife.systems.discord.DiscordHandler;
import com.egirlsnation.swissknife.systems.handlers.customItems.CustomItemHandler;
import com.egirlsnation.swissknife.systems.hooks.votingPlugin.VotingPluginHook;
import com.egirlsnation.swissknife.systems.sql.MySQL;
import com.egirlsnation.swissknife.systems.sql.SqlQuery;
import com.egirlsnation.swissknife.utils.Config;
import com.egirlsnation.swissknife.utils.ServerUtil;
import com.egirlsnation.swissknife.utils.SwissLogger;
import com.egirlsnation.swissknife.utils.player.PingUtil;
import com.egirlsnation.swissknife.utils.player.RankUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class SwissKnife extends JavaPlugin {

    private final PluginManager pluginManager = Bukkit.getPluginManager();

    public MySQL SQL;
    public SqlQuery sqlQuery;

    private final PingUtil pingUtil = new PingUtil();
    private final DiscordHandler discordHandler = new DiscordHandler();
    private final ServerUtil serverUtil = new ServerUtil();
    private final RankUtil rankUtil = new RankUtil();
    private final CustomItemHandler customItemHandler = new CustomItemHandler();
    private final VotingPluginHook votingPluginHook = new VotingPluginHook();

    @Override
    public void onEnable() {
        SwissLogger.info("Loading config handler.");
        Config.init(this);

        if (!Config.instance.webhookURL.isBlank()) {
            if (Bukkit.getServer().getTPS().length == 3) {
                discordHandler.setWebhookURL(Config.instance.webhookURL);
                discordHandler.setTpsArrSize(3);
                initTPSnotifyTask();
            } else if (Bukkit.getServer().getTPS().length == 4) {
                discordHandler.setWebhookURL(Config.instance.webhookURL);
                discordHandler.setTpsArrSize(4);
                initTPSnotifyTask();
            } else {
                SwissLogger.warning("You're running server software that's modifiying TPS results that's not supported by this plugin.\nTPS Notifications won't be sent.\nSupported server software for this feature is Spigot, PaperMC, Tuinity, Airplane, Purpur and Pufferfish");
            }
        }

        correctConfigValues();

        registerEvents();
        registerCommands();
        registerRecipes();

        initPluginHooks();
        initSQL();
    }

    @Override
    public void onDisable() {
        if(SQL != null) {
            if (SQL.isConnected()) {
                SQL.disconnect();
            }
        }
        getLogger().info(ChatColor.GREEN + "Swiss Knife plugin disabled.");
    }

    private void registerEvents() {
        pluginManager.registerEvents(new CommandPreProcessor(this), this);
        SwissLogger.info(ChatColor.AQUA + "Registering block events");
        pluginManager.registerEvents(new onBlockDispense(), this);
        pluginManager.registerEvents(new onBlockPlace(), this);

        SwissLogger.info(ChatColor.AQUA + "Registering entity events");
        pluginManager.registerEvents(new onEntityChangeBlock(), this);
        pluginManager.registerEvents(new onEntityChangeBlock(), this);
        pluginManager.registerEvents(new onEntityDamage(), this);
        pluginManager.registerEvents(new onEntityDamageByBlock(), this);
        pluginManager.registerEvents(new onEntityDamageByEntity(), this);
        pluginManager.registerEvents(new onEntityDeath(), this);
        pluginManager.registerEvents(new onEntityPickupItem(), this);
        pluginManager.registerEvents(new onEntityPortalTeleport(), this);
        pluginManager.registerEvents(new onCreatureSpawn(), this);
        pluginManager.registerEvents(new onProjectileHit(), this);

        SwissLogger.info(ChatColor.AQUA + "Registering inventory events");
        pluginManager.registerEvents(new onInventoryClick(), this);
        pluginManager.registerEvents(new onInventoryClose(), this);
        pluginManager.registerEvents(new onInventoryOpen(), this);

        SwissLogger.info(ChatColor.AQUA + "Registering player events");
        pluginManager.registerEvents(new onGamemodeSwitch(this), this);
        pluginManager.registerEvents(new onJoin(this), this);
        pluginManager.registerEvents(new onLeave(this), this);
        pluginManager.registerEvents(new onPlayerDeath(), this);
        pluginManager.registerEvents(new onPlayerInteract(this), this);
        pluginManager.registerEvents(new onPlayerInteractEntity(), this);
        pluginManager.registerEvents(new onRespawn(), this);
        pluginManager.registerEvents(new onPlayerPlaceCrystal(), this);
        pluginManager.registerEvents(new onSwapHandItems(), this);
        pluginManager.registerEvents(new EnderCrystalListeners(this), this);
        pluginManager.registerEvents(new onVehicleCreate(), this);
        pluginManager.registerEvents(new onVehicleCollision(), this);
        pluginManager.registerEvents(new onPlayerMove(), this);
        pluginManager.registerEvents(new onPlayerTeleport(), this);
        pluginManager.registerEvents(new onCraftItemEvent(), this);
        pluginManager.registerEvents(new onProjectileLaunch(), this);
        pluginManager.registerEvents(new onPlayerChat(this), this);
        pluginManager.registerEvents(new DebugStickDupe(), this);
    }

    private void registerCommands() {
        SwissLogger.info("Registering commands.");
        Objects.requireNonNull(this.getCommand("kill")).setExecutor(new KillCommand(this));
        Objects.requireNonNull(this.getCommand("ping")).setExecutor(new PingCommand());
        Objects.requireNonNull(this.getCommand("playtime")).setExecutor(new PlaytimeCommand(this));
        if(Config.instance.enableShitlist) {
            Objects.requireNonNull(this.getCommand("shitlist")).setExecutor(new ShitListCommand(this));
        }
        Objects.requireNonNull(this.getCommand("shrug")).setExecutor(new ShrugCommand());
        Objects.requireNonNull(this.getCommand("refreshrank")).setExecutor(new RefreshRankCommand());
        Objects.requireNonNull(this.getCommand("monkey")).setExecutor(new MonkeyCommand());
        Objects.requireNonNull(this.getCommand("tpsalerttest")).setExecutor(new TpsAlertTestCommand(this));
        Objects.requireNonNull(this.getCommand("toggleitemability")).setExecutor(new ToggleItemAbilityCommand());
    }

    private void initSQL() {
        SwissLogger.info(ChatColor.AQUA + "Starting up SQL driver.");

        this.SQL = new MySQL();
        this.sqlQuery = new SqlQuery(this);

        if (Config.instance.databaseName.equals("name") && Config.instance.databaseUsername.equals("username") && Config.instance.databasePassword.equals("password")) {
            SwissLogger.warning("Default SQL config values detected. SQL driver won't be initiated.");
            return;
        }

        try {
            SQL.connect();
        } catch (SQLException | ClassNotFoundException throwables) {
            SwissLogger.error("Something went wrong while initiating SQL\nStack trace will follow.");
            throwables.printStackTrace();
        }

        if (SQL.isConnected()) {
            SwissLogger.info(ChatColor.GREEN + "Sucessfully connected to SwissKnife database.");
            sqlQuery.createStatsTable();
            //sqlQuery.createPingTable();
            SwissLogger.info(ChatColor.GREEN + "Finished SQL initialization.");
        }
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    private void initPingLogTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> pingUtil.uploadPingMap(pingUtil.getAllPings(), SQL, sqlQuery), 6000, 12000);
    }

    private void initPluginHooks(){
        if(pluginManager.getPlugin("VotingPlugin") != null){
            if(pluginManager.getPlugin("VotingPlugin").isEnabled()){
                if(votingPluginHook.isVotingPluginHookActive()) return;
                SwissLogger.info("Enabling VotingPlugin hook.");
                votingPluginHook.initVotingPluginHook();
            }
        }
    }

    private void initTPSnotifyTask() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            List<Double> tps = serverUtil.getTPS();
            if (discordHandler.shouldPostAlert(tps)) {
                List<String> rankNames = null;
                if(Config.instance.listOnlinePlayers){
                    rankNames = rankUtil.getOnlinePlayerRankList();
                }
                List<String> namesUnderPt = null;
                if(Config.instance.listLowPtPlayers){
                    namesUnderPt = rankUtil.getOnlinePlayerNamesUnderPlaytime(Config.instance.lowPtThreshold);
                }

                List<String> finalRankNames = rankNames;
                List<String> finalNamesUnderPt = namesUnderPt;
                int playercount = Bukkit.getServer().getOnlinePlayers().size();
                int maxSlots = Bukkit.getServer().getMaxPlayers();
                Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                    try {
                        discordHandler.postDiscordTPSNotif(tps, playercount, maxSlots, finalRankNames, finalNamesUnderPt);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }, serverUtil.getTicksFromMinutes(Config.instance.delayAfterLoad), Config.instance.tpsTaskTime);
    }

    private void registerRecipes(){
        SwissLogger.info("Registering recipes");
        if(Config.instance.enablePickaxeCraft){
            SwissLogger.info("Registering draconite pickaxe recipe");
            NamespacedKey draconitePickKey = new NamespacedKey(this, "draconite_pickaxe");
            ShapedRecipe draconitePick = new ShapedRecipe(draconitePickKey, customItemHandler.getDraconitePickaxe())
                    .shape("GHG", " S ", " S ");
            if(Config.instance.useDraconiteGems){
                draconitePick.setIngredient('G', Material.PLAYER_HEAD).setIngredient('H', Material.END_CRYSTAL);
            }else{
                draconitePick.setIngredient('G', Material.END_CRYSTAL).setIngredient('H', Material.DRAGON_HEAD);
            }
            if(Config.instance.useBedrockSticks){
                draconitePick.setIngredient('S', Material.BEDROCK);
            }else{
                draconitePick.setIngredient('S', Material.STICK);
            }
            if(Bukkit.getRecipe(draconitePickKey) != null){
                Bukkit.removeRecipe(draconitePickKey);
            }
            Bukkit.addRecipe(draconitePick);
        }
    }

    private void correctConfigValues(){
        if(Config.instance.replaceChance > 100){
            Config.instance.replaceChance = 100;
        }
        if(Config.instance.replaceChance < 0){
            Config.instance.replaceChance = 0;
        }

    }
}
