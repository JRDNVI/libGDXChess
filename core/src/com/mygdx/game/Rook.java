package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

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

    @Override
    public Texture getTexture() {
        if(this.getColour() == PieceColour.BLACK) {
            return new Texture("black_rook.png");
        } else {
            return new Texture("white_rook.png");
        }
    }
}
