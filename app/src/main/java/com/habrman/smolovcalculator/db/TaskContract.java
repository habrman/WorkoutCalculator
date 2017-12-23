package com.habrman.smolovcalculator.db;

import android.provider.BaseColumns;

/**
 * Created by david on 10/20/17.
 */

public class TaskContract {
    public static final String DB_NAME = "com.habrman.smolovcalculator.db";
    public static final int DB_VERSION = 21;

    public class WorkoutEntry implements BaseColumns {
        public static final String TABLE = "workouts";

        public static final String COL_WORKOUT_TITLE = "title";
        public static final String COL_WORKOUT_MAX = "max";

        public static final String COL_WORKOUT_INCREMENT = "increment";

        public static final String COL_WORKOUT_W1D1 = "w1d1_boolean";
        public static final String COL_WORKOUT_W1D2 = "w1d2_boolean";
        public static final String COL_WORKOUT_W1D3 = "w1d3_boolean";
        public static final String COL_WORKOUT_W1D4 = "w1d4_boolean";
        public static final String COL_WORKOUT_W2D1 = "w2d1_boolean";
        public static final String COL_WORKOUT_W2D2 = "w2d2_boolean";
        public static final String COL_WORKOUT_W2D3 = "w2d3_boolean";
        public static final String COL_WORKOUT_W2D4 = "w2d4_boolean";
        public static final String COL_WORKOUT_W3D1 = "w3d1_boolean";
        public static final String COL_WORKOUT_W3D2 = "w3d2_boolean";
        public static final String COL_WORKOUT_W3D3 = "w3d3_boolean";
        public static final String COL_WORKOUT_W3D4 = "w3d4_boolean";
    }
}
