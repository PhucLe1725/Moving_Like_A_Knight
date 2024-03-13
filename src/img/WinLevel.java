package img;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import movinglikeaknight.WindowGame;

public class WinLevel extends Image {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage img;

	public WinLevel(WindowGame window, boolean isEasterEgg) {
		super(window);
		if(isEasterEgg) {
			img = ImageLoader.loadImage("ggeasteregg.png");
			return;
		}
		double rand = Math.random();
		if(rand < 0.04)
			img = ImageLoader.loadImage("easteregg.png");
		else
			img = ImageLoader.loadImage("gg.png");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		super.keyTyped(e);
		if(e.getKeyChar() == KeyEvent.VK_SPACE) {
            window.startGame();
        }
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		super.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		super.keyReleased(e);
	}

}
