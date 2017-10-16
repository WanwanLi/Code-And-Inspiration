import java.awt.*;
import java.awt.event.*;
public class JavaAndIndexedQuadArray
{
	public static void main(String[] args)
	{
		double[] coordinates=
		{
			0,0,0,
			120,-120,250,
			250,0,0,
			120,250,0
		};
		int[] coordinateIndices=
		{
			0,1,2,3
		};
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(coordinates.length,IndexedQuadArray.COORDINATES,coordinateIndices.length);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		IndexedQuadArray1.setTransform3D(Math.PI/6,0,0);
	}

}
class IndexedQuadArray extends Frame implements MouseListener,MouseMotionListener
{
	int dx=600,dy=400,dz=0;
	double r,rotY,rotX;
	final int X=0,Y=1,Z=2;
	public static int COORDINATES=1;
	double[] coordinates;
	int[] coordinateIndices;
	DirectionalLight directionalLight;
	Color diffuseColor;
	public IndexedQuadArray(int coordinatesLength,int mode,int coordinateIndicesLength)
	{
		coordinates=new double[coordinatesLength];
		coordinateIndices=new int[coordinateIndicesLength];
		directionalLight=new DirectionalLight(new Color(255,255,255),new Vector(0,0,-1));
		diffuseColor=new Color(255,0,0);
		this.setBackground(new Color(0,0,0));
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.setVisible(true);
	}
	public void setCoordinates(int index,double[] coordinates)
	{
		for(int i=index;i<coordinates.length&&i<this.coordinates.length;i++)this.coordinates[i]=coordinates[i];
	}
	public void setCoordinateIndices(int index,int[] coordinateIndices)
	{
		for(int i=index;i<coordinateIndices.length&&i<this.coordinateIndices.length;i++)this.coordinateIndices[i]=coordinateIndices[i];
	}
	public void getAppearance(DirectionalLight directionalLight,Color diffuseColor)
	{
		if(directionalLight!=null)this.directionalLight=directionalLight;
		if(diffuseColor!=null)this.diffuseColor=diffuseColor;
	}
	public void setTransform3D(double rotX,double rotY,double rotZ)
	{
		for(int i=0;i<coordinates.length;i+=3)
		{
			double y=coordinates[i+Y];
			double z=coordinates[i+Z];
			coordinates[i+Y]=y*Math.cos(rotX)-z*Math.sin(rotX);
			coordinates[i+Z]=y*Math.sin(rotX)+z*Math.cos(rotX);
		}
		for(int i=0;i<coordinates.length;i+=3)
		{
			double x=coordinates[i+X];
			double z=coordinates[i+Z];
			coordinates[i+X]=x*Math.cos(rotY)+z*Math.sin(rotY);
			coordinates[i+Z]=-x*Math.sin(rotY)+z*Math.cos(rotY);
		}
		for(int i=0;i<coordinates.length;i+=3)
		{
			double x=coordinates[i+X];
			double y=coordinates[i+Y];
			coordinates[i+X]=x*Math.cos(rotZ)-y*Math.sin(rotZ);
			coordinates[i+Y]=x*Math.sin(rotZ)+y*Math.cos(rotZ);
		}
	}
	private Color getColor(Vector triangleNormal)
	{
		if(triangleNormal.z<0)return null;
		double redDiffusionRatio=diffuseColor.getRed()/255;
		double greenDiffusionRatio=diffuseColor.getGreen()/255;
		double blueDiffusionRatio=diffuseColor.getBlue()/255;
		double cosLightDirection_triangleNormal=(new Vector()).cos(directionalLight.direction.oppositeVector(),triangleNormal);
		double red=redDiffusionRatio*directionalLight.color.getRed()*cosLightDirection_triangleNormal;
		double green=greenDiffusionRatio*directionalLight.color.getGreen()*cosLightDirection_triangleNormal;
		double blue=blueDiffusionRatio*directionalLight.color.getBlue()*cosLightDirection_triangleNormal;
		if(red<0||green<0||blue<0){System.out.println("Color==null");return null;}
		return new Color((int)red,(int)green,(int)blue);
	}
	private void fillQuad(Graphics g,int startIndex)
	{
		double[] x=new double[4];
		double[] y=new double[4];
		double[] z=new double[4];
		for(int i=0;i<4;i++)
		{
			int index=coordinateIndices[startIndex+i];
			x[i]=coordinates[index*3+X];
			y[i]=coordinates[index*3+Y];
			z[i]=coordinates[index*3+Z];
		}
		Vector normal012=new Vector();
		normal012.cross(new Vector(x[1]-x[0],y[1]-y[0],z[1]-z[0]),new Vector(x[2]-x[1],y[2]-y[1],z[2]-z[1]));
		Color color012=this.getColor(normal012);
		if(color012!=null)
		{
			g.setColor(color012);
			this.fillTriangle(g,(int)x[0]+dx,-(int)y[0]+dy,(int)x[1]+dx,-(int)y[1]+dy,(int)x[2]+dx,-(int)y[2]+dy);
		}
		Vector normal230=new Vector();
		normal230.cross(new Vector(x[3]-x[2],y[3]-y[2],z[3]-z[2]),new Vector(x[0]-x[3],y[0]-y[3],z[0]-z[3]));
		Color color230=this.getColor(normal230);
		if(color230!=null)
		{
			g.setColor(color230);
			this.fillTriangle(g,(int)x[2]+dx,-(int)y[2]+dy,(int)x[3]+dx,-(int)y[3]+dy,(int)x[0]+dx,-(int)y[0]+dy);
		}
	}
	public void paint(Graphics g)
	{
		for(int i=0;i<coordinateIndices.length;i+=4)fillQuad(g,i);
	}
	public void fillTriangle(Graphics g,int x0,int y0,int x1,int y1,int x2,int y2)
	{
		int[] x=new int[3];
		int[] y=new int[3];
		if(y1>y2)
		{
			int t=x1;x1=x2;x2=t;
			t=y1;y1=y2;y2=t;
		}
		if(y0<=y1)
		{
			x[0]=x0;y[0]=y0;
			x[1]=x1;y[1]=y1;
			x[2]=x2;y[2]=y2;
		}
		else
		{
			x[0]=x1;y[0]=y1;
			if(y0<=y2)
			{
				x[1]=x0;y[1]=y0;
				x[2]=x2;y[2]=y2;
			}
			else
			{
				x[1]=x2;y[1]=y2;
				x[2]=x0;y[2]=y0;
			}
		}
		int dY01=y[1]-y[0];
		int dY12=y[2]-y[1];
		if(dY01+dY12==0){g.drawLine(x[0],y[0],x[2],y[2]);return;}
		int[] line02=this.getLine(x[0],y[0],x[2],y[2]);
		if(dY01==0)g.drawLine(x[0],y[0],x[1],y[1]);
		else
		{
			int[] line01=this.getLine(x[0],y[0],x[1],y[1]);
			for(int dy=0;dy<dY01;dy++)
			{
				int x01=line01[dy];
				int x02=line02[dy];
				int Y=y[0]+dy;
				g.drawLine(x01,Y,x02,Y);
			}
		}
		if(dY12==0)g.drawLine(x[1],y[1],x[2],y[2]);
		else
		{
			int[] line12=this.getLine(x[1],y[1],x[2],y[2]);
			for(int dy=0;dy<dY12;dy++)
			{
				int x12=line12[dy];
				int x02=line02[dY01+dy];
				int Y=y[0]+dY01+dy;
				g.drawLine(x12,Y,x02,Y);
			}
		}
	}
	private int[] getLine(int x0,int y0,int x1,int y1)
	{
		int sX=1;
		int sY=1;
		int dX=x1-x0;
		int dY=y1-y0;
		int[] line=new int[dY+1];
		if(dX<0){dX=-dX;sX=-sX;}
		for(int dy=0;dy<=dY;dy++)line[dy]=x0+sX*DIV(dX*dy,dY);
		return line;
	}
	private static int DIV(int a,int b)
	{
		int q=a/b;
		int m=a%b;
		if(m==0)return q;
		if(m>b-m)q++;
		return q;
	}
	public void drawLine(Graphics g,int x0,int y0,int x1,int y1)
	{
		int sX=1;
		int sY=1;
		int dX=x1-x0;
		int dY=y1-y0;
		if(dX<0){dX=-dX;sX=-sX;}
		if(dY<0){dY=-dY;sY=-sY;}
		for(int dy=0;dy<=dY;dy++)
		{
			int x=x0+sX*DIV(dX*dy,dY);
			int y=y0+sY*dy;
			g.drawLine(x,y,x,y);
		}
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseDragged(MouseEvent e){}
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
	public double cos(Vector v0,Vector v1)
	{
		return (v0.x*v1.x+v0.y*v1.y+v0.z*v1.z)/(Math.sqrt(v0.x*v0.x+v0.y*v0.y+v0.z*v0.z)*Math.sqrt(v1.x*v1.x+v1.y*v1.y+v1.z*v1.z));
	}
}
class DirectionalLight
{
	public Vector direction;
	public Color color;
	public DirectionalLight(Color color,Vector direction)
	{
		this.color=color;
		this.direction=direction;
	}
}