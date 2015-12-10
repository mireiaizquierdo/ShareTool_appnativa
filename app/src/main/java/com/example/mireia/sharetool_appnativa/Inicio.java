package com.example.mireia.sharetool_appnativa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.parse.ParseUser;

import java.io.IOException;

import java.util.List;
import java.util.Locale;

public class Inicio extends AppCompatActivity {

    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 0;
    private TextView message;

    private Location mobileLocation;

    double latitude=0;
    double longitude=0;
    Button buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
        setTitle("ShareTool - Inicio");

        message = (TextView) findViewById(R.id.address);
        buscar = (Button) findViewById(R.id.buttonFil);
        TextView usernam = (TextView) findViewById(R.id.usern);
        usernam.setText("Bienvenido/a, " + ParseUser.getCurrentUser().getUsername());
        final Button mapa = (Button) findViewById(R.id.bmapa);
        mapa.setEnabled(false);

        mapa.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                new mapa_p(Inicio.this).execute();
            }


        });
        buscar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                new buscar_p(Inicio.this).execute();
            }


        });


        getCurrentLocation(); // gets the current location and update mobileLocation variable

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){

                new logout_p(Inicio.this).execute();


            }
        });

        Button buttonloc = (Button) findViewById(R.id.Ubutton);
        buttonloc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                message.setText("");
                buttonGetLocationClick();

                if (latitude != 0.0 && longitude != 0.0) {
                    buscar.setEnabled(true);
                    mapa.setEnabled(true);
                }
            }
        });
    }

    /**
     * Gets the current location and update the mobileLocation variable
     */
    private void getCurrentLocation() {
        LocationManager locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                mobileLocation = location;
            }
        };


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COURSE_LOCATION);
            return;
        }
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

    }


    private void buttonGetLocationClick() {
        //getCurrentLocation(); // gets the current location and update mobileLocation variable

        if (mobileLocation != null) {

            //Obtener la direccion de la calle a partir de la latitud y la longitud
            if (mobileLocation.getLatitude() != 0.0 && mobileLocation.getLongitude() != 0.0) {
                try {
                    Latitudes ltg = Latitudes.getInstance();
                    latitude= mobileLocation.getLatitude();
                    longitude= mobileLocation.getLongitude();
                    ltg.setData(latitude, longitude);
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> list = geocoder.getFromLocation(
                            mobileLocation.getLatitude(), mobileLocation.getLongitude(), 1);
                    if (!list.isEmpty()) {
                        Address address = list.get(0);
                        message.setText(address.getAddressLine(0) + ", " + address.getLocality() + ", " + address.getCountryName());
                        ltg.setDataAdd(message.getText().toString());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            message.setText("Sorry, location is not determined");
        }
    }

    //mapa con processdialog

    private class mapa_p extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private AppCompatActivity activity;
        private Context context;
        public mapa_p(AppCompatActivity activity) {
            this.activity = activity;
            this.context = activity;
            this.dialog = new ProgressDialog(this.context);
        }

        /* progress dialog to show user that the backup is processing. */


        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(this.context, "Cargando mapa", "Espere unos segundos...", true, false);

        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (this.dialog.isShowing()) {
                Intent intt = new Intent(this.activity, MapsAquiestoy.class);
                this.context.startActivity(intt);
                this.dialog.dismiss();
            }
        }

        protected Boolean doInBackground(final String... args) {
            try{
                SystemClock.sleep(1000);
                return true;
            } catch (Exception e) {
                Log.e("tag", "error", e);
                return false;
            }

        }
    }

    //Logout con processdialog.

    private class logout_p extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private AppCompatActivity activity;
        private Context context;
        public logout_p(AppCompatActivity activity) {
            this.activity = activity;
            this.context = activity;
            this.dialog = new ProgressDialog(this.context);
        }

        /* progress dialog to show user that the backup is processing. */


        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(this.context, "Cerrando sesión", "Espere unos segundos...", true, false);

        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (this.dialog.isShowing()) {
                ParseUser.logOut();
                Intent intt = new Intent(this.activity, MainActivity.class);
                this.context.startActivity(intt);
                this.dialog.dismiss();
                finish();
            }
        }

        protected Boolean doInBackground(final String... args) {
            try{
                Filters f = Filters.getInstance();
                Latitudes l = Latitudes.getInstance();
                obglobal o = obglobal.getInstance();

                f.setData(null, null, null);
                l.setData(null, null);
                l.setDataAdd(null);
                o.setData(null);
                SystemClock.sleep(1000);
                return true;
            } catch (Exception e) {
                Log.e("tag", "error", e);
                return false;
            }

        }
    }

    //buscar con processdialog.

    private class buscar_p extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private AppCompatActivity activity;
        private Context context;
        private EditText distancia , precio;
        private RadioGroup ordenar;
        private RadioButton dista,pre,ord;
        private Intent intt1;
        private int ido;
        String orde,di,pr;

        public buscar_p(AppCompatActivity activity) {
            this.activity = activity;
            this.context = activity;
            this.dialog = new ProgressDialog(this.context);
            distancia = (EditText) findViewById(R.id.distanciam);
            precio = (EditText) findViewById(R.id.preciom);
            ordenar = (RadioGroup) findViewById(R.id.radioordenar);
            dista = (RadioButton) findViewById(R.id.radio_distancia);
            pre = (RadioButton) findViewById(R.id.radio_precio);
            ido = ordenar.getCheckedRadioButtonId();
            ord = (RadioButton) findViewById(ido);
            orde=ord.getText().toString();
            di=distancia.getText().toString();
            pr=precio.getText().toString();

        }

        /* progress dialog to show user that the backup is processing. */


        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(this.context, "Buscando herramientas", "Espere unos segundos...", true, false);

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
                this.context.startActivity(intt1);

            }
        }

        protected Boolean doInBackground(final String... args) {
            try{
                intt1 = new Intent(this.activity, Listah.class);

                Filters f=Filters.getInstance();
                if(di.equals("")){
                    if (pr.equals("")){
                        f.setData(orde, 1000.00, 0.00);
                    }else{
                        f.setData(orde, 1000.00, Double.parseDouble(pr));
                    }

                }else{
                    if (pr.equals("")){
                        f.setData(orde, Double.parseDouble(di), 0.00);
                    }else{
                        f.setData(orde, Double.parseDouble(di), Double.parseDouble(pr));
                    }

                }

                return true;


            } catch (Exception e) {
                Log.e("tag", "error", e);
                return false;
            }

        }
    }

}





