package com.example.malladam.AppGuarda;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by malladam on 07/05/2016.
 */
public class FragmentMain extends Fragment {

    ViewPager pager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main,container,false);

        pager= (ViewPager) v.findViewById(R.id.view_pager);
        tabLayout= (TabLayout) v.findViewById(R.id.tab_layout);

        FragmentManager manager=getActivity().getSupportFragmentManager();

        PageAdapter adapter=new PageAdapter(manager);

        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTabsFromPagerAdapter(adapter);
        return v;
    }
}
