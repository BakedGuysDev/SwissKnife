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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;

public class MySQL {

    private Connection connection;

    public boolean isConnected(){
        return (connection == null ? false : true);
    }

    public void connect() throws ClassNotFoundException, SQLException{
        if(!isConnected()){
            connection = DriverManager.getConnection("jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName + "?useSSL=false", databaseUsername, databasePassword);
        }
    }

    public Connection getConnection(){
        return connection;
    }


    public void disconnect(){
        if(!isConnected()) return;
        try{
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
