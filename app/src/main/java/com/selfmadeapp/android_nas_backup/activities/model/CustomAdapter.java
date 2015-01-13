package com.selfmadeapp.android_nas_backup.activities.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.selfmadeapp.android_nas_backup.R;

import java.util.List;

/**
 * Created by oruckdeschel on 11.01.2015.
 */
public class CustomAdapter extends ArrayAdapter<ListViewModel> {

    private final Context context;
    private final List<ListViewModel> values;

    static class ViewHolder {
        public TextView serverAddress;
        public TextView clientFolder;
        public TextView lastSync;
    }

    public CustomAdapter(Context context, List<ListViewModel> values) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        TextView serverAddress = (TextView) rowView.findViewById(R.id.list_item_server_address);
        TextView clientFolder = (TextView) rowView.findViewById(R.id.list_item_client_folder);
        TextView lastSync = (TextView) rowView.findViewById(R.id.list_item_last_sync);

        ListViewModel element = values.get(position);

        serverAddress.setText(element.getServerAddress());
        clientFolder.setText(element.getClientFolder());
        lastSync.setText(element.getLastSync());

        return rowView;
    }
}
