package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Bishop extends Piece {

    public Bishop(PieceColour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board) {
        if (sourceRow == destRow || sourceCol == destCol) {
            return false; // Bishop can only move diagonally
        }

        Piece destinationPiece = board.getPiece(destRow, destCol);

        // Check if destination is occupied by a piece of the same color
        if (destinationPiece != null && destinationPiece.getColour() == this.getColour()) {
            return false; // Bishop cannot take its own team's pieces
        }

        // Check if the move is a valid diagonal movement
        int rowOffset = Math.abs(destRow - sourceRow);
        int colOffset = Math.abs(destCol - sourceCol);
        return rowOffset == colOffset;
    }

    @Override
    public String getSymbol() {
        return "B";
    }

    @Override
    public Texture getTexture() {
        if (this.getColour() == PieceColour.BLACK) {
            return new Texture("black_bishop.png");
        } else {
            return new Texture("white_bishop.png");
        }
    }
}
