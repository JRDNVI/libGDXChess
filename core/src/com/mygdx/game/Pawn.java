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
        if (sourceCol == destCol) {
            // Moving forward
            if (this.getColour() == PieceColour.BLACK && destRow == sourceRow + 1 && destinationPiece == null) {
                return true;
            }
            // Moving forward two squares from initial position
            else if (this.getColour() == PieceColour.BLACK && sourceRow == 1 && destRow == 3 && destinationPiece == null && board.getPiece(2, destCol) == null) {
                return true;
            }
            // Moving forward
            else if (this.getColour() == PieceColour.WHITE && destRow == sourceRow - 1 && destinationPiece == null) {
                return true;
            }
            // Moving forward two squares from initial position
            else
                return this.getColour() == PieceColour.WHITE && sourceRow == 6 && destRow == 4 && destinationPiece == null && board.getPiece(5, destCol) == null;
        } else if (Math.abs(sourceCol - destCol) == 1) {
            // Capturing diagonally
            if (this.getColour() == PieceColour.BLACK && destRow == sourceRow + 1 && destinationPiece != null && destinationPiece.getColour() == PieceColour.WHITE) {
                return true;
            } else
                return this.getColour() == PieceColour.WHITE && destRow == sourceRow - 1 && destinationPiece != null && destinationPiece.getColour() == PieceColour.BLACK;
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
