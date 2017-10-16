import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.awt.GLCanvas;

public class JavaAndGLRobotArm2D
{
	public static void main(String[] args) 
	{
		GLCanvas canvas = new GLCanvas();
		GLDrawableListener listener = new GLDrawableListener();
		final Frame frame = new Frame("Java And RobotArm2D");
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
	vec3[] robotArmColors = new vec3[]
	{
		vec3.unit(100), vec3.unit(010),  vec3.unit(001)
	};
	vec3[] robotArmJoints = new vec3[]
	{
		vec3.zero(), vec3.zero(), vec3.zero(), vec3.zero()
	};
	double globalSize = 0.3, globalSpeed = 0.01;
	double[] robotArmAngles = {-40, -40, 60};
	double[] robotArmSpeed = {1, 1.2, -2};
	int[] robotArmWidth = {20, 10, 5};
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
		glDrawRobotArm2D(robotArmJoints, robotArmAngles,  robotArmColors, robotArmWidth);
		for(int i=0; i<robotArmAngles.length; robotArmAngles[i]+=globalSpeed*robotArmSpeed[i++]);
	}
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) 
	{
		this.setSize(width, height);
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-width/2, width/2, -height/2, height/2, -1.0, 1.0);
		for(int i=0; i<robotArmJoints.length; robotArmJoints[i].x=i*height*globalSize, i++);
	}
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	void glDrawRobotArm2D(vec3[] joints, double[] angles,  vec3[] colors, int[] width) 
	{
		glLoadIdentity();
		glRotate1d(angles[0]);
		glDrawLine(joints[0], joints[1], colors[0], width[0]);

		glTranslate1v(joints[1]);
		glRotate1d(angles[1]);
		glTranslate1dv(-1, joints[1]);
		glDrawLine(joints[1], joints[2], colors[1], width[1]);

		glTranslate1v(joints[2]);
		glRotate1d(angles[2]);
		glTranslate1dv(-1, joints[2]);
		glDrawLine(joints[2], joints[3], colors[2], width[2]);
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
	mat3 glMatrix = new mat3();
	int glMatrixIndex = -1, glMatrixSize = 64; 
	mat3[] glMatrixStack = new mat3[glMatrixSize];
	void glLoadIdentity()
	{
		glMatrix.identity();
	}
	void glTranslate1v(vec3 v)
	{
		glMatrix.translate(v.x, v.y);
	}
	void glTranslate1dv(double k, vec3 v)
	{
		glMatrix.translate(k*v.x, k*v.y);
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
