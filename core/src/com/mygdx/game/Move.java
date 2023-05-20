package com.mygdx.game;

public class Move {
    private int col;
    private int row;

    public Move(int col, int row ) {
        this.col = col;
        this.row = row;
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

    @Override
    public String toString() {
        return " col: " + col +
                " row: " + row + " ";
                //", piece=" + piece +;
    }
}
