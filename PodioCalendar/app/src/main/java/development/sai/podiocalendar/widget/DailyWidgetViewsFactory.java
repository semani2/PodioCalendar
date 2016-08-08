package development.sai.podiocalendar.widget;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.podio.sdk.Podio;
import com.podio.sdk.domain.CalendarEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import development.sai.podiocalendar.R;
import development.sai.podiocalendar.account.IAccountManager;
import development.sai.podiocalendar.account.PodioAccountManager;
import development.sai.podiocalendar.sdk.GlobalRequestListener;

/**
 * Created by sai on 8/4/16.
 */
public class DailyWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory{
    private ArrayList<CalendarEvent> dailyEvents = new ArrayList<>();
    private Context context;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public DailyWidgetViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        GlobalRequestListener listener = GlobalRequestListener.getInstance(context);
        IAccountManager accountManager = PodioAccountManager.getInstance(context);
        accountManager.resetPodioSdk(context, listener, listener);
        initRemoteViews();
    }

    private void initRemoteViews() {
        if(PodioAccountManager.getInstance(context).hasSession()) {
            dailyEvents.clear();
            Date today = Calendar.getInstance().getTime();
            dailyEvents = new ArrayList<>(Arrays.asList(Podio.calendar.getGlobalCalendar(today, today, 1, false).waitForResult(15)));
            Iterator<CalendarEvent> iterator = dailyEvents.iterator();
            while(iterator.hasNext()) {
                CalendarEvent event = iterator.next();
                if(event.hasStartTime() && event.hasEndTime()) {
                    if(event.getEndDate().before(new Date())) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    @Override
    public void onDataSetChanged() {
        initRemoteViews();
    }

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {
        return dailyEvents.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.events_widget_list_row);
        CalendarEvent calendarEvent = dailyEvents.get(position);
        if(calendarEvent.hasStartTime()) {
            remoteViews.setTextViewText(R.id.startTimeTextView, timeFormat.format(calendarEvent.getStartDate()));
        }
        else {
            remoteViews.setTextViewText(R.id.startTimeTextView, "All day");
        }

        remoteViews.setTextViewText(R.id.widgetTitleTextView, calendarEvent.getTitle());

        if(calendarEvent.getApp()!= null && calendarEvent.getApp().getName() != null) {
            remoteViews.setTextViewText(R.id.widgetAppTextView, calendarEvent.getApp().getName());
        }
        else {
            remoteViews.setViewVisibility(R.id.widgetAppTextView, View.GONE);
        }
        final Intent fillInIntent = new Intent();
        final Bundle data = new Bundle();
        data.putLong(DailyEventsWidgetProvider.ITEM_ID, calendarEvent.getRefId());
        fillInIntent.putExtras(data);

        remoteViews.setOnClickFillInIntent(R.id.widgetTitleTextView, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
