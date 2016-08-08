package development.sai.podiocalendar.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by sai on 8/4/16.
 */
public class DailyEventsWidgetViewsService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        DailyWidgetViewsFactory viewsFactory = new DailyWidgetViewsFactory(getApplicationContext());
        return viewsFactory;
    }
}
