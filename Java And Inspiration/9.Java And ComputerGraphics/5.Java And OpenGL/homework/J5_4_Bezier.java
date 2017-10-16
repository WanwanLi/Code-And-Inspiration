import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

/**


 * <p>Title: Foundations of 3D Graphics Programming : Using JOGL and Java3D </p>
 *
 * <p>Description: use OpenGL evaluators for Bezier curve ...</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: George Mason University</p>
 *
 * @author Dr. Jim X. Chen
 * @version 1.0
 */
public class J5_4_Bezier extends J5_3_Hermite {
  double ctrlpts[] = {0.0, -1.0, -0.5, -1.0, 1.0, -1.0,
                     -1.0, -1.0, 1.0, 1.0, 0.05, 1.0};

  public void drawSphere() {
    int i;

     // specify Bezier curve vertex with:
    //     0<=t<=1, 3 values (x,y,z), and 4-1 degrees
    gl.glMap1d(GL2.GL_MAP1_VERTEX_3, 0, 1, 3, 4, ctrlpts,0);
    gl.glEnable(GL2.GL_MAP1_VERTEX_3);
    gl.glDisable(GL2.GL_LIGHTING);

    gl.glLineWidth(3);
    gl.glColor4f(1f, 1f, 1f, 1f);
    gl.glBegin(GL2.GL_LINE_STRIP);
    for (i = 0; i<=30; i++) {
      gl.glEvalCoord1d(i/30.0); // use OpenGL evaluator
    }
    gl.glEnd();

    // Highlight the control points
    gl.glPointSize(4);
    gl.glBegin(GL2.GL_POINTS);
    gl.glColor4f(1f, 1f, 0f, 1f);
     gl.glVertex3d(ctrlpts[0], ctrlpts[1], ctrlpts[2]);
    gl.glVertex3d(ctrlpts[3], ctrlpts[4], ctrlpts[5]);
    gl.glVertex3d(ctrlpts[6], ctrlpts[7], ctrlpts[8]);
    gl.glVertex3d(ctrlpts[9], ctrlpts[10], ctrlpts[11]);
    gl.glEnd();

    // draw the convex hull as transparent
    gl.glEnable(GL2.GL_BLEND);
    gl.glDepthMask(true);
    gl.glBlendFunc(GL2.GL_SRC_ALPHA,
                   GL2.GL_ONE_MINUS_SRC_ALPHA);
    gl.glBegin(GL2.GL_TRIANGLES);
    gl.glColor4f(0.9f, 0.9f, 0.9f, 0.3f);
    gl.glVertex3d(ctrlpts[0], ctrlpts[1], ctrlpts[2]);
    gl.glVertex3d(ctrlpts[3], ctrlpts[4], ctrlpts[5]);
    gl.glVertex3d(ctrlpts[9], ctrlpts[10], ctrlpts[11]);

    gl.glColor4f(0.9f, 0.0f, 0.0f, 0.3f);
   gl.glVertex3d(ctrlpts[0], ctrlpts[1], ctrlpts[2]);
    gl.glVertex3d(ctrlpts[9], ctrlpts[10], ctrlpts[11]);
    gl.glVertex3d(ctrlpts[6], ctrlpts[7], ctrlpts[8]);

    gl.glColor4f(0.0f, 0.9f, 0.0f, 0.3f);
    gl.glVertex3d(ctrlpts[0], ctrlpts[1], ctrlpts[2]);
    gl.glVertex3d(ctrlpts[6], ctrlpts[7], ctrlpts[8]);
    gl.glVertex3d(ctrlpts[3], ctrlpts[4], ctrlpts[5]);

    gl.glColor4f(0.0f, 0.0f, 0.9f, 0.3f);
    gl.glVertex3d(ctrlpts[3], ctrlpts[4], ctrlpts[5]);
    gl.glVertex3d(ctrlpts[6], ctrlpts[7], ctrlpts[8]);
    gl.glVertex3d(ctrlpts[9], ctrlpts[10], ctrlpts[11]);

    gl.glEnd();

    gl.glDepthMask(false);
    // for the background texture
    gl.glBindTexture(GL2.GL_TEXTURE_2D, STARS_TEX[0]);
  }


  public static void main(String[] args) {
    J5_4_Bezier f = new J5_4_Bezier();

    f.setTitle("JOGL J5_4_Bezier");
    f.setSize(WIDTH, HEIGHT);
    f.setVisible(true);
  }
}
