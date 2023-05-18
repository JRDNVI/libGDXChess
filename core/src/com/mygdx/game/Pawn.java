package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Pawn extends Piece {


    public Pawn(PieceColour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board) {
        int tileAmount = Math.abs(destRow - sourceRow);
        Piece destinationPiece = board.getPiece(destRow, destCol);
        if ((sourceRow == 1 || sourceRow == 6) && (sourceCol == destCol) && !(tileAmount > 2)) {
            return destinationPiece == null || destinationPiece.getColour() != this.getColour();
        } else if ((sourceCol == destCol) && !(tileAmount > 1)) {
            return destinationPiece == null || destinationPiece.getColour() != this.getColour();
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return "P";
    }

    @Override
    public Texture getTexture() {
        if (this.getColour() == PieceColour.BLACK) {
            return new Texture("black_pawn.png");
        } else {
            return new Texture("white_pawn.png");
        }
    }
}
