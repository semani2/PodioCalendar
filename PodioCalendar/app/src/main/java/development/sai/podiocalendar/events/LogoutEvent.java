package development.sai.podiocalendar.events;

import android.content.Context;

/**
 * Created by sai on 8/3/16.
 */
public class LogoutEvent implements IEvent {

    public final Context context;

    public LogoutEvent(Context context) {
        this.context = context;
    }
}
