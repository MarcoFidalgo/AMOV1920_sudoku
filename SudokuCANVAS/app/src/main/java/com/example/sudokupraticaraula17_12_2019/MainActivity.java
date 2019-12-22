package com.example.sudokupraticaraula17_12_2019;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import pt.isec.ans.sudokulibrary.Sudoku;


public class MainActivity extends AppCompatActivity {


    FrameLayout flSudoku;
    SudokuView sudokuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flSudoku = (FrameLayout)findViewById(R.id.flSudoku);
        sudokuView = new SudokuView(this);
        flSudoku.addView(sudokuView);


    }

int [][] convert(JSONArray jsonArray){//conversao de um JSON para uma matriz
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
            json.put("board",convert(sudokuView.board));//.getboard, neste caso estou a aceder diretamente

            String strJson = Sudoku.solve(json.toString(), 1500);//1500 timeout para resolver

            json = new JSONObject(strJson);
            if(json.optInt("result",0) == 1){//caso nao exista, devolve este valor como default
                //vamos converter o tabuleiro
                JSONArray jsonArray = json.getJSONArray("board");
                sudokuView.setBoard(convert(jsonArray));
            }
        }catch(Exception e){}

    }
}
