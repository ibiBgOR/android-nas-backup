package com.selfmadeapp.android_nas_backup.storage.database.model;

import java.sql.Date;

/**
 * Created by oruckdeschel on 22.08.2015.
 */
public class FileModel {

    public static final short FILE_HAS_CHANGES = 0;
    public static final short FILE_HAS_NO_CHANGES = 1;
    public static final short FILE_WAS_NOT_SYNCED = 2;

    private String fileName;
    private int serverConfig;
    private Long lastSynced;
    private short changes;

    public FileModel(String fileName, int serverConfig, Long lastSynced, short changes) {
        this.fileName = fileName;
        this.serverConfig = serverConfig;
        this.lastSynced = lastSynced;
        this.changes = changes;
    }

    public short wereChanges() {
        return changes;
    }

    public Long getLastSynced() {
        return lastSynced;
    }

    public int getServerConfig() {
        return serverConfig;
    }

    public String getFileName() {
        return fileName;
    }
}
