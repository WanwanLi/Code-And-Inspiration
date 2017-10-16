import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.awt.GLCanvas;

public class JavaAndGLCircle
{
	public static void main(String[] args) 
	{
		GLCanvas canvas = new GLCanvas();
		GLDrawableListener listener = new GLDrawableListener();
		final Frame frame = new Frame("Java And GLCircle");
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
	public void init(GLAutoDrawable glDrawable) 
	{
		gl = glDrawable.getGL().getGL2();
		gl.glDrawBuffer(GL.GL_FRONT_AND_BACK);
	}
	public void display(GLAutoDrawable drawable) 
	{
		float red = (float)Math.random();
		float green = (float)Math.random();
		float blue = (float)Math.random();
		gl.glColor3f(red, green, blue);
		glDrawRandomCircle(50, 5, 100);
		glWait(50);
	}
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) 
	{
		this.setSize(width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, width, 0, height, -1.0, 1.0);
	}
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	void glDrawRandomCircle(double radius, int maxSize, int maxLength)
	{
		double x = Math.random()*width;
		double y = Math.random()*height;
		int size = 1 + (int)(maxSize*Math.random());
		int length = 4 + (int)(maxLength*Math.random());
		glDrawCircle(x, y, radius, size, length);
	}
	void glDrawCircle(double x, double y, double r, int size, int length) 
	{
		int i = 0; double x0, y0, x1, y1, t, dt = 2*Math.PI/length;
		for(x0 = r+x, y0 = y, t=dt; i<length; t+=dt,  x0 = x1, y0 = y1, i++) 
		{
			x1 = r*Math.cos(t)+x; y1 = r*Math.sin(t)+y;
			glDrawLine((int)x0, (int)y0, (int)x1, (int)y1, size);
		}
	}
	void glDrawLine(int x0, int y0, int x1, int y1, int size) 
	{
		int x, y, d, t, dx, dy, f = 0;
		if (x1<x0) 
		{
			t = x0; x0 = x1; x1 = t;
			t = y0; y0 = y1; y1 = t;
		}
		if (y1<y0) 
		{
			y0 = -y0; y1 = -y1; f = 10;
		}
		dy = y1-y0; dx = x1-x0;
		if (dx<dy) 
		{
			t = x0; x0 = y0; y0 = t;
			t = x1; x1 = y1; y1 = t;
			t = dx; dx = dy; dy = t; f++;
		}
		gl.glPointSize(size);
		gl.glBegin(GL.GL_POINTS);
		for(x = x0, y = y0, d = 2*dy-dx; x<=x1; x++) 
		{
			switch(f)
			{
				case 1: gl.glVertex2i(y, x); break;
				case 10: gl.glVertex2i(x, -y); break;
				case 11: gl.glVertex2i(y, -x); break;
				default: gl.glVertex2i(x, y);
			}
			if (d<=0) d += 2*dy;
			else { y++; d +=  2*(dy-dx);}
		}
		gl.glEnd();
	}
	void glWait(int interval)
	{
		try {Thread.sleep(interval);} catch (Exception e){}
	}
	public void dispose(GLAutoDrawable glDrawable) {}
}