package com.mygdx.game;

import java.util.Objects;

public class DamageSquare {
    private int col;
    private int row;

    public DamageSquare(int row, int col) {
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
        return " row: " + row +
                " col: " + col + " ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DamageSquare)) return false;
        DamageSquare that = (DamageSquare) o;
        return col == that.col && row == that.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }
}
