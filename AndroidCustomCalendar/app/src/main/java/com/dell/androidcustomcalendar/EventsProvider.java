package com.dell.androidcustomcalendar;

/**
 * Created by DELL on 14-01-2017.
 */

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.content.UriMatcher;
import android.content.Context;
import android.content.ContentUris;
import android.util.Log;


public class EventsProvider extends ContentProvider{

    private static final String PROVIDER_NAME = "com.dell.androidcustomcalendar.EventsProvider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/events");
    private static final int EVENTS = 1;
    private static final int EVENT_ID = 2;
    private static final UriMatcher uriMatcher = getUriMatcher();
    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "events", EVENTS);
        uriMatcher.addURI(PROVIDER_NAME, "events/#", EVENT_ID);
        return uriMatcher;
    }
    private EventDatabase eventDataBase = null;


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case EVENTS:
                return "vnd.android.cursor.dir/events";
            case EVENT_ID:
                return "vnd.android.cursor.item/events";
        }
        return "";
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        eventDataBase = new EventDatabase(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String id = null;
        if(uriMatcher.match(uri) == EVENT_ID) {
            //Query is for one single event. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }
        return eventDataBase.getEvents(id, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            long id = eventDataBase.addNewEvent(values);
            Uri returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
            return returnUri;
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == EVENT_ID) {
            //Delete is for one single event. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

        return eventDataBase.deleteEvents(id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == EVENT_ID) {
            //Update is for one single event. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

        return eventDataBase.updateEvents(id, values);
    }

}
