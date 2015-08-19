package com.selfmadeapp.android_nas_backup.network.model;

/**
 * Created by oruckdeschel on 18.08.2015.
 */
public abstract class Callback {
    public abstract void onSuccess();

    public void onSuccess(boolean result) {
        onSuccess();
    }

    public abstract void onError();

    public void onError(boolean result) {
        onError();
    }
}
