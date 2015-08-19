package com.selfmadeapp.android_nas_backup.network;

import android.util.Log;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oruckdeschel on 23.03.2015.
 */
public class NetworkDriveDiscovery {

    private static final String TAG = NetworkDriveDiscovery.class.getSimpleName();

    private static final int SAMBA_PORT = 445;

    private static final int THREAD_COUNT = 256;

    private static volatile List<String> networkDriveList;

    private static long lastScanTimestamp;

    public NetworkDriveDiscovery() {
        networkDriveList = new ArrayList<String>();
    }

    public List<String> getNetworkDrives() {

        if (networkDriveList != null && lastScanTimestamp + 10000 > System.currentTimeMillis()) {
            return networkDriveList;
        }

        //scanNetwork();

        return networkDriveList;
    }

    volatile int finishedThreads = 0;

    /**
     * Scans the network for samba drives.
     * Refreshes the lastScanTimestamp to the scantime.
     */
    public void scanNetwork(final Callback callback) {

        for (int i = 1; i <= THREAD_COUNT; i++) {
            new Thread(new Runnable() {
                int i;

                public void run() {
                    List<String> hosts = scanSubNet("192.168.1.", (i - 1) * 256 / THREAD_COUNT);
                    synchronized (this) {
                        finishedThreads++;
                        networkDriveList.addAll(hosts);
                        Log.d(TAG, String.format("%d", networkDriveList.size()));
                        if (finishedThreads >= THREAD_COUNT) {
                            callback.onResult(networkDriveList);
                        }
                    }
                }

                public Runnable init(int i) {
                    this.i = i;
                    return this;
                }
            }.init(i)).start();
        }

        lastScanTimestamp = System.currentTimeMillis();
    }

    private List<String> scanSubNet(String subnet, int start) {
        List<String> hosts = new ArrayList<String>();

        InetAddress inetAddress = null;

        for (int i = start; i < start + 256 / THREAD_COUNT; i++) {
            Log.d(TAG, "Trying: " + subnet + String.valueOf(i));
            try {
                inetAddress = InetAddress.getByName(subnet + String.valueOf(i));
                if (inetAddress.isReachable(1000)) {
                    if (checkCifsPorts(inetAddress.getHostAddress(), 1000)) {
                        hosts.add(inetAddress.getHostName());
                        Log.d(TAG, "Samba share found: " + inetAddress.getHostName());
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return hosts;
    }

    private boolean checkCifsPorts(final String address, final int timeout) {
        try {
            Socket socket = new Socket();

            // Port from http://www.cyberciti.biz/faq/what-ports-need-to-be-open-for-samba-to-communicate-with-other-windowslinux-systems/

            /* Check Samba Port */
            socket.connect(new InetSocketAddress(address, SAMBA_PORT), timeout);
            socket.close();

            Log.d(TAG, "Detected samba share");
            return true;
        } catch (ConnectException ex) {
            Log.e(TAG, ex.getMessage());
            return false;
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return false;
        }
    }

    public interface Callback {

        public void onResult(List<String> hosts);

    }

}
