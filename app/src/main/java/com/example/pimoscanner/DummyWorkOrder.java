package com.example.pimoscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class DummyWorkOrder extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_dummy_work_order );

        BottomNavigationView navigation = (BottomNavigationView) findViewById ( R.id.navigation );
        navigation.setSelectedItemId ( R.id.work );


        navigation.setOnNavigationItemSelectedListener ( new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId ()) {
                    case R.id.work:
                        return true;
                    case R.id.home:
                        startActivity ( new Intent ( getApplicationContext (), splash.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.scan:
                        startActivity ( new Intent ( getApplicationContext (), MainActivity.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.projects:
                        startActivity ( new Intent ( getApplicationContext (), ActiveProjects.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;

                    case R.id.camera:
                        startActivity ( new Intent ( getApplicationContext (), DummyWorkOrder.class ) );
                        overridePendingTransition ( 0, 0 );
                        return true;


                }
                return true;
            }
        } );

//        RecyclerView recyclerView = findViewById ( R.id.viewer );
//        recyclerView.setHasFixedSize ( (true) );
//        recyclerView.setLayoutManager ( new LinearLayoutManager ( this ) );
//
//
//        Dummy[] dummies = new Dummy[]{
//                new Dummy ( "Ean Bosman","2022-02-29",R.drawable.image ),
//
//        };
//        MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter ( dummies,DummyWorkOrder.this );
//        recyclerView.setAdapter ( myRecyclerViewAdapter );
//    }
    }
}