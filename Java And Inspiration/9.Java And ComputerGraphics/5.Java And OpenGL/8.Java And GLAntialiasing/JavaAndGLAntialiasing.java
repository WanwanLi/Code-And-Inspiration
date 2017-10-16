import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.awt.GLCanvas;

public class JavaAndGLAntialiasing
{
	public static void main(String[] args) 
	{
		GLCanvas canvas = new GLCanvas();
		GLDrawableListener listener = new GLDrawableListener();
		final Frame frame = new Frame("Java And GLAntialiasing");
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
	vec4 backgroundColor = new vec4(0,0,0,1);
	public void init(GLAutoDrawable glDrawable) 
	{
		gl = glDrawable.getGL().getGL2();
		gl.glDrawBuffer(GL.GL_FRONT_AND_BACK);
		gl.glClearColor
		(
			(float)backgroundColor.x,
			(float)backgroundColor.y,
			(float)backgroundColor.z,
			(float)backgroundColor.w
		);
	}
	public void display(GLAutoDrawable drawable) 
	{
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		glDrawRandomLine(10);
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
	private void glDrawRandomLine(int maxSize)
	{
		vec4 line = vec4.random(width, height, width, height);
		vec4 color = vec4.random(1, 1, 1, 1); color.w = 1;
		int size = 5 + (int)(maxSize*Math.random());
		glDrawLine(line, color, size);
	}
	void glDrawLine(vec4 line, vec4 color, int size) 
	{
		int x, y, d, t, dx, dy, f = 0;
		int x0 = line.x(), y0 = line.y();
		int x1 = line.z(), y1 = line.w();
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
		gl.glPointSize(1);
		gl.glBegin(GL.GL_POINTS);
		double dr = Math.sqrt(0.0+dx*dx+dy*dy);
		double D = 0, sinA = dy/dr, cosA = dx/dr, R = size/2*cosA;
		for(x = x0, y = y0, d = 2*dy-dx; x<=x1; x++) 
		{
			for(int i = -size/2; i <= size/2; i++)
			{
				color.w = 1 - Math.abs(D - i*cosA)/R;
				glColor2v(backgroundColor, color);
				glVertex3i(x, y+i, f);
			}
			if (d<=0) { d += 2*dy; D += sinA; }
			else { y++; d +=  2*(dy-dx); D += sinA-cosA;}
		}
		gl.glEnd();
	}
	void glColor2v(vec4 src, vec4 dest)
	{
		vec4 color = src.blend(dest);
		gl.glColor3f
		(
			(float)color.x, 
			(float)color.y,
			(float)color.z
		);
	}
	void glVertex3i(int x, int y, int f)
	{
		switch(f)
		{
			case 1: gl.glVertex2i(y, x); break;
			case 10: gl.glVertex2i(x, -y); break;
			case 11: gl.glVertex2i(y, -x); break;
			default: gl.glVertex2i(x, y);
		}
	}
	void glWait(int interval)
	{
		try {Thread.sleep(interval);} catch (Exception e){}
	}
	public void dispose(GLAutoDrawable glDrawable) {}
}