package com.example.pimoscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.XML;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


public class coordinates extends AppCompatActivity {
    private Button button;
    private ArrayList permissionsToRequest;
    private final ArrayList permissionsRejected = new ArrayList ();
    private final ArrayList permissions = new ArrayList ();
    private MapView mapView;
    private LocationDisplay mLocationDisplay;
    private File filePath;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    TextView txtLat;
    TextView txtLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_coordinates );
        txtLat = findViewById ( R.id.tv_latitude );
        txtLong = findViewById ( R.id.tv_longitude );

        mapView = findViewById ( R.id.mapView );
//        ArcGISMap map = new ArcGISMap ();
//        map.setBasemap ( Basemap.createOpenStreetMap () );

//        mapView.setMap ( map );
//        setupLocationDisplay ();

        permissions.add ( ACCESS_FINE_LOCATION );
        permissions.add ( ACCESS_COARSE_LOCATION );


        // authentication with an API key or named user is required to access basemaps and other
        // location services
//        ArcGISRuntimeEnvironment.setApiKey(BuildConfig.API_KEY);

        // inflate MapView from layout
        mapView = findViewById(R.id.mapView);
        // create a map with a web mercator basemap
        ArcGISMap map = new ArcGISMap(SpatialReferences.getWebMercator());
        map.setBasemap(new Basemap(BasemapStyle.ARCGIS_CHARTED_TERRITORY));

        // set the map to be displayed in this view
        mapView.setMap(map);

        // zoom to Minneapolis
        Geometry startingEnvelope = new Envelope(-10995912.335747, 5267868.874421, -9880363.974046, 5960699.183877,
                SpatialReferences.getWebMercator());
        mapView.setViewpointGeometryAsync(startingEnvelope);

        // create graphics to show the input location
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        // create a red marker symbol for the input point
        final SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFFFF0000, 5);
        final Graphic inputPointGraphic = new Graphic();
        inputPointGraphic.setSymbol(markerSymbol);
        graphicsOverlay.getGraphics().add(inputPointGraphic);

        final DecimalFormat decimalFormat = new DecimalFormat("#.00000");




        //---------------------------------------------------------------------
        permissionsToRequest = findUnAskedPermissions ( permissions );

        Point oldFaithfullPoint = new Point (-32.986380328037285, 27.899668148654577, SpatialReferences.getWgs84());
        Point cascadeGeyserPoint = new Point(-28.731758850558105, 24.76006714445476, SpatialReferences.getWgs84());
        Point plumeGeyserPoint = new Point(-33.8091170458541, 25.594799646993557, SpatialReferences.getWgs84());

        Envelope initialEnvelope = new Envelope (oldFaithfullPoint, plumeGeyserPoint);

         map = new ArcGISMap ( Basemap.createOpenStreetMap () );
        mapView.setMap(map);
        mapView.setViewpointGeometryAsync(initialEnvelope, 9);
        GraphicsOverlay graphicOverlay = new GraphicsOverlay ();
        mapView.getGraphicsOverlays().add(graphicOverlay);

        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol( SimpleMarkerSymbol.Style.CROSS, Color.RED,
                12); //size 12, style of cross
        SimpleRenderer renderer = new SimpleRenderer (symbol);

        graphicOverlay.setRenderer(renderer);

        Graphic oldFaithfullGraphic = new Graphic(oldFaithfullPoint);
        Graphic cascadeGeyserGraphic = new Graphic(cascadeGeyserPoint);
        Graphic plumeGeyserGraphic = new Graphic (plumeGeyserPoint);
        graphicOverlay.getGraphics().add(oldFaithfullGraphic);
        graphicOverlay.getGraphics().add(cascadeGeyserGraphic);
        graphicOverlay.getGraphics().add(plumeGeyserGraphic);
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


        locationTrack = new LocationTrack ( coordinates.this );


        if (locationTrack.canGetLocation ()) {


            double longitude = locationTrack.getLongitude ();
            double latitude = locationTrack.getLatitude ();

            Toast.makeText ( getApplicationContext (), "Longitude:" + longitude + "\nLatitude:" + latitude, Toast.LENGTH_SHORT ).show ();
            System.out.println ( "Location.........." + longitude + " " + latitude );
            txtLat.setText ( "" + latitude );

            txtLong.setText ( " " + longitude );
        } else {

            locationTrack.showSettingsAlert ();
        }


        btn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                locationTrack = new LocationTrack ( coordinates.this );


                if (locationTrack.canGetLocation ()) {


                    double longitude = locationTrack.getLongitude ();
                    double latitude = locationTrack.getLatitude ();

                    Toast.makeText ( getApplicationContext (), "Longitude:" + longitude + "\nLatitude:" + latitude, Toast.LENGTH_SHORT ).show ();
                    System.out.println ( "accessTokenNNN.........." + longitude + " " + latitude );
                    txtLat.setText ( "" + latitude );

                    txtLong.setText ( " " + longitude );
                } else {

                    locationTrack.showSettingsAlert ();
                }
