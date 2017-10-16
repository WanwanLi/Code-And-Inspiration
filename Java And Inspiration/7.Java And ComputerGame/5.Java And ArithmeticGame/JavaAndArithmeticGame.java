import java.awt.*;
import java.awt.event.*;

public class JavaAndArithmeticGame
{
	public static void main(String[] args)
	{
		new ArithmeticGame();
	}
}
class ArithmeticGame extends Frame implements KeyListener
{
	private int x0=50,y0=50,left=x0+50,top=y0+125;
	private Image backgroundImage,foregroundImage;
	private Color color_light=new Color(247,235,213);
	private Color color_dark=new Color(185,154,123);
	private String imputString="",expressionsString="";
	private String[] expressions=new String[]{"Nothig.."};
	public ArithmeticGame()
	{
		this.backgroundImage=Toolkit.getDefaultToolkit().getImage("background.jpg");
		this.foregroundImage=Toolkit.getDefaultToolkit().getImage("foreground.gif");
		this.setBounds(x0,y0,600,600);
		this.setResizable(false);
		this.setVisible(true);
		this.addKeyListener(this);
	}
	public void paint(Graphics g)
	{
		g.drawImage(backgroundImage,0,0,this);
		this.drawString(g,imputString,x0+50,y0+40+22,30);
		for(int i=0;i<expressions.length;i++)this.drawString(g,expressions[i],left,top+i*20,20);
		this.drawRect(g,x0+50,y0+40,400,30,3);
		this.drawRect(g,x0+50,y0+40+30+40,400,400,3);
		g.drawImage(foregroundImage,2,22,this);
	}
	private void drawString(Graphics g,String string,int x,int y,int size)
	{
		g.setColor(color_dark);
		g.setFont(new Font(null,Font.PLAIN,size));
		g.drawString(string,x+3,y+2);
		g.setColor(color_light);
		g.setFont(new Font(null,Font.PLAIN,size));
		g.drawString(string,x,y);
	}
	private void drawRect(Graphics g,int x,int y,int w,int h,int size)
	{
		g.setColor(color_dark);
		for(int i=0;i<size;i++)g.drawRect(x-i,y-i,w+2*i,h+2*i);
		g.setColor(color_light);
		for(int i=size;i<2*size;i++)g.drawRect(x-i,y-i,w+2*i,h+2*i);
	}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e)
	{
		char c=e.getKeyChar();
		if((c>='0'&&c<='9')||c=='_'||c=='=')this.imputString+=e.getKeyChar();
		else
		{
			String t=KeyEvent.getKeyText(c);
			if(t.equals("Backspace"))
			{
				int length=imputString.length();
				if(length>0)this.imputString=imputString.substring(0,length-1);
			}
			else if(t.equals("Enter"))
			{
				int n=imputIsValid();
				if(n==0)this.expressionsString="Invalid Imput..";
				else
				{
					int[] params=this.getParameters(n);
					int result=this.getResult();
					Arithmetic arithmetic=new Arithmetic(params,result);
					this.expressions=arithmetic.getExpressions();
				}
			}
		}
		this.repaint();
	}
	private int imputIsValid()
	{
		boolean isNotNumber=false;
		int a=0,b=0,e=0,l=imputString.length();
		for(int i=0;i<l;i++)
		{
			char c=imputString.charAt(i);
			if(c=='_')
			{
				if(isNotNumber)return 0;
				a++;
			}
			else if(c=='=')
			{
				if(isNotNumber)return 0;
				b++;
				e=i;
			}
			else isNotNumber=false;
		}
		if(a>0&&b==1&&e<l-1)return a;
		else return 0;
	}
	private int[] getParameters(int n)
	{
		int[] params=new int[n+1];
		int j=0,l=imputString.length();
		String s="";
		for(int i=0;i<l;i++)
		{
			char c=imputString.charAt(i);
			if(c>='0'&&c<='9')s+=c;
			else 
			{
				params[j++]=Integer.parseInt(s);
				if(c=='_')s="";
				else return params;
			}
		}
		return params;
	}
	private int getResult()
	{
		int result=0;
		int l=imputString.length();
		String s="";
		for(int i=l-1;i>0;i--)
		{
			char c=imputString.charAt(i);
			if(c=='=')return Integer.parseInt(s);
			else s=c+s;
		}
		return 0;
	}
}
class Arithmetic
{
	int n,r,q;
	int[] p;
	boolean[] searched;
	String expression="";
	int MAX_NUMBER=20;
	public Arithmetic(int[] params,int result)
	{
		this.p=params;
		this.r=result;
		this.n=params.length;
		this.searched=new boolean[n];
		for(int i=0;i<n;i++)this.searched[i]=false;
	}
	public void getRexpression()
	{
		this.q=0;
		for(int i=0;i<n;i++)
		{
			String exp=""+p[i];
			this.searched[i]=true;
			this.findRexpression(exp,p[i],n-1);
			this.searched[i]=false;
		}
	}
	private void findRexpression(String exp,int t,int l)
	{
		if(l==0&&t==r){this.expression+=exp+"\n";this.q++;}
		if(q>MAX_NUMBER)return;
		for(int i=0;i<n;i++)	
		{
			if(!searched[i])
			{
				this.searched[i]=true;
				String e=l==n-1?exp:"("+exp+")";
				this.findRexpression(e+"+"+p[i],t+p[i],l-1);
				this.findRexpression(e+"-"+p[i],t-p[i],l-1);
				this.findRexpression(e+"*"+p[i],t*p[i],l-1);
				this.findRexpression(e+"/"+p[i],t/p[i],l-1);
				this.searched[i]=false;
			}
		}
	}
	public String[] getExpressions()
	{
		this.getRexpression();
		if(expression.equals(""))return new String[]{"No Rexpression"};
		String[] expressions=new String[q];
		int n=0,b=0,d=0,l=expression.length();
		while(d<l)
		{
			char c=expression.charAt(d++);
			while(d<l&&c!='\n')c=expression.charAt(d++);
			expressions[n++]=expression.substring(b,d);
			b=d;
		}
		return expressions;
	}
}
