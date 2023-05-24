package com.mygdx.game.Pieces;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Chessboard.ChessBoard;

public class Knight extends Piece {

    public Knight(PieceColour colour) {
        super(colour);
    }

    @Override
    public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board) {
        int rowOffset = Math.abs(destRow - sourceRow);
        int colOffset = Math.abs(destCol - sourceCol);

        // Check if the move follows the L-shape pattern of a knight
        if ((rowOffset == 2 && colOffset == 1) || (rowOffset == 1 && colOffset == 2)) {
            Piece destinationPiece = board.getPiece(destRow, destCol);
            // Check if the destination is not occupied by a piece of the same color
            return destinationPiece == null || destinationPiece.getColour() != this.getColour();
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return "N";
    }

    @Override
    public int pieceValue() {
        return 3;
    }


    @Override
    public Texture getTexture() {
        if (this.getColour() == PieceColour.BLACK) {
            return new Texture("black_knight.png");
        } else {
            return new Texture("white_knight.png");
        }
    }
}
