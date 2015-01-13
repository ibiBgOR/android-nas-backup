package com.selfmadeapp.android_nas_backup.network.mover;

import android.content.Context;
import android.util.Log;

import com.selfmadeapp.android_nas_backup.network.mover.model.UploadFile;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * Created by oruckdeschel on 10.01.2015.
 */
public class FileMoverImpl implements FileMover {

    private Context context;

    public FileMoverImpl(Context context) {
        this.context = context;
    }

    @Override
    public void uploadFiles(NtlmPasswordAuthentication auth, List<UploadFile> files) {
        List<UploadFile> workFiles = files;

        for (UploadFile file : files) {
            uploadSingleFile(auth, file);
            workFiles.remove(file);
        }
    }

    private void uploadSingleFile(final NtlmPasswordAuthentication auth, final UploadFile file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadFileToServer(auth, file, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "Yey");
                    }

                    @Override
                    public void onError() {
                        Log.i(TAG, "Oh damn");
                    }
                });
            }
        });
    }

    private void uploadFileToServer(NtlmPasswordAuthentication auth, UploadFile file, Callback callback) {
        Log.i(TAG, "Upload: " + file.getFile().getName());

        if (!file.getFile().exists()) {
            callback.onError();
        }

        if (!file.getFile().isFile() || !file.getFile().canRead()) {
            Log.e(TAG, "Couldn't read the file or the file isn't a file.");
            return;
        }

        try {
            SmbFile smbFile = new SmbFile(file.getDestFolder().concat(file.getFile().getPath().replace("/storage/emulated/0/", "")), auth);

            try {
                smbFile.createNewFile();
            } catch (final SmbException e) {
                Log.e(TAG, "Couldn't create file on server");
                return;
            }

            InputStream stream_in = new DataInputStream(new FileInputStream(file.getFile()));
            OutputStream stream_out = smbFile.getOutputStream();

            byte[] buffer = new byte[1024];

            while (stream_in.read(buffer) != -1) {
                stream_out.write(buffer);
                stream_out.flush();
            }

            stream_in.close();
            stream_out.close();
            Log.i(TAG, "Copied file to server");
            callback.onSuccess();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            callback.onError();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            callback.onError();
        } catch (IOException e) {
            e.printStackTrace();
            callback.onError();
        }

    }

    private interface Callback {
        void onSuccess();

        void onError();
    }

}
