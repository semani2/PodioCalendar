package development.sai.podiocalendar.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.podio.sdk.Podio;
import com.podio.sdk.Request;
import com.podio.sdk.domain.User;

import development.sai.podiocalendar.R;

public class HomeFragment extends Fragment {

    private TextView userNameTextView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUser();
    }

    private void loadUser() {
        Podio.user.getUser().withResultListener(new Request.ResultListener<User>() {
            @Override
            public boolean onRequestPerformed(User content) {
                if(content != null) {
                    userNameTextView.setText("Welcome " + content.getEmailAddress());
                }
                return false;
            }
        })
        .withErrorListener(new Request.ErrorListener() {
            @Override
            public boolean onErrorOccurred(Throwable cause) {
                return false;
            }
        });
    }

    private void initView(View mainView) {
        userNameTextView = (TextView) mainView.findViewById(R.id.userNameTextView);
    }

}
