package com.example.jogosudoku;


import android.util.Log;

enum TipoJogo{
    VAZIO,
    FACIL,
    MEDIO,
    DIFICIL
}

public class Jogo {

    private int [][] grelhaJogo;
    private TipoJogo tipoJogo;   //

//CONSTRUTORES
    public Jogo(){
        grelhaJogo = new int[9][9];
        //setTipoJogo(TipoJogo.VAZIO);
        setTipoJogo(TipoJogo.FACIL);
        geraNovoJogo();
    }

//MÉTODOS
    //Função - geraNovoJogo
    public void geraNovoJogo(){
        JogosPredefinidos jp = new JogosPredefinidos();
        switch(this.getTipoJogo()){
            case VAZIO:
                for(int linha=0; linha < 9; linha++){
                    for(int coluna = 0; coluna < 9; coluna++){
                        grelhaJogo[linha][coluna] = 10*linha + coluna;
                    }
                }
                grelhaJogo[5][0] = 55;//DEBUG
                grelhaJogo[6][0] = 55;//DEBUG
                //DEBUG imprimeGrelha();
                break;
            case FACIL:
                grelhaJogo = jp.getJogoFacil();
                break;
            case MEDIO:
                break;
            case DIFICIL:
                break;
            default:
                break;
        }

    }

    public int [][] geraJogoFacil(){
        return null;
    }
    public int [][] geraJogoMedio(){
        return null;
    }
    public int [][] geraJogoDificil(){
        return null;
    }

    public int[][] getGrelhaJogo() {
        return grelhaJogo;
    }

    public void setCelulaGrelhaJogo(int linha, int coluna){
        this.grelhaJogo[linha][coluna] = grelhaJogo[linha][coluna];

    }
    public TipoJogo getTipoJogo() {
        return tipoJogo;
    }

    public void setTipoJogo(TipoJogo tipoJogo) {
        this.tipoJogo = tipoJogo;
    }

    /*DEBUG
    public void imprimeGrelha(){
        for(int linha=0; linha < 9; linha++){
            for(int coluna = 0; coluna < 9; coluna++){
                Log.d("ddd",String.valueOf(grelhaJogo[linha][coluna])+ "Linha: "+linha+ "Coluna: "+coluna);
            }
        }
    }*/

//GETTERS/SETTERS


}
