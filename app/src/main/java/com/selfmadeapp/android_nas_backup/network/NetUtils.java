package com.selfmadeapp.android_nas_backup.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.selfmadeapp.android_nas_backup.network.model.Callback;

import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * Created by oruckdeschel on 10.01.2015.
 */
public class NetUtils {

    private static final String TAG = "NetUtils";

    private static boolean isValidNetworkAddress = false;

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

    public static boolean isValidNetworkAddress() {
        return NetUtils.isValidNetworkAddress;
    }

    public static void startTempThread() {
        Log.i(TAG, "Start tempthread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", "usr", "pass");
                    String[] domains = (new SmbFile("smb://" + "address", auth)).list();
                    for (String file : domains) {
                        Log.d(TAG, file);
                    }
                } catch (SmbException e1) {
                    e1.printStackTrace();
                } catch (MalformedURLException e2) {
                    e2.printStackTrace();
                }
            }
        }

        ).start();
    }

    private static boolean checkCifsPorts(final String address, final int timeout) {
        try {
            Socket socket = new Socket();

            // Port from http://www.cyberciti.biz/faq/what-ports-need-to-be-open-for-samba-to-communicate-with-other-windowslinux-systems/

            /* Check Port 445 */
            socket.connect(new InetSocketAddress(address, 445), timeout);
            socket.close();

            startTempThread();

            return true;
        } catch (ConnectException ex) {
            Log.e(TAG, ex.getMessage());
            return false;
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return false;
        }
    }

    public static void isCifsShare(final String address, final Callback callback) {

        //String newAddress = address.replace("smb://", "").substring(0, address.indexOf("/"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Start thread to check whether a given address is valid.");
                if (checkCifsPorts(address, 10000)) {
                    callback.onSuccess();
                    Log.i(TAG, "Given address is valid Cifs Share.");
                } else {
                    callback.onError();
                    Log.i(TAG, "Given address is not a valid Cifs Share.");
                }
            }
        }).start();
    }

    public static void isValidNetworkAddress(String address, Callback callback) {
        if (address.length() < 13) { // Minimum length 13 ("smb://1.1.1.1")
            NetUtils.isValidNetworkAddress = false;
            callback.onError(false);
            return;
        }

        Pattern pattern = Pattern.compile("smb://(?:[0-9]{1,3}.){3}[0-9]{1,3}(:{1}[0-9]{1,}){0,1}/([a-zA-Z0-9*]*/{0,})*");

        Matcher matcher = pattern.matcher(address);

        if (matcher.matches()) {
            Log.i(TAG, "Given serveraddress is valid networkaddress.");
            NetUtils.isValidNetworkAddress = true;
            callback.onSuccess(true);
            return;
        } else {
            Log.d(TAG + "debug", "Address: " + address);
            NetUtils.isValidNetworkAddress = false;
            // Log.i(TAG, "Given serveraddress is not valid.");
            callback.onError(false);
            return;
        }
    }
}
