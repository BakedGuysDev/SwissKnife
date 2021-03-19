package com.egirlsnation.swissknife.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class mysqlService {

    private final String host = "172.18.0.1";
    private final String port = "3306";
    private String dbName = "s7_swissknife";
    private String userName = "u7_I6qTNAok8x";
    private String password = "63HkPER++anYmke=qIJa4..Q";

    private Connection connection;

    public boolean isConnected(){
        return (connection == null ? false : true);
    }

    public void connect() throws ClassNotFoundException, SQLException{
        if(!isConnected()){
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false", userName, password);
        }
    }

    public void disconnect(){
        if(isConnected()){
            try{
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection(){
        return connection;
    }
}
