package com.example.sudokupraticaraula17_12_2019;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

import pt.isec.ans.sudokulibrary.Sudoku;


public class JogoActivity extends AppCompatActivity {

    public static final int M1 = 0;
    public static final int M2 = 1;
    public static final int M3 = 2;
    public static final int SERVER = 0;
    public static final int CLIENT1 = 1;
    public static final int CLIENT2 = 2;
    private static final int PORT = 8899;
    private int[][] localBoard;
    boolean flagInicial = false;
    private int jogada[] = {0,0,0,0};
    private int jogadas[][] = { jogada,jogada,jogada };
    private int wins[] = { 0, 0, 0 };

    private int gameMode = M1;//default
    private int gamePlayer = SERVER;
    ProgressDialog pd = null;

    ServerSocket serverSocket=null;
    Socket socketGame = null;
    BufferedReader input;
    PrintWriter output;
    Handler procMsg = null;
    JSONObject json;
    FrameLayout flSudoku;
    SudokuView sudokuView;
    Jogo jogo;
    ImageButton btApaga, btNotas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        flSudoku = (FrameLayout)findViewById(R.id.flSudoku);
        localBoard = new int[9][9];
        jogo = new Jogo();
        sudokuView = new SudokuView(this, jogo);
        flSudoku.addView(sudokuView);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this, R.string.error_netconn, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Intent intent = getIntent();
        if (intent != null){
            gameMode = intent.getIntExtra("gameMode", M1);
            gamePlayer = intent.getIntExtra("gamePlayer", SERVER);
        }

        procMsg = new Handler();

        btApaga = (ImageButton)findViewById(R.id.btApaga);
        btNotas = (ImageButton)findViewById(R.id.btNotas);

        if(gamePlayer == SERVER) {//Se jogador for o serv, cria o tabuleiro
            onGerar(null);
            Log.d("jogo","[S] Preenchi o tabuleiro");
        }

        //Visibility
        /* playButton.setVisibility(View.GONE);
         stopButton.setVisibility(View.VISIBLE);*/
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (gameMode == M1){//singleplayer
            /*m1();*/}
        else  if(gameMode == M2){// 2 Jogadores LOCAL
            /*m2();*/}
        else if(gameMode == M3){//  2/3 Jogadores REDE
            if(gamePlayer == CLIENT1 || gamePlayer == CLIENT2)
                clientDlg();
            else
                server();
        }
    }

    public void server(){
        String ip = getLocalIpAddress();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.serverdlg_msg) + "\n(IP: " + ip
                + ")");
        pd.setTitle(R.string.serverdlg_title);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
                if (serverSocket!=null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                    }
                    serverSocket=null;
                }
            }
        });
        pd.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                    socketGame = serverSocket.accept();//espera ligações dos clientes
                    serverSocket.close();
                    serverSocket=null;
                    commThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    socketGame = null;
                }
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if (socketGame == null)
                            finish();
                    }
                });
            }
        });
        t.start();


    }

    void moveOtherPlayer(int[] move) {
        //if (move == ROCK || move == PAPER || move == SCISSORS) {
        if(move[2] != 0){//se for != 0 é uma jogada válida(1-9)
            Log.d("jogo","Validação da jogada X:"+move[0]+" Y:"+move[1]+" Valor:"+move[2]+" Jogador: "+move[3]);
            jogadas[move[3]] = move.clone();
            verifyGame();

        }
    }

