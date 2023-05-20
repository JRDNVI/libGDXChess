package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Queen extends Piece {

    public Queen(PieceColour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board) {
        if (sourceRow == destRow && sourceCol == destCol) {
            return false; // Queen cannot stay in the same position
        }

        Piece destinationPiece = board.getPiece(destRow, destCol);

        // Check if destination is occupied by a piece of the same color
        if (destinationPiece != null && destinationPiece.getColour() == this.getColour()) {
            return false; // Queen cannot take its own team's pieces
        }

        int rowOffset = Math.abs(destRow - sourceRow);
        int colOffset = Math.abs(destCol - sourceCol);

        // Check if the move is a valid diagonal movement
        if (rowOffset == colOffset) {
            // Check for obstacles along the diagonal path
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

        // Check if the move is a valid straight movement along rows or columns
        if (sourceRow == destRow || sourceCol == destCol) {
            // Check for obstacles along the row or column path
            int rowStep = (sourceRow != destRow) ? ((destRow - sourceRow) / rowOffset) : 0;
            int colStep = (sourceCol != destCol) ? ((destCol - sourceCol) / colOffset) : 0;

            int currentRow = sourceRow + rowStep;
            int currentCol = sourceCol + colStep;

            while (currentRow != destRow || currentCol != destCol) {
                if (board.getPiece(currentRow, currentCol) != null) {
                    return false; // Obstacle found, invalid move
                }
                currentRow += rowStep;
                currentCol += colStep;
            }

            return true; // Valid straight movement without obstacles
        }

        return false; // Invalid move (not diagonal or straight)
    }

    @Override
    public String getSymbol() {
        return "Q";
    }

    @Override
    public int pieceValue() {
        return 9;
    }

    @Override
    public Texture getTexture() {
        if (this.getColour() == PieceColour.BLACK) {
            return new Texture("black_queen.png");
        } else {
            return new Texture("white_queen.png");
        }
    }
}
