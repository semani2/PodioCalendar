package development.sai.podiocalendar.adapters;

import java.util.Date;

/**
 * Created by sai on 8/3/16.
 */
public class HeaderItem extends ListItem {
    private final Date date;

    public HeaderItem(Date date) {
        this.date = date;
    }

    public Date getDate(){
        return this.date;
    }
    @Override
    public int getType() {
        return ListItem.TYPE_HEADER;
    }
}
