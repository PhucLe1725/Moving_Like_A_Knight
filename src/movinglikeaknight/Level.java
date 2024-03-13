package movinglikeaknight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level {
	private final int boardHeight = 25, boardWidth = 35;
	public int[][] color = new int[boardHeight][boardWidth];
	public int horsey_x, horsey_y, horsey_step, horsey_teleStep;
	public List<Teleporter> teleporters;
	private int count = 1;
	
	Level(int x, int y, int step, int teleStep){
		horsey_x = x;
		horsey_y = y;
		horsey_step = step;
		horsey_teleStep = teleStep;
		teleporters = new ArrayList<Teleporter>();
		for(int[] arr : color) {
			Arrays.fill(arr, -1);
		}
	}
	
	Level(){
		
	}
	
	public void addTele(int x1, int y1, int x2, int y2) {
		teleporters.add(new Teleporter(x1, y1, x2, y2, count));
		count++;
	}
}
