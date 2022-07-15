package com.example.pimoscanner;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.pimoscanner.Camera.mImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RadioInspection extends AppCompatActivity {

    private TextView tvQuestion, tvScore, tvQuestionNo, tvtimer, tvtextQuestion;
    private RadioGroup radioGroup;
    private RadioButton rb1,rb2,rb3,rb4,rb5;
    private Button btnNext, btnPrevious, camBtn;
    private EditText comment;

    //------Camera image--------------------
    private ImageView mImageView;
    private ArrayList permissionsToRequest;
    private final ArrayList permissionsRejected = new ArrayList ();
    private final ArrayList permissions = new ArrayList ();
    private final static int ALL_PERMISSIONS_RESULT = 101;

    Uri image_uri;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    //----------------------------------

    private File filePath;
    private Object RequestDigest;



    int totalQuestions;
    int qCounter = 0;
    int score;




    ColorStateList dfRbColor;
    boolean answered;

    private QuestionModel currentQuestion;

    private List<QuestionModel> questionslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_radio_inspection );




        //-----------------------Camera---------------
        //Permissions
        ActivityCompat.requestPermissions ( RadioInspection.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1 );
        ActivityCompat.requestPermissions ( RadioInspection.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1 );

        //---
        permissions.add ( ACCESS_FINE_LOCATION );
        permissions.add ( ACCESS_COARSE_LOCATION );

        permissionsToRequest = findUnAskedPermissions ( permissions );
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size () > 0)
                requestPermissions ( (String[]) permissionsToRequest.toArray ( new String[permissionsToRequest.size ()] ), ALL_PERMISSIONS_RESULT );
        }


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder ().permitAll ().build ();
            StrictMode.setThreadPolicy ( policy );
        }
        //----------------------------------------------------------------

        questionslist = new ArrayList<> ();
        comment = findViewById ( R.id.comment );
        tvQuestion = findViewById ( R.id.textQuestion );
//        tvScore = findViewById ( R.id.textScore );
//        tvQuestionNo = findViewById ( R.id.textQuestionNo );
//        tvtimer = findViewById ( R.id.textTimer );

        radioGroup = findViewById ( R.id.radioGroup );
        rb1 = findViewById ( R.id.rb1 );
        rb2 = findViewById ( R.id.rb2 );
        rb3 = findViewById ( R.id.rb3 );
        rb4 = findViewById ( R.id.rb4 );
        rb5 = findViewById ( R.id.rb5 );
        tvtextQuestion = findViewById ( R.id.textQuestion );
        camBtn = findViewById ( R.id.camBtn );


        //PDF
        ActivityCompat.requestPermissions ( RadioInspection.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED );

        btnPrevious = findViewById ( R.id.btnPrevious );
        btnNext = findViewById ( R.id.btnNext );

        dfRbColor = rb1.getTextColors ();
        
        addQuestions();

        totalQuestions = questionslist.size ();
        
        showNextQuestion();

        btnNext.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if(answered == false){
                    if(rb1.isChecked () || rb2.isChecked () || rb3.isChecked () || rb4.isChecked () || rb5.isChecked ()){
                        checkAnswer();
                    }else{
                        Toast.makeText ( RadioInspection.this, "Please select an option", Toast.LENGTH_SHORT ).show ();
                    }
                }else{
                    showNextQuestion ();
                    mImageView.setImageResource(0);


                }
            }
        } );

        btnPrevious.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                showPreviousQuestion();
            }
        } );

//------------------Camera----------------------------------

        mImageView = findViewById ( R.id.mImageView );

        //Capture button click
        camBtn.setOnClickListener ( v -> {
            //if system os is >= marshmallow, request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission ( Manifest.permission.CAMERA ) ==
                        PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission ( Manifest.permission.WRITE_EXTERNAL_STORAGE ) ==
                                PackageManager.PERMISSION_DENIED) {
                    //permission not enabled, request it
                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    //show popup to request permissions
                    requestPermissions ( permission, PERMISSION_CODE );
                } else {
                    //permission already granted
                    openCamera ();
                }
            } else {
                //system os < marshmallow
                openCamera ();

            }
        } );
