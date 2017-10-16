import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.awt.GLCanvas;

public class JavaAndGLConeSystem
{
	public static void main(String[] args) 
	{
		GLCanvas canvas = new GLCanvas();
		GLDrawableListener listener = new GLDrawableListener();
		final Frame frame = new Frame("Java And GLConeSystem");
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
	GL2 gl; GLUT glut; int width = 640, height = 480;
	vec3[] units = 
	{
		vec3.unit(100), vec3.unit(010), vec3.unit(001), 
		vec3.unit(-100), vec3.unit(0-10), vec3.unit(00-1)
	};
	double  globalScale = 2.0;
	int sphereDepth = 1, timer = 0;
	vec3 sphereScale, sphereTranslate;
	vec3 sphereSpeed = new vec3(0.0, 1.0, 2.0);
	vec3 sphereSizes = new vec3(0.15, 0.05, 0.025);
	vec3 sphereDistances = new vec3(0.0, 0.25, 0.1);
	vec3 sphereRotate = vec3.zero(), coordScale = new vec3(2, 1.5, 0.001);
	vec3[] coordColors = {vec3.unit(100), vec3.unit(010), vec3.unit(001)};
	vec3[] coneUnits = {vec3.unit(100), vec3.unit(010), vec3.unit(-100), vec3.unit(0-10)};
	public void init(GLAutoDrawable glDrawable) 
	{
		glut = new GLUT();
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
		gl.glTranslated(0, -height*0.25, 0);
		glDrawConeSystem(sphereScale, sphereTranslate, sphereRotate, 45); 
		sphereDepth = 1+(timer++/100)%4; 
		sphereRotate.inc(sphereSpeed);
		if (timer % 100 < 50) 
		{
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
		}
		else gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) 
	{
		this.setSize(width, height);
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-width/2, width/2, -height/2, height/2, -width, width);
		this.sphereTranslate = sphereDistances.mul(globalScale*height);
		this.sphereScale = sphereSizes.mul(globalScale*height);
	}
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	void glDrawConeSystem(vec3 scale, vec3 translate, vec3 rotate, double tilt)
	{
		glDrawCoordSys(scale);
		gl.glPushMatrix();
		glTransformEarth(rotate, tilt, translate);
		glDrawEarth(scale, rotate);
		glDrawCone(translate);
		glDrawMoon(scale, translate, rotate);
		gl.glPopMatrix();
	}
	void  glDrawCoordSys(vec3 scale)
	{
		gl.glPushMatrix();
		gl.glScaled(scale.x, scale.x, scale.x);
		glDrawCoordSystem(coordScale, coordColors);
		gl.glPopMatrix();
	}
	void  glTransformEarth(vec3 rotate, double tilt, vec3 translate)
	{
		gl.glRotated(rotate.y, 0, 1, 0);
		gl.glRotated(tilt, 0, 0, 1);
		gl.glColor3d(1, 1, 0);
		gl.glLineWidth(5);
		glDrawLine(vec3.unit(0), vec3.unit(010).mul(translate.y));
		gl.glLineWidth(1);
		gl.glTranslated(0, translate.y, 0);
	}
	void  glDrawEarth(vec3 scale, vec3 rotate)
	{
		gl.glPushMatrix();
		gl.glRotated(rotate.y+rotate.z, 0, 1, 0);
		gl.glScaled(scale.y, scale.y, scale.y);
		glDrawCoordSystem(coordScale, coordColors);
		glDrawSubdivideSphere(units, sphereDepth);
		gl.glPopMatrix();
	}
	void  glDrawCone(vec3 translate)
	{
		gl.glPushMatrix();
		gl.glScaled(translate.y, translate.y, translate.y);
		gl.glRotated(90, 1, 0, 0);
		glDrawSubdivideCone(0.125, 1, coneUnits, sphereDepth-1);
		gl.glPopMatrix();
	}
	void  glDrawMoon(vec3 scale, vec3 translate, vec3 rotate)
	{
		gl.glPushMatrix();
		gl.glRotated(rotate.z, 0, 1, 0);
		gl.glTranslated(translate.z, 0, 0);
		gl.glScaled(scale.z, scale.z, scale.z);
		glDrawCoordSystem(coordScale, coordColors);
		glDrawSubdivideSphere(units, sphereDepth-1);
		gl.glPopMatrix();
	}
	void glDrawCoordSystem(vec3 scale, vec3[] colors)
	{
		gl.glLineWidth(scale.x());
		gl.glColor3d(colors[0].x, colors[0].y, colors[0].z);
		glDrawLine(vec3.unit(000), vec3.unit(100).mul(scale.y));
		gl.glPushMatrix();
		gl.glTranslated(scale.y, 0, 0);
		gl.glScaled(scale.z, scale.z, scale.z);
		glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, 'X');
		gl.glPopMatrix();

		gl.glColor3d(colors[1].x, colors[1].y, colors[1].z);
		glDrawLine(vec3.unit(000), vec3.unit(010).mul(scale.y));
		gl.glPushMatrix();
		gl.glTranslated(0, scale.y, 0);
		gl.glScaled(scale.z, scale.z, scale.z);
		glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, 'Y');
		gl.glPopMatrix();

		gl.glColor3d(colors[2].x, colors[2].y, colors[2].z);
		glDrawLine(vec3.unit(000), vec3.unit(001).mul(scale.y));
		gl.glPushMatrix();
		gl.glTranslated(0, 0, scale.y);
		gl.glScaled(scale.z, scale.z, scale.z);
		glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, 'Z');
		gl.glPopMatrix();
		gl.glLineWidth(1);
	}
	void glDrawSubdivideCone(double radius, double height, vec3[] vertices, int depth) 
	{
		glDrawSubdivideCone(radius, height, vertices[0], vertices[1], depth);
		glDrawSubdivideCone(radius, height, vertices[1], vertices[2], depth);
		glDrawSubdivideCone(radius, height, vertices[2], vertices[3], depth);
		glDrawSubdivideCone(radius, height, vertices[3], vertices[0], depth);
	}
	void glDrawSubdivideCone(double radius, double height, vec3 v1, vec3 v2, int depth) 
	{
		if (depth == 0) 
		{
			gl.glColor3d(v1.x* v1.x, v1.y * v1.y, v1.z *v1.z);
			vec3 v00 = vec3.unit(000);
			vec3 v11 = v1.mul(radius);
			vec3 v22 = v2.mul(radius);
			vec3 v33 = vec3.unit(001).mul(height);
			glDrawTriangle(v00, v11, v22);
			glDrawTriangle(v11, v22, v33);
			return;
		}
		vec3 v12 = v1.add(v2); v12.normalize();
		glDrawSubdivideCone(radius, height, v1, v12, depth - 1);
		glDrawSubdivideCone(radius, height, v12, v2, depth - 1);
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
		if (depth <= 0) 
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
	void glDrawLine(vec3 v1, vec3 v2) 
	{
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3dv(v1.xyz(), 0);
		gl.glVertex3dv(v2.xyz(), 0);
		gl.glEnd();
	}
	public void dispose(GLAutoDrawable glDrawable) {}
}
