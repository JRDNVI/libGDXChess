package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Knight extends Piece {


    public Knight(PieceColour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board) {
        return false;
    }

    @Override
    public String getSymbol() {
        return "N";
    }

    @Override
    public Texture getTexture() {
        if(this.getColour() == PieceColour.BLACK) {
            return new Texture("black_knight.png");
        } else {
            return new Texture("white_knight.png");
        }
    }
}
