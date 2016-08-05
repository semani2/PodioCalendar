package development.sai.podiocalendar.events;

/**
 * Created by sai on 8/5/16.
 */
public class ShowEventDetailsEvent implements IEvent {
    public final Long itemId;

    public ShowEventDetailsEvent(Long itemId) {
        this.itemId = itemId;
    }
}
