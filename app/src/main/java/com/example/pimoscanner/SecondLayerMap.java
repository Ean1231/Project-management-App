package com.example.pimoscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTableEditResult;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.data.ServiceGeodatabase;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SecondLayerMap extends AppCompatActivity {
    private ServiceFeatureTable mServiceFeatureTable;
    private String API_KEY = "AAPK4de07549fc254dfe91a6d96bc6a0f714xzqxPhZJz0rQxZvrGaC8av0spQx0gBHi_fUbA7VRoCcqiPFSTJ_ETMcKBQiCL8Wb";
    private final String TAG = MainActivity.class.getSimpleName();
    BottomNavigationView bottomNavigationView;
    //    private Button fab1;
    private MapView mMapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_second_layer_map );

        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

//        Toast.makeText ( this, "Hi projec+ '"+email+"'", Toast.LENGTH_SHORT ).show ();






//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setSelectedItemId ( R.id.projects);

//        fab1 = (Button) findViewById ( R.id.fab2 );
        FloatingActionButton fab2 = findViewById ( R.id.fab2);
//        bottomNavigationView = findViewById ( R.id.navigation );
//        bottomNavigationView.setSelectedItemId ( R.id.projects );

        fab2.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondLayerMap.this, Layers.class);
                overridePendingTransition ( 0,0 );

                intent.putExtra ( "jobTitle",jobTitle);
                intent.putExtra ( "emaillogin",email);
                intent.putExtra ( "passwordlogin", password);
                intent.putExtra ( "name", name );
                startActivity(intent);
            }
        } );


        // create MapView from layout
//        mMapView = findViewById(R.id.mapView);
//        // create a map with the Basemap Type topographic
//        ArcGISMap map = new ArcGISMap( Basemap.createOpenStreetMap ());
//        mMapView.setMap(map);
//        -33.006369210133215, 27.896389957802807
        API_KEY = "AAPK4de07549fc254dfe91a6d96bc6a0f714xzqxPhZJz0rQxZvrGaC8av0spQx0gBHi_fUbA7VRoCcqiPFSTJ_ETMcKBQiCL8Wb";
        ArcGISRuntimeEnvironment.setApiKey(API_KEY);
        // get a reference to the map view
        mMapView = findViewById(R.id.mapView);
        //<iframe src="https://maamdtqlbu7yw5te.maps.arcgis.com/apps/instant/basic/index.html?appid=097a04adf817455aaa660562aef9d005" width="400" height="600" frameborder="0" style="border:0" allowfullscreen>iFrames are not supported on this page.</iframe>

        // create a map with streets basemap
        ArcGISMap map = new ArcGISMap( BasemapStyle.ARCGIS_STREETS);
        //-28.732588726407393, 24.75787963280968...-33.006369210133215, 27.896389957802807

        // create and load the service geodatabase


        ServiceGeodatabase serviceGeodatabase =  new ServiceGeodatabase(getString(R.string.el_police_stn));
        serviceGeodatabase.loadAsync ();

        serviceGeodatabase.getPortalItem ();
        serviceGeodatabase.addDoneLoadingListener(() -> {
            // create a feature layer using the first layer in the ServiceFeatureTable
            mServiceFeatureTable = serviceGeodatabase.getTable(0);
            // create a feature layer from table
            FeatureLayer featureLayer = new FeatureLayer(mServiceFeatureTable);
            // add the layer to the map
            map.getOperationalLayers().add(featureLayer);
            // set map to be displayed in map view
            mMapView.setMap(map);
            mMapView.setViewpoint(new Viewpoint ( -33.006369210133215, 27.896389957802807, 10000000.0));
        });
        // add a listener to the MapView to detect when a user has performed a single tap to add a new feature to
        // the service feature table
        mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener (this, mMapView) {

            @Override public boolean onSingleTapConfirmed(MotionEvent event) {
                // create a point from where the user clicked
                android.graphics.Point point = new android.graphics.Point((int) event.getX(), (int) event.getY());

                // create a map point from a point
                Point mapPoint = mMapView.screenToLocation(point);

                // add a new feature to the service feature table
                addFeature(mapPoint, mServiceFeatureTable);
                return super.onSingleTapConfirmed(event);
            }
        });
    }


    /**
     * Adds a new Feature to a ServiceFeatureTable and applies the changes to the
     * server.
     *
     * @param mapPoint     location to add feature
     * @param featureTable service feature table to add feature
     */
    private void addFeature(Point mapPoint, final ServiceFeatureTable featureTable) {

        // create default attributes for the feature
        Map<String, Object> attributes = new HashMap<> ();
        attributes.put("typdamage", "Destroyed");
        attributes.put("primcause", "Earthquake");

        // creates a new feature using default attributes and point
        Feature feature = featureTable.createFeature(attributes, mapPoint);

        // check if feature can be added to feature table
        if (featureTable.canAdd()) {
            // add the new feature to the feature table and to server
            featureTable.addFeatureAsync(feature).addDoneListener(() -> applyEdits(featureTable));
        } else {
            runOnUiThread(() -> logToUser(true, getString(R.string.error_cannot_add_to_feature_table)));
        }
    }



    /**
     * Sends any edits on the ServiceFeatureTable to the server.
     *
     * @param featureTable service feature table
     *
     *
     */
    private void applyEdits(ServiceFeatureTable featureTable) {

        // apply the changes to the server
        final ListenableFuture<List<FeatureTableEditResult>> editResult = featureTable.getServiceGeodatabase().applyEditsAsync();
        editResult.addDoneListener(() -> {
            try {
                List<FeatureTableEditResult> editResults = editResult.get();
                // check if the server edit was successful
                if (editResults != null && !editResults.isEmpty()) {
                    runOnUiThread(() -> logToUser(false, getString(R.string.feature_added)));
                }
            } catch (InterruptedException | ExecutionException e) {
                runOnUiThread(() -> logToUser(true, getString(R.string.error_applying_edits, e.getCause().getMessage())));
            }
        });
    }

    /**
     * Shows a Toast to user and logs to logcat.
     *
     * @param isError whether message is an error. Determines log level.
     * @param message message to display
     */
    private void logToUser(boolean isError, String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if (isError) {
            Log.e(TAG, message);
        } else {
            Log.d(TAG, message);
        }
    }

    @Override protected void onResume() {
        super.onResume();
        mMapView.resume();
    }

    @Override protected void onPause() {
        mMapView.pause();
        super.onPause();
    }

    @Override protected void onDestroy() {
        mMapView.dispose();
        super.onDestroy();
    }
}