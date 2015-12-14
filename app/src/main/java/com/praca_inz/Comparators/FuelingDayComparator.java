package com.praca_inz.Comparators;

import com.praca_inz.ModelsAndDB.FuelingModel;

import java.util.Comparator;

public class FuelingDayComparator implements Comparator<FuelingModel> {

    @Override
    public int compare(FuelingModel lhs, FuelingModel rhs) {
        return lhs.getDay() - rhs.getDay();
    }
}
