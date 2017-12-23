// https://www.sitepoint.com/starting-android-development-creating-todo-app/

package com.habrman.smolovcalculator;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.habrman.smolovcalculator.db.TaskContract;
import com.habrman.smolovcalculator.db.TaskDbHelper;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_MESSAGE = "com.habrman.smolovcalculator.MESSAGE";
    private TaskDbHelper mHelper;
    private ListView mWorkoutListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWorkoutListView = (ListView) findViewById(R.id.list_workout);

        mHelper = new TaskDbHelper(this);
        updateUI();
    }

    private void updateUI() {
        ArrayList<String> taskList = mHelper.getTaskList();

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_in_progress,
                    R.id.task_title,
                    taskList);
            mWorkoutListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_workout:
                Intent intent = new Intent(this, SmolovJrActivity.class);
                startActivityForResult(intent, 1);
                /*
                final EditText workoutNameText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new workout")
                        .setView(workoutNameText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String workoutName = String.valueOf(workoutNameText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_TITLE, workoutName);
                                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_MAX, 120.5f);
                                db.insert(TaskContract.WorkoutEntry.TABLE,
                                        null,
                                        values);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
                */
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateUI();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String[] tasks = String.valueOf(taskTextView.getText()).split(":");

        mHelper.deleteTask(tasks[0]);
        updateUI();
    }

    public void openWorkout(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        String[] tasks = task.split(":");
        task = tasks[0];

        String message = mHelper.getMessage(task);

        Intent intent = new Intent(this, SmolovJrActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivityForResult(intent, 1);
    }
}
