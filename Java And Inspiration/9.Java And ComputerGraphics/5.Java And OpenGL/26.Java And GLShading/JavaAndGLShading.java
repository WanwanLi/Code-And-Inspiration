import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.awt.GLCanvas;

public class JavaAndGLShading
{
	public static void main(String[] args) 
	{
		GLCanvas canvas = new GLCanvas();
		GLDrawableListener listener = new GLDrawableListener();
		final Frame frame = new Frame("Java And GLShading");
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
	GL2 gl; int width = 640, height = 480, timer = 0;
	double[][] coordinates = 
	{ 
		{1, 0, 0},
		{0, 1, 0},
		{0, 0, 1}
	};
	double[][] colors = 
	{ 
		{1, 0, 0},
		{0, 1, 0},
		{0, 0, 1}
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
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslated(0, -height/4, 0);
		gl.glRotated(timer, 1, 1, 1);
		gl.glScaled(height/2, height/2, height/2);
		glDrawTriangle(coordinates, colors);
		if (timer%100==0)gl.glShadeModel(GL2.GL_SMOOTH);
		if (timer++%200==0) gl.glShadeModel(GL2.GL_FLAT);
	}
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) 
	{
		this.setSize(width, height);
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-width/2, width/2, -height/2, height/2, -width, width);
	}
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	void glDrawTriangle(double[][] v, double[][] c) 
	{
		gl.glBegin(GL.GL_TRIANGLES);
		for(int i=0;i<3;i++)
		{
			gl.glColor3d(c[i][0], c[i][1], c[i][2]);
			gl.glVertex3dv(v[i],0);
		}
		gl.glEnd();
	}
	public void dispose(GLAutoDrawable glDrawable) {}
}
