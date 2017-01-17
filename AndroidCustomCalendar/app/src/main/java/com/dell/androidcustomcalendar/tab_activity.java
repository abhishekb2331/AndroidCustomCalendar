package com.dell.androidcustomcalendar;

/**
 * Created by DELL on 12-01-2017.
 */
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import com.dell.androidcustomcalendar.Pager;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.dell.androidcustomcalendar.add_event_activity;
import com.dell.androidcustomcalendar.events_activity;
import com.dell.androidcustomcalendar.MainActivity;

public class tab_activity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    //This is our tablayout
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean flag;

    // Tab titles
    private String[] tabs = { "Calendar", "Events", "Add Event" };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flag=false;

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(tabs[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[2]));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager());
        ViewPagerListner viewPagerListner = new ViewPagerListner();

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(0);



        viewPager.addOnPageChangeListener(viewPagerListner);
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);

    }


    public class ViewPagerListner implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if(position == 0)
            {
                ListView listView = (ListView) viewPager.findViewById(R.id.lstViewEvents);
                listView.setOnItemClickListener(listner);
            }

        }

        @Override
        public void onPageSelected(int position) {

            Log.d("position",String.valueOf(position));
           // Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_LONG).show();
            TabLayout.Tab tab = tabLayout.getTabAt(position);
            tab.select();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

      //  events_activity.refreshValuesFromContentProvider();

        if(tab.getPosition()==0 ) {
            flag=false;
            Pager adapter = new Pager(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(tab.getPosition());

            ListView listView = (ListView) viewPager.findViewById(R.id.lstViewEvents);
            listView.setOnItemClickListener(listner);
        }
        else if(tab.getPosition()==1)
        {
            flag=false;
            Pager adapter = new Pager(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(tab.getPosition());

            //Toast.makeText(getApplicationContext(), "tab1", Toast.LENGTH_LONG).show();
            ListView listView1 = (ListView) viewPager.findViewById(R.id.lstEvents);
            listView1.setOnItemClickListener(listner);
        }
        else if(flag==false)
        {
            Pager adapter = new Pager(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(tab.getPosition());
        }
    }

    AdapterView.OnItemClickListener listner = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {

            flag=true;
            Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
            int id=cursor.getInt(cursor.getColumnIndex("_id"));
            String title =cursor.getString(cursor.getColumnIndex("EVENTTITLE"));
            String venue=cursor.getString(cursor.getColumnIndex("EVENTVENUE"));
            String date=cursor.getString(cursor.getColumnIndex("EVENTDATE"));
            String time=cursor.getString(cursor.getColumnIndex("EVENTTIME"));

            String[] to = new String[5];
            to[0]="_id";
            to[1]="EVENTTITLE";
            to[2]="EVENTVENUE";
            to[3]="EVENTDATE";
            to[4]="EVENTTIME";

            String[] from = new String[5];
            from[0]=String.valueOf(id);
            from[1]=title;
            from[2]=venue;
            from[3]=date;
            from[4]=time;
            Pager adapter = new Pager(getSupportFragmentManager(),to,from);
          //  adapter.addBundle(2,args);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(2,true);
           // TabLayout.Tab tab = tabLayout.getTabAt(2);
          //  tab.select();
        }
    };


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

        if(tab.getPosition()==0)
        {
            CalendarView simpleCalendarView;
            simpleCalendarView = (CalendarView) viewPager.findViewById(R.id.calendarView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) simpleCalendarView.getLayoutParams();

           // Toast.makeText(getApplicationContext(),"params: " + params.weight, Toast.LENGTH_LONG).show();
            if(params.weight==0f)
            {
                params.weight=3f;
                simpleCalendarView.setLayoutParams(params);
            }
            else
            {
                params.weight=0f;
                simpleCalendarView.setLayoutParams(params);
            }
        }
    }
}
