package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private Texture background;

    private ChessBoard chessBoard;
    private ChessBoard boardCurrentPosition;
    private int selectedRow = -1;
    private int selectedCol = -1;

    private final Vector2 touchOffset = new Vector2();

    private boolean whitesTurn = true;
    private boolean check = false;
    private boolean blackInCheck = false;
    private boolean whiteInCheck = false;

    private List<Piece> allPiecesOnBoard = new ArrayList<>();
    private final List<Piece> capturedPieces = new ArrayList<>();
    private final List<String> chessNotationMoveList = new ArrayList<>();
    private final List<Integer> kingLocations = new ArrayList<>();
    private final ChessNotationConverter converter = new ChessNotationConverter();

    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("board.png");
        background = new Texture("bg.png");
        chessBoard = new ChessBoard();
        allPiecesOnBoard = ChessBoard.getAllPiecesOnBoard();
        font = new BitmapFont();
    }

    @Override
    public void render() {
        handleInput();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.draw(background, 0, 0, 1000, 1000); // Adjust the coordinates and size as needed

        // Draw the chessboard
        batch.draw(img, 0, 0, 500, 500);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = chessBoard.getPiece(row, col);
                if (piece != null) {
                    batch.draw(piece.getTexture(), col * 62.5f + 6.25f, row * 62.5f + 6.25f, 50.5f, 50.5f);
                }
            }
            font.draw(batch, "Played Moves:", 510, 490); // Adjust the position as needed

            List<String> moves = new ArrayList<>();
            for (Piece piece : capturedPieces) {
                String move = piece.shortString();
                moves.add(move);
            }
            for (int i = 0; i < capturedPieces.size(); i++) {
                for (String move : moves) {
                    font.draw(batch, move, 510, 490 - 20); // Adjust the position as needed
                }
            }

        }
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            int col = (int) (touchX / 62.5f);
            int row = (int) (touchY / 62.5f);

            if (chessBoard.isValidPosition(row, col)) {
                if (selectedRow == -1 && selectedCol == -1) {
                    selectPiece(row, col, touchX, touchY);
                } else {
                    moveSelectedPiece(row, col);
                }
            }
        }
    }

    private void selectPiece(int row, int col, float touchX, float touchY) {
        Piece selectedPiece = chessBoard.getPiece(row, col);
        if (selectedPiece != null) {
//            if(!check) {
//                selectedRow = row;
//                selectedCol = col;
//                touchOffset.set(touchX - col * 62.5f, touchY - row * 62.5f);
//            } else if(selectedPiece instanceof King) {
            selectedRow = row;
            selectedCol = col;
            touchOffset.set(touchX - col * 62.5f, touchY - row * 62.5f);
            // }
        }
    }

    private void moveSelectedPiece(int row, int col) {
        Piece selectedPiece = chessBoard.getPiece(selectedRow, selectedCol);
        Piece pieceCaptured = chessBoard.getPiece(row, col);
        System.out.println(selectedPiece);

        if ((whitesTurn && selectedPiece.getColour() == PieceColour.WHITE) || (!whitesTurn && selectedPiece.getColour() == PieceColour.BLACK)) {
            if (selectedPiece.isValidMove(selectedRow, selectedCol, row, col, chessBoard)) {
                if (!isStillInCheck(row, col, selectedRow, selectedCol)) {
                    chessBoard.setPiece(row, col, selectedPiece);
                    chessBoard.setPiece(selectedRow, selectedCol, null);

                    if (pieceCaptured != null) {
                        allPiecesOnBoard.remove(pieceCaptured);
                        capturedPieces.add(pieceCaptured);
                        String moveNotation = selectedPiece.getSymbol() + "x" + converter.convertColumnToLetter(col) + row;
                        chessNotationMoveList.add(moveNotation);
                    } else {
                        String moveNotation = selectedPiece.getSymbol() + converter.convertColumnToLetter(col) + row;
                        chessNotationMoveList.add(moveNotation);
                    }

                    findKings(chessBoard);
                    findValidMovesAndDamageSquares(chessBoard);
                    check = isKingInCheck(kingLocations, chessBoard);
                    System.out.println(chessNotationMoveList);
                    whitesTurn = !whitesTurn;

                    boardCurrentPosition = chessBoard.copyBoard(chessBoard);
                } else {
                    System.out.println("Invalid move: King in check");
                }
            }
        }

        selectedRow = -1;
        selectedCol = -1;
    }

    public boolean isStillInCheck(int newRow, int newCol, int prevRow, int prevCol) {
        if (check) {
            if (whiteInCheck) {
                ChessBoard boardNextMove = chessBoard.copyBoard(chessBoard);
                findKings(boardNextMove);
                DamageSquare damageSquare = new DamageSquare(newRow, newCol);
                Piece whiteKing = boardNextMove.getPiece(kingLocations.get(0), kingLocations.get(1));
                boardNextMove.setPiece(newRow, newCol, whiteKing);
                boardNextMove.setPiece(prevRow, prevCol, null);
                findValidMovesAndDamageSquares(boardNextMove);
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        Piece piece = boardNextMove.getPiece(x, y);
                        if (piece != null) {
                            if (piece.getDamageSquares().contains(damageSquare)) {
                                return true;
                            }
                        }
                    }
                }
                // boardNextMove.setPiece(newRow, newCol);
                boardNextMove.currentWorldState(boardNextMove);
            }
        }
        return false;
    }

