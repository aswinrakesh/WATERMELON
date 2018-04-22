package com.example.hp.jsonparsing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
public class FirstActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_activty);
        Button button=(Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText=(EditText) findViewById(R.id.editText);
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name",editText.getText().toString());
                startActivity(intent);
            }
        });
    }
}
