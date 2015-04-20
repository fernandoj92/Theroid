package com.client.thera.theroid.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Fer on 17/03/2015.
 */
public class TheroidDbHelper extends SQLiteOpenHelper{

    private static TheroidDbHelper dbInstance;

    private static int version = 1;
    private static String name="TheroidDb";
    private static CursorFactory factory = null;

    private TheroidDbHelper(Context context){
        super(context,name,factory,version);
    }

    public static TheroidDbHelper getInstance(Context context){
        if (dbInstance == null) {
            dbInstance = new TheroidDbHelper(context.getApplicationContext());
        }
        return dbInstance;
    }

    @Override
     public void onCreate(SQLiteDatabase db){

        createTables(db);

        Log.i(this.getClass().toString(), "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w("UPDATE", "updating database from version " + oldVersion + " to version " + newVersion);
    }

    private void createTables(SQLiteDatabase db){
        db.execSQL(MessageTable.CREATE_MESSAGES_TABLE);

        //TODO eventTime index?
        // db.execSQL("CREATE UNIQUE INDEX EventTime ON MESSAGES(EventTime ASC)");
    }

}
