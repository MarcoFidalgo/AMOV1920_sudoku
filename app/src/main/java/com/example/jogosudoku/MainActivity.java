package com.example.jogosudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }













//Tratamento Botoes Menu Inicial
    public void trataBotaoM1(View view) {
        Intent intent = new Intent(MainActivity.this, ActivityJogo.class);
        intent.putExtra("gameMode", 1);
        startActivity(intent);
    }

    public void trataBotaoM2(View view) {
        Intent intent = new Intent(MainActivity.this, ActivityJogo.class);
        intent.putExtra("gameMode", 2);
        startActivity(intent);
    }

    public void trataBotaoM3(View view) {
        Intent intent = new Intent(MainActivity.this, ActivityJogo.class);
        intent.putExtra("gameMode", 3);
        startActivity(intent);
    }
}
