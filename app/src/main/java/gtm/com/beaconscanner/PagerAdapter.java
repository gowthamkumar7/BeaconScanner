package gtm.com.beaconscanner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Administrator on 3/6/2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private String mTitles[] = new String[]{Constants.REGISTERED_TITLE, Constants.NEW_TITLE};

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new RegisteredFragment();
        } else {
            return new NewBeaconsFragment();
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
