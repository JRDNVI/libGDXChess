package com.mygdx.game;

public class ChessBoard {
    private Piece[][] board;

    public ChessBoard() {
        board = new Piece[8][8];
        initialiseBoard();
    }

    private void initialiseBoard() {
        board[0][0] = new Rook(PieceColour.BLACK);
        board[0][1] = new Knight(PieceColour.BLACK);
        board[0][2] = new Bishop(PieceColour.BLACK);
        board[0][3] = new Queen(PieceColour.BLACK);
        board[0][4] = new King(PieceColour.BLACK);
        board[0][5] = new Bishop(PieceColour.BLACK);
        board[0][6] = new Knight(PieceColour.BLACK);
        board[0][7] = new Rook(PieceColour.BLACK);


        board[7][0] = new Rook(PieceColour.WHITE);
        board[7][1] = new Knight(PieceColour.WHITE);
        board[7][2] = new Bishop(PieceColour.WHITE);
        board[7][3] = new Queen(PieceColour.WHITE);
        board[7][4] = new King(PieceColour.WHITE);
        board[7][5] = new Bishop(PieceColour.WHITE);
        board[7][6] = new Knight(PieceColour.WHITE);
        board[7][7] = new Rook(PieceColour.WHITE);

        for (int col = 0; col < 8; col++) {
            board[1][col] = new Pawn(PieceColour.BLACK);
            board[6][col] = new Pawn(PieceColour.WHITE);
        }

        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                String symbol = (piece != null) ? piece.getSymbol() : " ";
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public Piece setPiece(int row, int col, Piece piece) {
        return board[row][col] = piece;
    }


}
