package com.example.sudokupraticaraula17_12_2019;

public class Jogo {



    public  int [][] board;//public, para facilitar....rip segurança
    public static final int BOARD_SIZE = 9;
    private boolean cellAtivaTabuleiro = false;

    private int posX;
    private int posY;

    public Jogo(){
        int [][] board = {
                {0,1,0,2,0,3,0,4,0},
                {5,0,6,0,7,0,8,0,9},
                {5,0,6,0,7,0,8,0,9},
                {5,0,6,0,7,0,8,0,9},
                {5,0,6,0,7,0,8,0,9},
                {5,0,6,0,7,0,8,0,9},
                {5,0,6,0,7,0,8,0,9},
                {5,0,6,0,7,0,8,0,9},
                {5,0,6,0,7,0,8,0,9},
        };
        this.board = board;



    }

//getters/setters
    public int getBoardSize(){return BOARD_SIZE;}
    public boolean getCellAtivaTabuleiro(){return cellAtivaTabuleiro;}
    public void setCellAtivaTabuleiro(boolean cellAtivaTabuleiro){this.cellAtivaTabuleiro = cellAtivaTabuleiro;}

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
