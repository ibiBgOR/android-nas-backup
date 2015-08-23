package com.selfmadeapp.android_nas_backup.storage.database.model;

import android.provider.BaseColumns;

/**
 * Created by oruckdeschel on 10.01.2015.
 */
public final class SyncSettingsModel {

    public SyncSettingsModel() {
    }

    public static abstract class SyncSettingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "syncEntry";
        public static final String COLUMN_NAME_SERVER_ADDRESS = "serveraddress";
        public static final String COLUMN_NAME_SERVER_USER = "serveruser";
        public static final String COLUMN_NAME_SERVER_PASS = "serverpass";
        public static final String COLUMN_NAME_CLIENT_FOLDER = "clientfolder";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String UNIQUE_KEY_CONSTRAINT_NAME = "UK_STC_SERVER_CLIENT";

    public static final String SYNC_TABLE_CREATE =
            "CREATE TABLE " + SyncSettingsEntry.TABLE_NAME + " (" +
                    SyncSettingsEntry._ID + " INTEGER PRIMARY KEY," +
                    SyncSettingsEntry.COLUMN_NAME_SERVER_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    SyncSettingsEntry.COLUMN_NAME_SERVER_USER + TEXT_TYPE + COMMA_SEP +
                    SyncSettingsEntry.COLUMN_NAME_SERVER_PASS + TEXT_TYPE + COMMA_SEP +
                    SyncSettingsEntry.COLUMN_NAME_CLIENT_FOLDER + TEXT_TYPE + COMMA_SEP +
                    " CONSTRAINT " + UNIQUE_KEY_CONSTRAINT_NAME + " UNIQUE (" +
                    SyncSettingsEntry.COLUMN_NAME_SERVER_ADDRESS + ", " +
                    SyncSettingsEntry.COLUMN_NAME_CLIENT_FOLDER + "))";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SyncSettingsEntry.TABLE_NAME;
}
