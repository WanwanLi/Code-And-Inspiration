/*
 * Created on 2004-3-17
 * @author Jim X. Chen: clipping against an arbitray plane.
 */
import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.awt.GLCanvas;

public class J2_12_Clipping extends J2_11_ConeSolarCollision {

	static double[] eqn = {0, 1, 0, -40};
	static double[] eqn1 = {0, -1, 0, 120};
	static double[] eqn2 = {1, 0, 0, 100};

	// plane equation ax + by + cz + d = 0

	public void display(GLAutoDrawable glDrawable) {

		//1. specify plane equation y = 50;
		//
		//2. tell OpenGL system the plane eqn is a clipping plane
		gl.glClipPlane(GL2.GL_CLIP_PLANE0, eqn, 0);
		gl.glClipPlane(GL2.GL_CLIP_PLANE1, eqn1, 0);
		gl.glClipPlane(GL2.GL_CLIP_PLANE2, eqn2, 0);
		//3. Enable clipping of the plane. 
		gl.glEnable(GL2.GL_CLIP_PLANE0);
		gl.glEnable(GL2.GL_CLIP_PLANE1);
		gl.glEnable(GL2.GL_CLIP_PLANE2);

		super.display(glDrawable);
	}

	public static void main(String[] args) {
		J2_12_Clipping f = new J2_12_Clipping();

		f.setTitle("JOGL J2_12_Clipping");
		f.setSize(WIDTH, HEIGHT);
		f.setVisible(true);
	}
}
