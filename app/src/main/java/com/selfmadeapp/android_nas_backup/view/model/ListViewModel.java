package com.selfmadeapp.android_nas_backup.view.model;

/**
 * Created by oruckdeschel on 11.01.2015.
 */
public class ListViewModel {

    private final String serverAddress;
    private final String clientFolder;
    private final String lastSync;

    public ListViewModel(String server, String client, String lastSync) {
        this.serverAddress = server;
        this.clientFolder = client;
        this.lastSync = lastSync;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getClientFolder() {
        return clientFolder;
    }

    public String getLastSync() {
        return lastSync;
    }
}
