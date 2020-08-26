package com.mason.mancala.game;

import java.awt.Canvas;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import com.mason.mancala.input.InputHandler;
import com.mason.mancala.input.InputProcesser;

/**
 * This class plays the game mancala in order to find the best possibles moves.
 * It mainly uses recursive loops to find each possible combinations of moves.
 * 
 * 
 * @author Mason
 *
 */
public class Mancala implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4125969329660533619L;

	/**
	 * Half of the default array of marbles. Duplicated to make
	 * {@link Mancala#MARBLES}.
	 * 
	 */
	public static final int[] MARBLES_HALF = new int[] { 4, 4, 4, 4, 4, 4, 0 };

	/**
	 * The default array of marbles in each pocket. To be used if the user chooses
	 * default marbles.
	 * 
	 * @see Mancala#setBoard()
	 * @see Mancala#MARBLES_HALF
	 */
	public static final int[] MARBLES = new int[14];

	public static int[] marbles = new int[14];

//	/**
//	 * The first layer of possible moves on the <code>Board</code>.
//	 * 
//	 * @see Board
//	 * @see Mancala#indexLayer1
//	 */
//	static List<Board> boardLayer1 = new ArrayList<Board>();
	/**
	 * // * The second layer of possible moves on the <code>Board</code>. // * //
	 * * @see Board // * @see Mancala#indexLayer2 //
	 */
//	static List<Board> boardLayer2 = new ArrayList<Board>();

	/**
	 * The index of {@link Mancala#boardLayer1}.
	 */
	static int indexLayer1 = 0;
	/**
	 * The index of {@link Mancala#boardLayer2}.
	 */
	static int indexLayer2 = 0;

	/**
	 * If the player has won.
	 */
	static boolean win = false;

	/**
	 * Whether or not to save the console output.
	 */
	private static final boolean SAVE_OUTPUT = false;

	static int gc = 0;

	static int saveNum = 0;

	static final int SHEET_MAX_LENGTH = 100000;
	static final int SHEET_NUMBER = 3;

	private Thread thread;

	private boolean running = false;

	private Display window;

	public static boolean[] playSlots = new boolean[6];

	public static boolean[] y_n = new boolean[2];

	static final double FPS = 15.0;

	public Mancala() {
		window = new Display();
	}

	public static void main(String[] args) {

		Mancala mancala = new Mancala();
		System.arraycopy(MARBLES_HALF, 0, MARBLES, 0, 6);
		System.arraycopy(MARBLES_HALF, 0, MARBLES, 7, 6);

		if (SAVE_OUTPUT) {
			try {
				PrintStream fileOut = new PrintStream("./out.txt");
				System.setOut(fileOut);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		mancala.start();

	}

	private void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	private void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void run() {
		marbles = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		Board board = new Board(marbles);
		double coolDown = 0.5;
		long downTime = 0;
		long previousTime = System.nanoTime();
		double frameTime = 1 / FPS;
		while (running) {
			long currentTime = System.nanoTime();
			previousTime = currentTime;
			long passedTime = currentTime - previousTime;
			double passedSeconds = passedTime / 1000000000.0;
			while (passedSeconds < frameTime) {
				if ((currentTime - downTime) / 1000000000.0 > coolDown)
					if (tick(board)) {
						downTime = currentTime;
						break;
					}
				currentTime = System.nanoTime();
				passedTime = currentTime - previousTime;
				passedSeconds = passedTime / 1000000000.0;
			}
			window.render(board);
		}
		stop();
	}

	private boolean tick(Board board) {
		window.process();
		board.prompt = !board.playing;
		if (board.prompt) {
			return setBoard(board);
		} else {
			return move(board);
		}
	}

	private boolean move(Board board) {
		for (int i = 0; i < playSlots.length; i++) {
			if (board.playerMove) {
				if (playSlots[i] && board.marbles[i] > 0) {
					board.playPlayer(i);
					return true;
				}
			} else {
				if (playSlots[i] && board.marbles[i + 7] > 0) {
					board.playOpponent(i + 7);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Tries all the possible moves on boards.
	 * 
	 * @param boards
	 */
	public static void tryMoves(List<Board> boards, List<Board> bests) {
		bests.add(Board.findBest(boards));
		Board.findBest(bests).printBoard();
	}

	private static String getMarbleLayout(int[] marbles) {
		String marbleString = "";
		for (int i = 0; i < marbles.length; i++) {
			marbleString = marbleString + marbles[i];
			if (i + 1 < marbles.length)
				marbleString = marbleString + ".";
		}
		return marbleString;
	}

	/**
	 * Creates the marbles to be used in the construction of {@link Board}.
	 * 
	 * @return An array of marbles
	 * @see Mancala#marbles
	 * @see Mancala#MARBLES
	 * @see Mancala#MARBLES_HALF
	 */
	private static boolean setBoard(Board board) {
		if (board.text == "") {
			board.text = "Your Move?(y/n)";
			return false;
		}
		if (y_n[0] && !y_n[1] && board.text == "Your Move?(y/n)") {
			board.playerMove = true;
			board.text = "Default Board?(y/n)";
			return true;
		} else if (!y_n[0] && y_n[1] && board.text == "Your Move?(y/n)") {
			board.playerMove = false;
			board.text = "Default Board?(y/n)";
			return true;
		}
		if (y_n[0] && !y_n[1] && board.text == "Default Board?(y/n)") {
			System.arraycopy(MARBLES, 0, board.marbles, 0, 14);
			board.text = "Play!";
			board.playing = true;
			return true;
		}

		return false;
	}

	/**
	 * @param board Board to be read.
	 * @return An object of different aspects of board.
	 */
	private static Object[] boardText(Board board) {
		return new Object[] { board.getMovesString(), board.marbles[6], board.getMarblesString(),
				Integer.toString(board.currentMove) };
	}
}
