package com.selfmadeapp.android_nas_backup.storage.database.model;

import android.provider.BaseColumns;

/**
 * Created by oruckdeschel on 22.08.2015.
 */
public final class FilesSettingsModel {

    public FilesSettingsModel() {
    }

    public static abstract class FileSettingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "fileEntry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_FILE_NAME = "file_name";
        public static final String COLUMN_NAME_SERVER_CONFIG = "server_config";
        public static final String COLUMN_NAME_LAST_SYNCED = "last_synced";
        public static final String COLUMN_NAME_CHANGES = "changes";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String BOOLEAN_TYPE = " BOOLEAN";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String TIMESTAMP_TYPE = " TIMESTAMP";
    private static final String COMMA_SEP = ",";

    public static final String FILE_TABLE_CREATE =
            "CREATE TABLE " + FileSettingsEntry.TABLE_NAME + " (" +
                    FileSettingsEntry._ID + " INTEGER PRIMARY KEY," +
                    FileSettingsEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    FileSettingsEntry.COLUMN_NAME_FILE_NAME + TEXT_TYPE + COMMA_SEP +
                    FileSettingsEntry.COLUMN_NAME_SERVER_CONFIG + INTEGER_TYPE + COMMA_SEP +
                    FileSettingsEntry.COLUMN_NAME_LAST_SYNCED + TIMESTAMP_TYPE + COMMA_SEP +
                    FileSettingsEntry.COLUMN_NAME_CHANGES + BOOLEAN_TYPE + COMMA_SEP +
                    " FOREIGN KEY(" + FileSettingsEntry.COLUMN_NAME_SERVER_CONFIG +
                    ") REFERENCES " + SyncSettingsModel.SyncSettingsEntry.TABLE_NAME +
                    "(" + SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_ENTRY_ID + "))";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FileSettingsEntry.TABLE_NAME;
}
