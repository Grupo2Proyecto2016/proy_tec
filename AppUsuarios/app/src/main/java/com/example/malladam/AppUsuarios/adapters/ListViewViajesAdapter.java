package com.example.malladam.AppUsuarios.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.models.Viaje;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by malladam on 19/08/2016.
 */
public class ListViewViajesAdapter extends ArrayAdapter<Viaje>{

    Context myContext;
    int myLayoutResourceID;
    List<Viaje> datos = new ArrayList<>();

    public ListViewViajesAdapter(Context context , int layoutResourceID, List<Viaje> viajes){
        super(context, layoutResourceID,  viajes);

        myContext = context;
        myLayoutResourceID = layoutResourceID;
        datos = viajes;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ViajesHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity) myContext).getLayoutInflater();
            row = inflater.inflate(myLayoutResourceID, parent, false);
            holder = new ViajesHolder();
            holder.origen = (TextView) row.findViewById(R.id.listview_viajes_origen);
            holder.destino = (TextView) row.findViewById(R.id.listview_viajes_destino);
            holder.horarios = (TextView) row.findViewById(R.id.listview_viajes_horarios);
            holder.duracion = (TextView) row.findViewById(R.id.listview_viajes_duracion);
            holder.precio = (TextView) row.findViewById(R.id.listview_viajes_precio);
            holder.libres = (TextView) row.findViewById(R.id.listview_viajes_libres);

            row.setTag(holder);


        }else {
            holder = (ViajesHolder) row.getTag();
        }

        Viaje viaje = datos.get(position);
        holder.origen.setText(reducir(viaje.getOrigen_description(), 40));
        holder.destino.setText(reducir(viaje.getDestino_description(), 40));
        holder.precio.setText("$ " + String.valueOf(viaje.getValor()));
        holder.horarios.setText(convertirTimestampADateLocal(viaje.getInicio()));
        holder.libres.setText(viaje.getLugares());

        //row.setId(viaje.getId_viaje());

        return row;

    }


    static class ViajesHolder{
        TextView origen, destino, horarios, duracion, precio, libres;
    }


    private String reducir(String cadena, int tamanio){
        String retorno=cadena;
        if (cadena.length() > tamanio){
            retorno = cadena.substring(0,tamanio-3).concat("...");
        }
        return retorno;
    }


    private String convertirTimestampADateLocal(long timestamp){

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(tz);
        String localTime = sdf.format(new Date(timestamp ));
        return  localTime;
    }
}
