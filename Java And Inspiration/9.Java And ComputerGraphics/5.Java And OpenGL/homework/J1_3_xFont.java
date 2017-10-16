/*
 * Created on 2004-4-21
 * @author Jim X. Chen: draw bitmap and stroke characters and strings
 */
import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.awt.GLCanvas;

public class J1_3_xFont extends J1_3_Triangle {
	static GLUT glut = new GLUT();
	double delta; 

	// called for OpenGL rendering every reshape
	public void display(GLAutoDrawable drawable) {
		//generate a random line;
		int x0 = (int) (Math.random() * WIDTH);
		int y0 = (int) (Math.random() * HEIGHT);
		int xn = (int) ((Math.random() * WIDTH));
		int yn = (int) (Math.random() * HEIGHT);

		// draw a yellow line
		gl.glColor3d(Math.random(), Math.random(), Math.random());
		bresenhamLine(x0, y0, xn, yn);

		// bitmap fonts that 
		gl.glWindowPos3f(x0, y0, 0); // start poistion glRasterpos or glWindowPos
		glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, 'S');
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "tart");

		// stroke fonts 
		gl.glPushMatrix();
		gl.glTranslatef(xn, yn, 0); // end of line position
		delta = delta + 5; 
		gl.glRotated(delta, 0, 0, 1); // size
		gl.glScalef(0.2f, 0.2f, 0.2f); // size
		glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, 'E');
		glut.glutStrokeString(GLUT.STROKE_ROMAN, "nd");
		gl.glPopMatrix();

		// display() sleeps 500 ms to slow down the rendering
		try {
			Thread.sleep(1000);
		} catch (Exception ignore) {
		}
	}

	public static void main(String[] args) {
		J1_3_xFont f = new J1_3_xFont();

		f.setTitle("JOGL J1_3_xFont");
		f.setSize(WIDTH, HEIGHT);
		f.setVisible(true);
	}
}
