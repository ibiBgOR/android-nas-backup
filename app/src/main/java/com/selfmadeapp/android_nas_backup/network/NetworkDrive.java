package com.selfmadeapp.android_nas_backup.network;

/**
 * Created by oruckdeschel on 23.03.2015.
 */
public class NetworkDrive {

    private String address;

    private String name;

    public NetworkDrive(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

}
