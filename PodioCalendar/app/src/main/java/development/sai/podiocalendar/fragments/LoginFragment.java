package development.sai.podiocalendar.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import development.sai.podiocalendar.R;
import development.sai.podiocalendar.authentication.AuthManager;
import development.sai.podiocalendar.authentication.IAuthManager;
import development.sai.podiocalendar.events.ProgressBarEvent;

public class LoginFragment extends Fragment {

    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button signInButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_login, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        userNameEditText = (EditText) v.findViewById(R.id.userNameEditText);
        setUpEditText(userNameEditText);
        passwordEditText = (EditText) v.findViewById(R.id.passwordEditText);
        setUpEditText(passwordEditText);

        signInButton = (Button) v.findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });
    }

    private void performLogin() {
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

    @Override
    public void onResume() {
        super.onResume();
    }
}
