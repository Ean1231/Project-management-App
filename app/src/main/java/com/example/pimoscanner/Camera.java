package com.example.pimoscanner;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


public class Camera extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;


    private ArrayList permissionsToRequest;
    private final ArrayList permissionsRejected = new ArrayList ();
    private final ArrayList permissions = new ArrayList ();
    private final static int ALL_PERMISSIONS_RESULT = 101;


    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    static LocationTrack locationTrack;
    Button saveImage;
    Button mCaptureBtn;
    static ImageView mImageView;
    Uri image_uri;
    TextView txtLat;
    TextView txtLong;

    private File filePath;
    private Object RequestDigest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_camera );

        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

//        Toast.makeText ( this, "Hi Cam '"+email+"'", Toast.LENGTH_SHORT ).show ();


        //------------------------------------------Menu Item

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId ( R.id.camera);


        navigation.setOnNavigationItemSelectedListener ( new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId ()){
                    case R.id.home:
                        String name = getIntent ().getStringExtra ( "name" );
                        String email = getIntent ().getStringExtra ( "emaillogin" );
                        String password = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent = new Intent(Camera.this, splash.class);
                        overridePendingTransition ( 0,0 );

                        intent.putExtra ( "jobTitle",jobTitle);
                        intent.putExtra ( "emaillogin",email);
                        intent.putExtra ( "passwordlogin", password);
                        intent.putExtra ( "name", name );
                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.camera:

                        return true;

                    case R.id.scan:
                        String name1 = getIntent ().getStringExtra ( "name" );
                        String email1 = getIntent ().getStringExtra ( "emaillogin" );
                        String password1 = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle1 = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent1 = new Intent(Camera.this, MainActivity.class);
                        overridePendingTransition ( 0,0 );

                        intent1.putExtra ( "jobTitle",jobTitle1);
                        intent1.putExtra ( "emaillogin",email1);
                        intent1.putExtra ( "passwordlogin", password1);
                        intent1.putExtra ( "name", name1 );
                        startActivity(intent1);
                        finish();
                        return true;

                    case R.id.projects:
                        String name2 = getIntent ().getStringExtra ( "name" );
                        String email2 = getIntent ().getStringExtra ( "emaillogin" );
                        String password2 = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle2 = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent2 = new Intent(Camera.this, MapsAppActivity.class);
                        overridePendingTransition ( 0,0 );

                        intent2.putExtra ( "jobtitle",jobTitle2);
                        intent2.putExtra ( "emaillogin",email2);
                        intent2.putExtra ( "passwordlogin", password2);
                        intent2.putExtra ( "name", name2 );
                        startActivity(intent2);
                        finish();
                        return true;

                    case R.id.work:
                        String jobTitle3 = getIntent ().getStringExtra ( "jobTitle" );
                        String name3 = getIntent ().getStringExtra ( "name" );
                        String email3 = getIntent ().getStringExtra ( "emaillogin" );
                        String password3 = getIntent ().getStringExtra ( "passwordlogin" );

                        Intent intent3 = new Intent(Camera.this, Workorder.class);
                        overridePendingTransition ( 0,0 );

                        intent3.putExtra ( "emaillogin",email3);
                        intent3.putExtra ( "passwordlogin", password3);
                        intent3.putExtra ( "name", name3 );
                        intent3.putExtra ( "jobTitle",jobTitle3);
                        startActivity(intent3);
                        finish();
                        return true;

                }
                return true;
            }
        } );

        //-------------------------------------------------------------

        //Handling android.os.NetworkOnMainThreadException ==> for W/System.err: android.os.NetworkOnMainThreadException Error
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder ().permitAll ().build ();
            StrictMode.setThreadPolicy ( policy );
        }

        //Assign Variables
        mImageView = findViewById ( R.id.image_view );
        FloatingActionButton mCaptureBtn = findViewById ( R.id.capture_image_btn );
        FloatingActionButton  saveImage = findViewById ( R.id.saveImage );


        //Permissions
        ActivityCompat.requestPermissions ( Camera.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1 );
        ActivityCompat.requestPermissions ( Camera.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1 );

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
        Button btn = findViewById ( R.id.btn_location );
        //Handling android.os.NetworkOnMainThreadException ==> for W/System.err: android.os.NetworkOnMainThreadException Error
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder ().permitAll ().build ();
            StrictMode.setThreadPolicy ( policy );
        }


        locationTrack = new LocationTrack ( Camera.this );


        if (locationTrack.canGetLocation ()) {


            double longitude = locationTrack.getLongitude ();
            double latitude = locationTrack.getLatitude ();

//            Toast.makeText ( getApplicationContext (), "Longitude:" + longitude + "\nLatitude:" + latitude, Toast.LENGTH_SHORT ).show ();
            System.out.println ( "Location.........." + longitude + " " + latitude );

        } else {

            locationTrack.showSettingsAlert ();
        }
