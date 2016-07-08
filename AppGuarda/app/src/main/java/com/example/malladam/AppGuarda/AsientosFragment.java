package com.example.malladam.AppGuarda;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.malladam.AppGuarda.adapters.PasajeArrayAdapter;
import com.example.malladam.AppGuarda.models.GroupPasajeDT;
import com.example.malladam.AppGuarda.models.PasajeDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AsientosFragment extends Fragment {

    private DataBaseManager dbManager;
    ListView lista;
    PasajeArrayAdapter<GroupPasajeDT> adaptador;
    int asientosBus = 44;
    static TextView infoAsiento;

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

        infoAsiento= (TextView) getView().findViewById(R.id.infoAsiento);

        /////JUEGO DE DATOS///

        PasajeDataType[]pasajes = new PasajeDataType[asientosBus];//obtener los pasajes activos
        /*PasajeDataType pasaje3 = new PasajeDataType("41", "001",R.drawable.gps,"Misa", "La PAz", "LAs Peidras", "01-01-01", "500", "A");
        PasajeDataType pasaje6 = new PasajeDataType("02", "001",R.drawable.gps,"Misa", "La PAz", "LAs Peidras", "01-01-01", "500", "A");
        PasajeDataType pasaje5 = new PasajeDataType("04", "001",R.drawable.gps,"Misa", "La PAz", "LAs Peidras", "01-01-01", "500", "A");
        PasajeDataType pasaje4 = new PasajeDataType("01", "001",R.drawable.gps,"Misa", "La PAz", "LAs Peidras", "01-01-01", "500", "A");
        PasajeDataType pasaje2 = new PasajeDataType("23", "001",R.drawable.gps,"Misa", "La PAz", "LAs Peidras", "01-01-01", "500", "A");
        PasajeDataType pasaje7 = new PasajeDataType("07", "001",R.drawable.gps,"Misa", "La PAz", "LAs Peidras", "01-01-01", "500", "A");
        pasajes[22]=pasaje2;
        pasajes[0]=pasaje4;
        pasajes[40]=pasaje3;
        pasajes[1]=pasaje6;
        pasajes[3]=pasaje5;
        pasajes[6]=pasaje7;*/

        PasajeDataType[]pasajesTodos = new PasajeDataType[asientosBus];

        for (int item = 0; item < asientosBus; item++){
            if(pasajes[item] !=null){
                pasajesTodos[item]= pasajes[item];
            }
            else{
                pasajesTodos[item] = null;
            }
        }

        List<GroupPasajeDT>pasajesAgrupados = new ArrayList<>();

        for (int item = 0; item < asientosBus;){
            GroupPasajeDT grupoPasajes = new GroupPasajeDT();
            for (int itemInterno = 0; itemInterno < 4; itemInterno++) {
                switch (itemInterno) {
                    case 0:
                        grupoPasajes.setPasaje1(pasajesTodos[item]);
                        break;
                    case 1:
                        grupoPasajes.setPasaje2(pasajesTodos[item]);
                        break;
                    case 2:
                        grupoPasajes.setPasaje3(pasajesTodos[item]);
                        break;
                    case 3:
                        grupoPasajes.setPasaje4(pasajesTodos[item]);
                        break;
                }
                item++;
            }
            pasajesAgrupados.add(grupoPasajes);
        }


        lista = (ListView)getView().findViewById(R.id.list_asientos);
        adaptador = new PasajeArrayAdapter<GroupPasajeDT>(getActivity(),pasajesAgrupados);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View header = inflater.inflate(R.layout.list_header_row, lista, false);
        lista.addHeaderView(header, null, false);

        lista.setAdapter(adaptador);
    }

    public void desplegarInfo(int asiento){

        //obtener los datos segun el asiento
        infoAsiento.setBackgroundResource(R.color.backMenu);
        infoAsiento.setText(String.valueOf(asiento));
    }

}
