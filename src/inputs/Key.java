package inputs;


import org.lwjgl.glfw.*;

public class Key extends GLFWKeyCallback {
		
		public static boolean[] keys = new boolean[65536]; //[Bart]: 65536 omdat de max is voor keys, zodat er niet een key opgeroepen kan worden die uiten de array komt
		
		public void invoke(long window, int key, int scancode, int action, int mods){
			keys[key] = action != GLFW.GLFW_RELEASE;
		}
}