package com.example.ayush.videocallapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        Button Connect_Button=(Button) findViewById(R.id.Connect_Button);
        TextView TXTV2=(TextView) findViewById(R.id.TXTV2);
        if(TXTV2.getText().toString().equals("No Incoming Call")){
            Connect_Button.setEnabled(false);
        }
        else{

        }


        ListView OU_List=(ListView) findViewById(R.id.Other_Users_List);
        OU_List.setOnItemClickListener(this);
    }


    public void onItemClick(AdapterView<?> L, View v,int position,long id){
        Intent intent=new Intent();
        intent.setClass(this, MainActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void Start_Video_Call(View view){
        TextView TXTV2=(TextView) findViewById(R.id.TXTV2);
        TXTV2.setText("No Incoming Call");
        if(TXTV2.getText().toString().equals("No Incoming Call")){
            CharSequence MESSAGETOBEDISPLAY="No Incoming Call Received";
            Toast TTTOAST= Toast.makeText(getApplicationContext(),MESSAGETOBEDISPLAY, Toast.LENGTH_LONG);
            TTTOAST.show();
        }
    }
}
