package com.bignerdranch.android.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    public static final String DATABASE_TABLE = "tasks";
    private static final int DATABASE_VERSION = 1;

    public static final String TITLE = "title";
    public static final String DATE_TIME = "date_time";
    public static final String ROWID = "_id";

    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " ("
                    + ROWID + " integer primary key autoincrement, "
                    + TITLE + " text not null, "
                    + DATE_TIME + " text not null);";

    public MySQLiteHelper(Context context)
    {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);

    }


}
