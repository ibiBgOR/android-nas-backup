package com.selfmadeapp.android_nas_backup.network.mover;

import com.selfmadeapp.android_nas_backup.network.mover.model.UploadFile;

import java.util.List;

import jcifs.smb.NtlmPasswordAuthentication;

/**
 * Created by oruckdeschel on 10.01.2015.
 */
public interface FileMover {

    static final String TAG = "FileMover";

    public void uploadFiles(NtlmPasswordAuthentication auth, List<UploadFile> files);

}