//                upload();
                pushDataToSharepoint();
            }
        } );

        button = (Button) findViewById(R.id.btn_location);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActive ();
            }
        });
    }


    //-------------

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
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//
//        super.onRequestPermissionsResult ( requestCode, permissions, grantResults );
//        switch (requestCode) {
//
//            case ALL_PERMISSIONS_RESULT:
//                for (Object perms : permissionsToRequest) {
//                    if (!hasPermission ( (String) perms )) {
//                        permissionsRejected.add ( perms );
//                    }
//                }
//
//                if (permissionsRejected.size () > 0) {
//
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (shouldShowRequestPermissionRationale ( (String) permissionsRejected.get ( 0 ) )) {
//                            showMessageOKCancel ( "These permissions are mandatory for the application. Please allow access.",
//                                    new DialogInterface.OnClickListener () {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                requestPermissions ( (String[]) permissionsRejected.toArray ( new String[permissionsRejected.size ()] ), ALL_PERMISSIONS_RESULT );
//                                            }
//                                        }
//                                    } );
//                            return;
//                        }
//                    }
//
//                }
//
//                break;
//        }
//
//    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder ( coordinates.this )
                .setMessage ( message )
                .setPositiveButton ( "OK", okListener )
                .setNegativeButton ( "Cancel", null )
                .create ()
                .show ();
    }


    private void setupLocationDisplay() {
        //----------------------------FOR COORDINATES-------------------------------------
//        locationTrack = new LocationTrack ( coordinates.this );
//
//
//        if (locationTrack.canGetLocation ()) {
//
//
//            double longitude = locationTrack.getLongitude ();
//            double latitude = locationTrack.getLatitude ();
//
//            Toast.makeText ( getApplicationContext (), "Longitude:" + Double.toString ( longitude ) + "\nLatitude:" + Double.toString ( latitude ), Toast.LENGTH_SHORT ).show ();
//            System.out.println("accessTokenNNN.........." + longitude+" "+latitude);
//            txtLat.setText("" +latitude );
//
//            txtLong.setText(" " + longitude );
//        } else {
//
//            locationTrack.showSettingsAlert ();
//        }
//
//---------------------------------------------------------------------------------------------------------------------------


        //  -------------------------------------------------------------------------ArcGis Map

//        mLocationDisplay = mapView.getLocationDisplay ();
//
//        mLocationDisplay.addDataSourceStatusChangedListener ( dataSourceStatusChangedEvent -> {
//            if (dataSourceStatusChangedEvent.isStarted () || dataSourceStatusChangedEvent.getError () == null) {
//                return;
//            }
//            int requestPermissionsCode = 2;
//            String[] requestPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//
//
//            if (!(ContextCompat.checkSelfPermission ( coordinates.this, requestPermissions[0] ) == PackageManager.PERMISSION_GRANTED
//                    && ContextCompat.checkSelfPermission ( coordinates.this, requestPermissions[1] ) == PackageManager.PERMISSION_GRANTED)) {
//                ActivityCompat.requestPermissions ( coordinates.this, requestPermissions, requestPermissionsCode );
//            } else {
//                String message = String.format ( "Error in DataSourceStatusChanged: %s",
//                        dataSourceStatusChangedEvent.getSource ().getLocationDataSource ().getError ().getMessage () );
//                Toast.makeText ( coordinates.this, message, Toast.LENGTH_LONG ).show ();
//            }
//        } );
//        mLocationDisplay.setAutoPanMode ( LocationDisplay.AutoPanMode.COMPASS_NAVIGATION );
//        mLocationDisplay.startAsync ();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        //--------------------------------------------------------------------------------------------------------------------
//        super.onRequestPermissionsResult ( requestCode, permissions, grantResults );
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            mLocationDisplay.startAsync ();
//        } else {
//            Toast.makeText ( coordinates.this, getResources ().getString ( R.string.location_permission_denied ), Toast.LENGTH_SHORT ).show ();
//        }
//
//        switch (requestCode) {
//
//            case ALL_PERMISSIONS_RESULT:
//                for (Object perms : permissionsToRequest) {
//                    if (!hasPermission ( (String) perms )) {
//                        permissionsRejected.add ( perms );
//                    }
//                }
//
//                if (permissionsRejected.size () > 0) {
//
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (shouldShowRequestPermissionRationale ( (String) permissionsRejected.get ( 0 ) )) {
//                            showMessageOKCancel ( "These permissions are mandatory for the application. Please allow access.",
//                                    new DialogInterface.OnClickListener () {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                requestPermissions ( (String[]) permissionsRejected.toArray ( new String[permissionsRejected.size ()] ), ALL_PERMISSIONS_RESULT );
//                                            }
//                                        }
//                                    } );
//                            return;
//                        }
//                    }
//
//                }
//
//                break;
//        }
//
//
//    }


    @Override
    protected void onPause() {
        mapView.pause ();
        super.onPause ();
    }

    @Override
    protected void onResume() {
        super.onResume ();
        mapView.resume ();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        mapView.dispose ();
        locationTrack.stopListener ();//
    }



    public static void pushDataToSharepoint() {
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



            JSONObject JObj = new JSONObject ();
            JSONObject obj1 = new JSONObject ();
            obj1.put ( "type", "SP.List" );
            JObj.put ( "__metadata", obj1 );
            JObj.put ( "Title", "Ean" );
            final String jsonString = JObj.toString ();
            URL obj = new URL ( api_page );
            HttpsURLConnection postConnection = (HttpsURLConnection) obj.openConnection ();
                        System.out.println("jsonString "+jsonString);

            postConnection.setHostnameVerifier ( new HostnameVerifier () {
//                @SuppressLint("BadHostnameVerifier")
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            } );
            postConnection.setRequestMethod("POST");
            postConnection.setRequestProperty("Accept", "'application/json; odata=verbose");
            postConnection.setRequestProperty("Content-Type", "application/json; odata=verbose");
            postConnection.setRequestProperty  ("Authorization", "Bearer "+accessToken);
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
                StringBuffer responseString = new StringBuffer ();

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


//--------------------------------------------------------------------------------------------------------------------
//public void upload() {
//
//    try {
//        String accessTokenInt = getSharepointToken();
//        String siteURL = "https://ayamahtibaa.sharepoint.com/sites/ICTSharepoint";
//        //e.g. gotobo.sharepoint.com/sites/mysite
//        String folderUrl = "Scanned%20Documents";
//
//        //===============================================//
//        //Sharepoint API to create Path
//        //===============================================//
//        String url1w = siteURL + "/_api/web/GetFolderByServerRelativeUrl('" + folderUrl + "')";
//        System.out.println("The complete path is : " + url1w);
//        URL urlcw = new URL(url1w);
//        URLConnection con1 = urlcw.openConnection();
//        HttpURLConnection httpCon1 = (HttpURLConnection) con1;
//        httpCon1.setDoOutput(true);
//        httpCon1.setDoInput(true);
//        httpCon1.setRequestMethod("POST");
//        httpCon1.setRequestProperty("Authorization", "Bearer " + accessTokenInt);
//        DataOutputStream wr1 = new DataOutputStream(httpCon1.getOutputStream());
//        wr1.flush();
//        wr1.close();
//
//        // Read the response.
//        String respStr1 = "";
//        if (httpCon1.getResponseCode() == 200) {
//            respStr1 = "Path has been found/created successfully. ResponseCode : " + httpCon1.getResponseCode();
//        } else {
//            respStr1 += "Error while writing file, ResponseCode : " + httpCon1.getResponseCode() + " "+ httpCon1.getResponseMessage();
//        }
//            System.out.println(respStr1);
//        //===============================================//
//        //Sharepoint API to upload file to Path
//        //===============================================//
//
//        try {
//            double latitude = 0;
//            double longitude = 0;
//            String Location = null;
//            if (locationTrack.canGetLocation ()) {
//
//                longitude = locationTrack.getLongitude ();
//                latitude = locationTrack.getLatitude ();
//
//                Location = longitude + " " + latitude;
//
//                Toast.makeText ( getApplicationContext (), "Longitude:" + Double.toString ( longitude ) + "\nLatitude:" + Double.toString ( latitude ), Toast.LENGTH_SHORT ).show ();
//                System.out.println ( "Location.........." + Location );
//                txtLat.setText ( "" + latitude );
//
//                txtLong.setText ( " " + longitude );
////                    filePath = new File ( String.valueOf ( Location ) ); //Made adjustments
//            } else {
//
//                locationTrack.showSettingsAlert ();
//
//            }
//
//            filePath = new File ( String.valueOf ( longitude ) );
//        }catch(Exception nre)
//        {
//            System.out.println("User Cancelled the operation");
//        }
//        String name = "Ean";
//        String filename = name;
////        filename = filename.replaceAll("/", "%20");
//        System.out.println(filename + " Selected for upload ");
//        String uploadlink = "";
//        uploadlink=siteURL+"/_api/web/GetFolderByServerRelativeUrl(\'"+ folderUrl+"/";
//        uploadlink = uploadlink+"\')/Files/Location/add(url='coordinates',overwrite=true)";
//        executeMultiPartRequest(uploadlink);
//        System.out.println("File uploaded : "+uploadlink);
//    } catch (Exception e) {
//        e.printStackTrace();
//        System.out.println("The file could not be uploaded OR Token error");
//    }
//}
//
//
//    public static void executeMultiPartRequest(String urlString) {
//        HttpPost postRequest = new HttpPost ( urlString );
//        postRequest = addHeader ( postRequest );
//        try {
//            System.out.println ( "urlString :" + urlString );
////            postRequest.setEntity ( new FileEntity ( file, "pic.png" ) );
//        } catch (Exception ex) {
//            ex.printStackTrace ();
//        }
//        executeRequest ( postRequest );
//    }
//
//    private static HttpPost addHeader(HttpPost httpPost) {
//        String accessTokenInt = null;
//        try {
//            accessTokenInt = getSharepointToken ();
//        } catch (IOException e) {
//            e.printStackTrace ();
//        }
//        httpPost.addHeader ( "Accept", "application/json;odata=verbose" );
//        httpPost.setHeader ( "Authorization", "Bearer " + accessTokenInt );
//        //System.out.println("httpPost…….." + httpPost);
//        return httpPost;
//    }
//
//    @SuppressWarnings({"resource", "deprecation"})
//    private static void executeRequest(HttpPost httpPost) {
//        try {
//            HttpClient client = new DefaultHttpClient ();
//            HttpResponse response = client.execute ( httpPost );
//            System.out.println ( "Response Code: " + response.getStatusLine ().getStatusCode () );
//        } catch (IllegalStateException | IOException e) {
//            e.printStackTrace ();
//        }
//    }
//
    //--------------------------------------------------------------------------------------------------------------------------------
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
                System.out.println ( "accessTokenNNN.........." + accessToken );
            }
        } catch (Exception e) {
            accessToken = "Error: Token" + e.getMessage ();
        }
        return accessToken;
    }

    public void openActive(){
        Intent intent = new Intent(this, ActiveProjects.class);
        startActivity(intent);
    }


}
