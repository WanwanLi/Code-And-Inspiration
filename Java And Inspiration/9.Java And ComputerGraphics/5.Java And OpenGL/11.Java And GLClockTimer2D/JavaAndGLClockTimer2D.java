import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.awt.GLCanvas;

public class JavaAndGLClockTimer2D
{
	public static void main(String[] args) 
	{
		GLCanvas canvas = new GLCanvas();
		GLDrawableListener listener = new GLDrawableListener();
		final Frame frame = new Frame("Java And GLClockTimer2D");
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
	double PI = 3.1415926;
	vec3 glTime = vec3.zero();
	vec3 clockCenter = new vec3(100, 0, 0);
	vec3 clockScale = vec3.zero();
	vec3 clockWidth = new vec3(10, 5, 2);
	vec3 clockCircle =  new vec3(0, 5, 100);
	GL2 gl; int width = 640, height = 480;
	vec3[] clockColors = new vec3[]
	{
		vec3.unit(100), vec3.unit(010),  vec3.unit(001), vec3.unit(101)
	};
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
		glDrawClock2D(clockCenter, clockScale, clockWidth, clockColors);
		glWait(10);
	}
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) 
	{
		this.setSize(width, height);
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-width/2, width/2, -height/2, height/2, -1.0, 1.0);
		this.clockScale.set(height/8, height/6, height/4);
		this.clockCircle.x =  height/4;
	}
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	void glDrawClock2D(vec3 center, vec3 scale, vec3 width, vec3[] colors) 
	{
		glGetTime();
		glLoadIdentity();
		glTranslate2d(center.x,  center.y);

		glPushMatrix();
		glScale2d(scale.x, scale.x);
		glRotate1d(-glTime.x*PI/6);
		glDrawLine(vec3.unit(000), vec3.unit(010), colors[0], width.x());
		glPopMatrix();
		
		glPushMatrix();
		glScale2d(scale.y, scale.y);
		glRotate1d(-glTime.y*PI/30);
		glDrawLine(vec3.unit(000), vec3.unit(010), colors[1], width.y());
		glPopMatrix();
		
		glPushMatrix();
		glScale2d(scale.z, scale.z);
		glRotate1d(-glTime.z*PI/30);
		glDrawLine(vec3.unit(000), vec3.unit(010), colors[2], width.z());
		glPopMatrix();

		glDrawCircle(clockCircle.x, colors[3], clockCircle.y(), clockCircle.z());
	}
	void glDrawCircle(double radius, vec3 color, int width, int length) 
	{
		int i = 0; vec3 v0 = vec3.zero(), v1 = vec3.zero();
		double x0, y0, x1, y1, t, r = radius, dt = 2*PI/length;
		for(x0 = r, y0 = 0, t=dt; i<length; t+=dt,  x0 = x1, y0 = y1, i++) 
		{
			x1 = r*Math.cos(t); 
			y1 = r*Math.sin(t);
			v0.set(x0, y0, 1); v1.set(x1, y1, 1);
			glDrawLine(v0, v1, color, width);
		}
	}
	void glDrawLine(vec3 v1, vec3 v2, vec3 color, int width) 
	{
		gl.glColor3d(color.x, color.y, color.z);
		gl.glLineWidth(width);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3dv(glMatrixVertex(v1), 0);
		gl.glVertex3dv(glMatrixVertex(v2), 0);
		gl.glEnd();
	}
	void glWait(int interval)
	{
		try {Thread.sleep(interval);} catch (Exception e){}
	}
	void glGetTime()
	{
		long time = System.currentTimeMillis()/1000;
		glTime.z = time%60; time/=60;
		glTime.y = time%60+glTime.z/60; time/=60;
		glTime.x = (time%12)+7+glTime.y/60;
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
