package com.example.malladam.AppGuarda;

import android.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.malladam.AppGuarda.models.Empresa;
import com.example.malladam.AppGuarda.utils.MenuTintUtils;


/**
 * Created by malladam on 07/05/2016.
 */
public class FragmentMain extends Fragment {

    ViewPager pager;
    TabLayout tabLayout;
    private Empresa empresa;
    private DataBaseManager dbManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main,container,false);

        dbManager = new DataBaseManager(getActivity().getApplicationContext());
        empresa = empresa.getInstance();
        setHasOptionsMenu(true);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuTintUtils menuTintUtils = new MenuTintUtils();
        menuTintUtils.tintAllIcons(menu, Color.parseColor(empresa.getColorHeader()));
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.action_finish_viaje:
                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.action_finish_viaje)+" "+dbManager.getViajeActual().getId_viaje())
                        .setMessage("Desea finalizar el viaje actual ?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //WScomunicarFinDelViaje
                                dbManager.eliminarViaje();

                                Toast.makeText(getActivity(), "El viaje ha sido finalizado con éxito", Toast.LENGTH_LONG).show();

                                Fragment newFragment = new FragmentIniciarViaje();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                transaction.replace(R.id.frameMain, newFragment);
                                transaction.addToBackStack(null);

                                // Commit the transaction
                                transaction.commit();

                                FragmentIniciarViaje fragmentIniciarViaje = new FragmentIniciarViaje();
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frameMain,fragmentIniciarViaje);
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whitch){
                                //NOT
                            }
                        })

                        .setIcon(R.drawable.bus_icon)
                        .show();

                return true;
        }
        return false;
    }


}
