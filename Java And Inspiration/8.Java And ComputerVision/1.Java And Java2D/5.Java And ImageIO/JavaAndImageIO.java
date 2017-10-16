import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
public class JavaAndImageIO
{
	public static void main(String[] args)
	{
		try
		{
			BufferedImage BufferedImage1=ImageIO.read(new File("..\\..\\JavaAndImageProcessing.jpg"));
			Graphics g=BufferedImage1.getGraphics();
			g.setColor(Color.red);
			g.setFont(new Font(null,Font.BOLD,50));
			g.drawString("Java And ImageIO",10,100);
			System.out.println("Begin writing..");
			ImageIO.write(BufferedImage1,"JPG",new File("JavaAndIamgeIO.jpg"));
			System.out.println("Finished..");	
		}
		catch(Exception e){e.printStackTrace();}
	}
}