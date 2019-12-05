package com.example.jogosudoku;


import android.util.Log;

enum TipoJogo{
    VAZIO,
    LOCALMENTE,
    SERVICO
}

public class Jogo {

    private int [][] grelhaJogo;
    private TipoJogo tipoJogo;   //0 - Vazio, 1 - Gerado localmente, 2 - Gerado por serviço,

//CONSTRUTORES
    public Jogo(){
        grelhaJogo = new int[9][9];
        setTipoJogo(TipoJogo.VAZIO);
        geraNovoJogo();
    }

//MÉTODOS
    //Função - geraNovoJogo
    public void geraNovoJogo(){
        switch(this.getTipoJogo()){
            case VAZIO:
                for(int linha=0; linha < 9; linha++){
                    for(int coluna = 0; coluna < 9; coluna++){
                        grelhaJogo[linha][coluna] = 10*linha + coluna;
                    }
                }
                //DEBUG imprimeGrelha();
                break;
            case LOCALMENTE:
                break;
            case SERVICO:
                break;
            default:
                break;
        }

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
