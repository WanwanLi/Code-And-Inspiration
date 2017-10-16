import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
public class JavaAndVirtualMachine
{
	public static void main(String[] args)
	{
		System.out.print("Java Flat Virtual Machine>");
		Scanner scanner=new Scanner(System.in);
		String input=scanner.nextLine();
		if(input.equals("exit"))System.exit(0);
		int i=0,length=input.length();
		String cmd="";
		char c=input.charAt(i++);
		while(i<length&&c==' ')c=input.charAt(i++);
		while(i<length&&c!=' '){cmd+=c;c=input.charAt(i++);}
		if(!cmd.equals("javab"))System.exit(0);
		String className="";
		while(i<length&&c==' ')c=input.charAt(i++);
		while(i<length&&c!='(')
		{
			if(c=='.')className+='\\';
			else className+=c;
			c=input.charAt(i++);
		}
		if(c!='(')className+=c;
		File file=new File(className+".javab");
		if(file.exists())VirtualMachine.generateVirtualMachineCode(className);
		file=new File(className+".classb");
		if(!file.exists()){System.out.println("Java Flat File :"+className+".classb does not exist...");System.exit(0);}
		String parameters="";
		if(i<length)c=input.charAt(i++);
		while(i<length&&c!=')'){parameters+=c;c=input.charAt(i++);}
		VirtualMachine VirtualMachine1=new VirtualMachine(className,parameters);
		VirtualMachine1.run();
		VirtualMachine1.setVisible(true);
	}
}

