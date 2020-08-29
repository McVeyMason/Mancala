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

	private int minMaxScore(Board board, int currentLayer, ArrayList<Integer> currentTree) {

		if (currentLayer < maxDepth && board.possibleMove()) {
			//if (currentLayer == 2)
			//	System.out.println(numChecked);
			return minMaxTurn(board, currentLayer, currentTree, board.playerMove, 0);
		} else {
			numChecked++;
			return board.scoreDiff(playerMove);
		}
	}

	private int minMaxTurn(Board board, int currentLayer, ArrayList<Integer> currentTree, boolean move, int turnNum) {
		int bestScore = 0;
		boolean hasBest = false;
		for (int i = 0; i < 6; i++) {
			/*
			if (hasBest)
				currentTree.add(bestScore);
			if (hasBest && currentLayer > 1 && currentTree.size() > 1) {
				if (currentLayer % 2 == 0 ) {
					if (bestScore <= currentTree.get(currentTree.size() - 2))
						return bestScore;
				} else {
					if (bestScore >= currentTree.get(currentTree.size() - 2))
						return bestScore;
				}
					
			}*/
			
			if (board.canPlay(i)) {
				Board child = new Board(board);
				child.play(i);

				if (move == child.playerMove) {
					if (hasBest) {
						if (currentLayer == 0 && turnNum == 0) {
							int minMax = minMaxTurn(child, currentLayer, currentTree, move, turnNum + 1);
							scores.add(Integer.toString(minMax));
							bestScore = Math.max(bestScore, minMax);
						} else if (currentLayer % 2 == 0)
							bestScore = Math.max(bestScore, minMaxTurn(child, currentLayer, currentTree, move, turnNum + 1));
						else
							bestScore = Math.min(bestScore, minMaxTurn(child, currentLayer, currentTree, move, turnNum + 1));
					} else {
						if (currentLayer == 0 && turnNum == 0) {
							bestScore = minMaxTurn(child, currentLayer, currentTree, move, turnNum + 1);
							scores.add(Integer.toString(bestScore));
						} else
							bestScore = minMaxTurn(child, currentLayer, currentTree, move, turnNum + 1);
						hasBest = true;
					}
				} else {
					if (hasBest) {
						if (currentLayer == 0 && turnNum == 0) {
							int minMax = minMaxScore(child, currentLayer + 1, currentTree);
							scores.add(Integer.toString(minMax));
							bestScore = Math.max(bestScore, minMax);
						} else if (currentLayer % 2 == 0)
							bestScore = Math.max(bestScore, minMaxScore(child, currentLayer + 1, currentTree));
						else
							bestScore = Math.min(bestScore, minMaxScore(child, currentLayer + 1, currentTree));
					} else {
						if (currentLayer == 0 && turnNum == 0) {
							bestScore = minMaxScore(child, currentLayer + 1, currentTree);
							scores.add(Integer.toString(bestScore));
						} else
							bestScore = minMaxScore(child, currentLayer + 1, currentTree);
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

		ArrayList<Integer> currentTree = new ArrayList<Integer>();
		playerMove = board.playerMove;
		scores.clear();
		int bestScore = minMaxScore(board, 0, currentTree);

		System.out.println(bestScore);

		for (int i = 0; i < scores.size(); i++) {
			System.out.print(scores.get(i) + ",");
		}
		System.out.println();

		System.out.println("Checked: " + numChecked);

		for (int i = 0; i < 6; i++) {
			if (scores.get(i) != null && Integer.parseInt(scores.get(i)) == bestScore)
				return i;
		}

		return -1;
	}
}
