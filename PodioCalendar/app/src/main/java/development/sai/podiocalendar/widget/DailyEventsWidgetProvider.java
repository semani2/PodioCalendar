package development.sai.podiocalendar.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import development.sai.podiocalendar.HomeActivity;
import development.sai.podiocalendar.R;

/**
 * Created by sai on 8/4/16.
 */
public class DailyEventsWidgetProvider extends AppWidgetProvider {
    public static final String ITEM_ID = "item_id";
    public static String SHOW_EVENT_ACTION = "development.sai.podiocalendar.widget.SHOW_EVENT_ACTION";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd");

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase(SHOW_EVENT_ACTION)) {
            Long itemId = intent.getExtras().getLong(ITEM_ID);

            Intent toHome = new Intent(context, HomeActivity.class);
            Bundle data = new Bundle();
            data.putLong(ITEM_ID, itemId);
            toHome.putExtras(data);

            toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            toHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(toHome);
            return;
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i=0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            Intent intent = new Intent(context, DailyEventsWidgetViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.daily_events_widget_layout);
            rv.setRemoteAdapter(appWidgetId, R.id.eventListView, intent);
            rv.setTextViewText(R.id.widgetDateTextView, dateFormat.format(new Date()));

            Intent toastIntent = new Intent(context, DailyEventsWidgetProvider.class);

            toastIntent.setAction(SHOW_EVENT_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            toastIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0 ,
                    toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            rv.setPendingIntentTemplate(R.id.eventListView, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
