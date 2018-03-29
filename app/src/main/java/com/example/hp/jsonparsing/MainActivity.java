package com.example.hp.jsonparsing;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    static double cels;
    final ProgressDialog[] pDialog = new ProgressDialog[1];
    public static Double result;
    Button button;
   // Switch switch1;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
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
    }
    // URL to get contacts JSON
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            new GetContacts().execute();
         }});
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
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
        });



    }


    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog , null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    String p= String.valueOf(editText.getText());
                    url="http://api.openweathermap.org/data/2.5/weather?q="+p+",In&appid=65bffd338ec8e3f4411a9b58bd22deb3";
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
    }
    class GetContacts extends AsyncTask<Void, Void, Double> {


        @Override
        protected Double doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            Log.i("GetContacts", jsonStr);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject contacts = jsonObj.getJSONObject("main");
                Double c = contacts.getDouble("temp");
                final String k = String.valueOf(c);
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

        protected void onPostExecute(final Double result) {
            super.onPostExecute(result);
            TextView textView = findViewById(R.id.t);
           // cels=result-273;
            textView.setText(result + "");
            //double r= result;
            Switch s1=findViewById(R.id.switch1);
            s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        Toast.makeText(getApplicationContext(),result+" kelvin" , Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),(result-273.15)+" Celcius", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }


    }

    }






