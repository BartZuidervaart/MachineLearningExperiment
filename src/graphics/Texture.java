package graphics;


import static org.lwjgl.opengl.GL11.*;
import Math.My_Math;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;


//DISCLAIMER: This is a function that i only partly written, i have no idea how buffer variables work, or opengl functions like glGetTexImage.
//Of the class function, the bind() function and the getPixel() function i only know partly what is happening
//all the other functions i made myself
public class Texture {
	My_Math m = new My_Math();
	private int id; //im going to be honest, i dont know exactly why this is needed
	int width; //width of the image
	int height; //height of the image
	int numberOfTexturesLimit = 500; //a limit that i made, making sure no more red dots and paths get made
	
	public int getTextureLimit(){
		return numberOfTexturesLimit;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	//DISCLAIMER: I only know partly what is happening in here. I did not made this and only edited it to get the information i needed
	public int[] getPixel(int x, int y){ 
		//set pixels in a byte[]
		byte[] pixels = new byte[width*height*4];
		ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length);
		glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		buffer.get(pixels);
		
		//get the Pixel color information
		if(x < width && y < height){
			int index = (x + y * width) * 4;
			int red = pixels[index] & 0xFF;
			int green = pixels[index+1] & 0xFF;
			int blue = pixels[index+2] & 0xFF;
			int alpha = pixels[index+3] & 0xFF;
			int[] color = {red,green,blue,alpha};
			buffer = ByteBuffer.allocateDirect(0);
			buffer.clear();
			return color;
			
		} else {
			buffer = ByteBuffer.allocateDirect(0);
			buffer.clear();
			return new int[4];
		}
	}

	//DISCLAIMER: I only know partly what is happening in here. I did not made this and only edited it to get the results that i needed
	public Texture(String filename) { 
		BufferedImage t;
		try {
			t = ImageIO.read(getClass().getResource(filename));
			width = t.getWidth();
			height = t.getHeight();

			int[] pixels_raw = new int[width * height];
			pixels_raw = t.getRGB(0, 0, width, height, null, 0, width);

			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

			for(int i = 0; i < pixels_raw.length; i++){
					int pixel = pixels_raw[i];
					pixels.put((byte) ((pixel >> 16) & 0xFF)); // RED
					pixels.put((byte) ((pixel >> 8) & 0xFF)); // GREEN
					pixels.put((byte) ((pixel >> 0)& 0xFF)); // BLUE
					pixels.put((byte) ((pixel >> 24) & 0xFF)); // ALPHA
			} //16, 8, 0, 24

			pixels.flip();

			id = glGenTextures();

			glBindTexture(GL_TEXTURE_2D, id);

			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Seems to bind the rendered image 'id' to a GL_QUADS or other opengl function, but to be completely honest i'm not certain.
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void image(float x, float y, float w, float h){ //renders a image with given x & y and width & height.
		bind();
		
		glBegin(GL_QUADS);
		glTexCoord2f(1,0);
		glVertex2f(m.convertPixelsToWidth(x+w),m.convertPixelsToHeight(y));
		glTexCoord2f(0,0);
		glVertex2f(m.convertPixelsToWidth(x),m.convertPixelsToHeight(y));
		glTexCoord2f(0,1);
		glVertex2f(m.convertPixelsToWidth(x),m.convertPixelsToHeight(y+h));
		glTexCoord2f(1,1);	
		glVertex2f(m.convertPixelsToWidth(x+w),m.convertPixelsToHeight(y+h));
		glEnd();
	}
	
	public void image(float x, float y){ //renders a image with given x & y and uses the original image size
		image(x, y, width, height);
	}
	
	public void centerImage(float x, float y, float w, float h){ //renders a image but now the x and y position are in the middle of the image instead of in the top left corner
		image(x-(w/2),y-(h/2),w,h);
	}
	
	public void centerImage(float x, float y){ //renders a image but now the x and y position are in the middle of the image instead of in the top left corner
		image(x-(width/2), y-(height/2), width, height);
	}
	
	public void rotateImage(float x, float y, float w, float h, float r){ //makes a image that can be rotated, if the image isn't a square it gets strangely miss shaped. The problem lies in the math, but i did not have the time to fix it.
		r = r+(float)(-Math.PI/4); //i want the image top to be the front facing side.
		
		int screenW, screenH;
		screenW = m.getScreenX();
		screenH = m.getScreenY();
		
		bind();
		
		//makes a GL_QUADS square
		glBegin(GL_QUADS);
		glTexCoord2f(0,0);
		glVertex2f(x +(float)Math.sin(r)*m.map(w, 0,screenW, 0, 1), y +(float)Math.cos(r)*m.map(h, 0,screenH, 0, 1));
		glTexCoord2f(1,0);
		glVertex2f(x +(float)Math.sin(r+(Math.PI/2))*m.map(w, 0,screenW, 0, 1), y +(float)Math.cos(r+(Math.PI/2))*m.map(h, 0,screenH, 0, 1));
		glTexCoord2f(1,1);
		glVertex2f(x +(float)Math.sin(r+(Math.PI))*m.map(w, 0,screenW, 0, 1), y +(float)Math.cos(r+(Math.PI))*m.map(h, 0,screenH, 0, 1));
		glTexCoord2f(0,1);
		glVertex2f(x +(float)Math.sin(r+(Math.PI+(Math.PI/2)))*m.map(w, 0,screenW, 0, 1), y +(float)Math.cos(r+((Math.PI/2)+Math.PI))*m.map(h, 0,screenH, 0, 1));
		glEnd();
	}
	
	public void rotateImage(float x, float y, float r){ //makes a image that can be rotated, uses the height and width of the original image.
		rotateImage(x,y,width,height,r);
	}
	
	public void drawThickLine(float x1, float y1, float x2, float y2, float thickness, float alpha){ 
		
		bind(); //for some reason the GL_LINE functions need a texture in order to get on top of other textures, strange but it works this way
		
		glColor4f(0f, 0f, 0f, alpha);
		glLineWidth(thickness);
		glBegin(GL_LINE_STRIP);
	    glVertex2f((x1), (y1));
	    glVertex2f((x2), (y2));
	    glEnd();
	    glColor4f(1f, 1f, 1f, 1f);
	}
	public void drawLine(float x1, float y1, float x2, float y2){ //a thinner and more transparent line for the path that are less important
		drawThickLine(x1,y1,x2,y2,1, 0.1f);
	}
}
