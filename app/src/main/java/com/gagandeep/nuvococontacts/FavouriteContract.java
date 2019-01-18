package com.gagandeep.nuvococontacts;

import android.provider.BaseColumns;

public class FavouriteContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FavouriteContract() {
    }

    /* Inner class that defines the table contents */
    public static class Favourite implements BaseColumns {
        public static final String TABLE_NAME = "favourite";
        public static final String COLUMN_LOCAL_NAME = "name";
        public static final String COLUMN_LOCAL_PHONE = "phoneno";
    }
}
