package com.example.ayush.videocallapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onItemClick(View view){
        Intent intent=new Intent();
        intent.setClass(this, HomeScreen.class);
        startActivity(intent);
    }
}
