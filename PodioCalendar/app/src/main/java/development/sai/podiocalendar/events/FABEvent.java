package development.sai.podiocalendar.events;

/**
 * Created by sai on 8/1/16.
 */
public class FABEvent implements IEvent {
    public final boolean showFAB;

    public FABEvent(boolean showFAB) {
        this.showFAB = showFAB;
    }
}
