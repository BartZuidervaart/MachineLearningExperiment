package loop;

//import character.Character; //was needed for a control able demo character, but that is no longer needed for the demo
import character.EndingPoint; //determines all the variables and graphics for the end location
import character.StartingPoint; //determines all the variables and graphics for the start location
import graphics.Texture; //makes it possible to load and place images and lines
import inputs.Key; //makes it possible to track keyboard inputs
import inputs.MouseClick; //makes it possible to determine if a mouse has clicked
import inputs.MousePosition; //makes it possible to determine the location of the mouse on the screen

import static org.lwjgl.glfw.GLFW.*; //the LWJGL is library that i use by far the most in my program, it does all the things needed to render images and inputs in the background.

import java.util.ArrayList; //needed to make ArrayLists

import Math.My_Math; //My own math functions
import LearningMethods.FireCharacters; //class that keeps track of all the things needed for firing characters.

public class Loop {
	static My_Math m = new My_Math(); //my own math functions because i couldn't find a library for them
	static FireCharacters f = new FireCharacters(); //makes a class for the characters that need to be fired in the program

	static Texture winCondition = new Texture("/labyrinth-behaald.png"); //win condition image that pops up in front of the screen after the program is finished
	static Texture background = new Texture("/labyrinth voorbeeld.jpg"); //background image, this image is also needed to determine if a character hit a wall
	static Texture hitMarker = new Texture("/hit-marker.png"); //a red dot that can be placed at every spot a character hit the wall
	static Texture trueHitMarker = new Texture("/true-hit-marker.png"); // a green dot that can be placed at the spot of that characters that got the farthest after a cycle
	
	static StartingPoint start = new StartingPoint(705,80);
	static EndingPoint end = new EndingPoint(705,550);
	
	static ArrayList<Float[]> hitLocation = new ArrayList<Float[]>(); //adds all locations where a character hit the wall
	static ArrayList<Float[]> trueHitLocation = new ArrayList<Float[]>(); //adds all the locations that got the farthest after a cycle
	
	static int attemptCounter = 0; //counts how many cycles were needed to find the end location
	static int fireCounter; //variable that counts how many characters were fired for a cycle
	static int checkStartCounter = 0; //variable needed to find the shortest path towards the end
	static int checkEndCounter; //variable need to find the shortest path towards the end
	static int triesMaximum = 10; //determines how many characters will fire in a given cycle, i've determined that 10 tries is the fastest way. Although 100 is the sweet spot for accuracy if you want to keep the attempts the shortest
	static boolean endLocationReached = false; //boolean needed to for the program to move to the next stage after the end location is reached.
	static boolean startTrying = false; //boolean that determines that the program will no longer listen to keyboard and mouse inputs and will keep on going until the end location has been reached.
	//static float startRotation; //start rotation for a demo character to start, a function no longer needed
	
	//NEEDED FOR A PLAYER CONTROLABLE TEST CHARACTER
	//static Character c = new Character(0,0,0);
	//static int[] previousColor = new int[4];
	
	public Loop(){
		
	}
	
	public static void loop(){
		if(!endLocationReached){			
			findLocation(); //find the end location
		} else {
			simplifyPath(); //after the location has been reach, this function will simplify the road towards it
		}
	}
	
	//simplified function of the process to find the end location
	private static void findLocation(){
		
		if(startTrying){ //if the experiment has been started, this will run
			if(fireCounter < triesMaximum){ //fires characters until a cycle has ended, at the end the program needs to determine which of the characters got away the farthest
				fireCharacters(); //fires characters to find new starting locations to eventually find the end location
			} else {
				findFurthestPath(); //if the cycle is over, this function determines which character got away the farthest and makes that the new starting position for the next cycle
			}
		} else { //else it will listen to keyboard and mouse commands to determine the start and end location
			findPathKeyControls(); //checks keyboard and mouse actions and all functions that get triggered by them.
		}
		renderScreen(); //else just these 
	}
	
	//This function fires characters around until a cycle is finished.
	//The characters will be fired from different angles away from the start position.
	//Once the characters are on the move, their movement will get random turns creating original curves adding more chance to reach new destinations
	private static void fireCharacters(){
		f.setStartRotation(m.map(fireCounter, 0, triesMaximum, 0, (float)Math.PI*2)); //fires the characters in a circle around the start position 
		f.fireCharacter(); //speaks for itself
		
		//makes a red dot where the character met its fate against a wall
		Float[] findHitmarker = {f.getHitMarker(fireCounter, 0),f.getHitMarker(fireCounter, 1)};
		hitLocation.add(findHitmarker);
		
		//Checks of the final destination has been reached
		if(f.getReached()){
			foundEndLocation();
		}
		
		fireCounter++; //the variable that controls how many characters have been fired, to see if the end of the cycle has been met
	}
	
