package development.sai.podiocalendar.events;

/**
 * Created by sai on 8/1/16.
 */
public interface IEventHandler {

    void onResume();

    void onPause();

    void onDestroy();

    void onEvent(ProgressBarEvent event);

    void onEvent(ShowFragmentEvent event);
}
