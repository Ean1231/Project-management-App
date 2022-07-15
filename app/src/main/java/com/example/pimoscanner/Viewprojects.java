package com.example.pimoscanner;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.ProgressDialog;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.app.SearchManager;

import android.widget.SearchView.OnQueryTextListener;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.Job;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureCollectionTable;
import com.esri.arcgisruntime.data.FeatureSet;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.FeatureTableEditResult;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.data.ServiceGeodatabase;
import com.esri.arcgisruntime.data.StatisticDefinition;
import com.esri.arcgisruntime.data.StatisticRecord;
import com.esri.arcgisruntime.data.StatisticType;
import com.esri.arcgisruntime.data.StatisticsQueryParameters;
import com.esri.arcgisruntime.data.StatisticsQueryResult;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.Multipoint;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.ArcGISVectorTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.WrapAroundMode;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalFolder;
import com.esri.arcgisruntime.portal.PortalItem;
import com.esri.arcgisruntime.portal.PortalUserContent;
import com.esri.arcgisruntime.security.AuthenticationChallengeHandler;
import com.esri.arcgisruntime.security.AuthenticationManager;
import com.esri.arcgisruntime.security.DefaultAuthenticationChallengeHandler;
import com.esri.arcgisruntime.security.OAuthConfiguration;
import com.esri.arcgisruntime.symbology.FillSymbol;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import com.esri.arcgisruntime.tasks.geocode.SuggestParameters;
import com.esri.arcgisruntime.tasks.geocode.SuggestResult;
import com.esri.arcgisruntime.tasks.geoprocessing.GeoprocessingFeatures;
import com.esri.arcgisruntime.tasks.geoprocessing.GeoprocessingJob;
import com.esri.arcgisruntime.tasks.geoprocessing.GeoprocessingParameters;
import com.esri.arcgisruntime.tasks.geoprocessing.GeoprocessingResult;
import com.esri.arcgisruntime.tasks.geoprocessing.GeoprocessingTask;
import com.esri.arcgisruntime.tasks.networkanalysis.DirectionManeuver;
import com.esri.arcgisruntime.tasks.networkanalysis.Route;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Stop;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Viewprojects extends AppCompatActivity {

    private MapView mMapView;
    private static final String TAG = Viewprojects.class.getSimpleName();

//        ArcGISMap map = new ArcGISMap( Basemap.createOpenStreetMap ());
    private String API_KEY = "AAPK4de07549fc254dfe91a6d96bc6a0f714xzqxPhZJz0rQxZvrGaC8av0spQx0gBHi_fUbA7VRoCcqiPFSTJ_ETMcKBQiCL8Wb";


    private ArrayList permissionsToRequest;
    private final ArrayList permissionsRejected = new ArrayList ();
    private final ArrayList permissions = new ArrayList ();
    private MapView mapView;


    private static final int MIN_SCALE = 60000000;




    private ServiceFeatureTable mServiceFeatureTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView(R.layout.activity_viewprojects);
        // New API_KEY===> AAPK4de07549fc254dfe91a6d96bc6a0f714xzqxPhZJz0rQxZvrGaC8av0spQx0gBHi_fUbA7VRoCcqiPFSTJ_ETMcKBQiCL8Wb
        API_KEY = "AAPK4de07549fc254dfe91a6d96bc6a0f714xzqxPhZJz0rQxZvrGaC8av0spQx0gBHi_fUbA7VRoCcqiPFSTJ_ETMcKBQiCL8Wb";
        ArcGISRuntimeEnvironment.setApiKey(API_KEY);
        // get a reference to the map view
        mMapView = findViewById(R.id.mapView);
        //<iframe src="https://maamdtqlbu7yw5te.maps.arcgis.com/apps/instant/basic/index.html?appid=097a04adf817455aaa660562aef9d005" width="400" height="600" frameborder="0" style="border:0" allowfullscreen>iFrames are not supported on this page.</iframe>

        // create a map with streets basemap
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_STREETS_NIGHT);
        //-28.732588726407393, 24.75787963280968...-33.006369210133215, 27.896389957802807

        // create and load the service geodatabase
        ServiceGeodatabase serviceGeodatabase =  new ServiceGeodatabase(getString(R.string.service_layer_url));
        serviceGeodatabase.loadAsync();
        serviceGeodatabase.addDoneLoadingListener(() -> {
            // create a feature layer using the first layer in the ServiceFeatureTable
            mServiceFeatureTable = serviceGeodatabase.getTable(0);
            // create a feature layer from table
            FeatureLayer featureLayer = new FeatureLayer(mServiceFeatureTable);
            // add the layer to the map
            map.getOperationalLayers().add(featureLayer);
            // set map to be displayed in map view
            mMapView.setMap(map);
            mMapView.setViewpoint(new Viewpoint( -33.006369210133215, 27.896389957802807, 10000000.0));
        });
        // add a listener to the MapView to detect when a user has performed a single tap to add a new feature to
        // the service feature table
        mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mMapView) {
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