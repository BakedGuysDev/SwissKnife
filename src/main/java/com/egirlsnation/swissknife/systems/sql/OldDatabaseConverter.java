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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OldDatabaseConverter {

    private final Connection connection;

    public OldDatabaseConverter(Connection connection){
        this.connection = connection;
    }

    private boolean hasOldDbVersion(){
        try{
            PreparedStatement ps = connection.prepareStatement("SHOW TABLES LIKE 'swissPlayerStats'");

            return ps.executeQuery().next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
