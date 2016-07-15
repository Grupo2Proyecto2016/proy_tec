package com.example.malladam.AppGuarda;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.malladam.AppGuarda.Activity.QrActivity;
import com.example.malladam.AppGuarda.adapters.PasajeArrayAdapter;
import com.example.malladam.AppGuarda.models.AsientoActivo;
import com.example.malladam.AppGuarda.models.Empresa;
import com.example.malladam.AppGuarda.models.GroupPasajeDT;
import com.example.malladam.AppGuarda.models.PasajeDataType;
import com.example.malladam.AppGuarda.utils.MenuTintUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AsientosFragment extends Fragment {

    private DataBaseManager dbManager;
    ListView lista;
    PasajeArrayAdapter<GroupPasajeDT> adaptador;
    int asientosBus;// = 44
    static TextView infoAsiento;
    private Empresa empresa;

    public AsientosFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asientos, container, false);


    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        dbManager = new DataBaseManager(getContext());
        empresa = empresa.getInstance();
        setHasOptionsMenu(true);

        asientosBus = dbManager.getViajeActual().getCantAsientos_vehiculo();
        infoAsiento = (TextView) getView().findViewById(R.id.infoAsiento);

        /////JUEGO DE DATOS///

        List<AsientoActivo> asientosActivos =dbManager.getAsientosActivos();

        AsientoActivo[] asientosTodos = new AsientoActivo[asientosBus];

        for (int i = 0 ; i<asientosBus; i++) {
            if (buscarByNroAsiento(asientosActivos, i + 1) == null){
                asientosTodos[i] = new AsientoActivo();
            }
            else{
                asientosTodos[i]=buscarByNroAsiento(asientosActivos, i + 1);
            }
        }

        List<GroupPasajeDT> pasajesAgrupados = new ArrayList<>();

        for (int item = 0; item < asientosBus; ) {
            GroupPasajeDT grupoPasajes = new GroupPasajeDT();
            for (int itemInterno = 0; itemInterno < 4; itemInterno++) {
                switch (itemInterno) {
                    case 0:
                        if (item >= 0 && item < asientosTodos.length){
                            grupoPasajes.setPasaje1(asientosTodos[item]);
                        }
                        break;
                    case 1:
                        if (item >= 0 && item < asientosTodos.length){
                            grupoPasajes.setPasaje2(asientosTodos[item]);
                        }
                        break;
                    case 2:
                        if (item >= 0 && item < asientosTodos.length){
                            grupoPasajes.setPasaje3(asientosTodos[item]);
                        }
                        break;
                    case 3:
                        if (item >= 0 && item < asientosTodos.length){
                            grupoPasajes.setPasaje4(asientosTodos[item]);
                        }
                        break;
                }
                item++;
            }
            pasajesAgrupados.add(grupoPasajes);
        }


        lista = (ListView) getView().findViewById(R.id.list_asientos);
        adaptador = new PasajeArrayAdapter<GroupPasajeDT>(getActivity(), pasajesAgrupados);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View header = inflater.inflate(R.layout.list_header_row, lista, false);
        lista.addHeaderView(header, null, false);

        lista.setAdapter(adaptador);
    }

    private AsientoActivo buscarByNroAsiento(List<AsientoActivo> asientosActivos, int numero_asiento) {
        for (AsientoActivo item: asientosActivos ) {
            if(item.getNumero_asiento()==numero_asiento){
                return item;
            }
        }
        return null;
    }

    public void desplegarInfo(int asiento) {

        //obtener los datos segun el asiento
        infoAsiento.setBackgroundResource(R.color.backMenu);
        infoAsiento.setText(String.valueOf(asiento));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_asientos_en_viaje, menu);
        MenuTintUtils menuTintUtils = new MenuTintUtils();
        menuTintUtils.tintAllIcons(menu, Color.parseColor(empresa.getColorHeader()));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

}