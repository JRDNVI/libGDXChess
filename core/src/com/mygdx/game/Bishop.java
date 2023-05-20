package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(PieceColour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board) {
        int rowStep = (destRow > sourceRow) ? 1 : -1;
        int colStep = (destCol > sourceCol) ? 1 : -1;
        int currentRow = sourceRow + rowStep;
        int currentCol = sourceCol + colStep;
        Piece destinationPiece = board.getPiece(destRow, destCol);

        if (sourceRow == destRow || sourceCol == destCol) {
            return false; // Bishop can only move diagonally
        }

        // Check if destination is occupied by a piece of the same color
        if (destinationPiece != null && destinationPiece.getColour() == this.getColour()) {
            return false;
        }

        // Check if the move is a valid diagonal movement without obstacles
        int rowOffset = Math.abs(destRow - sourceRow);
        int colOffset = Math.abs(destCol - sourceCol);

        if (rowOffset != colOffset) {
            return false;
        }

        while (currentRow != destRow && currentCol != destCol) {
            // System.out.println(currentRow + " " + currentCol);
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
    public int pieceValue() {
        return 3;
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
