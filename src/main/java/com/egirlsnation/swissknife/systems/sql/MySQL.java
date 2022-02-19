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
import com.egirlsnation.swissknife.systems.System;
import com.egirlsnation.swissknife.systems.Systems;
import org.bukkit.ChatColor;
import org.simpleyaml.configuration.ConfigurationSection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends System<MySQL> {

    public boolean useDatabase = false;

    public String databaseHost = "172.18.0.1";

    public String databasePort = "3306";

    public String databaseName = "name";

    public String databaseUsername = "username";

    public String databasePassword = "password";

    private Connection connection;

    private SqlQuery sql = null;

    public MySQL(){
        super("mysql");
    }

    public static MySQL get(){
        return Systems.get(MySQL.class);
    }

    public boolean isConnected(){
        return (connection != null);
    }

    private void connect() throws ClassNotFoundException, SQLException{
        if(!isConnected()){
            connection = DriverManager.getConnection("jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName + "?useSSL=false", databaseUsername, databasePassword);
        }
    }

    public Connection getConnection(){
        return connection;
    }


    private void disconnect(){
        if(!isConnected()) return;
        try{
            connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void writeToConfig(){
        ConfigurationSection section = getFile().createSection("mysql");

        getFile().setComment("mysql", "\nMySQL config options\n");
        section.set("use-mysql", useDatabase);
        getFile().setComment("mysql.use-mysql", "If mysql db will be used. Some modules depend on it");
        section.set("host", databaseHost);
        section.set("port", databasePort);
        section.set("db-name", databaseName);
        section.set("db-username", databaseUsername);
        section.set("db-password", databasePassword);

        try {
            getFile().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readFromConfig(){
        ConfigurationSection section = getFile().getConfigurationSection("config.mysql");
        if(section == null){
            writeToConfig();
            return;
        }

        useDatabase = section.getBoolean("use-mysql");
        databaseHost = section.getString("host");
        databasePort = section.getString("port");
        databaseName = section.getString("db-name");
        databaseUsername = section.getString("db-username");
        databasePassword = section.getString("db-password");
    }

    //TODO: Test and migrate database (possibly in code)

    public void initDatabase(){
        if(useDatabase) return;

        SwissKnife.swissLogger.info("Initializing SQL driver.");

        if(databaseName.equals("name") && databaseUsername.equals("username") && databasePassword.equals("password")){
            SwissKnife.swissLogger.warning("Default SQL config values detected. SQL driver won't be initiated.");
            return;
        }

        try{
            connect();
        }catch(SQLException | ClassNotFoundException ex){
            SwissKnife.swissLogger.severe("Something went wrong while initiating SQL\nStack trace will follow.");
            ex.printStackTrace();
        }

        if(isConnected()){
            SwissKnife.swissLogger.info(ChatColor.GREEN + "Sucessfully connected to SwissKnife database.");
            sql = new SqlQuery();
            sql.createStatsTable();
            sql.createPlayerDataTable();
            SwissKnife.swissLogger.info(ChatColor.GREEN + "Finished SQL initialization.");
        }
    }

    public SqlQuery getSqlQuery(){
        return sql;
    }

    @Override
    public void onSave(){
        if(isConnected()){
            disconnect();
        }
    }
}
