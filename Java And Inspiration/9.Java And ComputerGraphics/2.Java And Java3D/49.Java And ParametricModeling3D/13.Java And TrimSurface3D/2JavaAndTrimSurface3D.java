import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndTrimSurface3D
{
	public static void main(String[] args)
	{
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
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom();
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		MouseTranslate MouseTranslate1=new MouseTranslate();
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		int length=80;
		double r=0.5,x0=-1.5*r,x1=1.5*r,dx=(x1-x0)/(length-1),z0=-r,z1=r;
		Point3d[][] surfaceCoordinates=new Point3d[2][length];
		for(int j=0;j<length;j++)
		{
			double x=x0+j*dx,y=0.1*Math.sin(10*x);
			surfaceCoordinates[0][j]=new Point3d(x,y,z0);
			surfaceCoordinates[1][j]=new Point3d(x,y,z1);
		};
		double[] coordinatesUV=new double[]
		{
			0.1,0.4,
			0.4,0.2,
			0.8,0.5,
			0.6,0.6,
			0.7,0.7,
			0.5,0.9,
			0.2,0.8,
			0.35,0.7
		};
/*
		coordinatesUV=new double[]
		{
			0.2,0.7,
			0.8,0.5,
			0.8,0.9,
			0.2,0.7
		};
*/
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,1f));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new TrimSurface3D(coordinatesUV,new Function_Cos(),-1.5,1.5,-1,1,Appearance1));
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Function_Cos implements Function
{
	public Point3d surface(double x,double z)
	{
		double D=Math.sqrt(x*x+z*z);
		double A=0.04,W=10;
		double y=A*Math.cos(W*D);
		return new Point3d(x,y,z);
	}
}
interface Function
{
	Point3d surface(double u,double v);
}
class TrimSurface3D extends Shape3D
{
	private int m=100,n=100;
	private double dU,dV;
	private Point3d[][] surfaceCoordinates;
	private EdgeListTable edgeListTable;
	private EdgeList activeEdgeList;
	private CoordinateList coordinateList;
	public TrimSurface3D(double[] coordinatesUV,Function function,double u0,double u1,double v0,double v1,Appearance appearance)
	{
		this.getSurfaceCoordinates(function,u0,u1,v0,v1);
		this.dU=1.0/(m-1);
		this.dV=1.0/(n-1);
		this.initializeedgeListTable(coordinatesUV);
this.display();
		Point3d[] coordinates=this.getCoordinates();
		QuadArray QuadArray1=new QuadArray(coordinates.length,QuadArray.COORDINATES|QuadArray.NORMALS);
		QuadArray1.setCoordinates(0,coordinates);
		GeometryInfo GeometryInfo1=new GeometryInfo(QuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
	private void getSurfaceCoordinates(Function function,double u0,double u1,double v0,double v1)
	{
		this.surfaceCoordinates=new Point3d[m][n];
		double du=(u1-u0)/(n-1),dv=(v1-v0)/(m-1);
		for(int i=0;i<m;i++)
		{
			double v=v0+i*dv;
			for(int j=0;j<n;j++)
			{
				double u=u0+j*du;
				this.surfaceCoordinates[i][j]=function.surface(u,v);
			}
		}
	}
	private void initializeedgeListTable(double[] coordinates)
	{
		this.activeEdgeList=new EdgeList(0.0);
		this.edgeListTable=new EdgeListTable();
		int length=coordinates.length,l=length/2,L=l;
		for(int i=0;i<L;i++)
		{
			int i0=(i-1+l)%l;
			int i1=(i+1)%l;
			double u0=coordinates[i0*2+0];
			double v0=coordinates[i0*2+1];
			double u=coordinates[i*2+0];
			double v=coordinates[i*2+1];
			double u1=coordinates[i1*2+0];
			double v1=coordinates[i1*2+1];
			while(u0==u)
			{
System.out.println("u0==u");
				i=(i-1+l)%l;
				L--;
				i0=(i-1+l)%l;
				i1=(i+1)%l;
				u0=coordinates[i0*2+0];
				v0=coordinates[i0*2+1];
				u=coordinates[i*2+0];
				v=coordinates[i*2+1];
				u1=coordinates[i1*2+0];
				v1=coordinates[i1*2+1];
			}
			if(u1==u)
			{
System.out.println("u1==u");
				this.addEdgeNode(u0,v0,u,v,-1,-1);
				while(u1==u)
				{
					i=(i+1)%l;
					i1=(i+1)%l;
					u=coordinates[i*2+0];
					v=coordinates[i*2+1];
					u1=coordinates[i1*2+0];
					v1=coordinates[i1*2+1];
				}
				this.addEdgeNode(-1,-1,u,v,u1,v1);
			}
			else this.addEdgeNode(u0,v0,u,v,u1,v1);
		}
	}
	private void addEdgeNode(double u0,double v0,double u,double v,double u1,double v1)
	{
		double dv0=(v0-v)/(u0-u);
		double dv1=(v1-v)/(u1-u);
		if(u0>u&&u1>u)
		{
			this.edgeListTable.insert(u,new EdgeNode(v,dv0,u0));
			this.edgeListTable.insert(u,new EdgeNode(v,dv1,u1));
		}
		else if(u0<u&&u1<u);
		else
		{
			if(u1<u0)this.edgeListTable.insert(u,new EdgeNode(v,dv0,u0));
			else this.edgeListTable.insert(u,new EdgeNode(v,dv1,u1));
		}
	}
	public void display()
	{
		this.edgeListTable.display();
	}
	public Point3d[] getCoordinates()
	{
		this.coordinateList=new CoordinateList();
		this.activeEdgeList=new EdgeList(0.0);
		while(edgeListTable.isNotEmpty())
		{
			EdgeList tempEdgeList=edgeListTable.first();
			EdgeList midEdgeList=null;
			double u0=tempEdgeList.u;
			this.activeEdgeList.u=u0;
			while(tempEdgeList.isNotEmpty())activeEdgeList.insert(tempEdgeList.first());
			if(activeEdgeList.isNotEmpty())
			{
activeEdgeList.display();
				EdgeList newEdgeListTable=new EdgeList(0.0);
				while(activeEdgeList.isNotEmpty())
				{
					EdgeNode n0=activeEdgeList.first();
					EdgeNode n1=activeEdgeList.first();
					double v00=n0.v,v01=n1.v,v10=v00,v11=v01;
					double u1=edgeListTable.u();
					double maxU0=n0.maxU,maxU1=n1.maxU;
					if(u1<=maxU0&&u1<=maxU1)
					{
						v10=v00+(u1-u0)*n0.dv;
						v11=v01+(u1-u0)*n1.dv;
						n0.v=v10;n1.v=v11;
						if(u1<maxU0)newEdgeListTable.insert(n0);
						if(u1<maxU1)newEdgeListTable.insert(n1);
					}
					else if(u1>maxU0&&maxU0==maxU1)
					{
						u1=maxU0;
						v10=v00+(u1-u0)*n0.dv;
						v11=v01+(u1-u0)*n1.dv;
						n0.v=v10;n1.v=v11;
					}
					else if(u1>maxU1)
					{
						u1=maxU1;
						v10=v00+(u1-u0)*n0.dv;
						v11=v01+(u1-u0)*n1.dv;
						n0.v=v10;
						if(midEdgeList==null)midEdgeList=new EdgeList(u1);
						midEdgeList.insert(n0);
					}
					else if(u1>maxU0)
					{
						u1=maxU0;
						v10=v00+(u1-u0)*n0.dv;
						v11=v01+(u1-u0)*n1.dv;
						n1.v=v11;
						if(midEdgeList==null)midEdgeList=new EdgeList(u1);
						midEdgeList.insert(n1);
					}
//					this.subDivideQuad(u0,v00,v01,u1,v10,v11);
					this.divideQuad(u0,u1,v00,v01,n0.dv,n1.dv);
				}
				this.activeEdgeList=newEdgeListTable;
			}
			if(midEdgeList!=null)this.edgeListTable.insert(midEdgeList);
		}
		return this.coordinateList.toCoordinates();
	}
	private void subDivideQuad1(double u0,double v00,double v01,double u1,double v10,double v11)
	{
System.out.println("g.drawLine("+u0+","+v00+","+u0+","+v01+")");
System.out.println("g.drawLine("+u1+","+v10+","+u1+","+v11+")");
		Point3d p00=this.getCoordinate(u0,v00);
		Point3d p01=this.getCoordinate(u0,v01);
		Point3d p11=this.getCoordinate(u1,v11);
		Point3d p10=this.getCoordinate(u1,v10);
		this.coordinateList.addCoordinate(p00);
		this.coordinateList.addCoordinate(p10);
		this.coordinateList.addCoordinate(p11);
		this.coordinateList.addCoordinate(p01);
	}
	private void divideQuad(double u0,double u1,double v0,double v1,double dv0,double dv1)
	{
		int i0=coordinateU(u0);
		int i1=coordinateU(u1);
		double t0=u0,t1=u1;
		for(int i=i0+1;i<=i1;i++)
		{
			t1=i*dU;
			this.subdivideQuad(t0,t1,v0,v1,dv0,dv1);
			v0+=(t1-t0)*dv0;
			v1+=(t1-t0)*dv1;
			t0=t1;
		}
		t1=u1;
		this.subdivideQuad(t0,t1,v0,v1,dv0,dv1);
//		this.subdivideQuad(u0,u1,v0,v1,dv0,dv1);
	}
	private void subdivideQuad(double u0,double u1,double v0,double v1,double dv0,double dv1)
	{
		double du=u1-u0;

System.out.println("g.drawLine("+u0+","+v0+","+u0+","+v1+")");
System.out.println("g.drawLine("+u1+","+(v0+dv0*du)+","+u1+","+(v1+dv1*du)+")");

		double dv=dv0*du;
		int i=coordinateU(u0);
		int j0=coordinateV(v0),j=j0;
		int j1=coordinateV(v0+dv);
System.out.println(j0+","+j1);
		double w0=v0,w1=v0;
		if(equals(dv,0))j++;
		else if(dv>0)
		{
			double u10=u0,u11=u0;
			for(j=j0+1;j<=j1;j++)
			{
				w1=j*dV;
				u11=u0+du*(w1-v0)/dv;
				this.addCoordinate(u0,w0,u0,w1,u10,w0,u11,w1);
				w0=w1;
				u10=u11;
			}
			w1=v0+dv;
			u11=u1;
			this.addCoordinate(u0,w0,u0,w1,u10,w0,u11,w1);
		}
		else if(dv<0)
		{
			dv*=-1;
			w0=v0-dv;
			double u00=u1,u01=u1;
			for(j=j1+1;j<=j0;j++)
			{
				w1=j*dV;
				u01=u1-du*(w1-(v0-dv))/dv;
				this.addCoordinate(u00,w0,u01,w1,u1,w0,u1,w1);
				w0=w1;
				u00=u01;
			}
			w1=v0;
			u01=u0;
			this.addCoordinate(u00,w0,u01,w1,u1,w0,u1,w1);
		}
		w0=w1;
		dv=dv1*du;
		j0=coordinateV(v1);
		j1=coordinateV(v1+dv);
		int j2=dv>=0?j0:j1;
		for(;j<=j2;j++)
		{
			w1=j*dV;
			this.addCoordinate(u0,w0,u0,w1,u1,w0,u1,w1);
			w0=w1;
		}
		w1=dv>=0?v1:v1+dv;
		this.addCoordinate(u0,w0,u0,w1,u1,w0,u1,w1);
		w0=w1;
		if(equals(dv,0));
		else if(dv>0)
		{
			double u00=u0,u01=u0;
			for(;j<=j1;j++)
			{
				w1=j*dV;
				u01=u1-du*(v1+dv-w1)/dv;
				this.addCoordinate(u00,w0,u01,w1,u1,w0,u1,w1);
				w0=w1;
				u00=u01;
			}
			w1=v1+dv;
			u01=u1;
			this.addCoordinate(u00,w0,u01,w1,u1,w0,u1,w1);
		}
		else if(dv<0)
		{
			dv*=-1;
			double u10=u1,u11=u1;
			for(;j<=j0;j++)
			{
				w1=j*dV;
				u11=u0+du*(v1-w1)/dv;
				this.addCoordinate(u0,w0,u0,w1,u10,w0,u11,w1);
				w0=w1;
				u10=u11;
			}
			w1=v1;
			u11=u0;
			this.addCoordinate(u0,w0,u0,w1,u10,w0,u11,w1);
		}
	}
	private void addCoordinate(double u00,double v00,double u01,double v01,double u10,double v10,double u11,double v11)
	{
//System.out.println("("+u00+","+v00+")-("+u01+","+v01+")-("+u11+","+v11+")-("+u10+","+v10+")");
		Point3d p00=this.getCoordinate(u00,v00);
		Point3d p01=this.getCoordinate(u01,v01);
		Point3d p11=this.getCoordinate(u11,v11);
		Point3d p10=this.getCoordinate(u10,v10);
		this.coordinateList.addCoordinate(p00);
		this.coordinateList.addCoordinate(p10);
		this.coordinateList.addCoordinate(p11);
		this.coordinateList.addCoordinate(p01);
	}
	private Point3d getCoordinate(double u,double v)
	{
		int i=coordinateU(u);
		int j=coordinateV(v);
		u-=i*dU;u/=dU;
		v-=j*dV;v/=dV;
		Point3d p00=surfaceCoordinates[i+0][j+0];
		Point3d p01=surfaceCoordinates[i+0][j+1];
		Point3d p10=surfaceCoordinates[i+1][j+0];
		Point3d p11=surfaceCoordinates[i+1][j+1];
		Point3d pU0=midPoint(p00,p10,u);
		Point3d pU1=midPoint(p01,p11,u);
		return midPoint(pU0,pU1,v);
	}
	private Point3d midPoint(Point3d p0,Point3d p1,double u)
	{
		if(u==0.0)return p0;
		else if(u==1.0)return p1;
		double x=(1-u)*p0.x+u*p1.x;
		double y=(1-u)*p0.y+u*p1.y;
		double z=(1-u)*p0.z+u*p1.z;
		return new Point3d(x,y,z);
	}
	private int coordinateU(double u)
	{
		return (int)((m-1)*u);
	}
	private int coordinateV(double v)
	{
		return (int)((n-1)*v);
	}
	private boolean equals(double d1,double d2)
	{
		if(Math.abs(d1-d2)<1E-3)return true;
		else return false;
	}
}
class EdgeNode
{
	public double v;
	public double dv;
	public double maxU;
	public EdgeNode next;
	public EdgeNode(double v,double dv,double maxU)
	{
		this.v=v;
		this.dv=dv;
		this.maxU=maxU;
		this.next=null;
	}
}
class EdgeList
{
	public double u;
	private int length;
	private EdgeNode first,last;
	public EdgeList next;
	public EdgeList(double u)
	{
		this.u=u;
		this.next=null;
		this.first=null;
		this.last=null;
		this.length=0;
	}
	public void insert(EdgeNode node)
	{
		if(first==null)
		{
			this.first=node;
			this.last=node;
		}
		else if(node.v<first.v||(node.v==first.v&&node.dv<first.dv))
		{
			node.next=first;
			this.first=node;
		}
		else if(node.v>last.v||(node.v==last.v&&node.dv>last.dv))
		{
			this.last.next=node;
			this.last=node;
		}
		else
		{
			EdgeNode m,n;
			for(n=first,m=n;n!=null&&(node.v>n.v||(node.v==n.v&&node.dv>n.dv));m=n,n=n.next);
			node.next=n;
			m.next=node;
		}
		this.length++;
	}
	public EdgeNode first()
	{
		if(first==null)return null;
		EdgeNode node=first;
		this.first=first.next;
		node.next=null;
		this.length--;
		return node;
	}
	public boolean isNotEmpty()
	{
		return (first!=null);
	}
	public void display()
	{
		for(EdgeNode n=first;n!=null;n=n.next)
		{
			System.out.print("v="+n.v+","+"dv="+n.dv+","+"maxU="+n.maxU+"; ");
		}
		System.out.println();
	}
}
class EdgeListTable
{
	private EdgeList first,last;
	public EdgeListTable()
	{
		this.first=null;
		this.last=null;
	}
	public void insert(EdgeList list)
	{
		if(first==null)
		{
			this.first=list;
			this.last=list;
		}
		else if(list.u>=last.u)
		{
			this.last.next=list;
			this.last=list;
		}
		else if(list.u<=first.u)
		{
			list.next=first;
			this.first=list;
		}
		else
		{
			EdgeList m,n;
			for(n=first,m=n;list.u>n.u;m=n,n=n.next);
			list.next=n;
			m.next=list;
		}
	}
	public void insert(double u,EdgeNode node)
	{
		EdgeList l;
		for(l=first;l!=null&&l.u<u;l=l.next);
		if(l==null||l.u!=u)
		{
			EdgeList l1=new EdgeList(u);
			l1.insert(node);
			this.insert(l1);
		}
		else l.insert(node);
	}
	public double u()
	{
		return first==null?1.0:first.u;
	}
	public EdgeList first()
	{
		if(first==null)return null;
		EdgeList list=first;
		this.first=first.next;
		list.next=null;
		return list;
	}
	public boolean isNotEmpty()
	{
		return (first!=null);
	}
	public void display()
	{
		for(EdgeList l=first;l!=null;l=l.next)
		{
			System.out.print("u="+l.u+" : ");
			l.display();
		}
		System.out.println();
	}
}
class CoordinateNode
{
	public Point3d coordinate;
	public CoordinateNode next;
	public CoordinateNode(Point3d coordinate)
	{
		this.coordinate=coordinate;
		this.next=null;
	}
}
class CoordinateList
{
	private CoordinateNode first,last;
	public int length;
	public CoordinateList()
	{
		this.first=null;
		this.last=null;
		this.length=0;
	}
	public void addCoordinate(Point3d coordinate)
	{
		CoordinateNode node=new CoordinateNode(coordinate);
		if(first==null)
		{
			this.first=node;
			this.last=node;
		}
		else 
		{
			this.last.next=node;
			this.last=node;
		}
		this.length++;
	}
	public Point3d[] toCoordinates()
	{
		int i;CoordinateNode n;
		Point3d[] coordinates=new Point3d[length];
		for(i=0,n=first;i<length;i++,n=n.next)coordinates[i]=n.coordinate;
		return coordinates;
	}
}
