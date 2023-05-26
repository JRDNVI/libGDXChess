package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Chessboard.ChessBoard;
import com.mygdx.game.Chessboard.DamageSquare;
import com.mygdx.game.Chessboard.Move;
import com.mygdx.game.Pieces.King;
import com.mygdx.game.Pieces.Piece;
import com.mygdx.game.Pieces.PieceColour;
import com.mygdx.game.Utities.ChessNotationConverter;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private Texture background;

    private ChessBoard chessBoard;
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
        batch.draw(background, 0, 0, 1000, 1000);
        batch.draw(img, 0, 0, 500, 500);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = chessBoard.getPiece(row, col);
                if (piece != null) {
                    batch.draw(piece.getTexture(), col * 62.5f + 6.25f, row * 62.5f + 6.25f, 50.5f, 50.5f);
                }
            }

            font.draw(batch, "Played Moves:", 510, 490);
            List<String> moves = new ArrayList<>();
            for (Piece piece : capturedPieces) {
                String move = piece.shortString();
                moves.add(move);
            }

            for (int i = 0; i < capturedPieces.size(); i++) {
                for (String move : moves) {
                    font.draw(batch, move, 510, 490 - 20);
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
            selectedRow = row;
            selectedCol = col;
            touchOffset.set(touchX - col * 62.5f, touchY - row * 62.5f);
        }
    }

    private void moveSelectedPiece(int row, int col) {
        Piece selectedPiece = chessBoard.getPiece(selectedRow, selectedCol);
        Piece pieceCaptured = chessBoard.getPiece(row, col);
        System.out.println(selectedPiece);

        if ((whitesTurn && selectedPiece.getColour() == PieceColour.WHITE) || (!whitesTurn && selectedPiece.getColour() == PieceColour.BLACK)) {
            if (selectedPiece.isValidMove(selectedRow, selectedCol, row, col, chessBoard)) {
                if (!isStillInCheck(row, col)) {
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
                    chessBoard.findValidMovesAndDamageSquares(chessBoard);
                    check = isKingInCheck(kingLocations, chessBoard);
                    if (check && isCheckmate()) {
                        System.out.println("checkmate");
                    }
                    System.out.println(chessNotationMoveList);
                    whitesTurn = !whitesTurn;
                } else {
                    System.out.println("Invalid move: King in check");
                }
            }
        }

        selectedRow = -1;
        selectedCol = -1;
    }

    public boolean isKingInCheck(List<Integer> kingLocations, ChessBoard board) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPiece(x, y);
                if (piece != null) {
                    if (piece.getDamageSquares().contains(new DamageSquare(kingLocations.get(0), kingLocations.get(1))) || piece.getValidMoveList().contains(new Move(kingLocations.get(0), kingLocations.get(1), -1, -1))) {
                        System.out.println("White King is in check");
                        whiteInCheck = true;
                        return true;
                    } else if (piece.getDamageSquares().contains(new DamageSquare(kingLocations.get(2), kingLocations.get(3))) || piece.getValidMoveList().contains(new Move(kingLocations.get(2), -1, kingLocations.get(3), -1))) {
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

    private boolean isCheckmate() {
        ChessBoard testCheckmateBoard = chessBoard.copyBoard(chessBoard);
        testCheckmateBoard.findValidMovesAndDamageSquares(testCheckmateBoard);
        findKings(testCheckmateBoard);
        List<Piece> piecesToMove = new ArrayList<>();
        int numberOfMovesNotInCheck = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = chessBoard.getPiece(row, col);
                if (piece != null && piece.getColour() == (whiteInCheck ? PieceColour.WHITE : PieceColour.BLACK) && piece.getValidMoveList().size() != 0) {
                    piecesToMove.add(piece);
                }
            }
        }
        for (Piece piece : piecesToMove) {
            if (piece.getValidMoveList().size() != 0 && piece.getColour() == PieceColour.WHITE) {
                List<Move> piecesValidMoves = piece.getValidMoveList();
                for (Move move : piecesValidMoves) {
                    testCheckmateBoard = testCheckmateBoard.makeMove(testCheckmateBoard, move.getRow(), move.getCol(), move.getPrevRow(), move.getPrevCol());
                    findKings(testCheckmateBoard);

                    testCheckmateBoard.currentWorldState(testCheckmateBoard);
                    if (!isKingInCheck(kingLocations, testCheckmateBoard)) {
                        numberOfMovesNotInCheck++;
                    }
                    testCheckmateBoard = testCheckmateBoard.makeMove(testCheckmateBoard, move.getPrevRow(), move.getPrevCol(), move.getRow(), move.getCol());
                }
            }
        }
        return numberOfMovesNotInCheck == 0;
    }

    public boolean isStillInCheck(int newRow, int newCol) {
        if (check) {
            ChessBoard boardNextMove = chessBoard.copyBoard(chessBoard);
            boardNextMove = boardNextMove.makeMove(boardNextMove, newRow, newCol, selectedRow, selectedCol);
            boardNextMove.currentWorldState(boardNextMove);
            findKings(boardNextMove);
            return isKingInCheck(kingLocations, boardNextMove);
        }
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


    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
