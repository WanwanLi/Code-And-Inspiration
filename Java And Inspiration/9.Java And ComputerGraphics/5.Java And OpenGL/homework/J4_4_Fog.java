import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.awt.GLCanvas;

public class J4_4_Fog extends J4_3_Antialiasing {

  public void init(GLAutoDrawable glDrawable) {
    float fogColor[] = {0.4f, 0.4f, 0.4f, 1f};

    super.init(glDrawable);

    gl.glClearColor(0.4f, 0.4f, 0.4f, 1.0f);

    // lighting is calculated with viewpoint at origin
    // and models are transformed by MODELVIEW matrix
    // in our example, models are moved into -z in PROJECTION
    gl.glEnable(GL2.GL_BLEND);
    gl.glEnable(GL2.GL_FOG);

    gl.glFogi (GL2.GL_FOG_MODE, GL2.GL_EXP);
    // gl.glFogi (GL2.GL_FOG_MODE, GL2.GL_EXP2);
    //gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_LINEAR);
    gl.glFogfv(GL2.GL_FOG_COLOR, fogColor,0);
    gl.glFogf (GL2.GL_FOG_DENSITY, (float)(5/WIDTH));
    gl.glHint(GL2.GL_FOG_HINT, GL2.GL_NICEST);
    //gl.glFogf(GL2.GL_FOG_START, 0.1f*WIDTH);
    //gl.glFogf(GL2.GL_FOG_END, 0.5f*WIDTH);
  }


  public static void main(String[] args) {
    J4_4_Fog f = new J4_4_Fog();

    f.setTitle("JOGL J4_4_Fog");
    f.setSize(WIDTH, HEIGHT);
    f.setVisible(true);
  }
}
