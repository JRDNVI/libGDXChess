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

        // Check if the move is a valid diagonal movement without obstacles
        int rowOffset = Math.abs(destRow - sourceRow);
        int colOffset = Math.abs(destCol - sourceCol);

        if (rowOffset != colOffset) {
            return false; // Not a valid diagonal movement
        }

        int rowStep = (destRow > sourceRow) ? 1 : -1;
        int colStep = (destCol > sourceCol) ? 1 : -1;

        int currentRow = sourceRow + rowStep;
        int currentCol = sourceCol + colStep;

        while (currentRow != destRow && currentCol != destCol) {
            if (board.getPiece(currentRow, currentCol) != null) {
                return false; // Obstacle found, invalid move
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        return true; // Valid diagonal movement without obstacles
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
