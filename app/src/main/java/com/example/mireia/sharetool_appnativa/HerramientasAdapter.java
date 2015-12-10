package com.example.mireia.sharetool_appnativa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HerramientasAdapter extends BaseAdapter {

    Context mContext;
    List<ParseObject> obs = new ArrayList<>();
    double lat;
    double longi;

    public HerramientasAdapter(Context mContext) {
        this.mContext = mContext;
    }

    List<Herramientas> HerramientasList = getDataForListView();

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return HerramientasList.size();
    }

    @Override
    public Herramientas getItem(int arg0) {
        // TODO Auto-generated method stub
        return HerramientasList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {

        if (arg1 == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arg1 = inflater.inflate(R.layout.listview_item, arg2, false);
        }

        TextView chapterName = (TextView) arg1.findViewById(R.id.textView1);
        chapterName.setTypeface(null, Typeface.BOLD);
        TextView chapterDesc = (TextView) arg1.findViewById(R.id.textView2);
        TextView dist = (TextView) arg1.findViewById(R.id.textView3);
        dist.setTypeface(null, Typeface.BOLD);
        TextView pre = (TextView) arg1.findViewById(R.id.textView4);
        pre.setTypeface(null, Typeface.BOLD);
        ImageView img = (ImageView) arg1.findViewById(R.id.imageView1);


        Herramientas Herramienta = HerramientasList.get(arg0);

        chapterName.setText(Herramienta.HName);
        chapterDesc.setText(Herramienta.HDescription);
        if (Herramienta.dist < 500.00) {
            dist.setText("Aprox. " + Herramienta.dist.toString() + " Km");
        } else {
            dist.setText("Más de 500 Km");
        }
        pre.setText(Herramienta.precio + "€");

        Drawable drawableTest = LoadImageFromWeb(Herramienta.url);
        img.setImageDrawable(drawableTest);
        return arg1;
    }




    public List<Herramientas> getDataForListView() {
        List<Herramientas> HerramientasList = new ArrayList<>();
        getHerramientas();

        for (ParseObject Herr : obs) {

            Herramientas Herramienta = new Herramientas();
            Herramienta.HName = Herr.get("Nombre").toString();
            Herramienta.HDescription = Herr.get("Descripcion").toString();
            Herramienta.precio = Herr.get("preciodia").toString();
            Herramienta.url = Herr.get("perfimg").toString();

            double ds = distanciade(Herr.getParseGeoPoint("Localizacion"));
            if (ds != 0) {
                Herramienta.dist = ds;
            }else{
                Herramienta.dist = 0.0;
            }
            HerramientasList.add(Herramienta);
        }

        return HerramientasList;

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
            return new BitmapDrawable(mContext.getResources(), myBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getHerramientas() {
        Filters f = Filters.getInstance();
        List<Distancias> distl = new ArrayList<>();
        obglobal b = obglobal.getInstance();

        if (f.getDataord() != null) {

            if (f.getDatap() != 0) {

                try {
                    ParseQuery<ParseObject> query = new ParseQuery<>("Herramientas");
                    query.orderByAscending("preciodia");
                    query.whereLessThanOrEqualTo("preciodia", f.getDatap());

                    obs = query.find();


                    for (ParseObject o : obs) {
                        distl.add(new Distancias(distanciade(o.getParseGeoPoint("Localizacion")), o));
                    }

                    if (f.getDataord().equals("Distancia")){
                        Collections.sort(distl, new Comparator<Distancias>() {
                            @Override
                            public int compare(Distancias d1, Distancias d2) {

                                return d1.dist.compareTo(d2.dist);
                            }
                        });

                    }
                    obs = new ArrayList<>();
                    for (Distancias de : distl) {
                        if (de.dist < f.getDatad()) {
                            obs.add(de.ob);
                        }
                    }
                    b.setData(obs);
                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }else {
                try {
                    ParseQuery<ParseObject> query = new ParseQuery<>("Herramientas");
                    query.orderByAscending("preciodia");

                    obs = query.find();


                    for (ParseObject o : obs) {
                        distl.add(new Distancias(distanciade(o.getParseGeoPoint("Localizacion")), o));
                    }

                    if (f.getDataord().equals("Distancia")) {
                        Collections.sort(distl, new Comparator<Distancias>() {
                            @Override
                            public int compare(Distancias d1, Distancias d2) {

                                return d1.dist.compareTo(d2.dist);
                            }
                        });

                    }

                    obs = new ArrayList<>();
                    for (Distancias de : distl) {
                        if (de.dist < f.getDatad()) {
                            obs.add(de.ob);
                        }

                    }
                    b.setData(obs);
                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }

        } else {
            try {
                ParseQuery<ParseObject> query = new ParseQuery<>("Herramientas");
                query.orderByAscending("preciodia");

                obs = query.find();


                for (ParseObject o : obs) {
                    distl.add(new Distancias(distanciade(o.getParseGeoPoint("Localizacion")), o));


                    Collections.sort(distl, new Comparator<Distancias>() {
                        @Override
                        public int compare(Distancias d1, Distancias d2) {

                            return d1.dist.compareTo(d2.dist);
                        }
                    });

                }
                obs = new ArrayList<>();
                for (Distancias de : distl) {
                    if (de.dist < 100) {
                        obs.add(de.ob);
                    }

                }
                b.setData(obs);
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private double distanciade(ParseGeoPoint p) {
        Latitudes lt = Latitudes.getInstance();
        lat = lt.getDatalt();
        longi = lt.getDatalg();


        if(lat != 0.0 && longi != 0.0) {
            ParseGeoPoint point = new ParseGeoPoint(lat, longi);
            double n = p.distanceInKilometersTo(point);
            n = Math.round(n * 100);
            n = n/100;
            return n;
        }else{
            return 0.0;
        }
    }
}