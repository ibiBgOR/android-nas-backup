package com.selfmadeapp.android_nas_backup.storage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.selfmadeapp.android_nas_backup.storage.database.model.SyncModel;
import com.selfmadeapp.android_nas_backup.storage.database.model.SyncSettingsModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oruckdeschel on 10.01.2015.
 */
public class DatabaseHandler {

    private Context context;
    private DatabaseManager database;

    public DatabaseHandler(Context context) {
        this.context = context;
        database = new DatabaseManager(context);
    }

    public long saveData(SyncModel model) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_ADDRESS, model.getSyncServerAddress());
        values.put(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_USER, model.getSyncServerUserName());
        values.put(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_PASS, model.getSyncServerPassword());
        values.put(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_CLIENT_FOLDER, model.getSyncClientFolder());

        return db.insert(SyncSettingsModel.SyncSettingsEntry.TABLE_NAME, null, values);
    }

    public Map<String, String> getData() {
        Map<String, String> result = new HashMap<String, String>();

        SQLiteDatabase db = database.getReadableDatabase();

        String[] projection = {
                SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_ADDRESS,
                SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_ADDRESS
        };

        Cursor c = db.query(
                SyncSettingsModel.SyncSettingsEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null, null, null, null, null
        );

        if (!c.moveToFirst()) {
            return null;
        }

        result.put(c.getString(0), c.getString(1));

        while (c.moveToNext()) {
            result.put(c.getString(0), c.getString(1));
        }

        return result;
    }
}
