package com.mason.mancala.game.board;

import com.mason.mancala.game.Mancala;

/**
 * A mancala board. The marbles in each pocket is tracked by
 * {@link Board#marbles}.
 * 
 * @author Mason
 *
 */
public class Board extends BoardArray {

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
	static final int MOSTMOVES = 70;

	/**
	 * The text above the board.
	 */
	public String text = "";

	/**
	 * If the game is playing.
	 */
	public boolean playing = false;

	public boolean prompt = true;

	/**
	 * player, opponent, or tie
	 */
	public String winner = "";

	public Board() {
		super(Mancala.MARBLES, true);
	}

	/**
	 * Constructor.
	 * 
	 * @param marbles Sets {@link Board#marbles}
	 */
	public Board(int[] marbles) {
		super(marbles, true);
		this.moves = new int[MOSTMOVES];
		currentMove = 0;
	}

	/**
	 * @param marbles
	 * @param move
	 * @param moves
	 */
	Board(int[] marbles, int[] moves) {
		super(marbles, true);
		this.moves = moves;
		currentMove = 0;
	}

	/**
	 * @param marbles
	 * @param move
	 * @param moves
	 * @param currentMove
	 */
	Board(int[] marbles, int[] moves, int currentMove) {
		super(marbles, true);
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
		super(board.marbles, board.playerMove);
		this.currentMove = board.currentMove;
		this.moves = new int[MOSTMOVES];
		System.arraycopy(board.moves, 0, this.moves, 0, board.moves.length);

	}

	/**
	 * 
	 */
	public void printBoard() {
		String out = new String();
		out = marbles[6] + " : ";
		for (int i = 0; i < currentMove; i++) {
			out = out + Integer.toString(moves[i]);
			if (i + 1 < currentMove)
				out = out + ", ";
		}
		out = out + " : " + playerMove + " : ";
		for (int i = 0; i < marbles.length; i++) {
			out = out + marbles[i];
			if (i + 1 < marbles.length)
				out = out + ", ";
		}

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
		return new Object[] { getMovesString(), marbles[6], getMarblesString(), Integer.toString(currentMove) };
	}

	public void playBoard(int pickUp) {
		play(pickUp);
		if (winNum != -1)
			if (winNum == 0)
				winner = "player";
			else if (winNum == 1)
				winner = "opponent";
			else
				winner = "tie";
	}
}
