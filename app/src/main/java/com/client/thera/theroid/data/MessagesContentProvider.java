package com.client.thera.theroid.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Fer on 19/03/2015.
 * TODO: Comments on CRUD methods
 */
public class MessagesContentProvider extends ContentProvider{

    //Database
    private TheroidDbHelper dbHelper;

    private static final int MESSAGES = 1;
    private static final int MESSAGE_ID = 2;

    private static final String AUTHORITY = "com.client.thera.theroid.contentprovider";
    private static final String BASE_PATH = "messages";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/messages";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/message";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, MESSAGES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MESSAGE_ID);
    }

    @Override
    public boolean onCreate(){
        dbHelper = TheroidDbHelper.getInstance(getContext());
        return false;
    }
    //Not needed, Simple types...
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String where,
                        String[] whereArgs, String sortOrder) {

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        //If no sort order is specified, use the default one
        String orderby;
        if(TextUtils.isEmpty(sortOrder))
            orderby = MessageTable.DEFAULT_SORT_ORDER;
        else
            orderby = sortOrder;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sURIMatcher.match(uri);
        Cursor cursor;
        switch(match){
            case MESSAGES:
                //Query the database for all the messages
                cursor = db.query(MessageTable.TABLE_NAME, projection, where, whereArgs, null, null, orderby);
                break;
            case MESSAGE_ID:
                //Query the database for a specific message
                long messageID = ContentUris.parseId(uri);
                String composedWhere = MessageTable.COLUMN_ID + " = " + messageID +
                        (!TextUtils.isEmpty(where) ? "AND ("+ where + ')' : "");
                cursor = db.query(MessageTable.TABLE_NAME, projection, composedWhere, whereArgs, null, null, orderby);
                break;
            default: throw new IllegalArgumentException("unsuported uri: "+ uri);
        }
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }
    //One by one insertion right now... (even though it can insert multiple rows)
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case MESSAGES:
                id = db.insert(MessageTable.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case MESSAGES:
                rowsDeleted = db.delete(MessageTable.TABLE_NAME,where,whereArgs);
                break;
            case  MESSAGE_ID:
                long messageID = ContentUris.parseId(uri);
                String composedWhere = MessageTable.COLUMN_ID + " = " + messageID +
                        (!TextUtils.isEmpty(where) ? "AND ("+ where + ')' : "");
                db.delete(MessageTable.TABLE_NAME,composedWhere,whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case MESSAGES:
                rowsUpdated = db.update(MessageTable.TABLE_NAME, values, where, whereArgs);
                break;
            case MESSAGE_ID:
                long messageID = ContentUris.parseId(uri);
                String composedWhere = MessageTable.COLUMN_ID + " = " + messageID +
                        (!TextUtils.isEmpty(where) ? "AND ("+ where + ')' : "");
                rowsUpdated = db.update(MessageTable.TABLE_NAME,values,composedWhere,whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = MessageTable.columns;
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
