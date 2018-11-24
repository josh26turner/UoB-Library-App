package spe.uoblibraryapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;



public class CustomPagerAdapter extends FragmentStatePagerAdapter{

    private final List<Fragment> mFragmentList= new ArrayList<>();
    private final List<String> mFragmentTitleList= new ArrayList<>(); //not required but helpful to have.

    public CustomPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public boolean fragmentExists(String fragmentName){
        if (mFragmentTitleList.contains(fragmentName))
            return true;
        else return false;
    }

    public int getFragmentIndex(String fragmentName){
        return mFragmentTitleList.indexOf(fragmentName);
    }


}
