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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OldDatabaseConverter {

    private final Connection connection;

    public OldDatabaseConverter(Connection connection){
        this.connection = connection;
    }

    private final List<UUID> uuids = new ArrayList<>();
    private final List<UUID> failedUuids = new ArrayList<>(1);

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
                uuids.add(UUID.fromString(rs.getString("UUID")));
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

    public void emptyUuidList(){
        uuids.clear();
    }
}
