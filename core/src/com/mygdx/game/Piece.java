package com.mygdx.game;

public abstract class Piece {
    private PieceColour colour;

    public Piece(PieceColour colour) {
        this.colour = colour;
    }

    public PieceColour getColour() {
        return colour;
    }

    public void setColour(PieceColour colour) {
        this.colour = colour;
    }

    public abstract boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board);

    public abstract String getSymbol();
}
