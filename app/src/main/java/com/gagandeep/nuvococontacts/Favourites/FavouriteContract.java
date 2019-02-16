package com.gagandeep.nuvococontacts.Favourites;

import android.provider.BaseColumns;

public class FavouriteContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FavouriteContract() {
    }

    /* Inner class that defines the table contents */
    public static class Favourite implements BaseColumns {
        public static final String TABLE_FAV = "favourite";
        public static final String COLUMN_FAV_NAME = "name";
        public static final String COLUMN_FAV_PHONE = "phoneno";
    }
}
