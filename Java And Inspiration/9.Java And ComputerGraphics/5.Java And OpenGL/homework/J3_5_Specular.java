import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.awt.GLCanvas;

public class J3_5_Specular extends J3_4_Diffuse {

  public void init(GLAutoDrawable glDrawable) {

    super.init(glDrawable);

    gl.glEnable(GL2.GL_LIGHTING);
    gl.glEnable(GL2.GL_NORMALIZE);

    gl.glEnable(GL2.GL_LIGHT0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position,0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, white,0);

    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, white,0);
    gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 50.0f);

    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, black,0);
    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, black,0);
    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, black,0);
  }


  public static void main(String[] args) {
    J3_5_Specular f = new J3_5_Specular();

    f.setTitle("JOGL J3_5_Specular");
    f.setSize(WIDTH, HEIGHT);
    f.setVisible(true);
  }
}
