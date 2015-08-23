package com.selfmadeapp.android_nas_backup.storage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gc.materialdesign.views.ButtonFloatSmall;
import com.selfmadeapp.android_nas_backup.storage.database.model.FileModel;
import com.selfmadeapp.android_nas_backup.storage.database.model.FilesSettingsModel;
import com.selfmadeapp.android_nas_backup.storage.database.model.SyncModel;
import com.selfmadeapp.android_nas_backup.storage.database.model.SyncSettingsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public long saveSyncData(SyncModel model) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_ADDRESS, model.getSyncServerAddress());
        values.put(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_USER, model.getSyncServerUserName());
        values.put(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_PASS, model.getSyncServerPassword());
        values.put(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_CLIENT_FOLDER, model.getSyncClientFolder());

        return db.insert(SyncSettingsModel.SyncSettingsEntry.TABLE_NAME, null, values);
    }

    public Map<String, String> getSyncData() {
        Map<String, String> result = new HashMap<String, String>();

        SQLiteDatabase db = database.getReadableDatabase();

        String[] projection = {
                SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_ADDRESS,
                SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_CLIENT_FOLDER
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

    public long saveFilesData(FileModel model) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FilesSettingsModel.FileSettingsEntry.COLUMN_NAME_FILE_NAME, model.getFileName());
        values.put(FilesSettingsModel.FileSettingsEntry.COLUMN_NAME_SERVER_CONFIG, model.getServerConfig());
        values.put(FilesSettingsModel.FileSettingsEntry.COLUMN_NAME_LAST_SYNCED, model.getLastSynced());
        values.put(FilesSettingsModel.FileSettingsEntry.COLUMN_NAME_CHANGES, FileModel.FILE_WAS_NOT_SYNCED);

        return db.insert(FilesSettingsModel.FileSettingsEntry.TABLE_NAME, null, values);
    }

    public List<FileModel> getAllFilesData() {
        List<FileModel> result = new ArrayList<>();

        SQLiteDatabase db = database.getReadableDatabase();

        String[] projection = {
                FilesSettingsModel.FileSettingsEntry.COLUMN_NAME_FILE_NAME,
                FilesSettingsModel.FileSettingsEntry.COLUMN_NAME_SERVER_CONFIG,
                FilesSettingsModel.FileSettingsEntry.COLUMN_NAME_LAST_SYNCED,
                FilesSettingsModel.FileSettingsEntry.COLUMN_NAME_CHANGES
        };

        Cursor c = db.query(
                FilesSettingsModel.FileSettingsEntry.TABLE_NAME,    // The table to query
                projection,                                         // The columns to return
                null, null, null, null, null
        );

        if (!c.moveToFirst()) {
            return null;
        }

        while (c.moveToNext()) {
            result.add(new FileModel(c.getString(0), c.getInt(1), c.getLong(2), c.getShort(3)));
        }

        return result;
    }

    public List<FileModel> getFilesData(int serverConfig) {
        return null;
    }

    public List<FileModel> getFileData(boolean changes) {
        return null;
    }
}