//--


        //Save image to gallery
        saveImage.setOnClickListener ( new View.OnClickListener () {
                                           @Override
                                           public void onClick(View view) {
                                               locationTrack = new LocationTrack ( Camera.this );


                                               if (locationTrack.canGetLocation ()) {


                             double longitude = locationTrack.getLongitude ();
                          double latitude = locationTrack.getLatitude ();
     
//                                   Toast.makeText ( getApplicationContext (), "Longitude:" + longitude + "\nLatitude:" + latitude, Toast.LENGTH_SHORT ).show ();
                                                   System.out.println ( "accessTokenNNN.........." + longitude + " " + latitude );

                                               } else {

                                                   locationTrack.showSettingsAlert ();
                                               }
//                                               upload ();
                                               saveToGallery ();
//                                               pushDataToSharepoint();
                                           }
            }
        );




        //Capture button click
        mCaptureBtn.setOnClickListener ( v -> {
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
    }

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

    @TargetApi(Build.VERSION_CODES.M)

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder ( Camera.this )
                .setMessage ( message )
                .setPositiveButton ( "OK", okListener )
                .setNegativeButton ( "Cancel", null )
                .create ()
                .show ();
    }


    Thread thread = new Thread ( new Runnable () {
        @Override
        public void run() {
            try {
                //Your code goes here
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
    } );

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


    //-------------------Save Image to Gallery-----------Save Image to Gallery---------------Save Image to Gallery-------------------Save Image to Gallery
    private void saveToGallery() {

        BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageView.getDrawable ();
        Bitmap bitmap = bitmapDrawable.getBitmap ();

        FileOutputStream outputStream = null;
        File filepath = Environment.getExternalStorageDirectory ();
        File dir = new File ( filepath.getAbsolutePath () + "/Pictures/" );
        dir.mkdirs ();
        File file = new File ( dir, System.currentTimeMillis () + ".jpg" );
        System.out.println("accessTokenNNN.........." + filepath);
        Toast.makeText ( this, "Image Saved to Gallery...", Toast.LENGTH_SHORT ).show ();

        String filename = String.format("%s.id.png", System.currentTimeMillis());
        File outFile = new File(dir, filename);

        try {
            outputStream = new FileOutputStream ( file );
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
//        upload ();

    }



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

//-------------------------------------------------------------Get access token
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
            }
        else {
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

//------------------------------------------------------------------------Upload to sharepoint method
    public void upload() {
        pushDataToSharepoint();

        try {
            String accessTokenInt = getSharepointToken ();
            String siteURL = "https://ayamahtibaa.sharepoint.com/sites/ICTSharepoint";
            //System.out.println("accessToken........." + accessTokenInt);
            //e.g. ayamahtibaa.sharepoint.com/sites/mysite;

            String folderUrl = "Scanned%20Documents";

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
            }
        else {
                respStr1 += "Error while writing file, ResponseCode : " + httpCon1.getResponseCode () + " " + httpCon1.getResponseMessage ();
                System.out.println ( "Response.........." + respStr1 );
            }
            System.out.println ( respStr1 );

            //System.out.println("BBBaccessToken.........." + respStr1);

            //===============================================//
            //Sharepoint API to upload file to Path          //
            //===============================================//

        try {
                double latitude = 0;
                double longitude = 0;
                String Location = null;
        if (locationTrack.canGetLocation ()) {

                 longitude = locationTrack.getLongitude ();
                 latitude = locationTrack.getLatitude ();
                 Location = longitude + " " + latitude;

                }

                BitmapDrawable bitmapDrawable = (BitmapDrawable)mImageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                FileOutputStream outputStream = null;
                File filepath = Environment.getExternalStorageDirectory ();
                File dir = new File ( filepath.getAbsolutePath () + "/Pictures/" );
                //ir.mkdirs ();
                File file = new File ( dir, System.currentTimeMillis () + ".jpg" );
                System.out.println("accessTokenNNN.........." + filepath);
                Toast.makeText ( this, "Image Saved to Gallery...", Toast.LENGTH_SHORT ).show ();
                System.out.println  ( "BITMAP.........." + mImageView);
                filePath = new File (dir, System.currentTimeMillis () + ".jpg" );
        try {
                outputStream = new FileOutputStream ( filePath );
                }
        catch (FileNotFoundException e) {
                 e.printStackTrace ();
                }
                bitmap.compress ( Bitmap.CompressFormat.JPEG, 100, outputStream );
        try {
                assert outputStream != null;
                outputStream.flush ();
                }
        catch (IOException e) {
                e.printStackTrace ();
                }
        try {
                outputStream.close ();
                }
        catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        catch (Exception nre)
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
            }
        catch (Exception e) {
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
            }
        catch (Exception ex) {
            ex.printStackTrace ();
            }
        executeRequest ( postRequest );
    }

    private static HttpPost addHeader(HttpPost httpPost) {
             String accessTokenInt = null;
        try {
            accessTokenInt = getSharepointToken ();
            }
        catch (IOException e) {
            e.printStackTrace ();
            }
             httpPost.addHeader ( "Accept", "application/json;odata=verbose" );
             httpPost.setHeader ( "Authorization", "Bearer " + accessTokenInt );
//          System.out.println("httpPost…….." + httpPost);
        return httpPost;
    }

    @SuppressWarnings({"resource", "deprecation"})
    private static void executeRequest(HttpPost httpPost) {
        try {
            HttpClient client = new DefaultHttpClient ();
            HttpResponse response = client.execute ( httpPost );
            System.out.println ( "Response Code is: " + response.getStatusLine ().getStatusCode () );
             }
        catch (IllegalStateException | IOException e) {
            e.printStackTrace ();
        }


    }
    //------------------push Location and image to sharepoint Method------------------------------------------------------

    public void pushDataToSharepoint() {
        try {
//            String folderUrl = "Progress tracker list";
//            String siteUrl = "https://ayamahtibaa.sharepoint.com/sites/ICTSharepoint";
            String api_page = "https://ayamahtibaa.sharepoint.com/sites/ICTSharepoint/_api/web/lists/GetByTitle('Employees')/Items";
            //                "https://ericsson.sharepoint.com/sites/gotobo/_api/web/lists/getbytitle('samplelist')/items";

            System.out.println ( "Site_url" + " " + api_page );

            JsonParser parser = new JsonParser ();
            String accessToken = getSharepointToken ();
            if (accessToken.isEmpty ()) {
                System.out.println ( "Not able to get the token, return" );
                return;
            }
            double latitude = 0;
            double longitude = 0;
            String Location = null;

            if (locationTrack.canGetLocation ()) {

                longitude = locationTrack.getLongitude ();
                latitude = locationTrack.getLatitude ();

                Location = longitude + " , " + latitude;

            }

            BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageView.getDrawable ();
            Bitmap bitmap = bitmapDrawable.getBitmap ();

            FileOutputStream outputStream = null;
            File filepath = Environment.getExternalStorageDirectory ();
            File dir = new File ( filepath.getAbsolutePath () + "/Pictures/" );
            //ir.mkdirs ();
//              File file = new File ( dir, System.currentTimeMillis () + ".jpg" );
            System.out.println ( "accessTokenNNN.........." + filepath );
            Toast.makeText ( this, "Image Saved to Gallery...", Toast.LENGTH_SHORT ).show ();
            System.out.println ( "BITMAP.........." + mImageView );
//            filePath = new File ( "" + Location );
            filePath = new File (dir, System.currentTimeMillis () + ".jpeg" );
            System.out.println ( "file Path.........." + filePath );

            String filename = filePath.getName ();
            filename = filename.replaceAll ( "/", "" );

            JSONObject JObj = new JSONObject ();
            JSONObject obj1 = new JSONObject ();

            obj1.put ( "type", "SP.List" );
            JObj.put ( "__metadata", obj1 );
            JObj.put ( "Title", "Image Details");
            JObj.put ( "location", "Ean");
//            JObj.put ( "Image", filename);
//            JObj.put ( "Location", Location);

            

            final String jsonString = JObj.toString();
            URL obj = new URL ( api_page );
            HttpsURLConnection postConnection = (HttpsURLConnection) obj.openConnection ();
            System.out.println("jsonString " + JObj);

            postConnection.setHostnameVerifier ( new HostnameVerifier () {
                //                @SuppressLint("BadHostnameVerifier")
                @Override
                public boolean verify(String arg0, SSLSession arg1) {

                    return true;
                }
            });

            postConnection.setRequestMethod   ("POST");
            postConnection.setRequestProperty ("Accept", "application/json; odata=verbose");
            postConnection.setRequestProperty ("Content-Type", "application/json; odata=verbose");
            postConnection.setRequestProperty ("Authorization", "Bearer " + accessToken);
            postConnection.setRequestProperty ("X-RequestDigest", "form_digest_value" );
            postConnection.setConnectTimeout(20000);
            postConnection.setDoOutput(true);
            OutputStream os = postConnection.getOutputStream();
            os.write(jsonString.getBytes( ));
            os.flush();
            os.close();
            int responseCode = postConnection.getResponseCode ();
            System.out.println ( "POST request on sharepoint response code is" + postConnection.getResponseCode () + " " + postConnection.getResponseMessage () );
            System.out.println("PostConnection "+postConnection);

            if ((responseCode) == 200 || (responseCode == 201)) {
                BufferedReader in = new BufferedReader ( new InputStreamReader ( postConnection.getInputStream () ) );
                String inputLine;
                StringBuffer responseString = new StringBuffer();

                while ((inputLine = in.readLine ()) != null) {
                    responseString.append ( inputLine );
                }
                System.out.println ( "ResponseString "+responseString );

                JSONObject jsonObj = XML.toJSONObject ( responseString.toString () );
                //System.out.println(jsonObj.toString());
                //JsonParser parser1 = new JsonParser();
                // JsonObject jsonObj = parser.parse(responseString.toString()).getAsJsonObject();
                JSONObject entry = (JSONObject) jsonObj.get ( "entry" );
                JSONObject content = (JSONObject) entry.get ( "content" );
                JSONObject content1 = (JSONObject) content.get ( "m:properties" );

                ((JSONObject) content1.get ( "d:ID" )).get ( "content" );
            } else
                System.out.println ( "POST FAILED" );

        } catch (Exception e) {
            e.printStackTrace ();
            System.out.println ( "Exception while pushing data to Sharepoint" + e.getMessage () );
        }
    }
}

