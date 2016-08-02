package development.sai.podiocalendar.account;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.podio.sdk.Request;
import com.podio.sdk.internal.Utils;

import development.sai.podiocalendar.CalendarApplication;
import development.sai.podiocalendar.sdk.GlobalRequestListener;
import development.sai.podiocalendar.sdk.SuperPodio;

/**
 * Created by sai on 8/1/16.
 */
public class PodioAccountManager implements IAccountManager {

    private final Context context;

    //TODO :: Fix this to use own client id and secret registered to use transfer token to authenticate
    private static final String CLIENT_ID = "podio-chat-nie89c";
    private static final String CLIENT_SECRET = "pn3FsiN9JsunZVSsGPbrT1CunOnGBOazv4jvidWhJq3XQI18e72tQyyh1azE9grG";

    private PodioAccountManager(Context context) {
        this.context = context;
    }
    private static PodioAccountManager podioAccountManager = null;

    public static PodioAccountManager getInstance(Context context) {
        if(podioAccountManager == null) {
            podioAccountManager = new PodioAccountManager(context);
        }
        return podioAccountManager;
    }

    @Override
    public Bundle readPodioAccountInfo() {
        android.accounts.AccountManager accountManager = android.accounts.AccountManager.get(context);
        Bundle bundle = new Bundle();

        try {
            Account[] accounts = accountManager.getAccountsByType("com.podio");
            if (accounts.length > 0) {
                Account account = accounts[0];
                String userName = accountManager.getUserData(account, "real_name");
                String transferToken = accountManager.getUserData(account, "transfer_token");

                bundle.putString(CalendarApplication.ARG_REAL_NAME, userName);
                bundle.putString(CalendarApplication.ARG_TRANSFER_TOKEN, transferToken);
            }
        } catch (Exception e) {
            bundle.clear();
        }

        return bundle;
    }

    @Override
    public String readPodioAccountTransferToken() {
        android.accounts.AccountManager accountManager = android.accounts.AccountManager.get(context);

        Account[] accounts = accountManager.getAccountsByType("com.podio");
        if (accounts.length > 0) {
            Account account = accounts[0];
            String transferToken = accountManager.getUserData(account, "transfer_token");
            return transferToken;
        }
        return null;
    }

    @Override
    public boolean hasSession() {
        String accessToken = readSdkAccessToken();
        String refreshToken = readSdkRefreshToken();
        long expires = readSdkTokenExpires();

        return Utils.notAnyEmpty(accessToken, refreshToken) && expires > 0;
    }

    @Override
    public void clearSession() {
        SuperPodio.restoreSession(null, null, 0);
        context.getSharedPreferences("com.podio.sdk", Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    @Override
    public void addSession(String accessToken, String refreshToken, String transferToken, long expires) {
        context.getSharedPreferences("com.podio.sdk", Context.MODE_PRIVATE)
                .edit()
                .putString("com.podio.sdk.access_token", accessToken)
                .putString("com.podio.sdk.refresh_token", refreshToken)
                .putString("com.podio.sdk.transfer_token", transferToken)
                .putLong("com.podio.sdk.expires", expires)
                .apply();
    }

    @Override
    public String readSdkAccessToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.podio.sdk", Context.MODE_PRIVATE);
        return sharedPreferences.getString("com.podio.sdk.access_token", null);
    }

    @Override
    public String readSdkRefreshToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.podio.sdk", Context.MODE_PRIVATE);
        return sharedPreferences.getString("com.podio.sdk.refresh_token", null);
    }

    @Override
    public String readSdkTransferToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.podio.sdk", Context.MODE_PRIVATE);
        return sharedPreferences.getString("com.podio.sdk.transfer_token", null);
    }

    @Override
    public long readSdkTokenExpires() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.podio.sdk", Context.MODE_PRIVATE);
        return sharedPreferences.getLong("com.podio.sdk.expires", -1L);
    }

    @Override
    public void signOut() {
        GlobalRequestListener listener = GlobalRequestListener.getInstance(context);
        clearSession();
        resetPodioSdk(context, listener, listener);
    }

    @Override
    public void resetPodioSdk(Context context, Request.SessionListener globalSessionListener,
                                      Request.ErrorListener globalErrorListener)
    {
        String accessToken = readSdkAccessToken();
        String refreshToken = readSdkRefreshToken();
        long expires = readSdkTokenExpires();

        SuperPodio.setup(context, CLIENT_ID, CLIENT_SECRET);
        SuperPodio.restoreSession(accessToken, refreshToken, expires);

        SuperPodio.removeGlobalSessionListener(globalSessionListener);
        SuperPodio.removeGlobalErrorListener(globalErrorListener);
        SuperPodio.addGlobalSessionListener(globalSessionListener);
        SuperPodio.addGlobalErrorListener(globalErrorListener);
    }

}
