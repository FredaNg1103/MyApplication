package com.bignerdranch.android.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static com.bignerdranch.android.myapplication.MySQLiteHelper.DATE_TIME;
import static com.bignerdranch.android.myapplication.MySQLiteHelper.ROWID;
import static com.bignerdranch.android.myapplication.MySQLiteHelper.TITLE;



public class Database
{

    private MySQLiteHelper DbHelper;
    private SQLiteDatabase Db;
    private String[] allcolumns = {MySQLiteHelper.ROWID,MySQLiteHelper.TITLE,MySQLiteHelper.DATE_TIME};


    public Database(Context context)
    {
        DbHelper= new MySQLiteHelper(context);
    }

    public void open() throws SQLException
    {

        Db = DbHelper.getWritableDatabase();

    }

    public void close()
    {
        DbHelper.close();
    }

    public long createTask(String title, String DateTime)
    {
        ContentValues Values = new ContentValues();
        Values.put(TITLE, title);
        Values.put(DATE_TIME, DateTime);

        return Db.insert(MySQLiteHelper.DATABASE_TABLE, null, Values);
    }

    public boolean updateTask(long rowID, String title, String DateTime)
    {
        ContentValues Values = new ContentValues();
        Values.put(TITLE, title);
        Values.put(DATE_TIME, DateTime);

        return Db.update(MySQLiteHelper.DATABASE_TABLE, Values, ROWID + "=" + rowID, null) > 0;
    }


    public boolean deleteTask(long rowID)
    {

        return Db.delete(MySQLiteHelper.DATABASE_TABLE, ROWID + "=" + rowID, null) > 0;
    }

    public Cursor fetchTask(long rowID)
    {

        Cursor mCursor = Db.query
                (true, MySQLiteHelper.DATABASE_TABLE, allcolumns, ROWID + "=" + rowID,
                        null, null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor RetrieveAllTask()
    {

        return Db.query
                (MySQLiteHelper.DATABASE_TABLE, allcolumns,
                        null, null, null, null, null);
    }



    }

