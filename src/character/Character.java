package character;

import java.util.ArrayList; //Makes ArrayLists possible
import Math.My_Math; //My own math functions
import graphics.Texture; //Needed to make graphics appear on the screen and to keep track of the color underneath the characters to determine if they hit a wall.

public class Character {
	//NEEDED VARIABLES
	My_Math m = new My_Math(); //Needed to use my own math functions
	//Texture tex = new Texture("res/arrow.png"); //used for a controlable demo character, no longer needed
	Texture line = new Texture("/pixel.jpg"); //the image of a single pixel, needed to make lines.
	public float speed = 0.02f; //determines how fast the characters move in a single movement
	//public float rotationSpeed = (float)Math.PI/40; //determines how much the demo character moves at a time, no longer needed
	public float xPos, yPos; //x and y position of the character
	public float prevXPos, prevYPos; //previous x and y position of the character. Needed to make the character reappear before a wall instead of inside it at the next cycle
	public float rotation; //speaks for itself 
	//public float size = 5; //determines the size of the demo character, no longer needed
	ArrayList<Float[]> trackedPosition = new ArrayList<Float[]>(); //keeps track of positions traveled during a path
	ArrayList<Float[]> trackedTruePosition = new ArrayList<Float[]>(); //keeps track of positions traveled during the true path
	
	ArrayList<Integer> pathStartPosition = new ArrayList<Integer>(); //keeps track of the positions in a path that need to be determined as start position
	ArrayList<Integer> pathEndPosition = new ArrayList<Integer>(); //keeps track of the positions in a path that need to be determined as end position
	
	ArrayList<Integer> truePathStartPosition = new ArrayList<Integer>(); //keeps track of the positions in the true path that need to be determined as start position
	ArrayList<Integer> truePathEndPosition = new ArrayList<Integer>(); //keeps track of the positions in the true path that need to be determined as end position
		
	//MOVEMENT
	
	/*// Needed for the demo character, no longer needed
	public void rotateLeft(){ //pas de rotatie aan
		rotation = rotation-rotationSpeed;
	}
	public void rotateRight(){ //pas de rotatie aan
		rotation = rotation+rotationSpeed;
	}*/
	public void moveForward(){ //moves the demo character towards the direction it is facing
		prevXPos = xPos;
		prevYPos = yPos;
		xPos = border(xPos+(float)Math.sin(rotation)*speed);
		yPos = border(yPos+(float)Math.cos(rotation)*speed);
		Float[] p = {xPos, yPos};
		trackedPosition.add(p);
	}
	public void moveBackward(){ //moves the demo character towards the opposite direction it is facing
		prevXPos = xPos;
		prevYPos = yPos;
		xPos = border(xPos-(float)Math.sin(rotation)*speed);
		yPos = (border(yPos-(float)Math.cos(rotation)*speed));
		Float[] p = {xPos, yPos};
		trackedPosition.add(p);
	}
	public void rotateForward(float r){ //moves the fired characters in a direction determined by 'r'
		prevXPos = xPos;
		prevYPos = yPos;
		rotation = rotation+r;
		xPos = border(xPos+(float)Math.sin(rotation)*speed);
		yPos = border(yPos+(float)Math.cos(rotation)*speed);
		Float[] p = {xPos, yPos};
		trackedPosition.add(p);
	}
	public void rotateBackward(float r){ //moves the fired characters in the opposite direction determined by 'r' 
		prevXPos = xPos;
		prevYPos = yPos;
		rotation = rotation+r;
		xPos = border(xPos-(float)Math.sin(rotation)*speed);
		yPos = border(yPos-(float)Math.cos(rotation)*speed);
		Float[] p = {xPos, yPos};
		trackedPosition.add(p);
	}
	
	//FILTERS
	private float border(float n){ //makes sure the characters can't escape the screen
		if(n > 1.0f){
			n = 1.0f;
		}
		if(n < -1.0f){
			n = -1.0f;
		}
		return n;
	}
	
