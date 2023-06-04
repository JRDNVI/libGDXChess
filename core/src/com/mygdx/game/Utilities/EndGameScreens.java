package com.mygdx.game.Utilities;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Chessboard.ChessBoard;
import com.mygdx.game.Pieces.Piece;
import com.mygdx.game.Pieces.PieceColour;

import java.util.ArrayList;
import java.util.List;

public class EndGameScreens extends ApplicationAdapter {
    private Stage stage;
    private Group winScreen;
    private Group loseScreen;
    private Group drawScreen;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture background;

    public void create(Stage rootStage) {
        stage = rootStage;
        winScreen = new Group();
        loseScreen = new Group();
        drawScreen = new Group();

        // Create and add actors to each screen
        createWinScreen(stage);
        createLoseScreen();
        createDrawScreen();

        // Add the screens to the stage
        stage.addActor(winScreen);
        stage.addActor(loseScreen);
        stage.addActor(drawScreen);

        // Set the input processor to the stage
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw the stage
        stage.act();
        stage.draw();
    }

    private void createWinScreen(Stage stage) {
        batch = new SpriteBatch();
        font = new BitmapFont();
        background = new Texture("bg.png");

        Label winLabel = new Label("You Win!!", new Label.LabelStyle(font, Color.WHITE));
        winLabel.setPosition(500, 500);
        stage.addActor(winLabel);
    }

    private void createLoseScreen() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        background = new Texture("bg.png");

        Label loseLabel = new Label("You Lost!!", new Label.LabelStyle(font, Color.WHITE));
        loseLabel.setPosition(500, 500);
        loseScreen.addActor(loseLabel);
    }

    private void createDrawScreen() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        background = new Texture("bg.png");

        Label drawLabel = new Label("Draw!", new Label.LabelStyle(font, Color.WHITE));
        drawLabel.setPosition(500, 500);
        drawScreen.addActor(drawLabel);
    }

    // Methods to show/hide different screens

    public void showWinScreen() {
        winScreen.setVisible(true);
        loseScreen.setVisible(false);
        drawScreen.setVisible(false);
    }

    public void showLoseScreen() {
        winScreen.setVisible(false);
        loseScreen.setVisible(true);
        drawScreen.setVisible(false);
    }

    public void showDrawScreen() {
        winScreen.setVisible(false);
        loseScreen.setVisible(false);
        drawScreen.setVisible(true);
    }

    // Other methods and logic for handling the screens

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        font.dispose();
        background.dispose();
    }
}





