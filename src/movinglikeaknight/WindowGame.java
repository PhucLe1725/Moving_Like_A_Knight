package movinglikeaknight;


import java.awt.Component;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import customlevel.CustomLevel;
import img.*;

/**
 * The main window of the game
 */

public class WindowGame {
	public static final int WIDTH = 1055, HEIGHT = 730;
	
	private JFrame window;
	private Board board;
	private Level level;
	private Image title;
	private int currLevel = 0, finalLevel = 100, flags = 0;
	public int[] flagPos;
	private int totalPoint = 0;
	private LevelReader lvlReader;
	private MusicPlayer musicPlayer;
	private CustomLevel customLevel;

	public WindowGame(){
		window = new JFrame("movinglikeaknight");
		window.setSize(WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        
        lvlReader = new LevelReader();
        
        lvlReader.readData(this);
        
        flagPos = lvlReader.readFlagPos(currLevel);
        
        musicPlayer = new MusicPlayer();
        
        levelCreate(currLevel);
        title = new Title(this);
        
        window.addKeyListener(title);
        window.setVisible(true);
        window.add(title);
        
        musicPlayer.loop("titlemusic.wav");
	}
	
	public void startGame() {
		window.remove(title);
		window.removeKeyListener(title);
		
		board = new Board(level, this, false);
		
		window.add(board);
		window.addKeyListener(board);
		window.revalidate();
		musicPlayer.loop("nhacvip.wav");
	}
	
	public void levelComplete(boolean isEasterEgg) {
		if(isEasterEgg) flags++;
		totalPoint += board.levelPoint;
		
		window.remove(board);
		window.removeKeyListener(board);
		
		currLevel++;
		levelCreate(currLevel);
		
		lvlReader.updateData(currLevel, flags, totalPoint);
		
		flagPos = lvlReader.readFlagPos(currLevel);
		board = new Board(level, this, false);
		title = new WinLevel(this, isEasterEgg);
		
		musicPlayer.stop("nhacvip.wav");
		
		window.add(title);
		window.addKeyListener(title);
		window.revalidate();
	}
	
	public void loadEasterEgg(int id) {
		currLevel--;
		window.remove(board);
		window.removeKeyListener(board);
		levelCreate(id);
		board = new Board(level, this, true);
		
		musicPlayer.stop("nhacvip.wav");
		
		window.add(board);
		window.addKeyListener(board);
		window.revalidate();
	}
	
	public void victory() {
		window.remove(board);
		window.removeKeyListener(board);
		title = new Victory(this);
		window.add(title);
		window.revalidate();
	}
	
	public void resetData() {
		window.remove(board);
		window.removeKeyListener(board);
		
		lvlReader.updateData(0, 0, 0);
		lvlReader.readData(this);
		
		levelCreate(currLevel);
		
		flagPos = lvlReader.readFlagPos(currLevel);
		board = new Board(level, this, false);
		title = new ResetScreen(this);
		
		window.add(title);
		window.addKeyListener(title);
		window.revalidate();
	}
	
	public static void main(String[] args) {
        new WindowGame();
    }
	
	public void levelCreate(int currLevel) {
		level = lvlReader.readLevel(currLevel);
		//stage 5: 3-times tiles
		//stage 6: 2-times tiles + teleporters
		//stage 7: stage 6 but teleporters may be skipped
		//stage 8: 3-times tiles + teleporters
		//stage 9: stage 8 but teleporters may be skipped
		//stage 10: ultimate bosses
	} 
	
	public void moreAndMore() {
		Component[] components = window.getContentPane().getComponents();
	    for (Component component : components) {
	        window.remove(component);
	        if (component instanceof KeyListener) {
	            window.removeKeyListener((KeyListener) component);
	        }
	    }
		
		title = new MidScreen(this);
		
		window.add(title);
		window.addKeyListener(title);
		window.revalidate();
	}
	
	public void returnToMenu() {
		window.remove(title);
		window.removeKeyListener(title);
		
		title = new Title(this);
		
		window.add(title);
		window.addKeyListener(title);
		window.revalidate();
	}
	
	public void enterCustomLevel() {
		window.remove(title);
		window.removeKeyListener(title);
		
		customLevel = new CustomLevel(this);
		
		window.add(customLevel);
		window.addKeyListener(customLevel);
		window.revalidate();
	}
	
	public int getCurrLevel() {
		return currLevel;
	}
	
	public void setCurrLevel(int num) {
		if(num >= 0) {
			currLevel = num;
		}
	}
	
	public int getFlags() {
		return flags;
	}
	
	public void setFlags(int num) {
		if(num >= 0) {
			flags = num;
		}
	}
	
	public boolean isVictory() {
		return currLevel > finalLevel;
	}
	
	public int getTotalPoint() {
		return totalPoint;
	}
	
	public void setTotalPoint(int num) {
		if(num >= 0) {
			totalPoint = num;
		}
	}
	
	public void quit() {
	    window.dispose();
	    System.exit(0);
	}
	
	public JFrame getWindow() {
		return window;
	}
	
	public MusicPlayer getMusicPlayer() {
		return musicPlayer;
	}
}
