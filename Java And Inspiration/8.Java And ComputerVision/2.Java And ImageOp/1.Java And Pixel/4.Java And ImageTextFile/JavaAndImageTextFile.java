import java.io.*;
import java.awt.*;
import javax.imageio.*;
import java.awt.image.*;
class ImageTextFile
{
	public void writeDefaultTextFile(String path, String name, int width, int height)
	{
		try
		{
			String pixel="0,0,0,255";
			String fileName=path+"/"+name;
			PrintWriter PrintWriter1=new PrintWriter(fileName);
			for(int i=0;i<height;i++)
			{
				for(int j=0;j<width;j++)
				{
					String index=i+","+j;
					PrintWriter1.println(index+","+pixel);
				}
			}
			PrintWriter1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void writeImageFromTextFile(String path, String name, int width, int height)
	{
		try
		{
			String fileName=path+"/"+name, string;
			int[] pixels=new int[width*height];
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName));
			while((string=BufferedReader1.readLine())!=null)
			{
				String[] values=string.split(",");
				int i = Integer.parseInt(values[0]);
				int j = Integer.parseInt(values[1]);
				int red = Integer.parseInt(values[2]);
				int green = Integer.parseInt(values[3]);
				int blue = Integer.parseInt(values[4]);
				int alpha = Integer.parseInt(values[5]);
				pixels[i*width+j]=(alpha<<24)|(red<<16)|(green<<8)|blue;
			}
			BufferedReader1.close();
			BufferedImage BufferedImage1=this.getBufferedImage(pixels,width,height);
			ImageIO.write(BufferedImage1,"JPG",new File(fileName+".jpg"));
		}
		catch(Exception e){e.printStackTrace();}
	}
	private BufferedImage getBufferedImage(int[] pixels,int width,int height)
	{
		BufferedImage BufferedImage1=new BufferedImage(width,height,1);
		Graphics g=BufferedImage1.getGraphics();
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				int pixel=pixels[i*width+j];
				float alpha=getAlpha(pixel)/255f;
				float red=getRed(pixel)/255f;
				float green=getGreen(pixel)/255f;
				float blue=getBlue(pixel)/255f;
				g.setColor(new Color(red,green,blue,alpha));
				g.drawLine(j,i,j,i);
			}
		}
		return BufferedImage1;
	}
	private int getAlpha(int pixel)
	{
		return (pixel>>24)&0xff;
	}
	private int getRed(int pixel)
	{
		return (pixel>>16)&0xff;
	}
	private int getGreen(int pixel)
	{
		return (pixel>>8)&0xff;
	}
	private int getBlue(int pixel)
	{
		return pixel&0xff;
	}
}
public class JavaAndImageTextFile
{
	public static void main(String[] args)
	{
		String cmd=args[0];
		String path=args[1];
		String name=args[2];
		int width=Integer.parseInt(args[3]);
		int height=Integer.parseInt(args[4]);
		ImageTextFile e=new ImageTextFile();
		if(cmd.equals("-txt"))e.writeDefaultTextFile(path,name,width,height);
		else if(cmd.equals("-img"))e.writeImageFromTextFile(path,name,width,height);
		else System.out.println("Error: Unknown cmd: "+args[0]+". Please set cmd as -txt or -img");
	}
}
