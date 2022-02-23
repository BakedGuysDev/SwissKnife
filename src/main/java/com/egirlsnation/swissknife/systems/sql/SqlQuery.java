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
import com.egirlsnation.swissknife.utils.entity.player.PlayerInfo;
import com.egirlsnation.swissknife.utils.entity.player.SwissPlayer;
import com.egirlsnation.swissknife.utils.misc.ExistsCallback;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SqlQuery {

    //TODO: Change tinyint to bit

    public void createStatsTable(){
        PreparedStatement ps;
        try{
            ps = MySQL.get().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerStats "
                    + "(username VARCHAR(32)," +
                    "uuid CHAR(36)," +
                    "playtime BIGINT(19)," +
                    "kills INT(11)," +
                    "deaths INT(11)," +
                    "mobkills INT(11)," +
                    "shitlisted TINYINT(1)," +
                    "firstplayed VARCHAR(100)," +
                    "obsidianmined INT(11)," +
                    "distanceair BIGINT(19)," +
                    "distanceland BIGINT(19)," +
                    "timesincedeath INT(11)," +
                    "combatlogs INT(11)," +
                    " PRIMARY KEY (uuid))");
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createPlayerDataTable(){
        PreparedStatement ps;
        try{
            ps = MySQL.get().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS swissPlayerData "
                    + "(uuid CHAR(36)," +
                    "petTotems TINYINT(1)," +
                    "draconiteAbilities TINYINT(1)," +
                    " PRIMARY KEY (uuid))");
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createPlayerData(SwissPlayer player){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("SELECT * FROM swissPlayerData WHERE UUID=?");
            ps.setString(1, player.getUuid().toString());
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next()){

                PreparedStatement ps2 = MySQL.get().getConnection().prepareStatement("INSERT IGNORE INTO swissPlayerData"
                        + " (uuid,petTotems,draconiteAbilities)" +
                        " VALUES (?,?,?)");
                ps2.setString(1, player.getUuid().toString());
                if(player.hasFeatureEnabled(SwissPlayer.SwissFeature.PET_TOTEMS)){
                    ps2.setInt(2, 1);
                }else{
                    ps2.setInt(2, 0);
                }
                if(player.hasFeatureEnabled(SwissPlayer.SwissFeature.DRACONITE_ABILITIES)){
                    ps2.setInt(3, 1);
                }else{
                    ps2.setInt(3, 0);
                }

                ps2.executeUpdate();

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updatePlayerData(SwissPlayer player){
        try{
            if (!hasPlayerDataEntry(player.getUuid())) {
                createPlayerData(player);
            }

            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("UPDATE swissPlayerData SET" +
                    " petTotems=?,draconiteAbilities=?" +
                    " WHERE uuid=?");
            if(player.hasFeatureEnabled(SwissPlayer.SwissFeature.PET_TOTEMS)){
                ps.setInt(1, 1);
            }else{
                ps.setInt(1, 0);
            }
            if(player.hasFeatureEnabled(SwissPlayer.SwissFeature.DRACONITE_ABILITIES)){
                ps.setInt(2, 1);
            }else{
                ps.setInt(2, 0);
            }

            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean hasPlayerDataEntry(UUID uuid){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("SELECT * FROM swissPlayerData WHERE UUID=?");
            ps.setString(1, uuid.toString());

            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public void createPlayerAsync(Player player){
        createPlayerAsync(player, false);
    }

    public void createPlayerAsync(Player player, boolean shitlisted){
        UUID playerUUID = player.getUniqueId();
        String playerName = player.getName();
        long firstPlayed = player.getFirstPlayed();

        Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
           createPlayer(playerUUID, playerName, firstPlayed, 0, shitlisted);
        });

    }

    public void createPlayerAsync(PlayerInfo playerInfo){
        UUID playerUUID = playerInfo.getUuid();
        String playerName = playerInfo.getName();
        long firstPlayed = playerInfo.getFirstplayed();
        boolean shitlisted = playerInfo.isShitlisted();

        Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
           createPlayer(playerUUID, playerName, firstPlayed, 0, shitlisted);
        });
    }

    public void createPlayer(PlayerInfo playerInfo){
        UUID playerUUID = playerInfo.getUuid();
        String playerName = playerInfo.getName();
        long firstPlayed = playerInfo.getFirstplayed();
        boolean shitlisted = playerInfo.isShitlisted();

        createPlayer(playerUUID, playerName, firstPlayed, 0, shitlisted);
    }

    public void createPlayer(UUID playerUUID, String playerName, long firstPlayed, long playtime, boolean shitlisted){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("SELECT * FROM playerStats WHERE UUID=?");
            ps.setString(1, playerUUID.toString());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            if(!exists(playerUUID)){

                Date date = new Date(firstPlayed);
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String firstPlayedS = sdf.format(date);

                PreparedStatement ps2 = MySQL.get().getConnection().prepareStatement("INSERT IGNORE INTO playerStats"
                        + " (username,uuid,playtime," +
                        "kills,deaths,mobkills" +
                        ",shitlisted,firstplayed,obsidianmined" +
                        ",distanceair,distanceland" +
                        ",timesincedeath,combatlogs)" +
                        " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
                ps2.setString(1, playerName);
                ps2.setString(2, playerUUID.toString());
                ps2.setLong(3, playtime);
                ps2.setInt(4, 0);
                ps2.setInt(5, 0);
                ps2.setInt(6, 0);
                if(shitlisted){
                    ps2.setInt(7, 1);
                }else{
                    ps2.setInt(7, 0);
                }
                ps2.setString(8, firstPlayedS);
                ps2.setInt(9, 0);
                ps2.setInt(10, 0);
                ps2.setInt(11, 0);
                ps2.setInt(12,0);
                ps2.setInt(13, 0);

                ps2.executeUpdate();

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean exists(UUID uuid){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("SELECT * FROM playerStats WHERE UUID=?");
            ps.setString(1, uuid.toString());

            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean exists(String playerName){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("SELECT * FROM playerStats WHERE Name =?");
            ps.setString(1, playerName);

            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void existsAsync(String playerName, ExistsCallback callback){
        Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
           boolean exists = exists(playerName);
           Bukkit.getScheduler().runTask(SwissKnife.INSTANCE, () -> {
              callback.onQueryDone(exists);
           });
        });
    }

    public void updateValuesAsync(Player player){
        PlayerInfo playerInfo = new PlayerInfo(player);

        Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
           updateValues(playerInfo);
        });
    }

    public void updateValuesAsync(PlayerInfo playerInfo){
        Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
            updateValues(playerInfo);
        });
    }

    public void updateValues(PlayerInfo playerInfo){
        try{
            if (!exists(playerInfo.getUuid())) {
                createPlayer(playerInfo);
            }

            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("UPDATE playerStats SET" +
                    " playtime=?,kills=?,deaths=?" +
                    ",mobkills=?,obsidianmined=?,distanceland=?" +
                    ",distanceair=?,timeSinceDeath=?" +
                    " WHERE uuid=?");


            ps.setLong(1, playerInfo.getPlaytime());
            ps.setInt(2, playerInfo.getKills());
            ps.setInt(3, playerInfo.getKills());
            ps.setInt(4, playerInfo.getMobkills());
            ps.setInt(5, playerInfo.getObsidianMined());
            ps.setLong(6, playerInfo.getDistanceland());
            ps.setLong(7, playerInfo.getDistanceair());
            ps.setInt(8, playerInfo.getTimesincedeath());
            ps.setString(9, playerInfo.getUuid().toString());

            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public long getPlaytime(String playerName){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("SELECT playtime FROM playerStats WHERE name=?");
            ps.setString(1, playerName);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("playtime");
            }
            return 0;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }


    public String addToShitlist(PlayerInfo info){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("UPDATE playerStats SET shitlisted=? WHERE uuid=?");
            ps.setInt(1, 1);
            ps.setString(2, info.getUuid().toString());
            if (!exists(info.getUuid())) {
                createPlayerAsync(info);
                return "Player didn't have database record. Created one and shitlisted them";
            }
            ps.executeUpdate();
            return "Successfully shitlisted player " + info.getName();
        }catch (SQLException e){
            e.printStackTrace();
            return "Error occurred while shitlisting player " + info.getName();
        }
    }



    public String removeFromShitlist(PlayerInfo info){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("UPDATE playerStats SET shitlisted=? WHERE uuid=?");
            ps.setInt(1, 0);
            ps.setString(2, info.getUuid().toString());
            if (!exists(info.getUuid())) {
                createPlayerAsync(info);
                return "Player" + info.getName() + " didn't have a database record. Created one and unshitlisted them";
            }
            ps.executeUpdate();
            return "Successfully unshitlisted player " + info.getName();

        }catch (SQLException e){
            e.printStackTrace();
            return "Error occurred while unshitlisting player " + info.getName();
        }
    }

    public void addToShitlistAsync(Player player){
        String name = player.getName();
        Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
           addToShitlist(name);
        });
    }

    public String addToShitlist(String name){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("UPDATE playerStats SET shitlisted=? WHERE name=?");
            ps.setInt(1, 1);
            ps.setString(2, name);
            if (!exists(name)) {
                return "Player " + name + " doesn't exist in the database, can't shitlist them.";
            }
            ps.executeUpdate();
            return "Player " + name + " successfully shitlisted.";

        }catch (SQLException e){
            e.printStackTrace();
            return "Error occurred while shitlisting player " + name;
        }
    }

    public void removeFromShitlistAsync(Player player){
        String name = player.getName();
        Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
           removeFromShitlist(name);
        });
    }

    public String removeFromShitlist(String name){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("UPDATE playerStats SET shitlisted=? WHERE name=?");
            ps.setInt(1, 0);
            ps.setString(2, name);
            if (!exists(name)) {
                return "Player " + name + " doesn't exist in the database, can't unshitlist them.";
            }
            ps.executeUpdate();
            return  "Player " + name + " successfully unshitlisted.";

        }catch (SQLException e){
            e.printStackTrace();
            return "Error occurred while unshitlisting player " + name;
        }
    }

    public boolean isShitlisted(String name){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("SELECT shitlisted FROM playerStats WHERE name=?");
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                if(resultSet.getInt("shitlisted") == 1) return true;
                if(resultSet.getInt("shitlisted") == 0) return false;
            }
            return false;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean isShitlisted(UUID uuid){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("SELECT shitlisted FROM playerStats WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                if(resultSet.getInt("shitlisted") == 1) return true;
                if(resultSet.getInt("shitlisted") == 0) return false;
            }
            return false;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Deprecated
    public boolean isShitlisted(Player player){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("SELECT shitlisted FROM playerStats WHERE uuid=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                if(resultSet.getInt("shitlisted") == 1) return true;
                if(resultSet.getInt("shitlisted") == 0) return false;
            }
            return false;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void increaseCombatLog(UUID uuid){
        try{
            PreparedStatement ps = MySQL.get().getConnection().prepareStatement("UPDATE playerStats SET combatlog = combatlog + 1 WHERE uuid=?");
            ps.setString(1, uuid.toString());
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
