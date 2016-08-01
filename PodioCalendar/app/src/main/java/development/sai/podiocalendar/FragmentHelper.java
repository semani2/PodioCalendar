package development.sai.podiocalendar;

import android.app.Fragment;
import android.app.FragmentManager;

/**
 * Created by sai on 7/29/16.
 */
public class FragmentHelper {
    private final FragmentManager fragmentManager;

    public FragmentHelper(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void showFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
