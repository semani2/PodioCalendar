package development.sai.podiocalendar.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import development.sai.podiocalendar.R;

/**
 * Created by sai on 8/2/16.
 */
public class EntryFragmentHelper implements IFragmentHelper{

    @Override
    public void showFragment(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction()
                .add(R.id.entryContainer, fragment)
                .commit();
    }
}
