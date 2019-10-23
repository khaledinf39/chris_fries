package com.kh_sof_dev.chris_fries.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kh_sof_dev.chris_fries.Fragments.Cancel_request;
import com.kh_sof_dev.chris_fries.Fragments.Current_request;
import com.kh_sof_dev.chris_fries.Fragments.Waite_request;
import com.kh_sof_dev.chris_fries.Fragments.compte_request;
import com.kh_sof_dev.chris_fries.R;

public class Odrer_activity extends AppCompatActivity {


    //    public static ImageView avatar;
    // Titles of the individual pages (displayed in tabs)
    private final String[] PAGE_TITLES = new String[] {
            "في الإنتظار",
            "الحالية",

            "المكتملة",
            "ملغاة"
            // , "الملغية من طرفي"
    };

    private final Fragment[] PAGES = new Fragment[] {
            new Waite_request(),
            new Current_request(),
            new compte_request(),
            new Cancel_request(),
    };

    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;
    public static Context context;

    private ViewPager mViewPager;


    ImageView alerts_icon, cancel_btn;

    private String email,password,firebaseToken,logoutMessage;

    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private Boolean flag;
    public static int ReqType;

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odrer_activity);
        context = this;
        /****************************definitions*****************************/


        cancel_btn = (ImageView)findViewById(R.id.back_btn);
cancel_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});


        fragmentManager = getSupportFragmentManager();
        mViewPager = (ViewPager) findViewById(R.id.viewpage);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
//        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#56ce8b"));
        tabLayout.setSelectedTabIndicatorHeight((int) (4 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));

        tabLayout.setupWithViewPager(mViewPager);
//
        /*****************************Drawer Actions*******************************/




    }
    /* PagerAdapter for supplying the ViewPager with the pages (fragments) to display. */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

    }

    }




