package com.selfmadeapp.android_nas_backup.view.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.selfmadeapp.android_nas_backup.R;
import com.selfmadeapp.android_nas_backup.ServerAddressTextWatcherValidator;
import com.selfmadeapp.android_nas_backup.network.NetUtils;
import com.selfmadeapp.android_nas_backup.storage.database.DatabaseHandler;
import com.selfmadeapp.android_nas_backup.storage.database.model.SyncModel;
import com.selfmadeapp.android_nas_backup.view.activities.MainActivity;

import net.rdrei.android.dirchooser.DirectoryChooserFragment;

/**
 * Created by oruckdeschel on 24.03.2015.
 */
public class AddSynchronisationDialog {

    // UI Elements from dialog
    private MaterialEditText serverAddressET;
    private MaterialEditText serverNameET;
    private MaterialEditText serverPassET;
    private TextView folderChosen;
    private DirectoryChooserFragment mDialog;

    private DatabaseHandler dbHandler;

    public AddSynchronisationDialog(final Context context) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(context.getString(R.string.dlg_add_sync))
                .customView(R.layout.custom_dialog_enter_nas, false)
                .positiveText(context.getString(R.string.dlg_add_sync_pos))
                .positiveColor(Color.parseColor("#03a9f4"))
                .negativeText(context.getString(R.string.dlg_add_sync_neg))
                .negativeColor(context.getResources().getColor(R.color.primary_text))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        String serverAddress = serverAddressET.getText().toString();
                        String serverName = serverNameET.getText().toString();
                        String serverPass = serverPassET.getText().toString();

                        String clientFolder = folderChosen.getText().toString();

                        dbHandler = new DatabaseHandler(context);

                        if (!NetUtils.isValidNetworkAddress()) {
                            new SnackBar((android.app.Activity) context, context.getString(R.string.msg_address_not_valid)).show();
                        }
                        if (dbHandler.saveData(new SyncModel(serverAddress, serverName, serverPass, clientFolder)) != -1) {
                            ((MainActivity) context).fillLocationsListView(dbHandler.getData());
                        } else {
                            new SnackBar((android.app.Activity) context, context.getString(R.string.msg_could_not_save)).show();
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        // do nothing
                    }
                })
                .build();

        dialog.show();

        serverAddressET = ((MaterialEditText) dialog.findViewById(R.id.custom_dialog_nas_server_address));
        serverNameET = ((MaterialEditText) dialog.findViewById(R.id.custom_dialog_nas_server_user));
        serverPassET = ((MaterialEditText) dialog.findViewById(R.id.custom_dialog_nas_server_pass));
        folderChosen = ((TextView) dialog.findViewById(R.id.custom_dialog_nas_client_folder));

        serverAddressET.addTextChangedListener(new ServerAddressTextWatcherValidator(context, serverAddressET));
        folderChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = DirectoryChooserFragment.newInstance(context.getString(R.string.dlg_choose_folder_title), null);
                mDialog.show(((Activity) context).getFragmentManager(), null);
            }
        });
    }

    public void onSelectDirectory(@NonNull String s) {
        if (folderChosen != null) {
            folderChosen.setText(s);
        }
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void onCancelChooser() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

}
