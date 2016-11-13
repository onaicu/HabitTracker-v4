package mobi.storedot.habittracking;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import mobi.storedot.habittracking.data.HabitContract.HabitEntry;
import mobi.storedot.habittracking.data.HabitDbHelper;

/**
 * Allows user to create a new habit or edit an existing one.
 */

public class EditorActivity extends AppCompatActivity {

    /**
     * Schedule for the habit. The possible valid values are in the HabitContract.java file:
     * {@link HabitEntry#HABIT_UNKNOWN}, {@link HabitEntry#HABIT_WORKDAYS}, or
     * {@link HabitEntry#HABIT_WEEKEND}.
     */


    /** EditText field to enter the habit's name */
    private EditText mNameEditText;

    /** EditText field to enter the habit's To Do */
    private EditText mToDoEditText;

    /** EditText field to enter the habit's repetition */
    private EditText mRepetitionEditText;

    /** EditText field to enter the habit's schedule */
    private Spinner mScheduleSpinner;

    /**
     * Schedule of the habit. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mSchedule = HabitEntry.SCHEDULE_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_habit_name);
        mToDoEditText = (EditText) findViewById(R.id.edit_habit_ToDo);
        mRepetitionEditText = (EditText) findViewById(R.id.edit_habit_repetition);
        mScheduleSpinner = (Spinner) findViewById(R.id.spinner_schedule);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the habit.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_schedule_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mScheduleSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mScheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.schedule_workdays))) {
                        mSchedule = HabitEntry.SCHEDULE_WORKDAYS; // Male
                    } else if (selection.equals(getString(R.string.schedule_weekend))) {
                        mSchedule = HabitEntry.SCHEDULE_WEEKEND; // Female
                    } else {
                        mSchedule = HabitEntry.SCHEDULE_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSchedule = HabitEntry.SCHEDULE_UNKNOWN; // Unknown
            }
        });
    }

    /**
     * Get user input from editor and save new pet into database.
     */
    private void insertHabit() {

        //get text from the define edit text field above
        //add .trim method to eliminate any leading and trailing white space
        //from the string that we got.
        /**
         * create a variable so that we can use the string, lets create a string named nameString
         So now if I want to make a new content values object, I can use the key value pair
         where the key is the name column and the value is the EditText field.
         */

        String nameString = mNameEditText.getText().toString().trim();
        String todoString = mToDoEditText.getText().toString().trim();
        String repetitionString = mRepetitionEditText.getText().toString().trim();
        int repetition = Integer.parseInt(repetitionString);


        // Create database helper
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, nameString);
        values.put(HabitEntry.COLUMN_HABIT_TODO, todoString);
        values.put(HabitEntry.COLUMN_HABIT_SCHEDULE, mSchedule);
        values.put(HabitEntry.COLUMN_HABIT_REPETITION, repetition);

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving habit", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                insertHabit();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
