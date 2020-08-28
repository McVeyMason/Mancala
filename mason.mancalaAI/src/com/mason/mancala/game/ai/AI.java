package com.mason.mancala.game.ai;

import java.util.ArrayList;
import java.util.List;

import com.mason.mancala.game.Board;

public class AI {

	static final int MAX_DEPTH = 3;
	private int index;
	private Array_Tree tree;
	private boolean playerMove;

	public AI() {
		index = 0;
	}

	private List<Board> makeBoards(List<Board> boards, int currentLayer, int lastMove) {
		if (index != 0)
			tree.setNode(Integer.toString(boards.get(index).scoreDiff(playerMove)), currentLayer, lastMove);
		while (index < boards.size() && currentLayer < MAX_DEPTH && boards.get(index).possibleMove()) {
			tryTurn(boards, currentLayer, boards.get(index).playerMove);
		}
		boards.remove(index);
		index--;
		return boards;
	}

	private List<Board> tryTurn(List<Board> boards, int currentLayer, boolean move) {
		for (int i = 0; i < 6 && currentLayer < MAX_DEPTH; i++) {
			//System.out.println(boards.get(index).playerMove + "," + i + "," + boards.get(index).canPlay(i) + "," + index
			//		+ "," + currentLayer);
			if (boards.get(index).canPlay(i)) {
				boards.add(new Board(boards.get(index)));
				boards.get(index).play(i);
				//boards.get(index).printBoard();
				if (move == boards.get(index).playerMove)
					boards = tryTurn(boards, currentLayer, move);
				else
					boards = makeBoards(boards, currentLayer + 1, i);
				index++;
			}
		}
		return boards;
	}

	public Board findBest(Board board) {
		List<Board> boards = new ArrayList<Board>();
		tree = new Array_Tree(6, MAX_DEPTH);
		playerMove = board.playerMove;

		boards.add(board);
		tree.setRoot(Integer.toString(board.scoreDiff(playerMove)));
		boards = makeBoards(boards, -1, -1);
		int bestMarbles = 0;
		int bestIndex = 0;
		// System.out.println(boards.size() + "," + index + "," +
		// boards.get(index).currentMove + ","
		// + boards.get(index).possibleMove());
		for (int i = 0; i < boards.size(); i++) {
			if (board.playerMove) {
				if (bestMarbles < boards.get(i).marbles[6]) {
					bestMarbles = boards.get(i).marbles[6];
					bestIndex = i;
				} else if (bestMarbles == boards.get(i).marbles[6]
						&& boards.get(bestIndex).currentMove > boards.get(i).currentMove) {
					bestIndex = i;
				}
			} else {
				if (bestMarbles < boards.get(i).marbles[13]) {
					bestMarbles = boards.get(i).marbles[13];
					bestIndex = i;
				} else if (bestMarbles == boards.get(i).marbles[13]
						&& boards.get(bestIndex).currentMove > boards.get(i).currentMove) {
					bestIndex = i;
				}
			}
		}
		return new Board(boards.get(index));
	}
}
