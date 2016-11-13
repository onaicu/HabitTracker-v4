package mobi.storedot.habittracking;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import mobi.storedot.habittracking.data.HabitContract.HabitEntry;
import mobi.storedot.habittracking.data.HabitDbHelper;

/**
 * Displays list of habits that were entered and stored in the app.
 */

public class CatalogActivity extends AppCompatActivity {
    /**
     * Database helper that will provide us access to the database
     */
    private HabitDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }

        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new HabitDbHelper(this);
    }

    /**
     * Here I am overriding the on start method/
     * When the activity starts again- So this means after it comes back after the user has clicked
     * Save in the editor activity, the list would refresh with the new habit in the database.
     */
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     *To read from a database, use the query() method, passing it he selection criteria and desired columns.
     * The method combines elements of insert() and update(), except the column list defines
     * the data I want to fetch, rather than the data to insert.
     * The results of the query are returned in a Cursor object.
     */

    private Cursor readDB() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //To start writing the query method: write first any projection and any selection needed.
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_NAME,
                HabitEntry.COLUMN_HABIT_TODO,
                HabitEntry.COLUMN_HABIT_SCHEDULE,
                HabitEntry.COLUMN_HABIT_REPETITION
        };

        // Filter results WHERE "Schedule" = 'workdays'
        String selection = HabitEntry.COLUMN_HABIT_SCHEDULE + "=?";
        String[] selectionArgs = new String[] { String.valueOf(HabitEntry.SCHEDULE_WORKDAYS) };

        /** Perform a query on the habits table .
         * The Android SQLite query method returns a Cursor object containing the results of the query
         */

        Cursor cursor = db.query(
                HabitEntry.TABLE_NAME, // The table to query
                projection, // The columns to return
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // Don't group the rows
                null, // Don't filter by row
                null); // The sort order

        return cursor;
    }


    private void displayDatabaseInfo() {

        Cursor cursor = readDB();

        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {
            /** Create a header in the Text View that looks like this:
             *       The habits table contains <number of rows in Cursor> habits.
             * _id-HabbitName - to do - schedule-repetition
             * In the while loop below, iterate through the rows of the cursor and display
             * the information from each column in this order.
             */

            displayView.setText("The habits table contains " + cursor.getCount() + " habits.\n\n");
            displayView.append(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_HABIT_NAME + " - " +
                    HabitEntry.COLUMN_HABIT_TODO + " - " +
                    HabitEntry.COLUMN_HABIT_SCHEDULE + " - " +
                    HabitEntry.COLUMN_HABIT_REPETITION + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_NAME);
            int todoColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_TODO);
            int scheduleColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_SCHEDULE);
            int repetitionColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_REPETITION);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentTodo = cursor.getString(todoColumnIndex);
                int currentSchedule = cursor.getInt(scheduleColumnIndex);
                int currentRepetition = cursor.getInt(repetitionColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentTodo + " - " +
                        currentSchedule + " - " +
                        currentRepetition));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    // After including the method in the switch we need to define it as private method
    // and void(does not return any value).

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertHabit() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        // and the habit to care for the pet (PetCare) are the values.
        //Create ContentValues type object called values.

        ContentValues values = new ContentValues();
        //use ContentValues put method to store each for the key value pairs
        values.put(HabitEntry.COLUMN_HABIT_NAME, "Petcare");
        values.put(HabitEntry.COLUMN_HABIT_TODO, "dog");
        values.put(HabitEntry.COLUMN_HABIT_SCHEDULE, HabitEntry.SCHEDULE_WEEKEND);
        values.put(HabitEntry.COLUMN_HABIT_REPETITION, 5);

        // Insert a new row for habit PetCare in the database, returning the ID of that new row.
        // The first argument for db.insert() is the habits table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for PetCare.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        Log.v("CatalogActivity", "NewRowID" + newRowId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertHabit();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



