package com.mason.mancala.game;

import java.util.List;

/**
 * A mancala board. The marbles in each pocket is tracked by
 * {@link Board#marbles}.
 * 
 * @author Mason
 *
 */
public class Board {

	/**
	 * The amount of marbles in each pocket of Board.
	 * <p>
	 * Index 0 is the player's leftmost pocket. Numbering continue in a
	 * counter-clockwise motion and includes the player's store (index 6) and the
	 * opponent's store (index 13). Numbers are changed through
	 * {@link Board#playPlayer(int)} and {@link Board#playOpponent(int)}.
	 * </p>
	 */
	int[] marbles;

	/**
	 * Whether or not the player or opponent can play a move.
	 * 
	 * @see Board#playPlayer(int)
	 * @see Board#playOpponent(int)
	 */
	boolean move;

	/**
	 * Stored as 1 is the first pocket (index 0) and 12 in the last (index 12).
	 */
	int[] moves;

	/**
	 * The current move of the board.
	 */
	int currentMove;

	/**
	 * The maximum moves the player will make
	 */
	static final int MOSTMOVES = 60;

	/**
	 * Default constructor. {@link Board.marbles} set to {@link Mancala#MARBLES}.
	 * {@link Board#move} set to <code>true</code>. {@link Board#moves} created and
	 * {@link Board#currentMove} set to 0.
	 */
	Board() {
		marbles = new int[14];
		System.arraycopy(Mancala.MARBLES, 0, marbles, 0, 14);
		this.move = true;
		this.moves = new int[MOSTMOVES];
		currentMove = 0;
	}

	/**
	 * Constructor.
	 * @param marbles Sets {@link Board#marbles}
	 */
	Board(int[] marbles) {
		this.marbles = new int[marbles.length];
		System.arraycopy(marbles, 0, this.marbles, 0, marbles.length);
		this.move = true;
		this.moves = new int[MOSTMOVES];
		currentMove = 0;
	}

	/**
	 * @param marbles
	 * @param move
	 * @param moves
	 */
	Board(int[] marbles, boolean move, int[] moves) {
		this.marbles = new int[marbles.length];
		System.arraycopy(marbles, 0, this.marbles, 0, marbles.length);
		this.move = move;
		this.moves = moves;
		currentMove = 0;
	}

	/**
	 * @param marbles
	 * @param move
	 * @param moves
	 * @param currentMove
	 */
	Board(int[] marbles, boolean move, int[] moves, int currentMove) {
		this.marbles = new int[marbles.length];
		System.arraycopy(marbles, 0, this.marbles, 0, marbles.length);
		this.move = move;
		this.moves = moves;
		this.currentMove = currentMove;
	}

	/**
	 * Deep copy Board
	 * 
	 * @param board the Board to be copied
	 * @return a deep copy of the Board
	 */
	public Board(Board board) {
		this.currentMove = board.currentMove;
		this.marbles = new int[board.marbles.length];
		System.arraycopy(board.marbles, 0, this.marbles, 0, board.marbles.length);
		this.move = board.move;
		this.moves = new int[MOSTMOVES];
		System.arraycopy(board.moves, 0, this.moves, 0, board.moves.length);

	}

	/**
	 * Plays the game from <code>pickUp</code> and deposits marbles into
	 * 
	 * @param pickUp the pocket to play the game from
	 */
	public void playPlayer(int pickUp) {
		if (move) {
			moves[currentMove] = pickUp + 1;
			// System.out.println(pickUp + "," + currentMove);
			currentMove++;
		}

		move = false;

		if (marbles[pickUp] > 0) {
			int current = marbles[pickUp];
			marbles[pickUp] = 0;
			int currentPocket = pickUp;
			for (int i = 0; i < current; i++) {
				currentPocket = (currentPocket + 1) % 13;
				marbles[currentPocket]++;
			}
			// printBoard();
			if (marbles[currentPocket] > 1 && currentPocket != 6) {
				playPlayer(currentPocket);
			}
			// System.out.println(currentPocket);
			if (currentPocket == 6) {
				move = true;
			}
		}
	}

	/**
	 * @param pickUp
	 */
	public void playOpponent(int pickUp) {
		if (move) {
			moves[currentMove] = pickUp;
//			System.out.println(pickUp + "," + currentMove);
			currentMove++;
		}

		move = false;

		if (marbles[pickUp] > 0) {
			int current = marbles[pickUp];
			marbles[pickUp] = 0;
			int currentPocket = pickUp;
			for (int i = 0; i < current; i++) {
				currentPocket = (currentPocket + 1) % 14;
				if (currentPocket == 6)
					currentPocket++;
				marbles[currentPocket]++;
			}
			// printBoard();
			if (marbles[currentPocket] > 1 && currentPocket != 13) {
				playOpponent(currentPocket);
			}
			// System.out.println(currentPocket);
			if (currentPocket == 13) {
				move = true;
			}
		}
	}

	/**
	 * 
	 */
	public void printBoard() {
		String out = new String();
		out = Integer.toString(currentMove);
//		out = marbles[6] + " : ";
//		for (int i = 0; i < currentMove; i++) {
//			out = out + Integer.toString(moves[i]);
//			if (i + 1 < currentMove)
//				out = out + ", ";
//		}
//		out = out + " : " + move + " : ";
//		for (int i = 0; i < marbles.length; i++) {
//			out = out + marbles[i];
//			if (i + 1 < marbles.length)
//				out = out + ", ";
//		}

		System.out.println(out);
	}

	/**
	 * @return {@link Board.moves} in String form.
	 */
	public String getMovesString() {
		String moveString = "";
		for (int i = 0; i < this.currentMove; i++) {
			moveString = moveString + moves[i];
			if (i + 1 < this.currentMove)
				moveString = moveString + ", ";
		}
		return moveString;
	}

	/**
	 * @return
	 */
	public String getMarblesString() {
		String marbleString = "";
		for (int i = 0; i < marbles.length; i++) {
			marbleString = marbleString + marbles[i];
			if (i + 1 < marbles.length)
				marbleString = marbleString + ", ";
		}
		return marbleString;
	}
	
	/**
	 * @param board Board to be read.
	 * @return An object of different aspects of board.
	 */
	public Object[] boardText() {
		return new Object[] { getMovesString(), marbles[6], getMarblesString(),
				Integer.toString(currentMove) };
	}
	
	public static Board findBest(List<Board> boards) {
		int bestMarbles = 0;
		int bestIndex = 0;
		for (int i = 0; i < boards.size();i++) {
			if (bestMarbles < boards.get(i).marbles[6]) {
				bestMarbles = boards.get(i).marbles[6];
				bestIndex = i;
			}
			else if (bestMarbles == boards.get(i).marbles[6] && boards.get(bestIndex).currentMove > boards.get(i).currentMove) {
				bestIndex = i;
			}
		}
		return new Board(boards.get(bestIndex));
	}

}
