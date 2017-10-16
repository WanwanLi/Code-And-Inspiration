import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import java.nio.ByteBuffer;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.awt.GLCanvas;

public class J4_6_Texture extends J4_5_Image {

  public void init(GLAutoDrawable glDrawable) {

    super.init(glDrawable); // stars_image[] available
    initTexture(); // texture parameters initiated
  }


  public void display(GLAutoDrawable drawable) {
    gl.glClear(GL2.GL_COLOR_BUFFER_BIT
               |GL2.GL_DEPTH_BUFFER_BIT);

    // texture on a quad covering most of the drawing area
    drawTexture(-2.4f*WIDTH, -2.4f*HEIGHT, -1.9f*WIDTH);

    displayView();
  }


  void initTexture() {

    gl.glTexParameteri(GL2.GL_TEXTURE_2D,
                       GL2.GL_TEXTURE_MIN_FILTER,
                       GL2.GL_NEAREST);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D,
                       GL2.GL_TEXTURE_MAG_FILTER,
                       GL2.GL_NEAREST);
    gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_LUMINANCE,
                    imgW, imgH, 0, GL2.GL_LUMINANCE,
                    GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(img));
  }


  public void drawTexture(float x, float y, float z) {

    gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
                 GL2.GL_REPLACE);

    gl.glEnable(GL2.GL_TEXTURE_2D);
    {
      gl.glBegin(GL2.GL_QUADS);
      gl.glTexCoord2f(0.0f, 4.0f);
      gl.glVertex3f(x, y, z);
      gl.glTexCoord2f(4.0f, 4.0f);
      gl.glVertex3f(-x, y, z);
      gl.glTexCoord2f(4.0f, 0.0f);
      gl.glVertex3f(-x, -y, z);
      gl.glTexCoord2f(0.0f, 0.0f);
      gl.glVertex3f(x, -y, z);
      gl.glEnd();
    }
    gl.glDisable(GL2.GL_TEXTURE_2D);
  }


  public static void main(String[] args) {
    J4_6_Texture f = new J4_6_Texture();

    f.setTitle("JOGL J4_6_Texture");
    f.setSize(WIDTH, HEIGHT);
    f.setVisible(true);
  }
}
