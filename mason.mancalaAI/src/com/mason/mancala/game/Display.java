package com.mason.mancala.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.mason.mancala.game.input.InputHandler;
import com.mason.mancala.game.input.InputProcesser;

public class Display extends Canvas {

	private static final int WIDTH = 800;
	private static final int TEXT_HEIGHT = 50;
	private static final int HEIGHT = 200;
	private static final int TOTAL_HEIGHT = TEXT_HEIGHT + HEIGHT;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5568929097739915801L;
	private static final String TITLE = "Graph";
	private BufferedImage img;
	private int[] pixels;

	public static int scale;

	private static InputHandler input;

	private InputProcesser process;

	/**
	 * 
	 */
	public Display() {

		this.img = new BufferedImage(WIDTH, TOTAL_HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.pixels = ((DataBufferInt) this.img.getRaster().getDataBuffer()).getData();

		input = new InputHandler();
		process = new InputProcesser();

		addKeyListener(input);
		addMouseListener(input);
		addFocusListener(input);
		addMouseMotionListener(input);

		JFrame frame = new JFrame();
		frame.add(this);
		frame.setTitle(TITLE);
		frame.setLocationRelativeTo(null);
		this.setPreferredSize(new Dimension(WIDTH, TOTAL_HEIGHT));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		frame.pack();

	}

	void process() {
		process.tick(input.key);
	}

	void render(Board board) {

		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.red);
		g.setFont(new Font("times roman", Font.BOLD, 18));

		for (int i = 0; i < WIDTH * TOTAL_HEIGHT; i++) {
			this.pixels[i] = graph(i);

		}
		g.drawImage(this.img, 0, 0, WIDTH, TOTAL_HEIGHT, null);

		drawMarbles(board, g);

		g.dispose();
		bs.show();
	}

	private void drawMarbles(Board board, Graphics g) {

		g.drawString(board.text, 0, g.getFont().getSize());
		g.drawString("Your Move:" + board.playerMove, WIDTH * 5 / 8, g.getFont().getSize());
		if (board.finished)
			g.drawString("winner:" + board.winner, WIDTH * 5 / 8, 2 * g.getFont().getSize());
		for (int i = 0; i < 6; i++) {
			if (board.marbles[i] > 0)
				g.drawString(Integer.toString(board.marbles[i]), (i + 1) * WIDTH / 8 + (WIDTH / 16),
						HEIGHT * 3 / 4 + TEXT_HEIGHT);
		}
		for (int i = 0; i < 6; i++) {
			if (board.marbles[i + 7] > 0)
				g.drawString(Integer.toString(board.marbles[i + 7]), (6 - i) * WIDTH / 8 + (WIDTH / 16),
						HEIGHT / 4 + TEXT_HEIGHT);
		}
		if (board.marbles[6] > 0)
			g.drawString(Integer.toString(board.marbles[6]), WIDTH * 15 / 16, HEIGHT / 2 + TEXT_HEIGHT);
		if (board.marbles[13] > 0)
			g.drawString(Integer.toString(board.marbles[13]), WIDTH / 16, HEIGHT / 2 + TEXT_HEIGHT);
	}

	/**
	 * @param pixel
	 * @return
	 */
	private int graph(int pixel) {

		int y = (pixel - (pixel % WIDTH)) / WIDTH;
		int x = pixel % WIDTH;
		for (int i = 0; i < 7; i++) {
			if (x >= ((i + 1) * WIDTH / 8) - 5 && x <= ((i + 1) * WIDTH / 8) + 5 && y >= TEXT_HEIGHT)
				return Color.black.getRGB();
		}
		if (y >= HEIGHT / 2 - 5 + TEXT_HEIGHT && y <= HEIGHT / 2 + 5 + TEXT_HEIGHT && x >= WIDTH / 8
				&& x <= WIDTH / 8 * 7)
			return Color.black.getRGB();
		// System.out.println("white");
		return Color.WHITE.getRGB();
	}
}
