package LearningMethods;

import java.util.ArrayList;

import LearningMethods.randomRotations;
import character.Character;
import graphics.Texture;
import Math.My_Math;

public class FireCharacters {
	My_Math m = new My_Math(); //uses my own math functions	
	Texture background = new Texture("/labyrinth voorbeeld.jpg"); //renders the background image to determine if the character hit the wall
	ArrayList<randomRotations> input = new ArrayList<randomRotations>(); //keeps track of the rotations made during the making of a path
	ArrayList<Integer> numberOfTurns = new ArrayList<Integer>(); //keeps track of the number of turns it took for a character to meet either the end or a wall
	ArrayList<Float[]> hitMarker = new ArrayList<Float[]>(); //keeps track of the locations where a character hit a wall
	static Character c = new Character(0,0,0); //creates a character that will get fired over and over again
	public float startPositionX ,startPositionY; 
	public float endPositionX, endPositionY;
	public float startRotation;
	public boolean reached = false; //keeps track if the end location is reached 
	
	public void cleanUpCharacter(){ //cleans up variables after a cycle
		input.removeAll(input);
		numberOfTurns.removeAll(numberOfTurns);
		hitMarker.removeAll(hitMarker);
	}
	
	public void renderPath(){ //shows paths
		c.renderPath();
	}
	
	public void renderTruePath(){ //shows the true path
		c.renderTruePath();
	}

	public void cleanPath(){ //remove the path
		c.cleanPath();
	}
	
	public void removePath(int startPath, int endPath){ //removes a selective path
		c.removePath(startPath, endPath);
	}
	
	//fires a character from a start location, as soon as it gets flight its direction will be randomly altered making room for new paths to emerge
	public void fireCharacter(){	
		background.image(0,0); //places the image, needed to see if a character hit a wall
		c.setxPos(m.map(startPositionX,0,background.getWidth(), -1,1)); //character positions
		c.setyPos(m.map(startPositionY,0,background.getHeight(),1,-1));
		//c.setRotation(m.map((float)Math.random(), 0, 1, 0, (float)Math.PI*2)); //random rotatie werkte slechter dan een roterende cyclus
		c.setRotation(startRotation); //determines the rotation for the character to start at
		boolean killed = false; //boolean needed to see if a character hit a wall
		input.add(new randomRotations()); //adds random rotations along the way of the characters flight
		c.setPathStart(); //determines that a new path starts here
		while(!killed || !reached){ //keeps on moving until the character either hits a wall of the end
			float r = input.get(input.size()-1).pullRotation(); //pulls random rotations
			c.rotateForward(r); //moves in the direction of the random location
			float xInPixels = m.map(c.getxPos(), -1, 1, 0, background.getWidth()); //translates the positions in to pixel positions
			float yInPixels = m.map(c.getyPos(), 1, -1, 0, background.getHeight());
			int[] color = background.getPixel((int)xInPixels,(int)yInPixels); //tracks the color of the background underneath the character
			if(color[0] < 200){ //if color red is darker than 200 such as in a black wall, it will determine that it hit a wall
				c.setPathEnd(); //determines that a path ended here
				killed = true; //wall is hit
				numberOfTurns.add(input.get(input.size()-1).getRotationsLength()); //determines the size of the path					
				Float[] p = {m.map(c.getPrevXPos(), -1, 1, 0, background.getWidth()), m.map(c.getPrevYPos(), 1, -1, 0, background.getHeight())}; //converts x and y to pixels
				hitMarker.add(p); //add the x and y positions to a hitMarker
				input.get(input.size()-1).cleanRotation(); //gets rid of the tracked inputs
				break; //breaks out of the while loop
			}
			if(m.dist(xInPixels,yInPixels, endPositionX, endPositionY) < 10){ //determines if the character gets close enough to the end position
				c.setPathEnd(); //determines that a path ended here
				numberOfTurns.add(input.get(input.size()-1).getRotationsLength()); //determines the size of the path					
				Float[] p = {m.map(c.getPrevXPos(), -1, 1, 0, background.getWidth()), m.map(c.getPrevYPos(), 1, -1, 0, background.getHeight())}; //converts x and y to pixels
				hitMarker.add(p); //add the x and y position to a hitMarker
				input.get(input.size()-1).cleanRotation(); //gets rid of the tracked inputs
				reached = true; //end position was reached
				System.out.println("succes!");
				break; //breaks out of the while loop
			}
		}
	}
	
