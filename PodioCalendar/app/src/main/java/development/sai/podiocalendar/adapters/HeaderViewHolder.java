package development.sai.podiocalendar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import development.sai.podiocalendar.R;

/**
 * Created by sai on 8/3/16.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView dateTextView;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
    }
}
