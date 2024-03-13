package movinglikeaknight;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import img.ImageLoader;

public class Hinter {
	private int x, y, steps, teleSteps;
	private Horsey horsey;
	private BufferedImage hintDot, undo, skillIssue;
	private int[][] board;
	private int[][][] directions;
	protected boolean overFlow = false;
	private int id;
	
	Hinter(Horsey horsey, int[][] boards){
		this.x = horsey.getX();
		this.y = horsey.getY();
		this.horsey = horsey;
		this.steps = horsey.steps - horsey.currStep;
		this.teleSteps = horsey.teleSteps - horsey.currTeleStep;
		this.directions = horsey.directions;
		this.board = boards.clone();
		hintDot = ImageLoader.loadImage("hintdot.png");
		undo = ImageLoader.loadImage("undo.png");
		skillIssue = ImageLoader.loadImage("skillissue.png");
	}
	
	public void hints() {
		horsey.shownDirection = false;
		if(steps > 21) {
			overFlow = true;
			return;
		}
		id = 0;
		for(; id < 16; id++) {
			if(isPossible(id)) {
				break;
			}
		}
	}
	
	public void display(Graphics g) {
		if(overFlow) {
			g.drawImage(skillIssue, 260, 220, null);
			return;
		}
		if(id == 16) {
			g.drawImage(undo, (horsey.getY() + 1) * Board.blockSize, (horsey.getX() + 1) * Board.blockSize, null);
			return;
		}
		for(int i = 0; i < 2; i++) {
			g.drawImage(hintDot ,(horsey.getY() + directions[id][i][1] + 1) * Board.blockSize, (horsey.getX() + directions[id][i][0] + 1) * Board.blockSize, null);
		}
		g.drawImage(hintDot, (horsey.getY() + directions[id][2][1] + 1) * Board.blockSize, (horsey.getX() + directions[id][2][0] + 1) * Board.blockSize, null);
	}
	
	private boolean isPossible(int idx) {
		boolean res = false;
		boolean tele = false;
		if(steps == 0 && teleSteps == 0) {
			return true;
		}
		int count = 0;
		for(int i = 0; i < 3; i++) {
			if(board[x + directions[idx][i][0]][y + directions[idx][i][1]] > 0) {
				count++;
			}
		}
		if(count < 3) return false;
		steps--;
		board[x][y]--;
		for(int i = 0; i < 2; i++) {
			board[x + directions[idx][i][0]][y + directions[idx][i][1]]--;
		}
		y = y + directions[idx][2][1];
		x = x + directions[idx][2][0];
		for(Teleporter teleporter : horsey.teleporters) {
			if(teleporter.startX == x && teleporter.startY == y && board[teleporter.endX][teleporter.endY] > 0) {
				board[teleporter.startX][teleporter.startY]--;
				x = teleporter.endX;
				y = teleporter.endY;
				teleSteps--;
				tele = true;
			}
		}
		for(int i = 0; i < 16; i++) {
			if(isPossible(i)) {
				res = true;
				break;
			}
		}
		steps++;
		if(tele) {
			for(Teleporter teleporter: horsey.teleporters) {
				if(x == teleporter.endX && y == teleporter.endY) {
					x = teleporter.startX;
					y = teleporter.startY;
					board[x][y]++;
					teleSteps++;
				}
			}
		}
		x = x - directions[idx][2][0];
		y = y - directions[idx][2][1];
		board[x][y]++;
		for(int i = 0; i < 2; i++) {
			board[x + directions[idx][i][0]][y + directions[idx][i][1]]++;
		}
		return res;
	}
	
}
