package development.sai.podiocalendar.adapters;

import com.podio.sdk.domain.CalendarEvent;

/**
 * Created by sai on 8/3/16.
 */
public class EventItem extends ListItem {
    private final CalendarEvent calendarEvent;

    public EventItem(CalendarEvent calendarEvent) {
        this.calendarEvent = calendarEvent;
    }

    public CalendarEvent getCalendarEvent(){
        return this.calendarEvent;
    }

    @Override
    public int getType() {
        return ListItem.TYPE_EVENT;
    }
}
