import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.applet.Applet;
import com.sun.j3d.utils.applet.MainFrame;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
public class JavaAndVirtualMachine3D
{
	public static void main(String[] args)
	{
		System.out.print("Java Flat Virtual Machine>");
		Scanner scanner=new Scanner(System.in);
		String input=scanner.nextLine();
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
		File file=new File(className+".classb");
		if(!file.exists()){System.out.println("Java Flat File :"+className+".classb does not exist...");System.exit(0);}
		String parameters="";
		if(i<length)c=input.charAt(i++);
		while(i<length&&c!=')'){parameters+=c;c=input.charAt(i++);}
		file=new File(className+".javab");
		if(file.exists())VirtualMachine3D.generateVirtualMachine3DCode(className);
		VirtualMachine3D virtualMachine3D=new VirtualMachine3D(className,parameters);
		//virtualMachine3D.config(className,parameters);
		//virtualMachine3D.run();
		new MainFrame(virtualMachine3D,400,400);
	}
}

class VirtualMachine3D extends Applet
{
	public String className;
	private String classDirectory;
	private int classNumber;
	private int methodNumber;
	private int identifierNumber;
	private int stringNumber;
	private VirtualMachine3D[] virtualMachine3DTable;
	private String[] methodTable;
	private int[] identifierTypeTable;
	private int[] integerTable;
	private String[] stringTable;
	private int beginInstructionPointer,instructionPointer;
	private String[] instructions;
	private Image screenImage,iconImage;
	private String instruction;
	private int index,length;
	private final int INT=1,STR=2,ARY=3,EXIT=-1;
	private boolean isReturn=false;
	private StringQueue integerQueue;
	private StringStack stack;
	private Toolkit toolkit;
	private Graphics g;
	private Image image;
	private int screenWidth,screenHeight;
	private Canvas3D canvas3D;
	private BranchGroup branchGroup;
	private Transform3D transform3D;
	private TransformGroup transformGroup;
	private Material material;
	private Font3D font3D;
	public void config(String className,String parameters)
	{
		this.toolkit=Toolkit.getDefaultToolkit();
		this.screenWidth=(int)toolkit.getScreenSize().getWidth();
		this.screenHeight=(int)toolkit.getScreenSize().getHeight();
		this.screenImage=new BufferedImage(screenWidth,screenHeight,1);
		this.g=screenImage.getGraphics();
		this.getClassDirectoryAndClassName(className);
		this.integerQueue=new StringQueue();
		this.stack=new StringStack();
		this.getVirtualMachine3DInstructions();
		this.enQueueParameters(parameters);
	}
	public void config(String className,StringQueue integerQueue,StringStack stack,Transform3D transform3D,TransformGroup transformGroup,Material material,Font3D font3D)
	{
		this.getClassDirectoryAndClassName(className);
		this.integerQueue=integerQueue;
		this.stack=stack;
		this.transform3D=transform3D;
		this.transformGroup=transformGroup;
		this.material=material;
		this.font3D=font3D;
		this.getVirtualMachine3DInstructions();
	}
	public VirtualMachine3D(String className,StringQueue integerQueue,StringStack stack,Transform3D transform3D,TransformGroup transformGroup,Material material,Font3D font3D)
	{
		this.getClassDirectoryAndClassName(className);
		this.integerQueue=integerQueue;
		this.stack=stack;
		this.transform3D=transform3D;
		this.transformGroup=transformGroup;
		this.material=material;
		this.font3D=font3D;
		this.getVirtualMachine3DInstructions();
	}
	private void configJava3D()
	{
		GraphicsConfiguration GraphicsConfiguration1=SimpleUniverse.getPreferredConfiguration();
		this.canvas3D=new Canvas3D(GraphicsConfiguration1);
		this.canvas3D.setBounds(-5,-5,screenWidth+5,screenHeight+5);
		this.add(this.canvas3D);
		this.branchGroup=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		this.branchGroup.addChild(Background1);
		Color3f color3f=new Color3f(Color.orange);
		Vector3f vector3f=new Vector3f(-1f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		this.branchGroup.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		this.branchGroup.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		this.branchGroup.addChild(MouseRotate1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		this.branchGroup.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		this.branchGroup.addChild(MouseZoom1);
		this.transformGroup=TransformGroup1;
		this.material=new Material();
		this.material.setDiffuseColor(new Color3f(1f,0.8f,0f));
		this.transform3D=new Transform3D();
		this.font3D=new Font3D(new Font(null,20,Font.BOLD),new FontExtrusion());

	}
	public VirtualMachine3D(String className,String parameters)
	{
		this.toolkit=Toolkit.getDefaultToolkit();
		this.screenWidth=(int)toolkit.getScreenSize().getWidth();
		this.screenHeight=(int)toolkit.getScreenSize().getHeight();
		this.configJava3D();
		this.getClassDirectoryAndClassName(className);
		this.integerQueue=new StringQueue();
		this.stack=new StringStack();
		this.getVirtualMachine3DInstructions();
		this.enQueueParameters(parameters);
		this.run();
		this.branchGroup.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(this.canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(this.branchGroup);
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
	private void enQueueParameters(String parameters)
	{
		if(parameters.equals(""))return;
		this.index=0;
		this.instruction=parameters;
		this.length=instruction.length();
		while(index<length)
		{
			int p=this.nextInt();
			this.integerQueue.enQueue(p);
		}
	}
	private void getVirtualMachine3DInstructions()
	{
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(classDirectory+className+".classb"));
			this.classNumber=Integer.parseInt(BufferedReader1.readLine());
			this.virtualMachine3DTable=new VirtualMachine3D[classNumber];
			for(int i=0;i<classNumber;i++)
			{
				String className=BufferedReader1.readLine();
				File file=new File(classDirectory+className+".javab");
				if(file.exists())this.generateVirtualMachine3DCode(classDirectory+className);
				this.virtualMachine3DTable[i]=new VirtualMachine3D(classDirectory+className,integerQueue,stack,transform3D,transformGroup,material,font3D);
//				this.virtualMachine3DTable[i].config(classDirectory+className,integerQueue,stack,transform3D,transformGroup,material,font3D);
			}
			this.methodNumber=Integer.parseInt(BufferedReader1.readLine());
			this.methodTable=new String[methodNumber*2];
			for(int i=0;i<methodNumber;i++)
			{
				this.methodTable[i*2+0]=BufferedReader1.readLine();
				this.methodTable[i*2+1]=BufferedReader1.readLine();
			}
			this.identifierNumber=Integer.parseInt(BufferedReader1.readLine());
			this.identifierTypeTable=new int[identifierNumber];
			this.integerTable=new int[identifierNumber];
			this.stringNumber=Integer.parseInt(BufferedReader1.readLine());
			this.stringTable=new String[stringNumber];
			int length=Integer.parseInt(BufferedReader1.readLine());
			for(int i=0;i<length;i++)this.stringTable[i]=BufferedReader1.readLine();
			for(int i=length;i<stringNumber;i++)this.stringTable[i]="";
			this.beginInstructionPointer=Integer.parseInt(BufferedReader1.readLine());
			length=Integer.parseInt(BufferedReader1.readLine());
			this.instructions=new String[length];
			for(int i=0;i<length;i++)this.instructions[i]=BufferedReader1.readLine();
			BufferedReader1.close();
		}
		catch(Exception e){e.printStackTrace();}
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
		else if(OP.equals("ARY"))
		{
			this.nextChar();
			int i=this.nextInt();
			this.identifierTypeTable[i]=ARY;
		}
		else if(OP.equals("STR"))
		{
			this.nextChar();
			int i=this.nextInt();
			this.identifierTypeTable[i]=STR;
		}
		else if(OP.equals("LEN"))
		{
			this.nextChar();
			int i0=this.nextInt();
			this.nextChar();
			int i1=this.nextInt();
			this.identifierTypeTable[i0]=ARY+i1;
			for(int i=0;i<i1;i++)this.identifierTypeTable[integerTable[i0]+i]=INT;
		}
		else if(OP.equals("MOV"))
		{
			char c=this.nextChar();
			int i0=this.nextInt();
			if(c=='@')i0=integerTable[i0];
			c=this.nextChar();
			int i1=this.nextInt();
			if(c=='&')this.integerTable[i0]=integerTable[i1];
			else if(c=='@')this.integerTable[i0]=integerTable[integerTable[i1]] ;
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
				if(c=='&')
				{
					if(identifierTypeTable[i]==STR)s+=stringTable[integerTable[i]];
					else s+=integerTable[i];
				}
				else if(c=='@')
				{
					if(identifierTypeTable[i]==STR)s+=stringTable[integerTable[integerTable[i]]];
					else s+=integerTable[integerTable[i]];
				}
				else if(c=='$')s+=stringTable[i];
				else s+=i;
				while(index<length)
				{
					char op=this.nextChar();
					c=this.nextChar();
					i=this.nextInt();
					if(c=='&')
					{
						if(identifierTypeTable[i]==STR)s+=stringTable[integerTable[i]];
						else s+=integerTable[i];
					}
					else if(c=='@')
					{
						if(identifierTypeTable[i]==STR)s+=stringTable[integerTable[integerTable[i]]];
						else s+=integerTable[integerTable[i]];
					}
					else if(c=='$')s+=stringTable[i];
					else s+=i;
				}
				stringTable[r]=s;
			}
		}
		else if(OP.equals("MUL"))
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
				else i0/=i1;
			}
			integerTable[r]=i0;
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
			int i0=this.nextInt();
			if(c=='&')i0=integerTable[i0];
			else if(c=='@')i0=integerTable[integerTable[i0]];
			c=this.nextChar();
			int i1=this.nextInt();
			if(c=='&')i1=integerTable[i1];
			else if(c=='@')i1=integerTable[integerTable[i1]];
			integerTable[r]=(i0==i1?1:0);
		}
		else if(OP.equals("NE"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			int i0=this.nextInt();
			if(c=='&')i0=integerTable[i0];
			else if(c=='@')i0=integerTable[integerTable[i0]];
			c=this.nextChar();
			int i1=this.nextInt();
			if(c=='&')i1=integerTable[i1];
			else if(c=='@')i1=integerTable[integerTable[i1]];
			integerTable[r]=(i0!=i1?1:0);
		}
		else if(OP.equals("LE"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			int i0=this.nextInt();
			if(c=='&')i0=integerTable[i0];
			else if(c=='@')i0=integerTable[integerTable[i0]];
			c=this.nextChar();
			int i1=this.nextInt();
			if(c=='&')i1=integerTable[i1];
			else if(c=='@')i1=integerTable[integerTable[i1]];
			integerTable[r]=(i0<=i1?1:0);
		}
		else if(OP.equals("LT"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			int i0=this.nextInt();
			if(c=='&')i0=integerTable[i0];
			else if(c=='@')i0=integerTable[integerTable[i0]];
			c=this.nextChar();
			int i1=this.nextInt();
			if(c=='&')i1=integerTable[i1];
			else if(c=='@')i1=integerTable[integerTable[i1]];
			integerTable[r]=(i0<i1?1:0);
		}
		else if(OP.equals("GE"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			int i0=this.nextInt();
			if(c=='&')i0=integerTable[i0];
			else if(c=='@')i0=integerTable[integerTable[i0]];
			c=this.nextChar();
			int i1=this.nextInt();
			if(c=='&')i1=integerTable[i1];
			else if(c=='@')i1=integerTable[integerTable[i1]];
			integerTable[r]=(i0>=i1?1:0);
		}
		else if(OP.equals("GT"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			int i0=this.nextInt();
			if(c=='&')i0=integerTable[i0];
			else if(c=='@')i0=integerTable[integerTable[i0]];
			c=this.nextChar();
			int i1=this.nextInt();
			if(c=='&')i1=integerTable[i1];
			else if(c=='@')i1=integerTable[integerTable[i1]];
			integerTable[r]=(i0>i1?1:0);
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
				int i1=this.nextInt();
				if(c=='&')
				{
					if(identifierTypeTable[i1]==STR)s+=stringTable[integerTable[i1]];
					else s+=integerTable[i1];	
				}
				else if(c=='$')s+=stringTable[i1];
				else s+=i1;
				stringTable[integerTable[i0]]=s;
			}
			else
			{
				char c=this.nextChar();
				int i1=this.nextInt();
				if(c=='&')i1=integerTable[i1];
				this.integerTable[i0]+=i1;
			}
		}
		else if(OP.equals("DEL"))
		{
			this.nextChar();
			int i0=this.nextInt();
			char c=this.nextChar();
			int i1=this.nextInt();
			if(c=='&')i1=integerTable[i1];
			this.integerTable[i0]-=i1;
		}
		else if(OP.equals("ENQI"))
		{
			char c=this.nextChar();
			int i=this.nextInt();
			if(c=='&')i=integerTable[i];
			else if(c=='@')i=integerTable[integerTable[i]];
			this.integerQueue.enQueue(i);
		}
		else if(OP.equals("DEQI"))
		{
			char c=this.nextChar();
			int i=this.nextInt();
			String integer=integerQueue.deQueue();
			if(c=='&')this.integerTable[i]=Integer.parseInt(integer);
			else if(c=='@')this.integerTable[integerTable[i]]=Integer.parseInt(integer);
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
		else if(OP.equals("PRINT"))
		{
			String s="";
			char c=this.nextChar();
			int i=this.nextInt();
			if(c=='&')
			{
				if(identifierTypeTable[i]==STR)s=stringTable[integerTable[i]];
				else s+=integerTable[i];
			}
			else if(c=='@')s+=integerTable[integerTable[i]];
			else if(c=='$')s=stringTable[i];
			else s+=i;
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
			int i0=this.nextInt();
			if(c=='&')i0=integerTable[i0];
			else if(c=='@')i0=integerTable[integerTable[i0]];
			integerTable[r]=(int)(100*Math.sin(i0*Math.PI/180));
		}
		else if(OP.equals("COS"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			c=this.nextChar();
			int i0=this.nextInt();
			if(c=='&')i0=integerTable[i0];
			else if(c=='@')i0=integerTable[integerTable[i0]];
			integerTable[r]=(int)(100*Math.cos(i0*Math.PI/180));
		}
		else if(OP.equals("NEW"))
		{
			String className=this.nextString();
			VirtualMachine3D virtualMachine3D=this.getVirtualMachine3D(className);
			virtualMachine3D.run();
		}	
		else if(OP.equals("METHOD"))
		{
			this.stack.push(instructionPointer+1);
			String className=this.nextString();
			String methodName=this.nextString();
			VirtualMachine3D virtualMachine3D=this.getVirtualMachine3D(className);
			virtualMachine3D.runMethod(methodName);
		}
		else if(OP.equals("COLOR"))
		{
			char c=this.nextChar();
			int r=this.nextInt();
			if(c=='&')r=integerTable[r];
			c=this.nextChar();
			int g=this.nextInt();
			if(c=='&')g=integerTable[g];
			c=this.nextChar();
			int b=this.nextInt();
			if(c=='&')b=integerTable[b];
			if(r>255)r=255;
			if(g>255)g=255;
			if(b>255)b=255;
			this.material=new Material();
			this.material.setDiffuseColor(new Color3f((r+0f)/255,(g+0f)/255,(b+0f)/255));
		}
		else if(OP.equals("FONT"))
		{
			char c=this.nextChar();
			int size=this.nextInt();
			if(c=='&')size=integerTable[size];
			c=this.nextChar();
			int bold=this.nextInt();
			if(c=='&')bold=integerTable[bold];
			if(bold==0)this.font3D=new Font3D(new Font(null,Font.PLAIN,size),new FontExtrusion());
			else this.font3D=new Font3D(new Font(null,Font.BOLD,size),new FontExtrusion());
		}
		else if(OP.equals("SCALE"))
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
			int z=this.nextInt();
			if(c=='&')z=integerTable[z];
			else if(c=='@')z=integerTable[integerTable[z]];
			this.transform3D.setScale(new Vector3d(x/100.0,y/100.0,z/100.0));
		}
		else if(OP.equals("TRANS"))
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
			int z=this.nextInt();
			if(c=='&')z=integerTable[z];
			else if(c=='@')z=integerTable[integerTable[z]];
			this.transform3D.setTranslation(new Vector3d(x/100.0,y/100.0,z/100.0));
		}
		else if(OP.equals("TEXT"))
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
			Appearance appearance=new Appearance();
			appearance.setMaterial(material);
			Text3D text3D=new Text3D(font3D,s);
			TransformGroup TransformGroup1=new TransformGroup(transform3D);
			TransformGroup1.addChild(new Shape3D(text3D,appearance));
			this.transformGroup.addChild(TransformGroup1);
		}
		this.instructionPointer++;
	}
	public void runMethod(String methodName)
	{

		this.isReturn=false;
		this.instructionPointer=this.getPointer(methodName);
		while(true)
		{
			this.instruction=instructions[instructionPointer];
			this.length=instruction.length();
			this.execute();
			if(isReturn)return;
		}
	}
	private VirtualMachine3D getVirtualMachine3D(String className)
	{
		for(int i=0;i<classNumber;i++)
		{
			if(virtualMachine3DTable[i].className.equals(className))return virtualMachine3DTable[i];
		}
		return null;
	}
	private int getPointer(String methodName)
	{
		for(int i=0;i<methodNumber;i++)
		{
			if(methodTable[i*2+0].equals(methodName))
			{
				return Integer.parseInt(methodTable[i*2+1]);
			}
		}
		return -1;
	}
	public void showIdentifierTable()
	{
		for(int i=0;i<integerTable.length;i++)
		{
			String s="integerTable["+i+"]="+integerTable[i];
			String t="\ttype : ";
			if(identifierTypeTable[i]==INT)t+="INT";
			else if(identifierTypeTable[i]==STR)t+="STR";
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
	public static void generateVirtualMachine3DCode(String className)
	{
		String code=getCode(className);
		LexicalAnalyser LexicalAnalyser1=new LexicalAnalyser(code);
		String[] keyWordList=LexicalAnalyser1.getKeyWordList();
		int[] wordTable=LexicalAnalyser1.getWordTable();
		Identifier[] identifierTable=LexicalAnalyser1.getIdentifierTable();
		String[] stringTable=LexicalAnalyser1.getStringTable();
		SyntaxTranslator SyntaxTranslator1=new SyntaxTranslator(keyWordList,wordTable,identifierTable,stringTable);
		SyntaxTranslator1.generateVirtualMachine3DInstructions(className);
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
	private StringQueue classQueue;
	private String[] instructions;
	private Identifier[] identifiers;
	private int instructionPointer,beginInstructionPointer;
	private int classNumber,methodNumber,identifierNumber,stringNumber;
	private String[] keyWordList;
	private int[] wordTable;
	private String[] stringTable;
	private int w0,w1;
	private int index;
	private int length;
	private Identifier[] identifierTable;
	public SyntaxTranslator(String[] keyWordList,int[] wordTable,Identifier[] identifierTable,String[] stringTable)
	{
		this.keyWordList=keyWordList;
		this.wordTable=wordTable;
		this.identifierTable=identifierTable;
		this.classNumber=0;
		this.methodNumber=0;
		this.identifierNumber=identifierTable.length;
		this.stringTable=stringTable;
		this.stringNumber=stringTable.length;
		this.length=wordTable.length/2;
		this.instructionsQueue=new StringQueue();
		this.classQueue=new StringQueue();
		this.getVirtualMachine3DInstructions();
		this.instructions=this.instructionsQueue.getStrings();
	}
	private void getVirtualMachine3DInstructions()
	{
		this.getClassDeclarationInstructions();
		while(index<length)this.getMethodsInstructions();
	}
	public void generateVirtualMachine3DInstructions(String className)
	{
		try
		{
			PrintWriter PrintWriter1=new PrintWriter(className+".classb");
			String[] classes=this.classQueue.getStrings();
			PrintWriter1.println(classes.length);
			for(int i=0;i<classes.length;i++)PrintWriter1.println(classes[i]);
			PrintWriter1.println(methodNumber);
			for(int i=0;i<identifierTable.length;i++)
			{
				if(identifierTable[i].Type==WordList.Key_public)
				{
					int pointer=identifierTable[i].Pointer;
					if(pointer!=beginInstructionPointer)
					{
						PrintWriter1.println(identifierTable[i].Name);
						PrintWriter1.println(pointer);
					}
				}
			}
			PrintWriter1.println(identifierNumber);
			PrintWriter1.println(stringNumber);
			PrintWriter1.println(stringTable.length);
			for(int i=0;i<stringTable.length;i++)PrintWriter1.println(stringTable[i]);
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
	private void getMethodsInstructions()
	{
		this.nextWord();
		if(w0==Word.KEY&&(w1==WordList.Key_int||w1==WordList.Key_public))
		{
			boolean isPublic=(w1==WordList.Key_public)?true:false;
			this.nextWord();
			if(w0==Word.ID)
			{
				this.identifierTable[w1].Type=WordList.Key_public;
				this.identifierTable[w1].Pointer=instructionsQueue.length();
				if(isPublic)this.beginInstructionPointer=identifierTable[w1].Pointer;
				else this.methodNumber++;
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
		if(w0==Word.KEY&&w1==WordList.Key_int)
		{
			this.nextWord();
			if(w0==Word.ID)
			{
				this.identifierTable[w1].Type=WordList.Key_int;
				this.instructionsQueue.enQueue("INT &"+w1);
				this.instructionsQueue.enQueue("DEQI &"+w1);
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
			if(t0==Word.ARI&&(t1==Word.ACC||t1==Word.DEL))
			{
				String instruction="";
				if(t1==Word.ACC)instruction+="ACC ";
				else instruction+="DEL ";
				instruction+="&"+w1+",";
				this.nextWord();
				String id=this.getAddExpressionInstructions();
				instruction+=id;
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
			this.index--;
			this.getArithmeticAssignmentInstructions();
		}
		else if(w0==Word.KEY&&w1==WordList.Key_String)this.getStringDeclarationInstructions();
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
	}
	private void getIfElseExpressionInstructions()
	{
		this.nextWord();
		if(w0==Word.KEY&&w1==WordList.Key_if)
		{
			this.nextWord();
			String id=this.getAndExpressionInstructions();
			int ip_if=instructionsQueue.length();
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
								this.instructionsQueue.insert("JZ "+id+","+(ip_else+2),ip_if);
								this.instructionsQueue.insert("JMP "+(ip_end+2),ip_else+1);
							}
						}
						else if(w0==Word.KEY&&w1==WordList.Key_if)
						{
							this.index--;
							this.instructionsQueue.insert("JZ "+id+","+(ip_else+2),ip_if);
							int ip_begin=instructionsQueue.length();
							this.instructionsQueue.enQueue("NOP");
							this.getIfElseExpressionInstructions();
							int ip_end=instructionsQueue.length();
							this.instructionsQueue.set(ip_begin,"JMP "+ip_end);
						}
					}
					else
					{
						this.instructionsQueue.insert("JZ "+id+","+(ip_else+1),ip_if);
						this.index--;
					}
				}
			}
		}
	}
	private void getWhileExpressionInstructions()
	{
		int ip_while=instructionsQueue.length();
		this.nextWord();
		String id=this.getAndExpressionInstructions();
		int ip_begin=instructionsQueue.length();
		this.index++;
		this.nextWord();
		if(w0==Word.MRK&&w1==Word.L_BRACE)
		{
			this.getCodeBlockInstructions();
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.R_BRACE)
			{
				int ip_end=instructionsQueue.length();
				String instruction="JZ "+id+","+(ip_end+2);
				this.instructionsQueue.insert(instruction,ip_begin);
				instruction="JMP "+(ip_while+1);
				this.instructionsQueue.enQueue(instruction);
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
					this.instructionsQueue.enQueue("JMP "+(ip_begin-1));
					int ip_end=instructionsQueue.length();
					this.instructionsQueue.set(ip_begin,"JZ "+id+","+ip_end);
				}
			}
		}
		
	}
	private void getReturnExpressionInstructions()
	{
		String id=this.getAddExpressionInstructions();
		this.instructionsQueue.enQueue("ENQI "+id);
		this.instructionsQueue.enQueue("RET");
		this.index++;
	}
	private void getNewClassExpressionInstructions()
	{
		this.nextWord();
		if(w0==Word.ID&&identifierTable[w1].Type==WordList.Key_class)
		{
			String newClass="NEW "+identifierTable[w1].Name;
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.L_PARENTHESE)
			{
				this.w0=wordTable[index*2+0];
				this.w1=wordTable[index*2+1];
				while(!(w0==Word.MRK&&w1==Word.R_PARENTHESE))
				{
					String instruction="ENQI "+this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue(instruction);
					this.nextWord();
				}
			}
			this.instructionsQueue.enQueue(newClass);
			this.index++;
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
				else if(thisMethod.equals("setTranslation"))
				{
					this.index++;
					String x=this.getAddExpressionInstructions();
					this.index++;
					String y=this.getAddExpressionInstructions();
					this.index++;
					String z=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("TRANS "+x+","+y+","+z);
					this.index+=2;
				}
				else if(thisMethod.equals("setScale"))
				{
					this.index++;
					String x=this.getAddExpressionInstructions();
					this.index++;
					String y=this.getAddExpressionInstructions();
					this.index++;
					String z=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("SCALE "+x+","+y+","+z);
					this.index+=2;
				}
				else if(thisMethod.equals("addText3D"))
				{
					this.index++;
					String s=this.getStringAddExpressionInstructions();
					this.instructionsQueue.enQueue("TEXT "+s);
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
				this.instructionsQueue.enQueue("INT "+id);
				this.index++;
				this.nextWord();
				String id1=this.getAddExpressionInstructions();
				this.instructionsQueue.enQueue("SIN "+id+","+id1);
				this.index++;
				return id;
			}
			else if(identifierTable[id0].Name.equals("cos"))
			{
				String id="&"+(identifierNumber++);
				this.instructionsQueue.enQueue("INT "+id);
				this.index++;
				this.nextWord();
				String id1=this.getAddExpressionInstructions();
				this.instructionsQueue.enQueue("COS "+id+","+id1);
				this.index++;
				return id;
			}
		}
		return "&-1";
	}
	private void getIntDeclarationInstructions()
	{
		if(index+1>=length)return;
		int w0=this.wordTable[(index+1)*2+0];
		int w1=this.wordTable[(index+1)*2+1];
		if(w0==Word.MRK&&w1==Word.COMMA)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_int;
			this.instructionsQueue.enQueue("INT &"+w1);
			this.index+=2;
			this.getIntDeclarationInstructions();
		}
		else if(w0==Word.ARI&&w1==Word.MOV)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_int;
			this.instructionsQueue.enQueue("INT &"+w1);
			w0=this.wordTable[(index+2)*2+0];
			w1=this.wordTable[(index+2)*2+1];
			this.getArithmeticAssignmentInstructions();
			this.index++;
			this.getIntDeclarationInstructions();
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
						if(w0==Word.KEY&&w1==WordList.Key_int)
						{
							int startPointer=identifierNumber;
							this.identifierTable[id].Pointer=startPointer;
							this.instructionsQueue.enQueue("MOV &"+id+", "+startPointer);
							this.getArrayAssignmentInstructions(id);
						}
					}
				}
			}
		}
	}
	private void getArrayAssignmentInstructions(int id)
	{
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
					this.instructionsQueue.enQueue("LEN &"+id+", "+(++arrayLength));
					this.index=arrayIndex;
					int startPointer=identifierNumber;
					this.identifierNumber+=arrayLength;
					for(int i=0;i<arrayLength;i++)
					{
						String id1=this.getAddExpressionInstructions();
						String instruction="MOV &"+(startPointer+i)+","+id1;
						this.instructionsQueue.enQueue(instruction);
						this.index++;
					}
					this.index++;
				}
			}
			else
			{
				if(w0==Word.INT)
				{
					this.identifierNumber+=w1;
					String instruction="LEN &"+id+", "+w1;
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
		if(w1==Word.COMMA)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_String;
			this.instructionsQueue.enQueue("STR &"+w1);
			this.index+=2;
			this.getStringDeclarationInstructions();
		}
		else if(w1==Word.MOV)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_String;
			this.instructionsQueue.enQueue("STR &"+w1);
			w0=this.wordTable[(index+2)*2+0];
			w1=this.wordTable[(index+2)*2+1];
			this.getStringAssignmentInstructions();
			this.index++;
			this.getStringDeclarationInstructions();
		}
		else if(w1==Word.SEMICOLON)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_String;
			this.instructionsQueue.enQueue("STR &"+w1);
			this.index+=2;
		}
	}
	private void getCallMethodInstructions()
	{
		this.w1=this.wordTable[index*2+1];
		int ip=this.identifierTable[w1].Pointer;
		this.index+=2;
		this.w0=this.wordTable[index*2+0];
		this.w1=this.wordTable[index*2+1];
		if(w0==Word.MRK&&w1==Word.R_PARENTHESE)
		{
			this.instructionsQueue.enQueue("CALL "+ip);
			this.index++;
			return;
		}
		while(true)
		{
			String id=this.getAddExpressionInstructions();
			this.instructionsQueue.enQueue("ENQI "+id);
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.COMMA){}
			else if(w0==Word.MRK&&w1==Word.R_PARENTHESE)break;
		}
		this.instructionsQueue.enQueue("CALL "+ip);
	}

	private void getCallMethodInstructions(String className)
	{
		this.index+=2;
		this.w1=this.wordTable[index*2+1];
		String method=this.identifierTable[w1].Name;
		this.index+=2;
		this.w0=this.wordTable[index*2+0];
		this.w1=this.wordTable[index*2+1];
		if(w0==Word.MRK&&w1==Word.R_PARENTHESE)
		{
			this.instructionsQueue.enQueue("METHOD "+className+","+method);
			this.index++;
			return;
		}
		while(true)
		{
			String id=this.getAddExpressionInstructions();
			this.instructionsQueue.enQueue("ENQI "+id);
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.COMMA){}
			else if(w0==Word.MRK&&w1==Word.R_PARENTHESE)break;
		}
		this.instructionsQueue.enQueue("METHOD "+className+","+method);
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
	private void getStringAssignmentInstructions()
	{
		String id0="&"+this.wordTable[index*2+1];
		index+=2;
		String id1=this.getStringAddExpressionInstructions();
		String instruction="MOV "+id0+","+id1;
		this.instructionsQueue.enQueue(instruction);
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
	private String getStringAddExpressionInstructions()
	{
		String id=this.getArithmeticIdentifierValue();
		if(wordTable[index*2+1]==Word.ADD)
		{
			int newSID=(stringNumber++);
			String newID="&"+(identifierNumber++);
			String instruction="ADD $"+newSID+","+id;
			while(wordTable[index*2+0]==Word.ARI&&wordTable[index*2+1]==Word.ADD)
			{
				this.index++;
				id=this.getArithmeticIdentifierValue();
				instruction+=",+"+id;
			}
			this.instructionsQueue.enQueue("STR "+newID);
			this.instructionsQueue.enQueue("MOV "+newID+",$"+newSID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getMulExpressionInstructions()
	{
		String id=this.getArithmeticIdentifierValue();
		if(wordTable[index*2+1]==Word.MUL||wordTable[index*2+1]==Word.DIV)
		{
			String newID="&"+(identifierNumber++);
			String instruction="MUL "+newID+","+id;
			while(index<length&&wordTable[index*2+1]==Word.MUL||wordTable[index*2+1]==Word.DIV)
			{
				String sgn=(wordTable[index*2+1]==Word.MUL)?"*":"/";
				this.index++;
				id=this.getArithmeticIdentifierValue();
				instruction+=","+sgn+id;
			}
			this.instructionsQueue.enQueue("INT "+newID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getArithmeticIdentifierValue()
	{
		if(wordTable[index*2+0]==Word.ID)
		{
			int id0=wordTable[index*2+1];
			if(identifierTable[id0].Type==WordList.Key_array)
			{
				this.index+=2;
				String id2=this.getAddExpressionInstructions();
				String newID="&"+(identifierNumber++);
				String instruction="INT "+newID;
				this.instructionsQueue.enQueue(instruction);
				instruction="ADD "+newID+","+id2+",+ "+identifierTable[id0].Pointer;
				this.instructionsQueue.enQueue(instruction);
				newID=newID.substring(1,newID.length());
				String id="@"+newID;
				this.index++;
				return id;
			}
			else if(identifierTable[id0].Type==WordList.Key_public)
			{
				String id="&"+(identifierNumber++);
				this.getCallMethodInstructions();
				this.instructionsQueue.enQueue("INT "+id);
				this.instructionsQueue.enQueue("DEQI "+id);
				return id;
			}
			else if(identifierTable[id0].Type==WordList.Key_class)
			{
				String className=identifierTable[id0].Name;
				String id="&"+(identifierNumber++);
				this.getCallMethodInstructions(className);
				this.instructionsQueue.enQueue("INT "+id);
				this.instructionsQueue.enQueue("DEQI "+id);
				return id;
			}
			else if(identifierTable[id0].Type==WordList.Key_int)
			{
				String id="&"+id0;
				this.index++;
				return id;
			}
			else return "&-1";
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
		else if(wordTable[index*2+0]==Word.KEY&&wordTable[index*2+1]==WordList.Key_this)
		{
			this.index+=2;
			return this.getThisExpressionInstructions(0);
		}
		else if(wordTable[index*2+1]==Word.L_PARENTHESE)
		{
			this.index++;
			String id=this.getAddExpressionInstructions();
			if(wordTable[index*2+1]==Word.R_PARENTHESE)
			{
				this.index++;
				return id;
			}
			else return "&-1";
		}
		else return "&-1";
	}
	private void getLogicAssignmentInstructions()
	{
		String id0="&"+this.wordTable[index*2+1];
		index+=2;
		String id1=this.getAndExpressionInstructions();
		String instruction="MOV "+id0+","+id1;
		this.instructionsQueue.enQueue(instruction);
	}
	private String getAndExpressionInstructions()
	{
		String id=this.getCmpExpressionInstructions();
		if(wordTable[index*2+1]==Word.AND||wordTable[index*2+1]==Word.OR)
		{
			String newID="&"+(identifierNumber++);
			String instruction="AND "+newID+","+id;
			while(index<length&&wordTable[index*2+1]==Word.AND||wordTable[index*2+1]==Word.OR)
			{
				String sgn=(wordTable[index*2+1]==Word.AND)?"*":"+";
				this.index++;
				id=this.getCmpExpressionInstructions();
				instruction+=","+sgn+id;

			}
			this.instructionsQueue.enQueue("INT "+newID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getCmpExpressionInstructions()
	{
		String id=this.getNotExpressionInstructions();
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
			id=this.getNotExpressionInstructions();
			instruction+=","+id;
			this.instructionsQueue.enQueue("INT "+newID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getNotExpressionInstructions()
	{
		if(wordTable[index*2+1]==Word.NOT)
		{
			this.index++;
			String newID="&"+(identifierNumber++);
			String id=this.getLogicIdentifierValue();
			this.instructionsQueue.enQueue("INT "+newID);
			String instruction="NOT "+newID+","+id;
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return this.getAddExpressionInstructions();
	}
	private String getLogicIdentifierValue()
	{
		if(wordTable[index*2+0]==Word.ID)
		{
			String id="&"+wordTable[index*2+1];
			this.index++;
			return id;
		}
		else if(wordTable[index*2+0]==Word.INT)
		{
			String id=" "+wordTable[index*2+1];
			this.index++;
			return id;
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
		else return "&-1";
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
	private String[] keyWordList;
	private IdentifierList identifierList;
	private Identifier[] identifierTable;
	private StringQueue charQueue;
	private StringQueue stringQueue;
	public LexicalAnalyser(String code)
	{
		this.index=0;
		this.code=code;
		this.isCode=true;
		this.length=code.length();
		this.charQueue=new StringQueue();
		this.stringQueue=new StringQueue();
		this.keyWordList=WordList.getKeyWordList();
		this.identifierList=new IdentifierList();
		this.createWordTable();
		this.createCharTable();
		this.createStringTable();
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
			while(isAlpha(c)||isDigit(c))
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
			while(isDigit(c))
			{
				s+=c;
				c=code.charAt(index++);
				if(index>=length)break;
			}
			this.index--;
			return new Word(Word.INT,Integer.parseInt(s));
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
				if(c=='/'){this.isCode=true;break;}
				else{this.index--;return new Word(Word.ARI,Word.MUL);}
				case '/':
				if(index>=length)break;
				c=code.charAt(index++);
				if(c=='*'){this.isCode=false;break;}
				else{this.index--;return new Word(Word.ARI,Word.DIV);}
				case '&':
				c=code.charAt(index++);
				if(c=='&')return new Word(Word.LOG,Word.AND);
				else{this.index--;return new Word(Word.ARI,Word.AND);}
				case '|':
				c=code.charAt(index++);
				if(c=='|')return new Word(Word.LOG,Word.OR);
				else{this.index--;return new Word(Word.ARI,Word.OR);}
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
			"class"
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
	public void show()
	{
		System.out.println(stringStack);
	}
	public boolean isNotEmpty()
	{
		return (length>0);
	}
}