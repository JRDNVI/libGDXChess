package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;


public class Pawn extends Piece {

    public Pawn(PieceColour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board) {
        Piece destinationPiece = board.getPiece(destRow, destCol);
        Piece piece = board.getPiece(sourceRow, sourceCol);
        PieceColour pieceColour = this.getColour();

        if (sourceCol == destCol) {
            // Moving vertically
            int rowOffset = pieceColour == PieceColour.BLACK ? 1 : -1;

            // Moving one square forward
            if (destRow == sourceRow + rowOffset && destinationPiece == null ) {
                return true;
            }

            // Moving two squares forward from initial position
            if (sourceRow == (pieceColour == PieceColour.BLACK ? 1 : 6) && destRow == sourceRow + 2 * rowOffset && destinationPiece == null && board.getPiece(sourceRow + rowOffset, destCol) == null) {
                return true;
            }
        } else if (Math.abs(sourceCol - destCol) == 1) {
            // Capturing diagonally
            int rowOffset = pieceColour == PieceColour.BLACK ? 1 : -1;

            if (destRow == sourceRow + rowOffset && destinationPiece != null && destinationPiece.getColour() != pieceColour) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return "";
    }

    @Override
    public int pieceValue() {
        return 1;
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
