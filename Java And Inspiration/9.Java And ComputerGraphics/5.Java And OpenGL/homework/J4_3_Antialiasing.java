import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.awt.GLCanvas;

/**
 * <p>Title: Foundations of 3D Graphics Programming : Using JOGL and Java3D </p>
 *
 * <p>Description: antialiasing through blending</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: George Mason University</p>
 *
 * @author Dr. Jim X. Chen
 * @version 1.0
 */
public class J4_3_Antialiasing extends J4_3_TransLight {

  public void drawColorCoord(float xlen, float ylen,
                             float zlen) {
    boolean enabled = false;

    gl.glBlendFunc(GL2.GL_SRC_ALPHA,
                   GL2.GL_ONE_MINUS_SRC_ALPHA);
    gl.glHint(GL2.GL_LINE_SMOOTH, GL2.GL_NICEST);

    if (gl.glIsEnabled(GL2.GL_BLEND)) {
      enabled = true;
    } else {
      gl.glEnable(GL2.GL_BLEND);
    }
    gl.glEnable(GL2.GL_LINE_SMOOTH);
    super.drawColorCoord(xlen, ylen, zlen);
    gl.glDisable(GL2.GL_LINE_SMOOTH);

    // blending is only enabled for coordinates
    if (!enabled) {
      gl.glDisable(GL2.GL_BLEND);
    }
  }


  public static void main(String[] args) {
    J4_3_Antialiasing f = new J4_3_Antialiasing();

    f.setTitle("JOGL J4_3_Antialiasing");
    f.setSize(WIDTH, HEIGHT);
    f.setVisible(true);
  }

}
