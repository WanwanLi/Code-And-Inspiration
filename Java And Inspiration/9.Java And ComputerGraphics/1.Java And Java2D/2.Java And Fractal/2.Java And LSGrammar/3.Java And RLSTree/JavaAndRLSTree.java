import java.awt.*;
import java.awt.event.*;
public class JavaAndRLSTree
{
	public static void main(String[] args)
	{
		new Frame_RLSTree();
	}
}
class Frame_RLSTree extends Frame
{
	public Frame_RLSTree()
	{
		S="F";
		LS1="F[-F]F[+F]F";
		LS2="F[-F]F[+F[-F]]";
		LS3="FF+[+F-F-F]-[-F+F+F]"; 
		for(int i=0;i<5;i++)S=RLSGrammar(S,"F");
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.setVisible(true);
	}
	public void paint(Graphics g)
	{
		X=300;
		Y=650;
		L=5;
		a=PI/2;
		w=PI/6;
		stack=new LSStack();
		drawLSGrammar(S,g);
	}
	public String RLSGrammar(String S,String F)
	{
		double R=Math.random();
		if(R<=0.3)return S.replaceAll(F,LS1);
		else if(R>0.3&&R<=0.6)return S.replaceAll(F,LS2);
		else return S.replaceAll(F,LS3);
	}
	private double a,w,X,Y,L;
	private double PI=3.1415926;
	private String S,LS1,LS2,LS3;
	private LSStack stack=null;
	public void drawLSGrammar(String S,Graphics g)
	{
		for(int i=0;i<S.length();i++)
		{
			switch(S.charAt(i))
			{
				case 'F':g.drawLine((int)X,(int)Y,(int)(X+L*Math.cos(a)),(int)(Y-L*Math.sin(a)));
				case 'f':X+=L*Math.cos(a);Y-=L*Math.sin(a);break;
				case '+':a+=w;if(a>2*PI)a-=2*PI;if(a<0)a+=2*PI;break;
				case '-':a-=w;if(a>2*PI)a-=2*PI;if(a<0)a+=2*PI;break;
				case '[':stack.Push(X,Y,L,a,w);break;
				case ']':LSNode l=stack.Pop();X=l.X;Y=l.Y;L=l.L;a=l.a;w=l.w;break;
				default:break;
			}
		}
	}	
}
class LSNode
{
	public double X;
	public double Y;
	public double L;
	public double a;	
	public double w;
	public LSNode Next;	
	public LSNode(double x,double y,double l,double A,double W)
	{
		X=x;Y=y;L=l;a=A;w=W;
	}	
}
class LSStack
{
	LSNode Head;
	public LSStack()
	{
		Head=null;
	}
	public void Push(double x,double y,double l,double A,double W)
	{
		LSNode ls=new LSNode(x,y,l,A,W);
		ls.Next=Head;
		Head=ls;
	}
	public LSNode Pop() 
	{
		LSNode ls=Head;
		Head=Head.Next;
		return ls;
	}
}