
package se.slide.babyfeed;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.JazzyViewPager.TransitionEffect;

import se.slide.babyfeed.db.DatabaseManager;
import se.slide.babyfeed.utils.Utils;

public class FeedActivity extends FragmentActivity implements
        ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    // ViewPager mViewPager;
    private JazzyViewPager mViewPager;
    
    private SharedPreferences mSharedPreferences;
    
    private AlertDialog mRateAppDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseManager.init(this);
        setContentView(R.layout.activity_feed);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        firstTimeUse();
        
        rateApp();
        
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (JazzyViewPager) findViewById(R.id.jazzy_pager);
        mViewPager.setTransitionEffect(TransitionEffect.FlipHorizontal);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageMargin(30);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

    }
    
    private void firstTimeUse() {
        boolean showNotification = true;
        
        if (mSharedPreferences != null)
            showNotification = mSharedPreferences.getBoolean(Utils.PREF_FIRST_USE, true);
        
        if (mSharedPreferences != null && showNotification) {
            mSharedPreferences.edit().putBoolean(Utils.PREF_FIRST_USE, false).commit();
            
            FragmentManager fm = getSupportFragmentManager();
            
            CustomDialog firstUseDialog = CustomDialog.newInstance(getString(R.string.first_time_title), getString(R.string.first_time_message));
            firstUseDialog.show(fm, "custom_dialog_tag");
        }
    }
    
    private void rateApp() {
        if (!mSharedPreferences.getBoolean(Utils.PREF_APP_RATED, false)) {
            int appOpens = mSharedPreferences.getInt(Utils.PREF_APP_OPENS, 0) + 1;
            if (appOpens == 20 || appOpens == 100) {
                
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.rate_app_title);
                builder.setMessage(R.string.rate_app_message);
                builder.setPositiveButton(getString(R.string.rate_app_yes), new OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSharedPreferences.edit().putBoolean(Utils.PREF_APP_RATED, true).commit();
                        showGooglePlay();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(getString(R.string.rate_app_no), new OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        
                    }
                });
                
                mRateAppDialog = builder.create();
                mRateAppDialog.show();
                
            }
            
            if (appOpens < 101)
                mSharedPreferences.edit().putInt(Utils.PREF_APP_OPENS, appOpens).commit();
        }
        
    }
    
    private void showGooglePlay() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
          startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
          Toast.makeText(this, "Couldn't launch the market", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_feed, menu);
        return true;
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //

        if (item.getItemId() == R.id.menu_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        stopService(new Intent(this, PlayAlarmService2.class));
        super.onResume();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        
        if (mRateAppDialog != null)
            mRateAppDialog.dismiss();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            if (position == 0) {
                // Fragment fragment = new FeedFragment();
                Fragment fragment = new FeedFragment();
                Bundle args = new Bundle();
                fragment.setArguments(args);
                return fragment;
            }
            else {
                Fragment fragment = new LogFragment();
                Bundle args = new Bundle();
                fragment.setArguments(args);
                return fragment;
            }

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase();
                case 1:
                    return getString(R.string.title_section2).toUpperCase();
            }
            return null;
        }
    }

}
