package com.mygdx.game;

public class DamageSquare {
    private int col;
    private int row;
    //private Piece piece;

    public DamageSquare(int col, int row ) {
        this.col = col;
        this.row = row;
       // this.piece = piece;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

//    public Piece getPiece() {
//        return piece;
//    }
//
//    public void setPiece(Piece piece) {
//        this.piece = piece;
//    }

    @Override
    public String toString() {
        return " col: " + col +
                " row: " + row + " ";
    }
}