	//MAIN CLASS FUNCTION
	public Character(float xPos, float yPos, float r){ //needed for the character functions, the variables are no longer needed only for the demo character
		this.xPos = xPos;
		this.yPos = yPos;
		this.rotation = r;
	}
	
	//RENDERS THE CHARACTER ON THE SCREEN
	//zonder deze functie bestaat het karakter nogsteeds maar is hij niet te zien op het scherm.
	//public void renderCharacter(){
		//tex.rotateImage(xPos, yPos,rotation);
	//}
	
	//renders the paths the fired characters 
	public void renderPath(){	
		for(int i = 0; i < trackedPosition.size(); i++){
			if(i != 0){
				line.drawLine(trackedPosition.get(i-1)[0], trackedPosition.get(i-1)[1], trackedPosition.get(i)[0], trackedPosition.get(i)[1]); //draws thin lines
			}
		}
	}
	
	//gets rid of existing paths
	public void cleanPath(){
		trackedPosition.removeAll(trackedPosition);
	}
	
	//needed at the start of the true path to give the start position a path location as well
	public void setPathStartPosition(float x, float y){
		truePathStartPosition.add(trackedTruePosition.size()-1);
		Float[] p = {x, y};
		trackedPosition.add(p);
		truePathEndPosition.add(trackedTruePosition.size()-1);
	}
	
	//needed to determine where a path starts
	public void setPathStart(){
		pathStartPosition.add(trackedPosition.size()-1);
	}
	
	//needed to determine where a path ends
	public void setPathEnd(){
		pathEndPosition.add(trackedPosition.size()-1);
	}
	
	//gives the size of the total path size
	public int getPathSize(){
		return pathEndPosition.size()-1; 
	}
	
	//adds a chosen path to the final path
	public void setTruePath(int pathNumber){
		truePathStartPosition.add(trackedTruePosition.size()-1);
		for(int i = pathStartPosition.get(pathNumber)+1; i < pathEndPosition.get(pathNumber); i++){ //runs through the track to add all the known tracks to the true path
			trackedTruePosition.add(trackedPosition.get(i));
		}
		truePathEndPosition.add(trackedTruePosition.size()-1);
		pathEndPosition.removeAll(pathEndPosition); //gets rid of end positions that are no longer needed
		pathStartPosition.removeAll(pathStartPosition); //gets rid of start positions that are no longer needed
	}
	
	
	public void removePath(int startPath, int endPath){ //gets rid of a path, be sure to start with the and work towards the beginning instead of the other way around as i did in the beginning
		int b = truePathStartPosition.get(startPath+1);
		int e = truePathEndPosition.get(endPath);
		for(int i = e-1; i > b+1; i--){
			trackedTruePosition.remove(i); //removes tracks
		}
		for(int i = endPath; i > startPath; i--){ //removes start and end positions
			truePathStartPosition.remove(i);
			truePathEndPosition.remove(i);
		}
	}
	
	//draws thick lines along the tracks of the true path
	public void renderTruePath(){
		for(int i = 0; i < trackedTruePosition.size(); i++){
			if(i != 0){
				line.drawThickLine(trackedTruePosition.get(i-1)[0], trackedTruePosition.get(i-1)[1], trackedTruePosition.get(i)[0], trackedTruePosition.get(i)[1], 3, 1);
			}
		}
	}
	
	//GETTERS SETTERS
	//a lot of functions to change and get the variables inside this class. There is probably a way without these functions to do this but i did not find it yet.
		public float getxPos() {
		return xPos;
	}
	public float getPrevXPos() {
		return prevXPos;
	}
	public void setPrevXPos(float prevXPos) {
		this.prevXPos = prevXPos;
	}
	public float getPrevYPos() {
		return prevYPos;
	}
	public void setPrevYPos(float prevYPos) {
		this.prevYPos = prevYPos;
	}
	public void setxPos(float xPos) {
		this.xPos = xPos;
	}
	public float getyPos() {
		return yPos;
	}
	public void setyPos(float yPos) {
		this.yPos = yPos;
	}
	public float getRotation() {
		return rotation;
	}
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}	
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
