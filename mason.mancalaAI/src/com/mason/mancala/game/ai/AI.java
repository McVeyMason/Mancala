package com.mason.mancala.game.ai;

import java.util.ArrayList;

import com.mason.mancala.game.Board;

public class AI {

	private int maxDepth;
	private boolean playerMove;
	/**
	 * Uses string format so null is no move;
	 */
	private ArrayList<String> scores;
	private long numChecked = 0;

	public AI(int maxDepth) {
		this.maxDepth = maxDepth;
		scores = new ArrayList<String>();
	}

	private Score minMaxScore(Board board, int currentLayer, String[] currentTree) {

		if (currentLayer < maxDepth && board.possibleMove()) {
			//if (currentLayer == 5)
			//	System.out.println(numChecked);
			return minMaxTurn(board, currentLayer, currentTree, board.playerMove, 0);
		} else {
			numChecked++;
			return new Score(board.scoreDiff(playerMove), false);
		}
	}

	private Score minMaxTurn(Board board, int currentLayer, String[] currentTree, boolean move, int turnNum) {
		Score bestScore = new Score(0, false);
		boolean hasBest = false;
		for (int i = 0; i < 6; i++) {

			if (hasBest) {
				if (bestScore.pass) {
					currentTree[currentLayer] = null;
					return bestScore.continuePass();
				}
				currentTree[currentLayer] = Integer.toString(bestScore.score);
			}
			if (hasBest && currentLayer > 1 && currentTree[currentLayer - 2] != null) {
				if (currentLayer % 2 == 0) {
					if (bestScore.score <= Integer.parseInt(currentTree[currentLayer - 2])) {
						currentTree[currentLayer] = null;
						return bestScore.pass();
					}
				} else {
					if (bestScore.score >= Integer.parseInt(currentTree[currentLayer - 2])) {
						currentTree[currentLayer] = null;
						return bestScore.pass();
					}
				}

			}

			if (board.canPlay(i)) {
				Board child = new Board(board);
				child.play(i);

				if (move == child.playerMove) {
					Score minMax = minMaxTurn(child, currentLayer, currentTree, move, turnNum + 1);
					if (minMax.pass)
						return minMax;
					if (hasBest) {
						if (currentLayer == 0 && turnNum == 0) {
							scores.add(Integer.toString(minMax.score));
							bestScore.score = Math.max(bestScore.score, minMax.score);
						} else if (currentLayer % 2 == 0)
							bestScore.score = Math.max(bestScore.score, minMax.score);
						else
							bestScore.score = Math.min(bestScore.score, minMax.score);
					} else {
						if (currentLayer == 0 && turnNum == 0) {
							bestScore = minMax;
							scores.add(Integer.toString(bestScore.score));
						} else
							bestScore = minMax;
						hasBest = true;
					}
				} else {
					Score minMax = minMaxScore(child, currentLayer + 1, currentTree);
					if (minMax.pass) {
						currentTree[currentLayer] = null;
						return minMax.continuePass();
					}
					if (hasBest) {
						if (currentLayer == 0 && turnNum == 0) {
							scores.add(Integer.toString(minMax.score));
							bestScore.score = Math.max(bestScore.score, minMax.score);
						} else if (currentLayer % 2 == 0)
							bestScore.score = Math.max(bestScore.score, minMax.score);
						else
							bestScore.score = Math.min(bestScore.score, minMax.score);
					} else {
						if (currentLayer == 0 && turnNum == 0) {
							bestScore = minMax;
							scores.add(Integer.toString(bestScore.score));
						} else
							bestScore = minMax;
						hasBest = true;
					}
				}
			} else if (currentLayer == 0 && turnNum == 0) {
				scores.add(null);
			}
		}
		if (hasBest && board.possibleMove())
			return bestScore;
		else {
			numChecked++;
			return new Score(board.scoreDiff(playerMove), false);
		}
	}

	public int findBestMove(Board board) {

		String[] currentTree = new String[maxDepth];
		for (int i = 0; i < currentTree.length; i++) {
			currentTree[i] = null;
		}
		playerMove = board.playerMove;
		scores.clear();
		Score bestScore = minMaxScore(board, 0, currentTree);

		/*
		System.out.println(bestScore.score);

		for (int i = 0; i < scores.size(); i++) {
			System.out.print(scores.get(i) + ",");
		}
		System.out.println();

		System.out.println("Checked: " + numChecked);
*/
		for (int i = 0; i < 6; i++) {
			if (scores.get(i) != null && Integer.parseInt(scores.get(i)) == bestScore.score)
				return i;
		}

		return -1;
	}
}
