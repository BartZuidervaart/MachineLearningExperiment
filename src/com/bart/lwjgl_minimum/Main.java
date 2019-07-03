package com.bart.lwjgl_minimum;

import inputs.Key;
import inputs.MouseClick;
import inputs.MousePosition;
import inputs.WindowResize;
import static loop.Loop.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import Math.My_Math;

//FOREWORD: Dit is de main class, in mijn geval maak ik niet veel gebruik van deze class zelf. Hier worden de minimale functies geplaatst om een library genaamd LWJGL te runnen, een openGL library
//die ik gebruik voor mijn graphics. Er zijn vast andere manieren om graphics te creeeren in Java, maar dit was de gene die ik tegen kwam en enigsinds bekend mee kon worden binnen de gegeven tijd.

//Het meeste van mijn programmeren gebruikt in de overige classes, voornamelijk de 'Loop();' regelt de meeste acties die per frame worden uit gevoerd. 

public class Main {
	static My_Math m = new My_Math();
	
	
	public static void main(String[] args){
		
		
		
		
		if(!glfwInit()){
			throw new IllegalStateException("Failed to initialize GLFW!"); // error zodat ik weet of glfw werkt
		}
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		long window = glfwCreateWindow(m.getScreenX(), m.getScreenY(), "Machine Learning Experiment", 0, 0);
		if(window == 0){
			throw new IllegalStateException("Failed to create window!");
		}
		
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (videoMode.width()-500)/2, (videoMode.height()-500)/2);
		
		glfwShowWindow(window);
		
		glfwSetCursorPosCallback(window, new MousePosition());
		glfwSetKeyCallback(window, new Key());
		glfwSetWindowSizeCallback(window, new WindowResize());
		glfwSetMouseButtonCallback(window, new MouseClick());
		
		glfwMakeContextCurrent(window);
				
		GL.createCapabilities();
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		while (!glfwWindowShouldClose(window)){
			
			glfwPollEvents();
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			loop(); //handeld alle functies af die per frame gehandteerd moeten worden.
			
			glfwSwapBuffers(window);
		}
		
		glfwTerminate(); //clears memory
	}
}
