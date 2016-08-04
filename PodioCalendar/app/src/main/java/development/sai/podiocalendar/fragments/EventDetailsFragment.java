package development.sai.podiocalendar.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
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

import java.text.SimpleDateFormat;

import development.sai.podiocalendar.HomeActivity;
import development.sai.podiocalendar.R;
import development.sai.podiocalendar.events.ShowMessageEvent;
import development.sai.podiocalendar.widgets.LazyImageView;

/**
 * Created by sai on 8/3/16.
 */
public class EventDetailsFragment extends DialogFragment {
    private static final String ITEM_ID = "item_id";

    private TextView eventTitleTextView;
    private TextView createdByTextView;
    private TextView appNameTextView;
    private LazyImageView profileImageView;
    private LinearLayout contentLayout;
    private LinearLayout createdByLayout;
    private LinearLayout appNameLayout;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd, HH : mm");

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
        createdByTextView = (TextView) view.findViewById(R.id.createdByTextView);
        profileImageView = (LazyImageView) view.findViewById(R.id.profilePicImageView);
        appNameTextView = (TextView) view.findViewById(R.id.appNameTextView);
        contentLayout = (LinearLayout) view.findViewById(R.id.contentLayout);
        createdByLayout = (LinearLayout) view.findViewById(R.id.createdByLayout);
        appNameLayout = (LinearLayout) view.findViewById(R.id.appNameLayout);
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
                createdByLayout.setVisibility(View.VISIBLE);
                appNameLayout.setVisibility(View.VISIBLE);
                eventTitleTextView.setText(content.getTitle());
                createdByTextView.setText(content.getCreatedBy().getName());
                profileImageView.loadImage(((HomeActivity)getActivity()).getImageLoader(), ImageLoader.Size.AVATAR_LARGE, content.getCreatedBy().getImageUrl());
                appNameTextView.setText(content.getApplication().getName());

                for(Field field : content.getFields()) {
                    Log.d("FIELDS :",field.getLabel());
                    if(field instanceof TextField || field instanceof DateField || field instanceof LocationField) {
                        LinearLayout newField = new LinearLayout(getContext());
                        TextView titleTextView = new TextView(getContext());
                        TextView valueTextView = new TextView(getContext());

                        newField.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        newField.setOrientation(LinearLayout.VERTICAL);

                        titleTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        titleTextView.setTextAppearance(R.style.FieldTitleStyle);
                        titleTextView.setText(field.getLabel());

                        valueTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        valueTextView.setTextAppearance(R.style.FieldValueStyle);
                        if(field instanceof TextField) {
                            valueTextView.setText(Html.fromHtml(((TextField.Value) content.getVerifiedValue(field.getExternalId(), 0)).getValue()));
                        }
                        else if(field instanceof DateField) {
                            if(((DateField.Value) content.getVerifiedValue(field.getExternalId(), 0)).getStartDateTime() != null &&
                                    ((DateField.Value) content.getVerifiedValue(field.getExternalId(), 0)).getEndDateTime() != null) {
                                String startDate = dateFormat.format(((DateField.Value) content.getVerifiedValue(field.getExternalId(), 0)).getStartDateTime());
                                String endDate = dateFormat.format(((DateField.Value) content.getVerifiedValue(field.getExternalId(), 0)).getEndDateTime());
                                valueTextView.setText(startDate + " - " + endDate);
                            }
                            else if(((DateField.Value) content.getVerifiedValue(field.getExternalId(), 0)).getStartDateTime() != null) {
                                String date = dateFormat.format(((DateField.Value) content.getVerifiedValue(field.getExternalId(), 0)).getStartDateTime());
                                valueTextView.setText(date);
                            }
                            else if(((DateField.Value) content.getVerifiedValue(field.getExternalId(), 0)).getEndDateTime() != null) {
                                String date = dateFormat.format(((DateField.Value) content.getVerifiedValue(field.getExternalId(), 0)).getEndDateTime());
                                valueTextView.setText(date);
                            }
                        }
                        else if(field instanceof LocationField) {
                            valueTextView.setText(((LocationField.Value) content.getVerifiedValue(field.getExternalId(), 0)).getValue());
                        }

                        newField.addView(titleTextView);
                        newField.addView(valueTextView);
                        contentLayout.addView(newField);
                    }

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
