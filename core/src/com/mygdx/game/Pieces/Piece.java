package com.mygdx.game.Pieces;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Chessboard.ChessBoard;
import com.mygdx.game.Chessboard.DamageSquare;
import com.mygdx.game.Chessboard.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Piece {
    private PieceColour colour;
    private List<DamageSquare> damageSquares = new ArrayList<>();
    private List<Move> validMoveList = new ArrayList<>();

    public Piece(PieceColour colour) {
        this.colour = colour;
    }


    public void findValidMovesAndDamageSquares(int sourceRow, int sourceCol, ChessBoard board) {
        Piece startPiece = board.getPiece(sourceRow, sourceCol);
        startPiece.getValidMoveList().clear(); // Clear the previous list of valid moves
        startPiece.getDamageSquares().clear();

        for (int destRow = 0; destRow < 8; destRow++) {
            for (int destCol = 0; destCol < 8; destCol++) {
                if (sourceRow == destRow && sourceCol == destCol) {
                    continue;
                }
                Piece destPiece = board.getPiece(destRow, destCol);
                Move move = new Move(sourceRow, sourceCol, destRow, destCol);
                DamageSquare damageSquare = new DamageSquare(destRow, destCol);
                if (startPiece.isValidMove(sourceRow, sourceCol, destRow, destCol, board)) {
                    if(destPiece == null) {
                        startPiece.getValidMoveList().add(move);
                    } else  {
                        startPiece.getDamageSquares().add(damageSquare);
                    }
                }
            }
        }
    }

    public PieceColour getColour() {
        return colour;
    }

    public void setColour(PieceColour colour) {
        this.colour = colour;
    }

    public List<DamageSquare> getDamageSquares() {
        return damageSquares;
    }

    public void setDamageSquares(List<DamageSquare> damageSquares) {
        this.damageSquares = damageSquares;
    }

    public List<Move> getValidMoveList() {
        return validMoveList;
    }

    public void setValidMoveList(List<Move> validMoveList) {
        this.validMoveList = validMoveList;
    }

    public abstract boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, ChessBoard board);

    public abstract String getSymbol();

    public abstract int pieceValue();

    public abstract Texture getTexture();

    @Override
    public String toString() {
        return  "\n" + "Piece: " + getSymbol() +
                ", Colour: " + colour +
                ", Value: " + pieceValue() + "\n" +
                "DamageSquares: " + damageSquares + "\n" +
                "ValidMoveList: " + validMoveList + "\n" +
                "------------------------------------------";

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Piece other = (Piece) obj;
        return Objects.equals(this.getTexture(), other.getTexture()) &&
                Objects.equals(this.getColour(), other.getColour());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTexture(), getColour());
    }

    public String shortString() {
        return "Piece: " + getSymbol() +
                ", Colour: " + colour +
                ", Value: " + pieceValue();

    }
}
