package com.habrman.smolovcalculator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.habrman.smolovcalculator.db.TaskContract;
import com.habrman.smolovcalculator.db.TaskDbHelper;

import java.util.ArrayList;

import static android.R.attr.id;
import static android.R.id.message;
import static com.habrman.smolovcalculator.R.id.editTextIncrement;
import static com.habrman.smolovcalculator.R.id.editTextMax;

public class SmolovJrActivity extends AppCompatActivity {
    private TaskDbHelper mHelper;
    private long currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new TaskDbHelper(this);
        setContentView(R.layout.activity_smolov_jr);

        Intent intent = getIntent();
        if(intent.hasExtra(MainActivity.EXTRA_MESSAGE)) {
            String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
            String[] messages = message.split(":");

            TextView editTextName = (TextView) findViewById(R.id.editTextName);
            editTextName.setText(messages[1]);
            TextView editTextMax = (TextView) findViewById(R.id.editTextMax);
            editTextMax.setText(messages[2]);
            TextView editTextIncrement = (TextView) findViewById(R.id.editTextIncrement);
            editTextIncrement.setText(messages[3]);

            currentId = Integer.parseInt(messages[0]);
            syncCheckBoxes();
        } else {
            String workoutName = ((EditText) findViewById(R.id.editTextName)).getText().toString();
            String workoutMax = ((EditText) findViewById(editTextMax)).getText().toString();
            String workoutIncrement = ((EditText) findViewById(editTextIncrement)).getText().toString();

            ContentValues values = new ContentValues();
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_TITLE, workoutName);
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_MAX, workoutMax);

            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_INCREMENT, workoutIncrement);
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W1D1, "0");
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W1D2, "0");
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W1D3, "0");
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W1D4, "0");
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W2D1, "0");
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W2D2, "0");
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W2D3, "0");
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W2D4, "0");
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W3D1, "0");
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W3D2, "0");
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W3D3, "0");
            values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W3D4, "0");

            currentId = mHelper.addTask(values);
        }

        EditText editTextMax = (EditText) findViewById(R.id.editTextMax);
        editTextMax.addTextChangedListener(filterTextWatcher);
        EditText editTextIncrement = (EditText) findViewById(R.id.editTextIncrement);
        editTextIncrement.addTextChangedListener(filterTextWatcher);

        updateWeights();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.workout_plan_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void syncCheckBoxes() {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM " + TaskContract.WorkoutEntry.TABLE +
                " WHERE " + TaskContract.WorkoutEntry._ID + "=?", new String[]{String.valueOf(currentId)});
        /*
        Cursor cursor = db.query(TaskContract.WorkoutEntry.TABLE,
                new String[]{TaskContract.WorkoutEntry._ID,
                        TaskContract.WorkoutEntry.COL_WORKOUT_W1D1,
                        TaskContract.WorkoutEntry.COL_WORKOUT_W1D2,
                        TaskContract.WorkoutEntry.COL_WORKOUT_W1D3,
                        TaskContract.WorkoutEntry.COL_WORKOUT_W1D4,
                        TaskContract.WorkoutEntry.COL_WORKOUT_W2D1,
                        TaskContract.WorkoutEntry.COL_WORKOUT_W2D2,
                        TaskContract.WorkoutEntry.COL_WORKOUT_W2D3,
                        TaskContract.WorkoutEntry.COL_WORKOUT_W2D4,
                        TaskContract.WorkoutEntry.COL_WORKOUT_W3D1,
                        TaskContract.WorkoutEntry.COL_WORKOUT_W3D2,
                        TaskContract.WorkoutEntry.COL_WORKOUT_W3D3,
                        TaskContract.WorkoutEntry.COL_WORKOUT_W3D4},
                TaskContract.WorkoutEntry._ID + " = ?",
                new String[]{String.valueOf(currentId)},
                null, null, null);*/

        cursor.moveToFirst();
        int idx_t = cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_TITLE);

        String idx_w1d1 = cursor.getString(cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_W1D1));
        String idx_w1d2 = cursor.getString(cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_W1D2));
        String idx_w1d3 = cursor.getString(cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_W1D3));
        String idx_w1d4 = cursor.getString(cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_W1D4));
        String idx_w2d1 = cursor.getString(cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_W2D1));
        String idx_w2d2 = cursor.getString(cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_W2D2));
        String idx_w2d3 = cursor.getString(cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_W2D3));
        String idx_w2d4 = cursor.getString(cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_W2D4));
        String idx_w3d1 = cursor.getString(cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_W3D1));
        String idx_w3d2 = cursor.getString(cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_W3D2));
        String idx_w3d3 = cursor.getString(cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_W3D3));
        String idx_w3d4 = cursor.getString(cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_W3D4));

        if(Integer.parseInt(idx_w1d1) > 0) ((CheckBox)findViewById(R.id.checkBoxW1D1)).setChecked(true);
        if(Integer.parseInt(idx_w1d2) > 0) ((CheckBox)findViewById(R.id.checkBoxW1D2)).setChecked(true);
        if(Integer.parseInt(idx_w1d3) > 0) ((CheckBox)findViewById(R.id.checkBoxW1D3)).setChecked(true);
        if(Integer.parseInt(idx_w1d4) > 0) ((CheckBox)findViewById(R.id.checkBoxW1D4)).setChecked(true);
        if(Integer.parseInt(idx_w2d1) > 0) ((CheckBox)findViewById(R.id.checkBoxW2D1)).setChecked(true);
        if(Integer.parseInt(idx_w2d2) > 0) ((CheckBox)findViewById(R.id.checkBoxW2D2)).setChecked(true);
        if(Integer.parseInt(idx_w2d3) > 0) ((CheckBox)findViewById(R.id.checkBoxW2D3)).setChecked(true);
        if(Integer.parseInt(idx_w2d4) > 0) ((CheckBox)findViewById(R.id.checkBoxW2D4)).setChecked(true);
        if(Integer.parseInt(idx_w3d1) > 0) ((CheckBox)findViewById(R.id.checkBoxW3D1)).setChecked(true);
        if(Integer.parseInt(idx_w3d2) > 0) ((CheckBox)findViewById(R.id.checkBoxW3D2)).setChecked(true);
        if(Integer.parseInt(idx_w3d3) > 0) ((CheckBox)findViewById(R.id.checkBoxW3D3)).setChecked(true);
        if(Integer.parseInt(idx_w3d4) > 0) ((CheckBox)findViewById(R.id.checkBoxW3D4)).setChecked(true);

        cursor.close();
        db.close();
    }

    private void updateWeights() {
        String maxString = ((EditText) findViewById(R.id.editTextMax)).getText().toString();
        String incrementString = ((EditText) findViewById(R.id.editTextIncrement)).getText().toString();

        if(maxString.length() > 0 && incrementString.length() > 0) {
            float maxWeight = Float.valueOf(maxString);
            float increment = Float.valueOf(incrementString);
            String w1_str = "textViewWeightW1D";
            String w2_str = "textViewWeightW2D";
            String w3_str = "textViewWeightW3D";
            float d1_percentage = 0.7f;

            for(int i = 0; i < 4; ++i) {
                int week = i + 1;
                String w1di_str = w1_str + week;
                String w2di_str = w2_str + week;
                String w3di_str = w3_str + week;

                float w1_weight = maxWeight * (d1_percentage + 0.05f * i);
                float w2_weight = w1_weight + increment;
                float w3_weight = w1_weight + increment * 2;

                SetWeight(w1di_str, w1_weight);
                SetWeight(w2di_str, w2_weight);
                SetWeight(w3di_str, w3_weight);
            }
        }
    }

    private void SetWeight(String textViewId, float weight) {
        int id = getResources().getIdentifier(textViewId, "id", getPackageName());
        TextView textView = (TextView) findViewById(id);
        textView.setText(String.format("%.2f", weight));;
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateWeights();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_workout:

                String workoutName = ((EditText) findViewById(R.id.editTextName)).getText().toString();
                String workoutMax = ((EditText) findViewById(R.id.editTextMax)).getText().toString();
                String workoutIncrement = ((EditText) findViewById(R.id.editTextIncrement)).getText().toString();

                String w1d1Checked = ((CheckBox)findViewById(R.id.checkBoxW1D1)).isChecked() ? "1" : "0";
                String w1d2Checked = ((CheckBox)findViewById(R.id.checkBoxW1D2)).isChecked() ? "1" : "0";
                String w1d3Checked = ((CheckBox)findViewById(R.id.checkBoxW1D3)).isChecked() ? "1" : "0";
                String w1d4Checked = ((CheckBox)findViewById(R.id.checkBoxW1D4)).isChecked() ? "1" : "0";
                String w2d1Checked = ((CheckBox)findViewById(R.id.checkBoxW2D1)).isChecked() ? "1" : "0";
                String w2d2Checked = ((CheckBox)findViewById(R.id.checkBoxW2D2)).isChecked() ? "1" : "0";
                String w2d3Checked = ((CheckBox)findViewById(R.id.checkBoxW2D3)).isChecked() ? "1" : "0";
                String w2d4Checked = ((CheckBox)findViewById(R.id.checkBoxW2D4)).isChecked() ? "1" : "0";
                String w3d1Checked = ((CheckBox)findViewById(R.id.checkBoxW3D1)).isChecked() ? "1" : "0";
                String w3d2Checked = ((CheckBox)findViewById(R.id.checkBoxW3D2)).isChecked() ? "1" : "0";
                String w3d3Checked = ((CheckBox)findViewById(R.id.checkBoxW3D3)).isChecked() ? "1" : "0";
                String w3d4Checked = ((CheckBox)findViewById(R.id.checkBoxW3D4)).isChecked() ? "1" : "0";

                ContentValues values = new ContentValues();
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_TITLE, workoutName);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_MAX, workoutMax);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_INCREMENT, workoutIncrement);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W1D1, w1d1Checked);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W1D2, w1d2Checked);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W1D3, w1d3Checked);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W1D4, w1d4Checked);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W2D1, w2d1Checked);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W2D2, w2d2Checked);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W2D3, w2d3Checked);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W2D4, w2d4Checked);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W3D1, w3d1Checked);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W3D2, w3d2Checked);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W3D3, w3d3Checked);
                values.put(TaskContract.WorkoutEntry.COL_WORKOUT_W3D4, w3d4Checked);

                mHelper.updateTask(values, currentId);
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
