package com.mason.mancala.game.ai;

import java.util.HashMap;

import com.mason.mancala.game.Board;
import com.mason.mancala.game.GameBoard;

public class AI {

	private int maxDepth;
	private final int MAX_DEPTH;
	private static final int MIN = -48;
	private static final int MAX = -MIN;
	private long numChecked;
	private long hashesUsed;
	private HashMap<Integer, Byte> bestMoves;

	public AI(int maxDepth) {
		this.MAX_DEPTH = maxDepth;
		bestMoves = new HashMap<Integer, Byte>();
	}

	private int minMaxScore(Board board, int currentLayer, int alpha, int beta) {

		if (currentLayer < maxDepth && board.possibleMove()) {
			// if (currentLayer == 5)
			// System.out.println(numChecked);
			return minMaxTurn(board, currentLayer, alpha, beta, board.getPlayerMove(), 0);
		} else {
			numChecked++;
			return board.scoreDiff();
		}
	}

	private int minMaxTurn(Board board, int currentLayer, int alpha, int beta, boolean move, int turnNum) {
		int bestScore = 0;
		boolean hasBest = false;

		int bestMove = 0;

		int[] moves = new int[] { 0, 1, 2, 3, 4, 5 };

		if (bestMoves.containsKey(board.toString().hashCode())) {
			hashesUsed++;
			int bestHash = 0;
			// System.out.println("Hash " + board.toString().hashCode() + " used!");
			bestHash = bestMoves.get(board.toString().hashCode());
			moves[bestHash] = 0;
			moves[0] = bestHash;
		}

		if (beta > alpha) {
			for (int i : moves) {

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
				int previousBest = bestScore;

				if (board.canPlay(i)) {
					Board child = new Board(board);
					child.play(i);

					if (move == child.getPlayerMove()) {
						int minMax = minMaxTurn(child, currentLayer, alpha, beta, move, turnNum + 1);

						if (currentLayer % 2 == 0 && hasBest) {
							bestScore = Math.max(bestScore, minMax);
						} else if (currentLayer % 2 == 1 && hasBest) {
							bestScore = Math.min(bestScore, minMax);
						} else {
							bestScore = minMax;
							hasBest = true;
						}
					} else {
						int minMax = minMaxScore(child, currentLayer + 1, alpha, beta);

						if (currentLayer % 2 == 0 && hasBest) {
							bestScore = Math.max(bestScore, minMax);
						} else if (currentLayer % 2 == 1 && hasBest) {
							bestScore = Math.min(bestScore, minMax);
						} else {
							bestScore = minMax;
							hasBest = true;
						}
					}

					if (previousBest != bestScore)
						bestMove = i;

				}
			}
		}

		if (hasBest && board.possibleMove()) {
			if (currentLayer < maxDepth  && (!bestMoves.containsKey(board.toString().hashCode())
			        || bestMoves.get(board.toString().hashCode()) != bestScore))
				bestMoves.put(board.toString().hashCode(), (byte) bestMove);
			return bestScore;
		} else {
			numChecked++;
			return board.scoreDiff();
		}
	}

	public int findBestMove(GameBoard board) {

		numChecked = 0;
		hashesUsed = 0;

		if (board.getPlayerMove()) {
			maxDepth = MAX_DEPTH;
			minMaxScore(board, 0, MIN, MAX);
		} else {
			maxDepth = MAX_DEPTH + 1;
			minMaxScore(board, 1, MIN, MAX);
		}

		System.out.println("Checked: " + numChecked);
		System.out.println("num Hashes: " + bestMoves.size() + "\nnum used: " + hashesUsed);

		return bestMoves.get(board.toString().hashCode());
	}
}
