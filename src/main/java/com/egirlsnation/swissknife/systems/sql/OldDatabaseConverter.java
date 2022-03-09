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

package com.egirlsnation.swissknife.systems.sql;

import com.egirlsnation.swissknife.SwissKnife;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OldDatabaseConverter {

    private final Connection connection;

    public OldDatabaseConverter(Connection connection){
        this.connection = connection;
    }

    private final List<UUID> unfinishedUuids = new ArrayList<>();
    private final List<UUID> failedUuids = new ArrayList<>(1);
    private final List<UUID> finishedUuids = new ArrayList<>(1);

    public boolean hasOldDbVersion(){
        try{
            PreparedStatement ps = connection.prepareStatement("SHOW TABLES LIKE 'swissPlayerStats'");

            return ps.executeQuery().next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean fillUuidList(){
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT UUID FROM playerStats");

            SwissKnife.swissLogger.info("Getting all UUIDs from the old database...");
            ResultSet rs = ps.executeQuery();

            if(rs == null){
                SwissKnife.swissLogger.severe("Old database doesn't contain any UUIDs. Can't convert the databases");
                return false;
            }

            rs.last();
            int size = rs.getRow();
            rs.beforeFirst();
            int i = 1;
            double percentage = 0;
            while(rs.next()){
                unfinishedUuids.add(UUID.fromString(rs.getString("UUID")));
                percentage = ++i * 100 / (double) size;
                if(Math.toIntExact((long) percentage) % 10 == 0){
                    SwissKnife.swissLogger.info("Getting all UUIDs from the old database... " + Math.toIntExact((long) percentage) + "%");
                }
            }

            SwissKnife.swissLogger.info("Finished getting UUIDs from the old database");
            return true;
        }catch(SQLException e){
            SwissKnife.swissLogger.severe("Error occurred while getting UUIDs from the old database. Stack trace will follow");
            e.printStackTrace();
            return false;
        }
    }

    public boolean retryFailedUuids(){
        return convertToNewDatabase(ConvertMode.FAILED) == 0;
    }


    public int convertToNewDatabase(ConvertMode mode){
        List<UUID> uuids;
        List<String> columnNames = null;
        if(mode.equals(ConvertMode.REGULAR)){
            uuids = unfinishedUuids;
        }else if(mode.equals(ConvertMode.FAILED)){
            uuids = failedUuids;
        }else{
            return -1;
        }
        int failed = 0;
        for(UUID uuid : uuids){
            if(finishedUuids.contains(uuid)){
                continue;
            }

            String name = null;
            boolean shitlisted = false;
            long playtime = 0;
            long firstplayed = 0;
            int kills = 0;
            int deaths = 0;
            int mobkills = 0;
            int distancewalked = 0;
            int distancesprinted = 0;
            int distanceair = 0;
            int timesincedeath = 0;
            int obsidianMined = 0;
            int combatlogs = 0;

            // Stage 1

            try{
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM playerStats WHERE UUID=?");
                ps.setString(1, uuid.toString());

                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    SwissKnife.swissLogger.warning("UUID: " + uuid.toString() + " was gotten from the database but now doesn't exist. Skipping.");
                    failedUuids.add(uuid);
                    continue;
                }

                if(columnNames == null){

                    ResultSetMetaData metaData = rs.getMetaData();
                    List<String> columnNamesHelp = new ArrayList<>(metaData.getColumnCount());
                    for(int i = 1; i <= metaData.getColumnCount(); i++){
                        columnNamesHelp.add(metaData.getColumnName(i));
                    }
                    columnNames = columnNamesHelp;
                }

                if(!columnNames.contains("Name")){
                    throw new SQLDataException("Old database doesn't contain player names. The conversion can't continue.");
                }
                name = rs.getString("Name");

                if(columnNames.contains("shitlisted")){
                    shitlisted = rs.getInt("shitlisted") == 1;
                }
                if(columnNames.contains("playTime")){
                    playtime = rs.getInt("playTime");
                }
                if(columnNames.contains("firstPlayed")){
                    firstplayed = rs.getInt("firstPlayed");
                }
                if(columnNames.contains("kills")){
                    kills = rs.getInt("kills");
                }
                if(columnNames.contains("deaths")){
                    deaths = rs.getInt("deaths");
                }
                if(columnNames.contains("mobKills")){
                    mobkills = rs.getInt("mobKills");
                }

                if(columnNames.contains("distanceWalked")){
                    distancewalked = rs.getInt("distanceWalked");
                }
                if(columnNames.contains("distanceSprinted")){
                    distancesprinted = rs.getInt("distanceSprinted");
                }
                if(columnNames.contains("distanceElytra")){
                    distanceair = rs.getInt("distanceElytra");
                }
                if(columnNames.contains("timeSinceDeath")){
                    timesincedeath = rs.getInt("timeSinceDeath");
                }
                if(columnNames.contains("blocksMined")){
                    obsidianMined = rs.getInt("blocksMined");
                }
                if(columnNames.contains("combatLogs")){
                    combatlogs = rs.getInt("combatLogs");
                }

            }catch(SQLException e){
                if(e instanceof SQLDataException){
                    e.printStackTrace();
                    unfinishedUuids.clear();
                    failedUuids.clear();
                    return -2;
                }
                failed++;
                SwissKnife.swissLogger.warning("Failed to get records for UUID " + uuid.toString() + " from the old database.\n" +
                        "Failed conversions will repeat at the end.\n" +
                        " Stacktrace will follow");
                failedUuids.add(uuid);
                uuids.remove(uuid);
                e.printStackTrace();
                continue;
            }

            if(MySQL.get().getPlayerStatsDriver().exists(uuid)) continue;

            //Stage 2

            try{
                    PreparedStatement ps2 = connection.prepareStatement("INSERT IGNORE INTO swissPlayerStats"
                            + " (username,uuid,playtime," +
                            "kills,deaths,mobkills" +
                            ",shitlisted,firstplayed,obsidianmined" +
                            ",distanceair,distancewalked,distancesprinted" +
                            ",timesincedeath,combatlogs)" +
                            " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    ps2.setString(1, name);
                    ps2.setString(2, uuid.toString());
                    ps2.setLong(3, playtime);
                    ps2.setInt(4, kills);
                    ps2.setInt(5, deaths);
                    ps2.setInt(6, mobkills);
                    ps2.setBoolean(7, shitlisted);
                    ps2.setLong(8, firstplayed);
                    ps2.setInt(9, obsidianMined);
                    ps2.setInt(10, distanceair);
                    ps2.setInt(11, distancewalked);
                    ps2.setInt(12, distancesprinted);
                    ps2.setInt(13,timesincedeath);
                    ps2.setInt(14, combatlogs);

                    ps2.executeUpdate();

            }catch(SQLException e){
                failed++;
                SwissKnife.swissLogger.warning("Failed to convert record for " + uuid.toString() + " to the new database.\n" +
                        "Failed conversions will repeat at the end.\n" +
                        " Stacktrace will follow");
                failedUuids.add(uuid);
                uuids.remove(uuid);
                e.printStackTrace();
                continue;
            }
            finishedUuids.add(uuid);
            uuids.remove(uuid);

        }
        return failed;
    }

    public int getFinished(){
        return finishedUuids.size();
    }

    public int getUnfinished(){
        return unfinishedUuids.size();
    }

    public int getFailed(){
        return failedUuids.size();
    }

    public void clearUnfinished(){
        unfinishedUuids.clear();
    }

    public void clearFailed(){
        failedUuids.clear();
    }

    public void clearFinished(){
        finishedUuids.clear();
    }

    public enum ConvertMode{
        REGULAR,
        FAILED
    }
}
