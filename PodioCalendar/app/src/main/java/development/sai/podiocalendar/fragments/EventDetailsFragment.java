package development.sai.podiocalendar.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.podio.sdk.ImageLoader;
import com.podio.sdk.Podio;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Item;
import com.podio.sdk.domain.field.DateField;
import com.podio.sdk.domain.field.Field;
import com.podio.sdk.domain.field.LocationField;
import com.podio.sdk.domain.field.TextField;

import org.greenrobot.eventbus.EventBus;

import development.sai.podiocalendar.HomeActivity;
import development.sai.podiocalendar.R;
import development.sai.podiocalendar.events.ShowMessageEvent;
import development.sai.podiocalendar.widgets.LazyImageView;

/**
 * Created by sai on 8/3/16.
 */
public class EventDetailsFragment extends DialogFragment {
    private static final String ITEM_ID = "item_id";
    private static final String APP_ID = "app_id";
    private static final String TIME = "time";
    private static final String AGENDA = "agenda";
    private static final String LOCATION = "location";

    private TextView eventTitleTextView;
    private TextView agendaTextView;
    private TextView durationTextView;
    private TextView createdByTextView;
    private TextView appNameTextView;
    private TextView locationTextView;
    private LazyImageView profileImageView;
    private LinearLayout agendaLayout;
    private LinearLayout durationLayout;
    private LinearLayout locationLayout;

    private ProgressBar progressBar;

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    public static EventDetailsFragment newInstance(long itemId) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_event_details, container, false);

        eventTitleTextView = (TextView) view.findViewById(R.id.eventTitleTextView);
        agendaTextView = (TextView) view.findViewById(R.id.agendaTextView);
        durationTextView = (TextView) view.findViewById(R.id.durationTextView);
        createdByTextView = (TextView) view.findViewById(R.id.createdByTextView);
        profileImageView = (LazyImageView) view.findViewById(R.id.profilePicImageView);
        appNameTextView = (TextView) view.findViewById(R.id.appNameTextView);
        locationTextView = (TextView) view.findViewById(R.id.locationTextView);
        agendaLayout = (LinearLayout) view.findViewById(R.id.agendaLayout);
        locationLayout = (LinearLayout) view.findViewById(R.id.locationLayout);
        durationLayout = (LinearLayout) view.findViewById(R.id.durationLayout);
        progressBar = (ProgressBar) view.findViewById(R.id.detailsProgressBar);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getArguments();
        if(bundle != null) {
            loadItemDetails(bundle.getLong(ITEM_ID));
        }
    }

    private void loadItemDetails(long itemId) {
        showProgress(true);

        Podio.item.get(itemId).withResultListener(new Request.ResultListener<Item>() {
            @Override
            public boolean onRequestPerformed(Item content) {
                boolean showAgenda = false, showLocation = false, showDuration = false;

                for(Field field: content.getFields()) {
                    Log.d("FIELDS :",field.getLabel().toString());
                    switch (field.getLabel().toLowerCase()) {
                        case TIME:
                            showDuration = true;
                            break;
                        case LOCATION:
                            showLocation = true;
                            break;
                        case AGENDA:
                            showAgenda = true;
                            break;
                    }
                }
                eventTitleTextView.setText(content.getTitle());
                createdByTextView.setText(content.getCreatedBy().getName());
                profileImageView.loadImage(((HomeActivity)getActivity()).getImageLoader(), ImageLoader.Size.AVATAR_LARGE, content.getCreatedBy().getImageUrl());
                appNameTextView.setText(content.getApplication().getName());

                if(showAgenda && content.getVerifiedValues("agenda") != null) {
                    agendaLayout.setVisibility(View.VISIBLE);
                    agendaTextView.setText(((TextField.Value)content.getVerifiedValue("agenda", 0)).getValue());
                }

                if(showDuration && content.getVerifiedValues("time") != null) {
                    durationLayout.setVisibility(View.VISIBLE);
                    durationTextView.setText(((DateField.Value)content.getVerifiedValue("time", 0)).getStartDateTime().toString() + " - " +
                            ((DateField.Value)content.getVerifiedValue("time", 0)).getEndDateTime().toString());
                }

                if(showLocation && content.getVerifiedValues("location") != null) {
                    locationLayout.setVisibility(View.VISIBLE);
                    locationTextView.setText(((LocationField.Value)content.getVerifiedValue("location", 0)).getValue());
                }

                showProgress(false);
                return false;
            }
        })
        .withErrorListener(new Request.ErrorListener() {
            @Override
            public boolean onErrorOccurred(Throwable cause) {
                EventBus.getDefault().post(new ShowMessageEvent(cause.getLocalizedMessage()));
                showProgress(false);
                return false;
            }
        });
    }

    private void showProgress(boolean show) {
        if(show) {
            progressBar.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
