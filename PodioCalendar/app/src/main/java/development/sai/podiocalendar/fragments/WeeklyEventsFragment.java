package development.sai.podiocalendar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import development.sai.podiocalendar.R;

/**
 * Created by sai on 8/2/16.
 */
public class WeeklyEventsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.weekly_events_tab,container,false);
        return v;
    }
}
