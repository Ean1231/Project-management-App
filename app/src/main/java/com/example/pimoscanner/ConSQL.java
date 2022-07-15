package com.example.pimoscanner;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConSQL {

    //20.87.30.25:3389 ,,,, 192.168.1.58//
    @SuppressLint ( "NewApi" )
    public String ip = "192.168.0.184";
    public String port = "1433";
    public String db = "workOrders";
    public String username = "";
    public String password = "M@ckey001";
    public String url = "";
    public String login = "EanBosman";
    public Connection connection = null;


   public Connection connection(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            url = "jdbc:jtds:sqlserver://"+ip+":"+port+";"+"databasename="+db+";user="+login+";password="+password+";";
            connection = DriverManager.getConnection(url);
            System.out.println (connection);
        }
        catch (Exception e){
        }
       return connection;
   }
}

