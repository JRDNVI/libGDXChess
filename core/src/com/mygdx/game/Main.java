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
import com.mygdx.game.Pieces.*;
import com.mygdx.game.Utilities.ChessNotationConverter;

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
            canPlayerCastle(chessBoard, row, col);
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
                    pawnPromotion(chessBoard);
                    whitesTurn = !whitesTurn;
                    if (isStalemate(chessBoard)) {
                        System.out.println("Stalemate");
                    } else {
                        System.out.println("Not Stalemate");
                    }
                    if (check && isCheckmate()) {
                        System.out.println("checkmate");
                    } else {
                        System.out.println("Not Checkmate");
                    }
                    System.out.println(chessNotationMoveList);

                } else {
                    System.out.println("Invalid move: King in check");
                }
            }
        }

        selectedRow = -1;
        selectedCol = -1;
    }

    // ------------------------ Logic for game win condition (look for Check, if in checked ensures the next move will prevent check, looks for checkmate) --------------------

    // Step 1 - findkings() locates both kings which provides a List which can be used to check for a win condition or check.

    // Step 2 - isKingInCheck() after every move this method is called and checks if the king locations (obtained in findKings()) are in a damageSquare.

    // Step 3 - isStillInCheck() ensures the next move removes check (simulates the wanted move on a copied chessboard and looks for check).

    // Step 4 - isCheckmate() this creates a list of pieces (colour of which is determined by which colour king is in check) and then loops through every piece's validMoveList which
    //          contains every possible move by the player in check on a copiedChessboard and checks if the king is in check. If there is at least one move that prevents check then
    //          it's not checkmate.

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


    private boolean isStalemate(ChessBoard board) {
        List<Piece> whitePieces = new ArrayList<>();
        List<Piece> blackPieces = new ArrayList<>();
        List<Move> piecesValidMoves;

        int piecesWithNoValidMove = 0;
        int kingsMovesInCheck = 0;
        int kingsValidMovesSize = 0;
        int numberOfPieces = 0;

        ChessBoard testCheckmateBoard = chessBoard.copyBoard(chessBoard);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColour() == PieceColour.WHITE) {
                    whitePieces.add(piece);
                } else if(piece != null && piece.getColour() == PieceColour.BLACK) {
                    blackPieces.add(piece);
                }
            }
        }

        if(!whitesTurn) {
            numberOfPieces = whitePieces.size();
            for (Piece piece : whitePieces) {
                if (!(piece instanceof King)) {
                    if (piece.getValidMoveList().isEmpty()) {
                        piecesWithNoValidMove++;
                    }
                } else {
                    piecesValidMoves = piece.getValidMoveList();
                    kingsValidMovesSize = piecesValidMoves.size();
                    int validMovesCount = kingsValidMovesSize; // Store the size of piecesValidMoves //TODO I can change int kingValidMoveCount in the for loop to kingsValidMoveSize
                    for (int i = 0; i < validMovesCount; i++) {
                        Move move = piecesValidMoves.get(i); // Access the moves through piecesValidMoves
                        testCheckmateBoard = testCheckmateBoard.makeMove(testCheckmateBoard, move.getRow(), move.getCol(), move.getPrevRow(), move.getPrevCol());
                        findKings(testCheckmateBoard);
                        testCheckmateBoard.currentWorldState(testCheckmateBoard);
                        if (isKingInCheck(kingLocations, testCheckmateBoard)) {
                            kingsMovesInCheck++;
                        }
                        testCheckmateBoard = testCheckmateBoard.makeMove(testCheckmateBoard, move.getPrevRow(), move.getPrevCol(), move.getRow(), move.getCol());
                    }
                }
            }
        } else {
            numberOfPieces = blackPieces.size();
            for (Piece piece : blackPieces) {
                if (!(piece instanceof King)) {
                    if (piece.getValidMoveList().isEmpty()) {
                        piecesWithNoValidMove++;
                    }
                } else {
                    piecesValidMoves = piece.getValidMoveList();
                    kingsValidMovesSize = piecesValidMoves.size();
                    int validMovesCount = kingsValidMovesSize; // Store the size of piecesValidMoves //TODO I can change int kingValidMoveCount in the for loop to kingsValidMoveSize
                    for (int i = 0; i < validMovesCount; i++) {
                        Move move = piecesValidMoves.get(i); // Access the moves through piecesValidMoves
                        testCheckmateBoard = testCheckmateBoard.makeMove(testCheckmateBoard, move.getRow(), move.getCol(), move.getPrevRow(), move.getPrevCol());
                        findKings(testCheckmateBoard);
                        testCheckmateBoard.currentWorldState(testCheckmateBoard);
                        if (isKingInCheck(kingLocations, testCheckmateBoard)) {
                            kingsMovesInCheck++;
                        }
                        testCheckmateBoard = testCheckmateBoard.makeMove(testCheckmateBoard, move.getPrevRow(), move.getPrevCol(), move.getRow(), move.getCol());
                    }
                }
            }
        }
        System.out.println("Number Of kings valid move: " + kingsValidMovesSize + ", " + "Number of kings moves in check: " + kingsMovesInCheck
                + "\n" + "number of pieces: " + numberOfPieces + ", " + "number of pieces with no validMoves: " + piecesWithNoValidMove);
        return kingsMovesInCheck == kingsValidMovesSize && numberOfPieces - 1 == piecesWithNoValidMove;
    }

    // ------------------------- Logic for games rules; Castling and pawn promotion ----------------------------

    public boolean pawnPromotion(ChessBoard board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof Pawn) {
                    if (piece.getColour() == PieceColour.WHITE) {
                        if (row == 0) {
                            Queen queen = new Queen(PieceColour.WHITE);
                            board.setPiece(row, col, null);
                            board.setPiece(row, col, queen);
                            return true;
                        }
                    } else {
                        if (row == 7) {
                            Queen queen = new Queen(PieceColour.BLACK);
                            board.setPiece(row, col, null);
                            board.setPiece(row, col, queen);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean canPlayerCastle(ChessBoard board, int destRow, int destCol) { //TODO this method needs to be simplified
        int whiteKingRow = 7;
        int whiteKingCol = 4;
        int whiteRookLeftRow = 7;
        int whiteRookLeftCol = 0;
        int whiteRookRightRow = 7;
        int whiteRookRightCol = 7;

        int blackKingRow = 0;
        int blackKingCol = 4;
        int blackRookLeftRow = 0;
        int blackRookLeftCol = 0;
        int blackRookRightRow = 0;
        int blackRookRightCol = 7;

        Piece whiteKing = board.getPiece(whiteKingRow, whiteKingCol);
        Piece blackKing = board.getPiece(blackKingRow, blackKingCol);

        if (whiteKing instanceof King) {
            // White left side castling
            if (destRow == whiteKingRow && destCol == 2) {
                Piece whiteRookLeft = board.getPiece(whiteRookLeftRow, whiteRookLeftCol);
                if (whiteRookLeft instanceof Rook) {
                    int newKingCol = 2;
                    int newRookCol = 3;
                    if (performCastle(board, whiteKingRow, whiteKingCol, whiteKingRow, newKingCol, whiteRookLeftRow, whiteRookLeftCol, whiteRookLeftRow, newRookCol)) {
                        whitesTurn = !whitesTurn;
                        return true;
                    }
                    return false;
                }
            }
            // White right side castling
            else if (destRow == whiteKingRow && destCol == 6) {
                Piece whiteRookRight = board.getPiece(whiteRookRightRow, whiteRookRightCol);
                if (whiteRookRight instanceof Rook) {
                    int newKingCol = 6;
                    int newRookCol = 5;
                    if (performCastle(board, whiteKingRow, whiteKingCol, whiteKingRow, newKingCol, whiteRookRightRow, whiteRookRightCol, whiteRookRightRow, newRookCol)) {
                        whitesTurn = !whitesTurn;
                        return true;
                    }
                    return false;
                }
            }
        }

        if (blackKing instanceof King) {
            // Black left side castling
            if (destRow == blackKingRow && destCol == 2) {
                Piece blackRookLeft = board.getPiece(blackRookLeftRow, blackRookLeftCol);
                if (blackRookLeft instanceof Rook) {
                    int newKingCol = 2;
                    int newRookCol = 3;
                    if (performCastle(board, blackKingRow, blackKingCol, blackKingRow, newKingCol, blackRookLeftRow, blackRookLeftCol, blackRookLeftRow, newRookCol)) {
                        whitesTurn = true;
                        return true;
                    }
                    return false;
                }
            }
            // Black right side castling
            else if (destRow == blackKingRow && destCol == 6) {
                Piece blackRookRight = board.getPiece(blackRookRightRow, blackRookRightCol);
                if (blackRookRight instanceof Rook) {
                    int newKingCol = 6;
                    int newRookCol = 5;
                    if (performCastle(board, blackKingRow, blackKingCol, blackKingRow, newKingCol, blackRookRightRow, blackRookRightCol, blackRookRightRow, newRookCol)) {
                        whitesTurn = true;
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }


    private boolean performCastle(ChessBoard board, int kingRow, int kingCol, int newKingRow, int newKingCol,
                                  int rookRow, int rookCol, int newRookRow, int newRookCol) {
        Piece king = board.getPiece(kingRow, kingCol);
        Piece rook = board.getPiece(rookRow, rookCol);

        // Check if the squares about to be set as null are empty
        if (board.getPiece(newKingRow, newKingCol) == null && board.getPiece(newRookRow, newRookCol) == null) {
            board.setPiece(kingRow, kingCol, null);
            board.setPiece(rookRow, rookCol, null);
            board.setPiece(newKingRow, newKingCol, king);
            board.setPiece(newRookRow, newRookCol, rook);
            return true;
        } else {
            System.out.println("Nope");
            return false;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
