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

package com.egirlsnation.swissknife.systems.sql;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.utils.player.PlayerInfo;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SqlQuery {

    private final SwissKnife plugin;
    public SqlQuery(SwissKnife plugin){ this.plugin = plugin; }

    public void createStatsTable(){
        PreparedStatement ps;
        try{
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerStats "
                    + "(Name VARCHAR(32),UUID CHAR(36),playTime INT(11),kills INT(11),deaths INT(11),mobKills INT(11),shitlisted TINYINT(1),firstPlayed VARCHAR(100), blocksMined INT(11), distanceWalked INT(11), distanceElytra INT(11), distanceSprinted INT(11), timeSinceDeath INT(11), combatLogs INT(11), PRIMARY KEY (Name))");
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createPingTable(){
        PreparedStatement ps;
        try{
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerPing "
                    + "(ID INT(11),Name VARCHAR(32),UUID CHAR(36),ping INT(11), timestamp INT(11),PRIMARY KEY (ID))");
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addPingRecord(PlayerInfo info, Integer ping){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO playerPing"
                    + "(Name,UUID,ping,timestamp) VALUES (?,?,?,?)");
            ps.setString(1, info.getName());
            ps.setString(2, info.getUuid().toString());
            ps.setInt(3, ping);
            ps.setLong(4, System.currentTimeMillis() / 1000);

            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void purgePingRecords(String playerName){
        try{
            PreparedStatement count = plugin.SQL.getConnection().prepareStatement("SELECT COUNT() FROM playerPing WHERE Name=? ORDER BY timestamp DESC");
            count.setString(1, playerName);
            ResultSet countSet = count.executeQuery();
            int countNum = countSet.getInt(1);
            if(countNum <= 100){
                return;
            }

            int toDelete = countNum - 100;

            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("DELETE FROM playerPing WHERE Name=? ORDER BY timestamp ASC limit ?");
            ps.setString(1, playerName);
            ps.setInt(2, toDelete);
            ResultSet resultSet = ps.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return;
    }

    public void createPlayer(Player player){
        try{
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM playerStats WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            if(!exists(uuid)){

                Date date = new Date(player.getFirstPlayed());
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String firstPlayed = sdf.format(date);

                PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO playerStats"
                        + " (Name,UUID,playTime,kills,deaths,mobKills,shitlisted,firstPlayed,blocksMined,distanceWalked,distanceElytra,distanceSprinted,timeSinceDeath,combatLogs) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.setInt(3, 0);
                ps2.setInt(4, 0);
                ps2.setInt(5, 0);
                ps2.setInt(6, 0);
                ps2.setInt(7, 0);
                ps2.setString(8, firstPlayed);
                ps2.setInt(9, 0);
                ps2.setInt(10, 0);
                ps2.setInt(11, 0);
                ps2.setInt(12,0);
                ps2.setInt(13, 0);
                ps2.setInt(14, 0);

                ps2.executeUpdate();

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createPlayer(Player player, boolean shitlisted){
        try{
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM playerStats WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            if(!exists(uuid)){

                Date date = new Date(player.getFirstPlayed());
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String firstPlayed = sdf.format(date);

                PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO playerStats"
                        + " (Name,UUID,playTime,kills,deaths,mobKills,shitlisted,firstPlayed,blocksMined,distanceWalked,distanceElytra,distanceSprinted,timeSinceDeath,combatLogs) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.setInt(3, 0);
                ps2.setInt(4, 0);
                ps2.setInt(5, 0);
                ps2.setInt(6, 0);
                if(shitlisted){
                    ps2.setInt(7, 1);
                }else{
                    ps2.setInt(7, 0);
                }
                ps2.setString(8, firstPlayed);
                ps2.setInt(9, 0);
                ps2.setInt(10, 0);
                ps2.setInt(11, 0);
                ps2.setInt(12, 0);
                ps2.setInt(13, 0);
                ps2.setInt(14, 0);

                ps2.executeUpdate();

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean exists(UUID uuid){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM playerStats WHERE UUID=?");
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
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM playerStats WHERE Name =?");
            ps.setString(1, playerName);

            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void updateValues(Player player){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE playerStats SET playTime=?,kills=?,deaths=?,mobKills=?,blocksMined=?,distanceWalked=?,distanceElytra=?,distanceSprinted=?,timeSinceDeath=? WHERE UUID=?");
            ps.setInt(1, player.getStatistic(Statistic.PLAY_ONE_MINUTE));
            ps.setInt(2, player.getStatistic(Statistic.PLAYER_KILLS));
            ps.setInt(3, player.getStatistic(Statistic.DEATHS));
            ps.setInt(4, player.getStatistic(Statistic.MOB_KILLS));
            ps.setInt(5, player.getStatistic(Statistic.MINE_BLOCK, Material.OBSIDIAN));
            ps.setInt(6, player.getStatistic(Statistic.WALK_ONE_CM));
            ps.setInt(7, player.getStatistic(Statistic.AVIATE_ONE_CM));
            ps.setInt(8, player.getStatistic(Statistic.SPRINT_ONE_CM));
            ps.setInt(9, player.getStatistic(Statistic.TIME_SINCE_DEATH));

            ps.setString(10, player.getUniqueId().toString());
            if (!exists(player.getUniqueId())) {
                createPlayer(player);
            }
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public long getPlaytime(Player player){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT playTime FROM playerStats WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("playTime");
            }
            return 0;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public long getPlaytime(String playerName){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT playTime FROM playerStats WHERE Name=?");
            ps.setString(1, playerName);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("playTime");
            }
            return 0;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void addToShitlist(Player player){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE playerStats SET shitlisted=? WHERE UUID=?");
            ps.setInt(1, 1);
            ps.setString(2, player.getUniqueId().toString());
            if (!exists(player.getUniqueId())) {
                createPlayer(player, true);
            }
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void removeFromShitlist(Player player){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE playerStats SET shitlisted=? WHERE UUID=?");
            ps.setInt(1, 0);
            ps.setString(2, player.getUniqueId().toString());
            if (!exists(player.getUniqueId())) {
                createPlayer(player, false);
            }
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addToShitlist(String name){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE playerStats SET shitlisted=? WHERE Name=?");
            ps.setInt(1, 1);
            ps.setString(2, name);
            if (!exists(name)) {
                return;
            }
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void removeFromShitlist(String name){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE playerStats SET shitlisted=? WHERE Name=?");
            ps.setInt(1, 0);
            ps.setString(2, name);
            if (!exists(name)) {
                return;
            }
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean isShitlisted(String name){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT shitlisted FROM playerStats WHERE Name=?");
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

    public boolean isShitlisted(Player player){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT shitlisted FROM playerStats WHERE UUID=?");
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
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE playerStats SET combatLog = combatLog + 1 WHERE UUID=?");
            ps.setString(1, uuid.toString());
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
