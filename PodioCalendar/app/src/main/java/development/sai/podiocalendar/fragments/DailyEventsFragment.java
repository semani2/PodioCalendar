package development.sai.podiocalendar.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.podio.sdk.Podio;
import com.podio.sdk.Request;
import com.podio.sdk.domain.CalendarEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import development.sai.podiocalendar.R;
import development.sai.podiocalendar.adapters.DailyEventsAdapter;
import development.sai.podiocalendar.adapters.RecyclerTouchListener;
import development.sai.podiocalendar.events.ProgressBarEvent;
import development.sai.podiocalendar.events.ShowEventDetailsEvent;
import development.sai.podiocalendar.events.ShowMessageEvent;
import development.sai.podiocalendar.widget.DailyEventsWidgetProvider;

/**
 * Created by sai on 8/2/16.
 */
public class DailyEventsFragment extends Fragment {

    private final EventBus eventBus;

    private RecyclerView eventsRecyclerView;

    private DailyEventsAdapter dailyEventsAdapter;

    private List<CalendarEvent> calendarEventList = new ArrayList<>();

    public DailyEventsFragment() {
        eventBus =  EventBus.getDefault();
    }

    public static DailyEventsFragment getInstance() {
        return new DailyEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.daily_events_tab,container,false);

        initView(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTodaysCalendarEvents();
    }

    private void loadTodaysCalendarEvents() {
        eventBus.post(new ProgressBarEvent(true));
        Date today = Calendar.getInstance().getTime();
        Podio.calendar.getGlobalCalendar(today, today, 1, false)
                .withResultListener(new Request.ResultListener<CalendarEvent[]>() {
                    @Override
                    public boolean onRequestPerformed(CalendarEvent[] content) {
                        if(content.length > 0) {
                            eventBus.post(new ProgressBarEvent(false));

                            calendarEventList = new ArrayList<>(Arrays.asList(content));

                            dailyEventsAdapter = new DailyEventsAdapter(calendarEventList);

                            eventsRecyclerView.setAdapter(dailyEventsAdapter);
                            dailyEventsAdapter.notifyDataSetChanged();
                        }
                        AppWidgetManager widgetManager = AppWidgetManager.getInstance(getActivity());
                        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(getActivity(), DailyEventsWidgetProvider.class));

                        widgetManager.notifyAppWidgetViewDataChanged(ids, R.id.eventListView);
                        return false;
                    }
                })
                .withErrorListener(new Request.ErrorListener() {
                    @Override
                    public boolean onErrorOccurred(Throwable cause) {
                        eventBus.post(new ProgressBarEvent(false));
                        eventBus.post(new ShowMessageEvent(cause.getLocalizedMessage()));
                        return false;
                    }
                });


    }

    private void initView(View v) {
        eventsRecyclerView = (RecyclerView) v.findViewById(R.id.eventsRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        eventsRecyclerView.setLayoutManager(mLayoutManager);
        eventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eventsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), eventsRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CalendarEvent calendarEvent = calendarEventList.get(position);
                showDetailsDialog(calendarEvent.getRefId());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void showDetailsDialog(Long itemId) {
        eventBus.post(new ShowEventDetailsEvent(itemId));
    }
}
