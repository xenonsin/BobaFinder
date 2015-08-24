package com.boba.keno.bobafinder;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.boba.keno.bobafinder.models.Business;
import com.boba.keno.bobafinder.models.BusinessRecyclerViewAdapter;
import com.boba.keno.bobafinder.models.SortByDistance;
import com.boba.keno.bobafinder.models.SortByRating;
import com.boba.keno.bobafinder.models.SortByReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class FindBobaActivity extends AppCompatActivity {

    ArrayList<Business> alBusinesses;
    ArrayList<Business> alBusinessRating;
    ArrayList<Business> alBusinessReview;
    RecyclerView rvBusinessesDistance;
    RecyclerView rvBusinessesRating;
    RecyclerView rvBusinessesReview;

    BusinessRecyclerViewAdapter adapterDistance;
    BusinessRecyclerViewAdapter adapterRating;
    BusinessRecyclerViewAdapter adapterReview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_boba);

        rvBusinessesDistance = (RecyclerView) findViewById(R.id.rvBusinessDistance);
        // Set layout manager to position the items
        rvBusinessesDistance.setLayoutManager(new LinearLayoutManager(this));

        rvBusinessesRating = (RecyclerView) findViewById(R.id.rvBusinessRating);
        rvBusinessesRating.setLayoutManager(new LinearLayoutManager(this));

        rvBusinessesReview = (RecyclerView) findViewById(R.id.rvBusinessReview);
        rvBusinessesReview.setLayoutManager(new LinearLayoutManager(this));
        setupTabs();
        YelpTask task = new YelpTask();
        // Lookup the recyclerview in activity layout

        task.execute();


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
            String businesses = yelp.searchUsingCoordinates("bubble tea", 37.788022, -122.399797);
            //Log.d("help",businesses);
            try {
                JSONObject json = new JSONObject(businesses);
                JSONArray businessesJson = json.getJSONArray("businesses");
                //Log.d("help",businessesJson.toString());
                alBusinesses = Business.fromJson(businessesJson);
                alBusinessReview = Business.fromJson(businessesJson);
                alBusinessRating = Business.fromJson(businessesJson);
                Collections.sort(alBusinesses, new SortByDistance());
                Collections.sort(alBusinessReview, new SortByReview());
                Collections.sort(alBusinessRating, new SortByRating());


                result =  1; // Success
            } catch (JSONException e) {
                e.printStackTrace();
                result = 0;
            }
            return result;
        }

        protected void onPostExecute(Integer result) {

            if (result == 1) {

                // Create adapter passing in the sample user data
                adapterDistance = new BusinessRecyclerViewAdapter(FindBobaActivity.this, alBusinesses);
                // Attach the adapter to the recyclerview to populate items
                rvBusinessesDistance.setAdapter(adapterDistance);

                adapterRating = new BusinessRecyclerViewAdapter(FindBobaActivity.this, alBusinessRating);
                rvBusinessesRating.setAdapter(adapterRating);

                adapterReview = new BusinessRecyclerViewAdapter(FindBobaActivity.this, alBusinessReview);
                rvBusinessesReview.setAdapter(adapterReview);
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
        distanceTab.setContent(R.id.Distance);
        distanceTab.setIndicator("Distance");
        tabs.addTab(distanceTab);

        //Rating
        TabHost.TabSpec ratingTab = tabs.newTabSpec("Rating");
        ratingTab.setContent(R.id.Rating);
        ratingTab.setIndicator("Rating");
        tabs.addTab(ratingTab);

        //Review
        TabHost.TabSpec reviewTab = tabs.newTabSpec("Review");
        reviewTab.setContent(R.id.Reviews);
        reviewTab.setIndicator("Review");
        tabs.addTab(reviewTab);
    }
}


