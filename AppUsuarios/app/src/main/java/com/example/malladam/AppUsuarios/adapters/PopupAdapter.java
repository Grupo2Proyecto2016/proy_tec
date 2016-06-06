package com.example.malladam.AppUsuarios.adapters;

/**
 * Created by malladam on 06/06/2016.
 */
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.example.malladam.AppUsuarios.R;

public class PopupAdapter implements InfoWindowAdapter {
    private View popup=null;
    private LayoutInflater inflater=null;

    public PopupAdapter(LayoutInflater inflater) {
        this.inflater=inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup=inflater.inflate(R.layout.popup, null);
        }

        TextView tv=(TextView)popup.findViewById(R.id.title);
        tv.setText(marker.getTitle());
        String todos = marker.getSnippet();
        String subString = "";
        for (int i = 0;i<3;i++){
            int pos = todos.indexOf(":");
            if (pos != -1) {
                subString = todos.substring(0, pos);
                if(i == 0){
                    TextView dir=(TextView)popup.findViewById(R.id.dir);
                    dir.setText(" "+subString);
                }else if (i == 1){
                    TextView tel=(TextView)popup.findViewById(R.id.tel);
                    tel.setText("  "+subString);
                }
            }
            if(i==2){
                TextView email=(TextView)popup.findViewById(R.id.email);
                email.setText("   "+todos);
            }

            todos = todos.substring(pos+1,todos.length());
        }
        return(popup);
    }
}
