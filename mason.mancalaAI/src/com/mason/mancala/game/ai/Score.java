package com.mason.mancala.game.ai;

public class Score {

	int score;
	boolean pass;
	/**
	 * Constructor for score. Used to decrease work by allowing skipping of whole trees
	 * @param score
	 * @param pass
	 */
	Score(int score, boolean pass) {
		this.score = score;
		this.pass = pass;
	}
	
	Score pass() {
		pass = true;
		return this;
	}
	
	Score continuePass() {
		pass = false;
		return this;
	}

}
