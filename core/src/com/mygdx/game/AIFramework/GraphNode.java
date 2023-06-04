package com.mygdx.game.AIFramework;

import com.mygdx.game.Chessboard.ChessBoard;

public class GraphNode {

    private ChessBoard chessBoard;
    private int whiteOrBlack; //black 0, white 1
    private int nodeValue;

    public GraphNode(ChessBoard chessBoard, int whiteOrBlack, int nodeValue) {
        this.chessBoard = chessBoard;
        this.whiteOrBlack = whiteOrBlack;
        this.nodeValue = nodeValue;
    }


    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public int getWhiteOrBlack() {
        return whiteOrBlack;
    }

    public void setWhiteOrBlack(int whiteOrBlack) {
        this.whiteOrBlack = whiteOrBlack;
    }

    public int getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(int nodeValue) {
        this.nodeValue = nodeValue;
    }

    @Override
    public String toString() {
        return "GraphNode{" +
                "chessBoard=" + chessBoard +
                ", whiteOrBlack=" + whiteOrBlack +
                ", nodeValue=" + nodeValue +
                '}';
    }
}
