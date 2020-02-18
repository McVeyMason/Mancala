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
	 * @see Mancala#setBoard()
	 * @see Mancala#MARBLES_HALF
	 */
	public static final int[] MARBLES = new int[14];

	public static int[] marbles = new int[14];
	

	/**
	 * The first layer of possible moves on the <code>Board</code>.
	 * @see Board
	 * @see Mancala#indexLayer1
	 */
	static List<Board> boardLayer1 = new ArrayList<Board>();
	/**
	 * The second layer of possible moves on the <code>Board</code>.
	 * @see Board
	 * @see Mancala#indexLayer2
	 */
	static List<Board> boardLayer2 = new ArrayList<Board>();

	/**
	 * The index of {@link Mancala#boardLayer1}.
	 */
	static int indexLayer1 = 0;
	/**
	 * The index of {@link Mancala#boardLayer2}.
	 */
	static int indexLayer2 = 0;

	static boolean win = false;

	/**
	 * Whether or not to delete entries if trees will be explored
	 */
	private static final boolean DELETE_AFTER_USE = true;

	public static void main(String[] args) {

		System.arraycopy(MARBLES_HALF, 0, MARBLES, 0, 6);
		System.arraycopy(MARBLES_HALF, 0, MARBLES, 7, 6);
		try {
			PrintStream fileOut = new PrintStream("./out.txt");
			System.setOut(fileOut);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Prompts for user input 
		marbles = setBoard();
		for (int i = 0; i < 6; i++) {
			boardLayer1.add(new Board(marbles));
		}
		tryMoves(boardLayer1);

	}

	public static void tryMoves(List<Board> boards) {
		tryMovesPlayer(boards);
		saveBoard(boards, marbles);
		indexLayer1 = 0;
		List<Board> boards2 = new ArrayList<Board>();
		for (int n = 0; n < boards.size(); n++) {
			for (int a = 0; a < 6; a++) {
				boards2.add(new Board(boards.get(n)));
				boards2.get(a).move = true;
			}
			tryMovesOpponent(boards2);
		}
		int biggestDiff = 0;
		int biggestIndex = 0;
		for (int i = 0; i < boards2.size(); i++) {
			if (boards2.get(i).marbles[6] - boards2.get(i).marbles[13] > biggestDiff) {
				biggestDiff = boards2.get(i).marbles[6] - boards2.get(i).marbles[13];
				biggestIndex = i;
			}
		}
		System.out.println(boards2.get(biggestIndex).getMovesString());
	}

	public static void tryMovesPlayer(List<Board> boards) {

		for (int i = 0; i < 6; i++) {
			if (boards.get(indexLayer1).marbles[i] > 0) {
				boards.get(indexLayer1).playPlayer(i);
				boards.get(indexLayer1).printBoard();
				if (boards.get(indexLayer1).move) {
					for (int n = 0; n < 6; n++) {
						boards.add(n + indexLayer1 + 1, new Board(boards.get(indexLayer1)));
					}
					if (DELETE_AFTER_USE)
						boards.remove(indexLayer1);
					else
						indexLayer1++;
					tryMovesPlayer(boards);
				} else
					indexLayer1++;
			} else
				boards.remove(indexLayer1);
		}
	}

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

	public static void saveBoard(List<Board> boards, int[] marbles) {
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet1 = workbook.createSheet("Mancala Data 1");

		// This data needs to be written (Object[])
		if (boards.size() > 65000) {
			Map<String, Object[]> data1 = new TreeMap<String, Object[]>();
			for (int i = 0; i < 65000; i++) {
				data1.put(Integer.toString(i), new Object[] { boards.get(i).marbles[6], boards.get(i).getMovesString(),
						boards.get(i).getMarblesString(), Boolean.toString(boards.get(i).move) });
			}

			// Iterate over data and write to sheet
			Set<String> keyset1 = data1.keySet();
			int rownum1 = 0;
			for (String key : keyset1) {
				Row row = sheet1.createRow(rownum1++);
				Object[] objArr = data1.get(key);
				int cellnum = 0;
				for (Object obj : objArr) {
					Cell cell = row.createCell(cellnum++);
					if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof Integer)
						cell.setCellValue((Integer) obj);
				}
			}

			// Create a blank sheet
			XSSFSheet sheet = workbook.createSheet("Mancala Data 2");

			// This data needs to be written (Object[])
			Map<String, Object[]> data = new TreeMap<String, Object[]>();
			for (int i = 65000; i < boards.size(); i++) {
				data.put("1", new Object[] { boards.get(i).marbles[6], boards.get(i).getMovesString(),
						boards.get(i).getMarblesString(), Boolean.toString(boards.get(i).move) });
			}

			// Iterate over data and write to sheet
			Set<String> keyset = data.keySet();
			int rownum = 0;
			for (String key : keyset) {
				Row row = sheet.createRow(rownum++);
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
		} else {

			Map<String, Object[]> data1 = new TreeMap<String, Object[]>();
			for (int i = 0; i < boards.size(); i++) {
				data1.put(Integer.toString(i), new Object[] { boards.get(i).marbles[6], boards.get(i).getMovesString(),
						boards.get(i).getMarblesString(), Boolean.toString(boards.get(i).move) });
			}

			// Iterate over data and write to sheet
			Set<String> keyset1 = data1.keySet();
			int rownum1 = 0;
			for (String key : keyset1) {
				Row row = sheet1.createRow(rownum1++);
				Object[] objArr = data1.get(key);
				int cellnum = 0;
				for (Object obj : objArr) {
					Cell cell = row.createCell(cellnum++);
					if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof Integer)
						cell.setCellValue((Integer) obj);
				}
			}
		}

		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File("Board-" + getMarbleLayout(marbles) + ".xlsx"));
			workbook.write(out);
			out.close();
			System.out.println("Board-" + getMarbleLayout(marbles) + ".xlsx saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			workbook.close();
		} catch (IOException e) {
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
}
