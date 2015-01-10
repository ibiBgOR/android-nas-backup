package com.selfmadeapp.android_nas_backup.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.widgets.SnackBar;
import com.selfmadeapp.android_nas_backup.R;
import com.selfmadeapp.android_nas_backup.network.NetUtils;
import com.selfmadeapp.android_nas_backup.storage.database.DatabaseHandler;
import com.selfmadeapp.android_nas_backup.storage.database.model.SyncModel;

import net.rdrei.android.dirchooser.DirectoryChooserFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity implements DirectoryChooserFragment.OnFragmentInteractionListener {

    private DatabaseHandler dbHandler;

    private Map<String, String> syncLocations;

    private ListView syncLocationsListView;
    private TextView syncLocationsTitle;
    private ImageView syncNoLocationBackground;
    private Button syncAddLocationButton;

    private TextView folderChosen;
    private DirectoryChooserFragment mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DatabaseHandler(this);

        if (!NetUtils.hasConnection(this)) {
            new SnackBar(this, getString(R.string.msg_no_internet_connection)).show();
        }

        syncAddLocationButton = (Button) findViewById(R.id.sync_add_location_button);
        syncAddLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.dlg_add_sync))
                        .customView(R.layout.custom_dialog_enter_nas, false)
                        .positiveText(getString(R.string.dlg_add_sync_pos))
                        .positiveColor(Color.parseColor("#03a9f4"))
                        .negativeText(getString(R.string.dlg_add_sync_neg))
                        .negativeColor(Color.parseColor("#000000"))
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                String serverAddress = ((EditText) dialog.findViewById(R.id.custom_dialog_nas_server_address)).getText().toString();
                                String serverName = ((EditText) dialog.findViewById(R.id.custom_dialog_nas_server_user)).getText().toString();
                                String serverPass = ((EditText) dialog.findViewById(R.id.custom_dialog_nas_server_pass)).getText().toString();

                                String clientFolder = folderChosen.getText().toString();

                                if (!NetUtils.isValidNetworkAddress(serverAddress)) {
                                    new SnackBar(MainActivity.this, getString(R.string.msg_address_not_valid)).show();
                                }
                                if (!NetUtils.isCifsShare(serverAddress)) {
                                    new SnackBar(MainActivity.this, getString(R.string.msg_server_no_cifs)).show();
                                }
                                if (dbHandler.saveData(new SyncModel(serverAddress, serverName, serverPass, clientFolder)) != -1) {
                                    MainActivity.this.fillLocationsListView(dbHandler.getData());
                                } else {
                                    new SnackBar(MainActivity.this, getString(R.string.msg_could_not_save)).show();
                                }
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                // do nothing
                            }
                        })
                        .build();

                dialog.show();

                folderChosen = ((TextView) dialog.findViewById(R.id.custom_dialog_nas_client_folder));
                folderChosen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog = DirectoryChooserFragment.newInstance(getString(R.string.dlg_choose_folder_title), null);
                        mDialog.show(getFragmentManager(), null);
                    }
                });


            }
        });

        syncLocations = dbHandler.getData();

        if (syncLocations == null) {
            syncNoLocationBackground = (ImageView) findViewById(R.id.sync_no_locations);
            // Set background image visible
            syncNoLocationBackground.setVisibility(View.VISIBLE);

            Toast.makeText(this, getString(R.string.msg_no_server_specified), Toast.LENGTH_LONG).show();
        } else {
            syncLocationsListView = (ListView) findViewById(R.id.sync_locations_list_view);
            syncLocationsTitle = (TextView) findViewById(R.id.sync_locations_title);

            fillLocationsListView(syncLocations);
        }
    }

    private void fillLocationsListView(Map<String, String> syncLocations) {
        List<String> clients = new ArrayList<String>();
        List<String> servers = new ArrayList<String>();
        // Fill the listview.
        for (String element : syncLocations.keySet()) {
            servers.add(element);
            clients.add(syncLocations.get(element));
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, servers);
        syncLocationsListView.setAdapter(adapter);

        // Show the title and the listview
        syncLocationsTitle.setVisibility(View.VISIBLE);
        syncLocationsListView.setVisibility(View.VISIBLE);

        if (syncNoLocationBackground != null) {
            syncNoLocationBackground.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelectDirectory(@NonNull String s) {
        if (folderChosen != null) {
            folderChosen.setText(s);
        }
        mDialog.dismiss();
    }

    @Override
    public void onCancelChooser() {
        mDialog.dismiss();
    }
}
