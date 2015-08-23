package com.selfmadeapp.android_nas_backup.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.selfmadeapp.android_nas_backup.storage.database.DatabaseHandler;
import com.selfmadeapp.android_nas_backup.storage.database.model.FileModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oruckdeschel on 23.08.2015.
 */
public class FileFetcherService extends Service {

    public static final String EXTRA_CLIENT_FOLDER = "EXTRA_CLIENT_FOLDER";
    public static final String EXTRA_SERVER_LOCATION = "EXTRA_SERVER_LOCATION";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String clientFolderName = intent.getStringExtra(EXTRA_CLIENT_FOLDER);
        String serverFolderName = intent.getStringExtra(EXTRA_SERVER_LOCATION);

        File rootFolder = new File(clientFolderName);
        List<File> fileList = getListFiles(rootFolder);

        DatabaseHandler handler = new DatabaseHandler(this);

        for (File file : fileList) {
            handler.saveFilesData(new FileModel(file.getAbsolutePath(),
                    new DatabaseHandler(this).getSyncLocation(clientFolderName, serverFolderName),
                    -1L, FileModel.FILE_WAS_NOT_SYNCED));
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                inFiles.add(file);
            }
        }
        return inFiles;
    }
}
