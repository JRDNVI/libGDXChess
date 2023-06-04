package com.mygdx.game.Utilities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.Chessboard.ChessBoard;
import com.mygdx.game.Chessboard.Move;
import com.mygdx.game.Pieces.Piece;
import com.mygdx.game.Pieces.PieceColour;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class GameInformation {

    public static void displayCapturedPieces(Batch batch, List<Piece> capturedPieces, List<String> moves) {
        int whiteSpacing = 0;
        int whiteSpacingSecondLine = 0;
        int blackSpacingSecondLine = 0;
        int blackSpacing = 0;
        for (Piece piece : capturedPieces) {
            if (piece.getColour() == PieceColour.WHITE) {
                if (whiteSpacing < 176) {
                    batch.draw(piece.getTexture(), 530 + whiteSpacing, 460, 20, 20);
                    whiteSpacing += 22;
                } else {
                    batch.draw(piece.getTexture(), 530 + whiteSpacingSecondLine, 435, 20, 20);
                    whiteSpacingSecondLine += 22;
                }

            } else if (blackSpacing < 176) {
                batch.draw(piece.getTexture(), 530 + blackSpacing, 25, 20, 20);
                blackSpacing += 22;
            } else {
                batch.draw(piece.getTexture(), 530 + blackSpacingSecondLine, 4, 20, 20);
                blackSpacingSecondLine += 22;
            }
            String move = piece.shortString();
            moves.add(move);
        }
    }

    public static void displayPlayedMoves(Batch batch, List<String> moves, BitmapFont font) {
        int space = 0;
        int spaceSecondLine = 0;
        int spaceThirdLine = 0;
        if(!moves.isEmpty()) {
            for(String move : moves) {
                if(space < 224) {
                    font.draw(batch, move , 525 + space, 400);
                    space += 28;
                } else if(spaceSecondLine < 224) {
                    font.draw(batch, move , 525 + spaceSecondLine, 380);
                    spaceSecondLine += 28;
                } else {
                    font.draw(batch, move , 525 + spaceThirdLine, 360);
                    spaceThirdLine += 28;
                }
            }
        }
    }

    public static void visitPrevGamePositions(ChessBoard board) {
        LinkedList<ChessBoard> gameHistory = new LinkedList<>();
        gameHistory.addLast(board);
        gameHistory.getLast().currentWorldState(gameHistory.getLast());

    }

    public static void evaluationBar(Batch batch, int whiteScore, int blackScore) {

    }
}
