package com.mygdx.game.Utities;

import java.util.HashMap;
import java.util.Map;

public class ChessNotationConverter {
    private final Map<Integer, Character> columnToLetter;

    public ChessNotationConverter() {
        columnToLetter = new HashMap<>();
        columnToLetter.put(0, 'a');
        columnToLetter.put(1, 'b');
        columnToLetter.put(2, 'c');
        columnToLetter.put(3, 'd');
        columnToLetter.put(4, 'e');
        columnToLetter.put(5, 'f');
        columnToLetter.put(6, 'g');
        columnToLetter.put(7, 'h');
    }

    public char convertColumnToLetter(int column) {
        return columnToLetter.get(column);
    }
}

