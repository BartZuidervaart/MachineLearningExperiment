package inputs;

import org.lwjgl.glfw.*;

public class MousePosition extends GLFWCursorPosCallback{
	public static int x, y;
	public void invoke(long window, double xpos, double ypos){
		x = (int)xpos;
		y = (int)ypos;
	}

}
