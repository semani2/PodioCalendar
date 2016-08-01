package development.sai.podiocalendar.events;

/**
 * Created by sai on 8/1/16.
 */
public class ProgressBarEvent implements IEvent {
    public final boolean showProgressBar;

    public ProgressBarEvent(boolean showProgressBar) {
        this.showProgressBar = showProgressBar;
    }
}