	//same as the fireCharacter function, only this time the rotation isn't alternated midway through and the function can get its own x and y location and a x and y destination to which it wants to travel
	public boolean straightLine(float startX, float startY, float desX, float desY){
		background.image(0,0);
		c.setxPos(m.map(startX,0,background.getWidth(), -1,1));
		c.setyPos(m.map(startY,0,background.getHeight(),1,-1));
		float r = (float)Math.atan2((desX-startX),-(desY-startY));
		c.setRotation(r);
		boolean killed = false;
		boolean reachedPoint = false;
		c.setPathStart();
		while(!killed || !reachedPoint){
			c.rotateForward(0);
			float xInPixels = m.map(c.getxPos(), -1, 1, 0, background.getWidth());
			float yInPixels = m.map(c.getyPos(), 1, -1, 0, background.getHeight());
			int[] color = background.getPixel((int)xInPixels,(int)yInPixels); //tracked de kleur onder het karakter, als deze zwart wordt is het mannetje geraakt
			if(color[0] < 200){
				c.setPathEnd();
				killed = true;				
				Float[] p = {m.map(c.getPrevXPos(), -1, 1, 0, background.getWidth()), m.map(c.getPrevYPos(), 1, -1, 0, background.getHeight())};
				hitMarker.add(p);
				return false;
			}
			if(m.dist(xInPixels,yInPixels, desX, desY) < 10){
				c.setPathEnd();					
				Float[] p = {m.map(c.getPrevXPos(), -1, 1, 0, background.getWidth()), m.map(c.getPrevYPos(), 1, -1, 0, background.getHeight())};
				hitMarker.add(p);
				reachedPoint = true;
				return true;
			}
		}
		return false;
	}
	
	//GETTERS AND SETTERS
	public void setPathStartPosition(float x, float y){
		c.setPathStartPosition(x, y);
	}
		
	public int getNumberOfTurns(int i){
		if(i < numberOfTurns.size()){
			return numberOfTurns.get(i);
		} else {
			return 0;
		}
	}
	
	public void setTruePath(int pathNumber){
		c.setTruePath(pathNumber);
	}
	
	public int getPathSize(){
		return c.getPathSize();
	}
	
	public float getStartPositionX() {
		return startPositionX;
	}
	
	
	public void setStartPositionX(float startPositionX) {
		this.startPositionX = startPositionX;
	}
	
	
	
	public float getStartPositionY() {
		return startPositionY;
	}
	
	
	
	public void setStartPositionY(float startPositionY) {
		this.startPositionY = startPositionY;
	}
	
	
	
	public float getEndPositionX() {
		return endPositionX;
	}
	
	
	
	public void setEndPositionX(float endPositionX) {
		this.endPositionX = endPositionX;
	}
	
	
	
	public float getEndPositionY() {
		return endPositionY;
	}
	
	
	
	public void setEndPositionY(float endPositionY) {
		this.endPositionY = endPositionY;
	}
	
	
	
	public float getStartRotation() {
		return startRotation;
	}
	
	
	public void setStartRotation(float startRotation) {
		this.startRotation = startRotation;
	}
	
	
	public float getHitMarker(int i, int j) {
		if(i < hitMarker.size()){
		return hitMarker.get(i)[j];
		} else {
			return 0;
		}
	}
	
	public float getHitMarkerSize(){
		return hitMarker.size();
	}
	
	public boolean getReached(){
		return reached;
	}
}
