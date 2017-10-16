import java.awt.*;
import java.awt.event.*;
public class JavaAndMLSTree
{
	public static void main(String[] args)
	{
		new Frame_MLSTree();
	}
}
class Frame_MLSTree extends Frame
{
	private MLSTree MLSTree1,MLSTree2,MLSTree3,MLSTree4,MLSTree5,MLSTree6,MLSTree7;
	private double X,Y,L,a,w,PI=Math.PI;
	private String S;
	private String[] F,LS;
	private int times;
	public Frame_MLSTree()
	{
		this.times=5;
		this.X=20;
		this.Y=750;
		this.L=3;
		this.a=PI/2;
		this.w=PI/48;
		this.S="G";
		this.F=new String[]{"G","X"};
		this.LS=new String[]{"GFX[+++++GFG][----GFG]","F-XF"};
		this.MLSTree1=new MLSTree(X,Y,L,a,w,S,F,LS,times);
		this.X=250;
		this.Y=750;
		this.L=2.2;
		this.times=6;
		this.w=PI/6;
		this.S="X";
		this.F=new String[]{"X","Y"};
		this.LS=new String[]{"FX[-FYF][+FYF]XF","YFX[+Y][-Y]"};
		this.MLSTree2=new MLSTree(X,Y,L,a,w,S,F,LS,times);
		this.X=400;
		this.times=7;
		this.S="Z";
		this.F=new String[]{"X","Z"};
		this.LS=new String[]{"X[-FFF][+FFF]FX","ZFX[+Z][-Z]"};
		this.MLSTree3=new MLSTree(X,Y,L,a,w,S,F,LS,times);
		this.X=600;
		this.times=5;
		this.a=0;
		this.w=PI/7;
		this.L=4;
		this.S="++++F";
		this.F=new String[]{"F","X","Y"};
		this.LS=new String[]{"FF-[XY]+[XY]","+FY","-FX"};
		this.MLSTree4=new MLSTree(X,Y,L,a,w,S,F,LS,times);
		this.X=700;
		this.times=4;
		this.a=PI/2;
		this.w=PI/8;
		this.L=5;
		this.S="L";
		this.F=new String[]{"L","R"};
		this.LS=new String[]{"LL[+L-L+LR][-L+L-LR]","[R-R++R]"};
		this.MLSTree5=new MLSTree(X,Y,L,a,w,S,F,LS,times);
		this.MLSTree5.replaceIntoF(new String[]{"L","R"});
		this.X=850;
		this.times=4;
		this.a=PI/2;
		this.w=PI/8;
		this.L=5;
		this.S="R";
		this.F=new String[]{"L","R"};
		this.LS=new String[]{"LL","LR[+R][+++R][-R][--R][---R]R"};
		this.MLSTree6=new MLSTree(X,Y,L,a,w,S,F,LS,times);
		this.MLSTree6.replaceIntoF(new String[]{"L","R"});
		this.X=1050;
		this.times=5;
		this.a=PI/2;
		this.w=PI/8;
		this.L=1.5;
		this.S="L";
		this.F=new String[]{"L","R"};
		this.LS=new String[]{"L[-LR-LR-LR]L[+LR+LR+LR]L[LR]","R"};
		this.MLSTree7=new MLSTree(X,Y,L,a,w,S,F,LS,times);
		this.MLSTree7.replaceIntoF(new String[]{"L","R"});
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
		if(MLSTree1!=null)this.MLSTree1.paint(g);
		if(MLSTree2!=null)this.MLSTree2.paint(g);
		if(MLSTree3!=null)this.MLSTree3.paint(g);
		if(MLSTree4!=null)this.MLSTree4.paint(g);
		if(MLSTree5!=null)this.MLSTree5.paint(g);
		if(MLSTree6!=null)this.MLSTree6.paint(g);
		if(MLSTree7!=null)this.MLSTree7.paint(g);
	}
}
class MLSTree
{
	String S;
	private double X,X0,Y,Y0,L,a,a0,w;
	private double PI=Math.PI;
	private LSStack stack=null;
	public MLSTree(double X,double Y,double L,double a,double w,String init,String[] regex,String[] replacement,int times)
	{
		this.X0=X;
		this.Y0=Y;
		this.L=L;
		this.a0=a;
		this.w=w;
		this.S=init;
		int l=regex.length;
		for(int i=0;i<times;i++)for(int j=0;j<l;j++)this.S=S.replaceAll(regex[j],replacement[j]);
	}
	private String replaceAll(String string,String[] regex,String[] replacement)
	{
		String newString="";
		int l=string.length();
		for(int i=0;i<l;i++)
		{
			char c=string.charAt(i);
			String si=c+"";
			int n=regex.length;
			for(int j=0;j<n;j++)
			{
				char c0=regex[j].charAt(0);
				if(c0==c)
				{
					si=replacement[j];
					break;
				}
			}
			newString+=si;
		}
		return newString;
	}
	public void replaceIntoF(String[] s)
	{
		int l=s.length;
		for(int i=0;i<l;i++)this.S=S.replaceAll(s[i],"F");
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