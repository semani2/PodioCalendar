package development.sai.podiocalendar.events;

/**
 * Created by sai on 8/2/16.
 */
public class ShowMessageEvent implements IEvent {

    public final String message;

    public ShowMessageEvent(String message) {
        this.message = message;
    }
}
