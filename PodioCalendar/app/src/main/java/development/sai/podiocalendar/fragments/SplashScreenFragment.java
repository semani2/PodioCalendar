package development.sai.podiocalendar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import development.sai.podiocalendar.CalendarApplication;
import development.sai.podiocalendar.R;
import development.sai.podiocalendar.authentication.AuthManager;
import development.sai.podiocalendar.authentication.IAuthManager;
import development.sai.podiocalendar.events.ProgressBarEvent;

public class SplashScreenFragment extends Fragment {
    private Button acceptButton;
    private TextView helloTextView;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private LinearLayout splashFooter;

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
        helloTextView = (TextView) mainView.findViewById(R.id.splashScreenTextView);
        signInButton = (Button) mainView.findViewById(R.id.signInButton);
        splashFooter = (LinearLayout) mainView.findViewById(R.id.splashFooter);
        userNameEditText = (EditText) mainView.findViewById(R.id.userNameEditText);
        setUpEditText(userNameEditText);
        passwordEditText = (EditText) mainView.findViewById(R.id.passwordEditText);
        setUpEditText(passwordEditText);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLoginWithCredentials();
            }
        });

        Bundle args = getArguments();
        if(args != null && args.containsKey(CalendarApplication.ARG_REAL_NAME) && args.containsKey(CalendarApplication.ARG_TRANSFER_TOKEN)) {
            acceptButton.setVisibility(View.VISIBLE);

            helloTextView.setText(getString(R.string.strHelloUser, args.getString(CalendarApplication.ARG_REAL_NAME)));
            splashFooter.setVisibility(View.VISIBLE);
        }
        else {
            splashFooter.setVisibility(View.GONE);
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLoginWithToken();
            }
        });
    }

    private void performLoginWithToken() {
        IAuthManager authManager = AuthManager.getInstance(getActivity());
        authManager.authenticateWithTransferToken();
    }

    private void performLoginWithCredentials() {
        signInButton.setEnabled(false);
        EventBus.getDefault().post(new ProgressBarEvent(true));

        String email = userNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        IAuthManager authManager = AuthManager.getInstance(getActivity());
        authManager.authenticateWithCredentials(email, password);
    }

    private void setUpEditText(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                boolean shouldEnable = userNameEditText.length() > 0 && passwordEditText.length() > 0;
                signInButton.setEnabled(shouldEnable);
            }
        });
    }
}
