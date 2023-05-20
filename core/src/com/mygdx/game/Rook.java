package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Rook extends Piece {


    public Rook(PieceColour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board) {
        int rowOffset = Math.abs(destRow - sourceRow);
        int colOffset = Math.abs(destCol - sourceCol);

        // Check if the move follows a straight line horizontally or vertically
        if ((rowOffset > 0 && colOffset == 0) || (rowOffset == 0 && colOffset > 0)) {
            int rowStep = Integer.compare(destRow, sourceRow);
            int colStep = Integer.compare(destCol, sourceCol);

            // Check for obstacles in the path
            int currentRow = sourceRow + rowStep;
            int currentCol = sourceCol + colStep;
            while (currentRow != destRow || currentCol != destCol) {
                if (board.getPiece(currentRow, currentCol) != null) {
                    return false; // Obstacle found, invalid move
                }
                currentRow += rowStep;
                currentCol += colStep;
            }

            Piece destinationPiece = board.getPiece(destRow, destCol);
            // Check if the destination is not occupied by a piece of the same color
            return destinationPiece == null || destinationPiece.getColour() != this.getColour();
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return "R";
    }

    @Override
    public int pieceValue() {
        return 5;
    }

    @Override
    public Texture getTexture() {
        if (this.getColour() == PieceColour.BLACK) {
            return new Texture("black_rook.png");
        } else {
            return new Texture("white_rook.png");
        }
    }
}
