package com.boba.keno.bobafinder.models;


import java.util.Comparator;

public class SortByRating implements Comparator<Business>
{
    //Sorting business by rating in descending  order
    @Override
    public int compare(Business o1, Business o2)
    {
        double a = o1.getRating();
        double b = o2.getRating();
        return a < b ? 1 : (a == b ? 0 : -1);

    }
}

