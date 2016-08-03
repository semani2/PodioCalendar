package development.sai.podiocalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import development.sai.podiocalendar.account.IAccountManager;
import development.sai.podiocalendar.account.PodioAccountManager;
import development.sai.podiocalendar.events.IEventHandler;
import development.sai.podiocalendar.fragments.SplashScreenFragment;
import development.sai.podiocalendar.helpers.EntryFragmentHelper;
import development.sai.podiocalendar.helpers.IFragmentHelper;
import development.sai.podiocalendar.sdk.GlobalRequestListener;

public class EntryActivity extends AppCompatActivity {

    private IEventHandler entryEventHandler;

    public EntryActivity() {
        entryEventHandler = new EntryEventHandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_entry);
    }

    @Override
    protected void onResume() {
        super.onResume();
        entryEventHandler.onResume();
        IAccountManager accountManager = PodioAccountManager.getInstance(this);
        GlobalRequestListener listener = GlobalRequestListener.getInstance(this);
        accountManager.resetPodioSdk(this, listener, listener);
        Bundle loggedInData = accountManager.readPodioAccountInfo();

        if(accountManager.hasSession()) {
            goHome();
        }
        else {
            showSplashScreen(loggedInData);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        entryEventHandler.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        entryEventHandler.onDestroy();
    }

    private void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void showSplashScreen(Bundle bundle) {
        String realName = bundle.getString(CalendarApplication.ARG_REAL_NAME);
        String transferToken = bundle.getString(CalendarApplication.ARG_TRANSFER_TOKEN);
        IFragmentHelper fragmentHelper = new EntryFragmentHelper();
        fragmentHelper.showFragment(getSupportFragmentManager(), SplashScreenFragment.newInstance(realName, transferToken));
    }

    public ProgressBar getProgressBar() {
        return (ProgressBar) findViewById(R.id.entryProgressBar);
    }
}
