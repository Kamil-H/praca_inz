package com.praca_inz;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.praca_inz.Fragments.CalcFragment;
import com.praca_inz.Fragments.InfoFragment;
import com.praca_inz.Fragments.NavFragment;
import com.praca_inz.Fragments.FuelFragment;
import com.praca_inz.Fragments.RoutesFragment;
import com.praca_inz.InternetClasses.Region;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private int[] tabIcons = {
                R.drawable.ic_information_white_24dp,
                R.drawable.ic_navigation_white_24dp,
                R.drawable.ic_maps_map,
                R.drawable.ic_gas_station_white_24dp,
                R.drawable.ic_calculator_white_24dp,
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getPetrolPrices();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                String names[] = {"Informacje o aucie", "Nawigacja", "Przebyte trasy", "Dziennik tankowania", "Kalkulkator kosztów podróży"};
                getSupportActionBar().setTitle(names[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        // przechodzenie do konkretnej zakładki (TAB)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TabLayout.Tab tab = tabLayout.getTabAt(extras.getInt("position"));
            tab.select();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getPetrolPrices() {
        Criteria cr = new Criteria();
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location loc = lm.getLastKnownLocation(lm.getBestProvider(cr, true));

        if (loc != null){
            // odczytanie długości i szerokości geograficznej
            double lat = loc.getLatitude();
            double lon = loc.getLongitude();
            new Region(this).execute(lat, lon);
        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InfoFragment());
        adapter.addFragment(new NavFragment());
        adapter.addFragment(new RoutesFragment());
        adapter.addFragment(new FuelFragment());
        adapter.addFragment(new CalcFragment());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}