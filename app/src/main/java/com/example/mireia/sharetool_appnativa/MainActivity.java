package com.example.mireia.sharetool_appnativa;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity {

    EditText pass, username;
    String musername, mpass;
    ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ShareTool");
        // Enable Local Datastore.
        try {
            Parse.enableLocalDatastore(this);

            Parse.initialize(this, "tN9uG7NreDb9yL8sCP05DpEyElNdSGqfpE4zYBxD", "nC1B9zxJnPjfmxb1dF49SmHlNtyW75d7HXJEVLNl");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Button login = (Button) findViewById(R.id.ENTRARBUTTON);
        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                pd = ProgressDialog.show(MainActivity.this, "Iniciando sesión", "Espere unos segundos...", true, false);

                username = (EditText) findViewById(R.id.user);
                pass = (EditText) findViewById(R.id.password);

                musername = username.getText().toString();
                mpass = pass.getText().toString();

                ParseUser.logInInBackground(musername, mpass, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Intent intent = new Intent(MainActivity.this, Inicio.class);
                            startActivity(intent);
                            finish();
                            if (pd != null) {
                                pd.dismiss();
                            }
                        } else {
                            pd.dismiss();
                            Toast toast = Toast.makeText(getApplicationContext(), "Error, intentalo de nuevo", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });

            }
        });
    }

}

