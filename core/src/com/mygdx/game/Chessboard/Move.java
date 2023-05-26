package com.mygdx.game.Chessboard;

public class Move {
    private int prevRow;
    private int prevCol;
    private int col;
    private int row;

    public Move(int prevRow, int prevCol, int row, int col) {
        this.prevRow = prevRow;
        this.prevCol = prevCol;
        this.row = row;
        this.col = col;

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

    public int getPrevRow() {
        return prevRow;
    }

    public void setPrevRow(int prevRow) {
        this.prevRow = prevRow;
    }

    public int getPrevCol() {
        return prevCol;
    }

    public void setPrevCol(int prevCol) {
        this.prevCol = prevCol;
    }

    @Override
    public String toString() {
        return "Move{" +
                "prevRow=" + prevRow +
                ", prevCol=" + prevCol +
                ", row=" + row +
                ", col=" + col +
                '}';
    }
}