//    public boolean validMoveAfterCheck(int row, int col, int selectedRow, int selectedCol) {
//        if (check) {
//            if (whiteInCheck) {
//                int whiteKingCol = kingLocations.get(0);
//                int whiteKingRow = kingLocations.get(1);
//                Piece whiteKing = chessBoard.getPiece(whiteKingCol, whiteKingRow);
//
//                // Create a copy of the chess board
//                ChessBoard copiedBoard = chessBoard.copyBoard(chessBoard);
//                // Set the intended move in the copied board
//                copiedBoard.setPiece(row, col, whiteKing);
//                copiedBoard.setPiece(selectedRow, selectedCol, null);
//                copiedBoard.currentWorldState(boardCurrentPosition);
//                findKings(boardCurrentPosition);
//                findValidMovesAndDamageSquares(boardCurrentPosition);
//
//
//
//                // Check if the king is still in check on the copied board
//                boolean stillInCheck = isKingInCheck(kingLocations, boardCurrentPosition);
//                System.out.println(stillInCheck);
//                // The move is not valid
//                if (stillInCheck) {
//                    return false;
//                }
//            } else if (blackInCheck) {
//                return true;
//            }
//        }
//        return true;
//    }

    public boolean isKingInCheck(List<Integer> kingLocations, ChessBoard board) {
        DamageSquare whiteKingSquare = new DamageSquare(kingLocations.get(0), kingLocations.get(1));
        DamageSquare blackKingSquare = new DamageSquare(kingLocations.get(2), kingLocations.get(3));

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPiece(x, y);
                if (piece != null) {
                    if (piece.getDamageSquares().contains(whiteKingSquare)) {
                        System.out.println("White King is in check");
                        whiteInCheck = true;
                        return true;
                    } else if (piece.getDamageSquares().contains(blackKingSquare)) {
                        System.out.println("Black king is in Check");
                        blackInCheck = true;
                        return true;
                    }
                }
            }
        }
        System.out.println("King not in check");
        return false;

    }

    private void findKings(ChessBoard chessBoard) {
        int whiteKingRow = -1;
        int whiteKingCol = -1;
        int blackKingRow = -1;
        int blackKingCol = -1;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = chessBoard.getPiece(row, col);
                if (piece instanceof King) {
                    if (piece.getColour() == PieceColour.WHITE) {
                        whiteKingRow = row;
                        whiteKingCol = col;
                    } else if (piece.getColour() == PieceColour.BLACK) {
                        blackKingRow = row;
                        blackKingCol = col;
                    }
                }
            }
        }

        kingLocations.clear();
        kingLocations.add(whiteKingRow);
        kingLocations.add(whiteKingCol);
        kingLocations.add(blackKingRow);
        kingLocations.add(blackKingCol);
        System.out.println(kingLocations);
    }

    private void findValidMovesAndDamageSquares(ChessBoard board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    piece.findValidMovesAndDamageSquares(row, col, board);
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
