package development.sai.podiocalendar;

import android.view.View;
import android.widget.Toast;

import com.podio.sdk.ApiError;
import com.podio.sdk.ConnectionError;
import com.podio.sdk.NoResponseError;
import com.podio.sdk.Request;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import development.sai.podiocalendar.account.IAccountManager;
import development.sai.podiocalendar.account.PodioAccountManager;
import development.sai.podiocalendar.events.FABEvent;
import development.sai.podiocalendar.events.IEventHandler;
import development.sai.podiocalendar.events.ProgressBarEvent;
import development.sai.podiocalendar.events.ShowFragmentEvent;
import development.sai.podiocalendar.fragments.HomeFragment;

/**
 * Created by sai on 8/1/16.
 */
public class MainEventHandler implements IEventHandler, Request.ErrorListener, Request.SessionListener{
    private MainActivity activity;
    private boolean isPaused;

    private MainEventHandler(MainActivity mainActivity) {
        this.activity = mainActivity;
    }

    private static MainEventHandler instance = null;

    public static MainEventHandler getInstance(MainActivity mainActivity) {
        if(instance == null) {
            instance = new MainEventHandler(mainActivity);
        }
        return instance;
    }

    public void onPause() {
        isPaused = true;
    }

    public void onResume() {
        isPaused = false;
        EventBus.getDefault().register(this);
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    @Override
    public void onEvent(ProgressBarEvent event) {
        if(!isPaused) {
            activity.getProgressBar().setVisibility(event.showProgressBar ? View.VISIBLE : View.GONE);
        }
    }

    @Subscribe
    @Override
    public void onEvent(FABEvent event) {
        if(!isPaused) {
            if(event.showFAB) {
                activity.showFAB();
            }
            else {
                activity.hideFAB();
            }
        }
    }

    @Subscribe
    @Override
    public void onEvent(ShowFragmentEvent event) {
        FragmentHelper fragmentHelper = new FragmentHelper(activity.getFragmentManager());
        fragmentHelper.showFragment(event.fragment);
    }

    @Override
    public boolean onErrorOccurred(Throwable cause) {
        onEvent(new ProgressBarEvent(false));

        if (cause instanceof ConnectionError) {
            showToast(activity.getString(R.string.strNoNetwork));
        } else if (cause instanceof NoResponseError) {
            showToast(activity.getString(R.string.strNoNetwork));
        } else if (cause instanceof ApiError) {
            ApiError error = (ApiError) cause;

            if (error.doPropagate()) {
                showToast(error.getErrorDescription());
            } else if (error.isAuthError()) {
                IAccountManager accountManager = PodioAccountManager.getInstance(activity);
                accountManager.signOut();
            }
        } else if (cause != null) {
            showToast(activity.getString(R.string.strSomethingWrong));
            cause.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean onSessionChanged(String authToken, String refreshToken, String transferToken, long expires) {
        EventBus eventBus = EventBus.getDefault();
        IAccountManager accountManager = PodioAccountManager.getInstance(activity);
        eventBus.post(new ProgressBarEvent(false));
        accountManager.addSession(authToken, refreshToken, transferToken, expires);
        eventBus.post(new ShowFragmentEvent(HomeFragment.newInstance()));
        return true;
    }

    private void showToast(String msg) {
        if(activity != null) {
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
        }
    }
}
