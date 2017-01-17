package com.dell.androidcustomcalendar;

import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by DELL on 12-01-2017.
 */

public class events_activity extends Fragment{
  /*
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_view);
    }
    */


    private ListView listView;
    private SimpleCursorAdapter adapter;
    private static final String PROVIDER_NAME = "com.dell.androidcustomcalendar.EventsProvider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/events");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View v;
        v=inflater.inflate(R.layout.events_view, container, false);
        listView = (ListView) v.findViewById(R.id.lstEvents);

        adapter = new SimpleCursorAdapter(getActivity().getBaseContext(),
                R.layout.list_layout,
                null,
                new String[] { "EVENTTITLE", "EVENTVENUE", "EVENTDATE" , "EVENTTIME"},
                new int[] { R.id.eventTitle , R.id.eventVenue, R.id.eventDate,R.id.eventTime }, 0);
        listView.setAdapter(adapter);
        refreshValuesFromContentProvider();

        return v;
    }

    private void refreshValuesFromContentProvider() {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String curDate = sdf.format(date.getTime());
        String selection = "EVENTDATE >= \"" + curDate +"\"";

        CursorLoader cursorLoader = new CursorLoader(getActivity().getBaseContext(), CONTENT_URI,
                null, selection, null, "EVENTDATE");
        Cursor c = cursorLoader.loadInBackground();
        adapter.swapCursor(c);
    }
}