//[S] Thread para COMS entre clientes/servidor
    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {

            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayJogada = new JSONArray();
            try {
                input = new BufferedReader(new InputStreamReader(
                        socketGame.getInputStream()));
                output = new PrintWriter(socketGame.getOutputStream());
                while (!Thread.currentThread().isInterrupted()) {
                    if(gamePlayer == SERVER){
                    //if(gamePlayer == SERVER && flagInicial == false){
                        flagInicial = true;
                    //ENVIA o tabuleiro inicial ao novo jogador

                        json = new JSONObject();
                        json.put("board",convert(jogo.board));

                        Log.d("jogo","X41: "+Arrays.toString(jogada));
                        json.put("jogada",convertJogada(jogada));
                        Log.d("jogo","X42: "+json.toString());

                        output.println(json);
                        output.flush();

                    }
                    //RECEBE

                    String strJson = input.readLine();
                    Log.d("jogo", "Recebi pedido: "+strJson);
                    try{
                        json = new JSONObject(strJson);
                        jsonArray = json.getJSONArray("board");
                        jsonArrayJogada = json.getJSONArray("jogada");
                        jogo.board = (convert(jsonArray)).clone();

                        //BUSCA ID DO jogador EM CAUSA
                        int aa[] = convertJogada(jsonArrayJogada);
                        int idJogador = aa[3];//index 3 é o ID do jogador

                        jogadas[idJogador] = (convertJogada(jsonArrayJogada)).clone();
                        jogada = jogadas[convertJogada(jsonArrayJogada)[3]];

                        //Log.d("jogo", "Pedido Recebido : " + Arrays.toString(convertJogada(json.getJSONArray("jogada"))));

                        updateBoard();

                    }catch(Exception e){
                        Log.d("jogo","ERRO recebe "+e.toString());
                    }


                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            moveOtherPlayer(jogada);
                        }
                    });
                    //[S] Vou enviar a validacao da jogada ao outro jogador


                }
            } catch (Exception e) {
                Log.d("jogo","ERRO G "+e.toString());
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Toast.makeText(getApplicationContext(),
                                R.string.game_finished, Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        }
    });
    void clientDlg() {
        final EditText edtIP = new EditText(this);
        edtIP.setText("192.168.0.161");
        AlertDialog ad = new AlertDialog.Builder(this).setTitle("RPS Client")
                .setMessage("Server IP").setView(edtIP)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        client(edtIP.getText().toString(), PORT);
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }).create();
        ad.show();
    }

    void client(final String strIP, final int Port) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("RPS", "Connecting to the server  " + strIP);
                    socketGame = new Socket(strIP, Port);
                } catch (Exception e) {
                    socketGame = null;
                }
                if (socketGame == null) {
                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                    return;
                }
                Log.i("jogo","FUNC: client, Ligação estabelecida. Vou começar a comunicar");
                commThread.start();
            }
        });
        t.start();
    }

    void updateBoard(){
        /*Anything that causes the UI to be updated or changed HAS to happen on the UI thread.*/
        /*https://stackoverflow.com/questions/3652560/what-is-the-android-uithread-ui-thread*/
        procMsg.post(new Runnable() {
            @Override
            public void run() {
                sudokuView.setBoard(jogo.board);
            }
        });

        Log.d("jogo", "UPDATE "+Arrays.deepToString(jogo.board));

    }

