import javax.swing.*;
import java.awt.*;
import java.applet.*;
import java.awt.Font;
public class Java2D extends Applet
{
	public void paint(Graphics g)
	{
		paintJava2D((Graphics2D)g,200,200,300,300);
	}
	public void paintJava2D(Graphics2D g2D,int x,int y,int width,int height)
	{
		g2D.setPaint(new GradientPaint(x,y,Color.WHITE,x+width,y+height,Color.YELLOW));
		g2D.fillOval(x,y,width,height);
		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.2f));
		g2D.setColor(Color.BLUE);
		g2D.setFont(new Font("Î¢ÈíÑÅºÚ",Font.BOLD,100));
		g2D.rotate(Math.PI/6,x,y);
		g2D.drawString("Java 2D",x,y);						
	}
}
