package com.gagandeep.nuvococontacts.Groups;

import android.provider.BaseColumns;

public class GroupContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private GroupContract() {
    }

    /* Inner class that defines the table contents */
    public static class Group implements BaseColumns {
        public static final String TABLE_GROUP = "groups";
        public static final String COLUMN_GROUP_NAME = "group_name";
        public static final String COLUMN_MEMBERS_NUMBER = "member_number";
        public static final String COLUMN_MEMBER_COUNT = "member_count";
        public static final String COLUMN_MEMBER_NAME = "member_name";


    }
}
