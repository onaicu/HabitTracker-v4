package mobi.storedot.habittracking.data;


import android.provider.BaseColumns;

/**
 * API Contract for the Habits app.
 */

public final class HabitContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private HabitContract() {}

    /**
     * Inner class that defines constant values for the habits database table.
     * Each entry in the table represents a single habit.
     */
    public static final class HabitEntry implements BaseColumns {

        /** Name of database table for habit */
        public final static String TABLE_NAME = "habits";

        /**
         * Unique ID number for the habit (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the habit.
         *
         * Type: TEXT
         */
        public final static String COLUMN_HABIT_NAME ="name";

        /**
         * To do for the habit.
         *
         * Type: TEXT
         */
        public final static String COLUMN_HABIT_TODO = "To_Do";

        /**
         * Schedule for the habit/when it happens during the week
         *
         * The only possible values are {@link #SCHEDULE_UNKNOWN}, {@link #SCHEDULE_WORKDAYS},
         * or {@link #SCHEDULE_UNKNOWN}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_HABIT_SCHEDULE = "schedule";

        /**
         * Repetition of the habit/how many times it happens.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_HABIT_REPETITION = "repetition";

        /**
         * Possible values for the schedule of the habit
         */
        public static final int SCHEDULE_UNKNOWN = 0;
        public static final int SCHEDULE_WORKDAYS = 1;
        public static final int SCHEDULE_WEEKEND = 2;
    }

}