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

    /**
     * Get the server, which is assigned to the serverConfigId.
     * @param serverConfigId The Id from the database.
     * @return Map include the whole server config.
     */
    public Map<String, String> getServerConfig(int serverConfigId) {
        SQLiteDatabase db = database.getReadableDatabase();

        String[] projection = {
                SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_ADDRESS,
                SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_CLIENT_FOLDER,
                SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_USER,
                SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_PASS
        };

        String where = SyncSettingsModel.SyncSettingsEntry._ID + " = " + serverConfigId;

        Cursor c = db.query(
                SyncSettingsModel.SyncSettingsEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                where, null, null, null, null
        );

        if (!c.moveToFirst()) {
            return null;
        }

        Map<String, String> result = new HashMap<>();
        result.put(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_ADDRESS, c.getString(0));
        result.put(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_CLIENT_FOLDER, c.getString(1));
        result.put(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_USER, c.getString(2));
        result.put(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_PASS, c.getString(3));

        return result;
    }

    public long saveFilesData(FileModel model) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FilesSettingsModel.FileSettingsEntry.COLUMN_NAME_FILE_NAME, model.getFileName());
        values.put(FilesSettingsModel.FileSettingsEntry.COLUMN_NAME_SERVER_CONFIG, model.getServerConfig());
        values.put(FilesSettingsModel.FileSettingsEntry.COLUMN_NAME_LAST_SYNCED, model.getLastSynced());
        values.put(FilesSettingsModel.FileSettingsEntry.COLUMN_NAME_CHANGES, model.wereChanges());

        return db.insert(FilesSettingsModel.FileSettingsEntry.TABLE_NAME, null, values);
    }

    public long saveFilesData(List<FileModel> files) {
        long worstCase = 0;

        for(FileModel file : files) {
            long curCase = saveFilesData(file);
            worstCase = curCase > worstCase ? curCase : worstCase;
        }

        return worstCase;
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

        result.add(new FileModel(c.getString(0), c.getInt(1), c.getLong(2), c.getShort(3)));

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

    public int getSyncLocation(String clientFolderName, String serverFolderName) {
        SQLiteDatabase db = database.getReadableDatabase();

        String[] projection = {
                FilesSettingsModel.FileSettingsEntry._ID
        };

        String where = SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_CLIENT_FOLDER + " = '" + clientFolderName +
                "' AND " + SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_ADDRESS + " = '" + serverFolderName + "'";

        Cursor c = db.query(
                SyncSettingsModel.SyncSettingsEntry.TABLE_NAME,
                projection, where, null, null, null, null
        );

        if (!c.moveToFirst()) {
            return -1;
        }

        return c.getInt(0);
    }
}
