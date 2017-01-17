package com.dell.androidcustomcalendar;

/**
 * Created by DELL on 13-01-2017.
 */
import com.dell.androidcustomcalendar.add_event_activity;
import com.dell.androidcustomcalendar.events_activity;
import com.dell.androidcustomcalendar.MainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;


public class Pager extends FragmentStatePagerAdapter{

    private MainActivity main = new MainActivity();
    private events_activity events = new events_activity();
    private add_event_activity add_event = new add_event_activity();
    private Bundle bundle =new Bundle();
    //Constructor to the class
    public Pager(FragmentManager fm) {
        super(fm);
    }

    public Pager(FragmentManager fm , String[] to,String[] from) {

        super(fm);
        bundle.putString(to[0],from[0]);
        bundle.putString(to[1],from[1]);
        bundle.putString(to[2],from[2]);
        bundle.putString(to[3],from[3]);
        bundle.putString(to[4],from[4]);


    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                return main;
            case 1:
                return events;
            case 2:
                add_event.setArguments(bundle);
                return add_event;
        }
        return null;
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return 3;
    }
}
