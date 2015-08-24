package com.boba.keno.bobafinder.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Business {
    private String id;
    private String name;
    private String phone;
    private String imageUrl;
    private double rating;
    private int reviewCount;
    private double distance;
    private String snippetText;
    private String ratingImgUrl;
    private String snippetImgUrl;
    private String city="";
    private String address;
    private double logitude;
    private double latitude;
    private JSONObject location;


    public String getSnippetImgUrl() {
        return snippetImgUrl;
    }
    public void setSnippetImgUrl(String snippetImgUrl) {
        this.snippetImgUrl = snippetImgUrl;
    }
    public double getDistance() {return distance;}
    public void setDistance(double distance) {this.distance = distance;}
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {this.rating = rating;}
    public int getReviewCount() {return reviewCount;}
    public void setReviewCount(int reviewCount) {this.reviewCount = reviewCount;}
    public String getSnippetText() {
        return snippetText;
    }
    public void setSnippetText(String snippetText) {
        this.snippetText = snippetText;
    }
    public String getRatingImgUrl() {
        return ratingImgUrl;
    }
    public void setRatingImgUrl(String ratingImgUrl) {
        this.ratingImgUrl = ratingImgUrl;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLogitude() {
        return logitude;
    }

    public void setLogitude(double logitude) {
        this.logitude = logitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public static Business fromJson(JSONObject jsonObject) {
        Business b = new Business();
        // Deserialize json into object fields
        try {
            b.id = jsonObject.getString("id");
            b.name = jsonObject.getString("name");
            b.imageUrl = jsonObject.getString("image_url");
            b.rating = jsonObject.getDouble("rating");
            b.distance = jsonObject.getDouble("distance");
            b.reviewCount = jsonObject.getInt("review_count");
            b.ratingImgUrl = jsonObject.getString("rating_img_url");
            b.location = jsonObject.getJSONObject("location");
            JSONObject coords = b.location.getJSONObject("coordinate");
            b.latitude = coords.getDouble("latitude");
            b.logitude = coords.getDouble("longitude");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return b;
    }

    public static ArrayList<Business> fromJson(JSONArray jsonArray) {
        ArrayList<Business> businesses = new ArrayList<Business>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject businessJson = null;
            try {
                businessJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Business business = Business.fromJson(businessJson);
            if (business != null) {
                businesses.add(business);
            }
        }

        return businesses;
    }
    public Business()
    {}

    public Business (String name)
    {
        setName(name);
    }


    public JSONObject getLocation() {
        return location;
    }

    public void setLocation(JSONObject location) {
        this.location = location;
    }
}
