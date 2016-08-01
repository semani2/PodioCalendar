package development.sai.podiocalendar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;

import development.sai.podiocalendar.account.PodioAccountManager;
import development.sai.podiocalendar.events.ShowFragmentEvent;
import development.sai.podiocalendar.fragments.SplashScreenFragment;
import development.sai.podiocalendar.account.IAccountManager;

public class MainActivity extends AppCompatActivity {
    private MainEventHandler mainEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainEventHandler = MainEventHandler.getInstance(this);
        mainEventHandler.onResume();

        IAccountManager accountManager = PodioAccountManager.getInstance(this);
        accountManager.resetPodioSdk(this, mainEventHandler, mainEventHandler);
        Bundle loggedInData = accountManager.readPodioAccountInfo();
        showSplashScreen(loggedInData);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainEventHandler.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainEventHandler.onDestroy();
    }

    private void showSplashScreen(Bundle bundle) {
        hideFAB();
        String realName = bundle.getString(CalendarApplication.ARG_REAL_NAME);
        String transferToken = bundle.getString(CalendarApplication.ARG_TRANSFER_TOKEN);
        EventBus eventBus = EventBus.getDefault();
        eventBus.post(new ShowFragmentEvent(SplashScreenFragment.newInstance(realName, transferToken)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showFAB() {
        findViewById(R.id.fab).setVisibility(View.VISIBLE);
    }

    public void hideFAB() {
        findViewById(R.id.fab).setVisibility(View.GONE);
    }

    public ProgressBar getProgressBar() {
        return (ProgressBar) findViewById(R.id.mainProgressBar);
    }
}
