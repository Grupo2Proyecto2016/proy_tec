package com.example.malladam.AppGuarda.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.malladam.AppGuarda.AsientosFragment;
import com.example.malladam.AppGuarda.R;
import com.example.malladam.AppGuarda.models.AsientoActivo;
import com.example.malladam.AppGuarda.models.GroupPasajeDT;
import com.example.malladam.AppGuarda.models.PasajeDataType;

import java.util.List;

/**
 * Created by malladam on 15/06/2016.
 */
public class PasajeArrayAdapter<T> extends ArrayAdapter<T> {

    static AsientosFragment asientosFragment = new AsientosFragment();

    public PasajeArrayAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con columna_asientos.xml
            listItemView = inflater.inflate(R.layout.columna_asientos, parent,false);
        }

        //Obteniendo instancias de los text views
        TextView numero1 = (TextView) listItemView.findViewById(R.id.numero1);
        TextView numero2 = (TextView) listItemView.findViewById(R.id.numero2);
        TextView numero3 = (TextView) listItemView.findViewById(R.id.numero3);
        TextView numero4 = (TextView) listItemView.findViewById(R.id.numero4);

        ImageView asiento1 = (ImageView) listItemView.findViewById(R.id.asiento1);
        ImageView asiento2 = (ImageView) listItemView.findViewById(R.id.asiento2);
        ImageView asiento3 = (ImageView) listItemView.findViewById(R.id.asiento3);
        ImageView asiento4 = (ImageView) listItemView.findViewById(R.id.asiento4);

        //Obteniendo instancia de la Tarea en la posición actual
        T item = (T)getItem(position);
        GroupPasajeDT grupo = ((GroupPasajeDT) item);
        AsientoActivo pasaje1 = grupo.getPasaje1();
        AsientoActivo pasaje2 = grupo.getPasaje2();
        AsientoActivo pasaje3 = grupo.getPasaje3();
        AsientoActivo pasaje4 = grupo.getPasaje4();

        if(pasaje1 !=null) {
            if(pasaje1.getId_pasaje() == null){
                numero1.setText(String.valueOf((position * 4) +1));
                asiento1.setImageResource(R.drawable.icon_seat_libre);
                asiento1.setColorFilter(Color.GREEN);
            }else{
                asiento1.setImageResource(R.drawable.icon_seat_ocupado);
                asiento1.setColorFilter(Color.RED);
            }
            asiento1.setTag(pasaje1.getId_pasaje());
        }


        if(pasaje2 !=null) {
            if(pasaje2.getId_pasaje() == null){
                numero2.setText(String.valueOf((position * 4) +2));
                asiento2.setImageResource(R.drawable.icon_seat_libre);
                asiento2.setColorFilter(Color.GREEN);
            }else{
                asiento2.setImageResource(R.drawable.icon_seat_ocupado);
                asiento2.setColorFilter(Color.RED);
            }
            asiento2.setTag(pasaje2.getId_pasaje());
        }


        if(pasaje3 !=null) {
            if(pasaje3.getId_pasaje() == null){
                numero3.setText(String.valueOf((position * 4) +3));
                asiento3.setImageResource(R.drawable.icon_seat_libre);
                asiento3.setColorFilter(Color.GREEN);
            }else{
                asiento3.setImageResource(R.drawable.icon_seat_ocupado);
                asiento3.setColorFilter(Color.RED);
            }
            asiento3.setTag(pasaje3.getId_pasaje());
        }

        if(pasaje4 !=null) {
            if (pasaje4.getId_pasaje() == null) {
                numero4.setText(String.valueOf((position * 4) + 4));
                asiento4.setImageResource(R.drawable.icon_seat_libre);
                asiento4.setColorFilter(Color.GREEN);
            } else {
                asiento4.setImageResource(R.drawable.icon_seat_ocupado);
                asiento4.setColorFilter(Color.RED);
            }
            asiento4.setTag(pasaje4.getId_pasaje());
            //Devolver al ListView la fila creada
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                Integer id_pasaje = (Integer) v.getTag();
                if(id_pasaje != null)
                    asientosFragment.desplegarInfo(id_pasaje, (Activity) v.getContext());
            }
        };

        asiento1.setOnClickListener(onClickListener);
        asiento2.setOnClickListener(onClickListener);
        asiento3.setOnClickListener(onClickListener);
        asiento4.setOnClickListener(onClickListener);

        return listItemView;

    }
}