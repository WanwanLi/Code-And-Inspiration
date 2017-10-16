import java.awt.Graphics;
import java.awt.Color;
import java.applet.Applet;
public class Hello extends Applet
{
	
	public void paint(Graphics g)
	{
		g.setColor(Color.red);
		g.drawString("Hello",100,200);
	}
}