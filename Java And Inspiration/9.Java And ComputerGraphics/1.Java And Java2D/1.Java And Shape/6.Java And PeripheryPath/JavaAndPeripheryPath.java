import java.awt.*;
import java.awt.event.*;
public class JavaAndPeripheryPath
{
	public static void main(String[] args)
	{
		Frame_PeripheryPath Frame_PeripheryPath1=new Frame_PeripheryPath();
		Frame_PeripheryPath1.setVisible(true);
	}
}
class Frame_PeripheryPath extends Frame
{
	PeripheryPath peripheryPath;
	public Frame_PeripheryPath()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		int l=50;
		Point[] points=new Point[l];
		for(int i=0;i<l;i++)points[i]=new Point(100+(int)(1000*Math.random()),100+(int)(500*Math.random()));
		this.peripheryPath=new PeripheryPath(points);
	}
	public void paint(Graphics g)
	{
		peripheryPath.drawPoints(g,10);
		peripheryPath.drawIndices(g);
		peripheryPath.drawPath(g);
	}
}
class PeripheryPath
{
	private Point[] points;
	private int length;
	private int[] peripheryPathIndices;
	private int peripheryPathLength;
	private int k=10000;
	public PeripheryPath(Point[] points)
	{
		this.points=points;
		this.length=points.length;
		this.getPeripheryPath();
	}
	private int getDownMostPointIndex()
	{
		int index=0;
		int maxY=0;
		for(int i=0;i<length;i++)
		{
			if(points[i].y>maxY)
			{
				index=i;
				maxY=points[i].y;
			}
		}
		return index;
	}
	private void getPeripheryPath()
	{
		int downMostIndex=this.getDownMostPointIndex();
		int[] pointIndices=this.getPointIndicesByAngles(downMostIndex);
		int l=points.length,n=0;
		this.peripheryPathIndices=new int[l+1];
		this.peripheryPathIndices[n++]=pointIndices[0];
		this.peripheryPathIndices[n++]=pointIndices[1];
		this.peripheryPathIndices[n++]=pointIndices[2];
		int p0=peripheryPathIndices[0];
		for(int i=3;i<l;i++)
		{
			int p=peripheryPathIndices[n-1];
			int p1=pointIndices[i];
			int p2=peripheryPathIndices[n-2];
			if(isInTriangle(p,p0,p1,p2))
			{
				this.peripheryPathIndices[n-1]=p1;
				p=peripheryPathIndices[n-2];
				p1=peripheryPathIndices[n-1];
				p2=peripheryPathIndices[n-3];
				while(isInTriangle(p,p0,p1,p2)&&n>3)
				{
					n--;
					this.peripheryPathIndices[n-1]=p1;
					p=peripheryPathIndices[n-2];
					p1=peripheryPathIndices[n-1];
					p2=peripheryPathIndices[n-3];
				}
			}
			else this.peripheryPathIndices[n++]=p1;
			
		}
		this.peripheryPathIndices[n++]=this.peripheryPathIndices[0];
		this.peripheryPathLength=n;
	}
	private boolean isInTriangle(int p,int p0,int p1,int p2)
	{
		int x=points[p].x;
		int x0=points[p0].x;
		int x1=points[p1].x;
		int x2=points[p2].x;
		int y=points[p].y;
		int y0=points[p0].y;
		int y1=points[p1].y;
		int y2=points[p2].y;
		Vector v0=new Vector(x0-x,y0-y,0);
		Vector v1=new Vector(x1-x,y1-y,0);
		Vector v2=new Vector(x2-x,y2-y,0);
		Vector v01=new Vector();
		Vector v12=new Vector();
		Vector v20=new Vector();
		v01.cross(v0,v1);
		v12.cross(v1,v2);
		v20.cross(v2,v0);
		Vector v=new Vector();
		if(v.product(v01,v12)>=0&&v.product(v01,v20)>=0)return true;
		else return false;
	}
	private int[] getPointIndicesByAngles(int index)
	{
		int l=points.length;
		double[] angleArray=new double[l];
		for(int i=0;i<l;i++)angleArray[i]=this.getAngle(index,i);
		MergeSort MergeSort1=new MergeSort(angleArray);
		MergeSort1.mergeSort(0,l-1);
		return MergeSort1.getIndex();
	}
	private double getAngle(int p0,int p1)
	{
		if(p0==p1)return 0;
		double dy=points[p1].y-points[p0].y;
		double dx=points[p1].x-points[p0].x;
		double dL=Math.sqrt(dx*dx+dy*dy);
		return Math.acos(dx/dL);
	}
	public void drawPoints(Graphics g,int r)
	{
		for(int i=0;i<points.length;i++)g.fillOval(points[i].x,points[i].y,r,r);
	}
	public void drawIndices(Graphics g)
	{
		for(int i=0;i<points.length;i++)g.drawString(i+"",points[i].x,points[i].y);
	}
	public void drawPath(Graphics g)
	{
		for(int i=0;i<peripheryPathLength-1;i++)g.drawLine(points[peripheryPathIndices[i]].x,points[peripheryPathIndices[i]].y,points[peripheryPathIndices[i+1]].x,points[peripheryPathIndices[i+1]].y);
	}
}
class MergeSort
{
	private double[] array;
	private int[] index;
	public MergeSort(double[] array)
	{
		int l=array.length;
		this.array=new double[l];
		this.index=new int[l];
		for(int i=0;i<l;i++)
		{
			this.array[i]=array[i];
			this.index[i]=i;
		}
	}
	public double[] getArray()
	{
		return this.array;
	}
	public int[] getIndex()
	{
		return this.index;
	}
	private void merge(int low,int mid,int high)
	{
		int i=low,j=mid+1,k=0;
		double[] newArray=new double[high-low+1];
		int[] newIndex=new int[high-low+1];
		while(i<=mid&&j<=high)
		{
			if(array[i]<=array[j])
			{

				newArray[k]=array[i];
				newIndex[k++]=index[i++];
			}
			else
			{
				newArray[k]=array[j];
				newIndex[k++]=index[j++];
			}
		}
		while(i<=mid)
		{
			newArray[k]=array[i];
			newIndex[k++]=index[i++];
		}
		while(j<=high)
		{
			newArray[k]=array[j];
			newIndex[k++]=index[j++];
		}
		for(i=low,k=0;i<=high;i++,k++)
		{
			this.array[i]=newArray[k];
			this.index[i]=newIndex[k];
		}
	}
	public void mergeSort(int low,int high)
	{
		if(low<high)
		{
			int mid=(low+high)/2;
			this.mergeSort(low,mid);
			this.mergeSort(mid+1,high);
			this.merge(low,mid,high);
		}
	}
	public void display()
	{
		for(int i=0;i<array.length;i++)System.out.println("array["+index[i]+"]="+array[i]);
	}
}
class Vector
{
	public double x;
	public double y;
	public double z;
	public Vector()
	{
		this.x=0;
		this.y=0;
		this.z=0;
	}
	public Vector(double x,double y,double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	private double Abs(double x)
	{
		return (x<0?-x:x);
	}
	public void cross(Vector v0,Vector v1)
	{
		this.x=v0.y*v1.z-v1.y*v0.z;
		this.y=v0.z*v1.x-v1.z*v0.x;
		this.z=v0.x*v1.y-v1.x*v0.y;
	}
	public Vector oppositeVector()
	{
		return new Vector(-x,-y,-z);
	}
	public Vector reflectedVector(Vector normalVector)
	{
		Vector incidence=new Vector(x,y,z);
		Vector normal=new Vector(normalVector.x,normalVector.y,normalVector.z);
		double rotY=Math.atan(normal.z/normal.x);
		incidence.transform(0,rotY,0);
		normal.transform(0,rotY,0);
		double rotZ=Math.atan(normal.x/normal.y);
		incidence.transform(0,0,rotZ);
		normal.transform(0,0,rotZ);
		incidence.x=-incidence.x;
		incidence.z=-incidence.z;
		incidence.transform(0,0,-rotZ);
		incidence.transform(0,-rotY,0);
		incidence=incidence.oppositeVector();
		return incidence;
	}
	public double product(Vector v0,Vector v1)
	{
		return (v0.x*v1.x+v0.y*v1.y+v0.z*v1.z);
	}
	public double cos(Vector v0,Vector v1)
	{
		return (v0.x*v1.x+v0.y*v1.y+v0.z*v1.z)/(Math.sqrt(v0.x*v0.x+v0.y*v0.y+v0.z*v0.z)*Math.sqrt(v1.x*v1.x+v1.y*v1.y+v1.z*v1.z));
	}
	public void transform(double rotX,double rotY,double rotZ)
	{
		double X,Y,Z;
		Y=y*Math.cos(rotX)-z*Math.sin(rotX);
		Z=y*Math.sin(rotX)+z*Math.cos(rotX);
		y=Y;z=Z;
		Z=z*Math.cos(rotY)-x*Math.sin(rotY);
		X=z*Math.sin(rotY)+x*Math.cos(rotY);
		z=Z;x=X;
		X=x*Math.cos(rotZ)-y*Math.sin(rotZ);
		Y=x*Math.sin(rotZ)+y*Math.cos(rotZ);
		x=X;y=Y;
	}
}