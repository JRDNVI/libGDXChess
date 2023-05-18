package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private ChessBoard chessBoard;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Vector2 touchOffset = new Vector2();
    private boolean whitesTurn = true;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("board.png");
        chessBoard = new ChessBoard();
    }

    @Override
    public void render() {
        handleInput();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(img, 0, 0, 500, 500);

        // Render the pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = chessBoard.getPiece(row, col);
                if (piece != null) {
                    batch.draw(piece.getTexture(), col * 64.5f, row * 62.5f, 50.5f, 50.5f);
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
            int row = (int) (touchY / 62.5f);
            int col = (int) (touchX / 64.5f);

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
                    if (selectedPiece.isValidMove(selectedRow, selectedCol, row, col, chessBoard)) {
                        chessBoard.setPiece(row, col, selectedPiece);
                        chessBoard.setPiece(selectedRow, selectedCol, null);
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
