package com.example.pimoscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Layers extends AppCompatActivity {
    private TextView my_points, police;
    private Button back;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_layers );


        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );


        back = findViewById ( R.id.back );
        back.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Layers.this, MapsAppActivity.class);
                overridePendingTransition ( 0,0 );

                intent.putExtra ( "jobTitle",jobTitle);
                intent.putExtra ( "emaillogin",email);
                intent.putExtra ( "passwordlogin", password);
                intent.putExtra ( "name", name );
                startActivity(intent);
            }
        } );

        my_points = findViewById ( R.id.points );
        my_points.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Layers.this, ActiveProjects.class);
                overridePendingTransition ( 0,0 );

                intent.putExtra ( "jobTitle",jobTitle);
                intent.putExtra ( "emaillogin",email);
                intent.putExtra ( "passwordlogin", password);
                intent.putExtra ( "name", name );
                startActivity(intent);


            }
        } );
        police = findViewById ( R.id.police );
        police.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Layers.this, SecondLayerMap.class);
                overridePendingTransition ( 0,0 );

                intent.putExtra ( "jobTitle",jobTitle);
                intent.putExtra ( "emaillogin",email);
                intent.putExtra ( "passwordlogin", password);
                intent.putExtra ( "name", name );
                startActivity(intent);


            }
        } );
    }


}