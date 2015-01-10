package com.selfmadeapp.android_nas_backup.storage.database.model;

/**
 * Created by oruckdeschel on 10.01.2015.
 */
public class SyncModel {

    private String syncServerAddress;
    private String syncServerUserName;
    private String syncServerPassword;
    private String syncClientFolder;

    public SyncModel(String address, String name, String pass, String folder) {
        this.syncServerAddress = address;
        this.syncServerUserName = name;
        this.syncServerPassword = pass;
        this.syncClientFolder = folder;
    }

    public String getSyncServerAddress() {
        return syncServerAddress;
    }

    public String getSyncServerUserName() {
        return syncServerUserName;
    }

    public String getSyncServerPassword() {
        return syncServerPassword;
    }

    public String getSyncClientFolder() {
        return syncClientFolder;
    }

}
