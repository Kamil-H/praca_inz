package com.praca_inz.Fragments;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.praca_inz.Activities.AddFuelingActivity;
import com.praca_inz.Adapters.ExpandableAdapters.ExpandableAdapter;
import com.praca_inz.Adapters.ExpandableAdapters.ExpandableItemChild;
import com.praca_inz.Adapters.ExpandableAdapters.ExpandableItemParent;
import com.praca_inz.Comparators.ChainedComparator;
import com.praca_inz.Comparators.FuelingMonthComparator;
import com.praca_inz.Comparators.FuelingYearComparator;
import com.praca_inz.ModelsAndDB.FuelingDB;
import com.praca_inz.ModelsAndDB.FuelingModel;
import com.praca_inz.R;
import com.praca_inz.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by KamilH on 2015-10-13.
 */
public class PetrolFragment extends Fragment {
    View view;
    FloatingActionButton fab;

    private List<Object> listItems;

    public PetrolFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_petrol, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddFuelingActivity.class);
                startActivity(i);
            }
        });

        Object object = getActivity().getLastCustomNonConfigurationInstance();
        if(object!=null) {
            listItems = (ArrayList<Object>) object;
        } else {
            getFuelings();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ExpandableAdapter expandableAdapter = new ExpandableAdapter(listItems);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(expandableAdapter);
    }

    private void generateList(List<FuelingModel> fuelingModels){
        listItems = new ArrayList<>();

        Map<Integer, List<FuelingModel>> map = generateMap(fuelingModels);
        Set<Integer> mapKeySet = map.keySet();
        int sortedKeys[] = sortSet(mapKeySet);

        for(int key: sortedKeys){
            List<FuelingModel> fMs = map.get(key);
            FuelingModel fM = fMs.get(0);
            float cost = 0;

            if(fMs.size() > 0){
                for (int i = 0; i < fMs.size(); i++){
                    cost = cost + fMs.get(i).getCost();
                }
            }
            else cost = fM.getCost();

            ExpandableItemParent expandableItemParent = new ExpandableItemParent(cost, fM.getYear(), fM.getMonth());
            List<Object> childObjects = new ArrayList<>();

            for (int j = 0; j < fMs.size()+1; j++) {
                if(j != 0){
                    FuelingModel fuelingModel = fMs.get(j-1);
                    ExpandableItemChild expandableItemChild = new ExpandableItemChild(Utilities.replace(fuelingModel.getDate()), String.valueOf(fuelingModel.getCost()) +" zł",
                            String.valueOf(fuelingModel.getLitres()) + " l", String.valueOf(fuelingModel.getPrice()) + " zł/l");
                    childObjects.add(expandableItemChild);
                }
                else {
                    ExpandableItemChild expandableItemChild = new ExpandableItemChild("Data tankowania", "Koszt", "Ilość", "Cena");
                    childObjects.add(expandableItemChild);
                }
            }
            expandableItemParent.setChildObjectList(childObjects);
            listItems.add(expandableItemParent);
        }
    }

    private int[] sortSet(Set<Integer> set){
        List<FuelingModel> fuelingModels = new ArrayList<FuelingModel>();
        int array[] = new int[set.size()];
        int i = 0;

        for(int key: set){
            FuelingModel fM = new FuelingModel(key);
            fuelingModels.add(fM);
        }
        Collections.sort(fuelingModels, new ChainedComparator(
                new FuelingYearComparator(),
                new FuelingMonthComparator()
        ));

        for(FuelingModel fM : fuelingModels){
            array[i] = fM.getMonthYear();
            i++;
        }

        return array;
    }

    private Map<Integer, List<FuelingModel>> generateMap(List<FuelingModel> fuelingModels){
        Map<Integer, List<FuelingModel>> map = new HashMap<Integer, List<FuelingModel>>();

        for (FuelingModel fM : fuelingModels) {
            int key  = fM.getMonthYear();
            if(map.containsKey(key)){
                List<FuelingModel> list = map.get(key);
                list.add(fM);
            }else{
                List<FuelingModel> list = new ArrayList<FuelingModel>();
                list.add(fM);
                map.put(key, list);
            }
        }
        return map;
    }

    private void getFuelings(){
        FuelingDB fDB = new FuelingDB(getActivity());
        List<FuelingModel> fuelingModels = fDB.getAllFuelings();

        generateList(fuelingModels);
    }
}