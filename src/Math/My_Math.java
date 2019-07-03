package Math;

//My own math library with functions that i had no library for, or that i needed quickly
public class My_Math {
	public int screenX = 800; //screenWidth
	public int screenY = 600; //screenHeight
	
	public int getScreenX() {
		return screenX;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}
	

	public float map(float x, float in_min, float in_max, float out_min, float out_max){ //kleine functie om een waarde te schalen tussen zijn originele min en max, naar een nieuwe min en max
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	public double dist(double x1, double y1, double x2, double y2){ //kleine functie om het verschil te berekenen tussen dimensies, float gave me trouble so i made it double
		return Math.sqrt(((x1-x2)*(x1-x2))+((y1-y2)*(y1-y2))); //waarom werkt de Math.pow(Math.abs(x1-x2),2) hier niet :/
	}
	
	public float convertWidthToPixels(float n){
		return map(n, -1,1,0,screenX);
	}
	
	public float convertHeightToPixels(float n){
		return map(n, 1,-1,0, screenY);
	}
	
	public float convertPixelsToWidth(float n){
		return map(n, 0, screenX, -1, 1);
	}
	
	public float convertPixelsToHeight(float n){
		return map(n, 0, screenY, 1, -1);
	}
}
