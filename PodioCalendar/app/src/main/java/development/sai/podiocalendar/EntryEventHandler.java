package development.sai.podiocalendar;

import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import development.sai.podiocalendar.events.IEventHandler;
import development.sai.podiocalendar.events.LogoutEvent;
import development.sai.podiocalendar.events.ProgressBarEvent;
import development.sai.podiocalendar.events.ShowEventDetailsEvent;
import development.sai.podiocalendar.events.ShowFragmentEvent;
import development.sai.podiocalendar.events.ShowMessageEvent;
import development.sai.podiocalendar.helpers.EntryFragmentHelper;

/**
 * Created by sai on 8/2/16.
 */
public class EntryEventHandler implements IEventHandler {

    private EntryActivity activity;
    private boolean isPaused;

    public EntryEventHandler(EntryActivity activity) {
        this.activity = activity;

        EventBus.getDefault().register(this);
    }

    public void onPause() {
        isPaused = true;
    }

    public void onResume() {
        isPaused = false;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    @Override
    public void onEvent(ProgressBarEvent event) {
        if(!isPaused) {
            if(event.showProgressBar) {
                activity.getProgressBar().setVisibility(View.VISIBLE);
            }
            else {
                activity.getProgressBar().setVisibility(View.GONE);
            }
        }
    }

    @Subscribe
    @Override
    public void onEvent(ShowFragmentEvent event) {
        new EntryFragmentHelper().showFragment(activity.getSupportFragmentManager(), event.fragment);
    }

    @Subscribe
    @Override
    public void onEvent(ShowMessageEvent event) {
        Toast.makeText(activity, event.message, Toast.LENGTH_LONG).show();
    }

    @Subscribe
    @Override
    public void onEvent(LogoutEvent event) {
        // Technically should not be able to logout in entry activity
    }

    @Subscribe
    @Override
    public void onEvent(ShowEventDetailsEvent event) {
        //Nothing to do here
    }
}
