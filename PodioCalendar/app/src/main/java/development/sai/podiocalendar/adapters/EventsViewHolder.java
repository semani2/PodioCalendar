package development.sai.podiocalendar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import development.sai.podiocalendar.R;

/**
 * Created by sai on 8/3/16.
 */
public class EventsViewHolder extends RecyclerView.ViewHolder {
    public TextView eventStartTime;
    public TextView eventEndTime;
    public TextView eventTitleTextView;
    public TextView eventAppTextView;

    public EventsViewHolder(View itemView) {
        super(itemView);
        eventStartTime = (TextView) itemView.findViewById(R.id.eventStartTimeTextView);
        eventEndTime = (TextView) itemView.findViewById(R.id.eventEndTimeTextView);
        eventTitleTextView = (TextView) itemView.findViewById(R.id.eventTitleTextView);
        eventAppTextView = (TextView) itemView.findViewById(R.id.eventAppTextView);
    }
}
