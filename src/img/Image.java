package img;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import movinglikeaknight.WindowGame;

public class Image extends JPanel implements KeyListener{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WindowGame window;
	private Timer timer;
	
	public Image(WindowGame window){
        timer = new Timer(1000/10, new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		repaint();
			}
        });
        timer.start();
        this.window = window;
	}
	
	

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_X) {
			window.quit();
		}
		
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
