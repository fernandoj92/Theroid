package com.client.thera.theroid.presentation;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.client.thera.theroid.R;

/**
 * Created by Fer on 05/04/2015.
 */
//TODO: NOT USED
public class MessageAdapter  extends CursorAdapter{

    private LayoutInflater mInflater;
    private Cursor cursor;

    public MessageAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cursor = c;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO Auto-generated method stub

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.message_row, parent, false);
    }
}
