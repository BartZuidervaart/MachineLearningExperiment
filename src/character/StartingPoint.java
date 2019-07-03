package character;

import graphics.Texture;

//a common class, making it easy for me to determine the start position and all its graphics
public class StartingPoint {
	Texture start = new Texture("/starting-point.png");	
	public int xPos, yPos;
	
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	public StartingPoint(int x, int y){
		xPos = x;
		yPos = y;
	}
	public void displayStart(){
			
		start.centerImage(xPos,yPos);
	}
}
