package inputs;

import org.lwjgl.glfw.GLFWWindowSizeCallback;

public class WindowResize extends GLFWWindowSizeCallback{
	public static int w,h;
	
	public void invoke(long window, int width, int height){
		w = width;
		h = height;
	}
	
}
