package com.example.malladam.AppGuarda.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.malladam.AppGuarda.AsientosFragment;
import com.example.malladam.AppGuarda.UbicacionFragment;

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
                frag=new UbicacionFragment();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title=" ";
        switch (position){
            case 0:
                title="Asientos";
                break;
            case 1:
                title="Ubicación";
                break;
        }

        return title;
    }
}
