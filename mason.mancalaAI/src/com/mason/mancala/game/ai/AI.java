package com.mason.mancala.game.ai;

import java.util.ArrayList;
import java.util.HashMap;

import com.mason.mancala.game.board.Board;
import com.mason.mancala.game.board.BoardArray;

public class AI {

	private int maxDepth;
	private boolean playerMove;

	private static final int MIN = -48;
	private static final int MAX = -MIN;

	private static final boolean HASHING = false;

	private HashMap<Long, int[]> hm;

	private int lastBestMove = -1;

	@SuppressWarnings("unused")
	private long numChecked = 0;

	public AI() {
		hm = new HashMap<Long, int[]>();

	}

	private int minMaxScore(BoardArray boardArray, int depth, int alpha, int beta) {

		if (depth < maxDepth && boardArray.possibleMove()) {
			// if (depth == 5)
			// System.out.println(numChecked);
			int minMax = minMaxTurn(boardArray, depth, alpha, beta, boardArray.playerMove, 0);
			if (HASHING)
				if (hm.containsKey(boardArray.hashNum()))
					hm.replace(boardArray.hashNum(), createHash(boardArray, minMax, lastBestMove, 0, 255));
				else
					hm.put(boardArray.hashNum(), createHash(boardArray, minMax, lastBestMove, 0, 255));
			return minMaxTurn(boardArray, depth, alpha, beta, boardArray.playerMove, 0);
		} else {
			lastBestMove = -1;
			numChecked++;
			return  boardArray.scoreDiff(playerMove);
		}
	}

	private int minMaxTurn(BoardArray boardArray, int depth, int alpha, int beta, boolean move, int turnNum) {
		int bestScore = 0;
		int bestMove = -1;
		int oldBest = 0;
		boolean hasBest = false;

		if (HASHING)
			if (hm.containsKey(boardArray.hashNum())) {
				BoardArray child = new BoardArray(boardArray);
				int moveNum = hm.get(boardArray.hashNum())[2];
				child.play(moveNum);
				if (move == child.playerMove) {
					// bestScore = minMaxTurn(child, depth, alpha, beta, move, turnNum + 1);
				} else {
					bestScore = minMaxScore(child, depth + 1, alpha, beta);
					bestMove = moveNum;
					hasBest = true;
				}
			}

		for (int i = 0; i < 6; i++) {

			if (hasBest) {
				if (depth % 2 == 0) {
					alpha = Math.max(alpha, bestScore);
					if (beta <= alpha)
						break;
				} else {
					beta = Math.min(beta, bestScore);
					if (beta <= alpha)
						break;
				}
			}

			if (boardArray.canPlay(i)) {
				BoardArray child = new BoardArray(boardArray);
				child.play(i);

				if (move == child.playerMove) {
					int minMax = minMaxTurn(child, depth, alpha, beta, move, turnNum + 1);

					if (depth % 2 == 0 && hasBest) {
						bestScore = Math.max(bestScore, minMax);
						if (oldBest < bestScore)
							bestMove = i;
					} else if (depth % 2 == 1 && hasBest) {
						bestScore = Math.min(bestScore, minMax);
						if (oldBest > bestScore)
							bestMove = i;
					} else {
						bestScore = minMax;
						bestMove = i;
						hasBest = true;
					}
				} else {
					int minMax = minMaxScore(child, depth + 1, alpha, beta);

					if (depth % 2 == 0 && hasBest) {
						bestScore = Math.max(bestScore, minMax);
						if (oldBest < bestScore)
							bestMove = i;
					} else if (depth % 2 == 1 && hasBest) {
						bestScore = Math.min(bestScore, minMax);
						if (oldBest > bestScore)
							bestMove = i;
					} else {
						bestScore = minMax;
						bestMove = i;
						hasBest = true;
					}
				}
			}
		}

		if (hasBest && boardArray.possibleMove()) {
			if (HASHING)
				if (hm.containsKey(boardArray.hashNum()))
					hm.replace(boardArray.hashNum(), createHash(boardArray, bestScore, bestMove, 0, depth));
				else
					hm.put(boardArray.hashNum(), createHash(boardArray, bestScore, bestMove, 0, depth));
			lastBestMove = bestMove;
			return bestScore;
		} else {
			if (HASHING)
				hm.put(boardArray.hashNum(),
						createHash(boardArray, boardArray.scoreDiff(playerMove), bestMove, 0, depth));
			numChecked++;
			lastBestMove = -1;
			return boardArray.scoreDiff(playerMove);
		}
	}

	public int findBestMove(Board board, int maxDepth) {

		this.maxDepth = maxDepth;
		numChecked =  0;

		playerMove = board.playerMove;
		minMaxScore(board, 0, MIN, MAX);

		 System.out.println(numChecked);
		/*
		 * System.out.println(bestScore.score);
		 * 
		 * for (int i = 0; i < scores.size(); i++) { System.out.print(scores.get(i) +
		 * ","); } System.out.println();
		 * 
		 * System.out.println("Checked: " + numChecked);
		 *
		 * for (int i = 0; i < 6; i++) { if (scores.get(i) != null &&
		 * Integer.parseInt(scores.get(i)) == bestScore) return i; }
		 */
		// int[] hmh = hm.get(board.hashNum());
		// for (int i = 0; i < hmh.length; i++) {
		//System.out.println(lastBestMove);
		// }

		return lastBestMove;
	}

	int[] createHash(BoardArray boardArray, int minMax, int sugestedMove, int flag, int depth) {
		int pm;
		if (boardArray.playerMove)
			pm = 1;
		else
			pm = 0;
		return new int[] { flag, sugestedMove, pm, depth, minMax };
	}
}
