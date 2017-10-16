import java.io.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
public class JavaAndColorConvertOp
{
	public static void main(String[] args)
	{
		try
		{
			BufferedImage BufferedImage1=ImageIO.read(new File("..\\..\\JavaAndImageProcessing.jpg"));
			ColorConvertOp ColorConvertOp1=new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY),null);
			final BufferedImage BufferedImage2=ColorConvertOp1.filter(BufferedImage1,null);
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