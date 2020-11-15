package com.mason.mancala.game.input;

import java.awt.event.KeyEvent;

import com.mason.mancala.game.Mancala;

public class InputProcesser {

	public InputProcesser() {

	}

	public void process(boolean one, boolean two, boolean three, boolean four, boolean five, boolean six, boolean y,
	        boolean n) {
		Mancala.playSlots = new boolean[] { one, two, three, four, five, six };
		Mancala.y_n = new boolean[] { y, n };
	}

	public void tick(boolean[] key) {
		boolean one = key[KeyEvent.VK_1] || key[KeyEvent.VK_NUMPAD1];
		boolean two = key[KeyEvent.VK_2] || key[KeyEvent.VK_NUMPAD2];
		boolean three = key[KeyEvent.VK_3] || key[KeyEvent.VK_NUMPAD3];
		boolean four = key[KeyEvent.VK_4] || key[KeyEvent.VK_NUMPAD4];
		boolean five = key[KeyEvent.VK_5] || key[KeyEvent.VK_NUMPAD5];
		boolean six = key[KeyEvent.VK_6] || key[KeyEvent.VK_NUMPAD6];
		boolean y = key[KeyEvent.VK_Y];
		boolean n = key[KeyEvent.VK_N];
		// boolean jump = key[KeyEvent.VK_W];
		process(one, two, three, four, five, six, y, n);
	}

}
