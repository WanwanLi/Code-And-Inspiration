import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.fixedfunc.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.awt.GLCanvas;

public class JavaAndGLQuad
{
	public static void main(String[] args) 
	{
		GLCanvas canvas = new GLCanvas();
		final Frame frame = new Frame("Java And GLQuad");
		final Animator animator = new Animator(canvas);
		canvas.addGLEventListener(new GLDrawableListener());
		frame.add(canvas);
		frame.setSize(640, 480);
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
	GL2 gl;
	float glRotatef = 0.0f;
	float rotateSpeed = 0.2f;
	public void init(GLAutoDrawable glDrawable) 
	{
		gl = glDrawable.getGL().getGL2();
		gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}

	public void display(GLAutoDrawable glDrawable)
	{
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -5.0f);
 
		gl.glRotatef(glRotatef, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(glRotatef, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(glRotatef, 0.0f, 0.0f, 1.0f);
		glRotatef += rotateSpeed;
 
		gl.glBegin(GL2.GL_QUADS);       
		gl.glColor3f(0.0f, 1.0f, 1.0f);
		gl.glVertex3f(-1.0f, 1.0f, 0.0f);
		gl.glVertex3f( 1.0f, 1.0f, 0.0f);
		gl.glVertex3f( 1.0f,-1.0f, 0.0f);
		gl.glVertex3f(-1.0f,-1.0f, 0.0f);
		gl.glEnd(); 
	}
 
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) 
	{
		float aspect = (float) width / (float) height;
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		float fh = 0.5f, fw = fh * aspect;
		gl.glFrustumf(-fw, fw, -fh, fh, 1.0f, 1000.0f);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
 
	public void dispose(GLAutoDrawable glDrawable) {}
}
