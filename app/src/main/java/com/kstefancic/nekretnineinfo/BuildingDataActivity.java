package com.kstefancic.nekretnineinfo;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.kstefancic.nekretnineinfo.buildinginsert.AddressInformationFragment;
import com.kstefancic.nekretnineinfo.buildinginsert.BuildingDetailsFragment;
import com.kstefancic.nekretnineinfo.buildinginsert.DimensionsFragment;
import com.kstefancic.nekretnineinfo.buildinginsert.PicturesFragment;

import java.util.ArrayList;
import java.util.List;

public class BuildingDataActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private int[] tabIcons={
          /*  R.drawable.ic_business_black_48dp,
            R.drawable.ic_place_black_48dp,
            R.drawable.ic_straighten_black_48dp,
            R.drawable.ic_photo_library_black_48dp*/
            R.mipmap.building,
            R.mipmap.location,
            R.mipmap.dimension,
            R.mipmap.gallery
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_data);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        setUpViewPager();

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setUpTabIcons();
    }

    private void setUpTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        Log.d("TAB ICON", String.valueOf(tabIcons[0]));
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        Log.d("TAB ICON", String.valueOf(tabIcons[1]));
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        Log.d("TAB ICON", String.valueOf(tabIcons[2]));
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        Log.d("TAB ICON", String.valueOf(tabIcons[3]));
    }

    private void setUpViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BuildingDetailsFragment());
        adapter.addFragment(new AddressInformationFragment());
        adapter.addFragment(new DimensionsFragment());
        adapter.addFragment(new PicturesFragment());
        mViewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_building_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        public void addFragment(Fragment fragment){
            this.fragments.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
