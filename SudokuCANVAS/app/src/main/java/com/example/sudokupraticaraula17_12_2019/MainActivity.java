package com.example.sudokupraticaraula17_12_2019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button bServer, bClient,button, button2;
    TextView tvHelpMainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bServer = (Button) findViewById(R.id.bServer);
        bClient = (Button) findViewById(R.id.bClient);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        tvHelpMainActivity = (TextView) findViewById(R.id.helpMainActivity);
    //Visibility
        bServer.setVisibility(View.INVISIBLE);
        bClient.setVisibility(View.INVISIBLE);
        tvHelpMainActivity.setVisibility(View.INVISIBLE);
    }


    //Tratamento Botoes Menu Inicial
    public void trataBotaoM1(View view) {
        Intent intent = new Intent(MainActivity.this, JogoActivity.class);
        intent.putExtra("gameMode", JogoActivity.M1);
        startActivity(intent);
    }

    public void trataBotaoM2(View view) {
        Intent intent = new Intent(MainActivity.this, JogoActivity.class);
        intent.putExtra("gameMode", JogoActivity.M2);
        startActivity(intent);
    }

    public void trataBotaoM3(View view) {




        //Visibility
        if(button.getVisibility() == View.VISIBLE){
            bServer.setVisibility(View.VISIBLE);
            bClient.setVisibility(View.VISIBLE);
            tvHelpMainActivity.setVisibility(View.VISIBLE);
            button.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
        }
        else{
            bServer.setVisibility(View.INVISIBLE);
            bClient.setVisibility(View.INVISIBLE);
            tvHelpMainActivity.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
        }

    }
    public void trataBotaoCliente(View view){
        Intent intent = new Intent(MainActivity.this, JogoActivity.class);
        intent.putExtra("gameMode", JogoActivity.M3);
        intent.putExtra("gamePlayer", JogoActivity.CLIENT1);
        startActivity(intent);
    }
    public void trataBotaoServidor(View view){

        Intent intent = new Intent(MainActivity.this, JogoActivity.class);
        intent.putExtra("gameMode", JogoActivity.M3);
        intent.putExtra("gamePlayer", JogoActivity.SERVER);
        startActivity(intent);
    }

    public void trataBotaoProfile(View view) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
