package img;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import movinglikeaknight.WindowGame;

public class Victory extends Image {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage img;
	public Victory(WindowGame window) {
		super(window);
		img = ImageLoader.loadImage("victory.png");
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
