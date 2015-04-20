package com.client.thera.theroid.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Fer on 17/03/2015.
 */
public class MessageTable {

    public static final String TABLE_NAME="messages";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_TEMPERATURE="Temperature";
    public static final String COLUMN_HEALTH="Health";
    public static final String COLUMN_VOLTAGE="Voltage";
    public static final String COLUMN_STATUS="Status";
    public static final String COLUMN_EVENT_TIME="EventTime";


    public static String CREATE_MESSAGES_TABLE = "CREATE TABLE IF NOT EXISTS MESSAGES("+
                                                "_id INTEGER PRIMARY KEY,"+
                                                "Temperature INTEGER,"+
                                                "Health INTEGER," +
                                                "Voltage INTEGER," +
                                                "Status VARCHAR," +
                                                "EventTime DATETIME DEFAULT CURRENT_TIMESTAMP)";

    private Context context;
    private TheroidDbHelper dbHelper;
    private SQLiteDatabase db;

    //Used in queries
    public static String[] columns = new String[]{ COLUMN_ID,COLUMN_EVENT_TIME,COLUMN_TEMPERATURE,COLUMN_HEALTH,COLUMN_VOLTAGE,COLUMN_STATUS} ;

    public static String DEFAULT_SORT_ORDER = "EventTime";

    public MessageTable(Context context){
        this.context = context;
    }



}
