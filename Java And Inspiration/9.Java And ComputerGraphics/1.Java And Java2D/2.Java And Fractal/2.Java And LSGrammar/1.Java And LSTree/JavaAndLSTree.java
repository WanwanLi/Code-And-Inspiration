import java.awt.*;
import java.awt.event.*;
public class JavaAndLSTree
{
	public static void main(String[] args)
	{
		new Frame_LSTree();
	}
}
class Frame_LSTree extends Frame
{
	private LSTree LSTree1,LSTree2,LSTree3,LSTree4,LSTree5,LSTree6;
	private double X,Y,L,a,w,PI=Math.PI;
	private String S,F,LS;
	private int times;
	public Frame_LSTree()
	{		
		this.times=7;
		this.X=100;
		this.Y=750;
		this.L=1;
		this.a=PI/2;
		this.w=PI/6;
		this.S="F";
		this.F="F";
		this.LS="F[-F]F[+F]F";
		this.LSTree1=new LSTree(X,Y,L,a,w,S,F,LS,times);
		this.X=350;
		this.L=2;
		this.times=7;
		this.LS="F[-F]F[+F-F]";
		this.LSTree2=new LSTree(X,Y,L,a,w,S,F,LS,times);
		this.X=550;
		this.times=7;
		this.LS="F[-F][+F]F";
		this.LSTree3=new LSTree(X,Y,L,a,w,S,F,LS,times);
		this.X=750;
		this.times=6;
		this.L=1.5;
		this.LS="F[-F][+F][++F]F[--F]F";
		this.LSTree4=new LSTree(X,Y,L,a,w,S,F,LS,times);
		this.X=1000;
		this.Y=450;
		this.L=15;
		this.times=6;
		this.LS="F[-F]F[+F]-F";
		this.LSTree5=new LSTree(X,Y,L,a,w,S,F,LS,times);
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
		if(LSTree1!=null)this.LSTree1.paint(g);
		if(LSTree2!=null)this.LSTree2.paint(g);
		if(LSTree3!=null)this.LSTree3.paint(g);
		if(LSTree4!=null)this.LSTree4.paint(g);
		if(LSTree5!=null)this.LSTree5.paint(g);
		if(LSTree6!=null)this.LSTree6.paint(g);
	}
}
class LSTree
{
	String S;
	private double X,X0,Y,Y0,L,a,a0,w;
	private double PI=Math.PI;
	private LSStack stack=null;
	public LSTree(double X,double Y,double L,double a,double w,String init,String regex,String replacement,int times)
	{
		this.X0=X;
		this.Y0=Y;
		this.L=L;
		this.a0=a;
		this.w=w;
		this.S=init;
		for(int i=0;i<times;i++)this.S=S.replaceAll(regex,replacement);

	}
	public void paint(Graphics g)
	{
		this.X=X0;
		this.Y=Y0;
		this.a=a0;
		this.stack=new LSStack();
		this.drawLSGrammar(g);

	}
	private void drawLSGrammar(Graphics g)
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