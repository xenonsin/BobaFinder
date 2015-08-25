package com.boba.keno.bobafinder;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.boba.keno.bobafinder.models.Business;
import com.boba.keno.bobafinder.models.BusinessRecyclerViewAdapter;
import com.boba.keno.bobafinder.models.SortByDistance;
import com.boba.keno.bobafinder.models.SortByRating;
import com.boba.keno.bobafinder.models.SortByReview;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class FindBobaActivity extends AppCompatActivity implements ConnectionCallbacks,
        OnConnectionFailedListener{

    // Google client to interact with Google API
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    double latitude;
    double longitude;
    ArrayList<Business> alBusinesses;
    ArrayList<Business> alBusinessRating;
    ArrayList<Business> alBusinessReview;
    RecyclerView rvBusinessesDistance;
    RecyclerView rvBusinessesRating;
    RecyclerView rvBusinessesReview;

    BusinessRecyclerViewAdapter adapterDistance;
    BusinessRecyclerViewAdapter adapterRating;
    BusinessRecyclerViewAdapter adapterReview;

    SwipeRefreshLayout distanceSwipeContainer;
    SwipeRefreshLayout ratingSwipeContainer;
    SwipeRefreshLayout reviewSwipeContainer;

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("HELP", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        searchLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_boba);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        distanceSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.scDistance);
        distanceSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchLocation();
            }
        });
        distanceSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        ratingSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.scRating);
        ratingSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchLocation();
            }
        });
        ratingSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        reviewSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.scReviews);
        reviewSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchLocation();
            }
        });
        reviewSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        rvBusinessesDistance = (RecyclerView) findViewById(R.id.rvBusinessDistance);
        // Set layout manager to position the items
        rvBusinessesDistance.setLayoutManager(new LinearLayoutManager(this));

        rvBusinessesRating = (RecyclerView) findViewById(R.id.rvBusinessRating);
        rvBusinessesRating.setLayoutManager(new LinearLayoutManager(this));

        rvBusinessesReview = (RecyclerView) findViewById(R.id.rvBusinessReview);
        rvBusinessesReview.setLayoutManager(new LinearLayoutManager(this));
        setupTabs();


    }


    private class YelpTask extends AsyncTask<String, Void, Integer>
    {
        private ProgressDialog dialog = new ProgressDialog(FindBobaActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
        protected Integer  doInBackground(String... param) {
            Integer result = 0;

            YelpAPI yelp = new YelpAPI();
            Log.d("Latitude", "latitude: "+ latitude + "longitude: "+ longitude);
            String businesses = yelp.searchUsingCoordinates("boba", latitude, longitude);
            //Log.d("help",businesses);
            try {
                JSONObject json = new JSONObject(businesses);
                JSONArray businessesJson = json.getJSONArray("businesses");
                //Log.d("help",businessesJson.toString());
                alBusinesses = new ArrayList<>();
                alBusinessRating = new ArrayList<>();
                alBusinessReview = new ArrayList<>();

                alBusinesses = Business.fromJson(businessesJson);
                alBusinessReview = Business.fromJson(businessesJson);
                alBusinessRating = Business.fromJson(businessesJson);



                result =  1; // Success
            } catch (JSONException e) {
                e.printStackTrace();
                result = 0;
            }
            return result;
        }

        protected void onPostExecute(Integer result) {

            if (result == 1) {

                Collections.sort(alBusinesses, new SortByDistance());
                Collections.sort(alBusinessReview, new SortByReview());
                Collections.sort(alBusinessRating, new SortByRating());


                    adapterDistance = new BusinessRecyclerViewAdapter(FindBobaActivity.this, alBusinesses);
                    // Attach the adapter to the recyclerview to populate items
                    rvBusinessesDistance.setAdapter(adapterDistance);
                    adapterDistance.notifyDataSetChanged();

                    distanceSwipeContainer.setRefreshing(false);



                    adapterRating = new BusinessRecyclerViewAdapter(FindBobaActivity.this, alBusinessRating);
                    rvBusinessesRating.setAdapter(adapterRating);
                    adapterRating.notifyDataSetChanged();

                    ratingSwipeContainer.setRefreshing(false);



                    adapterReview = new BusinessRecyclerViewAdapter(FindBobaActivity.this, alBusinessReview);
                    rvBusinessesReview.setAdapter(adapterReview);
                    adapterReview.notifyDataSetChanged();

                   reviewSwipeContainer.setRefreshing(false);


            }
            else
            {
                Toast.makeText(FindBobaActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();

            }

            if (dialog.isShowing()) {
                dialog.dismiss();
            }



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_boba, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupTabs()
    {
        TabHost tabs = (TabHost)findViewById(R.id.tabHost);
        tabs.setup();

        //Distance
        TabHost.TabSpec distanceTab = tabs.newTabSpec("Distance");
        distanceTab.setContent(R.id.scDistance);
        distanceTab.setIndicator("Distance");
        tabs.addTab(distanceTab);

        //Rating
        TabHost.TabSpec ratingTab = tabs.newTabSpec("Rating");
        ratingTab.setContent(R.id.scRating);
        ratingTab.setIndicator("Rating");
        tabs.addTab(ratingTab);

        //Review
        TabHost.TabSpec reviewTab = tabs.newTabSpec("Review");
        reviewTab.setContent(R.id.scReviews);
        reviewTab.setIndicator("Review");
        tabs.addTab(reviewTab);
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Method to display the location on UI
     * */
    private void searchLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();


        } else {
            latitude = 34.056631;
            longitude = -117.820516;

        }

        YelpTask task = new YelpTask();
        // Lookup the recyclerview in activity layout

        task.execute();
    }
}