//-------------------------------------------------------------------
        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String surname = getIntent ().getStringExtra ( "surname" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

//        Toast.makeText ( this, "MACKEY '"+email+"'", Toast.LENGTH_SHORT ).show ();
    }


    //-----------------Camera

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList ();

        for (Object perm : wanted) {
            if (!hasPermission ( (String) perm )) {
                result.add ( perm );
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores ()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission ( permission ) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void openCamera() {
        ContentValues values = new ContentValues ();
        values.put ( MediaStore.Images.Media.TITLE, "New Picture" );
        values.put ( MediaStore.Images.Media.DESCRIPTION, "From the Camera" );
        image_uri = getContentResolver ().insert ( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values );
        //Camera intent
        Intent cameraIntent = new Intent ( MediaStore.ACTION_IMAGE_CAPTURE );
        cameraIntent.putExtra ( MediaStore.EXTRA_OUTPUT, image_uri );
        startActivityForResult ( cameraIntent, IMAGE_CAPTURE_CODE );
    }


    //------------------------Camera And Image

    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        super.onRequestPermissionsResult ( requestCode, permissions, grantResults );
        if (requestCode == PERMISSION_CODE) {
            {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    openCamera ();
                } else {
                    //permission from popup was denied
                    Toast.makeText ( this, "Permission denied...", Toast.LENGTH_SHORT ).show ();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //called when image was captured from camera

        super.onActivityResult ( requestCode, resultCode, data );
        if (resultCode == RESULT_OK) {
            //set the image captured to our ImageView
            mImageView.setImageURI ( image_uri );
        }
    }

    //-----------------------------------------------------------------------

    private void checkAnswer(){
        answered = true;
        RadioButton rbSelected = findViewById ( radioGroup.getCheckedRadioButtonId () );
        int answerNo = radioGroup.indexOfChild ( rbSelected ) + 1;
        if(answerNo == currentQuestion.getCorrectAnsno ()){
            score++;
//            tvScore.setText ( "Score: " +score );
        }
//        rb1.setTextColor ( Color.RED );
//        rb2.setTextColor ( Color.RED );
//        rb3.setTextColor ( Color.RED );
//        rb4.setTextColor ( Color.RED );
//        rb5.setTextColor ( Color.RED );
//
//        switch (currentQuestion.getCorrectAnsno ()){
//            case 1:
//                rb1.setTextColor ( Color.GREEN );
//                break;
//            case 2:
//                rb2.setTextColor ( Color.GREEN );
//                break;
//            case 3:
//                rb3.setTextColor ( Color.GREEN );
//                break;
//            case 4:
//                rb4.setTextColor ( Color.GREEN );
//                break;
//            case 5:
//                rb5.setTextColor ( Color.GREEN );
//                break;
//
//        }
        if(qCounter < totalQuestions){
//            btnNext.setText ( "Next" );


        }else{
            btnNext.setText ( "Finish" );

//            createPDF();
//            finish();
        }

    }

    private void showNextQuestion() {

        radioGroup.clearCheck ();
        rb1.setTextColor ( dfRbColor );
        rb2.setTextColor ( dfRbColor );
        rb3.setTextColor ( dfRbColor );
        rb4.setTextColor ( dfRbColor );
        rb5.setTextColor ( dfRbColor );


        if(qCounter < totalQuestions){
            currentQuestion = questionslist.get ( qCounter );
            tvQuestion.setText ( currentQuestion.getQuestions () );
            rb1.setText(currentQuestion.getC1 ());
            rb2.setText(currentQuestion.getC2 ());
            rb3.setText(currentQuestion.getC3 ());
            rb4.setText(currentQuestion.getC4 ());
            rb5.setText(currentQuestion.getC5 ());
            System.out.println ("image"+mImageView);

            qCounter++;
//            btnNext.setText ( "Next" );
            comment.setText("");

            //------------CONTINUE HERE---FOR CLEARING IMAGE


//            tvQuestionNo.setText ( "Question: "+qCounter+"/"+totalQuestions );
            answered = false;
        }else{

            //FINISH() MAKES APP CLOSE ENTIRELY
//            finish ();
            //CALL SAVE TO PDF METHOD HERE

            String name = getIntent ().getStringExtra ( "name" );
            String email = getIntent ().getStringExtra ( "emaillogin" );
            String password = getIntent ().getStringExtra ( "passwordlogin" );
            String surname = getIntent ().getStringExtra ( "surname" );
            String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

            Intent intent = new Intent(RadioInspection.this, signature.class);
            overridePendingTransition ( 0,0 );

            intent.putExtra ( "emaillogin",email);
            intent.putExtra ( "passwordlogin", password);
            intent.putExtra ( "name", name );
            intent.putExtra ( "surname", surname );
            intent.putExtra ( "jobTitle", jobTitle );

            startActivity(intent);

            createPdf(rb1.getText().toString(), rb2.getText ().toString (), rb3.getText ().toString (), rb4.getText ().toString (), rb5.getText ().toString ());
        }


    }

    private void showPreviousQuestion() {

        radioGroup.clearCheck ();
        rb1.setTextColor ( dfRbColor );
        rb2.setTextColor ( dfRbColor );
        rb3.setTextColor ( dfRbColor );
        rb4.setTextColor ( dfRbColor );
        rb5.setTextColor ( dfRbColor );


        if(qCounter < totalQuestions){
            qCounter--;
            currentQuestion = questionslist.get ( qCounter );
            tvQuestion.setText ( currentQuestion.getQuestions () );
            rb1.setText(currentQuestion.getC1 ());
            rb2.setText(currentQuestion.getC2 ());
            rb3.setText(currentQuestion.getC3 ());
            rb4.setText(currentQuestion.getC4 ());
            rb5.setText(currentQuestion.getC5 ());



//            tvQuestionNo.setText ( "Question: "+qCounter+"/"+totalQuestions );
            answered = true;
        }else{
            finish ();
        }
    }

    private void addQuestions() {
        questionslist.add(new QuestionModel   ( "5.1 External Walls and Wall Finishes", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
        questionslist.add(new QuestionModel   ( "5.2 External Doors", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView) );
        questionslist.add(new QuestionModel   ( "5.3 External Windows ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1, mImageView) );
        questionslist.add(new QuestionModel   ( "5.4 External Floors and Finishes ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1, mImageView ) );
        questionslist.add(new QuestionModel   ( "5.5 External Ceiling and Ceiling Finishes", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView) );
//        questionslist.add(new QuestionModel   ( "5.6 Roofs ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.7 Internal Walls and Finishes ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.8 Internal Doors ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1 ,mImageView) );
//        questionslist.add(new QuestionModel   ( "5.9Internal Floors and Finishes ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1 ,mImageView) );
//        questionslist.add(new QuestionModel   ( "5.10 Internal Ceiling and Ceiling Finishes ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1 ,mImageView) );
//        questionslist.add(new QuestionModel   ( "5.11 Handwash Basin ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.12 Carpets ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView) );
//        questionslist.add(new QuestionModel   ( "5.13 Tiles: Floors ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.14 Tiles: Wall ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.15 Toilets ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.16 Gutters ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.17 Down Pipes  ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.18 Paint: Exterior  ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.19 Paint: Interior  ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.20 Taps  ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.21 Bath  ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.22 Stairs  ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.23 Water Storage Tanks   ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );
//        questionslist.add(new QuestionModel   ( "5.24 Geysers  ", "C1 (Very Poor)", "C2 (Poor)", "C3 (Fair)", "C4 (Good)", "C5 (Excellent)", 1,mImageView ) );


    }

    //FOR PDF

    private void createPdf(String rb1, String rb2, String rb3, String rb4, String rb5){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(0, 0, 0, paint);
        paint.setColor(Color.BLACK);


        canvas.drawText(rb1, 80, 50, paint);
        canvas.drawText(rb2, 80, 50, paint);
        canvas.drawText(rb3, 80, 50, paint);
        canvas.drawText(rb4, 80, 50, paint);
        canvas.drawText(rb5, 80, 50, paint);

        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(0, 0, 0, paint);
        document.finishPage(page);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Documents/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test-2.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }



}