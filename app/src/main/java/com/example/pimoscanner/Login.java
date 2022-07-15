package com.example.pimoscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {
    EditText emaillogin,passwordlogin;
    Button loginbtn,regbtn;
    Spinner spinner;
    TextView register;
    Connection con;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );

        emaillogin = (EditText)findViewById(R.id.emaillogin);
        passwordlogin = (EditText)findViewById(R.id.passwordlogin);
        loginbtn = (Button)findViewById(R.id.loginbtn);
//      regbtn = (Button)findViewById(R.id.regbtn);
        register = (TextView)findViewById ( R.id.registerbtn );



        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Login.checkLogin().execute("");
            }
        });

//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent (Login.this,Register.class);
//                startActivity(intent);
//                finish();
//            }
//        });

    }

    public class checkLogin extends AsyncTask<String, String, String> {

        String z = null;
        Boolean isSuccess = false;


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {
        }

        @Override
        protected String doInBackground(String... strings) {
            con = connectionClass(ConnectionClass.un.toString(),ConnectionClass.pass.toString(),ConnectionClass.db.toString(),ConnectionClass.ip.toString());
            if(con == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Login.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                z = "On Internet Connection";
            }
            else {
                try {
                    String businessUnit = null;
                    String jobTitle = null;
                    String surname = null;
                    String name = null;
                    String sql = "SELECT * FROM registerr WHERE emailAddress = '" + emaillogin.getText() + "' And password = '"+passwordlogin.getText ()+ "' ";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);


                    if (rs.next()) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(Login.this, "Login Success...", Toast.LENGTH_LONG).show();
                            }

                        });
                        businessUnit = rs.getString ( "businessUnit" );
                        jobTitle = rs.getString ( "jobTitle" );
                        surname = rs.getString ( "surname" );
                        name = rs.getString ( "name" );
//                        Toast.makeText ( Login.this, "Name + '"+name+"'", Toast.LENGTH_SHORT ).show ();
                        System.out.println ("Name :" +name);
                        isSuccess=true;
                        z = "Success";

                       String email = emaillogin.getText ().toString ();
                       String password =  passwordlogin.getText ().toString ();




                        Intent intent = new Intent(Login.this, splash.class);

                        intent.putExtra ( "jobTitle" , jobTitle);
                        intent.putExtra ( "emaillogin",email);
                        intent.putExtra ( "passwordlogin", password);
                        intent.putExtra ( "name", name );
                        intent.putExtra ( "surname", surname );
                        intent.putExtra ( "businessUnit", businessUnit );


                        startActivity(intent);



                        finish();


                    } else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this, "Check email or password", Toast.LENGTH_LONG).show();
                            }
                        });
                        emaillogin.setText("");
                        passwordlogin.setText("");
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    Log.e("SQL Error : ", e.getMessage());
                }

            }

            return z;
        }

    }

    @SuppressLint("NewApi")
    public Connection connectionClass(String user, String password, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" + server+"/" + database + ";user=" + user + ";password=" + password + ";";
            connection = DriverManager.getConnection(connectionURL);
        }catch (Exception e){
            Log.e("SQL Connection Error : ", e.getMessage());
        }

        return connection;
    }
}