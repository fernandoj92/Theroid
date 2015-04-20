package com.client.thera.theroid.presentation;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import com.client.thera.theroid.R;
import com.client.thera.theroid.data.MessageTable;
import com.client.thera.theroid.data.MessagesContentProvider;


public class MessageListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;

    private static final int DELETE_ID = Menu.FIRST + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list);
        this.getListView().setDividerHeight(2);
        fillWithData();
        registerForContextMenu(getListView());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(MessagesContentProvider.CONTENT_URI + "/"
                        + info.id);
                getContentResolver().delete(uri, null, null);
                fillWithData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void fillWithData(){

        //Fields from the database (projection)
        String[] from = new String[]{MessageTable.COLUMN_TEMPERATURE,MessageTable.COLUMN_HEALTH,MessageTable.COLUMN_VOLTAGE,MessageTable.COLUMN_STATUS};
        //Fields on the UI to which we map (Message rows)
        int[] to = new int[]{R.id.temperature_label,R.id.health_label,R.id.voltage_label,R.id.status_label};

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.message_row, null, from,
                to, 0);

        setListAdapter(adapter);
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { MessageTable.COLUMN_ID, MessageTable.COLUMN_TEMPERATURE,MessageTable.COLUMN_HEALTH,MessageTable.COLUMN_VOLTAGE,MessageTable.COLUMN_STATUS };
        CursorLoader cursorLoader = new CursorLoader(this,
                MessagesContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }
}
