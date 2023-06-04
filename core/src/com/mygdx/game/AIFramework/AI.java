package com.mygdx.game.AIFramework;

import com.mygdx.game.Chessboard.ChessBoard;
import com.mygdx.game.Chessboard.DamageSquare;
import com.mygdx.game.Chessboard.Move;
import com.mygdx.game.Pieces.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class AI {
    private boolean AIColour;

    public List<Move> generatePossibleMoves(ChessBoard board) {
        List<Move> possibleMoves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);

                if (piece != null && piece.getColour() == PieceColour.BLACK) {
                    List<Move> validMoves = piece.getValidMoveList();

                    for (Move move : validMoves) {
                        if (piece.isValidMove( move.getPrevRow(), move.getPrevCol(), move.getRow(), move.getCol(), board)) {
                            possibleMoves.add(move);
                        }
                    }
                }
            }
        }
        System.out.println(possibleMoves);
        return possibleMoves;
    }

    public Move evaluateMove(ChessBoard board, int depth) {
        List<Move> possibleMoves = generatePossibleMoves(board);
        List<Move> prioritizedMoves = new ArrayList<>();

        for (Move move : possibleMoves) {
            if (move.isPieceCaptured()) {
                Piece selectedPiece = board.getPiece(move.getPrevRow(), move.getPrevCol());
                Piece capturedPiece = board.getPiece(move.getRow(), move.getCol());
                if (selectedPiece.pieceValue() <= capturedPiece.pieceValue()) {
                    prioritizedMoves.add(0, move);
                }
            } else {
                prioritizedMoves.add(move);
            }
        }

        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : prioritizedMoves) {
            ChessBoard copyBoard = board.copyBoard(board);
            copyBoard.makeMove(copyBoard, move.getPrevRow(), move.getPrevCol(), move.getRow(), move.getCol());

            int score = minimax(copyBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    public int minimax(ChessBoard board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0) {
            return board.evaluatePosition(board);
        }

        List<Move> possibleMoves = generatePossibleMoves(board);

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;

            for (Move move : possibleMoves) {
                ChessBoard copyBoard = board.copyBoard(board);
                copyBoard.makeMove(copyBoard, move.getRow(), move.getCol(), move.getRow(), move.getCol());

                int eval = minimax(copyBoard, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                if (beta <= alpha) {
                    break;
                }
            }

            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;

            for (Move move : possibleMoves) {
                ChessBoard copyBoard = board.copyBoard(board);
                copyBoard.makeMove(copyBoard, move.getRow(), move.getCol(), move.getRow(), move.getCol());

                int eval = minimax(copyBoard, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                if (beta <= alpha) {
                    break;
                }
            }

            return minEval;
        }
    }

    public void bestMove(ChessBoard chessBoard, List<Piece> blacksMoves) {

    }

//    public List<Move> allPossibleMoves(ChessBoard board, int playerToMove) {
//        for (int row = 0; row < 8; row++) {
//            for (int col = 0; col < 8; col++) {
//                Piece piece = board.getPiece(row, col);
//                if (piece.getColour() == PieceColour.BLACK) {
//                    if(!possibleMovesForPieces.containsKey(piece.getSymbol())) {
//                        possibleMovesForPieces.put(piece.getSymbol(), piece.getValidMoveList())
//                    }
//                }
//            }
//        }
//        return null;
//    }

    public void evaluateMove() {

    }
}
