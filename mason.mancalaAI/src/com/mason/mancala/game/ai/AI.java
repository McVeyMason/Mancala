package com.mason.mancala.game.ai;

import java.util.ArrayList;

import com.mason.mancala.game.Board;

public class AI {

	private int maxDepth;
	private boolean playerMove;
	private static final int MIN = -48;
	private static final int MAX = -MIN;
	/**
	 * Uses string format so null is no move;
	 */
	private ArrayList<String> scores;
	private long numChecked = 0;

	public AI(int maxDepth) {
		this.maxDepth = maxDepth;
		scores = new ArrayList<String>();
	}

	private int minMaxScore(Board board, int currentLayer, int alpha, int beta) {

		if (currentLayer < maxDepth && board.possibleMove()) {
			// if (currentLayer == 5)
			// System.out.println(numChecked);
			return minMaxTurn(board, currentLayer, alpha, beta, board.playerMove, 0);
		} else {
			numChecked++;
			return board.scoreDiff(playerMove);
		}
	}

	private int minMaxTurn(Board board, int currentLayer, int alpha, int beta, boolean move, int turnNum) {
		int bestScore = 0;
		boolean hasBest = false;
		for (int i = 0; i < 6; i++) {

			if (hasBest) {
				if (currentLayer % 2 == 0) {
					alpha = Math.max(alpha, bestScore);
					if (beta <= alpha)
						break;
				} else {
					beta = Math.min(beta, bestScore);
					if (beta <= alpha)
						break;
				}
			}

			if (board.canPlay(i)) {
				Board child = new Board(board);
				child.play(i);

				if (move == child.playerMove) {
					int minMax = minMaxTurn(child, currentLayer, alpha, beta, move, turnNum + 1);

					if (currentLayer == 0 && turnNum == 0)
						scores.add(Integer.toString(minMax));

					if (currentLayer % 2 == 0 && hasBest)
						bestScore = Math.max(bestScore, minMax);
					else if (currentLayer % 2 == 1 && hasBest)
						bestScore = Math.min(bestScore, minMax);
					else {
						bestScore = minMax;
						hasBest = true;
					}
				} else {
					int minMax = minMaxScore(child, currentLayer + 1, alpha, beta);

					if (currentLayer == 0 && turnNum == 0)
						scores.add(Integer.toString(minMax));

					if (currentLayer % 2 == 0 && hasBest)
						bestScore = Math.max(bestScore, minMax);
					else if (currentLayer % 2 == 1 && hasBest)
						bestScore = Math.min(bestScore, minMax);
					else {
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
			return board.scoreDiff(playerMove);
		}
	}

	public int findBestMove(Board board) {

		String[] currentTree = new String[maxDepth];
		for (int i = 0; i < currentTree.length; i++) {
			currentTree[i] = null;
		}
		playerMove = board.playerMove;
		scores.clear();
		int bestScore = minMaxScore(board, 0, MIN, MAX);

		System.out.println(numChecked);
		/*
		 * System.out.println(bestScore.score);
		 * 
		 * for (int i = 0; i < scores.size(); i++) { System.out.print(scores.get(i) +
		 * ","); } System.out.println();
		 * 
		 * System.out.println("Checked: " + numChecked);
		 */
		for (int i = 0; i < 6; i++) {
			if (scores.get(i) != null && Integer.parseInt(scores.get(i)) == bestScore)
				return i;
		}

		return -1;
	}
}
