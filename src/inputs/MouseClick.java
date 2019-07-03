package inputs;

import org.lwjgl.glfw.*;

public class MouseClick extends GLFWMouseButtonCallback {
	public static boolean[] click = new boolean[3];

	public void invoke(long window, int button, int action, int mods) {
		if (button < 3) {
			click[button] = action != GLFW.GLFW_RELEASE;
		}
	}

}
