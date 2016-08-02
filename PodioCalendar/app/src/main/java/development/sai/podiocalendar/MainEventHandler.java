package development.sai.podiocalendar;

import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import development.sai.podiocalendar.events.IEventHandler;
import development.sai.podiocalendar.events.ProgressBarEvent;
import development.sai.podiocalendar.events.ShowFragmentEvent;

/**
 * Created by sai on 8/1/16.
 */
public class MainEventHandler implements IEventHandler {
    private HomeActivity activity;
    private boolean isPaused;

    public MainEventHandler(HomeActivity activity) {
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
            activity.getProgressBar().setVisibility(event.showProgressBar ? View.VISIBLE : View.GONE);
        }
    }

    @Subscribe
    @Override
    public void onEvent(ShowFragmentEvent event) {
        // Nothing for now
    }
}
