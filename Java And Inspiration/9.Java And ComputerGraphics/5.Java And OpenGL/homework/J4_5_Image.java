import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.awt.GLCanvas;

import java.nio.ByteBuffer;

public class J4_5_Image extends J4_4_Fog {
  static byte[] img;
  static int imgW, imgH, imgType;

  public void init(GLAutoDrawable glDrawable) {

    super.init(glDrawable);

    gl.glDisable(GL2.GL_FOG);

    readImage("STARS.JPG"); // read the image to img[]
    gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
  }


  public void display(GLAutoDrawable drawable) {

    gl.glClear(GL2.GL_COLOR_BUFFER_BIT
               |GL2.GL_DEPTH_BUFFER_BIT);

    drawImage(-1.95f*WIDTH, -1.95f*HEIGHT, -1.99f*WIDTH);
    // remember : gl.glFrustum(-w/4,w/4,-h/4,h/4,w/2,4*w);
    //		gl.glTranslatef(0, 0, -2*w);

    displayView();
  }


  public void readImage(String fileName) {
    File f = new File(fileName);
    BufferedImage bufimg;

    try {
      // read the image into BufferredImage structure
      bufimg = ImageIO.read(f);
      imgW = bufimg.getWidth();
      imgH = bufimg.getHeight();
      imgType = bufimg.getType();
      System.out.println(fileName+" -- BufferedImage WIDTH&HEIGHT: "+imgW+", "+imgH);
      System.out.println("BufferedImage type TYPE_3BYTE_BGR 5; GRAY 10: "+imgType);
      //TYPE_BYTE_GRAY  10
      //TYPE_3BYTE_BGR 	5

      // retrieve the pixel array in raster's databuffer
      Raster raster = bufimg.getData();

      DataBufferByte dataBufByte = (DataBufferByte)raster.
                                   getDataBuffer();
      img = dataBufByte.getData();
      System.out.println("Image data's type TYPE_BYTE 0: "+
                         dataBufByte.getDataType());
      // TYPE_BYTE 0

    } catch (IOException ex) {
      System.exit(1);
    }
  }


  protected void drawImage(float x, float y, float z) {

    gl.glRasterPos3f(x, y, z);
    gl.glDrawPixels(imgW, imgH, GL2.GL_LUMINANCE,
                    GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(img));
  }


  public void displayView() {
    cnt++;
    depth = (cnt/100)%6;
   //System.out.println(cnt); 
   
// cnt = 220; // for color plates

    if (cnt%60==0) {
      dalpha = -dalpha;
      dbeta = -dbeta;
      dgama = -dgama;
    }
  alpha += dalpha;
  beta += dbeta;
  gama += dgama;

    gl.glPushMatrix();
    if (cnt%1000<500 || myCameraView) {
      // look at the solar system from the moon
      myCamera(A, B, C, alpha, beta, gama);
    }

    drawRobot(O, A, B, C, alpha, beta, gama);
    gl.glPopMatrix();

    try {
      Thread.sleep(15);
    } catch (Exception ignore) {}
  }


  public static void main(String[] args) {
    J4_5_Image f = new J4_5_Image();

    f.setTitle("JOGL J4_5_Image");
    f.setSize(530, 555);
    f.setVisible(true);
  }
}
