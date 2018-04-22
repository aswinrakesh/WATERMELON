package com.example.hp.jsonparsing;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hp.jsonparsing.HttpHandler;
import com.example.hp.jsonparsing.R;
import android.view.Menu;
import android.view.MenuItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    final String TAG = MainActivity.class.getSimpleName();
    static String url;
    String pos;
    String icon;
    static double cels;
    final ProgressDialog[] pDialog = new ProgressDialog[1];
    public static Double result;
    Button button;
    // Switch switch1;
    SwipeRefreshLayout swipeRefreshLayout;
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item:

        showInputDialog();        }
        return true;
    }*/
    // URL to get contacts JSON
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pos=getIntent().getExtras().getString("name");
        TextView cityname=(TextView) findViewById(R.id.city);
        cityname.setText(pos);

        //

       /* Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){*/
            url="http://api.openweathermap.org/data/2.5/weather?q="+pos+",In&appid=65bffd338ec8e3f4411a9b58bd22deb3";
            new GetContacts().execute();
        // }});

      /*  swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new GetContacts().execute();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });*/
    }


   /* protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog , null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

      //  final EditText editText = (EditText) promptView.findViewById(R.id.editText);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    //String p= String.valueOf(editText.getText());
                    url="http://api.openweathermap.org/data/2.5/weather?q="+pos+",In&appid=65bffd338ec8e3f4411a9b58bd22deb3";
                        new GetContacts().execute();
                                           }
                } )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }*/
    private class GetContacts extends AsyncTask<Void, Void, Double> {
        String ic="fonts/weather.ttf";
        @Override
        protected Double doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
           // url="http://api.openweathermap.org/data/2.5/weather?q=Kochi,In&appid=65bffd338ec8e3f4411a9b58bd22deb3";
           //url="http://api.openweathermap.org/data/2.5/weather?q="+pos+",In&appid=65bffd338ec8e3f4411a9b58bd22deb3";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.i("GetContacts", jsonStr);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject contacts = jsonObj.getJSONObject("main");
                    JSONArray we=jsonObj.getJSONArray("weather");
                    JSONObject idd=we.getJSONObject(0);
                    final int i=idd.getInt("id");
                    Double c = contacts.getDouble("temp");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView icon=(TextView) findViewById(R.id.weathericon);
                            Typeface icn=Typeface.createFromAsset(getAssets(),ic);
                            icon.setTypeface(icn);
                            icon.setText(getSt(i));
                        }
                    });
                    cels = c - 273.15;
                    final String k = String.valueOf(cels);
                    TextView temper = (TextView) findViewById(R.id.tempe);
                    temper.setText(k);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), k, Toast.LENGTH_LONG).show();


                        }
                    });
                    return c;
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }

            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }
        public String getSt(int i)
        {    String ic="";

            switch(i)
            {
                case(200):
                {
                    ic=getString(R.string.wi_owm_day_200);
                    break;
                }
                case(201):
                {
                    ic=getString(R.string.wi_owm_201);
                    break;
                }
                case(202):
                {
                    ic=getString(R.string.wi_owm_202);
                    break;
                }
                case(721):
                {
                    ic=getString(R.string.wi_owm_721);
                    break;
                }
                case(210):
                {
                    ic=getString(R.string.wi_owm_210);
                    break;
                }
                case(211):
                {
                    ic=getString(R.string.wi_owm_211);
                    break;
                }
                case(212):
                {
                    ic=getString(R.string.wi_owm_212);
                    break;
                }
                case(221):
                {
                    ic=getString(R.string.wi_owm_221);
                    break;
                }
                case(230):
                {
                    ic=getString(R.string.wi_owm_230);
                    break;
                }
                case(231):
                {
                    ic=getString(R.string.wi_owm_231);
                    break;
                }
                case(232):
                {
                    ic=getString(R.string.wi_owm_232);
                    break;
                }
                case (611):
                {
                    ic=getString(R.string.wi_owm_611);
                    break;
                }
                case (711):
                {
                    ic=getString(R.string.wi_owm_711);
                    break;
                }
                case (731):
                {
                    ic=getString(R.string.wi_owm_731);
                    break;
                }
                case (741):
                {
                    ic=getString(R.string.wi_owm_741);
                    break;
                }
                case (761):
                {
                    ic=getString(R.string.wi_owm_761);
                    break;
                }
                case (800):
                {
                    ic=getString(R.string.wi_owm_800);
                    break;
                }
                case (801):
                {
                    ic=getString(R.string.wi_owm_801);
                    break;
                }
                case (957):
                {
                    ic=getString(R.string.wi_owm_night_957);
                    break;
                }
                case (906):
                {
                    ic=getString(R.string.wi_owm_night_906);
                    break;
                }
                case (904):
                {
                    ic=getString(R.string.wi_owm_night_904);
                    break;
                }
                case (500):
                {
                    ic=getString(R.string.wi_owm_500);
                    break;
                }
                case (521):
                {
                    ic=getString(R.string.wi_owm_521);
                    break;
                }
                case(701):
                    {
                ic = getString(R.string.wi_owm_500);
                    }
            }
            return ic;
        }

        protected void onPostExecute(final Double result) {
            super.onPostExecute(result);
            Switch s1=findViewById(R.id.switch1);
            s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        Toast.makeText(getApplicationContext(),result+" kelvin" , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),(result-273.15)+" Celcius", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }


    }

    }






