package com.example.mireia.sharetool_appnativa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;

import java.util.List;

public class Listah extends AppCompatActivity {

    ListView listview;
    public List<ParseObject> ob;
    HerramientasAdapter HerramientasListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listah);
        setTitle("ShareTool - Herramientas");
        ActionBar mactionbar = getSupportActionBar();
        assert mactionbar != null;
        mactionbar.setDisplayHomeAsUpEnabled(true);
        listview = (ListView) findViewById(R.id.listview);
        new Herramientas_p(Listah.this).execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private class Ficha_p extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private AppCompatActivity activity;
        private Context context;
        private int arg;
        public Ficha_p(AppCompatActivity activity, int arg) {
            this.activity = activity;
            this.context = activity;
            this.arg = arg;
            this.dialog = new ProgressDialog(this.context);
        }


        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(this.context, "Cargando ficha de herramienta", "Espere unos segundos...", true, false);

        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
        }

        protected Boolean doInBackground(final String... args) {
            try{
                obglobal b = obglobal.getInstance();
                ob = b.getData();
                ParseObject herr = ob.get(this.arg);
                String projectId = herr.getObjectId();
                Intent intent = new Intent(Listah.this, Fichaprod.class);
                intent.putExtra("id", projectId);
                this.context.startActivity(intent);

                return true;
            } catch (Exception e) {
                Log.e("tag", "error", e);
                return false;
            }

        }
    }



    private class Herramientas_p extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private AppCompatActivity activity;
        public Context context;

        public Herramientas_p(AppCompatActivity activity) {
            this.activity = activity;
            this.context = activity;
            this.dialog = new ProgressDialog(this.context);
        }


        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(this.context, "Cargando lista de herramientas", "Espere unos segundos...", true, false);

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            ListView Herramientasv = (ListView) findViewById(R.id.listview);
            Herramientasv.setAdapter(HerramientasListAdapter);

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
                Herramientasv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        new Ficha_p(activity, arg2).execute();


                    }
                });
            }

        }

        protected Boolean doInBackground(final String... args) {
            try{
                HerramientasListAdapter = new HerramientasAdapter(this.context);
                SystemClock.sleep(1000);
                return true;
            } catch (Exception e) {
                Log.e("tag", "error", e);
                return false;
            }

        }
    }

}
