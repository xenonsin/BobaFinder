package com.boba.keno.bobafinder.models;


import java.util.Comparator;

public class SortByDistance implements Comparator<Business>
{
    //Sorts by distance in ascending order.
    @Override
    public int compare(Business o1, Business o2)
    {
        double a = o1.getDistance();
        double b = o2.getDistance();
        return a > b ? 1 : (a == b ? 0 : -1);

    }
}
