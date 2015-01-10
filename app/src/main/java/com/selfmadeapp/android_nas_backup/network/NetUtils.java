package com.selfmadeapp.android_nas_backup.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by oruckdeschel on 10.01.2015.
 */
public class NetUtils {

    private static final String TAG = "NetUtils";


    /**
     * Checks if device is connected to internet.
     *
     * @return true if the device is connected to the internet.
     */
    public static boolean hasConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static boolean isValidNetworkAddress(String address) {
        return true;
    }

    public static boolean isCifsShare(String address) {
        return true;
    }
}
