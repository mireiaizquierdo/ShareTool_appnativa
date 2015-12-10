package com.example.mireia.sharetool_appnativa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class Fichaprod extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    List<ParseObject> ob;
    ParseObject h;
    TextView loc;
    String Add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fichaprod);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActionBar mactionbar = getSupportActionBar();
        assert mactionbar != null;
        mactionbar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String projectId = intent.getStringExtra("id");

        loc = (TextView) findViewById(R.id.loc);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Herramientas");
        query.whereEqualTo("objectId", projectId);
        try {
            ob = query.find();
            h= ob.get(0);
            setTitle(h.get("Nombre").toString());
            Drawable drawableTest = LoadImageFromWeb(h.get("Foto").toString());
            ImageView img = (ImageView) findViewById(R.id.img);
            img.setImageDrawable(drawableTest);
            TextView prop = (TextView) findViewById(R.id.prop);
            TextView desc = (TextView) findViewById(R.id.desc);
            prop.setText("Propietario: " + h.get("Propietario").toString());
            desc.setText(h.get("Descripcion").toString());
            String dateStringi = (String) DateFormat.format("dd-MM-yyyy", h.getDate("Disponibilidad_inicio"));
            String dateStringf = (String) DateFormat.format("dd-MM-yyyy", h.getDate("Disponibilidad_final"));
            TextView disp = (TextView) findViewById(R.id.disp);
            disp.setText("De: " + dateStringi + " Hasta :" + dateStringf);
            TextView precio = (TextView) findViewById(R.id.prec);
            precio.setText(h.get("preciodia").toString() + " €");
            buttonGetLocationClick();

        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ParseGeoPoint g = h.getParseGeoPoint("Localizacion");

        LatLng ub = new LatLng(g.getLatitude(), g.getLongitude());
        googleMap.addCircle(new CircleOptions()
                .center(ub)
                .radius(200)
                .strokeColor(0x0080FF)
                .fillColor(0x450080FF));

        CameraUpdate mCamera = CameraUpdateFactory.newLatLngZoom(ub, 14);
        googleMap.animateCamera(mCamera);
    }

    public Drawable LoadImageFromWeb(String url1) {
        try {
            URL url = new URL(url1);
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Drawable d;
            d = new BitmapDrawable(getResources(), myBitmap);
            return d;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void buttonGetLocationClick() {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> list = geocoder.getFromLocation(
                    h.getParseGeoPoint("Localizacion").getLatitude(), h.getParseGeoPoint("Localizacion").getLongitude(), 1);
            if (!list.isEmpty()) {
                Address address = list.get(0);
                loc.setText(address.getLocality() + ", " + address.getCountryName());
                Add=loc.getText().toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}

