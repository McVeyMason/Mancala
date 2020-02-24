package com.mason.mancala.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
	public static final int[] MARBLES_HALF = new int[] { 6, 6, 6, 6, 6, 6, 0 };

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
	 * Whether or not to delete entries if trees will be explored.
	 */
	private static final boolean DELETE_AFTER_USE = true;

	/**
	 * Whether or not to save the console output.
	 */
	private static final boolean SAVE_OUTPUT = false;

	static int gc = 0;

	static int saveNum = 0;

	static final int SHEET_MAX_LENGTH = 100000;
	static final int SHEET_NUMBER = 3;

	public static void main(String[] args) {

		System.arraycopy(MARBLES_HALF, 0, MARBLES, 0, 6);
		System.arraycopy(MARBLES_HALF, 0, MARBLES, 7, 6);

		// Prompts for user input
		marbles = setBoard();

		if (SAVE_OUTPUT) {
			try {
				PrintStream fileOut = new PrintStream("./out.txt");
				System.setOut(fileOut);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<Board> boardLayer1 = new ArrayList<Board>();
		List<Board> bests = new ArrayList<Board>();

		for (int i = 0; i < 6; i++) {
			boardLayer1.add(new Board(marbles));
		}
		tryMoves(boardLayer1, bests);
	}

	/**
	 * Tries all the possible moves on boards.
	 * 
	 * @param boards
	 */
	public static void tryMoves(List<Board> boards, List<Board> bests) {
		tryMovesPlayer(boards, bests);
		bests.add(Board.findBest(boards));
		saveBoard(boards, marbles, saveNum);
		Board.findBest(bests).printBoard();
		// System.out.println(boards.size());
//		indexLayer1 = 0;
//		List<Board> boards2 = new ArrayList<Board>();
//		for (int n = 0; n < boards.size(); n++) {
//			for (int a = 0; a < 6; a++) {
//				boards2.add(new Board(boards.get(n)));
//				boards2.get(a).move = true;
//			}
//			tryMovesOpponent(boards2);
//		}
//		int biggestDiff = 0;
//		int biggestIndex = 0;
//		for (int i = 0; i < boards2.size(); i++) {
//			if (boards2.get(i).marbles[6] - boards2.get(i).marbles[13] > biggestDiff) {
//				biggestDiff = boards2.get(i).marbles[6] - boards2.get(i).marbles[13];
//				biggestIndex = i;
//			}
//		}
//		System.out.println(boards2.get(biggestIndex).getMovesString());
	}

	/**
	 * Tries each possible combination of moves on the players side(index 0-5).
	 * Requires the List to have a initial size of 6. Dynamically adds new indexes
	 * as new forks are explored.
	 * 
	 * @param boards An {@link ArrayList} of {@link Board}
	 */
	public static void tryMovesPlayer(List<Board> boards, List<Board> bests) {

		for (int i = 0; i < 6; i++, gc++) {
			if (boards.get(indexLayer1).marbles[i] > 0) {
				boards.get(indexLayer1).playPlayer(i);
				// boards.get(indexLayer1).printBoard();
				if (boards.get(indexLayer1).move) {
					for (int n = 0; n < 6; n++) {
						boards.add(n + indexLayer1 + 1, new Board(boards.get(indexLayer1)));
					}
					if (DELETE_AFTER_USE)
						boards.remove(indexLayer1);
					else
						indexLayer1++;
					tryMovesPlayer(boards, bests);
				} else
					indexLayer1++;
			} else
				boards.remove(indexLayer1);
		}
//		if (gc % 60000 == 0) {
//			System.gc();
//		}
		if (boards.size() > SHEET_MAX_LENGTH * SHEET_NUMBER) {
			bests.add(Board.findBest(boards));
			saveBoard(boards, marbles, saveNum);
			saveNum++;
			System.gc();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Tries each possible combination of moves on the opponents side(index 7-12).
	 * Requires the List to have a initial size of 6. Dynamically adds new indexes
	 * as new forks are explored.
	 * 
	 * @param boards An {@link ArrayList} of {@link Board}
	 */
	public static void tryMovesOpponent(List<Board> boards) {

		for (int i = 0; i < 6; i++) {
			if (boards.get(indexLayer1).marbles[i] > 0) {
				boards.get(indexLayer1).playOpponent(i + 7);
				boards.get(indexLayer1).printBoard();
				if (boards.get(indexLayer1).move) {
					for (int n = 0; n < 6; n++) {
						boards.add(n + indexLayer1 + 1, new Board(boards.get(indexLayer1)));
					}
					if (DELETE_AFTER_USE)
						boards.remove(indexLayer1);
					else
						indexLayer1++;
					tryMovesOpponent(boards);
				} else
					indexLayer1++;
			} else
				boards.remove(indexLayer1);
		}
	}

	/**
	 * Creates an excel sheet of the boards.
	 * 
	 * @param boards  A list of boards.
	 * @param marbles The array of marbles used by the boards.
	 */
	public static void saveBoard(List<Board> boards, int[] marbles, int num) {
		System.out.println("saving...");

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet[] sheet = { workbook.createSheet("Mancala Data 1"), workbook.createSheet("Mancala Data 2"),
				workbook.createSheet("Mancala Data 3"), workbook.createSheet("Mancala Data 4"),
				workbook.createSheet("Mancala Data 5") };

		for (int n = 0; n < SHEET_NUMBER; n++) {
			System.out.println("mapping sheet " + (n + 1) + "...");
			System.out.println("index is " + indexLayer1);
			int index = indexLayer1;

			Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
			for (int i = 0; i < index && i < SHEET_MAX_LENGTH; i++) {
				if (!boards.get(0).move) {
					data.put(i, boardText(boards.get(0)));
					boards.remove(0);
					indexLayer1--;
				}
			}

			Set<Integer> keyset = data.keySet();
			int rownum = 0;
			for (Integer key : keyset) {
				Row row = sheet[n].createRow(rownum++);
				Object[] objArr = data.get(key);
				int cellnum = 0;
				for (Object obj : objArr) {
					Cell cell = row.createCell(cellnum++);
					if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof Integer)
						cell.setCellValue((Integer) obj);
				}
			}

			System.gc();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("index is " + indexLayer1);
		}

		try {
			FileOutputStream out = new FileOutputStream(
					new File("Boards/Board-" + getMarbleLayout(marbles) + "-" + num + ".xlsx"));
			workbook.write(out);
			out.close();
			System.out.println("Board-" + getMarbleLayout(marbles) + "-" + num + ".xlsx saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.gc();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
