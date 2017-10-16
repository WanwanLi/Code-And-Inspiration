import java.applet.*;
import java.awt.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndLSTree3D extends Applet
{
	public void init()
	{
		GraphicsConfiguration GraphicsConfiguration1=SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D=new Canvas3D(GraphicsConfiguration1);
		this.setLayout(new BorderLayout());
		this.add(canvas3D);	
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Color3f color3f=new Color3f(0f,0f,0f);
		Background Background1=new Background(color3f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		int times=3,n=7;
		double L,R,X,Y,Z,aX,aY,wX,wY;
		String S;
		String[] F,LS;
		double PI=Math.PI;
		L=0.2f;
		R=0.005f;
		X=0;
		Y=0;
		Z=0;
		aX=0;
		aY=0;
		wX=PI/6;
		wY=PI/6;
		S="FE";
		F=new String[]{"E","A","B","C","D"};
		LS=new String[]{"[A][B][C][D]","+F.,E","-F.,E","*F.,E","/F.,E"};
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.green));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new LSTree3D(L,R,X,Y,Z,aX,aY,wX,wY,S,F,LS,times,n,Appearance1,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndLSTree3D(),400,400);
	}
}
class LSTree3D extends Shape3D
{
	private int times,n;
	private String S;
	private double L,R,X,Y,Z,aX,aY,wX,wY;
	private double PI=Math.PI;
	private LSStack stack;
	private StringQueue coordinatesQueue,coordinateIndicesQueue;
	Appearance branchAppearance,leafAppearance;
	public LSTree3D(double L,double R,double X,double Y,double Z,double aX,double aY,double wX,double wY,String init,String[] regex,String[] replacement,int times,int n,Appearance branchAppearance,Appearance leafAppearance)
	{
		this.S=init;
		this.L=L;
		this.R=R;
		this.X=X;
		this.Y=Y;
		this.Z=Z;
		this.aX=aX;
		this.aY=aY;
		this.wX=wX;
		this.wY=wY;
		this.times=times;
		this.n=n;
		this.branchAppearance=branchAppearance;
		this.leafAppearance=leafAppearance;
		this.stack=new LSStack();
		this.coordinatesQueue=new StringQueue();
		this.coordinateIndicesQueue=new StringQueue();
		int l=regex.length;
		for(int i=0;i<times;i++)for(int j=0;j<l;j++)this.S=S.replaceAll(regex[j],replacement[j]);
		this.getLSTree();
		Point3d[] coordinates=coordinatesQueue.toPoint3dArray();
		int[] coordinateIndices=coordinateIndicesQueue.toIntArray();
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(coordinates.length,IndexedQuadArray.COORDINATES,coordinateIndices.length);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(branchAppearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
	private void getLSTree()
	{
		for(int i=0;i<S.length();i++)
		{
			switch(S.charAt(i))
			{
				case 'F':this.addBranch();
				case 'f':this.goForward();break;
				case '+':this.turnUp();break;
				case '-':this.turnDown();break;
				case '*':this.turnLeft();break;
				case '/':this.turnRight();break;
				case '[':this.push();break;
				case ']':this.pop();break;
				case'.':this.decayLength();break;
				case',':this.decayRadius();break;
				default:break;
			}
		}
	}
	private void addBranch()
	{
		double X1=X+L*Math.sin(aY)*Math.cos(aX);
		double Y1=Y+L*Math.cos(aY);
		double Z1=Z+L*Math.sin(aY)*Math.sin(aX);
		Point3d p0=new Point3d(X,Y,Z);
		Point3d p1=new Point3d(X1,Y1,Z1);
		Column3D column3D=new Column3D(R,p0,p1,n,null);
		int offset=coordinatesQueue.length();
		this.coordinatesQueue.enQueue(column3D.getCoordinates());
		this.coordinateIndicesQueue.enQueue(column3D.getCoordinateIndices(offset));
	}
	private void goForward()
	{
		this.X+=L*Math.sin(aY)*Math.cos(aX);
		this.Y+=L*Math.cos(aY);
		this.Z+=L*Math.sin(aY)*Math.sin(aX);
	}
	private void turnUp()
	{
		this.aY-=wY;
		if(aY<0)
		{
			this.aY=-aY;
			this.aX+=PI;
			if(aX>2*PI)aX-=2*PI;
		}
	}
	private void turnDown()
	{
		this.aY+=wY;
		if(aY>PI)
		{
			this.aY=2*PI-aY;
			this.aX+=PI;
			if(aX>2*PI)aX-=2*PI;
		}
	}
	private void turnLeft()
	{
		double x,y,z,x1,y1,z1,rotY,rotZ,l;
		x=Math.sin(aY)*Math.cos(aX);
		y=Math.cos(aY);
		z=Math.sin(aY)*Math.sin(aX);
		rotY=aX;
		z1=z*Math.cos(rotY)-x*Math.sin(rotY);
		x1=z*Math.sin(rotY)+x*Math.cos(rotY);
		z=z1;
		x=x1;
		rotZ=aY-PI/2;
		x1=x*Math.cos(rotZ)-y*Math.sin(rotZ);
		y1=x*Math.sin(rotZ)+y*Math.cos(rotZ);
		x=x1;
		y=y1;
		rotY=wX;
		z1=z*Math.cos(rotY)-x*Math.sin(rotY);
		x1=z*Math.sin(rotY)+x*Math.cos(rotY);
		z=z1;
		x=x1;
		rotZ=PI/2-aY;
		x1=x*Math.cos(rotZ)-y*Math.sin(rotZ);
		y1=x*Math.sin(rotZ)+y*Math.cos(rotZ);
		x=x1;
		y=y1;
		rotY=-aX;
		z1=z*Math.cos(rotY)-x*Math.sin(rotY);
		x1=z*Math.sin(rotY)+x*Math.cos(rotY);
		z=z1;
		x=x1;
		l=Math.sqrt(x*x+y*y+z*z);
		this.aY=Math.acos(y/l);
		l=Math.sqrt(x*x+z*z);
		if(l!=0)this.aX=(z>=0?Math.acos(x/l):(PI+Math.acos(-x/l)));
		if(aY>PI)
		{
			this.aY=2*PI-aY;
			this.aX+=PI;
			if(aX>2*PI)aX-=2*PI;
		}
		if(aY<0)
		{
			this.aY=-aY;
			this.aX+=PI;
			if(aX>2*PI)aX-=2*PI;
		}
		if(aX>2*PI)aX-=2*PI;
		if(aX<0)aX+=2*PI;
	}
	private void turnRight()
	{
		double x,y,z,x1,y1,z1,rotY,rotZ,l;
		x=Math.sin(aY)*Math.cos(aX);
		y=Math.cos(aY);
		z=Math.sin(aY)*Math.sin(aX);
		rotY=aX;
		z1=z*Math.cos(rotY)-x*Math.sin(rotY);
		x1=z*Math.sin(rotY)+x*Math.cos(rotY);
		z=z1;
		x=x1;
		rotZ=aY-PI/2;
		x1=x*Math.cos(rotZ)-y*Math.sin(rotZ);
		y1=x*Math.sin(rotZ)+y*Math.cos(rotZ);
		x=x1;
		y=y1;
		rotY=-wX;
		z1=z*Math.cos(rotY)-x*Math.sin(rotY);
		x1=z*Math.sin(rotY)+x*Math.cos(rotY);
		z=z1;
		x=x1;
		rotZ=PI/2-aY;
		x1=x*Math.cos(rotZ)-y*Math.sin(rotZ);
		y1=x*Math.sin(rotZ)+y*Math.cos(rotZ);
		x=x1;
		y=y1;
		rotY=-aX;
		z1=z*Math.cos(rotY)-x*Math.sin(rotY);
		x1=z*Math.sin(rotY)+x*Math.cos(rotY);
		z=z1;
		x=x1;
		l=Math.sqrt(x*x+y*y+z*z);
		this.aY=Math.acos(y/l);
		l=Math.sqrt(x*x+z*z);
		if(l!=0)this.aX=(z>=0?Math.acos(x/l):(PI+Math.acos(-x/l)));
		if(aY>PI)
		{
			this.aY=2*PI-aY;
			this.aX+=PI;
			if(aX>2*PI)aX-=2*PI;
		}
		if(aY<0)
		{
			this.aY=-aY;
			this.aX+=PI;
			if(aX>2*PI)aX-=2*PI;
		}
		if(aX>2*PI)aX-=2*PI;
		if(aX<0)aX+=2*PI;
	}
	private void push()
	{
		this.stack.push(L,R,X,Y,Z,aX,aY,wX,wY);
	}
	private void pop()
	{
		LSNode node=stack.pop();
		this.L=node.L;
		this.R=node.R;
		this.X=node.X;
		this.Y=node.Y;
		this.Z=node.Z;
		this.aX=node.aX;
		this.aY=node.aY;
		this.wX=node.wX;
		this.wY=node.wY;
	}
	private void decayLength()
	{
		this.L/=2;
	}
	private void decayRadius()
	{
		this.R/=2;
	}
	public void replaceIntoF(String[] s)
	{
		int l=s.length;
		for(int i=0;i<l;i++)this.S=S.replaceAll(s[i],"F");
	}
}
class Column3D extends Shape3D
{
	Point3d[] coordinates;
	int[] coordinateIndices;
	public Column3D(double R,Point3d p0,Point3d p1,int n,Appearance appearance)
	{
		double x0=p0.x,y0=p0.y,z0=p0.z,x1=p1.x,y1=p1.y,z1=p1.z;
		double dx=x1-x0,dy=y1-y0,dz=z1-z0;
		double dL=Math.sqrt(dx*dx+dy*dy+dz*dz);
		double dl=Math.sqrt(dx*dx+dz*dz);
		double rotZ=Math.acos(dy/dL);
		double rotY=-((dl==0)?0:((dz>=0)?Math.acos(dx/dl):(Math.PI+Math.acos(-dx/dl))))-Math.PI;
		this.coordinates=new Point3d[2*n+2];
		this.coordinateIndices=new int[4*n*2*3];
		double u=2*Math.PI/(n-1);
		double[][] dXYZ=new double[n][3];
		double x,y,z,xt,yt,zt;
		final int X=0,Y=1,Z=2;
		int v=0;
		for(int j=0;j<n;j++)
		{
			x=R*Math.cos(u*j);
			y=0;
			z=R*Math.sin(u*j);
			xt=x*Math.cos(rotZ)-y*Math.sin(rotZ);
			yt=x*Math.sin(rotZ)+y*Math.cos(rotZ);
			x=xt;
			y=yt;
			zt=z*Math.cos(rotY)-x*Math.sin(rotY);
			xt=z*Math.sin(rotY)+x*Math.cos(rotY);
			z=zt;
			x=xt;
			dXYZ[j][X]=x;
			dXYZ[j][Y]=y;
			dXYZ[j][Z]=z;
		}
		for(int j=0;j<n;j++)this.coordinates[v++]=new Point3d(x1+dXYZ[j][X],y1+dXYZ[j][Y],z1+dXYZ[j][Z]);
		for(int j=0;j<n;j++)this.coordinates[v++]=new Point3d(x0+dXYZ[j][X],y0+dXYZ[j][Y],z0+dXYZ[j][Z]);
		this.coordinates[v++]=new Point3d(x1,y1,z1);
		this.coordinates[v++]=new Point3d(x0,y0,z0);
		v=0;
		for(int j=0;j<n;j++)
		{
			this.coordinateIndices[v++]=0*n+(j+0)%n;
			this.coordinateIndices[v++]=2*n+0;
			this.coordinateIndices[v++]=2*n+0;
			this.coordinateIndices[v++]=0*n+(j+1)%n;
			this.coordinateIndices[v++]=1*n+(j+1)%n;
			this.coordinateIndices[v++]=2*n+1;
			this.coordinateIndices[v++]=2*n+1;
			this.coordinateIndices[v++]=1*n+(j+0)%n;
			this.coordinateIndices[v++]=0*n+(j+0)%n;
			this.coordinateIndices[v++]=0*n+(j+1)%n;
			this.coordinateIndices[v++]=1*n+(j+1)%n;
			this.coordinateIndices[v++]=1*n+(j+0)%n;
		}
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(coordinates.length,IndexedQuadArray.COORDINATES,coordinateIndices.length);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
	public Point3d[] getCoordinates()
	{
		return this.coordinates;
	}
	public int[] getCoordinateIndices(int offset)
	{
		int l=this.coordinateIndices.length;
		int[] newCoordinateIndices=new int[l];
		for(int i=0;i<l;i++)newCoordinateIndices[i]=this.coordinateIndices[i]+offset;
		return newCoordinateIndices;
	}
	private int sgn(double x)
	{
		return (x<0?-1:1);
	}
}
class LSNode
{
	public double L;
	public double R;
	public double X;
	public double Y;
	public double Z;
	public double aX;
	public double aY;
	public double wX;
	public double wY;
	public LSNode Next;	
	public LSNode(double L,double R,double X,double Y,double Z,double aX,double aY,double wX,double wY)
	{
		this.L=L;
		this.R=R;
		this.X=X;
		this.Y=Y;
		this.Z=Z;
		this.aX=aX;
		this.aY=aY;
		this.wX=wX;
		this.wY=wY;
	}	
}
class LSStack
{
	LSNode Head;
	public LSStack()
	{
		Head=null;
	}
	public void push(double L,double R,double X,double Y,double Z,double aX,double aY,double wX,double wY)
	{
		LSNode ls=new LSNode(L,R,X,Y,Z,aX,aY,wX,wY);
		ls.Next=Head;
		Head=ls;
	}
	public LSNode pop() 
	{
		LSNode ls=Head;
		Head=Head.Next;
		return ls;
	}
}
class StringQueue
{
	private String stringQueue;
	private int length;
	public StringQueue()
	{
		this.stringQueue="";
		this.length=0;
	}
	public void enQueue(String string)
	{
		this.stringQueue+=string+";";
		this.length++;
	}
	public void enQueue(int[] array)
	{
		int l=array.length;
		for(int i=0;i<l;i++)this.stringQueue+=array[i]+";";
		this.length+=l;
	}
	public void enQueue(double[] array)
	{
		int l=array.length;
		for(int i=0;i<l;i++)this.stringQueue+=array[i]+";";
		this.length+=l;
	}
	public void enQueue(Point3d[] array)
	{
		int l=array.length;
		for(int i=0;i<l;i++)this.stringQueue+=array[i].x+";"+array[i].y+";"+array[i].z+";";
		this.length+=l;
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
	public int length()
	{
		return this.length;
	}
	public void show()
	{
		System.out.println(stringQueue);
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
	public double[] toDoubleArray()
	{
		String s="";
		int n=0;
		double[] array=new double[length];
		for(int i=0;i<length;i++)
		{
			char c=stringQueue.charAt(n++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(n++);
			}
			array[i]=Double.parseDouble(s);
			s="";
		}
		return array;
	}
	public Point3d[] toPoint3dArray()
	{
		String s="";
		int n=0;
		Point3d[] array=new Point3d[length];
		for(int i=0;i<length;i++)
		{
			char c=stringQueue.charAt(n++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(n++);
			}
			double x=Double.parseDouble(s);
			s="";
			c=stringQueue.charAt(n++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(n++);
			}
			double y=Double.parseDouble(s);
			s="";
			c=stringQueue.charAt(n++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(n++);
			}
			double z=Double.parseDouble(s);
			s="";
			array[i]=new Point3d(x,y,z);
		}
		return array;
	}
	public boolean isNotEmpty()
	{
		return (this.stringQueue.length()>0);
	}
}