package com.mason.mancaca.game;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class plays the game mancala in order to find the best possibles moves.
 * It mainly uses recursive loops to find each possible combinations of moves.
 * 
 * 
 * @author Mason
 *
 */
public class Mancala {

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

	public static void main(String[] args) {

		Display display = new Display();

		System.arraycopy(MARBLES_HALF, 0, MARBLES, 0, 6);
		System.arraycopy(MARBLES_HALF, 0, MARBLES, 7, 6);

		// Prompts for user input
		marbles = setBoard();

		if (SAVE_OUTPUT) {
			try {
				PrintStream fileOut = new PrintStream("./out.txt");
				System.setOut(fileOut);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		List<Board> boardLayer1 = new ArrayList<Board>();
		List<Board> bests = new ArrayList<Board>();

		for (int i = 0; i < 6; i++) {
			boardLayer1.add(new Board(marbles));
		}

		while (true)
			display.render(boardLayer1.get(0));
		// tryMoves(boardLayer1, bests);
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
	private static int[] setBoard() {
		@SuppressWarnings("resource")
		Scanner myObj = new Scanner(System.in);
		System.out.println("New Game?(y/n)");

		int[] allMarbles = new int[14];

		String yesNo = myObj.nextLine();
		if (yesNo.equals("y") || yesNo.equals("Y")) {
			System.out.println("Default marbles?(y/n)");
			String deafault = myObj.nextLine();
			if (deafault.equals("y") || deafault.equals("Y")) {
				return MARBLES;
			} else if (deafault.equals("n") || deafault.equals("N")) {
				int[] marbles = new int[6];
				for (int i = 0; i < 6; i++) {
					System.out.println("Please enter the marbles in pocket " + (i + 1));
					marbles[i] = Integer.parseInt(myObj.nextLine());
				}
				for (int i = 0; i < 6; i++) {
					allMarbles[i] = marbles[i];
					allMarbles[i + 7] = marbles[i];
				}
			}
		} else if (yesNo.equals("n") || yesNo.equals("N")) {
			for (int i = 0; i < 14; i++) {
				System.out.println("Please enter the marbles in pocket " + (i + 1));
				allMarbles[i] = Integer.parseInt(myObj.nextLine());
			}
		} else
			return setBoard();

		return allMarbles;
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
