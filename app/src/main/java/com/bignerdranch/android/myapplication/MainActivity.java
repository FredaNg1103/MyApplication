package com.bignerdranch.android.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.widget.SimpleCursorAdapter;


public class MainActivity extends ListActivity {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private Database mDbHelper;

//where the activity is initialize and call which layout xml class
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        mDbHelper = new Database(this);
        mDbHelper.open();  //calling the database

        retrieve();
        registerForContextMenu(getListView());   //to call the listview from the List Activity class

    }

    //action bar display and call the menu xml class
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.AddItem:
                Intent i = new Intent(this, AddItemActivity.class);  //leads to the Add Item Activity class
                startActivityForResult(i, ACTIVITY_CREATE); // the activity begins there
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    // for edit purposes, by just a short press on whichever item in the listview
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, AddItemActivity.class); //edit activity begins by heading back to the add item activity class
        i.putExtra(MySQLiteHelper.ROWID, id);  // add any updates
        startActivityForResult(i, ACTIVITY_EDIT);
    }


    // a menu where need to be long pressed , then the pop up menu will be display
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
    }

    // long pressed on which item selected on the listview, then it will be deleted from the database and then dissapear from the listview
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.delete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteTask(info.id);
                retrieve();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        retrieve();
    }

    // all the items from the database will be display in the listview, which have to call out from the database
    private void retrieve()
    {
        Cursor taskCursor = mDbHelper.RetrieveAllTask();
        startManagingCursor(taskCursor);

        String[] details = new String[]{MySQLiteHelper.TITLE, MySQLiteHelper.DATE_TIME};  //Array of cursor columns bind to

        int[] whichRow = new int[]{R.id.text1,R.id.text2}; //parallel array of the String [] details


        SimpleCursorAdapter task = new SimpleCursorAdapter(this, R.layout.item_row, taskCursor, details, whichRow); //specify the row template to use
        setListAdapter(task);   //new adapter is binded


    }


    }



