package development.sai.podiocalendar.events;

import android.app.Fragment;

/**
 * Created by sai on 8/1/16.
 */
public class ShowFragmentEvent implements IEvent {
    public final Fragment fragment;

    public ShowFragmentEvent(Fragment fragment) {
        this.fragment = fragment;
    }
}
