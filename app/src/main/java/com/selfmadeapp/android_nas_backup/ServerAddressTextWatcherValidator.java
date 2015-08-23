package com.selfmadeapp.android_nas_backup;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.selfmadeapp.android_nas_backup.network.NetUtils;
import com.selfmadeapp.android_nas_backup.network.model.Callback;

/**
 * Created by oruckdeschel on 20.01.2015.
 */
public class ServerAddressTextWatcherValidator implements TextWatcher {

    private Context context;

    private MaterialEditText materialEditText;

    public ServerAddressTextWatcherValidator(Context context, MaterialEditText materialEditText) {
        this.context = context;
        this.materialEditText = materialEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void afterTextChanged(final Editable editable) {
        // validate text
        if (editable.toString().equals("")) {
            materialEditText.setBaseColor(context.getResources().getColor(R.color.primary_text));
            materialEditText.setPrimaryColor(context.getResources().getColor(R.color.primary));
            return;
        }

        NetUtils.isValidNetworkAddress(editable.toString(), new Callback() {
            @Override
            public void onSuccess() {
                NetUtils.isCifsShare(editable.toString(), new Callback() {
                    @Override
                    public void onSuccess() {
                        // make edittext show success!
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        materialEditText.setBaseColor(context.getResources().getColor(android.R.color.holo_purple));
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        // make edittext show error!
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        materialEditText.setError(context.getResources().getText(R.string.dlg_add_sync_edit_server_neg));
                                    }
                                });
                            }
                        });
                    }
                });
            }

            @Override
            public void onError() {
                // make edittext show error!
                materialEditText.setError(context.getResources().getText(R.string.dlg_add_sync_edit_server_neg));
            }
        });
    }
}
