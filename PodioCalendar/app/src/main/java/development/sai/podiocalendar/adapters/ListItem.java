package development.sai.podiocalendar.adapters;

/**
 * Created by sai on 8/3/16.
 */
public abstract class ListItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_EVENT = 1;

    abstract public int getType();
}
