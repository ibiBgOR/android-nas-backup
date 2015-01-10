package com.selfmadeapp.android_nas_backup.storage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.selfmadeapp.android_nas_backup.storage.database.model.SyncSettingsModel;

/**
 * Created by oruckdeschel on 10.01.2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NAS-Backup.db";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SyncSettingsModel.SYNC_TABLE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: How can I save the Data and make it possible to import it to the nextGen Version.
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: @see onUpgrade
    }

}
