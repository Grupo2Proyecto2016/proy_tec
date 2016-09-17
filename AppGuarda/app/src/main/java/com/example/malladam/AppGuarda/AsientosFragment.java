package com.example.malladam.AppGuarda;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malladam.AppGuarda.adapters.PasajeArrayAdapter;
import com.example.malladam.AppGuarda.adapters.PasajeParadosAdapter;
import com.example.malladam.AppGuarda.models.AsientoActivo;
import com.example.malladam.AppGuarda.models.Empresa;
import com.example.malladam.AppGuarda.models.GroupParadoDT;
import com.example.malladam.AppGuarda.models.GroupPasajeDT;
import com.example.malladam.AppGuarda.utils.DataBaseManager;
import com.example.malladam.AppGuarda.utils.MenuTintUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AsientosFragment extends Fragment {

    private DataBaseManager dbManager;
    private ListView lista, listaParados;
    PasajeArrayAdapter<GroupPasajeDT> adaptador;
    PasajeParadosAdapter<GroupParadoDT> adaptadorParados;
    private int asientosBus, paradosBus;
    private Empresa empresa;
    private SwipeRefreshLayout swipeLayout;

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
        paradosBus = dbManager.getViajeActual().getCantParados_vehiculo();


        swipeLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_container);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        cargarAsientos();
                    }
                }, 2000);
            }
        });

        cargarAsientos();

    }

    private AsientoActivo buscarByNroAsiento(List<AsientoActivo> asientosActivos, int numero_asiento) {
        for (AsientoActivo item: asientosActivos ) {
            if(item.getNumero_asiento()==numero_asiento){
                return item;
            }
        }
        return null;
    }


    public void desplegarInfo(int id_pasaje, Activity context) {

        dbManager = new DataBaseManager(context);
        AsientoActivo pasaje = dbManager.getPasajeActivoById(id_pasaje);
        if(pasaje.getId_pasaje() != null) {
            // Inflate the popup_layout.xml
            LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popupInfoPasajero);
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(R.layout.popup_info_pasajero, viewGroup);

            // Creating the PopupWindow
            final PopupWindow popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            TextView origen = (TextView) layout.findViewById(R.id.popupInfoPasajeroOrigen);
            TextView destino = (TextView) layout.findViewById(R.id.popupInfoPasajeroDestino);
            TextView asiento = (TextView) layout.findViewById(R.id.popupInfoPasajeroNroAsiento);

            origen.setText(dbManager.getParadaDelViajeById(pasaje.getId_paradaSube()).getDescripcion());
            destino.setText(dbManager.getParadaDelViajeById(pasaje.getId_paradaBaja()).getDescripcion());
            asiento.setText(String.valueOf(pasaje.getNumero_asiento()));

            // Clear the default translucent background
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

            // Getting a reference to Close button, and close the popup when clicked.
            Button close = (Button) layout.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.dismiss();
                }
            });
        }
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

    private void cargarAsientos(){

        //ASIENTOS
        List<AsientoActivo> asientosActivos = dbManager.getAsientosActivos();

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
        if(lista.getHeaderViewsCount() ==0)
            lista.addHeaderView(header, null, false);

        lista.setAdapter(adaptador);
        //FIN ASIENTOS

        //PARADOS

        List<AsientoActivo> paradosActivos = dbManager.getParadosActivos();

        AsientoActivo[] paradosTodos = new AsientoActivo[paradosBus];

        for (int i = 0 ; i<paradosBus; i++) {
            if (i < paradosActivos.size()){
                paradosTodos[i] = paradosActivos.get(i);
            }
            else{
                paradosTodos[i] = new AsientoActivo();
            }
        }

        List<GroupParadoDT> paradosAgrupados = new ArrayList<>();

        for (int item = 0; item < paradosBus; ) {
            GroupParadoDT grupoParados = new GroupParadoDT();
            for (int itemInterno = 0; itemInterno < 10; itemInterno++) {
                switch (itemInterno) {
                    case 0:
                        if (item >= 0 && item < paradosTodos.length){
                            grupoParados.setPasajeParado1(paradosTodos[item]);
                        }
                        break;
                    case 1:
                        if (item >= 0 && item < paradosTodos.length){
                            grupoParados.setPasajeParado2(paradosTodos[item]);
                        }
                        break;
                    case 2:
                        if (item >= 0 && item < paradosTodos.length){
                            grupoParados.setPasajeParado3(paradosTodos[item]);
                        }
                        break;
                    case 3:
                        if (item >= 0 && item < paradosTodos.length){
                            grupoParados.setPasajeParado4(paradosTodos[item]);
                        }
                        break;
                    case 4:
                        if (item >= 0 && item < paradosTodos.length){
                            grupoParados.setPasajeParado5(paradosTodos[item]);
                        }
                        break;
                    case 5:
                        if (item >= 0 && item < paradosTodos.length){
                            grupoParados.setPasajeParado6(paradosTodos[item]);
                        }
                        break;
                    case 6:
                        if (item >= 0 && item < paradosTodos.length){
                            grupoParados.setPasajeParado7(paradosTodos[item]);
                        }
                        break;
                    case 7:
                        if (item >= 0 && item < paradosTodos.length){
                            grupoParados.setPasajeParado8(paradosTodos[item]);
                        }
                        break;
                    case 8:
                        if (item >= 0 && item < paradosTodos.length){
                            grupoParados.setPasajeParado9(paradosTodos[item]);
                        }
                        break;
                    case 9:
                        if (item >= 0 && item < paradosTodos.length){
                            grupoParados.setPasajeParado10(paradosTodos[item]);
                        }
                        break;
                }
                item++;
            }
            paradosAgrupados.add(grupoParados);
        }

        listaParados = (ListView) getView().findViewById(R.id.list_parados);
        adaptadorParados = new PasajeParadosAdapter<GroupParadoDT>(getActivity(), paradosAgrupados);

        listaParados.setAdapter(adaptadorParados);

    }

}