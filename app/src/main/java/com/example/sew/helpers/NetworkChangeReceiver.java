package com.example.sew.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.example.sew.activities.BaseActivity;
import com.example.sew.common.ICommonValues;

public class NetworkChangeReceiver extends BroadcastReceiver implements ICommonValues {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        boolean isOffline = checkIfInternetOffline(context);
        if (isOffline)
            waitForNetworkToCome(context);
//        else{
//            Intent in = new Intent();
//            in.putExtra(IS_OFFLINE, false);
//            BaseActivity.sendBroadCast(BROADCAST_NETWORK_CHANGED, in);
//            onNetworkChanged(false);
//        }

    }

    private void waitForNetworkToCome(final Context context) {
        new Handler().postDelayed(() -> {
            boolean isOffline = checkIfInternetOffline(context);
            if (isOffline) {
                Intent in = new Intent();
                in.putExtra(IS_OFFLINE, true);
                BaseActivity.sendBroadCast(BROADCAST_NETWORK_CHANGED, in);
                onNetworkChanged(true);
            }
        }, 2000);
    }

    boolean checkIfInternetOffline(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        return !serviceManager.isNetworkAvailable();
    }

    public void onNetworkChanged(boolean isOffline) {

    }
}
