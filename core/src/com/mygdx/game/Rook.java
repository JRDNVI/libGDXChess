package com.mygdx.game;

public class Rook extends Piece {


    public Rook(PieceColour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board) {
        return false;
    }

    @Override
    public String getSymbol() {
        return "R";
    }
}
