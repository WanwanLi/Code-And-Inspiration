import java.io.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.imageio.*;
public class JavaAndAffineTransformOp
{
	public static void main(String[] args)
	{
		try
		{
			BufferedImage BufferedImage1=ImageIO.read(new File("..\\..\\JavaAndImageProcessing.jpg"));
			AffineTransform AffineTransform1=new AffineTransform();
			AffineTransform1.setToScale(0.7,0.7);
			AffineTransformOp AffineTransformOp1=new AffineTransformOp(AffineTransform1,AffineTransformOp.TYPE_BILINEAR);
			final BufferedImage BufferedImage2=AffineTransformOp1.filter(BufferedImage1,null);
			AffineTransform1.setToTranslation(250,200);
			AffineTransformOp1=new AffineTransformOp(AffineTransform1,AffineTransformOp.TYPE_BILINEAR);
			final BufferedImage BufferedImage3=AffineTransformOp1.filter(BufferedImage2,null);
			AffineTransform1.setToRotation(Math.PI/6,250,200);
			AffineTransformOp1=new AffineTransformOp(AffineTransform1,AffineTransformOp.TYPE_BILINEAR);
			final BufferedImage BufferedImage4=AffineTransformOp1.filter(BufferedImage3,null);
			Frame Frame1=new Frame()
			{
				public void paint(Graphics g)
				{
					((Graphics2D)g).drawImage(BufferedImage4,0,0,this);
				}
			};
			Frame1.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
			});
			Frame1.setVisible(true);		
		}catch(Exception e){e.printStackTrace();}
	}
}