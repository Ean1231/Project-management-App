package com.example.pimoscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class splash extends AppCompatActivity {
    private Button button, gotoInspection;
    private TextView username;
    private ImageView inspector, imageLogout, projects, workOrders, camera;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_splash );

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId ( R.id.home);


        inspector = findViewById ( R.id.inspector);
        bottomNavigationView = findViewById ( R.id.navigation );
        bottomNavigationView.setSelectedItemId ( R.id.home );
        username = findViewById ( R.id.username );
        imageLogout = findViewById ( R.id.imageLogout );
        workOrders = findViewById ( R.id.workOrders );
        projects = findViewById ( R.id.projects );
        camera = findViewById ( R.id.camera );

        camera.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String name = getIntent ().getStringExtra ( "name" );
                String email = getIntent ().getStringExtra ( "emaillogin" );
                String password = getIntent ().getStringExtra ( "passwordlogin" );
                String surname = getIntent ().getStringExtra ( "surname" );
                String jobTitle = getIntent ().getStringExtra ( "jobTitle" );
                String businessUnit = getIntent ().getStringExtra ( "businessUnit" );

                Intent intent = new Intent(splash.this, Camera.class);
                overridePendingTransition ( 0,0 );

                intent.putExtra ( "jobTitle",jobTitle);
                intent.putExtra ( "emaillogin",email);
                intent.putExtra ( "passwordlogin", password);
                intent.putExtra ( "name", name );
                intent.putExtra ( "surname", surname );
                intent.putExtra ( "businessUnit", businessUnit );


                startActivity(intent);
            }
        } );

        inspector.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String name = getIntent ().getStringExtra ( "name" );
                String email = getIntent ().getStringExtra ( "emaillogin" );
                String password = getIntent ().getStringExtra ( "passwordlogin" );
                String surname = getIntent ().getStringExtra ( "surname" );
                String jobTitle = getIntent ().getStringExtra ( "jobTitle" );
                String businessUnit = getIntent ().getStringExtra ( "businessUnit" );

                Intent intent = new Intent(splash.this, Inspection.class);
                overridePendingTransition ( 0,0 );

                intent.putExtra ( "emaillogin",email);
                intent.putExtra ( "passwordlogin", password);
                intent.putExtra ( "name", name );
                intent.putExtra ( "surname", surname );
                intent.putExtra ( "jobTitle",jobTitle);
                intent.putExtra ( "businessUnit", businessUnit );


                startActivity(intent);
            }
        } );

        imageLogout.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(splash.this, Login.class);
                startActivity(intent);
                Toast.makeText ( splash.this, "Logout success", Toast.LENGTH_SHORT ).show ();
                finish ();
            }
        } );
        projects.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String businessUnit2 = getIntent ().getStringExtra ( "businessUnit" );
                String name2 = getIntent ().getStringExtra ( "name" );
                String email2 = getIntent ().getStringExtra ( "emaillogin" );
                String password2 = getIntent ().getStringExtra ( "passwordlogin" );
                String jobTitle2 = getIntent ().getStringExtra ( "jobTitle" );

                Intent intent2 = new Intent(splash.this, MapsAppActivity.class);
                overridePendingTransition ( 0,0 );

                intent2.putExtra ( "emaillogin",email2);
                intent2.putExtra ( "passwordlogin", password2);
                intent2.putExtra ( "name", name2 );
                intent2.putExtra ( "jobTitle",jobTitle2);
                intent2.putExtra ( "businessUnit", businessUnit2 );

                startActivity(intent2);
            }
        } );

        workOrders.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String businessUnit3 = getIntent ().getStringExtra ( "businessUnit" );
                String name3 = getIntent ().getStringExtra ( "name" );
                String email3 = getIntent ().getStringExtra ( "emaillogin" );
                String password3 = getIntent ().getStringExtra ( "passwordlogin" );
                String jobTitle3 = getIntent ().getStringExtra ( "jobTitle" );

                Intent intent3 = new Intent(splash.this, Workorder.class);
                overridePendingTransition ( 0,0 );

                intent3.putExtra ( "emaillogin",email3);
                intent3.putExtra ( "passwordlogin", password3);
                intent3.putExtra ( "name", name3 );
                intent3.putExtra ( "jobTitle",jobTitle3);
                intent3.putExtra ( "businessUnit",businessUnit3);

                startActivity(intent3);
            }
        } );
        String businessUnit = getIntent ().getStringExtra ( "businessUnit" );
        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String surname = getIntent ().getStringExtra ( "surname" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

        username.setText ( name );

//        Toast.makeText ( this, "YOYOYOYO '"+email+"'", Toast.LENGTH_SHORT ).show ();

        bottomNavigationView.setOnNavigationItemSelectedListener ( new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId ()){
                    case R.id.camera:
                        String name = getIntent ().getStringExtra ( "name" );
                        String email = getIntent ().getStringExtra ( "emaillogin" );
                        String password = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );
                        String businessUnit = getIntent ().getStringExtra ( "businessUnit" );

                        Intent intent = new Intent(splash.this, Camera.class);
                        overridePendingTransition ( 0,0 );

                        intent.putExtra ( "emaillogin",email);
                        intent.putExtra ( "passwordlogin", password);
                        intent.putExtra ( "name", name );
                        intent.putExtra ( "jobTitle",jobTitle);
                        intent.putExtra ( "businessUnit", businessUnit );

                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.home:

                        return true;

                    case R.id.scan:
                        String businessUnit1 = getIntent ().getStringExtra ( "businessUnit" );
                        String name1 = getIntent ().getStringExtra ( "name" );
                        String email1 = getIntent ().getStringExtra ( "emaillogin" );
                        String password1 = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle1 = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent1 = new Intent(splash.this, MainActivity.class);
                        overridePendingTransition ( 0,0 );

                        intent1.putExtra ( "emaillogin",email1);
                        intent1.putExtra ( "passwordlogin", password1);
                        intent1.putExtra ( "name", name1 );
                        intent1.putExtra ( "jobTitle", jobTitle1 );
                        intent1.putExtra ( "businessUnit", businessUnit1 );

                        startActivity(intent1);
                        finish();
                        return true;

                    case R.id.projects:
                        String name2 = getIntent ().getStringExtra ( "name" );
                        String email2 = getIntent ().getStringExtra ( "emaillogin" );
                        String password2 = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle2 = getIntent ().getStringExtra ( "jobTitle" );
                        String businessUnit2 = getIntent ().getStringExtra ( "businessUnit" );

                        Intent intent2 = new Intent(splash.this, MapsAppActivity.class);
                        overridePendingTransition ( 0,0 );

                        intent2.putExtra ( "emaillogin",email2);
                        intent2.putExtra ( "passwordlogin", password2);
                        intent2.putExtra ( "name", name2 );
                        intent2.putExtra ( "jobtitle", jobTitle2 );
                        intent2.putExtra ( "businessUnit2", businessUnit2 );
                        startActivity(intent2);
                        finish();
                        return true;

                    case R.id.work:
                        String name3 = getIntent ().getStringExtra ( "name" );
                        String email3 = getIntent ().getStringExtra ( "emaillogin" );
                        String password3 = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle3 = getIntent ().getStringExtra ( "jobTitle" );
                        String businessUnit3 = getIntent ().getStringExtra ( "businessUnit" );

                        Intent intent3 = new Intent(splash.this, Workorder.class);
                        overridePendingTransition ( 0,0 );

                        intent3.putExtra ( "emaillogin",email3);
                        intent3.putExtra ( "passwordlogin", password3);
                        intent3.putExtra ( "name", name3 );
                        intent3.putExtra ( "jobTitle", jobTitle3 );
                        intent3.putExtra ( "businessUnit2", businessUnit3 );

                        startActivity(intent3);
                        finish();
                        return true;


                }
                return false;
            }
        } );

//         for Navigating to a page or activity
//        button = (Button) findViewById(R.id.button2);
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                openScaner();
//            }
//        });


//        button = (Button) findViewById(R.id.button1);
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                openCamera();
//            }
//        });
//
//        button = (Button) findViewById(R.id.btnlocation);
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                openLocation ();
//            }
//        });
//
//        button = (Button) findViewById(R.id.workOrder);
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                openWorkOrder();
//            }
//        });
    }

    private void replaceActivity(ActivityCompat activityCompat){

    }

    public void openCamera(){
        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);
    }

    public void Projects(){
        Intent intent = new Intent(this, Viewprojects.class);
        startActivity(intent);
    }

    public void openScaner(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openLocation(){
        Intent intent = new Intent(this, coordinates.class);
        startActivity(intent);
    }

    public void openWorkOrder(){
        Intent intent = new Intent(this, Workorder.class);
        startActivity(intent);
    }








}