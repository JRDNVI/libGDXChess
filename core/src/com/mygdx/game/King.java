package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class King extends Piece{


    public King(PieceColour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board) {
        return false;
    }

    @Override
    public String getSymbol() {
        return "K";
    }

    @Override
    public Texture getTexture() {
        if(this.getColour() == PieceColour.BLACK) {
            return new Texture("black_king.png");
        } else {
            return new Texture("white_king.png");
        }
    }
}
