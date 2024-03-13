package customlevel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import img.ImageLoader;
import movinglikeaknight.LevelReader;
import movinglikeaknight.Teleporter;
import movinglikeaknight.WindowGame;

public class CustomLevel extends JPanel implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int boardHeight = 25, boardWidth = 35;
	public static final int blockSize = 25;
	
	private final int FPS = 10;
	private final int delay = 1000 / FPS;
	private BufferedImage img, dirt, cursor, save;
	
	private int x = 0, y = 0;
	private int verifyHorseyX = 0, verifyHorseyY = 0;
	
	private WindowGame window;
	private LevelReader levelReader;
	
	private int[][] board = new int[boardHeight][boardWidth];
	private int[][] verifyBoard = new int[boardHeight][boardWidth];
	
	private BufferedImage[] colors = new BufferedImage[4];
	
	private Timer looper;
	private CustomHorsey horsey;
	private boolean putHorsey = false;
	
	private Teleporter tempTeleporter;
	public List<Teleporter> teleporters;
	private boolean verifying = false;
	private boolean verified = false;
	private boolean saving = false;
	private boolean repeatedName = false;
	private StringBuilder levelName = new StringBuilder();

	private int timeToPutOutTele = 0;
	
	public CustomLevel(WindowGame window) {
		this.window = window;
		for(int[] arr : board) {
			Arrays.fill(arr, -1);
		}
		
		img = ImageLoader.loadImage("ins_custom.png");
		colors[0] = ImageLoader.loadImage("grass_0.png");
		colors[1] = ImageLoader.loadImage("grass_1.png");
		colors[2] = ImageLoader.loadImage("grass_2.png");
		colors[3] = ImageLoader.loadImage("grass_3.png");
		dirt = ImageLoader.loadImage("dirt.png");
		cursor = ImageLoader.loadImage("custom_cursor.png");
		save = ImageLoader.loadImage("savelevelscreen.png");
		horsey = new CustomHorsey(0, 0, this);
		this.teleporters = horsey.teleporters;
		
		levelReader = new LevelReader();
		
		looper = new Timer(delay, new GameLooper());
		looper.start();
	}
	
	public void update() {
		horsey.update();
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		
		if(verified) {
			g.drawImage(save, 0, 0, null);
			if(saving) {
				g.setColor(Color.WHITE);
		        g.setFont(new Font("Georgia", Font.BOLD, 35));
		        g.drawString("Level name: " + levelName.toString(), 100, 200);
		        if(repeatedName)
		        	g.drawString("This name is used. Please choose another name.", 100, 250);
			}
			return;
		}
		
		g.setColor(Color.decode("#8A4F34"));
		//fill the window
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(dirt, 25, 25, null);
        
        if(verifying) {
        	drawVerifyBoard(g);
        } else {
        	drawBoard(g);
        }
        
        
        if(timeToPutOutTele > 0) {
        	tempTeleporter.endX = x;
        	tempTeleporter.endY = y;
        	g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 15));
            if(timeToPutOutTele == 1)
        	g.drawString("Press Enter to put the out teleporter", 400, 20);
            else g.drawString("Teleporter can't be placed above other teleporters", 350, 20);
        }
        
		for(Teleporter teleporter : teleporters) {
        	teleporter.render(g);
        }
		
        if(putHorsey) {
        	horsey.render(g);
        }
        
        g.drawImage(img, 900, 0, null);
        
        if(!verifying)
        	g.drawImage(cursor, (y + 1) * blockSize, (x + 1) * blockSize, null);
        
        g.setColor(Color.WHITE);
        String levelString = "Custom Level";
        g.setFont(new Font("Georgia", Font.BOLD, 35));
        g.drawString(levelString, 400, 685);
	}
	
	public void drawBoard(Graphics g) {
		for(int row = 0; row < board.length; row++) {
        	for(int col = 0; col < board[0].length; col++) {
        		if(board[row][col] > -1) {
        			g.drawImage(colors[board[row][col]] ,(col + 1) * blockSize, (row + 1) * blockSize, null);
        		}
        	}
        }
	}
	
	public void drawVerifyBoard(Graphics g) {
		for(int row = 0; row < verifyBoard.length; row++) {
        	for(int col = 0; col < verifyBoard[0].length; col++) {
        		if(board[row][col] > -1) {
        			g.drawImage(colors[verifyBoard[row][col]] ,(col + 1) * blockSize, (row + 1) * blockSize, null);
        		}
        	}
        }
	}

	public void verify() {
		int sumBoard = -1;
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				verifyBoard[i][j] = board[i][j];
				if(board[i][j] > 0)
					sumBoard += board[i][j];
			}
		}
		
		verifying = !verifying;
		if(verifying) {
			verifyHorseyX = horsey.getX();
			verifyHorseyY = horsey.getY();
			img = ImageLoader.loadImage("ins_verify.png");
			horsey.steps = sumBoard / 3;
			horsey.teleSteps = sumBoard % 3;
			horsey.currStep = 0;
			horsey.currTeleStep = 0;
		} else {
			horsey.shownDirection = false;
			horsey.setX(verifyHorseyX);
			horsey.setY(verifyHorseyY);
			img = ImageLoader.loadImage("ins_custom.png");
		}
	}
	
	private void putInTeleporter() {
		if(timeToPutOutTele == 1) return;
		for(int i = 0; i < teleporters.size(); i++) {
			if(teleporters.get(i).startX == x && teleporters.get(i).startY == y || teleporters.get(i).endX == x && teleporters.get(i).endY == y) {
				timeToPutOutTele = 2;
				return;
			}
		}
		tempTeleporter = new Teleporter(x, y, 0, 0, teleporters.size() + 1);
		teleporters.add(tempTeleporter);
		x = 0;
		y = 0;
		timeToPutOutTele  = 1;
	}
	
	private void putOutTeleporter() {
		if(timeToPutOutTele == 0) return;
		for(int i = 0; i < teleporters.size() - 1; i++) {
			if(teleporters.get(i).startX == x && teleporters.get(i).startY == y || teleporters.get(i).endX == x && teleporters.get(i).endY == y) {
				timeToPutOutTele = 2;
				return;
			}
		}
		if(tempTeleporter.startX == x && tempTeleporter.startY == y) {
			timeToPutOutTele = 2;
			return;
		}
		tempTeleporter = new Teleporter(0, 0, 0, 0, 0);
		timeToPutOutTele  = 0;
	}
	
	private void deleteTeleporter() {
		for(int i = 0; i < teleporters.size(); i++) {
			if(teleporters.get(i).startX == x && teleporters.get(i).startY == y || teleporters.get(i).endX == x && teleporters.get(i).endY == y) {
				teleporters.remove(i);
			}
		}
		for(int i = 0; i < teleporters.size(); i++) {
			teleporters.get(i).count = i + 1;
		}
	}

	class GameLooper implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			update();
            repaint();
		}
		
	}
	
	public int[][] getVerifyBoard() {
		return verifyBoard;
	}
	
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
	public void setRepeatedName() {
		repeatedName = true;
	}
	
	public WindowGame getWindow() {
		return window;
	}
	
	public int[][] getBoard(){
		return board;
	}
	
	public int getX() {
		return verifyHorseyX;
	}
	
	public int getY() {
		return verifyHorseyY;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(saving) {
			keySaving(e);
			return;
		}
		
		if(verified) {
			keyVerified(e);
			return;
		}
		if(verifying) {
			keyVerifying(e);
		} else keyCustoming(e);
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void keyVerifying(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
            horsey.showDirection();
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            horsey.showDirectionReverse();
        } else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
        	horsey.chooseDirection();
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
        	horsey.undo();
        } else if(e.getKeyCode() == KeyEvent.VK_V) {
        	verify();
        }
	}
	
	private void keyCustoming(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(x > 0) {
				x--;
			}
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
        	if(x < 24) {
				x++;
			}
        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
        	if(y < 34) {
				y++;
			}
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
        	if(y > 0) {
				y--;
			}
        } else if(e.getKeyCode() == KeyEvent.VK_0) {
        	board[x][y] = -1;
        } else if(e.getKeyCode() == KeyEvent.VK_1) {
        	board[x][y] = 1;
        } else if(e.getKeyCode() == KeyEvent.VK_2) {
        	board[x][y] = 2;
        } else if(e.getKeyCode() == KeyEvent.VK_3) {
        	board[x][y] = 3;
        } else if(e.getKeyCode() == KeyEvent.VK_K) {
        	horsey.setX(x);
        	horsey.setY(y);
        	putHorsey = true;
        } else if(e.getKeyCode() == KeyEvent.VK_V) {
        	if(putHorsey) verify();
        } else if(e.getKeyCode() == KeyEvent.VK_T) {
        	putInTeleporter();
        } else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
        	putOutTeleporter();
        } else if(e.getKeyCode() == KeyEvent.VK_E) {
        	if(timeToPutOutTele == 0)
        	deleteTeleporter();
        }
	}
	
	private void keyVerified(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_S) {
        	saving = true;
        } else if(e.getKeyCode() == KeyEvent.VK_E) {
        	verified = false;
        	verify();
        }
	}
	
	private void keySaving(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			levelReader.saveCustomLevel(levelName, this);
        	window.moreAndMore();
        } else if((e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z)
                || (e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9 
                || e.getKeyCode() == KeyEvent.VK_SPACE)) {
        	char keyChar = e.getKeyChar();
        	if(levelName.length() < 21)
        		levelName.append(keyChar);
        } else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
        	if(!levelName.isEmpty())
        		levelName.deleteCharAt(levelName.length() - 1);
        }
	}

}
