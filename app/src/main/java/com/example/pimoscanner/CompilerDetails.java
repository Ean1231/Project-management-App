package com.example.pimoscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CompilerDetails extends AppCompatActivity {
    private TextView compilerName, compilerSurname, compileDate, jobtitle;
    private ImageView compilerSignature;
    private Button submit;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_compiler_details );

        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String surname = getIntent ().getStringExtra ( "surname" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );
        compilerSignature = findViewById ( R.id.signatureView );




//
//        Bundle bundle = getIntent ().getExtras ();
//        if(bundle!=null){
//            int bitmap = bundle.getInt("bitmap");
//            compilerSignature.setImageResource ( bitmap );
//        }


        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("bitmap");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = (ImageView) findViewById(R.id.signatureView);

        image.setImageBitmap(bmp);
//        Toast.makeText ( this, "MOOOOOOOOOI '"+email+"'", Toast.LENGTH_SHORT ).show ();

        jobtitle = findViewById ( R.id.jobtitle );
        compilerName = findViewById ( R.id.compilerName );
        compilerSurname = findViewById ( R.id.compilerSurname );
        compileDate = findViewById ( R.id.compileDate );
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        compileDate.setText(date);

        submit = findViewById ( R.id.submit );
        submit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {


                String name = getIntent ().getStringExtra ( "name" );
                String email = getIntent ().getStringExtra ( "emaillogin" );
                String password = getIntent ().getStringExtra ( "passwordlogin" );
                String surname = getIntent ().getStringExtra ( "surname" );
                String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

                Intent intent = new Intent(CompilerDetails.this, splash.class);
                overridePendingTransition ( 0,0 );

                intent.putExtra ( "emaillogin",email);
                intent.putExtra ( "passwordlogin", password);
                intent.putExtra ( "name", name );
                intent.putExtra ( "surname", surname );
                intent.putExtra ( "jobTitle", jobTitle );

                startActivity(intent);
                Toast.makeText ( CompilerDetails.this, "Saved", Toast.LENGTH_SHORT ).show ();

            }
        } );


        compilerName.setText ( name );
        compilerSurname.setText ( surname );
        jobtitle.setText ( jobTitle );




    }
}