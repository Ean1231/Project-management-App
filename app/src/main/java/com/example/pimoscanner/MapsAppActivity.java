package com.example.pimoscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pimoscanner.account.AccountManager;
import com.example.pimoscanner.basemaps.BasemapsDialogFragment.BasemapsDialogListener;
import com.example.pimoscanner.basemaps.BasemapsDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.example.pimoscanner.ContentBrowserFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsAppActivity extends AppCompatActivity  implements ActivityCompat.OnRequestPermissionsResultCallback{



    private static final int PERMISSION_REQUEST_LOCATION = 0;
    private static final int REQUEST_LOCATION_SETTINGS = 1;
    private static final int REQUEST_WIFI_SETTINGS = 2;
    private static final int REQUEST_ARCGIS_CRED = 3;
    private static final String TAG = MapsAppActivity.class.getSimpleName();
    public  DrawerLayout mDrawerLayout;
    private final List<DrawerItem> mDrawerItems = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    /**
     * The list of menu items in the navigation drawer
     */
    @BindView(R.id.maps_app_activity_left_drawer) ListView mDrawerList;
    private View mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps_app);
        mLayout = findViewById(R.id.maps_app_activity_content_frame);

        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId ( R.id.projects);


        bottomNavigationView = findViewById ( R.id.navigation );
        bottomNavigationView.setSelectedItemId ( R.id.projects );

        bottomNavigationView.setOnNavigationItemSelectedListener ( new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId ()){
                    case R.id.home:
                        String name = getIntent ().getStringExtra ( "name" );
                        String email = getIntent ().getStringExtra ( "emaillogin" );
                        String password = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent = new Intent(MapsAppActivity.this, splash.class);
                        overridePendingTransition ( 0,0 );

                        intent.putExtra ( "jobTitle", jobTitle );
                        intent.putExtra ( "emaillogin",email);
                        intent.putExtra ( "passwordlogin", password);
                        intent.putExtra ( "name", name );

                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.projects:

                        return true;

                    case R.id.camera:
                        String name1 = getIntent ().getStringExtra ( "name" );
                        String email1 = getIntent ().getStringExtra ( "emaillogin" );
                        String password1 = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle1 = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent1 = new Intent(MapsAppActivity.this, Camera.class);
                        overridePendingTransition ( 0,0 );

                        intent1.putExtra ( "jobTitle", jobTitle1 );
                        intent1.putExtra ( "emaillogin",email1);
                        intent1.putExtra ( "passwordlogin", password1);
                        intent1.putExtra ( "name", name1 );

                        startActivity(intent1);
                        finish();
                        return true;

                    case R.id.scan:
                        String name2 = getIntent ().getStringExtra ( "name" );
                        String email2 = getIntent ().getStringExtra ( "emaillogin" );
                        String password2 = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle2 = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent2 = new Intent(MapsAppActivity.this, MainActivity.class);
                        overridePendingTransition ( 0,0 );

                        intent2.putExtra ( "jobTitle", jobTitle2 );
                        intent2.putExtra ( "emaillogin",email2);
                        intent2.putExtra ( "passwordlogin", password2);
                        intent2.putExtra ( "name", name2 );
                        startActivity(intent2);
                        finish();
                        return true;

                    case R.id.work:
                        String name3 = getIntent ().getStringExtra ( "name" );
                        String email3 = getIntent ().getStringExtra ( "emaillogin" );
                        String password3 = getIntent ().getStringExtra ( "passwordlogin" );
                        String jobTitle3 = getIntent ().getStringExtra ( "jobTitle" );

                        Intent intent3 = new Intent(MapsAppActivity.this, Workorder.class);
                        overridePendingTransition ( 0,0 );

                        intent3.putExtra ( "jobTitle",jobTitle3);
                        intent3.putExtra ( "emaillogin",email3);
                        intent3.putExtra ( "passwordlogin", password3);
                        intent3.putExtra ( "name", name3 );
                        startActivity(intent3);
                        finish();
                        return true;

                }
                return false;
            }
        } );

        // We need a reference to the containing widget when
        // managing the Snackbar (used for notifying users about app
        // permissions)
        mLayout = findViewById(R.id.maps_app_activity_content_frame);
        mDrawerList = findViewById ( R.id.maps_app_activity_left_drawer );

        ButterKnife.bind(this);

        setupDrawer();

        // All devices running N and above require explicit permissions
        // checking when the app is first run.

        requestLocationPermission();
    }

    /*
     * Prompt user to turn on location tracking and wireless if needed
     */
    private void checkSettings() {
        // Is GPS enabled?
        boolean gpsEnabled = locationTrackingEnabled();
        // Is there internet connectivity?
        boolean internetConnected = internetConnectivity();

        if (gpsEnabled && internetConnected) {
            setView();
        }else if (!gpsEnabled) {
            Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            showDialog(gpsIntent, REQUEST_LOCATION_SETTINGS, getString(R.string.location_tracking_off));
        }else {
            Intent internetIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            showDialog(internetIntent, REQUEST_WIFI_SETTINGS, getString(R.string.wireless_off));
        }
    }

    /**
     * Prompt user to enable location tracking
     */
    private void showDialog(final Intent intent, final int requestCode, String message) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(getString(R.string.open_location_options), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                startActivityForResult(intent, requestCode);
            }
        });
        alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create().show();
    }

    /**
     * When returning from activities concerning airplane mode and location
     * settings, check them again.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WIFI_SETTINGS || requestCode == REQUEST_LOCATION_SETTINGS) {
            checkSettings();
        }else if (requestCode == REQUEST_ARCGIS_CRED){
            Log.i(TAG, "Browser returned...");
        }

    }



    /**
     * Requests the {@link Manifest.permission#ACCESS_COARSE_LOCATION}
     * permission. If an additional rationale should be displayed, the user has
     * to launch the request from a SnackBar that includes additional
     * information.
     */

    private void requestLocationPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Provide an additional rationale to the user if the permission was
            // not granted
            // and the user would benefit from additional context for the use of
            // the permission.
            // Display a SnackBar with a button to request the missing
            // permission.
            Snackbar.make(mLayout, "Location access is required to display the map.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request the permission
                            ActivityCompat.requestPermissions(MapsAppActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSION_REQUEST_LOCATION);
                        }
                    }).show();

        } else {
            // Request the permission. The result will be received in
            // onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        }
    }



    /**
     * Once the app has prompted for permission to access location, the response
     * from the user is handled here. If permission exists to access location
     * check if GPS is available and device is not in airplane mode.
     *
     * @param requestCode
     *            int: The request code passed into requestPermissions
     * @param permissions
     *            String: The requested permission(s).
     * @param grantResults
     *            int: The grant results for the permission(s). This will be
     *            either PERMISSION_GRANTED or PERMISSION_DENIED
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted, do we have the right phone
                // settings?
                // Check for GPS and wireless
                checkSettings();

            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Location permission request was denied.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getData()== null){
            updateDrawer();
        }

    }

    /**
     * Initializes the navigation drawer.
     */
    private void setupDrawer() {
        mDrawerLayout = findViewById(R.id.maps_app_activity_drawer_layout);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        updateDrawer();
    }

    private void edit(){

        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );


        Intent intent = new Intent(MapsAppActivity.this, Layers.class);
        overridePendingTransition ( 0,0 );

        intent.putExtra ( "jobTitle",jobTitle);
        intent.putExtra ( "emaillogin",email);
        intent.putExtra ( "passwordlogin", password);
        intent.putExtra ( "name", name );
        startActivity(intent);
    }

    private void setView() {
        if (AccountManager.getInstance().isSignedIn()) {
            // we are signed in to a portal - show the content browser to choose
            // a map
            showContentBrowser();
        } else {
            // show the default map
            showMap(null, null);
        }
    }

    /**
     * Opens the content browser that shows the user's maps.
     */
    private void showContentBrowser() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment browseFragment = fragmentManager.findFragmentByTag(ContentBrowserFragment.TAG);
        if (browseFragment == null) {
            browseFragment = new ContentBrowserFragment();
        }

        if (!browseFragment.isVisible()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.maps_app_activity_content_frame, browseFragment, ContentBrowserFragment.TAG);
            transaction.addToBackStack(null);
            transaction.commit();

            invalidateOptionsMenu(); // reload the options menu
        }

        mDrawerLayout.closeDrawers();
    }

    /**
     * Opens the map represented by the specified portal item or if null, opens
     * a default map.
     *
     * @param portalItemId
     *            - String representing a portal resource
     * @param basemapPortalItemId
     *            - String representing a basemap portal item
     */
    public void showMap(String portalItemId, String basemapPortalItemId) {
        // remove existing MapFragment explicitly, simply replacing it can cause
        // the app to freeze when switching basemaps
        FragmentTransaction transaction;
        FragmentManager fragmentManager = getFragmentManager();
        Fragment currentMapFragment = fragmentManager.findFragmentByTag(MapFragment.TAG);
        if (currentMapFragment != null) {
            transaction = fragmentManager.beginTransaction();
            transaction.remove(currentMapFragment);
            transaction.commit();
        }

        MapFragment mapFragment = MapFragment.newInstance(portalItemId, basemapPortalItemId);

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.maps_app_activity_content_frame, mapFragment, MapFragment.TAG);
        transaction.addToBackStack(null);
        transaction.commit();

        invalidateOptionsMenu(); // reload the options menu
    }

    private void showSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);

        mDrawerLayout.closeDrawers();
    }

    /**
     * Resets the app to "signed out" state.
     */
    private void signOut() {
        AccountManager.getInstance().setPortal(null);

        setView();

        updateDrawer();
        mDrawerLayout.closeDrawers();
    }

    /**
     * Updates the navigation drawer items.
     */
    private void updateDrawer() {
        mDrawerItems.clear();

        DrawerItem item;
        if (AccountManager.getInstance().isSignedIn()) {

            // user info
            LinearLayout userInfoView = (LinearLayout) getLayoutInflater().inflate(R.layout.drawer_item_user_layout,
                    null);
            TextView textView = userInfoView.findViewById(R.id.drawer_item_fullname_textview);
            textView.setText(AccountManager.getInstance().getPortalUser().getFullName());

            textView = userInfoView.findViewById(R.id.drawer_item_username_textview);
            textView.setText(AccountManager.getInstance().getPortalUser().getUsername());

            item = new DrawerItem(userInfoView, null);
            mDrawerItems.add(item);

            // Sign Out

            LinearLayout view_signOut = (LinearLayout) getLayoutInflater().inflate(R.layout.drawer_item_layout, null);
            TextView text_drawer_signOut = view_signOut.findViewById(R.id.drawer_item_textview);
            ImageView icon_drawer_signOut = view_signOut.findViewById(R.id.drawer_item_icon);

            text_drawer_signOut.setText(getString(R.string.sign_out));
            icon_drawer_signOut.setImageResource(R.drawable.ic_peronnew);
            item = new DrawerItem(view_signOut, new DrawerItem.OnClickListener() {

                @Override
                public void onClick() {
                    signOut();
                }
            });
            mDrawerItems.add(item);

            // My Maps

            LinearLayout view_myMaps = (LinearLayout) getLayoutInflater().inflate(R.layout.drawer_item_layout, null);
            TextView text_drawer_myMaps = view_myMaps.findViewById(R.id.drawer_item_textview);
            ImageView icon_drawer_myMaps = view_myMaps.findViewById(R.id.drawer_item_icon);

            text_drawer_myMaps.setText(getString(R.string.my_maps));
            icon_drawer_myMaps.setImageResource(R.drawable.ic_mapnew);
            item = new DrawerItem(view_myMaps, new DrawerItem.OnClickListener() {

                @Override
                public void onClick() {
                    showContentBrowser();
                }
            });
            mDrawerItems.add(item);

            //Edit
            LinearLayout view_edit = (LinearLayout) getLayoutInflater().inflate(R.layout.drawer_item_layout, null);
            TextView text_drawer_edit = view_edit.findViewById(R.id.drawer_item_textview);
            ImageView icon_drawer_edit = view_edit.findViewById(R.id.drawer_item_icon);

            text_drawer_edit.setText(getString(R.string.edit_mmap));
            icon_drawer_edit.setImageResource(R.drawable.ic_editnew);
            item = new DrawerItem(view_edit, new DrawerItem.OnClickListener() {
                @Override
                public void onClick() {
                    edit();
                }
            });
            mDrawerItems.add(item);
            //--
        } else {
                        // The Default when not signed in yet
            // Adding the Sign In item in the drawer
            LinearLayout view_signIn = (LinearLayout) getLayoutInflater().inflate(R.layout.drawer_item_layout, null);
            TextView text_drawer_signIn = view_signIn.findViewById(R.id.drawer_item_textview);
            ImageView icon_drawer_signIn = view_signIn.findViewById(R.id.drawer_item_icon);

            text_drawer_signIn.setText(getString(R.string.sign_in));
            icon_drawer_signIn.setImageResource(R.drawable.ic_profile);
            item = new DrawerItem(view_signIn, new DrawerItem.OnClickListener() {

                @Override
                public void onClick() {
                    showSignInActivity();
                }
            });
            mDrawerItems.add(item);
        }

        // Adding the basemap item in the drawer
        LinearLayout view_basemap = (LinearLayout) getLayoutInflater().inflate(R.layout.drawer_item_layout, null);
        TextView text_drawer_basemap = view_basemap.findViewById(R.id.drawer_item_textview);
        ImageView icon_drawer_basemap = view_basemap.findViewById(R.id.drawer_item_icon);
        text_drawer_basemap.setText(getString(R.string.menu_basemaps));
        icon_drawer_basemap.setImageResource(R.drawable.basemapzo);
        item = new DrawerItem(view_basemap, new DrawerItem.OnClickListener() {

            @Override
            public void onClick() {
                // Show BasemapsDialogFragment to offer a choice if basemaps.
                // This calls back to onBasemapChanged() if one is selected.
                BasemapsDialogFragment basemapsFrag = new BasemapsDialogFragment();
                basemapsFrag.setBasemapsDialogListener(new BasemapsDialogFragment.BasemapsDialogListener() {

                    @Override
                    public boolean onGetRoute(String startPoint, String endPoint) {
                        return false;
                    }

                    @Override
                    public void onBasemapChanged(String itemId) {
                        showMap(null, itemId);
                    }
                });
                basemapsFrag.show(getFragmentManager(), null);
                mDrawerLayout.closeDrawers();
            }

        });

        mDrawerItems.add(item);

        BaseAdapter adapter = (BaseAdapter) mDrawerList.getAdapter();
        if (adapter == null) {
            adapter = new DrawerItemListAdapter();
            mDrawerList.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    private boolean locationTrackingEnabled() {
        LocationManager locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean internetConnectivity(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getActiveNetworkInfo();
        if (wifi == null){
            return false;
        }else {
            return wifi.isConnected();
        }
    }

    /**
     * Handles selection of items in the navigation drawer.
     */
    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mDrawerItems.get(position).onClicked();
        }
    }

    /**
     * Populates the navigation drawer list with items.
     */
    private class DrawerItemListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDrawerItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mDrawerItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DrawerItem drawerItem = (DrawerItem) getItem(position);
            return drawerItem.getView();
        }
    }


    }
