package com.selfmadeapp.android_nas_backup.network.mover.model;

import java.io.File;

/**
 * Created by oruckdeschel on 10.01.2015.
 */
public class UploadFile {

    private String destFolder;
    private File file;

    public UploadFile(String destFolder, File file) {
        this.destFolder = destFolder;
        this.file = file;
    }

    public String getDestFolder() {
        return destFolder;
    }

    public File getFile() {
        return file;
    }
}
