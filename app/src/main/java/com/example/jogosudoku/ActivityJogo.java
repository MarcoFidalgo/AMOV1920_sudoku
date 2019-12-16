package com.example.jogosudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityJogo extends AppCompatActivity {

//VARIÁVEIS
    private Button bJogo[][];
    private int jogoBackup[][];//para NAO deixar EDITAR as casas de base (serve p/ validacoes)
    private TextView botaoConta;
    Jogo jogo;

    int conta = 0;//DEBUG
    int posXBotaoSelec,posYBotaoSelec, valorSelec;

    boolean flagBotaoJogoSelec = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);
        jogo = new Jogo(); jogoBackup = jogo.getGrelhaJogo();
        loadBotoes();
        corTextoTabuleiroBase();
        updateBotoes();

        botaoConta = (TextView)findViewById(R.id.tvPontos);
    }



    public void onClickNumerosEscolha(View v){
        Button b = (Button) v;//DEBUG
        botaoConta.setText(getResources().getResourceEntryName(b.getId()));//DEBUG

        if(flagBotaoJogoSelec){//só se tiver alguma cell escolhida no tabuleiro é q deixa "carregar" nos numeros de escolha
            if (posicaoValidaBase())
            bJogo[posXBotaoSelec][posYBotaoSelec].setText(b.getText());

        }
    }


    public boolean posicaoValidaBase(){
        if(jogoBackup[posXBotaoSelec][posYBotaoSelec] == 0)
            return true;
        return false;
    }


    public void onClickBotaoJogo(View v){
        Button b = (Button) v;
        botaoConta.setText(getResources().getResourceEntryName(b.getId()));

        flagBotaoJogoSelec = true;
        seleccionaBotao(b);

    }
    public void seleccionaBotao(Button b){
        int lin,col;


        for(int linha = 0; linha < 9; linha ++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                if(bJogo[linha][coluna].getId() == b.getId()){
                    posXBotaoSelec = linha; posYBotaoSelec = coluna;
                    //valorSelec = Integer.parseInt(bJogo[linha][coluna].getText().toString());    (jogo.getGrelhaJogo())[linha][coluna] == 0
                    valorSelec = (jogo.getGrelhaJogo())[linha][coluna];
                }
            }
        }
        corBotoes();




    }
    public void corBotoes(){
        corBrancoTudo();
        corQuadrado();
        for(int linha = 0; linha < 9; linha ++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                if(linha == posXBotaoSelec || coluna == posYBotaoSelec){                                /*Linha e coluna do botao seleccionado*/
                    bJogo[linha][coluna].setBackgroundColor(Color.rgb(250, 239, 210));
                }

            }
        }
        corProcuraNumsRepetidos();
    }
    public void corProcuraNumsRepetidos(){


        for(int linha = 0; linha < 9; linha ++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                if(linha == posXBotaoSelec && coluna == posYBotaoSelec){
                    bJogo[linha][coluna].setBackgroundColor(Color.rgb(250,242,150));
                }
                else if(    (jogo.getGrelhaJogo())[linha][coluna] == valorSelec && valorSelec != 0){

                        bJogo[linha][coluna].setBackgroundColor(Color.rgb(255, 193, 31));//igual ao VALOR SELECCIONADO
                        Log.i("d", "l:"+linha+" c:"+coluna);
                }
            }
        }
    }
    public void corBrancoTudo(){
        for(int linha = 0; linha < 9; linha ++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                bJogo[linha][coluna].setBackgroundColor(Color.WHITE);
            }
        }

    }
    /*public void corEspacosVaziosABranco(){
        for(int linha = 0; linha < 9; linha ++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                if((jogo.getGrelhaJogo())[linha][coluna] == 0)
                bJogo[linha][coluna].setBackgroundColor(Color.WHITE);
            }
        }
    }*/


    public void corQuadrado(){
        Log.i("d", "corQuadrado");
        int linha, coluna;
        if(posXBotaoSelec < 3)
            linha = 0;
        else if(posXBotaoSelec >=3 && posXBotaoSelec < 6)
            linha = 3;
        else
            linha = 6;

        if(posYBotaoSelec < 3)
            coluna = 0;
        else if(posYBotaoSelec >=3 && posYBotaoSelec < 6)
            coluna = 3;
        else
            coluna = 6;

        for(int l = linha; l < linha + 3; l ++){
            for(int c = coluna; c < coluna + 3; c ++){
                bJogo[l][c].setBackgroundColor(Color.rgb(250, 239, 210));

            }
        }
    }

    public void corTextoTabuleiroBase(){
        for(int linha = 0; linha < 9; linha ++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                if( (jogo.getGrelhaJogo())[linha][coluna] != 0  )
                    bJogo[linha][coluna].setTextColor(Color.BLACK);
            }
        }

    }
    public void updateBotoes(){

        for(int linha = 0; linha < 9; linha ++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                if( (jogo.getGrelhaJogo())[linha][coluna] == 0  ){
                    bJogo[linha][coluna].setText(" ");

                }
                else {
                    bJogo[linha][coluna].setText(String.valueOf((jogo.getGrelhaJogo())[linha][coluna]));
                    //Log.d("ddd",String.valueOf((jogo.getGrelhaJogo())[linha][coluna]));
                }
            }
        }
    }
    public void loadBotoes(){
        bJogo = new Button[9][9];
    //Linha 1
        bJogo[0][0] = (Button)findViewById(R.id.b00);
        bJogo[0][1] = (Button)findViewById(R.id.b01);
        bJogo[0][2] = (Button)findViewById(R.id.b02);
        bJogo[0][3] = (Button)findViewById(R.id.b03);
        bJogo[0][4] = (Button)findViewById(R.id.b04);
        bJogo[0][5] = (Button)findViewById(R.id.b05);
        bJogo[0][6] = (Button)findViewById(R.id.b06);
        bJogo[0][7] = (Button)findViewById(R.id.b07);
        bJogo[0][8] = (Button)findViewById(R.id.b08);
    //Linha 2
        bJogo[1][0] = (Button)findViewById(R.id.b10);
        bJogo[1][1] = (Button)findViewById(R.id.b11);
        bJogo[1][2] = (Button)findViewById(R.id.b12);
        bJogo[1][3] = (Button)findViewById(R.id.b13);
        bJogo[1][4] = (Button)findViewById(R.id.b14);
        bJogo[1][5] = (Button)findViewById(R.id.b15);
        bJogo[1][6] = (Button)findViewById(R.id.b16);
        bJogo[1][7] = (Button)findViewById(R.id.b17);
        bJogo[1][8] = (Button)findViewById(R.id.b18);
    //Linha 3
        bJogo[2][0] = (Button)findViewById(R.id.b20);
        bJogo[2][1] = (Button)findViewById(R.id.b21);
        bJogo[2][2] = (Button)findViewById(R.id.b22);
        bJogo[2][3] = (Button)findViewById(R.id.b23);
        bJogo[2][4] = (Button)findViewById(R.id.b24);
        bJogo[2][5] = (Button)findViewById(R.id.b25);
        bJogo[2][6] = (Button)findViewById(R.id.b26);
        bJogo[2][7] = (Button)findViewById(R.id.b27);
        bJogo[2][8] = (Button)findViewById(R.id.b28);
    //Linha 4
        bJogo[3][0] = (Button)findViewById(R.id.b30);
        bJogo[3][1] = (Button)findViewById(R.id.b31);
        bJogo[3][2] = (Button)findViewById(R.id.b32);
        bJogo[3][3] = (Button)findViewById(R.id.b33);
        bJogo[3][4] = (Button)findViewById(R.id.b34);
        bJogo[3][5] = (Button)findViewById(R.id.b35);
        bJogo[3][6] = (Button)findViewById(R.id.b36);
        bJogo[3][7] = (Button)findViewById(R.id.b37);
        bJogo[3][8] = (Button)findViewById(R.id.b38);
    //Linha 5
        bJogo[4][0] = (Button)findViewById(R.id.b40);
        bJogo[4][1] = (Button)findViewById(R.id.b41);
        bJogo[4][2] = (Button)findViewById(R.id.b42);
        bJogo[4][3] = (Button)findViewById(R.id.b43);
        bJogo[4][4] = (Button)findViewById(R.id.b44);
        bJogo[4][5] = (Button)findViewById(R.id.b45);
        bJogo[4][6] = (Button)findViewById(R.id.b46);
        bJogo[4][7] = (Button)findViewById(R.id.b47);
        bJogo[4][8] = (Button)findViewById(R.id.b48);
    //Linha 6
        bJogo[5][0] = (Button)findViewById(R.id.b50);
        bJogo[5][1] = (Button)findViewById(R.id.b51);
        bJogo[5][2] = (Button)findViewById(R.id.b52);
        bJogo[5][3] = (Button)findViewById(R.id.b53);
        bJogo[5][4] = (Button)findViewById(R.id.b54);
        bJogo[5][5] = (Button)findViewById(R.id.b55);
        bJogo[5][6] = (Button)findViewById(R.id.b56);
        bJogo[5][7] = (Button)findViewById(R.id.b57);
        bJogo[5][8] = (Button)findViewById(R.id.b58);
    //Linha 7
        bJogo[6][0] = (Button)findViewById(R.id.b60);
        bJogo[6][1] = (Button)findViewById(R.id.b61);
        bJogo[6][2] = (Button)findViewById(R.id.b62);
        bJogo[6][3] = (Button)findViewById(R.id.b63);
        bJogo[6][4] = (Button)findViewById(R.id.b64);
        bJogo[6][5] = (Button)findViewById(R.id.b65);
        bJogo[6][6] = (Button)findViewById(R.id.b66);
        bJogo[6][7] = (Button)findViewById(R.id.b67);
        bJogo[6][8] = (Button)findViewById(R.id.b68);
    //Linha 8
        bJogo[7][0] = (Button)findViewById(R.id.b70);
        bJogo[7][1] = (Button)findViewById(R.id.b71);
        bJogo[7][2] = (Button)findViewById(R.id.b72);
        bJogo[7][3] = (Button)findViewById(R.id.b73);
        bJogo[7][4] = (Button)findViewById(R.id.b74);
        bJogo[7][5] = (Button)findViewById(R.id.b75);
        bJogo[7][6] = (Button)findViewById(R.id.b76);
        bJogo[7][7] = (Button)findViewById(R.id.b77);
        bJogo[7][8] = (Button)findViewById(R.id.b78);
    //Linha 9
        bJogo[8][0] = (Button)findViewById(R.id.b80);
        bJogo[8][1] = (Button)findViewById(R.id.b81);
        bJogo[8][2] = (Button)findViewById(R.id.b82);
        bJogo[8][3] = (Button)findViewById(R.id.b83);
        bJogo[8][4] = (Button)findViewById(R.id.b84);
        bJogo[8][5] = (Button)findViewById(R.id.b85);
        bJogo[8][6] = (Button)findViewById(R.id.b86);
        bJogo[8][7] = (Button)findViewById(R.id.b87);
        bJogo[8][8] = (Button)findViewById(R.id.b88);
    }
}
