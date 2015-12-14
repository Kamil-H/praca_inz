package com.praca_inz.Comparators;

import com.praca_inz.ModelsAndDB.FuelingModel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by KamilH on 2015-10-21.
 */
public class ChainedComparator implements Comparator<FuelingModel> {

    private List<Comparator<FuelingModel>> listComparators;

    @SafeVarargs
    public ChainedComparator(Comparator<FuelingModel>... comparators) {
        this.listComparators = Arrays.asList(comparators);
    }

    @Override
    public int compare(FuelingModel emp1, FuelingModel emp2) {
        for (Comparator<FuelingModel> comparator : listComparators) {
            // zamiana emp1 i emp2 powoduje odwrócenie sortowania
            int result = comparator.compare(emp2, emp1);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}