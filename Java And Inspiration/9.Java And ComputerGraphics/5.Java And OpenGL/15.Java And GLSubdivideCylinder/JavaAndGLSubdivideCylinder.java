import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.awt.GLCanvas;

public class JavaAndGLSubdivideCylinder
{
	public static void main(String[] args) 
	{
		GLCanvas canvas = new GLCanvas();
		GLDrawableListener listener = new GLDrawableListener();
		final Frame frame = new Frame("Java And GLSubdivideCylinder");
		final Animator animator = new Animator(canvas);
		frame.add(canvas);
		frame.setSize(640, 480);
		listener.setSize(640, 480);
		canvas.addGLEventListener(listener);
		frame.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				animator.stop();
				frame.dispose();
				System.exit(0);
			}
		});
		frame.setVisible(true);
		animator.start();
		canvas.requestFocus();
	}
}
class GLDrawableListener implements GLEventListener 
{
	GL2 gl; int width = 640, height = 480; 
	double cylinderScale = 100, cylinderRadius = 0.75, cylinderHeight = 1.25, increaseScale = 1;
	vec3[] units = {vec3.unit(100), vec3.unit(010), vec3.unit(-100), vec3.unit(0-10)};
	public void init(GLAutoDrawable glDrawable) 
	{
		gl = glDrawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glDrawBuffer(GL.GL_FRONT_AND_BACK);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}
	public void display(GLAutoDrawable drawable) 
	{
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		cylinderScale += increaseScale;
		if (cylinderScale >= height / 2 || cylinderScale <=  height / 5) 
		{
			if ((increaseScale = -increaseScale) <0) 
			{
				gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
			}
			else gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
		}
		gl.glRotated(1, 1, 1, 1);
		gl.glPushMatrix();
		gl.glScaled(cylinderScale, cylinderScale, cylinderScale);
		glDrawSubdivideCylinder(cylinderRadius, cylinderHeight, units, (int)(cylinderScale/50));
		gl.glPopMatrix();
	}
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) 
	{
		this.setSize(width, height);
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-width/2, width/2, -height/2, height/2, -width, width);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		this.cylinderScale = height/3;
		this.increaseScale = Math.abs(increaseScale);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	void glDrawSubdivideCylinder(double radius, double height, vec3[] vertices, int depth) 
	{
		glDrawSubdivideCylinder(radius, height, vertices[0], vertices[1], depth);
		glDrawSubdivideCylinder(radius, height, vertices[1], vertices[2], depth);
		glDrawSubdivideCylinder(radius, height, vertices[2], vertices[3], depth);
		glDrawSubdivideCylinder(radius, height, vertices[3], vertices[0], depth);
	}
	void glDrawSubdivideCylinder(double radius, double height, vec3 v1, vec3 v2, int depth) 
	{
		if (depth == 0) 
		{
			gl.glColor3d(v1.x* v1.x, v1.y * v1.y, v1.z *v1.z);
			vec3 v00 = vec3.unit(000);
			vec3 v11 = v1.mul(radius);
			vec3 v22 = v2.mul(radius);
			vec3 v000 = vec3.unit(001).mul(height);
			vec3 v111 = v11.add(v000);
			vec3 v222 = v22.add(v000);
			glDrawTriangle(v00, v11, v22);
			glDrawTriangle(v000, v111, v222);
			glDrawQuad(v11, v22, v222, v111);
			return;
		}
		vec3 v12 = v1.add(v2); v12.normalize();
		glDrawSubdivideCylinder(radius, height, v1, v12, depth - 1);
		glDrawSubdivideCylinder(radius, height, v12, v2, depth - 1);
	}
	void glDrawQuad(vec3 v1, vec3 v2, vec3 v3, vec3 v4) 
	{
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex3dv(v1.xyz(), 0);
		gl.glVertex3dv(v2.xyz(), 0);
		gl.glVertex3dv(v3.xyz(), 0);
		gl.glVertex3dv(v4.xyz(), 0);
		gl.glEnd();
	}
	void glDrawTriangle(vec3 v1, vec3 v2, vec3 v3) 
	{
		gl.glBegin(GL.GL_TRIANGLES);
		gl.glVertex3dv(v1.xyz(), 0);
		gl.glVertex3dv(v2.xyz(), 0);
		gl.glVertex3dv(v3.xyz(), 0);
		gl.glEnd();
	}
	public void dispose(GLAutoDrawable glDrawable) {}
}
