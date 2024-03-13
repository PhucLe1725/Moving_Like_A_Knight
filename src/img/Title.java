package img;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import movinglikeaknight.WindowGame;

public class Title extends Image {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int continueState = 0, replayState = 1, moreAndMoreState = 2;
	private int currState = 0;

	private BufferedImage img;

	public Title(WindowGame window){
		super(window);
        img = ImageLoader.loadImage("title.png");
	}

	
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
        g.setFont(new Font("Georgia", Font.BOLD, 40));
        String[] str = new String[] {
        		"CONTINUE " + Integer.toString(window.getCurrLevel()),
        		"REPLAY",
        		"MORE"
        };
        for(int i = 0; i < 3; i++) {
        	if(i == currState) {
        		g.setColor(Color.WHITE);
        	} else g.setColor(Color.GRAY);
        	g.drawString(str[i], 400, 475 + 50*i);
        }
	}

	private void progress() {
		if(currState == continueState) {
			window.getMusicPlayer().stop("titlemusic.wav");
            window.startGame();
		} else if (currState == replayState) {
			
		} else if (currState == moreAndMoreState) {
			window.moreAndMore();
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
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
        	currState = (currState + 1) % 3;
        } else if(e.getKeyCode() == KeyEvent.VK_UP) {
        	currState = (currState + 2) % 3;
        } 
	}

	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