	//Function that runs the needed variables to function after the end location has been reached.
	private static void foundEndLocation(){
		attemptCounter++; //counts this path as an attempt as well
		startTrying = false; //stops the code from keep on trying to reach a end spot
		
		//makes a path for the previous hit marker to the end
		int place = f.getPathSize();
		f.setTruePath(place);
		start.setxPos((int)f.getHitMarker(place, 0));
		start.setyPos((int)f.getHitMarker(place, 1));
		
		//adds a true hit marker (green dot) at the end of the road
		Float[] findTrueHitmarker = {f.getHitMarker(place, 0), f.getHitMarker(place, 1)};
		trueHitLocation.add(findTrueHitmarker);
		endLocationReached = true; //needed to move the next step of the code
		checkEndCounter = trueHitLocation.size()-1; //needed for the simplification function to determine how many possible spots there are at the beginning of the function 
		cleanHitMarkers(); //deletes the red dots
	}
	
	//function that tries to find the path that gets away the farthest from the begin location and uses that as a new begin location
	//it does this by firing multiple characters, giving them a slight curve every time they move, making their movement relatively random which opens the chance of finding new and hidden roads
	private static void findFurthestPath(){
		double[] highestNumber = new double[2]; //needed to see which of the known shots got away the fartest
		for(int i = 0; i < triesMaximum; i++){
			
			//checks how far the given location is from the begin location
			if(m.dist((double)start.getxPos(),(double)start.getyPos(),(double)f.getHitMarker(i, 0),(double)f.getHitMarker(i, 1)) > highestNumber[0]){				
				
				//if the location is longer than any known locations, it will be determined the longest
				highestNumber[0] = m.dist((double)start.getxPos(),(double)start.getyPos(),(double)f.getHitMarker(i, 0),(double)f.getHitMarker(i, 1));
				highestNumber[1] = i;
			}
		}		
		f.setTruePath((int)highestNumber[1]); //gives the longest path to the function that draws the lines
		start.setxPos((int)f.getHitMarker((int)highestNumber[1], 0)); //makes the new location a start position for the next cycle
		start.setyPos((int)f.getHitMarker((int)highestNumber[1], 1));
		
		//adds a trueHitMarker (green dot) to show where character that got the farthest hit the wall
		Float[] findTrueHitmarker = {f.getHitMarker((int)highestNumber[1], 0), f.getHitMarker((int)highestNumber[1], 1)};
		trueHitLocation.add(findTrueHitmarker);
			
		f.cleanUpCharacter();
		attemptCounter++;
		f.setStartPositionX(start.getxPos());
		f.setStartPositionY(start.getyPos());
		fireCounter = 0;
		if(hitLocation.size() > hitMarker.getTextureLimit()){
			cleanHitMarkers();
		}
	}
	
	//after program found its way to an end location, this function will try to shorten the path.
	//This method is not optimal and only makes straight lines instead of curves, but it was all i could make in the time given to me.
	//The function goes from the last known hit marker and tries to find straight lines towards the earliest marker possible. It deletes every path in between making the path significantly shorter.
	private static void simplifyPath(){
		if(checkEndCounter > 0){ //counts from the last hit marker towards the very first
			f.setStartPositionX(trueHitLocation.get(checkEndCounter)[0]); //places a start position at the last known hit marker
			f.setStartPositionY(trueHitLocation.get(checkEndCounter)[1]); 
			if(checkEndCounter > checkStartCounter){ //runs this until this location has checked every previous location known
				//fires a character towards a known location to see if a straight line can be made
				if(f.straightLine(trueHitLocation.get(checkEndCounter)[0], trueHitLocation.get(checkEndCounter)[1] ,trueHitLocation.get(checkStartCounter)[0], trueHitLocation.get(checkStartCounter)[1])){
					//gets rid of lines between a successful shot, straightening the line
					f.removePath(checkStartCounter, checkEndCounter);
					//gets rid of known hit locations in between the 2 lines, making them unnecessary
					for(int i = checkEndCounter-1; i > checkStartCounter; i--){
						trueHitLocation.remove(i);
					}
					//adjust the number of known locations
					checkEndCounter = checkStartCounter;
					//cleans the demonstration paths
					f.cleanPath();
				}
				//draws number of known locations closer to the end known location
				checkStartCounter++;
			} else {
				//moves on the the next known location 
				checkEndCounter--;
				checkStartCounter = 0;
			}
			renderScreenWithoutStartEnd(); //renders screen
		} else {
			renderScreenWithoutStartEnd(); //renders the screen with a winning screen, the program is done now
			winCondition.image(0, 0);
		}
		//System.out.println(attemptCounter); //could show how many attempts at reaching the end location had been made
	}
	
