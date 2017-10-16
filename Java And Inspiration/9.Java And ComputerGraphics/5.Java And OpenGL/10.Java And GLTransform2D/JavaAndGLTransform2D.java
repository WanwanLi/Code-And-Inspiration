import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.awt.GLCanvas;

public class JavaAndGLTransform2D
{
	public static void main(String[] args) 
	{
		GLCanvas canvas = new GLCanvas();
		GLDrawableListener listener = new GLDrawableListener();
		final Frame frame = new Frame("Java And GLTransform2D");
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
	double transform2d = 20, transformSpeed = 1;
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
		if (transform2d<20 || transform2d>200)
		{
			transformSpeed = -transformSpeed;
		}
		transform2d += transformSpeed;

		gl.glColor3f(1, 0, 0);
		glLoadIdentity();
		glScale2d(transform2d, transform2d);
		glDrawTriangle(units[0], units[1], units[2]);

		gl.glColor3f(0, 1, 0);
		glRotate1d(transform2d/15);
		glDrawTriangle(units[0], units[1], units[2]);

		gl.glColor3f(0, 0, 1);
		glTranslate2d(transform2d/100, 0);
		glDrawTriangle(units[0], units[1], units[2]);
		glWait(10);
	}
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) 
	{
		this.setSize(width, height);
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-width/2, width/2, -height/2, height/2, -1.0, 1.0);
	}
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	void glDrawTriangle(vec3 v1, vec3 v2, vec3 v3) 
	{
		gl.glBegin(GL.GL_TRIANGLES);
		gl.glVertex3dv(glMatrixVertex(v1), 0);
		gl.glVertex3dv(glMatrixVertex(v2), 0);
		gl.glVertex3dv(glMatrixVertex(v3), 0);
		gl.glEnd();
	}
	void glWait(int interval)
	{
		try {Thread.sleep(interval);} catch (Exception e){}
	}
	mat3 glMatrix = new mat3();
	int glMatrixIndex = -1, glMatrixSize = 64; 
	mat3[] glMatrixStack = new mat3[glMatrixSize];
	void glLoadIdentity()
	{
		glMatrix.identity();
	}
	void glTranslate2d(double x, double y)
	{
		glMatrix.translate(x, y);
	}
	void glRotate1d(double a)
	{
		glMatrix.rotate(a);
	}
	void glScale2d(double x, double y)
	{
		glMatrix.scale(x, y);
	}
	double[] glMatrixVertex(vec3 v)
	{
		vec3 k = new vec3(v.x, v.y, 1);
		vec3 s = glMatrix.mul(k);
		s.scale(1.0/s.xyz()[2]);
		return s.xyz();
	}
	void glPushMatrix()
	{
		if(glMatrixIndex >= glMatrixSize-1)return;
		glMatrixStack[++glMatrixIndex] = new mat3(glMatrix.v);
	}
	void glPopMatrix()
	{
		if(glMatrixIndex < 0)return;
		glMatrix.set(glMatrixStack[glMatrixIndex--].v);
	}
	public void dispose(GLAutoDrawable glDrawable) {}
}
