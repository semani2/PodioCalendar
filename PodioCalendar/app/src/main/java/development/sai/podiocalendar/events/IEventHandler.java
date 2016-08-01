package development.sai.podiocalendar.events;

/**
 * Created by sai on 8/1/16.
 */
public interface IEventHandler {

    public void onEvent(ProgressBarEvent event);

    public void onEvent(FABEvent event);

    public void onEvent(ShowFragmentEvent event);
}
