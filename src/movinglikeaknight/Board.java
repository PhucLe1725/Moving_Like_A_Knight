package movinglikeaknight;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

import img.ImageLoader;

public class Board extends JPanel implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int boardHeight = 25, boardWidth = 35;
	public static final int blockSize = 25;
	
	private final int FPS = 10;
	private final int delay = 1000 / FPS;
	private BufferedImage img, flag, flagIns, flagRiel, dirt;
	private WindowGame window;
	private boolean hintOn = false;
	
	//the color to draw the board
	private int[][] board = new int[boardHeight][boardWidth];
	protected int levelPoint = 0, minusPoint = 0;
	
	private BufferedImage[] colors = new BufferedImage[4];
	
	private Hinter hinter;
	
	protected boolean isEasterEgg;
	
	private boolean flagged = false, flagInsOn = false;
	
	private int[] flagPos;
	
	private boolean dataDeleteCounting = false;
	private int dataDeleteThreshold = 15;
	
	//looper
	Timer looper;
	
	Horsey horsey;
	
	public Board(Level level, WindowGame window, boolean isEasterEgg) {	
		this.window = window;
		board = level.color;
		this.isEasterEgg = isEasterEgg;
		this.flagPos = window.flagPos;
		
		horsey = new Horsey(level.horsey_x, level.horsey_y, level.horsey_step, level.horsey_teleStep, this);
		horsey.teleporters = level.teleporters;
		
		img = ImageLoader.loadImage("instruction.png");
		flag = ImageLoader.loadImage("flag.png");
		flagIns = ImageLoader.loadImage("flagins.png");
		flagRiel = ImageLoader.loadImage("flagriel.png");
		if(isEasterEgg) {
			colors[0] = ImageLoader.loadImage("grass_0_ee.png");
			colors[1] = ImageLoader.loadImage("grass_1_ee.png");
			colors[2] = ImageLoader.loadImage("grass_2_ee.png");
			colors[3] = ImageLoader.loadImage("grass_3_ee.png");
			dirt = ImageLoader.loadImage("dirt_ee.png");
		} else {
			colors[0] = ImageLoader.loadImage("grass_0.png");
			colors[1] = ImageLoader.loadImage("grass_1.png");
			colors[2] = ImageLoader.loadImage("grass_2.png");
			colors[3] = ImageLoader.loadImage("grass_3.png");
			dirt = ImageLoader.loadImage("dirt.png");
			int point1 = window.getCurrLevel() % 10 > 0 ? window.getCurrLevel() % 10 : 10;
			int point2 = (window.getCurrLevel() + 9) / 10;
			levelPoint = point1 * point2 * 1000 + point2 * 200;
			minusPoint = 1500 + 500 * point2;
		}
		
		looper = new Timer(delay, new GameLooper());
		looper.start();
	}

	private void update() {
		horsey.update();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(isEasterEgg) {
			g.setColor(Color.decode("#8034E3"));
		} else g.setColor(Color.decode("#8A4F34"));
		//fill the window
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(dirt, 25, 25, null);
        
        //fill the board
        for(int row = 0; row < board.length; row++) {
        	for(int col = 0; col < board[0].length; col++) {
        		if(board[row][col] > -1) {
        			g.drawImage(colors[board[row][col]] ,(col + 1) * blockSize, (row + 1) * blockSize, null);
        		}
        	}
        }
        
        horsey.render(g);
        
        if(hintOn) {
        	hinter.display(g);
        }
        //the instruction image
        g.drawImage(img, 900, 0, null);
        
        //draw flag instruction if available
        if(window.getFlags() > 0)
        	g.drawImage(flag, 900, 635, null);
        
        //the current level
        String levelString = isEasterEgg? "Level ???" : "Level " + Integer.toString(window.getCurrLevel());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Georgia", Font.BOLD, 35));
        g.drawString(levelString, 400, 685);
        
        String levelPointString = "Level point: " + Integer.toString(levelPoint);
        String totalPointString = "Total point: " + Integer.toString(window.getTotalPoint());
        
        g.setFont(new Font("Georgia", Font.BOLD, 15));
        
        g.drawString(levelPointString, 700, 665);
        g.drawString(totalPointString, 700, 685);
        
        if(flagInsOn) {
        	g.drawImage(flagIns, 0, 0, null);
        }
        
        if(window.getFlags() > 0) {
        	String flagsLeft = "Flag uses left: " + Integer.toString(window.getFlags());
        	g.drawString(flagsLeft, 25, 675);
        }
        
        if(horsey.winLevel()) {
        	if(flagged && horsey.getX() == flagPos[0] && horsey.getY() == flagPos[1] && Math.random() < 0.34) {
        		window.setFlags(window.getFlags() + 1);
        	}
        	if(window.isVictory()) {
            	window.victory();
            }
        	else window.levelComplete(isEasterEgg);
        }
        
        if(dataDeleteThreshold <= 0) {
        	window.resetData();
        }
	}
	
	public void renderFlag(Graphics g) {
		if(flagged) {
        	g.drawImage(flagRiel ,(flagPos[1] + 1) * blockSize, (flagPos[0] + 1) * blockSize, null);
        }
	}
	
	public void hint() {
		hinter = new Hinter(horsey, board);
		hinter.hints();
		hintOn = true;
		if(!hinter.overFlow)
			levelPoint -= minusPoint;
	}

	public int[][] getBoard(){
		return board;
	}
	public WindowGame getWindow() {
		return window;
	}
	
	class GameLooper implements ActionListener {

		
		public void actionPerformed(ActionEvent e) {
			update();
            repaint();
            
            if(levelPoint > 0)
            	levelPoint -= 5;
            else levelPoint = 0;
            
            if(dataDeleteCounting) {
            	dataDeleteThreshold--;
            }
		}
		
	}

	protected void loadEasterEgg(int id) {
		window.loadEasterEgg(id + 1000);
	}
	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() != KeyEvent.VK_F) {
			flagInsOn = false;
        }
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			hintOn = false;
            horsey.showDirection();
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
        	hintOn = false;
            horsey.showDirectionReverse();
        } else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
        	horsey.chooseDirection();
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
        	hintOn = false;
        	horsey.undo();
        } else if(e.getKeyCode() == KeyEvent.VK_H) {
        	if(hintOn) hintOn = false;
        	else hint();
        } else if(e.getKeyCode() == KeyEvent.VK_F) {
        	if(!flagged && window.getFlags() > 0) {
        		if(flagInsOn) {
        			flagInsOn = false;
        			flagged = true;
        			window.setFlags(window.getFlags() - 1);
        		} else {
        			flagInsOn = true;
        		}
        	}
        } else if(e.getKeyCode() == KeyEvent.VK_D) {
        	dataDeleteCounting = true;
        } else if(e.getKeyCode() == KeyEvent.VK_X) {
        	window.quit();
        }
	}
	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_D) {
        	dataDeleteCounting = false;
        	dataDeleteThreshold = 15;
        }
	}
}