//Métodos BOTÕES
    public void onGerar(View view) {
        String strJson = Sudoku.generate(7);
        Log.i("Sudoku", "JSON: "+strJson);
        try{
            JSONObject json = new JSONObject(strJson);
            //if(json.optInt("result",0) == 1){//caso nao exista, devolve este valor como default
                //vamos converter o tabuleiro
                JSONArray jsonArray = json.getJSONArray("board");
                sudokuView.setBoard(convert(jsonArray));
            //}
        }catch(Exception e){}
    }


    void moveMyPlayer(int [] move) {
        //if (jogada[0] != -1 && jogada[1] != -1 && jogada[2] != -1 && jogada[3] != -1) {//x,y,valor,nºjogador(0=serv, 1=cli1, 2=cli2)
            jogadas[SERVER] = move;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        /*AQUI AQUI*/
                        /*é necessário passar um JSON correto, só um array convertido em json estoira no logcat A11
                        está [2 2 1 3] e devia estar   {"jogada":[2 2 1 3]}*/
                        JSONObject json = new JSONObject();
                        json.put("jogada",convertJogada(jogadas[SERVER]));
                        json.put("board",convert(jogo.board));//só para n dar erro(de faltar um board no json)

                        Log.d("jogo", "Sending move - moveMyPlayer(): " + json.toString());
                        output.println(json);
                        output.flush();
                    } catch (Exception e) {
                        Log.d("jogo", "Error sending a move");
                    }
                }
            });
            t.start();
            verifyGame();
       // }
    }
    void verifyGame() {/*FALTA: tirar o gamePlayer, para as validaçoes,uso o ultimo campo da Jogada q tem o ID do jogador*/

        int x,y,valor,jogador;

        if(gamePlayer == SERVER){//só o servidor é q valida jogadas
            for(int i=0;i<3; i++){
                x = jogadas[i][0];
                y = jogadas[i][1];
                valor = jogadas[i][2];
                jogador = jogadas[i][3];

                //1ªvalidacao - Repetição de valor na Vizinhança ou coluna/linha
                if(validacaoSuplementar(x, y, valor)) {
                    //2ªvalidação - Validação feita pelo serviço disponibilizado pelo prof.


                    jogo.board[x][y] = valor;
                    Log.d("jogo","[S] Jogada ACEITE");

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                /*ENVIA Tabuleiro correto*/
                                JSONObject json = new JSONObject();
                                json.put("jogada", convertJogada(jogadas[SERVER]));
                                json.put("board", convert(jogo.board));//só para n dar erro(de faltar um board no json)

                                Log.d("jogo", "Sending jogo To Player: " + json.toString());
                                output.println(json);
                                output.flush();
                            } catch (Exception e) {
                                Log.d("jogo", "Error sending board" + e);
                            }
                        }
                    });
                    t.start();
                    updateBoard();
                }
                else{
                    Log.d("jogo","[S] A Jogada NAO FOI aceite como final");
                }
                //Limpar a jogada do array 'jogadas' smp q faz uma validacao
                jogadas[i][0] = 0;jogadas[i][1] = 0;jogadas[i][2] = 0;jogadas[i][3] = 0;
            }

        }

    }
    public void onResolver(View view) {
        try{
            //criar um objeto JSON com o tabuleiro la dentro
            JSONObject json = new JSONObject();
            json.put("board",convert(jogo.board));//.getboard, neste caso estou a aceder diretamente

            String strJson = Sudoku.solve(json.toString(), 2500);//1500 timeout para resolver
            Log.i("jogo","JOGO: "+strJson);

            json = new JSONObject(strJson);
            //JOGADA VALIDA
            if(json.optInt("result",0) == 1){//caso nao exista, devolve este valor como default
                //vamos converter o tabuleiro
                JSONArray jsonArray = json.getJSONArray("board");
                //Validação suplementar, para reforçar soluções erradas da biblioteca
                //if(validacaoSuplementar(convert(jsonArray))){
                //    sudokuView.setBoard(convert(jsonArray));
                //}
                //else{
                //    jogo.board[jogo.getPosX()][jogo.getPosY()]= 0;
                //    sudokuView.invalidate();
                //    Thread.sleep(1000);
                //}
                //
                //sudokuView.setBoard(convert(jsonArray));
            }
            else{
                //JOGADA INVALIDA

                //jogo.board[jogo.getPosX()][jogo.getPosY()]= 0;
                //sudokuView.invalidate();
                //Thread.sleep(1000);
            }

        }catch(Exception e){}

    }
    public boolean validacaoSuplementar(int x, int y, int numeroJogado){
         int xTemp;
        int yTemp;
        if(numeroJogado != 0) {
            Log.i("jogo", "Validacao supl. 1FASE");//OK

            for(int l= 0; l < 9;l++){
                for(int c= 0; c < 9;c++){
                    //if(x != l && y != c) {//Salta a posicao escolhida
                    if(x == l || y == c){//linha ou coluna igual
                        if (numeroJogado == jogo.board[l][c]) {//se encontrar um nr igual
                            if(x == l && y == c) {
                                continue;//Salta NUMERO escolhido
                            }
                            Log.i("jogo", "Validacao supl. - Jogada INválida");
                            Log.i("jogo", "Numero Jogado:"+numeroJogado+" Linha:"+l+" "+x+"|||Coluna:"+c+" "+y);
                            return false;
                        }
                    }
                    //}
                }
            }

            Log.i("jogo", "Validacao supl. 2FASE");//OK
            //Esta parte serve para validar a vizinhança do quadrado afetado
            if (x < 3)
                xTemp = 0;
            else if (x >= 3 && x < 6)
                xTemp = 3;
            else
                xTemp = 6;
            if (y < 3)
                yTemp = 0;
            else if (y >= 3 && y < 6)
                yTemp = 3;
            else
                yTemp = 6;
            for (int l = xTemp; l < xTemp + 3; l++) {
                for (int c = yTemp; c < yTemp + 3; c++) {
                    if (numeroJogado == jogo.board[l][c]) {
                        return false;
                    }
                }
            }

            Log.i("jogo", "VAL. supl. Terminada - Jogada válida: xTemp: "+xTemp+" yTemp:"+yTemp+" valor:"+numeroJogado);
            return true;
        }
        Log.i("jogo", "Validacao supl.INVALIDA");
        return false;//jogada valor = 0(invalida)
    }
    //conversao de um JSON para uma matriz
    int [][] convert(JSONArray jsonArray){
        int [][] array = new int[9][9];


        try{
            for(int r = 0; r < 9; r++){
                JSONArray jsonRow = jsonArray.getJSONArray(r);
                for(int c = 0; c < 9; c++){
                    array[r][c] = jsonRow.getInt(c);
                }
            }

        }catch(Exception e){
            array = null;
        }
        return array;
    }
    //conversao de uma matriz para um JSON
    JSONArray convert(int[][] array){
        JSONArray jsonArray = new JSONArray();
        try{
            for(int r = 0; r< 9; r++){
                JSONArray jsonRow = new JSONArray();
                for(int c = 0; c< 9; c++)
                    jsonRow.put(array[r][c]);
                jsonArray.put(jsonRow);
            }
        }
        catch(Exception e){

        }
        return jsonArray;
    }
    //conversao de uma JOGADA(JSON) para um ARRAY
    int [] convertJogada(JSONArray jsonArray){
        Log.d("jogo","PrintJSON NA CONVERSAO: "+jsonArray.toString());
        int [] array = new int[4];
        try{
            // Elementos array:
            // 0-Linha(x),     1-Coluna(y),    2-valorEscolhido  3-IdJogador
            for(int elem = 0; elem < 4; elem++){
                array[elem] = jsonArray.getInt(elem);
            }
        }catch(Exception e){
            array = null;
            Log.d("jogo","ERROR ConvertJogadaArray "+e.toString());
        }
        Log.d("jogo","ConvertJogadaArray: "+Arrays.toString(array));
        return array;
    }
    //conversao de uma JOGADA(array) para um JSON
    JSONArray convertJogada(int[] array){
        JSONArray jsonArray = new JSONArray();
        try{
            // Elementos array:
            // 0-Linha(x),     1-Coluna(y),    2-valorEscolhido
            for(int elem = 0; elem < 4; elem++){
                jsonArray.put(array[elem]);
            }
        }
        catch(Exception e){
            Log.d("jogo","ERROConvertJogadaJson "+e.toString());
        }
        Log.d("jogo","ConvertJogadaJson: "+jsonArray.toString());
        return jsonArray;
    }

    public void onClickNumerosEscolha(View view) {
        //int x,y;
        Button b = (Button) view;

        if(jogo.getCellAtivaTabuleiro()){
            jogada[0] = jogo.getPosX();//Posição no tabuleiro x,y
            jogada[1] = jogo.getPosY();
            jogada[2] = Integer.parseInt(b.getText().toString());                            //valor escolhido 1-9
            jogada[3] = gamePlayer;                                                         //passa tambem o ID DO JOGADOR
            //jogo.board[jogada[0]][jogada[1]] = jogada[2]; //FALTA: apagar isto . só desenha localmente, nao tem utilidade futuramente

            Log.d("jogo","Board X:"+jogada[0]+" Y:"+jogada[1]+" valor: "+jogada[2]+" JOGADOR: "+jogada[3]);
            moveMyPlayer(jogada);
            sudokuView.invalidate();

        }
    }



    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