	//renders everything needed on the screen except the start and end location (because that could be annoying after the location has been reached and the program is trying to demonstrate how to make a shorter path
	private static void renderScreenWithoutStartEnd(){
		background.image(0,0); //loads the background image
		f.renderPath(); //makes lines for the attempts the program makes to reach the end goal
		f.renderTruePath(); //renders thicker lines for the ones that got farthest after a cycle of attempts
		
		//places red dots at all the attempts that hit a wall
		for(int i = 0; i < hitLocation.size(); i++){
			hitMarker.centerImage(hitLocation.get(i)[0], hitLocation.get(i)[1]);
		}

		//places green dots for all the attempts that got the farthest 
		for(int i = 0; i < trueHitLocation.size(); i++){
			trueHitMarker.centerImage(trueHitLocation.get(i)[0], trueHitLocation.get(i)[1]);
		}
	}
	
	//renders everything as well as the start and end position
	private static void renderScreen(){
		renderScreenWithoutStartEnd();
			
		start.displayStart();
		end.displayEnd();
	}
	
	
	//Keyboard and mouse actions needed for the experiment
	private static void findPathKeyControls(){
		
		//The Q key accompanied by a mouse click on the screen determines where the experiment will start 
		if(Key.keys[GLFW_KEY_Q] && MouseClick.click[GLFW_MOUSE_BUTTON_LEFT]){
			start.setxPos(MousePosition.x);
			start.setyPos(MousePosition.y);
		}
		
		//The E key accompanied by a mouse click on the screen determines where the experiment needs to go
		//This location is only used to know if the location has been reached, the program has no idea where this location is before its found
		if(Key.keys[GLFW_KEY_E] && MouseClick.click[GLFW_MOUSE_BUTTON_LEFT]){
			end.setxPos(MousePosition.x);
			end.setyPos(MousePosition.y);
		}
		
		//The R key accompanied by a mouse click on the screen gives the start Position a rotation relative to the mouse click
		//This is a dead feature originally used for a demonstration character to give it a different start, the program at the moment chooses its own rotation cycle
		//if(Key.keys[GLFW_KEY_R] && MouseClick.click[GLFW_MOUSE_BUTTON_LEFT]){
			//startRotation = (float)Math.atan2(MousePosition.x-start.getxPos(), -(MousePosition.y-start.getyPos()));
		//}
		
		//If the program has not been started yet, the T key will start it
		if(!startTrying){
			if(Key.keys[GLFW_KEY_T]){
					f.setEndPositionX(end.getxPos()); //gives the fireCharacter method a location to recognize so it knows when it reached its destination
					f.setEndPositionY(end.getyPos()); //gives the fireCharacter method a location to recognize so it knows when it reached its destination
					f.setStartPositionX(start.getxPos());  //gives the fireCharacter a location to start from, it will only use this location at the beginning, after a cycle a new start position will be determined
					f.setStartPositionY(start.getyPos());  //gives the fireCharacter a location to start from, it will only use this location at the beginning, after a cycle a new start position will be determined
					f.setPathStartPosition(start.getxPos(), start.getyPos());  //gives the characters in the fireCharacter a path start position from which it will draw a path
					startTrying = true; //start the experiment and makes sure it doesn't stop until the end location has been reached
					
					//places a trueHitMarker (green dot) right at the start
					Float[] findTrueHitmarker = {f.getStartPositionX(), f.getStartPositionY()};
					trueHitLocation.add(findTrueHitmarker);
				
			}
		}
	}
	
	
	//removes all the hitMarkers (red dots), also removes paths and hitLocations which are needed for calculating the successful path so don't remove or place on unintended places
	private static void cleanHitMarkers(){
		hitLocation.removeAll(hitLocation); //the locations for the HitMarkers (red dots), after choosing the correct path these can be deleted
		f.cleanPath(); //cleans up the memorized paths, after choosing the correct path these can be deleted 
	}
	
	//feature that adds a control able character in the screen to demonstrate what the particles do, not needed for the end result.
	/*private static void controlableCharacter(){
			if(Key.keys[GLFW_KEY_W]){
				c.moveForward();
			}
			if(Key.keys[GLFW_KEY_S]){
				c.moveBackward();
			}
			if(Key.keys[GLFW_KEY_A]){
				c.rotateLeft();
			}
			if(Key.keys[GLFW_KEY_D]){
				c.rotateRight();
			}
			
			int[] color = background.getPixel((int)m.map(c.getxPos(),-1,1,0,background.getWidth()),(int)m.map(c.getyPos(),1,-1,0,background.getHeight())); //tracked de kleur onder het karakter, als deze zwart wordt is het mannetje geraakt
			c.renderPath();
			c.renderCharacter();
			
			if(color[0] != previousColor[0]){
				if(color[0] < 200){
					c.setRotation(startRotation);
					c.setxPos(m.map(start.getxPos(),0,background.getWidth(),-1,1));
					c.setyPos(m.map(start.getyPos(),0,background.getHeight(),1,-1));
				}
				previousColor[0] = color[0];
			}
	}*/
}
