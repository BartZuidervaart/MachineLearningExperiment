package character;

import graphics.Texture;

//a common class, making it easy for me to determine the end position and all its graphics
public class EndingPoint {
	Texture start = new Texture("/ending-point.png");	
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
	public EndingPoint(int x, int y){
		xPos = x;
		yPos = y;
	}
	public void displayEnd(){
			
		start.centerImage(xPos,yPos);
	}
}
