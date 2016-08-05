package development.sai.podiocalendar;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import development.sai.podiocalendar.account.IAccountManager;
import development.sai.podiocalendar.account.PodioAccountManager;
import development.sai.podiocalendar.events.IEventHandler;
import development.sai.podiocalendar.events.LogoutEvent;
import development.sai.podiocalendar.events.ProgressBarEvent;
import development.sai.podiocalendar.events.ShowEventDetailsEvent;
import development.sai.podiocalendar.events.ShowFragmentEvent;
import development.sai.podiocalendar.events.ShowMessageEvent;
import development.sai.podiocalendar.fragments.EventDetailsFragment;

/**
 * Created by sai on 8/1/16.
 */
public class HomeEventhandler implements IEventHandler {
    private HomeActivity activity;
    private boolean isPaused;
    private static final String DIALOG = "dialog";

    public HomeEventhandler(HomeActivity activity) {
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

    @Subscribe
    @Override
    public void onEvent(ShowMessageEvent event) {
        Snackbar.make(activity.getProgressBar(), event.message, Snackbar.LENGTH_LONG).show();
    }

    @Subscribe
    @Override
    public void onEvent(LogoutEvent event) {
        IAccountManager accountManager = PodioAccountManager.getInstance(event.context);
        accountManager.signOut();

        Intent intent = new Intent(event.context, EntryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        event.context.startActivity(intent);
    }

    @Subscribe
    @Override
    public void onEvent(ShowEventDetailsEvent event) {
        dismissAllDialogs();
        EventDetailsFragment fragment = EventDetailsFragment.newInstance(event.itemId);
        fragment.setShowsDialog(true);
        fragment.show(activity.getSupportFragmentManager(), DIALOG);
    }

    private void dismissAllDialogs() {
        if(isPaused) return;

        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(DIALOG);
        if(fragment != null && fragment instanceof DialogFragment) {
            ((DialogFragment) fragment).dismissAllowingStateLoss();
        }
    }
}
