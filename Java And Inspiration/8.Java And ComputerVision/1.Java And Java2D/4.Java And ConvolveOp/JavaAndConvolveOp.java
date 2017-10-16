import java.io.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
public class JavaAndConvolveOp
{
	public static void main(String[] args)
	{
		try
		{
			BufferedImage BufferedImage1=ImageIO.read(new File("..\\..\\JavaAndImageProcessing.jpg"));
			float[] Kernel_Smooth=new float[]
			{
				1f/9f,1f/9f,1f/9f,
				1f/9f,1f/9f,1f/9f,
				1f/9f,1f/9f,1f/9f
			};
			float[] Kernel_Sharpen=new float[]
			{
				0,-1,0,
				-1,5,-1,
				0,-1,0
			};
			float[] Kernel_Edge=new float[]
			{
				0,-1,0,
				-1,4,-1,
				0,-1,0
			};
			Kernel Kernel1=new Kernel(3,3,Kernel_Smooth);
			ConvolveOp ConvolveOp1=new ConvolveOp(Kernel1);
			final BufferedImage BufferedImage2=ConvolveOp1.filter(BufferedImage1,null);
			Frame Frame1=new Frame()
			{
				public void paint(Graphics g)
				{
					((Graphics2D)g).drawImage(BufferedImage2,0,0,this);
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