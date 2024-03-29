package com.mygdx.game.Pieces;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Chessboard.ChessBoard;

public class King extends Piece {


    public King(PieceColour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board) {
        int rowOffset = Math.abs(destRow - sourceRow);
        int colOffset = Math.abs(destCol - sourceCol);
        Piece king = board.getPiece(sourceRow, sourceCol);

        // Check if the destination is within one square in any direction
        if ((rowOffset == 1 && colOffset <= 1) || (colOffset == 1 && rowOffset <= 1)) {
            Piece destinationPiece = board.getPiece(destRow, destCol);
            return destinationPiece == null || destinationPiece.getColour() != king.getColour(); // Move is valid
        }
        return false; // Move is not valid
    }

    @Override
    public String getSymbol() {
        return "K";
    }

    @Override
    public int pieceValue() {
        return 0;
    }

    @Override
    public Texture getTexture() {
        if (this.getColour() == PieceColour.BLACK) {
            return new Texture("black_king.png");
        } else {
            return new Texture("white_king.png");
        }
    }
}
