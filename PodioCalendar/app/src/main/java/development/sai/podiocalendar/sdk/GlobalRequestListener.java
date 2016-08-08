package development.sai.podiocalendar.sdk;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.podio.sdk.ApiError;
import com.podio.sdk.ConnectionError;
import com.podio.sdk.NoResponseError;
import com.podio.sdk.Request;

import org.greenrobot.eventbus.EventBus;

import development.sai.podiocalendar.EntryActivity;
import development.sai.podiocalendar.HomeActivity;
import development.sai.podiocalendar.R;
import development.sai.podiocalendar.account.IAccountManager;
import development.sai.podiocalendar.account.PodioAccountManager;
import development.sai.podiocalendar.events.ProgressBarEvent;
import development.sai.podiocalendar.events.ShowFragmentEvent;

/**
 * Created by sai on 8/2/16.
 */
public class GlobalRequestListener implements Request.ErrorListener, Request.SessionListener {

    private static GlobalRequestListener instance = null;
    private final Context context;
    private final EventBus eventBus;

    private GlobalRequestListener(Context context) {
        this.context = context;
        this.eventBus = EventBus.getDefault();
    }

    public static GlobalRequestListener getInstance(Context context) {
        if(instance == null) {
            instance = new GlobalRequestListener(context);
        }
        return instance;
    }

    @Override
    public boolean onErrorOccurred(Throwable cause) {
        eventBus.post(new ProgressBarEvent(false));

        if (cause instanceof ConnectionError) {
            showToast(context.getString(R.string.strNoNetwork));
        } else if (cause instanceof NoResponseError) {
            showToast(context.getString(R.string.strNoNetwork));
        } else if (cause instanceof ApiError) {
            ApiError error = (ApiError) cause;

            if (error.doPropagate()) {
                showToast(error.getErrorDescription());
            } else if (error.isAuthError()) {
                IAccountManager accountManager = PodioAccountManager.getInstance(context);
                accountManager.signOut();
            }
        } else if (cause != null) {
            showToast(context.getString(R.string.strSomethingWrong));
            cause.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean onSessionChanged(String authToken, String refreshToken, String transferToken, long expires) {
        EventBus eventBus = EventBus.getDefault();
        IAccountManager accountManager = PodioAccountManager.getInstance(context);
        eventBus.post(new ProgressBarEvent(false));
        accountManager.addSession(authToken, refreshToken, transferToken, expires);

        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

        return true;
    }

    private void showToast(String msg) {
        if(context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }
}
