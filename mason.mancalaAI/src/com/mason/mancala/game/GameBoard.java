package com.mason.mancala.game;

/**
 * A mancala board. The marbles in each pocket is tracked by
 * {@link GameBoard#marbles}.
 * 
 * @author Mason
 *
 */
public class GameBoard extends Board{

	/**
	 * Stored as 1 is the first pocket (index 0) and 12 in the last (index 12).
	 */
	protected int[] moves;

	/**
	 * The current move of the board.
	 */
	protected int currentMove;

	/**
	 * The maximum moves the player will make
	 */
	protected static final int MOSTMOVES = 70;

	/**
	 * The text above the board.
	 */
	protected String text = "";

	/**
	 * If the game is playing.
	 */
	protected boolean playing = false;

	protected boolean prompt = true;

	/**
	 * If the game is finished
	 */
	protected boolean finished = false;

	/**
	 * player, opponent, or tie
	 */
	protected String winner = "";

	/**
	 * Default constructor. {@link GameBoard.marbles} set to {@link Mancala#MARBLES}.
	 * {@link GameBoard#move} set to <code>true</code>. {@link GameBoard#moves} created and
	 * {@link GameBoard#currentMove} set to 0.
	 */
	GameBoard() {
		marbles = new int[14];
		System.arraycopy(Mancala.MARBLES, 0, marbles, 0, 14);
		this.moves = new int[MOSTMOVES];
		currentMove = 0;
	}

	/**
	 * Constructor.
	 * 
	 * @param marbles Sets {@link GameBoard#marbles}
	 */
	GameBoard(int[] marbles) {
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
	GameBoard(int[] marbles, int[] moves) {
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
	GameBoard(int[] marbles, int[] moves, int currentMove) {
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
	public GameBoard(GameBoard board) {
		this.currentMove = board.currentMove;
		this.marbles = new int[board.marbles.length];
		System.arraycopy(board.marbles, 0, this.marbles, 0, board.marbles.length);
		this.setPlayerMove(board.getPlayerMove());
		this.moves = new int[MOSTMOVES];
		System.arraycopy(board.moves, 0, this.moves, 0, board.moves.length);

	}

	public void play(int pickUp) {
		if (canPlay(pickUp)) {
			if (getPlayerMove())
				playPlayer(pickUp);
			else
				playOpponent(pickUp);
			if (!possibleMoveOpponent()) {
				for (int i = 0; i < 6; i++) {
					marbles[6] += marbles[i];
					marbles[i] = 0;
				}
				finished = true;
				setPlayerMove(false);
				if (marbles[6] > marbles[13])
					winner = "player";
				else if (marbles[6] == marbles[13])
					winner = "tie";
				else
					winner = "opponent";
			} else if (!possibleMovePlayer()) {
				for (int i = 0; i < 6; i++) {
					marbles[13] += marbles[i + 7];
					marbles[i + 7] = 0;
				}
				finished = true;
				setPlayerMove(true);
				if (marbles[6] > marbles[13])
					winner = "player";
				else if (marbles[6] == marbles[13])
					winner = "tie";
				else
					winner = "opponent";
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
		out = out + " : " + getPlayerMove() + " : ";
		for (int i = 0; i < marbles.length; i++) {
			out = out + marbles[i];
			if (i + 1 < marbles.length)
				out = out + ", ";
		}

		System.out.println(out);
	}

	/**
	 * @return {@link GameBoard.moves} in String form.
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

	

	
}
