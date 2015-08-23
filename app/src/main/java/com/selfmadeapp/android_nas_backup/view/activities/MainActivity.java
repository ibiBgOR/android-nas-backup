package com.selfmadeapp.android_nas_backup.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.widgets.SnackBar;
import com.selfmadeapp.android_nas_backup.R;
import com.selfmadeapp.android_nas_backup.network.NetUtils;
import com.selfmadeapp.android_nas_backup.services.FileFetcherService;
import com.selfmadeapp.android_nas_backup.services.FileUploadService;
import com.selfmadeapp.android_nas_backup.storage.database.DatabaseHandler;
import com.selfmadeapp.android_nas_backup.view.dialogs.AddSynchronisationDialog;
import com.selfmadeapp.android_nas_backup.view.model.CustomAdapter;
import com.selfmadeapp.android_nas_backup.view.model.ListViewModel;

import net.rdrei.android.dirchooser.DirectoryChooserFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity implements DirectoryChooserFragment.OnFragmentInteractionListener {

    private DatabaseHandler dbHandler;

    private Map<String, String> syncLocations;

    private ListView syncLocationsListView;
    private TextView syncLocationsTitle;
    private ImageView syncNoLocationBackground;
    private Button syncAddLocationButton;
    private SwipeRefreshLayout syncSwipeRefreshContainer;

    private ArrayAdapter adapter;
    private List<ListViewModel> adapterData;

    private AddSynchronisationDialog dialog;

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
                dialog = new AddSynchronisationDialog(MainActivity.this);
            }
        });

        syncLocations = dbHandler.getSyncData();

        syncLocationsListView = (ListView) findViewById(R.id.sync_locations_list_view);
        syncLocationsTitle = (TextView) findViewById(R.id.sync_locations_title);

        syncNoLocationBackground = (ImageView) findViewById(R.id.sync_no_locations);

        syncSwipeRefreshContainer = (SwipeRefreshLayout) findViewById(R.id.sync_locations_swipe_refresh_container);
        syncSwipeRefreshContainer.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);
        syncSwipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        final Map<String, String> tmpSettingsMap = dbHandler.getSyncData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fillLocationsListView(tmpSettingsMap);
                                syncSwipeRefreshContainer.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });

        syncLocationsListView.setEmptyView(syncNoLocationBackground);
        syncLocationsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (syncLocationsListView == null || syncLocationsListView.getChildCount() == 0) ?
                                0 : syncLocationsListView.getChildAt(0).getTop();
                syncSwipeRefreshContainer.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        if (syncLocations == null) {
            // Set background image visible
            syncNoLocationBackground.setVisibility(View.VISIBLE);
            Toast.makeText(this, getString(R.string.msg_no_server_specified), Toast.LENGTH_LONG).show();
        } else {
            fillLocationsListView(syncLocations);
        }
    }

    public void fillLocationsListView(Map<String, String> syncLocations) {

        if (adapterData == null) {
            adapterData = new ArrayList<ListViewModel>();
        }

        adapterData.clear();

        // Fill the listview.
        for (String element : syncLocations.keySet()) {
            adapterData.add(new ListViewModel(element, syncLocations.get(element), "Niemals"));
        }

        if (adapter == null) {
            adapter = new CustomAdapter(this, adapterData);
            syncLocationsListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

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
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_upload:
                // Start FileFetching to retrieve all files
                Intent i= new Intent(this, FileUploadService.class);
                this.startService(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelectDirectory(@NonNull String s) {
        if (dialog != null) {
            dialog.onSelectDirectory(s);
        }
    }

    @Override
    public void onCancelChooser() {
        if (dialog != null) {
            dialog.onCancelChooser();
        }
    }
}