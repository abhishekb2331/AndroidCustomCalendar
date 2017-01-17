package com.dell.androidcustomcalendar;

/**
 * Created by DELL on 13-01-2017.
 */

import android.content.ContentUris;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ImageButton;
import java.util.Calendar;
import android.content.ContentValues;
import android.net.Uri;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


public class add_event_activity extends Fragment implements View.OnClickListener{

    ImageButton btn_date,btn_time;
    EditText txt_date,txt_time,txt_title,txt_venue;
    Button btn;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String id =null;
    private static final String PROVIDER_NAME = "com.dell.androidcustomcalendar.EventsProvider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/events");

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View v;
        v=inflater.inflate(R.layout.add_event_view, container, false);

        btn_date=(ImageButton)v.findViewById(R.id.btn_date);
        btn_time=(ImageButton)v.findViewById(R.id.btn_time);
        txt_date=(EditText)v.findViewById(R.id.in_date);
        txt_time=(EditText)v.findViewById(R.id.in_time);
        txt_title=(EditText)v.findViewById(R.id.title);
        txt_venue=(EditText)v.findViewById(R.id.venue);
        btn=(Button) v.findViewById(R.id.button);

        Bundle args = getArguments();
        if(args!=null)
        {
            txt_title.setText(args.getString("EVENTTITLE"));
            txt_venue.setText(args.getString("EVENTVENUE"));
            txt_date.setText(args.getString("EVENTDATE"));
            txt_time.setText(args.getString("EVENTTIME"));
            id=args.getString("_id");
          //  Toast.makeText(getActivity().getApplicationContext(),id + args.getString("EVENTTITLE") + "hu"+args.getString("test")+" sa" + args.getString("YourKey"), Toast.LENGTH_LONG).show();

        }

        btn_date.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        btn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

        if (v == btn_date) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity() , datePickerListener, mYear, mMonth, mDay);
            datePickerDialog.setTitle("Select Date");
            datePickerDialog.show();

        }
        else if (v == btn_time)
        {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),timePickerListner, mHour, mMinute, false);
            timePickerDialog.setTitle("Select Time");
            timePickerDialog.show();

        }
        else if (v == btn)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("EVENTTITLE", txt_title.getText().toString());
            contentValues.put("EVENTVENUE" , txt_venue.getText().toString());
            contentValues.put("EVENTDATE", txt_date.getText().toString());
            contentValues.put("EVENTTIME", txt_time.getText().toString());

            if(id!=null)
            {
                Uri _uri = ContentUris.withAppendedId(CONTENT_URI, Long.parseLong(id));
                getActivity().getContentResolver().update(_uri,contentValues,null,null);
                Toast.makeText(getActivity().getApplicationContext(), "Data Updated", Toast.LENGTH_LONG).show();
            }
            else {
                Uri uri = getActivity().getContentResolver().insert(CONTENT_URI, contentValues);
                Toast.makeText(getActivity().getApplicationContext(), "Data Inserted", Toast.LENGTH_LONG).show();
                // Toast.makeText(getActivity().getApplicationContext(), uri.toString(), Toast.LENGTH_LONG).show();
            }

            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar cal = Calendar.getInstance();

           // Toast.makeText(getActivity().getApplicationContext(), mDay +"-" + mMonth +"-" + mYear +"  " + mHour+":"+mMinute, Toast.LENGTH_LONG).show();

            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, mMinute);
            if(mHour<12) {
                cal.set(Calendar.HOUR, mHour);
                cal.set(Calendar.AM_PM, Calendar.AM);
            }
            else
            {
                cal.set(Calendar.HOUR,( (mHour-12) == 0 ? (12) : (mHour-12)));
                cal.set(Calendar.AM_PM, Calendar.PM);

            }
            cal.set(Calendar.MONTH, mMonth);
            cal.set(Calendar.DAY_OF_MONTH, mDay);
            cal.set(Calendar.YEAR, mYear);

            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);


        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mDay=dayOfMonth;
            mMonth=monthOfYear;
            mYear=year;

            String day=('0' + String.valueOf(dayOfMonth));
            String mnth=('0'+ String.valueOf(monthOfYear+1));
            String sel_day=day.substring(day.length() - 2);
            String sel_mnth=mnth.substring(mnth.length() - 2);
            txt_date.setText(sel_day + "-" + (sel_mnth) + "-" + year);
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListner = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            mHour=hourOfDay;
            mMinute=minute;

            String hr=('0' + String.valueOf(hourOfDay));
            String mn=('0' + String.valueOf(minute));
            String sel_hr=hr.substring(hr.length()-2);
            String sel_mn=mn.substring(mn.length()-2);

            txt_time.setText(sel_hr + ":" + sel_mn);
        }
    };

}
