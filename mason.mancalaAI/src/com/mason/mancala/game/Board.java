package com.mason.mancala.game;

public class Board {
	
	/**
	 * The amount of marbles in each pocket of Board.
	 * <p>
	 * Index 0 is the player's leftmost pocket. Numbering continue in a
	 * counter-clockwise motion and includes the player's store (index 6) and the
	 * opponent's store (index 13). Numbers are changed through
	 * {@link GameBoard#playPlayer(int)} and {@link GameBoard#playOpponent(int)}.
	 * </p>
	 */
	protected int[] marbles = new int[14];
	
	/**
	 * If it is the player's turn.
	 */
	private boolean playerMove = true;
	
	public Board() {
		
	}
	
	public Board(Board board) {
		this.playerMove = board.playerMove;
		System.arraycopy(board.marbles, 0, marbles, 0, 14);
	}
	
	/**
	 * Plays the game from <code>pickUp</code> and deposits marbles into
	 * 
	 * @param pickUp the pocket to play the game from
	 */
	protected void playPlayer(int pickUp) {

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
			playerMove  = false;
	}

	/**
	 * @param pickUp the starting pocket (0-6) on the opponents side.
	 */
	protected void playOpponent(int pickUp) {
		pickUp += 7;

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
			if (getPlayerMove())
				playPlayer(pickUp);
			else
				playOpponent(pickUp);
			if (!possibleMoveOpponent()) {
				for (int i = 0; i < 6; i++) {
					marbles[6] += marbles[i];
					marbles[i] = 0;
				}
			} else if (!possibleMovePlayer()) {
				for (int i = 0; i < 6; i++) {
					marbles[13] += marbles[i + 7];
					marbles[i + 7] = 0;
				}
			}
		}
	}
	
	/**
	 * Returns the score difference
	 * 
	 * @param player From the players perspective?
	 * @return
	 */
	public int scoreDiff(boolean player) {
		if (player)
			return marbles[6] - marbles[13];
		else
			return marbles[13] - marbles[6];
	}
	
	public boolean possibleMove() {
		return possibleMovePlayer() && possibleMoveOpponent();
	}

	/**
	 * If the player can take a move from the pocket.
	 * 
	 * @param pocket
	 * @return
	 */
	public boolean canPlay(int pocket) {
		if (pocket >= 0 && pocket < 6)
			if (getPlayerMove())
				return (marbles[pocket] > 0);
			else
				return (marbles[pocket + 7] > 0);
		else
			return false;
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
	
	public boolean getPlayerMove() {
		return playerMove;
	}
	
	public void setPlayerMove(boolean playerMove) {
		this.playerMove = playerMove;
	}
}
