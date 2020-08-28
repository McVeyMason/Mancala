package com.mason.mancala.game.ai;

public class Array_Tree {
	
	
	int root = 0;
	String[] scores;
	int numBranches;
	
	Array_Tree(int numBranches, int depth) {
		scores = new String[(int) Math.pow(6, depth)];
		this.numBranches = numBranches;
	}
	
	void setRoot(String score) {
		scores[0] = score;
	}
	
	void setNode(String score, int root, int branch) {
		int t = (root * numBranches) + 1 + branch;
		
		if (scores[root] == null && root != 0) {
			System.out.println("no parent found! Score:" + score + " root:" + root+ " branch:" + branch);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			scores[t] = score;
			System.out.println("Score:" + score + " root:" + root+ " branch:" + branch);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
