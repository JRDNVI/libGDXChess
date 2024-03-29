package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.Chessboard.ChessBoard;

import java.io.PrintStream;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Chess");
		config.setWindowedMode(750, 550);
		//config.enableGLDebugOutput(true, new PrintStream(System.out));
		new Lwjgl3Application(new Main(), config);
		//ChessBoard board = new ChessBoard();
	}
}
