package com.dell.androidcustomcalendar;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.TabHost;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;


public class MainActivity  extends Fragment {

    CalendarView simpleCalendarView;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private static final String PROVIDER_NAME = "com.dell.androidcustomcalendar.EventsProvider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/events");
    private String Selected_date,Selected_month,Selected_year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View v;
        v=inflater.inflate(R.layout.calendar_view, container, false);
        Bundle args = getArguments();
        if(args!=null)
        {
          //  Toast.makeText(getActivity().getApplicationContext(),args.getString("YourKey"), Toast.LENGTH_LONG).show();

        }


        simpleCalendarView = (CalendarView) v.findViewById(R.id.calendarView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String selectedDate = sdf.format(new Date(simpleCalendarView.getDate()));
        String sel[] =selectedDate.split("-");
        Selected_date=sel[0];
        Selected_month=sel[1];
        Selected_year=sel[2];


        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast

                String day=('0' + String.valueOf(dayOfMonth));
                String mnth=('0'+ String.valueOf(month+1));
                String sel_day=day.substring(day.length() - 2);
                String sel_mnth=mnth.substring(mnth.length() - 2);
                Selected_date=sel_day;
                Selected_month=sel_mnth;
                Selected_year=String.valueOf(year);
               // Toast.makeText(getActivity().getApplicationContext(),sel_day + "/" + sel_mnth + "/" + year, Toast.LENGTH_LONG).show();
                refreshValuesFromContentProvider();
            }
        });

        listView = (ListView) v.findViewById(R.id.lstViewEvents);

        adapter = new SimpleCursorAdapter(getActivity().getBaseContext(),
                R.layout.listlayout,
                null,
                new String[] { "EVENTTITLE", "EVENTVENUE" ,"EVENTTIME"},
                new int[] { R.id.eventTitle , R.id.eventVenue, R.id.eventTime}, 0);
        listView.setAdapter(adapter);
      //  listView.setOnItemClickListener(listner);
        refreshValuesFromContentProvider();

        return v;
    }

    private void refreshValuesFromContentProvider() {

        String selectedDate= Selected_date + "-" + Selected_month + "-" + Selected_year;

        String selection = "EVENTDATE = \"" + selectedDate +"\"";
        CursorLoader cursorLoader = new CursorLoader(getActivity().getBaseContext(), CONTENT_URI,
                null, selection, null, "EVENTTIME");
        Cursor c = cursorLoader.loadInBackground();
        adapter.swapCursor(c);
    }


}
