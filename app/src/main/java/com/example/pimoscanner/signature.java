package com.example.pimoscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.ByteArrayOutputStream;

public class signature extends AppCompatActivity {
    public static final String KEY1 = "bitmap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_signature );

        SignaturePad signaturePad = findViewById ( R.id.signature_pad );
        Button button = findViewById ( R.id.signatureBtn );
        ImageView imageView = findViewById ( R.id.signatureView );
        Button clearSignature = findViewById ( R.id.clearSignature );

        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String surname = getIntent ().getStringExtra ( "surname" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );


//        Toast.makeText ( this, "MOOOOOOOOOI '"+email+"'", Toast.LENGTH_SHORT ).show ();




        button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {


                Bitmap bitmap = signaturePad.getSignatureBitmap ();
                imageView.setImageBitmap ( bitmap );
//                signaturePad.clear ();
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();



//                Toast.makeText ( signature.this, "Inspection Document saved", Toast.LENGTH_SHORT ).show ();


                String name = getIntent ().getStringExtra ( "name" );
                String email = getIntent ().getStringExtra ( "emaillogin" );
                String password = getIntent ().getStringExtra ( "passwordlogin" );
                String surname = getIntent ().getStringExtra ( "surname" );
                String jobTitle = getIntent ().getStringExtra ( "jobTitle" );


                System.out.println ("Image name: " +bitmap);

//                Intent intent = new Intent(signature.this, CompilerDetails.class);
                Intent intent = new Intent(signature.this, CompilerDetails.class);
                intent.putExtra("bitmap", byteArray);

                intent.putExtra ( "emaillogin",email);
                intent.putExtra ( "passwordlogin", password);
                intent.putExtra ( "name", name );
                intent.putExtra ( "surname", surname );
                intent.putExtra ( "jobTitle", jobTitle );
                startActivity(intent);

                finish ();
            }


        } );

        clearSignature.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                signaturePad.clear ();
            }
        } );


    }
}