class VirtualMachine extends Frame
{
	public StringQueue methodQueue;
	public String className;
	public String classDirectory;
	public int objectNumber;
	public int classNumber;
	public int identifierNumber;
	public int stringNumber;
	public int doubleNumber;
	public VirtualMachine[] virtualMachineTable;
	public String[] methodTable;
	public int[] identifierTypeTable;
	public int[] integerTable;
	public String[] stringTable;
	public double[] doubleTable;
	public int beginInstructionPointer,instructionPointer;
	public String[] instructions;
	public int[] declarations;
	public Image screenImage,iconImage;
	public String instruction;
	public int index,length;
	public final int INT=1,STR=2,DOU=3,ARY=4,EXIT=-1;
	public boolean isReturn=false;
	public StringQueue parameterQueue;
	public StringStack stack;
	public Toolkit toolkit;
	public Graphics g;
	public Image image;
	public int screenWidth,screenHeight;
	public VirtualMachine(String className,String parameters)
	{
		this.getClassDirectoryAndClassName(className);
		this.toolkit=Toolkit.getDefaultToolkit();
		this.screenWidth=(int)toolkit.getScreenSize().getWidth();
		this.screenHeight=(int)toolkit.getScreenSize().getHeight();
		this.screenImage=new BufferedImage(screenWidth,screenHeight,1);
		try{this.iconImage=ImageIO.read(new File("VirtualMachineIcon.jpg"));}catch(Exception e){}
		this.setIconImage(iconImage);
		this.setTitle(className);
		this.setBounds(0,0,screenWidth,screenHeight);
		this.parameterQueue=new StringQueue();
		this.stack=new StringStack();
		this.g=screenImage.getGraphics();
		this.getVirtualMachineInstructions();
		this.enQueueParameters(parameters);
		this.executeDeclarations();
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	public VirtualMachine(String className,StringQueue parameterQueue,StringStack stack,Image screenImage)
	{
		this.getClassDirectoryAndClassName(className);
		this.parameterQueue=parameterQueue;
		this.stack=stack;
		this.screenImage=screenImage;
		this.g=screenImage.getGraphics();
		this.getVirtualMachineInstructions();
		this.executeDeclarations();
	}
	public VirtualMachine(VirtualMachine virtualMachine)
	{
		this.methodQueue=virtualMachine.methodQueue;
		this.objectNumber=virtualMachine.objectNumber;
		this.classNumber=virtualMachine.classNumber;
		this.identifierNumber=virtualMachine.identifierNumber;
		this.stringNumber=virtualMachine.stringNumber;
		this.virtualMachineTable=virtualMachine.virtualMachineTable;
		this.methodTable=virtualMachine.methodTable;
		this.identifierTypeTable=virtualMachine.identifierTypeTable;
		this.doubleTable=virtualMachine.doubleTable;
		this.stringTable=virtualMachine.stringTable;
		this.beginInstructionPointer=virtualMachine.beginInstructionPointer;
		this.declarations=virtualMachine.declarations;
		this.parameterQueue=virtualMachine.parameterQueue;
		this.stack=virtualMachine.stack;
		this.screenImage=virtualMachine.screenImage;
		this.g=screenImage.getGraphics();
		this.instructions=virtualMachine.instructions;
		this.integerTable=new int[virtualMachine.integerTable.length];
		this.executeDeclarations();
	}
	private void getClassDirectoryAndClassName(String className)
	{
		int n=0;
		int l=className.length();
		for(int i=l-1;i>=0;i--)
		{
			if(className.charAt(i)=='\\'){n=i;break;};
		}
		this.classDirectory=className.substring(0,n+1);
		this.className=className.substring(n+1,l);
	}
	private static String getClassDirectory(String className)
	{
		int n=0;
		int l=className.length();
		for(int i=l-1;i>=0;i--)
		{
			if(className.charAt(i)=='\\'){n=i;break;};
		}
		return className.substring(0,n+1);
	}
	private void enQueueParameters(String parameters)
	{
		if(parameters.equals(""))return;
		this.index=0;
		this.instruction=parameters;
		this.length=instruction.length();
		while(index<length)
		{
			String p=this.nextString();
			this.parameterQueue.enQueue(p);
		}
	}
	private void getVirtualMachineInstructions()
	{
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(classDirectory+className+".classb"));
			this.methodQueue=new StringQueue();
			int length=Integer.parseInt(BufferedReader1.readLine());
			for(int i=0;i<length;i++)this.methodQueue.enQueue(BufferedReader1.readLine());
			this.objectNumber=Integer.parseInt(BufferedReader1.readLine());
			this.classNumber=Integer.parseInt(BufferedReader1.readLine());
			this.virtualMachineTable=new VirtualMachine[classNumber+objectNumber];
			for(int i=0;i<classNumber;i++)
			{
				String className=BufferedReader1.readLine();
				this.virtualMachineTable[i]=new VirtualMachine(classDirectory+className,parameterQueue,stack,screenImage);
			}
			this.identifierNumber=Integer.parseInt(BufferedReader1.readLine());
			this.identifierTypeTable=new int[identifierNumber];
			this.integerTable=new int[identifierNumber];
			this.stringNumber=Integer.parseInt(BufferedReader1.readLine());
			this.stringTable=new String[stringNumber];
			length=Integer.parseInt(BufferedReader1.readLine());
			for(int i=0;i<length;i++)this.stringTable[i]=BufferedReader1.readLine();
			for(int i=length;i<stringNumber;i++)this.stringTable[i]="";
			this.doubleNumber=Integer.parseInt(BufferedReader1.readLine());
			this.doubleTable=new double[doubleNumber];
			length=Integer.parseInt(BufferedReader1.readLine());
			for(int i=0;i<length;i++)this.doubleTable[i]=Double.parseDouble(BufferedReader1.readLine());
			for(int i=length;i<doubleNumber;i++)this.doubleTable[i]=0.0;
			length=Integer.parseInt(BufferedReader1.readLine());
			this.declarations=new int[length];
			for(int i=0;i<length;i++)this.declarations[i]=Integer.parseInt(BufferedReader1.readLine());
			this.beginInstructionPointer=Integer.parseInt(BufferedReader1.readLine());
			length=Integer.parseInt(BufferedReader1.readLine());
			this.instructions=new String[length];
			for(int i=0;i<length;i++)this.instructions[i]=BufferedReader1.readLine();
			BufferedReader1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	private void executeDeclarations()
	{
		int l=declarations.length/2;
		for(int i=0;i<l;i++)
		{
			int pointer0=declarations[i*2+0];
			int pointer1=declarations[i*2+1];
			this.instructionPointer=pointer0;
			while(instructionPointer!=pointer1)
			{
				this.instruction=instructions[instructionPointer];
				this.length=instruction.length();
				this.execute();
			}
		}
	}
	public void run()
	{
		this.instructionPointer=beginInstructionPointer;
		while(instructionPointer!=EXIT)
		{
			this.instruction=instructions[instructionPointer];
			this.length=instruction.length();
			this.execute();
		}
//this.showIdentifierTable();
//this.showDoubleTable();
	}
	public void runMethod(String methodName)
	{
		this.isReturn=false;
		this.instructionPointer=this.methodQueue.getMethodPointer(methodName);
		while(true)
		{
			this.instruction=instructions[instructionPointer];
			this.length=instruction.length();
			this.execute();
			if(isReturn)return;
		}
	}
	public void paint(Graphics g)
	{
		g.drawImage(screenImage,0,0,null);
	}
	private void execute()
	{
		String OP=this.getOperator();
		if(OP.equals("INT"))
		{
			this.nextChar();
			int i=this.nextInt();
			this.identifierTypeTable[i]=INT;
		}
		else if(OP.equals("STR"))
		{
			this.nextChar();
			int i=this.nextInt();
			this.identifierTypeTable[i]=STR;
		}
		else if(OP.equals("DOU"))
		{
			this.nextChar();
			int i=this.nextInt();
			this.identifierTypeTable[i]=DOU;
		}
		else if(OP.equals("ARY"))
		{
			this.nextChar();
			int i=this.nextInt();
			this.identifierTypeTable[i]=ARY;
		}
		else if(OP.equals("LEN"))
		{
			int type=this.nextInt();
			this.nextChar();
			int i0=this.nextInt();
			this.nextChar();
			int i1=this.nextInt();
			this.identifierTypeTable[i0]=ARY+i1;
			if(type==WordList.Key_int)for(int i=0;i<i1;i++)this.identifierTypeTable[integerTable[i0]+i]=INT;
		}
		else if(OP.equals("MOV"))
		{
			char c=this.nextChar();
			int i0=this.nextInt();
			if(c=='@')i0=integerTable[i0];
			else if(c=='#')
			{
				c=this.nextChar();
				int i1=this.nextInt();
				this.doubleTable[i0]=this.getDouble(c,i1);
				this.instructionPointer++;
				return;
			}
			else if(c=='^')
			{
				i0=integerTable[i0];
				c=this.nextChar();
				int i1=this.nextInt();
				this.doubleTable[i0]=this.getDouble(c,i1);
				this.instructionPointer++;
				return;
			}
			else if(c=='$')
			{
				c=this.nextChar();
				int i1=this.nextInt();
				this.stringTable[i0]=this.getString(c,i1);
				this.instructionPointer++;
				return;
			}
			else if(c=='~')
			{
				i0=integerTable[i0];
				c=this.nextChar();
				int i1=this.nextInt();
				this.stringTable[i0]=this.getString(c,i1);
				this.instructionPointer++;
				return;
			}
			c=this.nextChar();
			int i1=this.nextInt();
			if(c=='&')
			{
				if(identifierTypeTable[i0]==INT&&identifierTypeTable[i1]==DOU)
				{
					this.integerTable[i0]=(int)(doubleTable[integerTable[i1]]+0.5);
				}
				else this.integerTable[i0]=integerTable[i1];
			}
			else if(c=='@')this.integerTable[i0]=integerTable[integerTable[i1]];
			else if(c=='#')
			{
				if(identifierTypeTable[i0]==INT)this.integerTable[i0]=(int)(doubleTable[i1]+0.5);
				else this.integerTable[i0]=i1;
			}
			else this.integerTable[i0]=i1;
		}
		else if(OP.equals("MOVLEN"))
		{
			this.nextChar();
			int i0=this.nextInt();
			this.nextChar();
			int i1=this.nextInt();
			this.integerTable[i0]=identifierTypeTable[i1]-ARY;
		}
		else if(OP.equals("ADD"))
		{
			char c=this.nextChar();
			if(c=='&')
			{
				int r=this.nextInt();
				c=this.nextChar();
				int i0=this.nextInt();
				if(c=='&')i0=integerTable[i0];
				else if(c=='@')i0=integerTable[integerTable[i0]];
				while(index<length)
				{
					char op=this.nextChar();
					c=this.nextChar();
					int i1=this.nextInt();
					if(c=='&')i1=integerTable[i1];
					else if(c=='@')i1=integerTable[integerTable[i1]];
					if(op=='+')i0+=i1;
					else i0-=i1;
				}
				integerTable[r]=i0;
			}
			else if(c=='$')
			{
				String s="";
				int r=this.nextInt();
				c=this.nextChar();
				int i=this.nextInt();
				s+=this.getString(c,i);
				while(index<length)
				{
					char op=this.nextChar();
					c=this.nextChar();
					i=this.nextInt();
					s+=this.getString(c,i);
				}
				stringTable[r]=s;
			}
			else if(c=='#')
			{
				int r=this.nextInt();
				c=this.nextChar();
				int i0=this.nextInt();
				double d0=this.getDouble(c,i0);
				while(index<length)
				{
					char op=this.nextChar();
					c=this.nextChar();
					int i1=this.nextInt();
					double d1=this.getDouble(c,i1);
					if(op=='+')d0+=d1;
					else d0-=d1;
				}
				doubleTable[r]=d0;
			}
		}
		else if(OP.equals("MUL"))
		{
			char c=this.nextChar();
			if(c=='&')
			{
				int r=this.nextInt();
				c=this.nextChar();
				int i0=this.nextInt();
				if(c=='&')i0=integerTable[i0];
				else if(c=='@')i0=integerTable[integerTable[i0]];
				while(index<length)
				{
					char op=this.nextChar();
					c=this.nextChar();
					int i1=this.nextInt();
					if(c=='&')i1=integerTable[i1];
					else if(c=='@')i1=integerTable[integerTable[i1]];
					if(op=='*')i0*=i1;
					else if(op=='/')i0/=i1;
					else if(op=='%')i0%=i1;
				}
				integerTable[r]=i0;
			}
			else if(c=='#')
			{
				int r=this.nextInt();
				c=this.nextChar();
				int i0=this.nextInt();
				double d0=this.getDouble(c,i0);
				while(index<length)
				{
					char op=this.nextChar();
					c=this.nextChar();
					int i1=this.nextInt();
					double d1=this.getDouble(c,i1);
					if(op=='*')d0*=d1;
					else d0/=d1;
				}
				doubleTable[r]=d0;
			}
		}
		else if(OP.equals("AND"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			int i0=this.nextInt();
			if(c=='&')i0=integerTable[i0];
			else if(c=='@')i0=integerTable[integerTable[i0]];
			while(index<length)
			{
				char op=this.nextChar();
				c=this.nextChar();
				int i1=this.nextInt();
				if(c=='&')i1=integerTable[i1];
				else if(c=='@')i1=integerTable[integerTable[i1]];
				if(op=='*')i0*=i1;
				else i0+=i1;
			}
			if(i0>1)i0=1;
			integerTable[r]=i0;
		}
		else if(OP.equals("EQ"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			double d0=0.0,d1=0.0;
			int i0=this.nextInt();
			d0=this.getDouble(c,i0);
			c=this.nextChar();
			int i1=this.nextInt();
			d1=this.getDouble(c,i1);
			integerTable[r]=(this.equals(d0,d1)?1:0);
		}
		else if(OP.equals("NE"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			double d0=0.0,d1=0.0;
			int i0=this.nextInt();
			d0=this.getDouble(c,i0);
			c=this.nextChar();
			int i1=this.nextInt();
			d1=this.getDouble(c,i1);
			integerTable[r]=(this.equals(d0,d1)?0:1);
		}
		else if(OP.equals("LE"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			double d0=0.0,d1=0.0;
			int i0=this.nextInt();
			d0=this.getDouble(c,i0);
			c=this.nextChar();
			int i1=this.nextInt();
			d1=this.getDouble(c,i1);
			integerTable[r]=((d0<=d1)||this.equals(d0,d1)?1:0);
		}
		else if(OP.equals("GE"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			double d0=0.0,d1=0.0;
			int i0=this.nextInt();
			d0=this.getDouble(c,i0);
			c=this.nextChar();
			int i1=this.nextInt();
			d1=this.getDouble(c,i1);
			integerTable[r]=((d0>=d1)||this.equals(d0,d1)?1:0);
		}
		else if(OP.equals("LT"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			double d0=0.0,d1=0.0;
			int i0=this.nextInt();
			d0=this.getDouble(c,i0);
			c=this.nextChar();
			int i1=this.nextInt();
			d1=this.getDouble(c,i1);
			integerTable[r]=((d0<d1)?1:0);
		}
		else if(OP.equals("GT"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			double d0=0.0,d1=0.0;
			int i0=this.nextInt();
			d0=this.getDouble(c,i0);
			c=this.nextChar();
			int i1=this.nextInt();
			d1=this.getDouble(c,i1);
			integerTable[r]=((d0>d1)?1:0);
		}
		else if(OP.equals("GT"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			double d0=0.0,d1=0.0;
			int i0=this.nextInt();
			d0=this.getDouble(c,i0);
			c=this.nextChar();
			int i1=this.nextInt();
			d1=this.getDouble(c,i1);
			integerTable[r]=((d0<=d1)||this.equals(d0,d1)?1:0);
		}
		else if(OP.equals("INC"))
		{
			this.nextChar();
			int i=this.nextInt();
			this.integerTable[i]++;
		}
		else if(OP.equals("DEC"))
		{
			this.nextChar();
			int i=this.nextInt();
			this.integerTable[i]--;
		}
		else if(OP.equals("ACC"))
		{
			this.nextChar();
			int i0=this.nextInt();
			if(identifierTypeTable[i0]==STR)
			{
				String s=stringTable[integerTable[i0]];
				char c=this.nextChar();
				c=this.nextChar();
				int i1=this.nextInt();
				s+=this.getString(c,i1);
				stringTable[integerTable[i0]]=s;
			}
			else if(identifierTypeTable[i0]==DOU)
			{
				char op=this.nextChar();
				char c=this.nextChar();
				int i1=this.nextInt();
				double d1=this.getDouble(c,i1);
				if(op=='+')this.doubleTable[integerTable[i0]]+=d1;
				else this.doubleTable[integerTable[i0]]-=d1;
			}
			else
			{
				char op=this.nextChar();
				char c=this.nextChar();
				int i1=this.nextInt();
				if(c=='&')i1=integerTable[i1];
				else if(c=='@')i1=integerTable[integerTable[i1]];
				if(op=='+')this.integerTable[i0]+=i1;
				else this.integerTable[i0]-=i1;
			}
		}
		else if(OP.equals("PRO"))
		{
			this.nextChar();
			int i0=this.nextInt();
			if(identifierTypeTable[i0]==DOU)
			{
				char op=this.nextChar();
				char c=this.nextChar();
				int i1=this.nextInt();
				double d1=this.getDouble(c,i1);
				if(op=='*')this.doubleTable[integerTable[i0]]*=d1;
				else this.doubleTable[integerTable[i0]]/=d1;
			}
			else
			{
				char op=this.nextChar();
				char c=this.nextChar();
				int i1=this.nextInt();
				if(c=='&')i1=integerTable[i1];
				else if(c=='@')i1=integerTable[integerTable[i1]];
				if(op=='*')this.integerTable[i0]*=i1;
				else this.integerTable[i0]/=i1;
			}
		}
		else if(OP.equals("ENQ"))
		{
			char c=this.nextChar();
			String s="";
			int i=this.nextInt();
			if(c=='&')
			{
				if(identifierTypeTable[i]==INT)s+=integerTable[i];
				else if(identifierTypeTable[i]==DOU)s+=doubleTable[integerTable[i]];
				else if(identifierTypeTable[i]==STR)s+=stringTable[integerTable[i]];
			}
			else if(c=='@')s+=integerTable[integerTable[i]];
			else if(c=='^')s+=doubleTable[integerTable[i]];
			else if(c=='~')s+=stringTable[integerTable[i]];
			else if(c=='#')s+=doubleTable[i];
			else if(c=='$')s+=stringTable[i];
			else s+=i;
			this.parameterQueue.enQueue(s);
		}
		else if(OP.equals("DEQ"))
		{
			char c=this.nextChar();
			int i=this.nextInt();
			String s=parameterQueue.deQueue();
			if(c=='&')
			{
				if(identifierTypeTable[i]==INT)this.integerTable[i]=Integer.parseInt(s);
				else if(identifierTypeTable[i]==DOU)this.doubleTable[integerTable[i]]=Double.parseDouble(s);
				else if(identifierTypeTable[i]==STR)this.stringTable[integerTable[i]]=s;
			}
			else if(c=='@')this.integerTable[integerTable[i]]=Integer.parseInt(s);
		}
		else if(OP.equals("JZ"))
		{
			this.nextChar();
			int i=this.nextInt();
			if(integerTable[i]==0)
			{
				this.instructionPointer=this.nextInt();
				return;
			}
		}
		else if(OP.equals("JMP"))
		{
			this.instructionPointer=this.nextInt();
			return;
		}
		else if(OP.equals("CALL"))
		{
			this.stack.push(instructionPointer+1);
			this.instructionPointer=this.nextInt();
			return;
		}
		else if(OP.equals("RET"))
		{
			this.instructionPointer=Integer.parseInt(this.stack.pop());
			this.isReturn=true;
			return;
		}
		else if(OP.equals("EXIT"))
		{
			this.instructionPointer=EXIT;
			return;
		}
		else if(OP.equals("NEW"))
		{
			int index=this.nextInt();
			this.virtualMachineTable[index].run();
		}	
		else if(OP.equals("METHOD"))
		{
			this.stack.push(instructionPointer+1);
			int index=this.nextInt();
			String methodName=this.nextString();
			this.virtualMachineTable[index].runMethod(methodName);
		}
		else if(OP.equals("OBJECT"))
		{
			this.stack.push(instructionPointer+1);
			int object=this.nextInt();
			int classIndex=this.nextInt();
			int index=classNumber+object;
			this.virtualMachineTable[index]=new VirtualMachine(this.virtualMachineTable[classIndex]);
			this.virtualMachineTable[index].run();
		}
		else if(OP.equals("PRINT"))
		{
			char c=this.nextChar();
			int i=this.nextInt();
			String s=this.getString(c,i);
			System.out.println(s);
		}
		else if(OP.equals("READI"))
		{
			char c=this.nextChar();
			int i=this.nextInt();
			Scanner scanner=new Scanner(System.in);
			if(c=='&')this.integerTable[i]=scanner.nextInt();
			else if(c=='@')this.integerTable[integerTable[i]]=scanner.nextInt();
		}
		else if(OP.equals("SIN"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			int i=this.nextInt();
			this.doubleTable[r]=Math.sin(this.getDouble(c,i));
		}
		else if(OP.equals("COS"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			int i=this.nextInt();
			this.doubleTable[r]=Math.cos(this.getDouble(c,i));
		}
		else if(OP.equals("COLOR"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			if(c=='&')r=integerTable[r];
			else if(c=='@')r=integerTable[integerTable[r]];
			c=this.nextChar();
			int g=this.nextInt();
			if(c=='&')g=integerTable[g];
			else if(c=='@')g=integerTable[integerTable[g]];
			c=this.nextChar();
			int b=this.nextInt();
			if(c=='&')b=integerTable[b];
			else if(c=='@')b=integerTable[integerTable[b]];
			if(r>255)r=255;
			if(g>255)g=255;
			if(b>255)b=255;
			this.g.setColor(new Color(r,g,b));
		}
		else if(OP.equals("FONT"))
		{
			char c=this.nextChar();
			int size=this.nextInt();
			if(c=='&')size=integerTable[size];
			else if(c=='@')size=integerTable[integerTable[size]];
			c=this.nextChar();
			int bold=this.nextInt();
			if(c=='&')bold=integerTable[bold];
			else if(c=='@')bold=integerTable[integerTable[bold]];
			if(bold==0)this.g.setFont(new Font(null,Font.PLAIN,size));
			else this.g.setFont(new Font(null,Font.BOLD,size));
		}
		else if(OP.equals("LINE"))
		{
			char c=this.nextChar();
			int i=this.nextInt();
			int x0=this.getInt(c,i);
			c=this.nextChar();
			i=this.nextInt();
			int y0=this.getInt(c,i);
			c=this.nextChar();
			i=this.nextInt();
			int x1=this.getInt(c,i);
			c=this.nextChar();
			i=this.nextInt();
			int y1=this.getInt(c,i);
			this.g.drawLine(x0,y0,x1,y1);
		}
		else if(OP.equals("DRECT"))
		{
			char c=this.nextChar();
			int x=this.nextInt();
			if(c=='&')x=integerTable[x];
			else if(c=='@')x=integerTable[integerTable[x]];
			c=this.nextChar();
			int y=this.nextInt();
			if(c=='&')y=integerTable[y];
			else if(c=='@')y=integerTable[integerTable[y]];
			c=this.nextChar();
			int w=this.nextInt();
			if(c=='&')w=integerTable[w];
			else if(c=='@')w=integerTable[integerTable[w]];
			c=this.nextChar();
			int h=this.nextInt();
			if(c=='&')h=integerTable[h];
			else if(c=='@')h=integerTable[integerTable[h]];
			this.g.drawRect(x,y,w,h);
		}
		else if(OP.equals("FRECT"))
		{
			char c=this.nextChar();
			int x=this.nextInt();
			if(c=='&')x=integerTable[x];
			else if(c=='@')x=integerTable[integerTable[x]];
			c=this.nextChar();
			int y=this.nextInt();
			if(c=='&')y=integerTable[y];
			else if(c=='@')y=integerTable[integerTable[y]];
			c=this.nextChar();
			int w=this.nextInt();
			if(c=='&')w=integerTable[w];
			else if(c=='@')w=integerTable[integerTable[w]];
			c=this.nextChar();
			int h=this.nextInt();
			if(c=='&')h=integerTable[h];
			else if(c=='@')h=integerTable[integerTable[h]];
			this.g.fillRect(x,y,w,h);
		}
		else if(OP.equals("STRING"))
		{
			String s="";
			char c=this.nextChar();
			int i=this.nextInt();
			if(c=='&')
			{
				if(identifierTypeTable[i]==STR)s=stringTable[integerTable[i]];
				else s+=integerTable[i];
			}
			else if(c=='$')s=stringTable[i];
			else s+=i;
			c=this.nextChar();
			int x=this.nextInt();
			if(c=='&')x=integerTable[x];
			else if(c=='@')x=integerTable[integerTable[x]];
			c=this.nextChar();
			int y=this.nextInt();
			if(c=='&')y=integerTable[y];
			else if(c=='@')y=integerTable[integerTable[y]];
			this.g.drawString(s,x,y);
		}
		else if(OP.equals("IMAGE"))
		{
			String s="";
			char c=this.nextChar();
			int i=this.nextInt();
			if(c=='&')
			{
				if(identifierTypeTable[i]==STR)s=stringTable[integerTable[i]];
				else s+=integerTable[i];
			}
			else if(c=='$')s=stringTable[i];
			else s+=i;
			c=this.nextChar();
			int x=this.nextInt();
			if(c=='&')x=integerTable[x];
			else if(c=='@')x=integerTable[integerTable[x]];
			c=this.nextChar();
			int y=this.nextInt();
			if(c=='&')y=integerTable[y];
			else if(c=='@')y=integerTable[integerTable[y]];
			try{this.g.drawImage(ImageIO.read(new File(s)),x,y,this);}catch(Exception e){}
		}
		else if(OP.equals("FPOLY"))
		{
			this.nextChar();
			int i0=this.nextInt();
			int l=this.identifierTypeTable[i0]-ARY;
			int[] coordinates=new int[l];
			for(int i=0;i<l;i++)coordinates[i]=integerTable[integerTable[i0]+i];
			Polygon polygon=new Polygon(coordinates);
			polygon.fill(g);
		}
		this.instructionPointer++;
	}
	public void showIdentifierTable()
	{
		for(int i=0;i<integerTable.length;i++)
		{
			String s="integerTable["+i+"]="+integerTable[i];
			String t="\ttype : ";
			if(identifierTypeTable[i]==INT)t+="INT";
			else if(identifierTypeTable[i]==STR)t+="STR";
			else if(identifierTypeTable[i]==DOU)t+="DOU";
			else if(identifierTypeTable[i]>=ARY)t+="ARY"+"   LEN="+(identifierTypeTable[i]-ARY);
			else t+="NULL";
			System.out.println(s+t);
		}
	}
	public void showStringTable()
	{
		for(int i=0;i<stringTable.length;i++)
		{
			String s="stringTable["+i+"]="+stringTable[i];
			System.out.println(s);
		}
	}
	public void showDoubleTable()
	{
		for(int i=0;i<doubleTable.length;i++)
		{
			String s="doubleTable["+i+"]="+doubleTable[i];
			System.out.println(s);
		}
	}
	private String getOperator()
	{
		this.index=0;
		String OP="";
		char c=instruction.charAt(index++);
		while(c!=' ')
		{
			OP+=c;
			if(index>=length)break;
			c=instruction.charAt(index++);
		}
		return OP;
	}
	private int getInt(char c,int i)
	{
		int n=0;
		if(c=='&')
		{
			if(identifierTypeTable[i]==DOU)n=(int)(doubleTable[integerTable[i]]+0.5);
			else n=integerTable[i];
		}
		else if(c=='@')
		{
			i=integerTable[i];
			if(identifierTypeTable[i]==DOU)n=(int)(doubleTable[integerTable[i]]+0.5);
			else n=integerTable[i];
		}
		else if(c=='#')n=(int)(doubleTable[i]+0.5);
		else if(c=='^')n=(int)(doubleTable[integerTable[i]]+0.5);
		else n=i;
		return n;
	}
	private double getDouble(char c,int i)
	{
		double d=0.0;
		if(c=='&')
		{
			if(identifierTypeTable[i]==DOU)d=doubleTable[integerTable[i]];
			else d=integerTable[i]+0.0;
		}
		else if(c=='@')
		{
			i=integerTable[i];
			if(identifierTypeTable[i]==DOU)d=doubleTable[integerTable[i]];
			else d=integerTable[i]+0.0;
		}
		else if(c=='#')d=doubleTable[i];
		else if(c=='^')d=doubleTable[integerTable[i]];
		else d=i+0.0;
		return d;
	}
	private boolean equals(double d0,double d1)
	{
		double d01=d1-d0;
		if(d01<0)d01*=-1;
		return (d01<1E-8);
	}
	private String getString(char c,int i)
	{
		String s="";
		if(c=='&')
		{
			if(identifierTypeTable[i]==STR)s+=stringTable[integerTable[i]];
			else if(identifierTypeTable[i]==DOU)s+=doubleTable[integerTable[i]];
			else s+=integerTable[i];
		}
		else if(c=='@')
		{
			i=integerTable[i];
			if(identifierTypeTable[i]==STR)s+=stringTable[integerTable[i]];
			else if(identifierTypeTable[i]==DOU)s+=doubleTable[integerTable[i]];
			else s+=integerTable[i];
		}
		else if(c=='$')s+=stringTable[i];
		else if(c=='#')s+=doubleTable[i];
		else if(c=='^')s+=doubleTable[integerTable[i]];
		else if(c=='~')s+=stringTable[integerTable[i]];
		else s+=i;
		return s;
	}
	private int nextInt()
	{
		String i="";
		char c=instruction.charAt(index++);
		while(c!=',')
		{
			i+=c;
			if(index>=length)break;
			c=instruction.charAt(index++);
		}
		return Integer.parseInt(i);
	}
	private char nextChar()
	{
		return instruction.charAt(index++);
	}
	private String nextString()
	{
		String i="";
		char c=instruction.charAt(index++);
		while(c!=',')
		{
			i+=c;
			if(index>=length)break;
			c=instruction.charAt(index++);
		}
		return i;
	}
	public static void generateVirtualMachineCode(String className)
	{
		String code=getCode(className);
		LexicalAnalyser LexicalAnalyser1=new LexicalAnalyser(code);
		String[] keyWordList=LexicalAnalyser1.getKeyWordList();
		int[] wordTable=LexicalAnalyser1.getWordTable();
		Identifier[] identifierTable=LexicalAnalyser1.getIdentifierTable();
		String[] stringTable=LexicalAnalyser1.getStringTable();
		double[] doubleTable=LexicalAnalyser1.getDoubleTable();
		String classDirectory=getClassDirectory(className);
		SyntaxTranslator SyntaxTranslator1=new SyntaxTranslator(keyWordList,wordTable,identifierTable,stringTable,doubleTable,classDirectory);
		SyntaxTranslator1.generateVirtualMachineInstructions(className);
//SyntaxTranslator1.showInstructions();
	}
	private static String getCode(String className)
	{
		String code="";
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(className+".javab"));
			String s=BufferedReader1.readLine();
			while(s!=null)
			{
				code+=s;
				s=BufferedReader1.readLine();
			}
			BufferedReader1.close();
		}
		catch(Exception e){e.printStackTrace();}
		return code;
	}
}




class SyntaxTranslator
{
	private StringQueue instructionsQueue;
	private StringQueue methodQueue;
	private StringQueue classQueue;
	private StringStack breakStack;
	private StringStack ip_while_forStack;
	private String[] instructions;
	private int[] declarations;
	private Identifier[] identifiers;
	private int instructionPointer,beginInstructionPointer;
	private int classNumber,methodNumber,objectNumber,identifierNumber,stringNumber,doubleNumber;
	private StringQueue declarationsQueue;
	private String[] keyWordList;
	private int[] wordTable;
	private String[] stringTable;
	private double[] doubleTable;
	private String[] classTable;
	private StringQueue[] methodQueueTable;
	private int w0,w1;
	private int index;
	private int length;
	private int methodReturnType;
	private Identifier[] identifierTable;
	public SyntaxTranslator(String[] keyWordList,int[] wordTable,Identifier[] identifierTable,String[] stringTable,double[] doubleTable,String classDirectory)
	{
		this.keyWordList=keyWordList;
		this.wordTable=wordTable;
		this.identifierTable=identifierTable;
		this.classNumber=0;
		this.methodNumber=0;
		this.objectNumber=0;
		this.identifierNumber=identifierTable.length;
		this.stringTable=stringTable;
		this.doubleTable=doubleTable;
		this.stringNumber=stringTable.length;
		this.doubleNumber=doubleTable.length;
		this.length=wordTable.length/2;
		this.instructionsQueue=new StringQueue();
		this.methodQueue=new StringQueue();
		this.declarationsQueue=new StringQueue();
		this.classQueue=new StringQueue();
		this.breakStack=new StringStack();
		this.ip_while_forStack=new StringStack();
		this.getVirtualMachineInstructions(classDirectory);
		this.instructions=this.instructionsQueue.getStrings();
		this.declarations=this.declarationsQueue.toIntArray();
	}
	private void getVirtualMachineInstructions(String classDirectory)
	{
		this.getClassDeclarationInstructions();
		this.classTable=this.classQueue.getStrings();
		this.getMethodQueueTable(classDirectory);
		while(index+2<length)
		{
			int k=index+2;
			int t0=wordTable[k*2+0];
			int t1=wordTable[k*2+1];
			if(t0==Word.MRK&&t1==Word.L_PARENTHESE)this.getMethodsInstructions();
			else
			{
				this.declarationsQueue.enQueue(instructionsQueue.length());
				this.nextWord();
				this.getDeclarationOrAssignmentExpressionInstructions();
				this.declarationsQueue.enQueue(instructionsQueue.length());
			}
		}
	}
	public void generateVirtualMachineInstructions(String className)
	{
		try
		{
			PrintWriter PrintWriter1=new PrintWriter(className+".classb");
			String[] methodStrings=methodQueue.getStrings();
			PrintWriter1.println(methodStrings.length);
			for(int i=0;i<methodStrings.length;i++)PrintWriter1.println(methodStrings[i]);
			PrintWriter1.println(objectNumber);
			PrintWriter1.println(classTable.length);
			for(int i=0;i<classTable.length;i++)PrintWriter1.println(classTable[i]);
			PrintWriter1.println(identifierNumber);
			PrintWriter1.println(stringNumber);
			PrintWriter1.println(stringTable.length);
			for(int i=0;i<stringTable.length;i++)PrintWriter1.println(stringTable[i]);
			PrintWriter1.println(doubleNumber);
			PrintWriter1.println(doubleTable.length);
			for(int i=0;i<doubleTable.length;i++)PrintWriter1.println(doubleTable[i]);
			PrintWriter1.println(declarations.length);
			for(int i=0;i<declarations.length;i++)PrintWriter1.println(declarations[i]);
			PrintWriter1.println(beginInstructionPointer);
			PrintWriter1.println(instructions.length);
			for(int i=0;i<instructions.length;i++)PrintWriter1.println(instructions[i]);
			PrintWriter1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	private void getClassDeclarationInstructions()
	{
		this.nextWord();
		if(w0==Word.KEY&&w1==WordList.Key_class)
		{
			this.nextWord();
			while(w0==Word.ID)
			{
				this.identifierTable[w1].Type=WordList.Key_class;
				this.classQueue.enQueue(identifierTable[w1].Name);
				this.nextWord();
				if(w0==Word.MRK&&w1==Word.SEMICOLON)return;
				this.nextWord();
			}
		}
		else this.index--;
	}
	private void getMethodQueueTable(String classDirectory)
	{
		this.methodQueueTable=new StringQueue[classTable.length];
		for(int i=0;i<classTable.length;i++)
		{
			String className=classDirectory+classTable[i];
			VirtualMachine.generateVirtualMachineCode(className);
			try
			{
				BufferedReader BufferedReader1=new BufferedReader(new FileReader(className+".classb"));
				int l=Integer.parseInt(BufferedReader1.readLine());
				this.methodQueueTable[i]=new StringQueue();
				for(int j=0;j<l;j++)this.methodQueueTable[i].enQueue(BufferedReader1.readLine());
			}
			catch(Exception e){e.printStackTrace();}
		}
	}
	private void getMethodsInstructions()
	{
		this.nextWord();
		if(w0==Word.KEY&&(w1==WordList.Key_public||w1==WordList.Key_int||w1==WordList.Key_double||w1==WordList.Key_String))
		{
			int label=w1;
			this.methodReturnType=label;
			boolean isPublic=(w1==WordList.Key_public)?true:false;
			this.nextWord();
			if(w0==Word.ID)
			{
				this.identifierTable[w1].Type=WordList.Key_public;
				this.identifierTable[w1].Label=label;
				this.identifierTable[w1].Pointer=instructionsQueue.length();
				if(isPublic)this.beginInstructionPointer=identifierTable[w1].Pointer;
				this.methodQueue.enQueue(identifierTable[w1].Name);
				this.methodQueue.enQueue(identifierTable[w1].Label);
				this.methodQueue.enQueue(identifierTable[w1].Pointer);
				this.methodNumber++;
				this.nextWord();
				if(w0==Word.MRK&&w1==Word.L_PARENTHESE)
				{
					this.getParametersInstructions();
					this.nextWord();
					if(w0==Word.MRK&&w1==Word.R_PARENTHESE)
					{
						this.nextWord();
						if(w0==Word.MRK&&w1==Word.L_BRACE)
						{
							this.getCodeBlockInstructions();
							if(isPublic)this.instructionsQueue.enQueue("EXIT");
							this.nextWord();
							if(w0==Word.MRK&&w1==Word.R_BRACE){}
						}
					}
				}
			}
		}
	}
	private void getParametersInstructions()
	{	
		this.nextWord();
		if(w0==Word.KEY&&(w1==WordList.Key_int||w1==WordList.Key_double||w1==WordList.Key_String))
		{
			int type=w1;
			this.methodQueue.enQueue(type);
			this.nextWord();
			if(w0==Word.ID)
			{
				if(type==WordList.Key_int)
				{
					this.identifierTable[w1].Type=WordList.Key_int;
					this.instructionsQueue.enQueue("INT &"+w1);
					this.instructionsQueue.enQueue("DEQ &"+w1);
				}
				else if(type==WordList.Key_double)
				{
					this.identifierTable[w1].Type=WordList.Key_double;
					this.instructionsQueue.enQueue("DOU &"+w1);
					int newID=(doubleNumber++);
					this.instructionsQueue.enQueue("MOV &"+w1+",#"+newID);
					this.instructionsQueue.enQueue("DEQ &"+w1);
				}
				else if(type==WordList.Key_String)
				{
					this.identifierTable[w1].Type=WordList.Key_String;
					this.instructionsQueue.enQueue("STR &"+w1);
					int newID=(stringNumber++);
					this.instructionsQueue.enQueue("MOV &"+w1+",$"+newID);
					this.instructionsQueue.enQueue("DEQ &"+w1);
				}
			}
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.COMMA)this.getParametersInstructions();
			else if(w0==Word.MRK&&w1==Word.R_PARENTHESE){this.index--;return;}
		}
		else if(w0==Word.MRK&&w1==Word.R_PARENTHESE){this.index--;return;}
	}
	private void getCodeBlockInstructions()
	{
		this.nextWord();
		if(w0==Word.MRK&&w1==Word.R_BRACE){this.index--;return;}
		else if(w0==Word.MRK&&w1==Word.L_BRACE)
		{
			this.getCodeBlockInstructions();
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.R_BRACE){}
		}
		else
		{
			this.getDeclarationOrAssignmentExpressionInstructions();
			if(w0==Word.KEY&&w1==WordList.Key_if){this.index--;this.getIfElseExpressionInstructions();}
			else if(w0==Word.KEY&&w1==WordList.Key_while)this.getWhileExpressionInstructions();
			else if(w0==Word.KEY&&w1==WordList.Key_for)this.getForExpressionInstructions();
			else if(w0==Word.KEY&&w1==WordList.Key_return)this.getReturnExpressionInstructions();
			else if(w0==Word.KEY&&w1==WordList.Key_this)this.getThisExpressionInstructions();
			else if(w0==Word.KEY&&w1==WordList.Key_new)this.getNewClassExpressionInstructions();
			else if(w0==Word.KEY&&w1==WordList.Key_break)this.getBreakExpressionInstructions();
		}
		this.getCodeBlockInstructions();
	}
	private void getDeclarationOrAssignmentExpressionInstructions()
	{
		if(w0==Word.KEY&&w1==WordList.Key_int)this.getIntDeclarationInstructions();
		else if(w0==Word.ID&&identifierTable[w1].Type==WordList.Key_int)
		{
			int t0=wordTable[index*2+0];
			int t1=wordTable[index*2+1];
			if(t0==Word.ARI&&(t1==Word.INC||t1==Word.DEC))
			{
				if(t1==Word.INC)this.instructionsQueue.enQueue("INC &"+w1);
				else this.instructionsQueue.enQueue("DEC &"+w1);
				this.index++;
				return;
			}
			else if(t0==Word.ARI&&(t1==Word.ACC||t1==Word.DEL))
			{
				String instruction="ACC &"+w1+",";
				if(t1==Word.ACC)instruction+="+";
				else instruction+="-";
				this.nextWord();
				instruction+=this.getAddExpressionInstructions();
				this.instructionsQueue.enQueue(instruction);
				return;
			}
			else if(t0==Word.ARI&&(t1==Word.PRO||t1==Word.QUO))
			{
				String instruction="PRO &"+w1+",";
				if(t1==Word.PRO)instruction+="*";
				else instruction+="/";
				this.nextWord();
				instruction+=this.getAddExpressionInstructions();
				this.instructionsQueue.enQueue(instruction);
				return;
			}
			int k0=wordTable[(index+1)*2+0];
			int k1=wordTable[(index+1)*2+1];
			this.index--;
			this.getArithmeticAssignmentInstructions();
		}
		else if(w0==Word.ID&&identifierTable[w1].Type==WordList.Key_array)
		{
			int label=identifierTable[w1].Label;
			this.index--;
			if(label==WordList.Key_int)this.getArithmeticAssignmentInstructions();
			else if(label==WordList.Key_double)this.getDoubleAssignmentInstructions();
			else if(label==WordList.Key_String)this.getStringAssignmentInstructions();
		}
		else if(w0==Word.KEY&&w1==WordList.Key_String)this.getStringDeclarationInstructions();
		else if(w0==Word.KEY&&w1==WordList.Key_double)this.getDoubleDeclarationInstructions();
		else if(w0==Word.ID&&identifierTable[w1].Type==WordList.Key_String)
		{
			int t0=wordTable[index*2+0];
			int t1=wordTable[index*2+1];
			if(t0==Word.ARI&&t1==Word.ACC)
			{
				String instruction="ACC ";
				instruction+="&"+w1+",";
				this.nextWord();
				String id=this.getStringAddExpressionInstructions();
				instruction+=id;
				this.instructionsQueue.enQueue(instruction);
				return;
			}
			int k0=wordTable[(index+1)*2+0];
			int k1=wordTable[(index+1)*2+1];
			this.index--;
			this.getStringAssignmentInstructions();
		}
		else if(w0==Word.ID&&identifierTable[w1].Type==WordList.Key_double)
		{
			int t0=wordTable[index*2+0];
			int t1=wordTable[index*2+1];
			if(t0==Word.ARI&&(t1==Word.ACC||t1==Word.DEL))
			{
				String instruction="ACC &"+w1+",";
				if(t1==Word.ACC)instruction+="+";
				else instruction+="-";
				this.nextWord();
				instruction+=this.getDoubleAddExpressionInstructions();
				this.instructionsQueue.enQueue(instruction);
				return;
			}
			else if(t0==Word.ARI&&(t1==Word.PRO||t1==Word.QUO))
			{
				String instruction="PRO &"+w1+",";
				if(t1==Word.PRO)instruction+="*";
				else instruction+="/";
				this.nextWord();
				instruction+=this.getDoubleAddExpressionInstructions();
				this.instructionsQueue.enQueue(instruction);
				return;
			}
			int k0=wordTable[(index+1)*2+0];
			int k1=wordTable[(index+1)*2+1];
			this.index--;
			this.getDoubleAssignmentInstructions();
		}
		else if(w0==Word.ID&&identifierTable[w1].Type==WordList.Key_class)
		{
			this.getNewObjectInstructions();
		}
	}
	private void getNewObjectInstructions()
	{
		this.nextWord();
		if(w0==Word.ID)
		{
			identifierTable[w1].Type=WordList.Key_Object;
			int object=this.objectNumber++;
			identifierTable[w1].Pointer=object;
			this.nextWord();
			if(w0==Word.ARI&&w1==Word.MOV)
			{
				this.nextWord();
				if(w0==Word.KEY&&w1==WordList.Key_new)
				{
					this.nextWord();
					if(w0==Word.ID&&identifierTable[w1].Type==WordList.Key_class)
					{
						String className=identifierTable[w1].Name;
						int classIndex=this.getIndex(className,classTable);
						identifierTable[w1].Label=classIndex;
						String newClass="NEW "+classIndex;
						this.nextWord();
						if(w0==Word.MRK&&w1==Word.L_PARENTHESE)
						{
							this.nextWord();
							if(w0==Word.MRK&&w1==Word.R_PARENTHESE)
							{
								this.instructionsQueue.enQueue("OBJECT "+object+","+classIndex);
								this.index++;
								return;
							}
							this.index--;
							int[] types=methodQueueTable[classIndex].getParameterTypes(className);
							for(int i=0;i<types.length;i++)
							{
								String id="";
								if(types[i]==WordList.Key_int)id=this.getAddExpressionInstructions();
								else if(types[i]==WordList.Key_double)id=this.getDoubleAddExpressionInstructions();
								else if(types[i]==WordList.Key_String)id=this.getStringAddExpressionInstructions();
								this.instructionsQueue.enQueue("ENQ "+id);
								this.index++;
							}
							this.instructionsQueue.enQueue("OBJECT "+object+","+classIndex);
						}
					}
				}
			}
		}
	}
	private int getIndex(String string,String[] strings)
	{
		for(int i=0;i<strings.length;i++)if(string.equals(strings[i]))return i;
		return -1;
	}
	private void getIfElseExpressionInstructions()
	{
		this.nextWord();
		if(w0==Word.KEY&&w1==WordList.Key_if)
		{
			this.nextWord();
			String id=this.getAndExpressionInstructions();
			int ip_if=instructionsQueue.length();
			this.instructionsQueue.enQueue("NOP");
			this.index++;
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.L_BRACE)
			{
				this.getCodeBlockInstructions();
				this.nextWord();
				if(w0==Word.MRK&&w1==Word.R_BRACE)
				{
					this.nextWord();
					int ip_else=instructionsQueue.length();
					this.instructionsQueue.enQueue("NOP");
					if(w0==Word.KEY&&w1==WordList.Key_else)
					{
						this.nextWord();
						if(w0==Word.MRK&&w1==Word.L_BRACE)
						{
							this.getCodeBlockInstructions();
							this.nextWord();
							if(w0==Word.MRK&&w1==Word.R_BRACE)
							{
								int ip_end=instructionsQueue.length();
								this.instructionsQueue.set(ip_if,"JZ "+id+","+(ip_else+1));
								this.instructionsQueue.set(ip_else,"JMP "+ip_end);
							}
						}
						else if(w0==Word.KEY&&w1==WordList.Key_if)
						{
							this.index--;
							int ip_begin=instructionsQueue.length();
							this.instructionsQueue.set(ip_if,"JZ "+id+","+ip_begin);
							this.getIfElseExpressionInstructions();
							int ip_end=instructionsQueue.length();
							this.instructionsQueue.set(ip_else,"JMP "+ip_end);
						}
					}
					else
					{
						this.instructionsQueue.set(ip_if,"JZ "+id+","+(ip_else+1));
						this.index--;
					}
				}
			}
		}
	}
	private void getBreakExpressionInstructions()
	{
		this.breakStack.push(instructionsQueue.length());
		this.breakStack.push(ip_while_forStack.top());
		this.instructionsQueue.enQueue("BREAK");
		this.nextWord();
	}
	private void getBreakExpressionInstructions(int ip_end)
	{
		int ip_while_for=Integer.parseInt(ip_while_forStack.pop());
		int top=breakStack.top();
		while(top==ip_while_for)
		{
			this.breakStack.pop();
			int ip_break=Integer.parseInt(breakStack.pop());
			this.instructionsQueue.set(ip_break,"JMP "+ip_end);
			top=breakStack.top();
		}
	}
	private void getWhileExpressionInstructions()
	{
		int ip_while=instructionsQueue.length();
		this.ip_while_forStack.push(ip_while);
		this.nextWord();
		String id=this.getAndExpressionInstructions();
		int ip_begin=instructionsQueue.length();
		this.instructionsQueue.enQueue("NOP");
		this.index++;
		this.nextWord();
		if(w0==Word.MRK&&w1==Word.L_BRACE)
		{
			this.getCodeBlockInstructions();
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.R_BRACE)
			{
				int ip_end=instructionsQueue.length()+1;
				String instruction="JZ "+id+","+ip_end;
				this.instructionsQueue.set(ip_begin,instruction);
				instruction="JMP "+(ip_while+1);
				this.instructionsQueue.enQueue(instruction);
				this.getBreakExpressionInstructions(ip_end);
			}
		}
	}
	private void getForExpressionInstructions()
	{
		this.nextWord();
		if(w0==Word.MRK&&w1==Word.L_PARENTHESE)
		{
			this.nextWord();
			this.getDeclarationOrAssignmentExpressionInstructions();
			int ip_for=instructionsQueue.length()+1;
			this.ip_while_forStack.push(ip_for);
			String id=this.getAndExpressionInstructions();
			int ip_begin=instructionsQueue.length();
			this.instructionsQueue.enQueue("NOP");
			int l0=instructionsQueue.length();
			this.index++;
			this.nextWord();
			this.getDeclarationOrAssignmentExpressionInstructions();
			int l1=instructionsQueue.length()-1;
			StringQueue q=this.instructionsQueue.deStringQueue(l0,l1);
			this.index++;
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.L_BRACE)
			{
				this.getCodeBlockInstructions();
				this.nextWord();
				if(w0==Word.MRK&&w1==Word.R_BRACE)
				{
					this.instructionsQueue.enStringQueue(q);
					this.instructionsQueue.enQueue("JMP "+ip_for);
					int ip_end=instructionsQueue.length();
					this.instructionsQueue.set(ip_begin,"JZ "+id+","+ip_end);
					this.getBreakExpressionInstructions(ip_end);
				}
			}
		}
	}
	private void getReturnExpressionInstructions()
	{
		String id="";
		if(methodReturnType==WordList.Key_int)id=this.getAddExpressionInstructions();
		else if(methodReturnType==WordList.Key_double)id=this.getDoubleAddExpressionInstructions();
		else if(methodReturnType==WordList.Key_String)id=this.getStringAddExpressionInstructions();
		this.instructionsQueue.enQueue("ENQ "+id);
		this.instructionsQueue.enQueue("RET");
		this.index++;
	}
	private void getNewClassExpressionInstructions()
	{
		this.nextWord();
		if(w0==Word.ID&&identifierTable[w1].Type==WordList.Key_class)
		{
			String className=identifierTable[w1].Name;
			int classIndex=this.getIndex(className,classTable);
			String newClass="NEW "+classIndex;
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.L_PARENTHESE)
			{
				this.nextWord();
				if(w0==Word.MRK&&w1==Word.R_PARENTHESE)
				{
					this.instructionsQueue.enQueue("NEW "+classIndex);
					this.index++;
					return;
				}
				this.index--;
				int[] types=methodQueueTable[classIndex].getParameterTypes(className);
				for(int i=0;i<types.length;i++)
				{
					String id="";
					if(types[i]==WordList.Key_int)id=this.getAddExpressionInstructions();
					else if(types[i]==WordList.Key_double)id=this.getDoubleAddExpressionInstructions();
					else if(types[i]==WordList.Key_String)id=this.getStringAddExpressionInstructions();
					this.instructionsQueue.enQueue("ENQ "+id);
					this.index++;
				}
				this.instructionsQueue.enQueue("NEW "+classIndex);
			}
		}
	}
	private void getThisExpressionInstructions()
	{
		this.nextWord();
		if(w0==Word.MRK&&w1==Word.DOT)
		{
			this.nextWord();
			if(w0==Word.ID)
			{
				String thisMethod=identifierTable[w1].Name;
				if(thisMethod.equals("println"))
				{
					this.nextWord();
					String id=this.getStringAddExpressionInstructions();
					this.instructionsQueue.enQueue("PRINT "+id);
					this.index++;
				}
				else if(thisMethod.equals("setColor"))
				{
					this.index++;
					String r=this.getAddExpressionInstructions();
					this.index++;
					String g=this.getAddExpressionInstructions();
					this.index++;
					String b=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("COLOR "+r+","+g+","+b);
					this.index+=2;
				}
				else if(thisMethod.equals("setFont"))
				{
					this.index++;
					String size=this.getAddExpressionInstructions();
					this.index++;
					String bold=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("FONT "+size+","+bold);
					this.index+=2;
				}
				else if(thisMethod.equals("drawImage"))
				{
					this.index++;
					String name=this.getStringAddExpressionInstructions();
					this.index++;
					String x=this.getAddExpressionInstructions();
					this.index++;
					String y=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("IMAGE "+name+","+x+","+y);
					this.index+=2;
				}
				else if(thisMethod.equals("drawString"))
				{
					this.index++;
					String s=this.getStringAddExpressionInstructions();
					this.index++;
					String x=this.getAddExpressionInstructions();
					this.index++;
					String y=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("STRING "+s+","+x+","+y);
					this.index+=2;
				}
				else if(thisMethod.equals("drawLine"))
				{
					this.index++;
					String x0=this.getAddExpressionInstructions();
					this.index++;
					String y0=this.getAddExpressionInstructions();
					this.index++;
					String x1=this.getAddExpressionInstructions();
					this.index++;
					String y1=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("LINE "+x0+","+y0+","+x1+","+y1);
					this.index+=2;
				}
				else if(thisMethod.equals("drawRect"))
				{
					this.index++;
					String x=this.getAddExpressionInstructions();
					this.index++;
					String y=this.getAddExpressionInstructions();
					this.index++;
					String width=this.getAddExpressionInstructions();
					this.index++;
					String height=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("DRECT "+x+","+y+","+width+","+height);
					this.index+=2;
				}
				else if(thisMethod.equals("fillRect"))
				{
					this.index++;
					String x=this.getAddExpressionInstructions();
					this.index++;
					String y=this.getAddExpressionInstructions();
					this.index++;
					String width=this.getAddExpressionInstructions();
					this.index++;
					String height=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("FRECT "+x+","+y+","+width+","+height);
					this.index+=2;
				}
				else if(thisMethod.equals("fillPolygon"))
				{
					this.index++;
					this.nextWord();
					if(w0==Word.ID)this.instructionsQueue.enQueue("FPOLY &"+w1);
					this.index+=2;
				}
			}
		}
	}
	private String getThisExpressionInstructions(int a)
	{
		if(wordTable[index*2+0]==Word.ID)
		{
			int id0=wordTable[index*2+1];
			if(identifierTable[id0].Name.equals("nextInt"))
			{
				String id="&"+(identifierNumber++);
				this.instructionsQueue.enQueue("INT "+id);
				this.instructionsQueue.enQueue("READI "+id);
				this.index+=3;
				return id;
			}
			else if(identifierTable[id0].Name.equals("length"))
			{
				String id="&"+(identifierNumber++);
				this.instructionsQueue.enQueue("INT "+id);
				this.index+=2;
				this.nextWord();
				String id1="&"+w1;
				this.instructionsQueue.enQueue("MOVLEN "+id+","+id1);
				this.index++;
				return id;
			}
			else if(identifierTable[id0].Name.equals("sin"))
			{
				String id="&"+(identifierNumber++);
				this.instructionsQueue.enQueue("DOU "+id);
				String id2="#"+(doubleNumber++);
				this.instructionsQueue.enQueue("MOV "+id+","+id2);
				this.index++;
				this.nextWord();
				String id1=this.getDoubleAddExpressionInstructions();
				this.instructionsQueue.enQueue("SIN "+id2+","+id1);
				this.index++;
				return id;
			}
			else if(identifierTable[id0].Name.equals("cos"))
			{
				String id="&"+(identifierNumber++);
				this.instructionsQueue.enQueue("DOU "+id);
				String id2="#"+(doubleNumber++);
				this.instructionsQueue.enQueue("MOV "+id+","+id2);
				this.index++;
				this.nextWord();
				String id1=this.getDoubleAddExpressionInstructions();
				this.instructionsQueue.enQueue("COS "+id2+","+id1);
				this.index++;
				return id;
			}
		}
		return "&-1";
	}
	private void getIntDeclarationInstructions()
	{
		int w0=this.wordTable[(index+1)*2+0];
		int w1=this.wordTable[(index+1)*2+1];
		if(w0==Word.MRK&&w1==Word.COMMA)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_int;
			this.instructionsQueue.enQueue("INT &"+w1);
			this.index+=2;
			w0=this.wordTable[(index+1)*2+0];
			w1=this.wordTable[(index+1)*2+1];
			if(w0==Word.MRK&&w1==Word.COMMA)this.getIntDeclarationInstructions();
			else if(w0==Word.ARI&&w1==Word.MOV)this.getIntDeclarationInstructions();
			else
			{
				w1=this.wordTable[index*2+1];
				this.identifierTable[w1].Type=WordList.Key_int;
				this.instructionsQueue.enQueue("INT &"+w1);
				this.index+=2;
			}
		}
		else if(w0==Word.ARI&&w1==Word.MOV)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_int;
			this.instructionsQueue.enQueue("INT &"+w1);
			w0=this.wordTable[(index+2)*2+0];
			w1=this.wordTable[(index+2)*2+1];
			this.getArithmeticAssignmentInstructions();
			w0=this.wordTable[index*2+0];
			w1=this.wordTable[index++*2+1];
			if(w0==Word.MRK&&w1==Word.COMMA)this.getIntDeclarationInstructions();
		}
		else if(w0==Word.MRK&&w1==Word.SEMICOLON)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_int;
			this.instructionsQueue.enQueue("INT &"+w1);
			this.index+=2;
		}
		else if(w0==Word.MRK&&w1==Word.R_SQRBRACKET)this.getArrayDeclarationInstructions();
	}
	private void getArrayDeclarationInstructions()
	{
		this.nextWord();
		if(w0==Word.MRK&&w1==Word.L_SQRBRACKET)
		{
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.R_SQRBRACKET)
			{
				this.nextWord();
				int id=w1;
				this.identifierTable[id].Type=WordList.Key_array;
				this.instructionsQueue.enQueue("ARY &"+id);
				this.nextWord();
				if(w0==Word.ARI&&w1==Word.MOV)
				{
					this.nextWord();
					if(w0==Word.KEY&&w1==WordList.Key_new)
					{
						this.nextWord();
						int startPointer=0;
						if(w1==WordList.Key_int)startPointer=identifierNumber;
						else if(w1==WordList.Key_double)startPointer=doubleNumber;
						else startPointer=stringNumber;
						this.identifierTable[id].Pointer=startPointer;
						this.identifierTable[id].Label=w1;
						this.instructionsQueue.enQueue("MOV &"+id+", "+startPointer);
						this.getArrayAssignmentInstructions(id);
					}
				}
			}
		}
	}
	private void getArrayAssignmentInstructions(int id)
	{
		int startPointer=this.identifierTable[id].Pointer;
		int label=this.identifierTable[id].Label;
		this.nextWord();
		if(w0==Word.MRK&&w1==Word.L_SQRBRACKET)
		{
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.R_SQRBRACKET)
			{
				this.nextWord();
				if(w0==Word.MRK&&w1==Word.L_BRACE)
				{
					int arrayIndex=index;
					int arrayLength=0;
					this.nextWord();
					while(!(w0==Word.MRK&&w1==Word.R_BRACE))
					{
						this.nextWord();
						if(w0==Word.MRK&&w1==Word.COMMA)arrayLength++;
					}
					this.instructionsQueue.enQueue("LEN "+label+",&"+id+", "+(++arrayLength));
					this.index=arrayIndex;
					String id1="",instruction="";
					if(label==WordList.Key_int)
					{
						this.identifierNumber+=arrayLength;
						for(int i=0;i<arrayLength;i++)
						{
							id1=this.getAddExpressionInstructions();
							instruction="MOV &"+(startPointer+i)+","+id1;
							this.instructionsQueue.enQueue(instruction);
							this.index++;
						}
					}
					else if(label==WordList.Key_double)
					{
						this.doubleNumber+=arrayLength;
						for(int i=0;i<arrayLength;i++)
						{
							id1=this.getDoubleAddExpressionInstructions();
							instruction="MOV #"+(startPointer+i)+","+id1;
							this.instructionsQueue.enQueue(instruction);
							this.index++;
						}
					}
					else if(label==WordList.Key_String)
					{
						this.stringNumber+=arrayLength;
						for(int i=0;i<arrayLength;i++)
						{
							id1=this.getStringAddExpressionInstructions();
							instruction="MOV $"+(startPointer+i)+","+id1;
							this.instructionsQueue.enQueue(instruction);
							this.index++;
						}
					}
					this.index++;
				}
			}
			else
			{
				if(w0==Word.INT)
				{
					if(label==WordList.Key_int)this.identifierNumber+=w1;
					else if(label==WordList.Key_double)this.doubleNumber+=w1;
					else if(label==WordList.Key_String)this.stringNumber+=w1;
					String instruction="LEN "+label+",&"+id+", "+w1;
					this.instructionsQueue.enQueue(instruction);
					this.index+=2;
				}
			}
		}
	}
	private void getStringDeclarationInstructions()
	{
		int w0=this.wordTable[(index+1)*2+0];
		int w1=this.wordTable[(index+1)*2+1];
		if(w0==Word.MRK&&w1==Word.COMMA)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_String;
			this.instructionsQueue.enQueue("STR &"+w1);
			this.index+=2;
			w0=this.wordTable[(index+1)*2+0];
			w1=this.wordTable[(index+1)*2+1];
			if(w0==Word.MRK&&w1==Word.COMMA)this.getStringDeclarationInstructions();
			else if(w0==Word.ARI&&w1==Word.MOV)this.getStringDeclarationInstructions();
			else
			{
				w1=this.wordTable[index*2+1];
				this.identifierTable[w1].Type=WordList.Key_String;
				this.instructionsQueue.enQueue("STR &"+w1);
				this.index+=2;
			}
		}
		else if(w0==Word.ARI&&w1==Word.MOV)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_String;
			this.instructionsQueue.enQueue("STR &"+w1);
			w0=this.wordTable[(index+2)*2+0];
			w1=this.wordTable[(index+2)*2+1];
			this.getStringAssignmentInstructions();
			w0=this.wordTable[index*2+0];
			w1=this.wordTable[index++*2+1];
			if(w0==Word.MRK&&w1==Word.COMMA)this.getStringDeclarationInstructions();
		}
		else if(w0==Word.MRK&&w1==Word.SEMICOLON)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_String;
			this.instructionsQueue.enQueue("STR &"+w1);
			this.index+=2;
		}
		else if(w0==Word.MRK&&w1==Word.R_SQRBRACKET)this.getArrayDeclarationInstructions();
	}
	private void getDoubleDeclarationInstructions()
	{
		int w0=this.wordTable[(index+1)*2+0];
		int w1=this.wordTable[(index+1)*2+1];
		if(w0==Word.MRK&&w1==Word.COMMA)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_double;
			this.instructionsQueue.enQueue("DOU &"+w1);
			this.index+=2;
			w0=this.wordTable[(index+1)*2+0];
			w1=this.wordTable[(index+1)*2+1];
			if(w0==Word.MRK&&w1==Word.COMMA)this.getDoubleDeclarationInstructions();
			else if(w0==Word.ARI&&w1==Word.MOV)this.getDoubleDeclarationInstructions();
			else
			{
				w1=this.wordTable[index*2+1];
				this.identifierTable[w1].Type=WordList.Key_double;
				this.instructionsQueue.enQueue("DOU &"+w1);
				this.index+=2;
			}
		}
		else if(w0==Word.ARI&&w1==Word.MOV)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_double;
			this.instructionsQueue.enQueue("DOU &"+w1);
			w0=this.wordTable[(index+2)*2+0];
			w1=this.wordTable[(index+2)*2+1];
			this.getDoubleAssignmentInstructions();
			w0=this.wordTable[index*2+0];
			w1=this.wordTable[index++*2+1];
			if(w0==Word.MRK&&w1==Word.COMMA)this.getDoubleDeclarationInstructions();
		}
		else if(w0==Word.MRK&&w1==Word.SEMICOLON)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_double;
			this.instructionsQueue.enQueue("DOU &"+w1);
			this.index+=2;
		}
		else if(w0==Word.MRK&&w1==Word.R_SQRBRACKET)this.getArrayDeclarationInstructions();
	}
	private void getCallMethodInstructions()
	{
		this.w1=this.wordTable[index*2+1];
		int ip=this.identifierTable[w1].Pointer;
		String name=identifierTable[w1].Name;
		this.index+=2;
		this.w0=this.wordTable[index*2+0];
		this.w1=this.wordTable[index*2+1];
		if(w0==Word.MRK&&w1==Word.R_PARENTHESE)
		{
			this.instructionsQueue.enQueue("CALL "+ip);
			this.index++;
			return;
		}
		int[] types=methodQueue.getParameterTypes(name);
		for(int i=0;i<types.length;i++)
		{
			String id="";
			if(types[i]==WordList.Key_int)id=this.getAddExpressionInstructions();
			else if(types[i]==WordList.Key_double)id=this.getDoubleAddExpressionInstructions();
			else if(types[i]==WordList.Key_String)id=this.getStringAddExpressionInstructions();
			this.instructionsQueue.enQueue("ENQ "+id);
			this.index++;
		}
		this.instructionsQueue.enQueue("CALL "+ip);
	}
	private String getCallMethodInstructions(int classIndex,int objectIndex)
	{
		this.index+=2;
		this.w1=this.wordTable[index*2+1];
		String method=this.identifierTable[w1].Name;
		String methodInstruction;
		if(objectIndex==-1)methodInstruction="METHOD "+classIndex+","+method;
		else methodInstruction="METHOD "+objectIndex+","+method;
		this.index+=2;
		this.w0=this.wordTable[index*2+0];
		this.w1=this.wordTable[index*2+1];
		if(w0==Word.MRK&&w1==Word.R_PARENTHESE)
		{
			this.instructionsQueue.enQueue(methodInstruction);
			this.index++;
			return method;
		}
		int[] types=methodQueueTable[classIndex].getParameterTypes(method);
		for(int i=0;i<types.length;i++)
		{
			String id="";
			if(types[i]==WordList.Key_int)id=this.getAddExpressionInstructions();
			else if(types[i]==WordList.Key_double)id=this.getDoubleAddExpressionInstructions();
			else if(types[i]==WordList.Key_String)id=this.getStringAddExpressionInstructions();
			this.instructionsQueue.enQueue("ENQ "+id);
			this.index++;
		}
		this.instructionsQueue.enQueue(methodInstruction);
		return method;
	}
	private void getArithmeticAssignmentInstructions()
	{
		this.nextWord();
		int id0=w1;
		if(identifierTable[id0].Type==WordList.Key_array)
		{
			this.index++;
			String id2=this.getAddExpressionInstructions();
			this.index++;
			String newID="&"+(identifierNumber++);
			String instruction="INT "+newID;
			this.instructionsQueue.enQueue(instruction);
			instruction="ADD "+newID+","+id2+",+ "+identifierTable[id0].Pointer;
			this.instructionsQueue.enQueue(instruction);
			this.index++;
			String id1=this.getAddExpressionInstructions();
			newID=newID.substring(1,newID.length());
			instruction="MOV @"+newID+","+id1;
			this.instructionsQueue.enQueue(instruction);
		}
		else
		{
			this.index++;
			String id1=this.getAddExpressionInstructions();
			String instruction="MOV &"+id0+","+id1;
			this.instructionsQueue.enQueue(instruction);
		}
	}
	private void getDoubleAssignmentInstructions()
	{
		this.nextWord();
		int id0=w1;
		if(identifierTable[id0].Type==WordList.Key_array)
		{
			this.index++;
			String id2=this.getAddExpressionInstructions();
			this.index++;
			int newID=identifierNumber++;
			String instruction="INT &"+newID;
			this.instructionsQueue.enQueue(instruction);
			instruction="ADD &"+newID+","+id2+",+ "+identifierTable[id0].Pointer;
			this.instructionsQueue.enQueue(instruction);
			this.index++;
			String id1=this.getDoubleAddExpressionInstructions();
			instruction="MOV ^"+newID+","+id1;
			this.instructionsQueue.enQueue(instruction);
		}
		else
		{
			this.index++;
			String id1=this.getDoubleAddExpressionInstructions();
			String instruction="MOV &"+id0+","+id1;
			this.instructionsQueue.enQueue(instruction);
		}
	}
	private void getStringAssignmentInstructions()
	{
		this.nextWord();
		int id0=w1;
		if(identifierTable[id0].Type==WordList.Key_array)
		{
			this.index++;
			String id2=this.getAddExpressionInstructions();
			this.index++;
			int newID=identifierNumber++;
			String instruction="INT &"+newID;
			this.instructionsQueue.enQueue(instruction);
			instruction="ADD &"+newID+","+id2+",+ "+identifierTable[id0].Pointer;
			this.instructionsQueue.enQueue(instruction);
			this.index++;
			String id1=this.getStringAddExpressionInstructions();
			instruction="MOV ~"+newID+","+id1;
			this.instructionsQueue.enQueue(instruction);
		}
		else
		{
			this.index++;
			String id1=this.getStringAddExpressionInstructions();
			String instruction="MOV &"+id0+","+id1;
			this.instructionsQueue.enQueue(instruction);
		}
	}
	private String getAddExpressionInstructions()
	{
		String id=this.getMulExpressionInstructions();
		if(wordTable[index*2+1]==Word.ADD||wordTable[index*2+1]==Word.SUB)
		{
			String newID="&"+(identifierNumber++);
			String instruction="ADD "+newID+","+id;
			while(index<length&&wordTable[index*2+1]==Word.ADD||wordTable[index*2+1]==Word.SUB)
			{
				String sgn=(wordTable[index*2+1]==Word.ADD)?"+":"-";
				this.index++;
				id=this.getMulExpressionInstructions();
				instruction+=","+sgn+id;
			}
			this.instructionsQueue.enQueue("INT "+newID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getMulExpressionInstructions()
	{
		String id=this.getArithmeticIdentifierValue(WordList.Key_int);
		if(wordTable[index*2+1]==Word.MUL||wordTable[index*2+1]==Word.DIV||wordTable[index*2+1]==Word.MOD)
		{
			String newID="&"+(identifierNumber++);
			String instruction="MUL "+newID+","+id;
			while(index<length&&wordTable[index*2+1]==Word.MUL||wordTable[index*2+1]==Word.DIV||wordTable[index*2+1]==Word.MOD)
			{
				
				String sgn=" ";
				if(wordTable[index*2+1]==Word.MUL)sgn="*";
				else if(wordTable[index*2+1]==Word.DIV)sgn="/";
				else if(wordTable[index*2+1]==Word.MOD)sgn="%";
				this.index++;
				id=this.getArithmeticIdentifierValue(WordList.Key_int);
				instruction+=","+sgn+id;
			}
			this.instructionsQueue.enQueue("INT "+newID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getStringAddExpressionInstructions()
	{
		String id=this.getArithmeticIdentifierValue(WordList.Key_String);
		if(wordTable[index*2+1]==Word.ADD)
		{
			int newSID=(stringNumber++);
			String newID="&"+(identifierNumber++);
			String instruction="ADD $"+newSID+","+id;
			while(wordTable[index*2+0]==Word.ARI&&wordTable[index*2+1]==Word.ADD)
			{
				this.index++;
				id=this.getArithmeticIdentifierValue(WordList.Key_String);
				instruction+=",+"+id;
			}
			this.instructionsQueue.enQueue("STR "+newID);
			this.instructionsQueue.enQueue("MOV "+newID+",$"+newSID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getDoubleAddExpressionInstructions()
	{
		String id=this.getDoubleMulExpressionInstructions();
		if(wordTable[index*2+1]==Word.ADD||wordTable[index*2+1]==Word.SUB)
		{
			int newDID=(doubleNumber++);
			String newID="&"+(identifierNumber++);
			String instruction="ADD #"+newDID+","+id;
			while(wordTable[index*2+1]==Word.ADD||wordTable[index*2+1]==Word.SUB)
			{
				String sgn=(wordTable[index*2+1]==Word.ADD)?"+":"-";
				this.index++;
				id=this.getDoubleMulExpressionInstructions();
				instruction+=","+sgn+id;
			}
			this.instructionsQueue.enQueue("DOU "+newID);
			this.instructionsQueue.enQueue("MOV "+newID+",#"+newDID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getDoubleMulExpressionInstructions()
	{
		String id=this.getArithmeticIdentifierValue(WordList.Key_double);
		if(wordTable[index*2+1]==Word.MUL||wordTable[index*2+1]==Word.DIV)
		{
			int newDID=(doubleNumber++);
			String newID="&"+(identifierNumber++);
			String instruction="MUL #"+newDID+","+id;
			while(index<length&&wordTable[index*2+1]==Word.MUL||wordTable[index*2+1]==Word.DIV)
			{
				String sgn=(wordTable[index*2+1]==Word.MUL)?"*":"/";
				this.index++;
				id=this.getArithmeticIdentifierValue(WordList.Key_double);
				instruction+=","+sgn+id;
			}
			this.instructionsQueue.enQueue("DOU "+newID);
			this.instructionsQueue.enQueue("MOV "+newID+",#"+newDID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getArithmeticIdentifierValue(int key)
	{
		if(wordTable[index*2+0]==Word.ID)
		{
			int id0=wordTable[index*2+1];
			if(identifierTable[id0].Type==WordList.Key_array)
			{
				this.index+=2;
				String id2=this.getAddExpressionInstructions();
				int newID=(identifierNumber++);
				String instruction="INT &"+newID;
				this.instructionsQueue.enQueue(instruction);
				instruction="ADD &"+newID+","+id2+",+ "+identifierTable[id0].Pointer;
				this.instructionsQueue.enQueue(instruction);
				String label="";
				if(identifierTable[id0].Label==WordList.Key_int)label="@";
				else if(identifierTable[id0].Label==WordList.Key_double)label="^";
				else if(identifierTable[id0].Label==WordList.Key_String)label="~";
				String id=label+newID;
				this.index++;
				return id;
			}
			else if(identifierTable[id0].Type==WordList.Key_public)
			{
				int label=identifierTable[id0].Label;
				String id="&"+(identifierNumber++);
				this.getCallMethodInstructions();
				if(label==WordList.Key_int)this.instructionsQueue.enQueue("INT "+id);
				else if(label==WordList.Key_double)
				{
					String id1="#"+(doubleNumber++);
					this.instructionsQueue.enQueue("DOU "+id);
					this.instructionsQueue.enQueue("MOV "+id+","+id1);
				}
				else if(label==WordList.Key_String)
				{
					String id1="$"+(stringNumber++);
					this.instructionsQueue.enQueue("STR "+id);
					this.instructionsQueue.enQueue("MOV "+id+","+id1);
				}
				this.instructionsQueue.enQueue("DEQ "+id);
				return id;
			}
			else if(identifierTable[id0].Type==WordList.Key_class)
			{
				int classIndex=this.getIndex(identifierTable[id0].Name,classTable);
				String id="&"+(identifierNumber++);
				String method=this.getCallMethodInstructions(classIndex,-1);
				int type=methodQueueTable[classIndex].getMethodReturnType(method);
				if(type==WordList.Key_int)this.instructionsQueue.enQueue("INT "+id);
				else if(type==WordList.Key_double)this.instructionsQueue.enQueue("DOU "+id);
				else if(type==WordList.Key_String)this.instructionsQueue.enQueue("STR "+id);
				this.instructionsQueue.enQueue("DEQ "+id);
				return id;
			}
			else if(identifierTable[id0].Type==WordList.Key_Object)
			{
				int classIndex=identifierTable[id0].Label;
				int objectIndex=classTable.length+identifierTable[id0].Pointer;
				String id="&"+(identifierNumber++);
				String method=this.getCallMethodInstructions(classIndex,objectIndex);
				int type=methodQueueTable[classIndex].getMethodReturnType(method);
				if(type==WordList.Key_int)this.instructionsQueue.enQueue("INT "+id);
				else if(type==WordList.Key_double)this.instructionsQueue.enQueue("DOU "+id);
				else if(type==WordList.Key_String)this.instructionsQueue.enQueue("STR "+id);
				this.instructionsQueue.enQueue("DEQ "+id);
				return id;
			}
			else
			{
				String id="&"+id0;
				this.index++;
				return id;
			}
		}
		else if(wordTable[index*2+0]==Word.INT)
		{
			String id=" "+wordTable[index*2+1];
			this.index++;
			return id;
		}
		else if(wordTable[index*2+0]==Word.STR)
		{
			String id="$"+wordTable[index*2+1];
			this.index++;
			return id;
		}
		else if(wordTable[index*2+0]==Word.DOU)
		{
			String id="#"+wordTable[index*2+1];
			this.index++;
			return id;
		}
		else if(wordTable[index*2+0]==Word.KEY&&wordTable[index*2+1]==WordList.Key_this)
		{
			this.index+=2;
			return this.getThisExpressionInstructions(0);
		}
		else if(wordTable[index*2+0]==Word.ARI&&wordTable[index*2+1]==Word.SUB)
		{
			this.index++;
			String id0,id1;
			if(key==WordList.Key_int)
			{
				id0="&"+(identifierNumber++);
				id1=this.getAddExpressionInstructions();
			}
			else if(key==WordList.Key_double)
			{
				id0="#"+(doubleNumber++);
				id1=this.getDoubleAddExpressionInstructions();
			}
			else id0=id1="";
			this.instructionsQueue.enQueue("MUL "+id0+","+id1+",* -1");
			return id0;
		}
		else if(wordTable[index*2+1]==Word.L_PARENTHESE)
		{
			this.index++;
			String id;
			if(key==WordList.Key_int)id=this.getAddExpressionInstructions();
			else if(key==WordList.Key_double)id=this.getDoubleAddExpressionInstructions();
			else if(key==WordList.Key_String)id=this.getStringAddExpressionInstructions();
			else id="";
			if(wordTable[index*2+1]==Word.R_PARENTHESE)
			{
				this.index++;
				return id;
			}
			else return "&-1";
		}
		else return "&-1";
	}
	private String getAndExpressionInstructions()
	{
		String id=this.getLogicExpressionInstructions();
		if(wordTable[index*2+1]==Word.AND||wordTable[index*2+1]==Word.OR)
		{
			String newID="&"+(identifierNumber++);
			String instruction="AND "+newID+","+id;
			while(index<length&&wordTable[index*2+1]==Word.AND||wordTable[index*2+1]==Word.OR)
			{
				String sgn=(wordTable[index*2+1]==Word.AND)?"*":"+";
				this.index++;
				id=this.getLogicExpressionInstructions();
				instruction+=","+sgn+id;
			}
			this.instructionsQueue.enQueue("INT "+newID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getLogicExpressionInstructions()
	{
		if(wordTable[index*2+1]==Word.NOT)
		{
			this.index++;
			String newID="&"+(identifierNumber++);
			String id=this.getLogicExpressionInstructions();
			this.instructionsQueue.enQueue("INT "+newID);
			String instruction="NOT "+newID+","+id;
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else if(wordTable[index*2+1]==Word.L_PARENTHESE)
		{
			this.index++;
			String id=this.getAndExpressionInstructions();
			if(wordTable[index*2+1]==Word.R_PARENTHESE)
			{
				this.index++;
				return id;
			}
			else return "&-1";
		}
		else return this.getCmpExpressionInstructions();
	}
	private String getCmpExpressionInstructions()
	{
		String id=this.getDoubleAddExpressionInstructions();
		int OP=wordTable[index*2+1];
		if(OP==Word.EQ||OP==Word.NE||OP==Word.LE||OP==Word.LT||OP==Word.GE||OP==Word.GT)
		{
			String newID="&"+(identifierNumber++);
			this.index++;
			String CMP="";
			switch(OP)
			{
				case Word.EQ:CMP="EQ ";break;
				case Word.NE:CMP="NE ";break;
				case Word.LE:CMP="LE ";break;
				case Word.LT:CMP="LT ";break;
				case Word.GE:CMP="GE ";break;
				case Word.GT:CMP="GT ";break;
			}
			String instruction=CMP+newID+","+id;
			id=this.getDoubleAddExpressionInstructions();
			instruction+=","+id;
			this.instructionsQueue.enQueue("INT "+newID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private void nextWord()
	{
		this.w0=this.wordTable[index*2+0];
		this.w1=this.wordTable[index*2+1];
		this.index++;
	}
	public String[] getInstructions()
	{
		return this.instructions;
	}
	public void showInstructions()
	{
		for(int i=0;i<instructions.length;i++)
		{
			System.out.println(i+":"+instructions[i]);
		}
	}
}









class LexicalAnalyser
{
	private int index;
	private String code;
	private boolean isCode;
	private int length;
	private int[] wordTable;
	private char[] charTable;
	private String[] stringTable;
	private double[] doubleTable;
	private String[] keyWordList;
	private IdentifierList identifierList;
	private Identifier[] identifierTable;
	private StringQueue charQueue;
	private StringQueue stringQueue;
	private StringQueue doubleQueue;
	public LexicalAnalyser(String code)
	{
		this.index=0;
		this.code=code;
		this.isCode=true;
		this.length=code.length();
		this.charQueue=new StringQueue();
		this.stringQueue=new StringQueue();
		this.doubleQueue=new StringQueue();
		this.keyWordList=WordList.getKeyWordList();
		this.identifierList=new IdentifierList();
		this.createWordTable();
		this.createCharTable();
		this.createStringTable();
		this.createDoubleTable();
		this.createIdentifierTable();
	}
	private void createWordTable()
	{
		WordList wordList=new WordList();
		this.index=0;
		while(index<length)
		{
			Word word=this.scanNextWord();
			if(isCode)wordList.addWord(word);
		}
		this.wordTable=wordList.getWordTable();
	}
	private void createCharTable()
	{
		this.charTable=this.charQueue.toCharArray();
	}
	private void createStringTable()
	{
		this.stringTable=this.stringQueue.getStrings();
	}
	private void createDoubleTable()
	{
		this.doubleTable=this.doubleQueue.getDoubles();
	}
	private void createIdentifierTable()
	{
		this.identifierTable=this.identifierList.getIdentifierTable();
	}
	public String[] getKeyWordList()
	{
		return this.keyWordList;
	}
	public int[] getWordTable()
	{
		return this.wordTable;
	}
	public char[] getCharTable()
	{
		return this.charTable;
	}
	public String[] getStringTable()
	{
		return this.stringTable;
	}
	public double[] getDoubleTable()
	{
		return this.doubleTable;
	}
	public Identifier[] getIdentifierTable()
	{
		return this.identifierTable;
	}
	public void printWordTable()
	{
		for(int i=0;i<wordTable.length/2;i++)
		{
			int c=wordTable[i*2+0];
			int o=wordTable[i*2+1];
			switch(c)
			{
				case Word.ID:System.out.println(identifierTable[o].Name);break;
				case Word.KEY:System.out.println(keyWordList[o]);break;
				case Word.INT:System.out.println(o);break;
				case Word.CHR:System.out.println(charTable[o]);break;
				case Word.STR:System.out.println(stringTable[o]);break;
				case Word.DOU:System.out.println(doubleTable[o]);break;
				case Word.LOG:System.out.println(getLogicString(o));break;
				case Word.ARI:System.out.println(getArithmeticString(o));break;
				case Word.MRK:System.out.println(getMarkString(o));break;
			}
		}	
	}
	private String getLogicString(int operationType)
	{
		switch(operationType)
		{
			case Word.EQ:return "==";
			case Word.NE:return "!=";
			case Word.LE:return "<=";
			case Word.LT:return "<";
			case Word.GE:return ">=";
			case Word.GT:return ">";
			case Word.AND:return "&&";
			case Word.OR:return "||";
			case Word.NOT:return "!";
		}
		return "null";
	}
	private String getArithmeticString(int operationType)
	{	
		switch(operationType)
		{
			case Word.MOV:return "=";
			case Word.INC:return "++";
			case Word.ADD:return "+";
			case Word.DEC:return "--";
			case Word.SUB:return "-";
			case Word.MUL:return "*";
			case Word.DIV:return "/";
			case Word.AND:return "&";
			case Word.OR:return "|";
			case Word.ACC:return "+=";
			case Word.DEL:return "-=";
			case Word.MOD:return "%";
			case Word.PRO:return "*=";
			case Word.QUO:return "/=";
		}
		return "null";
	}
	private String getMarkString(int operationType)
	{	
		switch(operationType)
		{	
			case Word.L_PARENTHESE:return "(";
			case Word.R_PARENTHESE:return ")";
			case Word.L_SQRBRACKET:return "[";
			case Word.R_SQRBRACKET:return "]";
			case Word.L_BRACE:return "{";
			case Word.R_BRACE:return "}";
			case Word.DOT:return ".";
			case Word.COMMA:return ",";
			case Word.SEMICOLON:return ";";
		}
		return "null";
	}
	private Word scanNextWord()
	{
		String s="";
		char c=code.charAt(index++);
		while(c==' '||c=='\t'||c=='\n'||c=='\r')c=code.charAt(index++);
		if(isAlpha(c))
		{
			while(isAlpha(c)||isDigit(c)||c=='_')
			{
				s+=c;
				c=code.charAt(index++);
				if(index>=length)break;
			}
			this.index--;
			int n=this.getKeyWordIndex(s);
			if(n==-1){n=this.getIdentifierIndex(s);return new Word(Word.ID,n);}
			else return new Word(Word.KEY,n);
		}
		else if(isDigit(c))
		{
			boolean isDouble=false,isPercent=false;
			while(isDigit(c)||c=='.'||(isDouble&&c=='%'))
			{
				if(c=='%'){isPercent=true;break;}
				s+=c;
				if(c=='.')isDouble=true;
				c=code.charAt(index++);
				if(index>=length)break;
			}
			this.index--;
			if(isDouble)
			{
				if(isPercent)this.doubleQueue.enQueue(""+Double.parseDouble(s)/100.0);
				else this.doubleQueue.enQueue(s);
				return new Word(Word.DOU,doubleQueue.length()-1);
			}
			else return new Word(Word.INT,Integer.parseInt(s));
		}
		else
		{
			switch(c)
			{
				case '=':
				c=code.charAt(index++);
				if(c=='=')return new Word(Word.LOG,Word.EQ);
				else{this.index--;return new Word(Word.ARI,Word.MOV);}
				case '!':
				c=code.charAt(index++);
				if(c=='=')return new Word(Word.LOG,Word.NE);
				else{this.index--;return new Word(Word.LOG,Word.NOT);}
				case '<':
				c=code.charAt(index++);
				if(c=='=')return new Word(Word.LOG,Word.LE);
				else{this.index--;return new Word(Word.LOG,Word.LT);}
				case '>':
				c=code.charAt(index++);
				if(c=='=')return new Word(Word.LOG,Word.GE);
				else{this.index--;return new Word(Word.LOG,Word.GT);}
				case '+':
				c=code.charAt(index++);
				if(c=='+')return new Word(Word.ARI,Word.INC);
				else if(c=='=')return new Word(Word.ARI,Word.ACC);
				else{this.index--;return new Word(Word.ARI,Word.ADD);}
				case '-':
				c=code.charAt(index++);
				if(c=='-')return new Word(Word.ARI,Word.DEC);
				else if(c=='=')return new Word(Word.ARI,Word.DEL);
				else{this.index--;return new Word(Word.ARI,Word.SUB);}
				case '*':
				c=code.charAt(index++);
				if(c=='=')return new Word(Word.ARI,Word.PRO);
				else{this.index--;return new Word(Word.ARI,Word.MUL);}
				case '/':
				if(index>=length)break;
				c=code.charAt(index++);
				while(c=='*')
				{
					if(index>=length)break;
					c=code.charAt(index++);
					if(c=='/')break;
					while(c!='*'&&index<length)c=code.charAt(index++);
				}
				if(c=='=')return new Word(Word.ARI,Word.QUO);
				else{this.index--;return new Word(Word.ARI,Word.DIV);}
				case '&':
				c=code.charAt(index++);
				if(c=='&')return new Word(Word.LOG,Word.AND);
				else{this.index--;return new Word(Word.ARI,Word.AND);}
				case '|':
				c=code.charAt(index++);
				if(c=='|')return new Word(Word.LOG,Word.OR);
				else{this.index--;return new Word(Word.ARI,Word.OR);}
				case '%':return new Word(Word.ARI,Word.MOD);
				case '(':return new Word(Word.MRK,Word.L_PARENTHESE);
				case ')':return new Word(Word.MRK,Word.R_PARENTHESE);
				case '[':return new Word(Word.MRK,Word.L_SQRBRACKET);
				case ']':return new Word(Word.MRK,Word.R_SQRBRACKET);
				case '{':return new Word(Word.MRK,Word.L_BRACE);
				case '}':return new Word(Word.MRK,Word.R_BRACE);
				case '\'':
				c=code.charAt(index++);
				this.charQueue.enQueue(c);
				c=code.charAt(index++);
				return new Word(Word.CHR,charQueue.length()-1);
				case '\"':
				c=code.charAt(index++);
				while(c!='\"')
				{
					if(c!=';')s+=c;
					c=code.charAt(index++);
					if(index>=length)break;
				}
				this.stringQueue.enQueue(s);
				return new Word(Word.STR,stringQueue.length()-1);
				case '.':return new Word(Word.MRK,Word.DOT);
				case ',':return new Word(Word.MRK,Word.COMMA);
				case ';':return new Word(Word.MRK,Word.SEMICOLON);
			}
		}
		return new Word(-1,-1);
	}
	private int getKeyWordIndex(String word)
	{
		int l=this.keyWordList.length;
		for(int i=0;i<l;i++)if(word.equals(keyWordList[i]))return i;
		return -1;
	}
	private int getIdentifierIndex(String word)
	{
		return this.identifierList.getIdentifierIndex(word);
	}
	private boolean isAlpha(char c)
	{
		return ('A'<=c&&c<='Z')||('a'<=c&&c<='z');
	}
	private boolean isDigit(char c)
	{
		return ('0'<=c&&c<='9');
	}
}
class CodeBlock
{
}
class Identifier
{
	public String Name;
	public Identifier Next;
	public int Type;
	public int Pointer;
	public int Label;
	public CodeBlock ParentBlock;
	public Identifier(String name)
	{
		this.Name=name;
	}
	public Identifier(int type)
	{
		this.Name="null";
		this.Type=type;
	}
}
class IdentifierList
{
	private Identifier firstIdentifier;
	private Identifier lastIdentifier;
	private int length;
	public void add(Identifier identifier)
	{
		this.length++;
		if(firstIdentifier==null)
		{
			this.firstIdentifier=identifier;
			this.lastIdentifier=identifier;
		}
		else
		{
			this.lastIdentifier.Next=identifier;
			this.lastIdentifier=identifier;
		}
	}
	public void addIdentifier(String name)
	{
		this.length++;
		if(firstIdentifier==null)
		{
			this.firstIdentifier=new Identifier(name);
			this.lastIdentifier=firstIdentifier;
		}
		else
		{
			if(notExist(name))
			{
				Identifier id=new Identifier(name);
				this.lastIdentifier.Next=id;
				this.lastIdentifier=id;
			}
		}
	}
	public int length()
	{
		return length;
	}
	public Identifier[] getIdentifierTable()
	{
		Identifier[] table=new Identifier[length];
		Identifier id=firstIdentifier;
		for(int i=0;i<length;i++,id=id.Next)table[i]=id;
		return table;
	}
	public int getIdentifierIndex(String name)
	{
		int index=0;
		for(Identifier id=firstIdentifier;id!=null;id=id.Next,index++)
		{
			if(id.Name.equals(name))return index;
		}
		this.addIdentifier(name);
		return this.length-1;
	}
	private boolean notExist(String name)
	{
		for(Identifier id=firstIdentifier;id!=null;id=id.Next)
		{
			if(id.Name.equals(name))return false;
		}
		return true;
	}
}
class Word
{
	public Word Next;
	public int Classification;
	public int OperationType;
	public static final int ID=0;
	public static final int KEY=1;
	public static final int INT=2;
	public static final int LOG=3;
	public static final int ARI=4;
	public static final int MRK=5;
	public static final int CHR=6;
	public static final int STR=7;
	public static final int DOU=8;
	public static final int EQ=30;
	public static final int NE=31;
	public static final int LE=32;
	public static final int LT=33;
	public static final int GE=34;
	public static final int GT=35;
	public static final int AND=36;
	public static final int OR=37;
	public static final int NOT=38;
	public static final int MOV=40;
	public static final int ADD=41;
	public static final int SUB=42;
	public static final int MUL=43;
	public static final int DIV=44;
	public static final int INC=45;
	public static final int DEC=46;
	public static final int ACC=47;
	public static final int DEL=48;
	public static final int MOD=49;
	public static final int PRO=410;
	public static final int QUO=411;
	public static final int L_PARENTHESE=50;
	public static final int R_PARENTHESE=51;
	public static final int L_SQRBRACKET=52;
	public static final int R_SQRBRACKET=53;
	public static final int L_BRACE=54;
	public static final int R_BRACE=55;
	public static final int DOT=56;
	public static final int COMMA=57;
	public static final int SEMICOLON=58;
	public Word(int classification,int operationType)
	{
		this.Classification=classification;
		this.OperationType=operationType;
	}
}
class WordList
{
	private Word firstWord;
	private Word lastWord;
	private int length;
	public void addWord(Word word)
	{
		this.length++;
		Word newWord=new Word(word.Classification,word.OperationType);
		if(firstWord==null)
		{

			this.firstWord=newWord;
			this.lastWord=newWord;
		}
		else
		{
			this.lastWord.Next=newWord;
			this.lastWord=newWord;
		}
	}
	public int[] getWordTable()
	{
		int[] table=new int[2*length];
		Word word=firstWord;
		for(int i=0;i<length;i++,word=word.Next)
		{
			table[i*2+0]=word.Classification;
			table[i*2+1]=word.OperationType;
		}
		return table;
	}
	public int length()
	{
		return length;
	}
	public static final int Key_int=1;
	public static final int Key_char=2;
	public static final int Key_String=3;
	public static final int Key_void=4;
	public static final int Key_public=5;
	public static final int Key_new=6;
	public static final int Key_for=7;
	public static final int Key_while=8;
	public static final int Key_if=9;
	public static final int Key_else=10;
	public static final int Key_array=11;
	public static final int Key_return=12;
	public static final int Key_this=13;
	public static final int Key_class=14;
	public static final int Key_Object=15;
	public static final int Key_double=16;
	public static final int Key_break=17;
	public static String[] getKeyWordList()
	{
		return new String[]
		{
			"null",
			"int",
			"char",
			"String",
			"void",
			"public",
			"new",
			"for",
			"while",
			"if",
			"else",
			"array",
			"return",
			"this",
			"class",
			"Object",
			"double",
			"break"
		};
	}
}
class StringQueue
{
	private String stringQueue;
	private int length;
	public StringQueue()
	{
		this.stringQueue="";
	}
	public StringQueue(String queue)
	{
		this.stringQueue=queue;
		this.length=this.getLength();
		if(length==0)
		{
			this.stringQueue+=";";
			this.length=1;
		}
	}
	public void enQueue(char character)
	{
		this.stringQueue+=character;
		this.length++;
	}
	public void enQueue(String string)
	{
		this.stringQueue+=string+";";
		this.length++;
	}
	public void enQueue(int integer)
	{
		this.stringQueue+=integer+";";
		this.length++;
	}
	public void enQueue(int[] array)
	{
		int l=array.length;
		for(int i=0;i<l;i++)this.stringQueue+=array[i]+";";
		this.length+=l;
	}
	public void insert(String string,int index)
	{
		this.length++;
		if(index>=length)
		{
			this.stringQueue+=string+";";
			return;
		}
		int i=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==index)break;
			}
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(i,Length);
		this.stringQueue=s0+";"+string+s1;
	}
	public void set(int index,String string)
	{
		if(index>=length)return;
		int i=0,j=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==index)break;
			}
		}
		for(j=i+1;j<Length;j++)
		{
			char c=stringQueue.charAt(j);
			if(c==';')break;
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(j,Length);
		this.stringQueue=s0+";"+string+s1;
	}
	public String deQueue()
	{
		String string="";
		if(stringQueue.length()==0)return string;
		int n=0;
		char c=stringQueue.charAt(n++);
		while(c!=';')
		{
			string+=c;
			c=stringQueue.charAt(n++);
		}
		this.stringQueue=stringQueue.substring(n,stringQueue.length());
		this.length--;
		return string;
	}
	private int getLength()
	{
		int i=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')l++;
		}
		return l;
	}
	public int length()
	{
		return this.length;
	}
	public void show()
	{
		System.out.println(stringQueue);
	}
	public String getStringQueue()
	{
		String queue="";
		queue+=stringQueue;
		return queue;
	}
	public void enStringQueue(StringQueue queue)
	{
		this.stringQueue+=queue.getStringQueue();
		this.length+=queue.length();
	}
	public StringQueue deStringQueue(int begin,int end)
	{
		int i=0,j=0,l=0,Length=stringQueue.length();
		String queue="";
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==begin)break;
			}
		}
		for(j=i+1;j<Length;j++)
		{
			char c=stringQueue.charAt(j);
			queue+=c;
			if(c==';')
			{
				l++;
				if(l>end)break;
			}
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(j,Length);
		this.stringQueue=s0+s1;
		this.length-=(end-begin+1);
		return new StringQueue(queue);
	}
	public String[] getStrings()
	{
		int l=this.length();
		String[] strings=new String[l];
		int n=0,i=0;
		String s="";
		char c;
		while(n<l)
		{
			c=stringQueue.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(i++);
			}
			strings[n++]=s;
			s="";
		}
		return strings;
	}
	public double[] getDoubles()
	{
		int l=this.length();
		double[] doubles=new double[l];
		int n=0,i=0;
		String s="";
		char c;
		while(n<l)
		{
			c=stringQueue.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(i++);
			}
			doubles[n++]=Double.parseDouble(s);
			s="";
		}
		return doubles;
	}
	public char[] toCharArray()
	{
		char[] array=new char[length];
		for(int i=0;i<length;i++)array[i]=stringQueue.charAt(i);
		return array;
	}
	public int[] toIntArray()
	{
		String s="";
		int n=0;
		int[] array=new int[length];
		for(int i=0;i<length;i++)
		{
			char c=stringQueue.charAt(n++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(n++);
			}
			array[i]=Integer.parseInt(s);
			s="";
		}
		return array;
	}
	public int getMethodReturnType(String methodName)
	{
		int l=this.length();
		int n=0,i=0;
		String s="";
		char c;
		StringQueue q=new StringQueue();
		while(n<l)
		{
			c=stringQueue.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(i++);
			}
			if(s.equals(methodName))
			{
				s="";
				c=stringQueue.charAt(i++);
				while(c!=';'){s+=c;c=stringQueue.charAt(i++);}
				return Integer.parseInt(s);
			}
			s="";
			n++;
		}
		return -1;
	}
	public int getMethodPointer(String methodName)
	{
		int l=this.length();
		int n=0,i=0;
		String s="";
		char c;
		StringQueue q=new StringQueue();
		while(n<l)
		{
			c=stringQueue.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(i++);
			}
			if(s.equals(methodName))
			{
				s="";
				c=stringQueue.charAt(i++);
				while(c!=';')c=stringQueue.charAt(i++);
				c=stringQueue.charAt(i++);
				while(c!=';'){s+=c;c=stringQueue.charAt(i++);}
				return Integer.parseInt(s);
			}
			s="";
			n++;
		}
		return -1;
	}
	public int[] getParameterTypes(String methodName)
	{
		int l=this.length();
		int n=0,i=0;
		String s="";
		char c;
		StringQueue q=new StringQueue();
		while(n<l)
		{
			c=stringQueue.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(i++);
			}
			if(s.equals(methodName))
			{
				c=stringQueue.charAt(i++);
				while(c!=';')c=stringQueue.charAt(i++);
				c=stringQueue.charAt(i++);
				while(c!=';')c=stringQueue.charAt(i++);
				c=stringQueue.charAt(i++);
				while(true)
				{
					s="";
					do
					{
						s+=c;
						c=stringQueue.charAt(i++);
					}
					while(c!=';');
					q.enQueue(s);
					if(i>=stringQueue.length())return q.toIntArray();
					c=stringQueue.charAt(i++);
					if(c<'0'||c>'9')return q.toIntArray();
				}
			}
			s="";
			n++;
		}
		return new int[0];
	}
	public boolean isNotEmpty()
	{
		return (this.stringQueue.length()>0);
	}
}

class StringStack
{
	private String stringStack;
	private int length;
	public StringStack()
	{
		this.stringStack="";
	}
	public void push(String string)
	{
		this.stringStack=string+";"+stringStack;
		this.length++;
	}
	public void push(int integer)
	{
		this.stringStack=integer+";"+stringStack;
		this.length++;
	}
	public String pop()
	{
		int i=0,l=0,Length=stringStack.length();
		for(i=0;i<Length;i++)
		{
			char c=stringStack.charAt(i);
			if(c==';')break;
		}
		String s0=stringStack.substring(0,i);
		String s1=stringStack.substring(i+1,Length);
		this.stringStack=s1;
		this.length--;
		return s0;
	}
	public int top()
	{
		int i=0,l=0,Length=stringStack.length();
		String s="";
		for(i=0;i<Length;i++)
		{
			char c=stringStack.charAt(i);
			if(c==';')break;
			s+=c;
		}
		if(s.equals(""))return -1;
		return Integer.parseInt(s);
	}
	public void show()
	{
		System.out.println(stringStack);
	}
	public boolean isNotEmpty()
	{
		return (length>0);
	}
}
