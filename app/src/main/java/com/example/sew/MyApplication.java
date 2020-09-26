package com.example.sew;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.HomeActivity;
import com.example.sew.activities.ServerErrorActivity;
import com.example.sew.common.ICommonValues;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.NetworkChangeReceiver;
import com.example.sew.helpers.ThemeHelper;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.splunk.mint.Mint;
import com.thin.downloadmanager.util.Log;

import java.util.List;

import io.paperdb.Paper;

import static com.example.sew.common.MyConstants.SPLUNK_API_KEY;

public class MyApplication extends MultiDexApplication implements ICommonValues {

    private static MyApplication myApplication;
    public static Context mAppContext;

    private boolean isBrowsingOffline = false;

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Paper.init(this);
        ThemeHelper.applyTheme(MyService.getDarkModePref());
        myApplication = this;
        mAppContext = getApplicationContext();
        Mint.initAndStartSession(this, SPLUNK_API_KEY);
        Logger.addLogAdapter(new AndroidLogAdapter());
        androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler);
        networkChangeReceiver = new NetworkChangeReceiver() {
            @Override
            public void onNetworkChanged(boolean isOffline) {
                super.onNetworkChanged(isOffline);
                if (isOffline && !isBrowsingOffline()) {
                    if (!isAppIsInBackground(MyApplication.getContext())) {
                        setBrowsingOffline(true);
                        Intent intent = new Intent(MyApplication.getContext(), ServerErrorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getContext().startActivity(intent);
                    }

                }

                setBrowsingOffline(isOffline);
            }
        };
        registerNetworkBroadcastForNougat();
    }

    public boolean isBrowsingOffline() {
        return isBrowsingOffline;
    }

    public void setBrowsingOffline(boolean browsingOffline) {
        isBrowsingOffline = browsingOffline;
    }

    public static Context getContext() {
        return mAppContext;
    }

    private Thread.UncaughtExceptionHandler androidDefaultUEH;

    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            Log.e("TestApplication", "Uncaught exception is: ", ex);
            androidDefaultUEH.uncaughtException(thread, ex);
        }
    };
    NetworkChangeReceiver networkChangeReceiver;

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(networkChangeReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (android.app.ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<android.app.ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
