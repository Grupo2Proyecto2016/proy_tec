package com.example.malladam.AppGuarda.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.malladam.AppGuarda.AsientosFragment;
import com.example.malladam.AppGuarda.R;
import com.example.malladam.AppGuarda.models.AsientoActivo;
import com.example.malladam.AppGuarda.models.GroupParadoDT;

import java.util.List;

public class PasajeParadosAdapter<T> extends ArrayAdapter<T> {

    static AsientosFragment asientosFragment = new AsientosFragment();

    public PasajeParadosAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listItemView = convertView;

        if (null == convertView) {
            listItemView = inflater.inflate(R.layout.columna_parados, parent,false);
        }

        ImageView parado1 = (ImageView) listItemView.findViewById(R.id.parado1);
        ImageView parado2 = (ImageView) listItemView.findViewById(R.id.parado2);
        ImageView parado3 = (ImageView) listItemView.findViewById(R.id.parado3);
        ImageView parado4 = (ImageView) listItemView.findViewById(R.id.parado4);
        ImageView parado5 = (ImageView) listItemView.findViewById(R.id.parado5);
        ImageView parado6 = (ImageView) listItemView.findViewById(R.id.parado6);
        ImageView parado7 = (ImageView) listItemView.findViewById(R.id.parado7);
        ImageView parado8 = (ImageView) listItemView.findViewById(R.id.parado8);
        ImageView parado9 = (ImageView) listItemView.findViewById(R.id.parado9);
        ImageView parado10 = (ImageView) listItemView.findViewById(R.id.parado10);


        //Obteniendo instancia de la Tarea en la posición actual
        T item = (T)getItem(position);
        GroupParadoDT grupo = ((GroupParadoDT) item);
        AsientoActivo pasaje1 = grupo.getPasajeParado1();
        AsientoActivo pasaje2 = grupo.getPasajeParado2();
        AsientoActivo pasaje3 = grupo.getPasajeParado3();
        AsientoActivo pasaje4 = grupo.getPasajeParado4();
        AsientoActivo pasaje5 = grupo.getPasajeParado5();
        AsientoActivo pasaje6 = grupo.getPasajeParado6();
        AsientoActivo pasaje7 = grupo.getPasajeParado7();
        AsientoActivo pasaje8 = grupo.getPasajeParado8();
        AsientoActivo pasaje9 = grupo.getPasajeParado9();
        AsientoActivo pasaje10 = grupo.getPasajeParado10();


        if(pasaje1 !=null) {
            if(pasaje1.getId_pasaje() == null){
                parado1.setImageResource(R.drawable.man);
                parado1.setColorFilter(Color.GREEN);
            }else{
                parado1.setImageResource(R.drawable.man);
                parado1.setColorFilter(Color.RED);
            }
            parado1.setTag(pasaje1.getId_pasaje());
        }


        if(pasaje2 !=null) {
            if(pasaje2.getId_pasaje() == null){
                parado2.setImageResource(R.drawable.man);
                parado2.setColorFilter(Color.GREEN);
            }else{
                parado2.setImageResource(R.drawable.man);
                parado2.setColorFilter(Color.RED);
            }
            parado2.setTag(pasaje2.getId_pasaje());
        }

        if(pasaje3 !=null) {
            if(pasaje3.getId_pasaje() == null){
                parado3.setImageResource(R.drawable.man);
                parado3.setColorFilter(Color.GREEN);
            }else{
                parado3.setImageResource(R.drawable.man);
                parado3.setColorFilter(Color.RED);
            }
            parado3.setTag(pasaje3.getId_pasaje());
        }

        if(pasaje4 !=null) {
            if(pasaje4.getId_pasaje() == null){
                parado4.setImageResource(R.drawable.man);
                parado4.setColorFilter(Color.GREEN);
            }else{
                parado4.setImageResource(R.drawable.man);
                parado4.setColorFilter(Color.RED);
            }
            parado1.setTag(pasaje4.getId_pasaje());
        }

        if(pasaje5 !=null) {
            if(pasaje5.getId_pasaje() == null){
                parado5.setImageResource(R.drawable.man);
                parado5.setColorFilter(Color.GREEN);
            }else{
                parado5.setImageResource(R.drawable.man);
                parado5.setColorFilter(Color.RED);
            }
            parado5.setTag(pasaje5.getId_pasaje());
        }

        if(pasaje6 !=null) {
            if(pasaje6.getId_pasaje() == null){
                parado6.setImageResource(R.drawable.man);
                parado6.setColorFilter(Color.GREEN);
            }else{
                parado6.setImageResource(R.drawable.man);
                parado6.setColorFilter(Color.RED);
            }
            parado6.setTag(pasaje6.getId_pasaje());
        }

        if(pasaje7 !=null) {
            if(pasaje7.getId_pasaje() == null){
                parado7.setImageResource(R.drawable.man);
                parado7.setColorFilter(Color.GREEN);
            }else{
                parado7.setImageResource(R.drawable.man);
                parado7.setColorFilter(Color.RED);
            }
            parado7.setTag(pasaje7.getId_pasaje());
        }

        if(pasaje8 !=null) {
            if(pasaje8.getId_pasaje() == null){
                parado8.setImageResource(R.drawable.man);
                parado8.setColorFilter(Color.GREEN);
            }else{
                parado8.setImageResource(R.drawable.man);
                parado8.setColorFilter(Color.RED);
            }
            parado8.setTag(pasaje8.getId_pasaje());
        }

        if(pasaje9 !=null) {
            if(pasaje9.getId_pasaje() == null){
                parado9.setImageResource(R.drawable.man);
                parado9.setColorFilter(Color.GREEN);
            }else{
                parado9.setImageResource(R.drawable.man);
                parado9.setColorFilter(Color.RED);
            }
            parado9.setTag(pasaje9.getId_pasaje());
        }

        if(pasaje10 !=null) {
            if(pasaje10.getId_pasaje() == null){
                parado10.setImageResource(R.drawable.man);
                parado10.setColorFilter(Color.GREEN);
            }else{
                parado10.setImageResource(R.drawable.man);
                parado10.setColorFilter(Color.RED);
            }
            parado10.setTag(pasaje10.getId_pasaje());
        }


        View.OnClickListener onClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                Integer id_pasaje = (Integer) v.getTag();
                if(id_pasaje != null) {
                    asientosFragment.desplegarInfo(id_pasaje, (Activity) v.getContext());
                }
            }
        };



        parado1.setOnClickListener(onClickListener);
        parado2.setOnClickListener(onClickListener);
        parado3.setOnClickListener(onClickListener);
        parado4.setOnClickListener(onClickListener);
        parado5.setOnClickListener(onClickListener);
        parado6.setOnClickListener(onClickListener);
        parado7.setOnClickListener(onClickListener);
        parado8.setOnClickListener(onClickListener);
        parado9.setOnClickListener(onClickListener);
        parado10.setOnClickListener(onClickListener);

        return listItemView;
    }
}