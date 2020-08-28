package com.mason.mancala.game.ai;

import java.util.ArrayList;
import java.util.List;

import com.mason.mancala.game.Board;

public class AI {

	private int maxDepth;
	private int index;

	public AI(int maxDepth) {
		this.maxDepth = maxDepth;
		index = 0;
	}

	private int minMaxScore(Board board, int currentLayer, ArrayList<> scores) {
		if (currentLayer < MAX_DEPTH) {
			if (currentLayer % 2 == 0)
				return Math.max(, b)
					
		}
	}

	private int minMaxTurn(Board board, int currentLayer, boolean move) {
		for (int i = 0; i < 6 && currentLayer < MAX_DEPTH; i++) {
			// System.out.println(boards.get(index).playerMove + "," + i + "," +
			// boards.get(index).canPlay(i) + "," + index
			// + "," + currentLayer);
			if (boards.get(index).canPlay(i)) {
				boards.add(new Board(boards.get(index)));
				boards.get(index).play(i);
				// boards.get(index).printBoard();
				if (move == board.get(index).playerMove)
					boards = minMaxTurn(boards, currentLayer, move);
				else
					boards = makeBoards(boards, currentLayer + 1, i);
				index++;
			}
		}
		return boards;
	}

	public int findBestMove(Board board) {

		int bestScore = minMaxScore(board, 0);

		return bestMove;
	}
}
