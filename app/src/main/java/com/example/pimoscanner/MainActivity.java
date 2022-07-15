package com.example.pimoscanner;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private File filePath;
    //  -----------------SCANNER PAGE----------------------------SCANNER PAGE-----------------------SCANNER PAGE----------------SCANNER PAGE--------------
    ImageView scannedImageView;
    int count = 0;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );
//        Toast.makeText ( this, "Hi scan+ '"+email+"'", Toast.LENGTH_SHORT ).show ();


        //---------------------------------------------------------Menu Item-----------------------



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId ( R.id.scan);


        bottomNavigationView = findViewById ( R.id.navigation );
        bottomNavigationView.setSelectedItemId ( R.id.scan );

        bottomNavigationView.setOnNavigationItemSelectedListener ( new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId ()){
                    case R.id.home:
                        String name = getIntent ().getStringExtra ( "name" );
                        String email = getIntent ().getStringExtra ( "emaillogin" );
                        String password = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent = new Intent(MainActivity.this, splash.class);
                        overridePendingTransition ( 0,0 );

                        intent.putExtra ( "emaillogin",email);
                        intent.putExtra ( "passwordlogin", password);
                        intent.putExtra ( "name", name );
                        intent.putExtra ( "jobTitle", jobTitle );
                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.scan:

                        return true;

                    case R.id.camera:
                        String name1 = getIntent ().getStringExtra ( "name" );
                        String email1 = getIntent ().getStringExtra ( "emaillogin" );
                        String password1 = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle1 = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent1 = new Intent(MainActivity.this, Camera.class);
                        overridePendingTransition ( 0,0 );

                        intent1.putExtra ( "emaillogin",email1);
                        intent1.putExtra ( "passwordlogin", password1);
                        intent1.putExtra ( "name", name1 );
                        intent1.putExtra ( "jobTitle", jobTitle1 );

                        startActivity(intent1);
                        finish();
                        return true;

                    case R.id.projects:
                        String name2 = getIntent ().getStringExtra ( "name" );
                        String email2 = getIntent ().getStringExtra ( "emaillogin" );
                        String password2 = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle2 = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent2 = new Intent(MainActivity.this, MapsAppActivity.class);
                        overridePendingTransition ( 0,0 );

                        intent2.putExtra ( "emaillogin",email2);
                        intent2.putExtra ( "passwordlogin", password2);
                        intent2.putExtra ( "name", name2 );
                        intent2.putExtra ( "jobTitle", jobTitle2 );

                        startActivity(intent2);
                        finish();
                        return true;

                    case R.id.work:
                        String name3 = getIntent ().getStringExtra ( "name" );
                        String email3 = getIntent ().getStringExtra ( "emaillogin" );
                        String password3 = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle3 = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent3 = new Intent(MainActivity.this, Workorder.class);
                        overridePendingTransition ( 0,0 );

                        intent3.putExtra ( "emaillogin",email3);
                        intent3.putExtra ( "passwordlogin", password3);
                        intent3.putExtra ( "name", name3 );
                        intent3.putExtra ( "jobTitle", jobTitle3 );

                        startActivity(intent3);
                        finish();
                        return true;


                }
                return false;
            }
        } );
        //-----------------------------------------------------------------------------

        scannedImageView = findViewById(R.id.image_view);

        //Handling android.os.NetworkOnMainThreadException ==> for W/System.err: android.os.NetworkOnMainThreadException Error
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder ().permitAll ().build ();
            StrictMode.setThreadPolicy ( policy );
        }
    }

    public void openGallery(View v){
        int REQUEST_CODE = 99;
        int preference = ScanConstants.OPEN_MEDIA;
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void openScanner(View v){
        int REQUEST_CODE = 99;
        int preference = ScanConstants.OPEN_CAMERA;
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                scannedImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //---------------------Convert Image to PDF-----------------------//

            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(960,1280, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pi);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#FFFFFF"));
            canvas.drawPaint(paint);

//            if (bitmap != null) {
//                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),bitmap.getHeight(), true);
//            }
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),bitmap.getHeight(),true);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap,0,0,null);

            pdfDocument.finishPage(page);
        //Save Bitmap Image

            File root = new File(Environment.getExternalStorageDirectory(), "/Documents/");
            System.out.println ( "ROOT.........." + root );
            if (!root.exists()) {
                boolean mkdir = root.mkdir();
                if (!mkdir) {
                    Log.e(TAG, "Directory creation failed.");
                }
            }
            File file = new File(root, "picture.pdf");

            Toast.makeText(this, "Saved to Documents...", Toast.LENGTH_SHORT).show();
            try
             {
                 FileOutputStream fileOutputStream = new FileOutputStream(file);
                 pdfDocument.writeTo(fileOutputStream);
             }
             catch (IOException e)
             {
                 e.printStackTrace();
             }
            upload();
            pdfDocument.close();
        }

    }



    public static String getSharepointToken() throws IOException {
        /**
         * This function helps to get SharePoint Access Token. SharePoint Access
         * Token is required to authenticate SharePoint REST service while
         * performing Read/Write events. SharePoint REST-URL to get access token
         * is as: https://accounts.accesscontrol.windows.net/
         * /tokens/OAuth/2
         *
         * Input required related to SharePoint are as: 1. shp_clientId 2.
         * shp_tenantId 3. shp_clientSecret
         */
        String accessToken = "";
        try {

            // AccessToken url

            String wsURL = "https://accounts.accesscontrol.windows.net/4ab46260-cd22-4c90-b1c1-fcdc1f4c93e7/tokens/OAuth/2";

            URL url = new URL ( wsURL );
            URLConnection connection = url.openConnection ();
            HttpURLConnection httpConn = (HttpURLConnection) connection;

            // Set header
            httpConn.setRequestProperty ( "Content-Type", "application/x-www-form-urlencoded" );
            httpConn.setDoOutput ( true );
            httpConn.setDoInput ( true );
            httpConn.setRequestMethod ( "POST" );


            String jsonParam = "grant_type = client_credentials"
                    + "&client_id =fc69ca49-09f4-40c1-8d93-e7183c28542d@4ab46260-cd22-4c90-b1c1-fcdc1f4c93e7"
                    + "&client_secret =fYpHebffLhlIjnJN0S9mNMaqA6yXCpZX/X8TplHKz7c="
                    + "&resource = 00000003-0000-0ff1-ce00-000000000000/ayamahtibaa.sharepoint.com@4ab46260-cd22-4c90-b1c1-fcdc1f4c93e7";
//                    + "&scope=https://ayamahtibaa.sharepoint.com/Sites.FullControl.All"
//                    + "&response_type=code";

            // Send Request
            DataOutputStream wr = new DataOutputStream ( httpConn.getOutputStream () );
            wr.writeBytes ( jsonParam );
            wr.flush ();
            wr.close ();

            // Read the response.
            InputStreamReader isr = null;
            if (httpConn.getResponseCode () == 200) {
                isr = new InputStreamReader ( httpConn.getInputStream () );
            } else {
                isr = new InputStreamReader ( httpConn.getErrorStream () );
            }
            System.out.println ( "Response.........." + isr );
            BufferedReader in = new BufferedReader ( isr );
            String responseString = "";
            StringBuilder outputString = new StringBuilder ();

            // Write response to a String.
            while ((responseString = in.readLine ()) != null) {
                outputString.append ( responseString );
            }
            // Extracting accessToken from string, here response
            // (outputString)is a Json format string
            if (outputString.indexOf ( "access_token\":\"" ) > -1) {
                int i1 = outputString.indexOf ( "access_token\":\"" );
                String str1 = outputString.substring ( i1 + 15 );
                int i2 = str1.indexOf ( "\"}" );
                String str2 = str1.substring ( 0, i2 );
                accessToken = str2;
                System.out.println("accessTokenNNN.........." + accessToken);
            }
        } catch (Exception e) {
            accessToken = "Error: Token" + e.getMessage ();
        }
        return accessToken;

    }


    public void upload() {

        try {
            String accessTokenInt = getSharepointToken ();
            String siteURL = "https://ayamahtibaa.sharepoint.com/sites/ICTSharepoint";
            //System.out.println("accessToken........." + accessTokenInt);
            //e.g. ayamahtibaa.sharepoint.com/sites/mysite;

            String folderUrl = "Scanned%20Documents" ;

            //===============================================//
            //Sharepoint API to create Path
            //===============================================//

            String url1w = siteURL + "/_api/web/GetFolderByServerRelativeUrl('" + folderUrl + "')";

            System.out.println ( "The complete path is : " + url1w );
            URL urlcw = new URL ( url1w );
            URLConnection con1 = urlcw.openConnection ();
            HttpURLConnection httpCon1 = (HttpURLConnection) con1;
            httpCon1.setDoOutput ( true );
            httpCon1.setDoInput ( true );
            httpCon1.setRequestMethod ( "POST" );
            httpCon1.setRequestProperty ( "Authorization", "Bearer " + accessTokenInt );
            DataOutputStream wr1 = new DataOutputStream ( httpCon1.getOutputStream () );
            wr1.flush ();
            wr1.close ();

            //Read the response.
            String respStr1 = "";
            if (httpCon1.getResponseCode () == 200) {
                respStr1 = "Path has been found/created successfully. ResponseCode : " + httpCon1.getResponseCode ();
                System.out.println ( "Response.........." + respStr1 );
            } else {
                respStr1 += "Error while writing file, ResponseCode : " + httpCon1.getResponseCode () + " " + httpCon1.getResponseMessage ();
                System.out.println ( "Response.........." + respStr1 );
            }
            System.out.println ( respStr1 );

            //System.out.println("BBBaccessToken.........." + respStr1);

            //===============================================//
            //Sharepoint API to upload file to Path          //
            //===============================================//

            try {
                BitmapDrawable bitmapDrawable = (BitmapDrawable)scannedImageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                FileOutputStream outputStream = null;
                File filepath = Environment.getExternalStorageDirectory ();
                File dir = new File ( filepath.getAbsolutePath () + "/Pictures/" );
//                dir.mkdirs ();
//                File file = new File ( dir, System.currentTimeMillis () + ".jpg" );
                System.out.println("accessTokenNNN.........." + filepath);
                Toast.makeText ( this, "Image Saved to Gallery...", Toast.LENGTH_SHORT ).show ();
                System.out.println  ( "BITMAP.........." + scannedImageView);
                filePath = new File (dir, System.currentTimeMillis () + ".jpg" );

                try {
                    outputStream = new FileOutputStream ( filePath );
                } catch (FileNotFoundException e) {
                    e.printStackTrace ();
                }
                bitmap.compress ( Bitmap.CompressFormat.JPEG, 100, outputStream );
                try {
                    assert outputStream != null;
                    outputStream.flush ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
                try {
                    outputStream.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }


            } catch (Exception nre)
            {
                System.out.println  ("User Cancelled the operation");
            }
            String filename = filePath.getName ();
            filename = filename.replaceAll ( "/", "%20" );
            System.out.println ( filename + " Selected for upload " );
            String uploadlink = "";
            uploadlink = siteURL + "/_api/web/GetFolderByServerRelativeUrl('" + folderUrl + "/";
            uploadlink = uploadlink + "')/Files/add(url='" + filename + "',overwrite=true)";
            executeMultiPartRequest ( uploadlink, filePath );
            System.out.println ( "File uploaded : " + uploadlink );
        } catch (Exception e) {
            e.printStackTrace ();
            System.out.println ( "The file could not be uploaded OR Token error" );
        }
    }


    public static void executeMultiPartRequest(String urlString, File file) {
        HttpPost postRequest = new HttpPost ( urlString );
        postRequest = addHeader ( postRequest );
        try {

            System.out.println ( "urlString :" + urlString );
            postRequest.setEntity ( new FileEntity ( file, "pic.png" ) );
        } catch (Exception ex) {
            ex.printStackTrace ();
        }
        executeRequest ( postRequest );
    }

    private static HttpPost addHeader(HttpPost httpPost) {
        String accessTokenInt = null;
        try {
            accessTokenInt = getSharepointToken ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        httpPost.addHeader ( "Accept", "application/json;odata=verbose" );
        httpPost.setHeader ( "Authorization", "Bearer " + accessTokenInt );
//System.out.println("httpPost…….." + httpPost);
        return httpPost;
    }

    @SuppressWarnings({"resource", "deprecation"})
    private static void executeRequest(HttpPost httpPost) {
        try {
            HttpClient client = new DefaultHttpClient ();
            HttpResponse response = client.execute ( httpPost );
            System.out.println ( "Response Code: " + response.getStatusLine ().getStatusCode () );
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace ();
        }


    }
}


