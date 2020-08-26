package com.mason.mancaca.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Display extends Canvas {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 200;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5568929097739915801L;
	private static final String TITLE = "Graph";
	private BufferedImage img;
	private int[] pixels;

	public static int scale;

	/**
	 * 
	 */
	public Display() {

		this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.pixels = ((DataBufferInt) this.img.getRaster().getDataBuffer()).getData();

		JFrame frame = new JFrame();
		frame.add(this);
		frame.setTitle(TITLE);
		frame.setLocationRelativeTo(null);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		frame.pack();

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

		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			this.pixels[i] = graph(i);

		}
		g.drawImage(this.img, 0, 0, WIDTH, HEIGHT, null);

		drawMarbles(board, g);

		g.dispose();
		bs.show();
	}

	private void drawMarbles(Board board, Graphics g) {

		for (int i = 0; i < 6; i++) {
			g.drawString(Integer.toString(board.getMarble(i)), (i + 1) * WIDTH / 8 + (WIDTH / 16), HEIGHT * 3 / 4);
		}
		for (int i = 0; i < 6; i++) {
			g.drawString(Integer.toString(board.getMarble(i + 7)), (6 - i) * WIDTH / 8 + (WIDTH / 16), HEIGHT / 4);
		}
		g.drawString(Integer.toString(board.getMarble(13)), WIDTH / 16, HEIGHT / 2);
		g.drawString(Integer.toString(board.getMarble(6)), WIDTH * 15 / 16, HEIGHT / 2);
	}

	/**
	 * @param pixel
	 * @return
	 */
	private int graph(int pixel) {

		int y = (pixel - (pixel % WIDTH)) / WIDTH;
		int x = pixel % WIDTH;
		for (int i = 0; i < 7; i++) {
			if (x >= ((i + 1) * WIDTH / 8) - 5 && x <= ((i + 1) * WIDTH / 8) + 5)
				return Color.black.getRGB();
		}
		if (y >= HEIGHT / 2 - 5 && y <= HEIGHT / 2 + 5 && x >= WIDTH / 8 && x <= WIDTH / 8 * 7)
			return Color.black.getRGB();
		// System.out.println("white");
		return Color.WHITE.getRGB();
	}
}
