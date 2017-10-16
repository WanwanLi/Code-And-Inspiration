import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.awt.GLCanvas;

public class JavaAndGLSubdivideSphere
{
	public static void main(String[] args) 
	{
		GLCanvas canvas = new GLCanvas();
		GLDrawableListener listener = new GLDrawableListener();
		final Frame frame = new Frame("Java And GLSubdivideSphere");
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
	double sphereScale = 100, increaseScale = 1;
	vec3[] units = 
	{
		vec3.unit(100), vec3.unit(010), vec3.unit(001), 
		vec3.unit(-100), vec3.unit(0-10), vec3.unit(00-1)
	};
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
		sphereScale += increaseScale;
		if (sphereScale >= height / 2 || sphereScale <=  height / 5) 
		{
			if ((increaseScale = -increaseScale) <0) 
			{
				gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
			}
			else gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
		}
		gl.glRotated(1, 1, 1, 1);
		gl.glPushMatrix();
		gl.glScaled(sphereScale, sphereScale, sphereScale);
		glDrawSubdivideSphere(units, (int)(sphereScale/80));
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
		this.sphereScale = height/3;
		this.increaseScale = Math.abs(increaseScale);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	void glDrawSubdivideSphere(vec3[] vertices, int depth) 
	{
		glDrawSubdivideSphere(vertices[0], vertices[1], vertices[2], depth);
		glDrawSubdivideSphere(vertices[1], vertices[2], vertices[3], depth);
		glDrawSubdivideSphere(vertices[2], vertices[3], vertices[4], depth);
		glDrawSubdivideSphere(vertices[3], vertices[4], vertices[5], depth);
		glDrawSubdivideSphere(vertices[4], vertices[5], vertices[0], depth);
		glDrawSubdivideSphere(vertices[5], vertices[0], vertices[1], depth);
		glDrawSubdivideSphere(vertices[0], vertices[2], vertices[4], depth);
		glDrawSubdivideSphere(vertices[1], vertices[3], vertices[5], depth);
	}
	void glDrawSubdivideSphere(vec3 v1, vec3 v2, vec3 v3, int depth) 
	{
		if (depth == 0) 
		{
			gl.glColor3d(v1.x* v1.x, v2.y * v2.y, v3.z *v3.z);
			glDrawTriangle(v1, v2, v3);
			return;
		}
		vec3 v12 = v1.add(v2); v12.normalize();
		vec3 v23 = v2.add(v3); v23.normalize();
		vec3 v31 = v3.add(v1); v31.normalize();
		glDrawSubdivideSphere(v1, v12, v31, depth - 1);
		glDrawSubdivideSphere(v2, v23, v12, depth - 1);
		glDrawSubdivideSphere(v3, v31, v23, depth - 1);
		glDrawSubdivideSphere(v12, v23, v31, depth - 1);
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
