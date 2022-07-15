package com.example.pimoscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Inspection extends AppCompatActivity {

 Button startInspection, fab, endInspection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_inspection );

//        Toolbar toolbar = findViewById ( R.id.toolbar );
//        setActionBar ( toolbar );
//        TextView toolbarTitle = toolbar.findViewById ( R.id.toolbarTitle );
//        toolbarTitle.setText ( getSupportActionBar ().getTitle () );
//        getSupportActionBar ().setTitle ( null );

        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String surname = getIntent ().getStringExtra ( "surname" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );


//        Toast.makeText ( this, "MACKEY '"+email+"'", Toast.LENGTH_SHORT ).show ();
        endInspection = (Button) findViewById ( R.id.endtInspection );
        startInspection = (Button) findViewById ( R.id.startInspection );
//        FloatingActionButton fab = findViewById(R.id.fab);
//        FloatingActionButton fab1 = findViewById(R.id.fab1);

        endInspection.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String name = getIntent ().getStringExtra ( "name" );
                String email = getIntent ().getStringExtra ( "emaillogin" );
                String password = getIntent ().getStringExtra ( "passwordlogin" );
                String surname = getIntent ().getStringExtra ( "surname" );
                String jobTitle = getIntent ().getStringExtra ( "jobTitle" );
                String businessUnit = getIntent ().getStringExtra ( "businessUnit" );

                Intent intent = new Intent(Inspection.this, splash.class);


                intent.putExtra ( "emaillogin",email);
                intent.putExtra ( "passwordlogin", password);
                intent.putExtra ( "name", name );
                intent.putExtra ( "surname", surname );
                intent.putExtra ( "jobTitle", jobTitle );
                intent.putExtra ( "businessUnit", businessUnit );

                startActivity(intent);
            }
        } );

        startInspection.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String name = getIntent ().getStringExtra ( "name" );
                String email = getIntent ().getStringExtra ( "emaillogin" );
                String password = getIntent ().getStringExtra ( "passwordlogin" );
                String surname = getIntent ().getStringExtra ( "surname" );
                String jobTitle = getIntent ().getStringExtra ( "jobTitle" );
                String businessUnit = getIntent ().getStringExtra ( "businessUnit" );
//                Toast.makeText ( Inspection.this, "business unit" + businessUnit, Toast.LENGTH_SHORT ).show ();
                System.out.println ( businessUnit);

                                           //ROLE RIGHTS
                if (businessUnit!=null && businessUnit.equals ( "ICT" )){
                    Intent intent = new Intent(Inspection.this, RadioInspection.class);
                    overridePendingTransition ( 0,0 );

                    intent.putExtra ( "emaillogin",email);
                    intent.putExtra ( "passwordlogin", password);
                    intent.putExtra ( "name", name );
                    intent.putExtra ( "surname", surname );
                    intent.putExtra ( "jobTitle", jobTitle );
                    intent.putExtra ( "businessUnit", businessUnit );

                    startActivity(intent);

                }else if(businessUnit!=null && businessUnit.equals ( "Electrical" )){
                    Intent intent = new Intent(Inspection.this, Electrical.class);
                    overridePendingTransition ( 0,0 );

                    intent.putExtra ( "emaillogin",email);
                    intent.putExtra ( "passwordlogin", password);
                    intent.putExtra ( "name", name );
                    intent.putExtra ( "surname", surname );
                    intent.putExtra ( "jobTitle", jobTitle );
                    intent.putExtra ( "businessUnit", businessUnit );

                    startActivity(intent);


                }else{
//                    Intent intent = new Intent(Inspection.this, RadioInspection.class);
//                    overridePendingTransition ( 0,0 );
//
//                    intent.putExtra ( "emaillogin",email);
//                    intent.putExtra ( "passwordlogin", password);
//                    intent.putExtra ( "name", name );
//                    intent.putExtra ( "surname", surname );
//                    intent.putExtra ( "jobTitle", jobTitle );
//
//                    startActivity(intent);
                    System.out.println ("ERROR OCCURED" );


                }



            }
        } );



    }
}