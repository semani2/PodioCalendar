package development.sai.podiocalendar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.podio.sdk.domain.CalendarEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import development.sai.podiocalendar.R;

/**
 * Created by sai on 8/3/16.
 */
public class WeeklyEventsAdapter extends RecyclerView.Adapter {
    private List<ListItem> eventItemList;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat headerDateFormat = new SimpleDateFormat("E, MMM dd");

    public WeeklyEventsAdapter(List<ListItem> calendarEvents) {
        this.eventItemList = calendarEvents;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ListItem.TYPE_EVENT) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list_row, parent, false);

            return new EventsViewHolder(itemView);
        }
        else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_list_row, parent, false);

            return new HeaderViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int type = getItemViewType(position);
        if(type == ListItem.TYPE_HEADER) {
            Date date = ((HeaderItem)eventItemList.get(position)).getDate();
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;

            headerViewHolder.dateTextView.setText(headerDateFormat.format(date));
        }
        else {
            CalendarEvent calendarEvent = ((EventItem)eventItemList.get(position)).getCalendarEvent();
            EventsViewHolder holder = (EventsViewHolder) viewHolder;
            holder.eventEndTime.setVisibility(View.VISIBLE);
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
    }

    @Override
    public int getItemCount() {
        return eventItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return eventItemList.get(position).getType();
    }
}
