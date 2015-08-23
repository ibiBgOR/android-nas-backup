package com.selfmadeapp.android_nas_backup.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.selfmadeapp.android_nas_backup.network.NetUtils;
import com.selfmadeapp.android_nas_backup.network.mover.FileMover;
import com.selfmadeapp.android_nas_backup.network.mover.FileMoverImpl;
import com.selfmadeapp.android_nas_backup.network.mover.model.UploadFile;
import com.selfmadeapp.android_nas_backup.storage.database.DatabaseHandler;
import com.selfmadeapp.android_nas_backup.storage.database.model.FileModel;
import com.selfmadeapp.android_nas_backup.storage.database.model.SyncSettingsModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jcifs.smb.NtlmPasswordAuthentication;

/**
 * Created by oruckdeschel on 23.08.2015.
 */
public class FileUploadService extends Service {

    private DatabaseHandler handler;
    private FileMover mover;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handler = new DatabaseHandler(this);
        mover = new FileMoverImpl(this);

        for (FileModel file : handler.getAllFilesData()) {

            File source = new File(file.getFileName());
            Map<String, String> serverConfig = handler.getServerConfig(file.getServerConfig());

            String baseDestination = serverConfig.get(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_ADDRESS);
            baseDestination = baseDestination.charAt(baseDestination.length() - 1) == '/' ?
                    baseDestination : baseDestination.concat("/");

            String destinationSubFolder = file.getFileName()
                    .replace(serverConfig.get(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_CLIENT_FOLDER), "");
            destinationSubFolder = destinationSubFolder.startsWith("/") ?
                    destinationSubFolder.substring(1) : destinationSubFolder ;

            mover.uploadFile(new NtlmPasswordAuthentication(baseDestination + destinationSubFolder,
                            serverConfig.get(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_USER),
                            serverConfig.get(SyncSettingsModel.SyncSettingsEntry.COLUMN_NAME_SERVER_PASS)),
                    new UploadFile(baseDestination + destinationSubFolder, source),
                    new FileMover.Callback() {
                        @Override
                        public void onSuccess() {
                            // We should save to the database that this file was successfully saved to NAS
                        }

                        @Override
                        public void onError() {
                            // We should save to the database that this file was not successfully saved to NAS
                        }
                    });
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
