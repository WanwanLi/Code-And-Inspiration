import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.awt.GLCanvas;

public class J3_2_Emission extends J2_13_TravelSolar {
  float white[] = {1, 1, 1, 1};

  public void init(GLAutoDrawable glDrawable) {

    super.init(glDrawable);

    gl.glEnable(GL2.GL_LIGHTING);
    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, white,0);
  }


  public static void main(String[] args) {
    J3_2_Emission f = new J3_2_Emission();

    f.setTitle("JOGL J3_2_Emission");
    f.setSize(WIDTH, HEIGHT);
    f.setVisible(true);
  }
}
