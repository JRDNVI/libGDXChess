package com.mygdx.game;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ChessBoard {
    private final Piece[][] board;
    private static List<Piece> allPiecesOnBoard = new ArrayList<>();

    public ChessBoard() {
        board = new Piece[8][8];
        initialiseBoard();
    }

    private void initialiseBoard() {
        allPiecesOnBoard.add(board[0][0] = new Rook(PieceColour.BLACK));
        allPiecesOnBoard.add(board[0][1] = new Knight(PieceColour.BLACK));
        allPiecesOnBoard.add(board[0][2] = new Bishop(PieceColour.BLACK));
        allPiecesOnBoard.add(board[0][3] = new Queen(PieceColour.BLACK));
        allPiecesOnBoard.add(board[0][4] = new King(PieceColour.BLACK));
        allPiecesOnBoard.add(board[0][5] = new Bishop(PieceColour.BLACK));
        allPiecesOnBoard.add(board[0][6] = new Knight(PieceColour.BLACK));
        allPiecesOnBoard.add(board[0][7] = new Rook(PieceColour.BLACK));


        allPiecesOnBoard.add(board[7][0] = new Rook(PieceColour.WHITE));
        allPiecesOnBoard.add(board[7][1] = new Knight(PieceColour.WHITE));
        allPiecesOnBoard.add(board[7][2] = new Bishop(PieceColour.WHITE));
        allPiecesOnBoard.add(board[7][3] = new Queen(PieceColour.WHITE));
        allPiecesOnBoard.add(board[7][4] = new King(PieceColour.WHITE));
        allPiecesOnBoard.add(board[7][5] = new Bishop(PieceColour.WHITE));
        allPiecesOnBoard.add(board[7][6] = new Knight(PieceColour.WHITE));
        allPiecesOnBoard.add(board[7][7] = new Rook(PieceColour.WHITE));

        for (int col = 0; col < 8; col++) {
            allPiecesOnBoard.add(board[1][col] = new Pawn(PieceColour.BLACK));
            allPiecesOnBoard.add(board[6][col] = new Pawn(PieceColour.WHITE));
        }
    }

    public void currentWorldState(ChessBoard board) {
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                String symbol = (piece != null) ? piece.getSymbol() : " ";
                System.out.print(symbol + " ");
            }
            System.out.println(); // Add a line break after each row
        }
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public Piece setPiece(int row, int col, Piece piece) {
        return board[row][col] = piece;
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public static List<Piece> getAllPiecesOnBoard() {
        return allPiecesOnBoard;
    }

    public static void setAllPiecesOnBoard(List<Piece> allPiecesOnBoard) {
        ChessBoard.allPiecesOnBoard = allPiecesOnBoard;
    }
}
