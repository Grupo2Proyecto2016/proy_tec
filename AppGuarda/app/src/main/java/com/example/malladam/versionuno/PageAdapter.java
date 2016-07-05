package com.example.malladam.versionuno;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by malladam on 07/05/2016.
 */
public class PageAdapter extends FragmentStatePagerAdapter{
    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag=null;
        switch (position){
            case 0:
                frag=new AsientosFragment();
                break;
            case 1:
                frag=new SegundoFragment();
                break;
            case 2:
                frag=new GameFragment();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title=" ";
        switch (position){
            case 0:
                title="Asientos";
                break;
            case 1:
                title="Oficiales";
                break;
            case 2:
                title="Paradas";
                break;
        }

        return title;
    }
}
