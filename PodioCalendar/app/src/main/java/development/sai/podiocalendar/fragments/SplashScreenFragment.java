package development.sai.podiocalendar.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import development.sai.podiocalendar.CalendarApplication;
import development.sai.podiocalendar.R;
import development.sai.podiocalendar.authentication.AuthManager;
import development.sai.podiocalendar.authentication.IAuthManager;
import development.sai.podiocalendar.events.ShowFragmentEvent;

public class SplashScreenFragment extends Fragment {
    private Button acceptButton;
    private Button switchUserButton;
    private Button signInButton;
    private TextView helloTextView;

    public SplashScreenFragment() {
        // Required empty public constructor
    }

    public static SplashScreenFragment newInstance(String userName, String transferToken) {
        SplashScreenFragment fragment = new SplashScreenFragment();
        Bundle args = new Bundle();
        args.putString(CalendarApplication.ARG_REAL_NAME, userName);
        args.putString(CalendarApplication.ARG_TRANSFER_TOKEN, transferToken);
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        View view =  inflater.inflate(R.layout.fragment_splash_screen, container, false);
        initView(view);
        
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void initView(View mainView) {
        acceptButton = (Button) mainView.findViewById(R.id.startUsingCalendarButton);
        switchUserButton = (Button) mainView.findViewById(R.id.switchUserButton);
        helloTextView = (TextView) mainView.findViewById(R.id.splashScreenTextView);
        signInButton = (Button) mainView.findViewById(R.id.signInButton);

        Bundle args = getArguments();
        if(args != null && args.containsKey(CalendarApplication.ARG_REAL_NAME) && args.containsKey(CalendarApplication.ARG_TRANSFER_TOKEN)) {
            signInButton.setVisibility(View.GONE);
            acceptButton.setVisibility(View.VISIBLE);
            switchUserButton.setVisibility(View.VISIBLE);

            helloTextView.setText(getString(R.string.strHelloUser, args.getString(CalendarApplication.ARG_REAL_NAME)));
        }
        else {
            helloTextView.setText(getString(R.string.strHelloStranger));
            signInButton.setVisibility(View.VISIBLE);
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLoginWithToken();
            }
        });

        switchUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
    }

    private void performLoginWithToken() {
        IAuthManager authManager = AuthManager.getInstance(getActivity());
        authManager.authenticateWithTransferToken();
    }

    private void goToLogin() {
        LoginFragment loginFragment = LoginFragment.newInstance();
        showFragment(loginFragment);
    }


    private void showFragment(Fragment fragment) {
        EventBus eventBus = EventBus.getDefault();
        eventBus.post(new ShowFragmentEvent(fragment));
    }
}
