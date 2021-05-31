package com.egirlsnation.swissknife.sql;

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
