package com.selfmadeapp.android_nas_backup.view.dialogs;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.selfmadeapp.android_nas_backup.R;
import com.selfmadeapp.android_nas_backup.network.NetworkDriveDiscovery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oruckdeschel on 24.03.2015.
 */
public class ChooseServerDialog {

    private static final String TAG = ChooseServerDialog.class.getSimpleName();

    private Activity activity;

    // UI Elements from dialog
    private ListView serverList;

    public ChooseServerDialog(final Context context) {

        try {
            activity = (Activity) context;
        } catch (ClassCastException ex) {
            Log.e(TAG, "Couldn't cast context to activity.");
            return;
        }

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(context.getString(R.string.dlg_choose_share))
                .customView(R.layout.custom_dialog_select_nas, false)
                .positiveText("Save")
                .neutralText("Refresh")
                .negativeText("Cancel")
                .build();

        dialog.show();

        serverList = ((ListView) dialog.findViewById(R.id.temp));

        final ArrayList<String> displayedHosts = new ArrayList<String>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, displayedHosts);
        serverList.setAdapter(arrayAdapter);

        new NetworkDriveDiscovery().scanNetwork(new NetworkDriveDiscovery.Callback() {

            @Override
            public void onResult(List<String> hosts) {
                displayedHosts.addAll(hosts);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
            }

        });
    }

}
