package development.sai.podiocalendar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.podio.sdk.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import development.sai.podiocalendar.events.IEventHandler;
import development.sai.podiocalendar.events.LogoutEvent;
import development.sai.podiocalendar.events.ShowEventDetailsEvent;
import development.sai.podiocalendar.fragments.DailyEventsFragment;
import development.sai.podiocalendar.fragments.WeeklyEventsFragment;
import development.sai.podiocalendar.widget.DailyEventsWidgetProvider;

public class HomeActivity extends AppCompatActivity {
    private IEventHandler mainEventHandler;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EventBus eventBus = EventBus.getDefault();
    private ImageLoader imageLoader;

    public HomeActivity() {
        mainEventHandler = new HomeEventhandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        imageLoader = new ImageLoader();
        imageLoader.setup(this, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainEventHandler.onResume();

        Bundle data = getIntent().getExtras();
        if(data != null && data.containsKey(DailyEventsWidgetProvider.ITEM_ID)) {
            eventBus.post(new ShowEventDetailsEvent(data.getLong(DailyEventsWidgetProvider.ITEM_ID)));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainEventHandler.onPause();
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

        if (id == R.id.action_logout) {
            eventBus.post(new LogoutEvent(this));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ProgressBar getProgressBar() {
        return (ProgressBar) findViewById(R.id.mainProgressBar);
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new DailyEventsFragment();

                case 1:
                    return new WeeklyEventsFragment();

                default:
                    return new DailyEventsFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return getString(R.string.strToday);

                case 1:
                    return getString(R.string.strThisWeek);

                default:
                    return getString(R.string.strToday);
            }
        }
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
