package spe.uoblibraryapp;

import java.util.Comparator;

import spe.uoblibraryapp.api.wmsobjects.WMSLoan;

public class SortCustomComparatorDueDate implements Comparator<WMSLoan> {
    public int compare(WMSLoan object1, WMSLoan object2) {
        return (object1.getDueDate().compareTo(object2.getDueDate()));
    }
}
