package com.mason.mancala.game;

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
	public int[] marbles;

	/**
	 * Stored as 1 is the first pocket (index 0) and 12 in the last (index 12).
	 */
	public int[] moves;

	/**
	 * The current move of the board.
	 */
	public int currentMove;

	/**
	 * The maximum moves the player will make
	 */
	static final int MOSTMOVES = 70;

	/**
	 * The text above the board.
	 */
	String text = "";

	/**
	 * If it is the player's turn.
	 */
	public boolean playerMove;

	/**
	 * If the game is playing.
	 */
	boolean playing = false;

	boolean prompt = true;
	
	/**
	 * If the game is finished
	 */
	boolean finished = false;
	
	/**
	 * player, opponent, or tie
	 */
	String winner = "";

	/**
	 * Default constructor. {@link Board.marbles} set to {@link Mancala#MARBLES}.
	 * {@link Board#move} set to <code>true</code>. {@link Board#moves} created and
	 * {@link Board#currentMove} set to 0.
	 */
	Board() {
		marbles = new int[14];
		System.arraycopy(Mancala.MARBLES, 0, marbles, 0, 14);
		this.moves = new int[MOSTMOVES];
		currentMove = 0;

	}

	/**
	 * Constructor.
	 * 
	 * @param marbles Sets {@link Board#marbles}
	 */
	Board(int[] marbles) {
		this.marbles = new int[marbles.length];
		System.arraycopy(marbles, 0, this.marbles, 0, marbles.length);
		this.moves = new int[MOSTMOVES];
		currentMove = 0;
	}

	/**
	 * @param marbles
	 * @param move
	 * @param moves
	 */
	Board(int[] marbles, int[] moves) {
		this.marbles = new int[marbles.length];
		System.arraycopy(marbles, 0, this.marbles, 0, marbles.length);
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
		this.marbles = new int[marbles.length];
		System.arraycopy(marbles, 0, this.marbles, 0, marbles.length);
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
		this.moves = new int[MOSTMOVES];
		this.playerMove = board.playerMove;
		System.arraycopy(board.moves, 0, this.moves, 0, board.moves.length);

	}

	/**
	 * Plays the game from <code>pickUp</code> and deposits marbles into
	 * 
	 * @param pickUp the pocket to play the game from
	 */
	private void playPlayer(int pickUp) {
		moves[currentMove] = pickUp + 1;
		// System.out.println(pickUp + "," + currentMove);
		currentMove++;

		int current = marbles[pickUp];
		marbles[pickUp] = 0;
		int currentPocket = pickUp;
		for (int i = 0; i < current; i++) {
			currentPocket = (currentPocket + 1) % 13;
			marbles[currentPocket]++;
		}
		/**
		 * Give a new turn if last marble is your bank or takes opponet's
		 */
		if (currentPocket < 6 && marbles[currentPocket] == 1) {
			int oppositePocket = 12 - currentPocket;
			if (marbles[oppositePocket] > 0) {
				marbles[6] += marbles[currentPocket] + marbles[oppositePocket];
				marbles[currentPocket] = 0;
				marbles[oppositePocket] = 0;
			}
		}

		if (currentPocket != 6)
			playerMove = false;
	}

	/**
	 * @param pickUp the starting pocket (0-6) on the opponents side.
	 */
	private void playOpponent(int pickUp) {
		pickUp += 7;
		moves[currentMove] = pickUp;
		currentMove++;

		int current = marbles[pickUp];
		marbles[pickUp] = 0;
		int currentPocket = pickUp;
		for (int i = 0; i < current; i++) {
			currentPocket = (currentPocket + 1) % 14;
			if (currentPocket == 6)
				currentPocket++;
			marbles[currentPocket]++;
		}
		/**
		 * Give a new turn if last marble is your bank or takes opponet's
		 */
		if (currentPocket > 6 && currentPocket != 13 && marbles[currentPocket] == 1) {
			int oppositePocket = 6 - (currentPocket - 6);
			if (marbles[oppositePocket] > 0) {
				marbles[13] += marbles[currentPocket] + marbles[oppositePocket];
				marbles[currentPocket] = 0;
				marbles[oppositePocket] = 0;
			}
		}

		if (currentPocket != 13)
			playerMove = true;
	}

	public void play(int pickUp) {
		if (canPlay(pickUp)) {
			if (playerMove)
				playPlayer(pickUp);
			else
				playOpponent(pickUp);
			if (!possibleMoveOpponent()) {
				for (int i = 0; i < 6; i++) {
					marbles[6] += marbles[i];
					marbles[i] = 0;
				}
				finished = true;
				if (marbles[6] > marbles[13]) 
					winner = "player";
				else if (marbles[6] == marbles[13])
					winner = "tie";
				else winner = "opponent";
			} else if (!possibleMovePlayer()) {
				for (int i = 0; i < 6; i++) {
					marbles[13] += marbles[i + 7];
					marbles[i + 7] = 0;
				}
				finished = true;
				if (marbles[6] > marbles[13]) 
					winner = "player";
				else if (marbles[6] == marbles[13])
					winner = "tie";
				else winner = "opponent";
			}
		}
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

	/**
	 * If the player can take a move from the pocket.
	 * 
	 * @param pocket
	 * @return
	 */
	public boolean canPlay(int pocket) {
		if (playerMove)
			return (marbles[pocket] > 0);
		else
			return (marbles[pocket + 7] > 0);
	}

	public boolean possibleMove() {
		return possibleMovePlayer() && possibleMoveOpponent();
	}

	boolean possibleMovePlayer() {
		for (int i = 0; i < 6; i++)
			if (marbles[i] > 0)
				return true;
		return false;
	}

	boolean possibleMoveOpponent() {
		for (int i = 0; i < 6; i++)
			if (marbles[i + 7] > 0)
				return true;
		return false;
	}
	
	/**
	 * Returns the score difference
	 * @param player From the players perspective?
	 * @return 
	 */
	public int scoreDiff(boolean player) {
		if (player)
			return marbles[6] - marbles [13];
		else return marbles[13] - marbles [6];
	}

}
