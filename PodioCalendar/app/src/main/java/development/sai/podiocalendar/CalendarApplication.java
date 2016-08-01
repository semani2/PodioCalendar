package development.sai.podiocalendar;


import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.greenrobot.eventbus.EventBus;

import development.sai.podiocalendar.account.IAccountManager;
import development.sai.podiocalendar.network.INetworkManager;

/**
 * Created by sai on 8/1/16.
 */
public class CalendarApplication extends Application implements INetworkManager{
    public static final String ARG_REAL_NAME = "com.podio.calendar.REAL_NAME";
    public static final String ARG_TRANSFER_TOKEN = "com.podio.calendar.TRANSFER_TOKEN";

    private static CalendarApplication instance;

    private IAccountManager accountManager;

    private static EventBus eventBus;

    public static Context getContext() {
        return instance != null ? instance.getApplicationContext() : null;
    }

    public static CalendarApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        eventBus = EventBus.getDefault();
        eventBus = EventBus.getDefault();
    }

    @Override
    public boolean isNetworkAvailable() {
        if(getContext() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        return false;
    }
}
