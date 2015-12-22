package com.praca_inz.Comparators;

import com.praca_inz.Models.FuelingModel;

import java.util.Comparator;

public class FuelingMonthComparator implements Comparator<FuelingModel> {

    @Override
    public int compare(FuelingModel lhs, FuelingModel rhs) {
        return lhs.getMonth() - rhs.getMonth();
    }
}


