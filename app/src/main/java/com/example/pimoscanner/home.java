package com.example.pimoscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class home extends AppCompatActivity {
    Handler handler;
    Runnable runnable;
    ImageView img;
private Button button;
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



    img = findViewById ( R.id.img );
    img.animate ().alpha ( 4000 ).setDuration ( 0 );

    handler = new Handler ();
    handler.postDelayed ( new Runnable () {
        @Override
        public void run() {
            Intent intent = new Intent (home.this, Login.class);
            startActivity ( intent );
            finish ();
        }
    }, 4000 );



//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Pimo_green)));


//         for Navigating to a page or activity



//        button = (Button) findViewById(R.id.button3);
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                openHomepage();
//            }
//        });

//        button = (Button) findViewById(R.id.button3);
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                openLogin ();
//            }
//        });

    }

//    public void openHomepage(){
//        Intent intent = new Intent(this, splash.class);
//        startActivity(intent);
//    }


//    public void openLogin(){
//        Intent intent = new Intent(this, Login.class);
//        startActivity(intent);
//    }

}