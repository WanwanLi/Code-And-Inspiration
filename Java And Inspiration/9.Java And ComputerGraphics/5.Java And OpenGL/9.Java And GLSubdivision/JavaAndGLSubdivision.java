import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.awt.GLCanvas;

public class JavaAndGLSubdivision
{
	public static void main(String[] args) 
	{
		GLCanvas canvas = new GLCanvas();
		GLDrawableListener listener = new GLDrawableListener();
		final Frame frame = new Frame("Java And GLSubdivision");
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
	vec3[] units = new vec3[]
	{
		new vec3(1, 0, 0), new vec3(0, 1, 0), 
		new vec3(-1, 0, 0), new vec3(0, -1, 0)
	};
	double circleDepth = 0, circleRadius = 0;
	double increaseDepth = 1, increaseRadius = 20;
	public void init(GLAutoDrawable glDrawable) 
	{
		gl = glDrawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glDrawBuffer(GL.GL_FRONT_AND_BACK);
	}
	public void display(GLAutoDrawable drawable) 
	{
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		circleRadius += increaseRadius;
		if (circleRadius  >= height / 2 || circleRadius   <=  height / 5) 
		{
			increaseRadius = -increaseRadius;
			
		}
		circleDepth += increaseDepth;
		if(circleDepth  >= 5 || circleDepth <= 0)
		{
			increaseDepth = -increaseDepth;
		}
		glDrawSubdivideCircle(circleRadius, units, (int)circleDepth);
		glWait(500);
	}
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) 
	{
		this.setSize(width, height);
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-width/2, width/2, -height/2, height/2, -1.0, 1.0);
		circleDepth = 0;  circleRadius =  height / 5; 
		increaseRadius = Math.abs(increaseRadius);
		increaseDepth = Math.abs(increaseDepth);
	}
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	void glDrawSubdivideCircle(double radius, vec3[] vertices, int depth) 
	{
		glDrawSubdivideCircle(radius, vertices[0], vertices[1], depth);
		glDrawSubdivideCircle(radius, vertices[1], vertices[2], depth);
		glDrawSubdivideCircle(radius, vertices[2], vertices[3], depth);
		glDrawSubdivideCircle(radius, vertices[3], vertices[0], depth);
	}
	void glDrawSubdivideCircle(double radius, vec3 v1, vec3 v2, int depth) 
	{
		if (depth == 0) 
		{
			gl.glColor3d(v1.x* v1.x, v1.y * v1.y, v1.z *v1.z);
			vec3 v00 = new vec3(0);
			vec3 v11 = v1.mul(radius);
			vec3 v22 = v2.mul(radius);
			glDrawTriangle(v00, v11, v22);
			return;
		}
		vec3 v12 = v1.add(v2); v12.normalize();
		glDrawSubdivideCircle(radius, v1, v12, depth - 1);
		glDrawSubdivideCircle(radius, v12, v2, depth - 1);
	}
	void glDrawTriangle(vec3 v1, vec3 v2, vec3 v3) 
	{
		gl.glBegin(GL.GL_TRIANGLES);
		gl.glVertex3dv(v1.xyz(), 0);
		gl.glVertex3dv(v2.xyz(), 0);
		gl.glVertex3dv(v3.xyz(), 0);
		gl.glEnd();
	}
	void glWait(int interval)
	{
		try {Thread.sleep(interval);} catch (Exception e){}
	}
	public void dispose(GLAutoDrawable glDrawable) {}
}
