import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.awt.GLCanvas;

public class JavaAndGLTriangle
{
	public static void main(String[] args) 
	{
		GLCanvas canvas = new GLCanvas();
		GLDrawableListener listener = new GLDrawableListener();
		final Frame frame = new Frame("Java And GLTriangle");
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
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glDrawBuffer(GL.GL_FRONT_AND_BACK);
	}
	public void display(GLAutoDrawable drawable) 
	{
		gl.glClear(GL.GL_COLOR_BUFFER_BIT); 
		float red = (float)Math.random();
		float green = (float)Math.random();
		float blue = (float)Math.random();
		gl.glColor3f(red, green, blue);
		glDrawRandomTriangleCircle(5);
		glDrawRandomTriangle();
		glWait(1000);
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
	public void glDrawRandomTriangleCircle(int maxSize) 
	{
		vec3[] v = new vec3[3];
		for (int i = 0; i < 3; i++)
		{ 
			v[i]=vec3.random(Math.random()*width, Math.random()*height, 0);
		}
		int size = 1 + (int)(maxSize*Math.random());
		glDrawTriangleCircle(v, size);
		glWait(50);
	}
	public void glDrawRandomTriangle()
	{
		vec3[] v = new vec3[3];
		for (int i = 0; i < 3; i++)
		{ 
			v[i]=vec3.random(Math.random()*width, Math.random()*height, 0);
		}
		glDrawTriangle(v);
		glWait(50);
	}
	public void glDrawTriangle(vec3[] v) 
	{
		int[] s = new vec3(v[0].y, v[1].y, v[2].y).sortIndices();
		int ymin = s[0], ymid = s[1], ymax = s[2], y = v[ymin].y(), dy;
		double x0 = v[ymin].x, x1 = x0, dx0 = 0, dx1 = 0;
		if ((dy = v[ymax].y() - v[ymin].y()) > 0) dx1 = (v[ymax].x - v[ymin].x) / dy; else return;
		if ((dy = v[ymid].y() - v[ymin].y()) > 0) dx0 = (v[ymid].x - v[ymin].x) / dy; else x0 = v[ymid].x;
		for (y = v[ymin].y(); y < v[ymid].y(); glDrawLine(x0, x1, y), x0+=dx0, x1+=dx1, y++);
		if ((dy = v[ymax].y() - v[ymid].y()) > 0) dx0 = (v[ymax].x - v[ymid].x) / dy;
		for (y = v[ymid].y(); y < v[ymax].y(); glDrawLine(x0, x1, y), x0+=dx0, x1+=dx1, y++);
	}
	void glDrawTriangleCircle(vec3[] v, int size)
	{
		glDrawLine(v[0].x(), v[0].y(), v[1].x(), v[1].y(), size);
		glDrawLine(v[1].x(), v[1].y(), v[2].x(), v[2].y(), size);
		glDrawLine(v[2].x(), v[2].y(), v[0].x(), v[0].y(), size);
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
	void glDrawLine(double x1, double x2, int y) 
	{
		gl.glPointSize(1);
		gl.glBegin(GL.GL_POINTS);
		if (x1 > x2) {double t = x2; x2 = x1; x1 = t;}
		for (int x = (int)x1; x <= (int)x2; gl.glVertex2i(x, y), x++);
		gl.glEnd();
	}
	void glWait(int interval)
	{
		try {Thread.sleep(interval);} catch (Exception e){}
	}
	public void dispose(GLAutoDrawable glDrawable) {}
}