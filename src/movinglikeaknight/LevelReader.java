package movinglikeaknight;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import customlevel.CustomLevel;

public class LevelReader {
	File file;
	
	public LevelReader(){
		
	}
	
	private void read(String str) {
		file = new File(System.getProperty("user.dir") + "\\data\\levels\\mainlevels\\" + str);
	}
	
	private void readCustomLevel(String str) {
		file = new File(System.getProperty("user.dir") + "\\data\\levels\\customlevels\\" + str + ".txt");
	}
	
	public Level readLevel(int n){
		Level level = new Level();
		read("level_" + Integer.toString(n) + ".txt");
		try (Scanner sc = new Scanner(file)) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			int step = sc.nextInt();
			int teleStep = sc.nextInt();
			level = new Level(x, y, step, teleStep);
			while(sc.hasNextLine()) {
				String str = sc.nextLine();
				if (str.isEmpty()) {
	                continue;
	            }
				if(str.equals("tele")){
					break;
				}
				String[] inf = str.split(",");
				int[] info = new int[4];
				for(int i = 0; i < 4; i++) {
					info[i] = Integer.parseInt(inf[i]);
				}
				for(int i = info[1]; i < info[2]; i++) {
					level.color[info[0]][i] = info[3];
				}
			}
			while(sc.hasNextLine()) {
				String str = sc.nextLine();
				if (str.isEmpty()) {
	                break;
	            }
				if(str.equals("tele")) {
					continue;
				}
				String[] inf = str.split(",");
				int[] info = new int[4];
				for(int i = 0; i < 4; i++) {
					info[i] = Integer.parseInt(inf[i]);
				}
				level.addTele(info[0], info[1], info[2], info[3]);
			}
			sc.close();
		} catch (FileNotFoundException e) {
            return null;
        }
		return level;
	}
	
	public void readData(WindowGame window) {
		read("userdata.txt");
		try {
			if(file.createNewFile()) {
				FileWriter writer = new FileWriter(file, true);
				writer.write("0 0 0");
				writer.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int[] data = new int[3];
		try (Scanner sc = new Scanner(file)){
			for(int i = 0; i < 3; i++) {
				data[i] = sc.nextInt();
			}
			window.setCurrLevel(data[0]);
			window.setFlags(data[1]);
			window.setTotalPoint(data[2]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void updateData(int currLevel, int flags, int totalPoint) {
		read("userdata.txt");
		try {
            FileWriter writer
                = new FileWriter(file);
            writer.write("");
            writer.write(Integer.toString(currLevel) + " " + Integer.toString(flags) + " " + Integer.toString(totalPoint));
            writer.close();
        }
        catch (IOException e) {
        	//nothing
        }
	}
	
	public int[] readFlagPos(int num) {
		read("flagpos.txt");
		int[] arr = new int[2];
		try (Scanner sc = new Scanner(file)){
			while(num --> 0) {
				sc.nextLine();
			}
			arr[0] = sc.nextInt();
			arr[1] = sc.nextInt();
		} catch (Exception e) {
			return new int[] {0, 0};
		}
		return arr;
	}
	
	public void saveCustomLevel(StringBuilder levelName, CustomLevel customLevel){
		readCustomLevel(levelName.toString());
		try {
			//if the name exists
			if(!file.createNewFile()) {
				customLevel.setRepeatedName();
				return;
			}
			
			
			try {
	            FileWriter writer
	                = new FileWriter(file);
	            int sumBoard = -1;
	    		for(int i = 0; i < customLevel.getBoard().length; i++) {
	    			for(int j = 0; j < customLevel.getBoard()[0].length; j++) {
	    				if(customLevel.getBoard()[i][j] > 0)
	    					sumBoard += customLevel.getBoard()[i][j];
	    			}
	    		}
	    		
	    		int steps = sumBoard / 3;
				int teleSteps = sumBoard % 3;
	            writer.write(Integer.toString(customLevel.getX()) + " " + Integer.toString(customLevel.getY()) + " " + Integer.toString(steps) + " " + Integer.toString(teleSteps) + "\n");
	            
	            int num = 0;
	            for(int i = 0; i < customLevel.getBoard().length; i++) {
	            	int start = 0, end = 0;
	    			for(int j = 0; j < customLevel.getBoard()[0].length; j++) {
	    				if(customLevel.getBoard()[i][j] > 0) {
	    					num = customLevel.getBoard()[i][j];
	    					start = j;
	    					while(++j < customLevel.getBoard()[0].length) {
	    						if(customLevel.getBoard()[i][j] != num) {
	    							end = j;
	    							break;
	    						}
	    					}
	    					writer.write(Integer.toString(i) + "," + Integer.toString(start) + "," + Integer.toString(end) + "," + Integer.toString(num) + "\n");
	    					j--;
	    				}
	    			}
	    		}
	            
	            if(customLevel.teleporters.size() != 0) {
	            	writer.write("tele\n");
	            	for(Teleporter t : customLevel.teleporters) {
	            		writer.write(Integer.toString(t.startX) + "," + Integer.toString(t.startY) + "," + Integer.toString(t.endX) + "," + Integer.toString(t.endY) + "\n");
	            	}
	            }
	            
	            writer.close();
	        }
	        catch (IOException e) {
	        	//nothing
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
