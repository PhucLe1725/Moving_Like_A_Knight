package img;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import movinglikeaknight.WindowGame;

public class MidScreen extends Image {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage img, mouse;
	
	private final int publicState = 0, savedState = 1, customState = 2;
	private int currState = 0;

	public MidScreen(WindowGame window){
		super(window);
        img = ImageLoader.loadImage("midtitle.png");
        mouse = ImageLoader.loadImage("mouse.png");
	}

	
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
		g.drawImage(mouse, 103 + 393 * currState, 150, null);
	}

	public void progress() {
		if(currState == publicState) {
			
		} else if (currState == savedState) {
			
		} else if (currState == customState) {
			window.enterCustomLevel();
		}
	}
	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyChar() == KeyEvent.VK_ENTER) {
			progress();
        }
	}

	
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
        	currState = (currState + 1) % 3;
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
        	currState = (currState + 2) % 3;
        } else if(e.getKeyCode() == KeyEvent.VK_E) {
        	window.returnToMenu();
        }
	}

	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
