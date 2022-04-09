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

import com.egirlsnation.swissknife.utils.entity.player.SwissPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataDriver {
    
    private final Connection connection;
    
    public PlayerDataDriver(Connection connection){
        this.connection = connection;
    }

    public void createPlayerDataTable(){
        PreparedStatement ps;
        try{
            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS swissPlayerData "
                    + "(uuid CHAR(36) NOT NULL," +
                    "petTotems BIT(1) DEFAULT 0," +
                    "draconiteAbilities BIT(1) DEFAULT 0," +
                    "moduleAlerts BIT(1) DEFAULT 1," +
                    " PRIMARY KEY (uuid))");
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createPlayerData(SwissPlayer player){
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM swissPlayerData WHERE uuid=?");
            ps.setString(1, player.getUuid().toString());
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next()){

                PreparedStatement ps2 = connection.prepareStatement("INSERT IGNORE INTO swissPlayerData"
                        + " (uuid,petTotems,draconiteAbilities,moduleAlerts)" +
                        " VALUES (?,?,?,?)");
                ps2.setString(1, player.getUuid().toString());
                ps2.setBoolean(2, player.hasFeatureEnabled(SwissPlayer.SwissFeature.PET_TOTEMS));
                ps2.setBoolean(3, player.hasFeatureEnabled(SwissPlayer.SwissFeature.DRACONITE_ABILITIES));
                ps2.setBoolean(4, true);

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

            PreparedStatement ps = connection.prepareStatement("UPDATE swissPlayerData SET" +
                    " petTotems=?,draconiteAbilities=?,moduleAlerts=?" +
                    " WHERE uuid=?");

            ps.setBoolean(1, player.hasFeatureEnabled(SwissPlayer.SwissFeature.PET_TOTEMS));
            ps.setBoolean(2, player.hasFeatureEnabled(SwissPlayer.SwissFeature.DRACONITE_ABILITIES));
            ps.setBoolean(3, player.hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS));
            ps.setString(4, player.getUuid().toString());

            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean hasPlayerDataEntry(UUID uuid){
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM swissPlayerData WHERE uuid=?");
            ps.setString(1, uuid.toString());

            return ps.executeQuery().next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Map<SwissPlayer.SwissFeature, Boolean> getFeatureMap(UUID uuid){
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM swissPlayerData WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                Map<SwissPlayer.SwissFeature, Boolean> featureMap = new HashMap<>(2);
                featureMap.put(SwissPlayer.SwissFeature.PET_TOTEMS, resultSet.getBoolean("petTotems"));
                featureMap.put(SwissPlayer.SwissFeature.DRACONITE_ABILITIES, resultSet.getBoolean("draconiteAbilities"));
                featureMap.put(SwissPlayer.SwissFeature.MODULE_ALERTS, resultSet.getBoolean("moduleAlerts"));

                return featureMap;
            }else{
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
