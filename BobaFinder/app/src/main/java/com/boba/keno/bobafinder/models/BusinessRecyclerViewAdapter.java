package com.boba.keno.bobafinder.models;


import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boba.keno.bobafinder.R;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class BusinessRecyclerViewAdapter extends RecyclerView.Adapter<BusinessRecyclerViewAdapter.ViewHolder>
{
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvName;
        public ImageView ivPicture;
        public ImageView ivRating;
        public TextView tvReview;
        public TextView tvDistance;
        private Context context;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.item_nameTextView);
            this.ivPicture = (ImageView) itemView.findViewById(R.id.item_imageView);
            this.ivRating = (ImageView) itemView.findViewById(R.id.item_ratingImageView);
            this.tvReview = (TextView) itemView.findViewById(R.id.item_reviewTextView);
            this.tvDistance = (TextView) itemView.findViewById(R.id.item_distanceTextView);
            // Store the context
            this.context = context;
            // Attach a click listener to the entire row view
            itemView.setOnClickListener(this);
        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            Business business = businesses.get(position);
            // We can access the data within the views
            //change this to access the map
            Toast.makeText(context, tvName.getText(), Toast.LENGTH_SHORT).show();
        }
    }
    // Store a member variable for the businesses
    private static ArrayList<Business> businesses;
    // Store the context for later use
    private Context context;

    public BusinessRecyclerViewAdapter(Context context, ArrayList<Business> businesses)
    {
        this.businesses = businesses;
        Log.d("help", businesses.get(0).getName());
        this.context = context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public BusinessRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(context).
                inflate(R.layout.item_business, parent, false);
        // Return a new holder instance
        return new BusinessRecyclerViewAdapter.ViewHolder(context, itemView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(BusinessRecyclerViewAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Business business = businesses.get(position);
        // Set item views based on the data model
        holder.tvName.setText(business.getName());
        holder.tvReview.setText(business.getReviewCount() + " Reviews");
        double temp = business.getDistance() * 0.00062137;
        holder.tvDistance.setText(String.format("%.2f mi", temp));
        Ion.with(holder.ivPicture)
                .placeholder(R.drawable.testboba)
                .load(business.getImageUrl());
        Ion.with(holder.ivRating)
                .placeholder(R.drawable.stars)
                .load(business.getRatingImgUrl());
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return businesses.size();
    }



}
