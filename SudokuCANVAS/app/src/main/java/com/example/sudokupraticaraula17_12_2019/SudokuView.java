package com.example.sudokupraticaraula17_12_2019;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuView extends View {

    Paint paintMainLines, paintSubLines, paintMainNumbers, paintSmallNumbers;
    Jogo jogo;
    public SudokuView(Context context, Jogo jogo) {
        super(context);
        this.jogo = jogo;

        createPaints();


    }

    void createPaints(){
        paintMainLines = new Paint(Paint.DITHER_FLAG);
        paintMainLines.setStyle(Paint.Style.FILL_AND_STROKE);
        //este paint main vai servir de base para os outros paints

        paintMainLines.setColor(Color.BLACK);
        paintMainLines.setStrokeWidth(8);

        paintSubLines = new Paint(paintMainLines);//já vou receber tdas as caracteristicas q configurei no MAIN paint
        paintSubLines.setStrokeWidth(3);

        //Paint para os numeros
        paintMainNumbers = new Paint(paintSubLines);
        paintMainNumbers.setColor(Color.rgb(0,0,128));
        paintMainNumbers.setTextSize(32);
        paintMainNumbers.setTextAlign(Paint.Align.CENTER);

        paintSmallNumbers = new Paint(paintMainNumbers);
        paintSmallNumbers.setTextSize(12);
        paintSmallNumbers.setStrokeWidth(2);
        paintSmallNumbers.setColor(Color.rgb(0x40,0x80, 0xa0));

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth(), cellW = w/jogo.getBoardSize();
        int h = getHeight(), cellH = h/jogo.getBoardSize();

        //Linhas tabuleiro
        for(int i = 0; i<=jogo.getBoardSize(); i++){
            canvas.drawLine(0,i*cellH, w, i*cellH,i % 3 == 0?paintMainLines : paintSubLines);//Linhas horizontais
            canvas.drawLine(i* cellW,0, i*cellW, h,i % 3 == 0?paintMainLines : paintSubLines);//Linhas verticais
        }
        //se n há numero, retorna
        if(jogo.board == null)
            return;

        //desenhar os numeros
        paintMainNumbers.setTextSize( cellH / 2);
        paintSmallNumbers.setTextSize(cellH / 4);

        for(int r = 0; r<jogo.getBoardSize(); r++) {
            for (int c = 0; c < jogo.getBoardSize(); c++){
                int n = jogo.board[r][c];
                if(n != 0){//qnd n tem numero atribuido
                    int x = c * cellW + cellW/2;//cellW/2 deslocamento
                    int y = r * cellH + cellH/2 + cellH/6;

                    canvas.drawText(""+n,x,y,paintMainNumbers);
                }
                else{//qnd tem numero atribuido
                    List<Integer> possibilities = Arrays.asList(1,2,3,4,5,6,7,8,9);
                    Collections.shuffle(possibilities);
                    Random rnd = new Random(SystemClock.elapsedRealtime());

                    possibilities = possibilities.subList(0,rnd.nextInt(5)+1);//elementos entre 0 e 1?

                    int x = c * cellW + cellW/6;//calcular o centro do numero do canto esquerdo superior
                    int y = r * cellH + cellH/6;

                    for(int p = 1; p < jogo.getBoardSize(); p++){
                        if(possibilities.contains(p)) {//se sim, desenha-o
                            int xp = x + (p-1) %3 * cellW/3;//o espaço q cada numero ocupada na celula
                            int yp = y + (p-1) /3 * cellH/3 + cellH/9;
                            canvas.drawText(""+p,xp,yp,paintSmallNumbers);
                        }
                    }
                }
            }
        }


    }
    public void setBoard(int [][] board){
        jogo.board = board;
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN)//deve ser smp processado para os posteriores eventos terem sucesso
            return true;
        if(event.getAction() == MotionEvent.ACTION_UP){
            int px = (int)event.getX();
            int py = (int)event.getY();

            int w = getWidth(), cellW = w/jogo.getBoardSize();
            int h = getHeight(), cellH = h/jogo.getBoardSize();

            int cellX = px/cellW;
            int cellY = py/cellH;

            //DIZ AO JOGO q tem UM CELULA SELECCIONADA NO TABULEIRO
            jogo.setCellAtivaTabuleiro(true);
            jogo.setPosX(cellY); jogo.setPosY(cellX);
            //jogo.board[cellY][cellX] = 6;

            invalidate();

            return true;
        }

        return super.onTouchEvent(event);

    }
}
