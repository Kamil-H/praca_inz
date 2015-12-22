package com.praca_inz.Comparators;

/**
 * Created by KamilH on 2015-10-21.
 */

import com.praca_inz.Models.FuelingModel;

import java.util.Comparator;

public class FuelingYearComparator implements Comparator<FuelingModel> {

    @Override
    public int compare(FuelingModel lhs, FuelingModel rhs) {
        return lhs.getYear() - rhs.getYear();
    }
}


