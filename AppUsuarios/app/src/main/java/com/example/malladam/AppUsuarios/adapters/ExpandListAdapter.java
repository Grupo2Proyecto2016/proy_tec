package com.example.malladam.AppUsuarios.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.malladam.AppUsuarios.Child;
import com.example.malladam.AppUsuarios.Group;
import com.example.malladam.AppUsuarios.R;

import java.util.ArrayList;

/**
 * Created by malladam on 01/05/2016.
 */
public class ExpandListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Group> groups;

    public ExpandListAdapter(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Child> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Child child = (Child) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_item, null);//la linea que cambio child view
        }
        TextView or = (TextView) convertView.findViewById(R.id.origen);
        TextView des = (TextView) convertView.findViewById(R.id.destino);
        ImageView iv = (ImageView) convertView.findViewById(R.id.imagenView);

        or.setText(child.getOrigen().toString());
        des.setText(child.getDestino().toString());
        iv.setImageResource(child.getImage());

        if(child.getDestino().length() == 0){
            convertView.setBackgroundColor(Color.CYAN);
        }else{
            convertView.setBackgroundColor(Color.LTGRAY);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Child> chList = groups.get(groupPosition).getItems();
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.listview_item_row, null);
        }
        TextView numero = (TextView) convertView.findViewById(R.id.numero);
        numero.setText(group.getNumero());

        ImageView icono = (ImageView) convertView.findViewById(R.id.imagenView);
        icono.setImageResource (group.getIcono());

        TextView origen = (TextView) convertView.findViewById(R.id.origen);
        origen.setText(group.getOrigen());

        TextView destino = (TextView) convertView.findViewById(R.id.destino);
        destino.setText(group.getDestino());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}