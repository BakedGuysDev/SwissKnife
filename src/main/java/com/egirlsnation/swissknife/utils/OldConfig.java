/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GPL-3.0 License.
 *
 * You should have received a copy of the GPL-3.0
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

package com.egirlsnation.swissknife.utils;

import com.egirlsnation.swissknife.SwissKnife;
import me.affanhaq.keeper.Keeper;
import me.affanhaq.keeper.data.ConfigFile;
import me.affanhaq.keeper.data.ConfigValue;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

@ConfigFile("Config.instance.yml")
public class OldConfig {
    public static OldConfig instance = null;
    private static Keeper keeper = null;

    public static void init(SwissKnife plugin){
        instance = new OldConfig();
        keeper = new Keeper(plugin).register(instance).load();
    }

    public static void save(){
        try{
            keeper.save();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Config values



    /*
     * Illegals config options
     */

    @ConfigValue("illegals.maxEnchant") // Done
    public int maxEnchantLevel = 100;

    @ConfigValue("illegals.checkLores") //Done
    public List<String> illegalLoreList = Arrays.asList("§9§lBig Dick Energy X", "§cCurse of Simping");

    @ConfigValue("illegals.maxTotemStack")
    public int maxTotemStack = 2;

    @ConfigValue("illegals.illegalBlockList") // Done
    public List<String> illegalBlockList = Arrays.asList("BEDROCK", "END_PORTAL_FRAME", "BARRIER", "STRUCTURE_BLOCK", "STRUCTURE_VOID");

    @ConfigValue("illegals.enable1kPicks") //Dropped
    public boolean enable1kPicks = false;

    @ConfigValue("illegals.unstackOverstackedInShulkers") //Done
    public boolean unstackInShulks = true;

    @ConfigValue("illegals.maxArmorStack") //Done
    public int maxArmorStack = 2;

    /*
     * Patches config options
     */

    @ConfigValue("patches.limitVehiclesInChunk") //Done - tested
    public boolean limitVehicles = true;

    @ConfigValue("patches.maxVehicleInChunk") //Done - tested
    public int vehicleLimitChunk = 26;

    @ConfigValue("patches.limitCrystalPlacementSpeed") //Done - replaced with crystal break
    public boolean limitCrystalPlacementSpeed = false;

    @ConfigValue("patches.msBetweenCrystals") //Done - tested
    public int crystalDelay = 200;

    /*
     * Throwables speed limit config options
     */

    @ConfigValue("patches.limitThrowablesSpeed.enabled") //Dropped
    public boolean limitThrowables = true;

    @ConfigValue("patches.limitThrowablesSpeed.delayMs") //Dropped
    public int throwablesDelay = 250;

    /*
     * High damage prevention config options //Done
     */

    @ConfigValue("preventions.highDamage.prevent")
    public boolean preventHighDmg = true;

    @ConfigValue("preventions.highDamage.kick")
    public boolean kickOnHighDamage = true;

    @ConfigValue("preventions.highDamage.threshold")
    public int highDmgThreshold = 1000;

    @ConfigValue("preventions.highDamage.redirect")
    public boolean redirectHighDmg = true;

    /*
     * Wither spawning at spawn config options
     */

    @ConfigValue("preventions.preventWitherSpawningAtSpawn.enabled") //Done - Tested
    public boolean preventWithersAtSpawn = false;

    @ConfigValue("preventions.preventWitherSpawningAtSpawn.spawnRadius") //Done - Tested
    public int spawnRadius = 2000;

    /*
     * XP bottle lag prevention config options
     */

    @ConfigValue("preventions.preventXpBottleLag.enabled") //Done - Tested
    public boolean preventXpBottleLag = true;

    @ConfigValue("preventions.preventXpBottleLag.xpBottleLimit") //Done - Tested
    public int xpBottleLimit = 64;

    /*
     * Hand switch lag prevention config options //Done
     */

    @ConfigValue("preventions.handSwitchCrash.prevent")
    public boolean handSwitchCrash = true;

    @ConfigValue("preventions.handSwitchCrash.delayMs")
    public int handSwitchDelay = 250;

    @ConfigValue("preventions.handSwitchCrash.kick")
    public boolean kickOnHandSwitchCrash = true;

    /*
     * Nether roof prevention config options
     */

    @ConfigValue("preventions.preventPlayersOnNetherRoof.enabled") //Done - Tested
    public boolean preventPlayersOnNether = true;

    @ConfigValue("preventions.preventPlayersOnNetherRoof.teleportDown") //Done - Tested
    public boolean teleportPlayersDown = true;

    @ConfigValue("preventions.preventPlayersOnNetherRoof.dealDamage") //Done - Tested
    public boolean dmgPlayersOnNether = false;

    @ConfigValue("preventions.preventPlayersOnNetherRoof.damage") //Done - Tested
    public int dmgToDealNether = 9999;

    @ConfigValue("preventions.preventPlayersOnNetherRoof.roofHeight") //Done - Tested
    public int netherRoofHeight = 127;

    /*
     * Entity portal teleportation config options
     */

    @ConfigValue("preventions.disableEntityPortalTP.enabled") //Done - Tested
    public boolean disableEntityPortal = true;

    @ConfigValue("preventions.disableEntityPortalTP.entityList") //Done - Tested
    public List<String> entityTypeDisablePortal = Arrays.asList(EntityType.BEE.name(), EntityType.ENDER_CRYSTAL.name());

    /*
     * Nether floor prevention config options
     */

    @ConfigValue("preventions.preventPlayersBellowBedrock.enabledOverworld") //Done - Tested
    public boolean preventPlayerBellowOw = true;

    @ConfigValue("preventions.preventPlayersBellowBedrock.enabledNether") //Done - Tested
    public boolean preventPlayerBellowNether = true;

    @ConfigValue("preventions.preventPlayersBellowBedrock.repairFloor") //Done - Tested
    public boolean placeBedrockBellow = true;

    @ConfigValue("preventions.preventPlayersBellowBedrock.dealDmg") //Done - Tested
    public boolean dealDmgBellow = false;

    @ConfigValue("preventions.preventPlayersBellowBedrock.damage") //Done - Tested
    public int dmgToDealBellow = 9999;


    /*
     * SQL config options //TODO: Test and migrate database (possibly in code)
     */

    @ConfigValue("sql.host")
    public String databaseHost = "172.18.0.1";

    @ConfigValue("sql.port")
    public String databasePort = "3306";

    @ConfigValue("sql.dbName")
    public String databaseName = "name";

    @ConfigValue("sql.dbUserName")
    public String databaseUsername = "username";

    @ConfigValue("sql.dbPassword")
    public String databasePassword = "password";

    /*
     * Combat check config options //TODO: Config options & finish
     */

    @ConfigValue("combatCheck.timeout")
    public long combatTimeout = 20000;

    @ConfigValue("combatCheck.elytraTimeout")
    public long elytraTimeout = 5000;

    /*
     * Jihads config options //TODO: Test new out of spawn radius
     */

    @ConfigValue("jihads.enabled")
    public boolean jihadsEnabled = true;

    @ConfigValue("jihads.limitRadius")
    public boolean limitJihadRadius = true;

    @ConfigValue("jihads.radius")
    public int jihadsRadius = 10000;

    @ConfigValue("jihads.power")
    public double jihadsPower = 6.0;


    /*
     * Disable Commands at spawn config options //TODO
     */

    @ConfigValue("disableCommandsAtSpawn.enabled")
    public boolean disableCommandsAtSpawn = true;

    @ConfigValue("disableCommandsAtSpawn.radius")
    public int disableCommandsRadius = 2000;

    @ConfigValue("disableCommandsAtSpawn.commandsList")
    public List<String> radiusLimitedCmds = Arrays.asList("tpa", "tpahere", "tpayes", "tpaccept", "tpaaccept", "tpno", "tpano", "tpdeny", "tpadeny", "tpyes");

    /*
     * Discord TPS Notifier config options //TODO
     */

    @ConfigValue("discordTPSnotifier.webhookURL")
    public String webhookURL = "";

    @ConfigValue("discordTPSnotifier.webhookName")
    public String webhookName = "TPS Alert";

    @ConfigValue("discordTPSnotifier.webhookAvatarURL")
    public String webhookAvatarURL = "";

    @ConfigValue("discordTPSnotifier.pingRolesIDs")
    public List<String> roleIDs = Arrays.asList("719274790795346031");

    @ConfigValue("discordTPSnotifier.minuteDelayAfterLoad")
    public int delayAfterLoad = 6;

    @ConfigValue("discordTPSnotifier.taskRepeatTimeTicks")
    public int tpsTaskTime = 1200;

    @ConfigValue("discordTPSnotifier.secondsBetweenNotify")
    public int notifyDelay = 600;

    @ConfigValue("discordTPSnotifier.tpsAverageThreshold")
    public int tpsAvgThreshold = 18;

    @ConfigValue("discordTPSnotifier.useLongerAverageTPS")
    public boolean longerTPSavg = false;

    @ConfigValue("discordTPSnotifier.tpsNotifyAlways")
    public int tpsNotifyAlways = 13;

    @ConfigValue("discordTPSnotifier.listOnlinePlayers")
    public boolean listOnlinePlayers = true;

    @ConfigValue("discordTPSnotifier.listLowPlaytimePlayers")
    public boolean listLowPtPlayers = true;

    @ConfigValue("discordTPSnotifier.lowPlaytimeThresholdInHours")
    public int lowPtThreshold = 30;

    /*
     * Shitlist config options //Done //TODO: Test
     */

    @ConfigValue("shitlist.enable")
    public boolean enableShitlist = true;

    @ConfigValue("shitlist.blacklistedCommands")
    public List<String> blacklistedCommands = Arrays.asList("tpa", "tpahere", "tpayes", "tpaccept", "tpaaccept", "tpno", "tpano", "tpdeny", "tpadeny", "tpyes");

    @ConfigValue("shitlist.leakCoords")
    public boolean leakCoords = false;

    @ConfigValue("shitlist.swapWordsRandomly")
    public boolean swapWordsRandomly = true;

    @ConfigValue("shitlist.wordsToReplaceWith")
    public List<String> replacementWords = Arrays.asList("titty", "pickle", "canoodle", "mitten", "badger", "doodlesack");

    @ConfigValue("shitlist.replaceChance")
    public int replaceChance = 50;

    /*
     * Command cooldowns options //TODO
     */
    @ConfigValue("commandCooldowns.enable")
    public boolean enableCooldowns = true;

    @ConfigValue("commandCooldowns.meCmd")
    public int ME_COOLDOWN = 60;

    @ConfigValue("commandCooldowns.killCmd")
    public int KILL_COOLDOWN = 60;

    @ConfigValue("commandCooldowns.playtimeCmd")
    public int PLAYTIME_COOLDOWN = 60;

    @ConfigValue("commandCooldowns.afkCmd")
    public int AFK_COOLDOWN = 20;

    @ConfigValue("commandCooldowns.pingCmd")
    public int PING_COOLDOWN = 60;


    /*
     * Draconite Items config options //TODO
     */

    @ConfigValue("draconiteItems.pickaxe.enable")
    public boolean enablePickaxe = false;

    @ConfigValue("draconiteItems.pickaxe.crafting.enable")
    public boolean enablePickaxeCraft = true;

    @ConfigValue("draconiteItems.pickaxe.crafting.useDraconiteGemsRecipe")
    public boolean useDraconiteGems = true;

    @ConfigValue("draconiteItems.pickaxe.crafting.useBedrockInsteadOfSticks")
    public boolean useBedrockSticks = true;

    @ConfigValue("draconiteItems.pickaxe.xpToDrain")
    public int xpToDrain = 5;

    @ConfigValue("draconiteItems.pickaxe.hasteLevel")
    public int hasteLevel = 4;

    /*
     * Chat config options //Done
     */

    @ConfigValue("chat.greentext.enabled")  //Done - Tested
    public boolean greentext = true;

    @ConfigValue("chat.coords.enabled") //Done - Tested
    public boolean coordsEnabled = true;

    @ConfigValue("chat.coords.placeholderRegex") //Done  - Tested
    public String coordsPlaceholder = "(?i)(\\[coords\\])";

    @ConfigValue("chat.coords.replaceWith") //Done  - Tested
    public String coordsReplace = "&bWorld: &f%player_world% &eX:&f%player_x% &eY:&f%player_y% &eZ:&f%player_z%";

    @ConfigValue("chat.coords.replaceInCommands") //TODO: Test in commands
    public boolean coordsCommandsEnabled = true;

    @ConfigValue("chat.coords.commands")
    public List<String> coordsCommands = Arrays.asList("/w", "/whisper", "/msg", "/r", "/message", "/reply");


    /*
     * Misc config options
     */

    @ConfigValue("misc.mainWorldName")
    public String mainWorldName = "world";

    @ConfigValue("misc.netherWorldName")
    public String netherWorldName = "world_nether";

    @ConfigValue("misc.endWorldName")
    public String endWorldName = "world_the_end";

    @ConfigValue("misc.petsUseTotems") //Done - tested
    public boolean petsUseTotems = false;

    @ConfigValue("misc.maxItemNameLength")
    public int maxItemNameLength = 50;

    @ConfigValue("misc.disableEndermenGriefInEnd") //Done
    public boolean disableEndermanGrief = false;

    /*
     * EgirlsNation config options
     */

    @ConfigValue("egirlsnation.enableAnniversaryItems") //Dropped
    public boolean anniversaryItems = false;

    @ConfigValue("egirlsnation.ranksEnabled") //TODO: Configurable hours and votes
    public boolean ranksEnabled = false;

    @ConfigValue("egirlsnation.refreshrankCmdCooldown") //TODO
    public int REFRESHRANK_COOLDOWN = 60;

    @ConfigValue("egirlsnation.newfagHours")
    public int newfagHours = 48;

    @ConfigValue("egirlsnation.midfagHours")
    public int midfagHours = 168;

    @ConfigValue("egirlsnation.oldfagHours")
    public int oldfagHours = 432;

    @ConfigValue("egirlsnation.elderfagHours")
    public int elderfagHours = 1200;

    @ConfigValue("egirlsnation.elderfagVotes")
    public int elderfagVotes = 100;

    @ConfigValue("egirlsnation.boomerfagHours")
    public int boomerfagHours = 2400;

    @ConfigValue("egirlsnation.boomerfagVotes")
    public int boomerfagVotes = 300;

    @ConfigValue("egirlsnation.fixDragonDeath.enabled") //Done
    public boolean fixDragonDeath = false;

    @ConfigValue("egirlsnation.fixDragonDeath.health") //TODO: Configurable health
    public int dragonHealth = 100;
}
