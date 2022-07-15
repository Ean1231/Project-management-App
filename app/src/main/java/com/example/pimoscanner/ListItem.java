package com.example.pimoscanner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItem {
    Connection connect;

    String ConnectionResult = "";
    Boolean isSuccess = false;

    public List<Map<String,String>> getlist(){
        List<Map<String,String>>data = null;
        data = new ArrayList<Map<String,String>>();
        try{
            ConSQL conSQL = new ConSQL ();
            connect = conSQL.connection ();

            if(connect !=null){
                String query = "select * from Users";
                Statement statement = connect.createStatement ();
                ResultSet resultset = statement.executeQuery ( query );
                while (resultset.next()){
                    Map<String,String> dtname = new HashMap<String, String> ();
                    dtname.put("ID", resultset.getString("ID"));
                    dtname.put("Firstname", resultset.getString("Firstname"));
                    dtname.put("lastname", resultset.getString("lastname"));
                    dtname.put("Age", resultset.getString("Age"));
                    data.add(dtname);
                }
                ConnectionResult = "Success";
                isSuccess = true;
                connect.close ();
            }
            else{
                ConnectionResult = "Failed";
            }

        }catch (SQLException throwables){
            throwables.printStackTrace ();
        }
        return data;
    }
}
