import java.io.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.imageio.*;
public class ImageBox
{
	public static void main(String[] args)
	{
		try
		{
			int frameWidth=900,frameHeight=700;
			BufferedImage image=ImageIO.read(new File(args[0]));
			double sX=(frameWidth+0.0)/image.getWidth();
			double sY=(frameHeight+0.0)/image.getHeight();
			AffineTransform AffineTransform1=new AffineTransform();
			AffineTransform1.setToScale(sX,sY);
			AffineTransformOp AffineTransformOp1=new AffineTransformOp(AffineTransform1,AffineTransformOp.TYPE_BILINEAR);
			final BufferedImage bufferedImage=AffineTransformOp1.filter(image,null);
			Frame Frame1=new Frame()
			{
				public void paint(Graphics g)
				{
					((Graphics2D)g).drawImage(bufferedImage,0,0,this);
				}
			};
			Frame1.setBounds(30,30,frameWidth,frameHeight);
			Frame1.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
			});
			Frame1.setResizable(false);
			Frame1.setVisible(true);		
		}catch(Exception e){e.printStackTrace();}
	}
}