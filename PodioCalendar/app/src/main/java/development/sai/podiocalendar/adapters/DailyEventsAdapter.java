package development.sai.podiocalendar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.podio.sdk.domain.CalendarEvent;

import java.text.SimpleDateFormat;
import java.util.List;

import development.sai.podiocalendar.R;

/**
 * Created by sai on 8/3/16.
 */
public class DailyEventsAdapter extends RecyclerView.Adapter<EventsViewHolder> {
    private List<CalendarEvent> calendarEventList;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public DailyEventsAdapter(List<CalendarEvent> calendarEvents) {
        this.calendarEventList = calendarEvents;
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list_row, parent, false);

        return new EventsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventsViewHolder holder, int position) {
        CalendarEvent calendarEvent = calendarEventList.get(position);
        if(calendarEvent.hasStartTime()) {
            holder.eventStartTime.setText(timeFormat.format(calendarEvent.getStartDate()));

            if(calendarEvent.hasEndTime()) {
                holder.eventEndTime.setText(timeFormat.format(calendarEvent.getEndDate()));
            }
        }
        else {
            holder.eventStartTime.setText("All day");
            holder.eventEndTime.setVisibility(View.GONE);
        }

        holder.eventTitleTextView.setText(calendarEvent.getTitle());
        if(calendarEvent.getApp() != null && calendarEvent.getApp().getName() != null) {
            holder.eventAppTextView.setText(calendarEvent.getApp().getName());
        }
    }

    @Override
    public int getItemCount() {
        return calendarEventList.size();
    }
}
