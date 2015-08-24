package com.boba.keno.bobafinder.models;


import java.util.Comparator;

public class SortByReview implements Comparator<Business>
{
    //Sorting business by review count in descending  order
    @Override
    public int compare(Business o1, Business o2)
    {
        int a = o1.getReviewCount();
        int b = o2.getReviewCount();
        return a < b ? 1 : (a == b ? 0 : -1);

    }
}
