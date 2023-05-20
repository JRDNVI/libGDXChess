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

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private Texture background;
    private ChessBoard chessBoard;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Vector2 touchOffset = new Vector2();
    private boolean whitesTurn = true;
    private boolean turnChange = false;
    private List<Piece> allPiecesOnBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();
    private List<String> chessNotationMoveList = new ArrayList<>();
    private ChessNotationConverter converter = new ChessNotationConverter();
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
                    piece.findValidMovesAndDamageSquares(row, col, chessBoard);
                    batch.draw(
                            piece.getTexture(),
                            col * 62.5f + 6.25f,
                            row * 62.5f + 6.25f,
                            50.5f,
                            50.5f
                    );
                }
            }
            font.draw(batch, "Played Moves:", 510, 490); // Adjust the position as needed

            List<String> moves = new ArrayList<>();
            for(Piece piece : capturedPieces) {
                String move = piece.shortString();
                moves.add(move);
            }
            for(int i = 0; i < capturedPieces.size(); i++) {
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

            // Convert the touch coordinates to board indices
            int col = (int) (touchX / 62.5f);
            int row = (int) (touchY / 62.5f);

            // Check if the touch is within the board boundaries
            if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                // Check if a piece is already selected
                if (selectedRow == -1 && selectedCol == -1) {
                    // Select the piece if it exists
                    Piece selectedPiece = chessBoard.getPiece(row, col);
                    System.out.println(selectedPiece);
                    if (selectedPiece != null) {
                        selectedRow = row;
                        selectedCol = col;
                        touchOffset.set(touchX - col * 62.5f, touchY - row * 62.5f);
                    }
                } else {
                    // Move the selected piece if the target position is valid
                    // Perform the move
                    Piece selectedPiece = chessBoard.getPiece(selectedRow, selectedCol);
                    Piece pieceCaptured = chessBoard.getPiece(row, col);
                    if (whitesTurn) {
                        if (selectedPiece.isValidMove(selectedRow, selectedCol, row, col, chessBoard) && selectedPiece.getColour() == PieceColour.WHITE) {
                            chessBoard.setPiece(row, col, selectedPiece);
                            chessBoard.setPiece(selectedRow, selectedCol, null);
                            if (pieceCaptured != null) {
                                allPiecesOnBoard.remove(pieceCaptured);
                                capturedPieces.add(pieceCaptured);
                                String moveNotation = selectedPiece.getSymbol() + "x" + converter.convertColumnToLetter(col) + row;
                                chessNotationMoveList.add(moveNotation);

                            } else {
                                String moveNotation = selectedPiece.getSymbol() +  converter.convertColumnToLetter(col) + row;
                                chessNotationMoveList.add(moveNotation);

                            }
                            whitesTurn = false;
                        }
                    } else {
                        if (selectedPiece.isValidMove(selectedRow, selectedCol, row, col, chessBoard) && selectedPiece.getColour() == PieceColour.BLACK) {
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
                            whitesTurn = true;
                            System.out.println(chessNotationMoveList);
                        }
                    }
                    selectedRow = -1;
                    selectedCol = -1;
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
