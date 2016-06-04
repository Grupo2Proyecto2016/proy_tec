package com.example.malladam.AppUsuarios.models;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.IntentIntegrator;
import com.android.IntentResult;
import com.example.malladam.AppUsuarios.R;

import java.util.ArrayList;

public class Asientos extends AppCompatActivity {

    public ArrayList<PasajeDataType> pasajesDT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asientos);

    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_asientos, menu);
            retn true;
        }
    /*
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.action_newpasaje) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            if (scanContent != null){
                String [] qr = new String[4];
                int lugar = 0;
                for (String sub: scanContent.split(";")){
                    qr[lugar] = sub;
                    lugar ++;
                }
                //R.drawable.perfil5, qr[0], qr[2], qr[3]
                Pasaje pasaje = new Pasaje("asiento", "idViajeSESSION", "idCliente", "idOrigen", "idDestino", "fecha", "valor", "tipoVenta", "medioPago");
                //GRABAR NUEVO PASAJE
                PasajeDataType pasajeDTnuevo = null; //convertir Pasaje a PasajeDataType
                pasajesDT.add(pasajeDTnuevo);
                /*ExpListItems = SetStandardGroups(pasajesDT);
                ExpAdapter = new ExpandListAdapter(Asientos.this, ExpListItems);
                ExpandList.setAdapter(ExpAdapter);
*/
                Toast.makeText(getApplicationContext(),"Pasaje Aceptado, asiento: " + qr[0], Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Sin QR", Toast.LENGTH_SHORT).show();
        }
    }
}
