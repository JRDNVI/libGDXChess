package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Queen extends Piece {


    public Queen(PieceColour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board) {
        return false;
    }

    @Override
    public String getSymbol() {
        return "Q";
    }

    @Override
    public Texture getTexture() {
        if(this.getColour() == PieceColour.BLACK) {
            return new Texture("black_queen.png");
        } else {
            return new Texture("white_queen.png");
        }
    }
}
