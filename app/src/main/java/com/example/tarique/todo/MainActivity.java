package com.example.tarique.todo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.text.style.TtsSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import db.TaskContract;
import db.TaskDBHelper;
import com.example.tarique.todo.NavigationDrawerFragment;


public class MainActivity extends AppCompatActivity {
    private TaskDBHelper helper;
    private ListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUI();





        SQLiteDatabase sqlDB = new TaskDBHelper(this).getWritableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns.TASK},
                null, null, null, null, null);

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Log.d("MainActivity cursor",
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    TaskContract.Columns.TASK)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
               // customDialogCoupon(MainActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
           //     builder.setDate("ddd");


           //     builder.setView(Date);
                builder.setTitle("Add a task");
             //   String Date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
             //   builder.setMessage(Date);
            //    Calendar c = Calendar.getInstance();
             //   int seconds = c.get(Calendar.SECOND);
              //  builder.setMessage(seconds);
             //   builder.hashCode();

                DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd hhmmss");
                dateFormatter.setLenient(false);
                Date today = new Date();
                String s = dateFormatter.format(today);
               // final TextView textView = new TextView(this);
                builder.setMessage(s);


                builder.setMessage("What do you want to do?");
                final EditText inputField = new EditText(this);
                builder.setView(inputField);

                builder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String task = inputField.getText().toString();
                                Log.d("MainActivity",task);

                                helper = new TaskDBHelper(MainActivity.this);
                                SQLiteDatabase db = helper.getWritableDatabase();
                                ContentValues values = new ContentValues();

                                values.clear();
                                values.put(TaskContract.Columns.TASK, task);

                                db.insertWithOnConflict(TaskContract.TABLE, null, values,
                                        SQLiteDatabase.CONFLICT_IGNORE);
                                   updateUI();
                            }
                        });

                builder.setNegativeButton("Cancel",null);

                builder.create().show();
                return true;

            default:
                return false;
        }
    }

    private void updateUI() {
        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                null,null,null,null,null);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.task_view,
                cursor,
                new String[] { TaskContract.Columns.TASK},
                new int[] { R.id.taskTextView},
                0
        );
   //    this.setListAdapter(listAdapter);
        // Display the list view
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(listAdapter);
    }
    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.TASK,
                task);


        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }
    public void customDialogCoupon(Context context) {
        final Dialog dialogCoupon = new Dialog(context);
        dialogCoupon.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCoupon.setContentView(R.layout.activity_dialogue);
        dialogCoupon.setCanceledOnTouchOutside(true);






        dialogCoupon.show();

    }


}
