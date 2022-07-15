package com.example.pimoscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ExtendWorkOrder extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button  btnBack;
    private TextView status, type,responsibleperson,description,workCenter, priority, dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_extend_work_order );

        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

//        Toast.makeText ( this, "WORKSS: "+email, Toast.LENGTH_SHORT ).show ();
        btnBack = findViewById ( R.id.btnBack );

        btnBack.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                status.setText ( getIntent ().getStringExtra ( "status" ) );
                type.setText ( getIntent ().getStringExtra ( "type" ) );
                responsibleperson.setText ( getIntent ().getStringExtra ( "responsibleperson" ) );
                description.setText ( getIntent ().getStringExtra ( "description" ) );
                workCenter.setText ( getIntent ().getStringExtra ( "workCenter" ) );
                dueDate.setText ( getIntent ().getStringExtra ( "dueDate" ));
                priority.setText ( getIntent ().getStringExtra ( "priority" ) );





                        String name = getIntent ().getStringExtra ( "name" );
                        String email = getIntent ().getStringExtra ( "emaillogin" );
                        String password = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent = new Intent(ExtendWorkOrder.this, Workorder.class);
//                        overridePendingTransition ( 0,0 );

                intent.putExtra ( "status", String.valueOf ( status ) );
                intent.putExtra ( "type", String.valueOf ( type ) );
                intent.putExtra ( "name", name );
                intent.putExtra ( "jobTitle", jobTitle );

                        intent.putExtra ( "emaillogin",email);
                        intent.putExtra ( "passwordlogin", password);
                        intent.putExtra ( "name", name );

                        startActivity(intent);
            }
        } );


        //------------------------------------------Menu Item

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setSelectedItemId ( R.id.work);



//        navigation.setOnNavigationItemSelectedListener ( new BottomNavigationView.OnNavigationItemSelectedListener () {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch(item.getItemId ()){
//                    case R.id.home:
//
//                        String name = getIntent ().getStringExtra ( "name" );
//                        String email = getIntent ().getStringExtra ( "emaillogin" );
//                        String password = getIntent ().getStringExtra ( "passwordlogin" );
//
//                        Intent intent = new Intent(ExtendWorkOrder.this, splash.class);
//                        overridePendingTransition ( 0,0 );
//
//                        intent.putExtra ( "emaillogin",email);
//                        intent.putExtra ( "passwordlogin", password);
//                        intent.putExtra ( "name", name );
//
//                        startActivity(intent);
//                        finish();
//                        return true;
//
//
//                    case R.id.work:
//
//                        return true;
//
//                    case R.id.camera:
//                        String name1 = getIntent ().getStringExtra ( "name" );
//
//                        String email1 = getIntent ().getStringExtra ( "emaillogin" );
//                        String password1 = getIntent ().getStringExtra ( "passwordlogin" );
//
//                        Intent intent1 = new Intent(ExtendWorkOrder.this, Camera.class);
//                        overridePendingTransition ( 0,0 );
//
//                        intent1.putExtra ( "emaillogin",email1);
//                        intent1.putExtra ( "passwordlogin", password1);
//                        intent1.putExtra ( "name", name1 );
//
//                        startActivity(intent1);
//                        finish();
//                        return true;
//
//                    case R.id.scan:
//                        String name2 = getIntent ().getStringExtra ( "name" );
//
//                        String email2 = getIntent ().getStringExtra ( "emaillogin" );
//                        String password2 = getIntent ().getStringExtra ( "passwordlogin" );
//
//                        Intent intent2 = new Intent(ExtendWorkOrder.this, MainActivity.class);
//                        overridePendingTransition ( 0,0 );
//
//                        intent2.putExtra ( "emaillogin",email2);
//                        intent2.putExtra ( "passwordlogin", password2);
//                        intent2.putExtra ( "name", name2 );
//
//                        startActivity(intent2);
//                        finish();
//                        return true;
//
//                    case R.id.projects:
//                        String name3 = getIntent ().getStringExtra ( "name" );
//
//                        String email3 = getIntent ().getStringExtra ( "emaillogin" );
//                        String password3 = getIntent ().getStringExtra ( "passwordlogin" );
//
//                        Intent intent3 = new Intent(ExtendWorkOrder.this, ActiveProjects.class);
//                        overridePendingTransition ( 0,0 );
//
//                        intent3.putExtra ( "emaillogin",email3);
//                        intent3.putExtra ( "passwordlogin", password3);
//                        intent3.putExtra ( "name", name3 );
//
//                        startActivity(intent3);
//                        finish();
//                        return true;
//                }
//                return true;
//            }
//        } );
//
//        //-------------------------------------------------------------
        status = findViewById ( R.id.status );
        type = findViewById ( R.id.type );
        responsibleperson = findViewById ( R.id.responsibleperson );
        description = findViewById ( R.id.workCenter );
        workCenter = findViewById ( R.id.description );
        dueDate = findViewById ( R.id.dueDate );
        priority = findViewById ( R.id.priority );


        status.setText ( getIntent ().getStringExtra ( "status" ) );
        type.setText ( getIntent ().getStringExtra ( "type" ) );
        responsibleperson.setText ( getIntent ().getStringExtra ( "responsibleperson" ) );
        description.setText ( getIntent ().getStringExtra ( "description" ) );
        workCenter.setText ( getIntent ().getStringExtra ( "workCenter" ) );
        dueDate.setText ( getIntent ().getStringExtra ( "dueDate" ));
        priority.setText ( getIntent ().getStringExtra ( "priority" ) );
//        dueDate.setText ( getIntent ().getStringExtra ( "dueDate" ) );


    }
}