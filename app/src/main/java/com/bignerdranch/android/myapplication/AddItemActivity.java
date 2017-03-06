package com.bignerdranch.android.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddItemActivity extends Activity {


    private static final int DATE_PICKER_DIALOG = 0;
    private static final int TIME_PICKER_DIALOG = 1;


    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy kk:mm";

    private EditText mTitle;
    private TextView mDate;
    private TextView mTime;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mConfirmButton;
    private Long mRowId;
    private Calendar mCalendar;
    private Database mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_item);

        mDbHelper = new Database(this);

        mCalendar = Calendar.getInstance();

        mTitle = (EditText) findViewById(R.id.title);
        mDate = (TextView) findViewById(R.id.date);
        mTime = (TextView) findViewById(R.id.time);
        mDateButton = (Button) findViewById(R.id.btn_date);
        mTimeButton = (Button) findViewById(R.id.btn_time);
        mConfirmButton = (Button) findViewById(R.id.confirm);

        mRowId = savedInstanceState != null ? savedInstanceState.getLong(MySQLiteHelper.ROWID)
                : null;
        ButtonTextChange();

    }

    //ensure that the items on the listview which added previously is still remain there unless is deleted manually by user
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putLong(MySQLiteHelper.ROWID, mRowId);
    }

    //date and time picker dialog is created, and is able to be adjust,up to the user
    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case DATE_PICKER_DIALOG:

                DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateButtonText();

            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
            return datePicker;

            case TIME_PICKER_DIALOG:

                TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener()
                {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mCalendar.set(Calendar.MINUTE, minute);
                        updateTimeButtonText();

                    }
                }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);

                return timePicker;

        }
        return super.onCreateDialog(id);
    }


    //once user already select the date/time and press ok,the date/time will be display according to the user's selection

    private void ButtonTextChange()
    {

        mDateButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                showDialog(DATE_PICKER_DIALOG);
            }
        });


        mTimeButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                showDialog(TIME_PICKER_DIALOG);
            }
        });

        //the user press confirm button once is in adding stage or editing stage

        mConfirmButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {

                String title = mTitle.getText().toString();
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
                String DateTime = dateTimeFormat.format(mCalendar.getTime());

                if (mRowId == null)
                {
                    //the title, date/time selected will be entered into the database
                    long id = mDbHelper.createTask(title, DateTime);
                    //as the first item entered, the row id will begins with 1 instead of 0
                    if (id > 0)
                    {
                        mRowId = id;
                    }
                }
                else
                {
                    mDbHelper.updateTask(mRowId, title, DateTime);
                }

                setResult(RESULT_OK); //operation succeed
                Toast.makeText(AddItemActivity.this, getString(R.string.save), Toast.LENGTH_SHORT).show();
                finish(); //leads back to the main activity page automatically
            }

        });

        updateDateButtonText();

        updateTimeButtonText();


    }

    //after the first time of adding an item which already display in the listview and would want to do for the second time, the date/time will be back to the current date/time
    private void updateDateButtonText()
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(mCalendar.getTime());
        mDateButton.setText(dateForButton);
    }

    private void updateTimeButtonText()
    {

        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        String timeForButton = timeFormat.format(mCalendar.getTime());
        mTimeButton.setText(timeForButton);
    }

    // when wanted to leave the app and ensure the database is closed
    @Override
    protected void onPause()
    {
        super.onPause();
        mDbHelper.close();
    }

    // when come back to the app and ensure the database is opened and the listview display according to the date order
    @Override
    protected void onResume()
    {
        super.onResume();
        mDbHelper.open();


    }






}
