package customlevel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import img.ImageLoader;
import movinglikeaknight.Teleporter;

public class CustomHorsey {
	private int x, y;
	private CustomLevel customLevel;
	
	protected boolean shownDirection = false;
	private boolean choseDirection = false;
	private int currDirection = 0;
	protected int steps = 0, teleSteps = -1;
	protected int currStep = 0, currTeleStep = 0;
	private BufferedImage horseyLeft, horseyRight, smallDot, bigDot;//the imageloaders
	
	protected int[][][] directions;//directions to choose
	
	private Stack<Integer> dirs;
	private Stack<Boolean> isTeles;
	public List<Teleporter> teleporters;
	
	private int[][] board;
	
	CustomHorsey(int x, int y, CustomLevel customLevel){
		this.x = x;
		this.y = y;
		this.customLevel = customLevel;
		board = customLevel.getVerifyBoard();
		dirs = new Stack<>();
		isTeles = new Stack<>();
		horseyLeft = ImageLoader.loadImage("horseyleft.png");
		horseyRight = ImageLoader.loadImage("horseyright.png");
		smallDot = ImageLoader.loadImage("smalldot.png");
		bigDot = ImageLoader.loadImage("bigdot.png");
		teleporters = new ArrayList<Teleporter>();
		
		directions = new int[][][] {
			{
				{-1, 0}, {-2, 0}, {-2, 1}
			},
			{
				{0, 1}, {-1, 1}, {-2, 1}
			},
			{
				{-1, 0}, {-1, 1}, {-1, 2}
			},
			{
				{0, 1}, {0, 2}, {-1, 2}
			},
			{
				{0, 1}, {0, 2}, {1, 2}
			},
			{
				{1, 0}, {1, 1}, {1, 2}
			},
			{
				{0, 1}, {1, 1}, {2, 1}
			},
			{
				{1, 0}, {2, 0}, {2, 1}
			},
			{
				{1, 0}, {2, 0}, {2, -1}
			},
			{
				{0, -1}, {1, -1}, {2, -1}
			},
			{
				{1, 0}, {1, -1}, {1, -2}
			},
			{
				{0, -1}, {0, -2}, {1, -2}
			}, 
			{
				{0, -1}, {0, -2}, {-1, -2}
			},
			{
				{-1, 0}, {-1, -1}, {-1, -2}
			},
			{
				{0, -1}, {-1, -1}, {-2, -1}
			},{
				{-1, 0}, {-2, 0}, {-2, -1}
			}
		};
	}
	
	public void update() {
		if(winLevel()) {
			customLevel.setVerified(true);
			steps = -1;
			teleSteps = 0;
		}
	}
	
	public void render(Graphics g) {
		for(Teleporter teleporter : teleporters) {
        	teleporter.render(g);
        }
		if(currDirection > 7)
			g.drawImage(horseyLeft, (y + 1) * CustomLevel.blockSize, (x + 1) * CustomLevel.blockSize, null);
		else g.drawImage(horseyRight, (y + 1) * CustomLevel.blockSize, (x + 1) * CustomLevel.blockSize, null);
		if(shownDirection) {
			for(int i = 0; i < 2; i++) {
				g.drawImage(smallDot ,(y + directions[currDirection][i][1] + 1) * CustomLevel.blockSize, (x + directions[currDirection][i][0] + 1) * CustomLevel.blockSize, null);
			}
			g.drawImage(bigDot, (y + directions[currDirection][2][1] + 1) * CustomLevel.blockSize, (x + directions[currDirection][2][0] + 1) * CustomLevel.blockSize, null);
		} else if(choseDirection) {
			choseDirection = false;
			dirs.push(currDirection);
			board[x][y]--;
			for(int i = 0; i < 2; i++) {
				board[x + directions[currDirection][i][0]][y + directions[currDirection][i][1]]--;
			}
			y = y + directions[currDirection][2][1];
			x = x + directions[currDirection][2][0];
			isTeles.push(teleport());//push and call the function if there is teleport
			currStep++;
		}
	}
	
	public boolean winLevel() {
		if(3 * (steps - currStep) + teleSteps - currTeleStep == 0)
			return true;
		else return false;
	}
	
	public void showDirection() {
		if(!shownDirection) {
			shownDirection = true;
			currDirection = 0;
		}
		currDirection = (currDirection + 1) % 16;
		int trial = 16;
		while(trial --> 0) {
			int count = 0;
			for(int i = 0; i < 3; i++) {
				if(x + directions[currDirection][i][0] < 0 || y + directions[currDirection][i][1] < 0) break;
				
				if(board[x + directions[currDirection][i][0]][y + directions[currDirection][i][1]] > 0) {
					count++;
				}
			}
			if(count == 3) {
				break;
			} else currDirection = (currDirection + 1) % 16;
		}
		if(trial < 0) {
			//don't show direction if there is no possible move
			shownDirection = false;
		}
	}
	
	public void showDirectionReverse() {
		if(!shownDirection) {
			shownDirection = true;
			currDirection = 0;
		}
		currDirection = (currDirection + 15) % 16;
		int trial = 16;
		while(trial --> 0) {
			int count = 0;
			for(int i = 0; i < 3; i++) {
				if(x + directions[currDirection][i][0] < 0 || y + directions[currDirection][i][1] < 0) break;
				
				if(board[x + directions[currDirection][i][0]][y + directions[currDirection][i][1]] > 0) {
					count++;
				}
			}
			if(count == 3) {
				break;
			} else currDirection = (currDirection + 15) % 16;
		}
		if(trial < 0) {
			//don't show direction if there is no possible move
			shownDirection = false;
		}
	}
	
	public void chooseDirection() {
		if(shownDirection) {
			shownDirection = false;
			choseDirection = true;
			if(Math.random() < 0.0075) 
				customLevel.getWindow().getMusicPlayer().play("move_1.wav");
			else customLevel.getWindow().getMusicPlayer().play("move_2.wav");
		}
	}
	
	public void undo() {
		if(!dirs.empty()) {
			int lastDir = dirs.pop();
			boolean isTele = isTeles.pop();
			if(isTele) { //do this if the last move was teleport
				for(Teleporter teleporter: teleporters) {
					teleporter.undoTele(this);
				}
				board[x][y]++;
				currTeleStep--;
			}
			x = x - directions[lastDir][2][0];
			y = y - directions[lastDir][2][1];
			board[x][y]++;
			for(int i = 0; i < 2; i++) {
				board[x + directions[lastDir][i][0]][y + directions[lastDir][i][1]]++;
			}
			shownDirection = false;
			currStep--;
		}
	}
	
	public boolean teleport() {
		for(Teleporter teleporter : teleporters) {
			if(teleporter.startX == x && teleporter.startY == y && board[teleporter.endX][teleporter.endY] > 0) {
				board[teleporter.startX][teleporter.startY]--;
				x = teleporter.endX;
				y = teleporter.endY;
				currTeleStep++;
				return true;
			}
		}
		return false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
}
