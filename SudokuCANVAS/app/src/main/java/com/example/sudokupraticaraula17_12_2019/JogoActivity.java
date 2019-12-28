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
import java.util.Enumeration;

import pt.isec.ans.sudokulibrary.Sudoku;


public class JogoActivity extends AppCompatActivity {

    public static final int M1 = 0;
    public static final int M2 = 1;
    public static final int M3 = 2;
    public static final int SERVER = 0;
    public static final int CLIENT = 1;
    private static final int PORT = 8899;

    int gameMode = M1;//default
    int gameHost = SERVER;
    ProgressDialog pd = null;

    ServerSocket serverSocket=null;
    Socket socketGame = null;
    BufferedReader input;
    PrintWriter output;
    Handler procMsg = null;

    FrameLayout flSudoku;
    SudokuView sudokuView;
    Jogo jogo;
    ImageButton btApaga, btNotas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        flSudoku = (FrameLayout)findViewById(R.id.flSudoku);

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
            gameHost = intent.getIntExtra("gameHost", SERVER);
        }

        procMsg = new Handler();

        btApaga = (ImageButton)findViewById(R.id.btApaga);
        btNotas = (ImageButton)findViewById(R.id.btNotas);


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
            if(gameHost == CLIENT)
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

    //Thread de Coms entre servidor e Clientes
    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
               /* input = new BufferedReader(new InputStreamReader(
                        socketGame.getInputStream()));
                output = new PrintWriter(socketGame.getOutputStream());
                while (!Thread.currentThread().isInterrupted()) {
                    String read = input.readLine();
                    final int move = Integer.parseInt(read);
                    Log.d("RPS", "Received: " + move);
                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            moveOtherPlayer(move);
                        }
                    });
                }*/
            } catch (Exception e) {
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
                        client(edtIP.getText().toString(), PORT); // to test with emulators: PORTaux);
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

//Métodos BOTÕES
    public void onGerar(View view) {
        String strJson = Sudoku.generate(7);
        Log.i("Sudoku", "JSON: "+strJson);
        try{
            JSONObject json = new JSONObject(strJson);
            if(json.optInt("result",0) == 1){//caso nao exista, devolve este valor como default
                //vamos converter o tabuleiro
                JSONArray jsonArray = json.getJSONArray("board");
                sudokuView.setBoard(convert(jsonArray));
            }
        }catch(Exception e){}
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
                if(validacaoSuplementar(convert(jsonArray))){
                    sudokuView.setBoard(convert(jsonArray));
                }
                else{
                    jogo.board[jogo.getPosX()][jogo.getPosY()]= 0;
                    sudokuView.invalidate();
                    Thread.sleep(1000);
                }
                //
                //sudokuView.setBoard(convert(jsonArray));
            }
            else{
                //JOGADA INVALIDA

                jogo.board[jogo.getPosX()][jogo.getPosY()]= 0;
                sudokuView.invalidate();
                Thread.sleep(1000);
            }

        }catch(Exception e){}

    }
    public boolean validacaoSuplementar(int[][] board){
        int x = jogo.getPosX(); int xTemp;
        int y = jogo.getPosY(); int yTemp;
        int numeroJogado = jogo.board[x][y];
        Log.i("jogo","Validacao supl. 1FASE");
        for(int l= 0; l<jogo.getBoardSize();l++){
            for(int c= 0; c<jogo.getBoardSize();c++){
                if(x != l && y != c) {//Salta a posicao escolhida
                    if(x == l || y == c){//linha ou coluna igual
                        if (numeroJogado == jogo.board[l][c]) {//se encontrar um nr igual
                                Log.i("jogo", "Validacao supl. - Jogada INválida");
                                Log.i("jogo", "Numero Jogado:"+numeroJogado+" Linha:"+l+" "+x+"|||Coluna:"+c+" "+y);
                                return false;
                        }
                    }
                }
            }
        }

        Log.i("jogo","Validacao supl. 2FASE");
        //Esta parte serve para validar a vizinhança do quadrado afetado
        if(x<3)
            xTemp = 0;
        else if(x>=3 && x<6)
            xTemp = 3;
        else
            xTemp = 6;
        if(y<3)
            yTemp = 0;
        else if(y>=3 && y<6)
            yTemp = 3;
        else
            yTemp = 6;
        for(int l = xTemp;l < xTemp+3; l++){
            for(int c = yTemp; c < yTemp+3; c++){
                if (numeroJogado == jogo.board[l][c]) {
                    if(x != l && y != c) {//Salta a posicao escolhida
                        Log.i("jogo", "Validacao supl. - Jogada INválida");
                        return false;
                    }
                }
            }
        }
        Log.i("jogo","Validacao supl. Terminada - Jogada válida");
        return true;
    }
    public void onClickNumerosEscolha(View view) {
        int x,y;
        Button b = (Button) view;

        if(jogo.getCellAtivaTabuleiro()){
            x = jogo.getPosX(); y = jogo.getPosY();

            jogo.board[x][y] = Integer.parseInt(b.getText().toString());
            Log.i("jogo","Board X:"+x+" Y:"+y+" valor: "+jogo.board[x][y]);
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
