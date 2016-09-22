package com.example.malladam.AppGuarda;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.malladam.AppGuarda.models.Parada;
import com.example.malladam.AppGuarda.utils.DataBaseManager;
import com.example.malladam.AppGuarda.utils.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UbicacionFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {


    private DataBaseManager dbManager;
    private GoogleMap mMap;
    List<Parada> paradas;
    private Marker mActualMarker;
    private LocationListener locListener;
    private LocationManager locMgr;


    public UbicacionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_ubicacion, container, false);
    }


    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        setHasOptionsMenu(true);
        dbManager = new DataBaseManager(getActivity().getApplicationContext());

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapPopup);
        mapFragment.getMapAsync(this);


        locMgr = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                LatLng local = new LatLng(location.getLatitude(), location.getLongitude());
                if(mActualMarker != null){
                    mActualMarker.remove();
                }
                if(mMap!= null) {
                    mActualMarker = mMap.addMarker(new MarkerOptions()
                            .position(local)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.position_indicator)));
                }
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, // minTime in ms
                10, // minDistance in meters
                locListener);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        paradas = dbManager.getParadasDelViaje();

        for (Parada item : paradas) {
            LatLng suc = new LatLng(item.getLatitud(), item.getLongitud());
            if (item.getEs_terminal()) {
                mMap.addMarker(new MarkerOptions()
                        .position(suc)
                        .title(item.getDireccion())
                        .snippet(item.getDescripcion())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            } else {
                mMap.addMarker(new MarkerOptions()
                        .position(suc)
                        .title(item.getDireccion())
                        .snippet(item.getDescripcion())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
        }

        //fin listar paradas

        //comienza trazar ruta

        if(paradas.size() >= 2){
            LatLng origin = new LatLng(paradas.get(0).getLatitud(), paradas.get(0).getLongitud());
            LatLng dest =  new LatLng(paradas.get(paradas.size()-1).getLatitud(), paradas.get(paradas.size()-1).getLongitud());

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(paradas.get(0).getLatitud(), paradas.get(0).getLongitud()), 13.0f));//actual

        }
        mMap.setOnInfoWindowClickListener(this);
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
    }


//comienza auxiliares dibujar ruta
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for(int i=1;i<paradas.size()-1;i++){
            LatLng point  = new LatLng (paradas.get(i).getLatitud(), paradas.get(i).getLongitud());
            if(i==1)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){

        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.parseColor("#05b1fb"));
                lineOptions.geodesic(true);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(mMap != null)
                mMap.addPolyline(lineOptions);
        }
    }
    //fin auxiliares dibijar ruta


}
