package com.example.jogosudoku;

import android.util.Log;

import java.util.Random;

public final class JogosPredefinidos {

//DIFICULDADE: Facil
    final int[][] jogoFacil1 = {
        { 0, 0, 0, 2, 6, 0, 7, 0, 1 },
        { 6, 8, 0, 0, 7, 0, 0, 9, 0 },
        { 1, 9, 0, 0, 0, 4, 5, 0, 0 },

        { 8, 2, 0, 1, 0, 0, 0, 4, 0 },
        { 0, 0, 4, 6, 0, 5, 9, 0, 0 },
        { 0, 5, 0, 0, 0, 3, 0, 2, 8 },

        { 0, 0, 9, 3, 0, 0, 0, 7, 4 },
        { 0, 4, 0, 0, 5, 0, 0, 3, 6 },
        { 7, 0, 3, 0, 1, 8, 0, 0, 0 }
    };
    final int[][] jogoFacil2 = {
        { 1, 0, 0, 4, 8, 9, 0, 0, 6 },
        { 7, 3, 0, 0, 0, 0, 0, 4, 0 },
        { 0, 0, 0, 0, 0, 1, 2, 9, 5 },

        { 0, 0, 7, 1, 2, 0, 6, 0, 0 },
        { 5, 0, 0, 7, 0, 3, 0, 0, 8 },
        { 0, 0, 6, 0, 9, 5, 7, 0, 0 },

        { 9, 1, 4, 6, 0, 0, 0, 0, 0 },
        { 0, 2, 0, 0, 0, 0, 0, 3, 7 },
        { 8, 0, 0, 5, 1, 2, 0, 0, 4 }
    };
    final int[][] jogoFacil3 = {
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },

        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },

        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 }
    };
//DIFICULDADE: Dificil XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXFALTA ACABAR OS TABULEIROS, FACIL; MEDIO E DIFICIL (Preencher)
    final int[][] jogoDificil1 = {
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },

        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },

        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 }
    };
    final int[][] jogoDificil2 = {
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },

        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },

        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 }
    };
    final int[][] jogoDificil3 = {
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },

        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },

        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 }
    };

    public JogosPredefinidos() {

    }

    public int[][] getJogoFacil(){
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        int ran = r.nextInt((3 - 1) + 1) + 1;//int ran = r.nextInt((max - min) + 1) + min;

        switch(ran){
            case 1:return jogoFacil1;
            case 2:return jogoFacil2;
            case 3:return jogoFacil3;
        }
        Log.i("d","DEBUG: Nao passou pelo RANDOM");
        return jogoFacil1;
    }


    public int[][] getJogoDificil(){
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        int ran = r.nextInt((3 - 1) + 1) + 1;//int ran = r.nextInt((max - min) + 1) + min;

        switch(ran){
            case 1:return jogoDificil1;
            case 2:return jogoDificil2;
            case 3:return jogoDificil3;
        }
        Log.i("d","DEBUG: Nao passou pelo RANDOM");
        return jogoFacil1;
    }


}
