package graphics;

import static org.lwjgl.opengl.GL11.*;
import Math.My_Math;

public class Square {
	My_Math m = new My_Math();

	public void createSquare(int x, int y, int w, int h, float[] c) {
		int screenW, screenH;

		screenW = m.getScreenX();
		screenH = m.getScreenY();

		// maakt een QUARDS vierkant
		glBegin(GL_QUADS);
		glColor4f(c[0], c[1], c[2], c[3]);
		glVertex2f(m.map(x + w, 0, screenW, -1, 1), m.map(y, 0, screenH, 1, -1));
		glVertex2f(m.map(x, 0, screenW, -1, 1), m.map(y, 0, screenH, 1, -1));
		glVertex2f(m.map(x, 0, screenW, -1, 1), m.map(y + h, 0, screenH, 1, -1));
		glVertex2f(m.map(x + w, 0, screenW, -1, 1), m.map(y + h, 0, screenH, 1, -1));
		glEnd();
	}
}
