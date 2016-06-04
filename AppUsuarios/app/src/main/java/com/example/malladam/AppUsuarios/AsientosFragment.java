package com.example.malladam.AppUsuarios;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.malladam.AppUsuarios.adapters.ExpandListAdapter;
import com.example.malladam.AppUsuarios.models.Pasaje;
import com.example.malladam.AppUsuarios.models.PasajeDataType;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AsientosFragment extends Fragment {


    private ExpandListAdapter ExpAdapter;
    private ArrayList<Group> ExpListItems;
    private ExpandableListView ExpandList;
    public ArrayList<Pasaje> pasajes;
    public ArrayList<PasajeDataType> pasajesDT;
    private DataBaseManager dbManager;

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

        ExpandList = (ExpandableListView)getView().findViewById(R.id.exp_list);
        ExpListItems = SetStandardGroups(pasajesDT);
        ExpAdapter = new ExpandListAdapter(getContext(), ExpListItems);
        View header = (View) getLayoutInflater(state).inflate(R.layout.list_header_row, null);
        ExpandList.addHeaderView(header);
        ExpandList.setAdapter(ExpAdapter);
    }

    public ArrayList<Group> SetStandardGroups(ArrayList<PasajeDataType> pas) {

        ArrayList<Group> list = new ArrayList<Group>();
        ArrayList<Child> ch_list;

        int asientos = 44;
        for (int iter=1; iter<=asientos; iter++){  //for para recorrer todos los asientos
            Group gru = new Group();
            gru.setNumero(String.valueOf(iter));    //crea un grupo por asiento
            PasajeDataType pasajeDT = null;
            if (pas != null){
                for (PasajeDataType pasaje_item : pas) {
                    if (iter == Integer.parseInt(pasaje_item.asiento)) {
                        pasajeDT = pasaje_item;
                    }
                }
            }

            if(pasajeDT != null){
                gru.setOrigen(pasajeDT.getNomOrigen());
                gru.setDestino(pasajeDT.getNomDestino());
                gru.setIcono(pasajeDT.getIcon());

                ch_list = new ArrayList<Child>();
                Child ch = new Child();
                ch.setOrigen(pasajeDT.getNomOrigen());
                ch.setDestino(pasajeDT.getNomDestino());
                ch.setImage(pasajeDT.getIcon());
                ch_list.add(ch);
                gru.setItems(ch_list);
                list.add(gru);
            }else{
                ch_list = new ArrayList<Child>();
                Child ch = new Child();         //habria que obtener la lista de pasajes inactivos
                ch.setOrigen("Agregar Pasaje");
                ch.setDestino("");
                ch.setImage(R.drawable.ic_control_point_black_24dp);
                ch_list.add(ch);
                gru.setItems(ch_list);
                list.add(gru);
            }
        }
        return list;
    }
    //EXPANDIBLE
}
