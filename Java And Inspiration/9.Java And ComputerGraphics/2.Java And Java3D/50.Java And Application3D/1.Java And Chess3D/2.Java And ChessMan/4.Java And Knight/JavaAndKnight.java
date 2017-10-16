import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
public class JavaAndKnight
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		TransformGroup1.addChild(new Knight());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
	}
}
class Knight extends TransformGroup
{
	public Knight()
	{
		float unit=0.05f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		Point3d[] points=new Point3d[]
		{
                                        new Point3d(0.0,0.5050000000000001,0),
                                        new Point3d(0.3800000000000001,0.5050000000000001,0),
                                        new Point3d(0.40000000000000013,0.45500000000000007,0),
                                        new Point3d(0.40000000000000013,0.4249999999999998,0),
                                        new Point3d(0.3900000000000001,0.41999999999999993,0),
                                        new Point3d(0.38500000000000023,0.375,0),
                                        new Point3d(0.3700000000000001,0.3599999999999999,0),
                                        new Point3d(0.33000000000000007,0.3250000000000002,0),
                                        new Point3d(0.32000000000000006,0.29000000000000004,0),
                                        new Point3d(0.3750000000000002,0.2450000000000001,0),
                                        new Point3d(0.5450000000000002,0.20500000000000007,0),
                                        new Point3d(0.56,0.18999999999999995,0),
                                        new Point3d(0.5750000000000002,0.18000000000000016,0),
                                        new Point3d(0.5900000000000001,0.14500000000000002,0),
                                        new Point3d(0.5950000000000002,0.08999999999999986,0),
                                        new Point3d(0.5900000000000001,0.08499999999999996,0),
                                        new Point3d(0.5950000000000002,0.08000000000000007,0),
                                        new Point3d(0.5900000000000001,0.04499999999999993,0),
                                        new Point3d(0.53,0.004999999999999893,0),
                                        new Point3d(0.5350000000000001,0.0,0),
                                        new Point3d(0.0,0.0,0)
		};
		Vector3d axis=new Vector3d(0,-1,0);
		double w0=0,w1=2*Math.PI,k=4.3,k1=3.8;
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k*unit,k*unit,k*unit));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new RotateSurface3D(points,axis,w0,w1,Appearance1));
		this.addChild(TransformGroup1);



		Point3d[] ctrlPoints=
		{
			new Point3d(0.0,0.0,0.0),
			new Point3d(0.0,0.25,0.0),
			new Point3d(0.15,0.6,0.0),
			new Point3d(0.3,0.8,0.0),
			new Point3d(0.6,1.2,0.0),
			new Point3d(0.2,1.5,0.0),
			new Point3d(-0.2,1.0,0.0)
		};
		double[] weights=
		{
			1,
			1,
			1,
			1,
			1,
			1.5,
			1
		};
		int order=3;double[] knots=NurbsCurve3D.getBezierUniformNurbsKnots(ctrlPoints.length,order);
		NurbsCurve3D nurbsCurve3D=new NurbsCurve3D(ctrlPoints,weights,knots,order);
		points=nurbsCurve3D.getCoordinates();
		ctrlPoints=new Point3d[]
		{
			new Point3d(0.0,0.5,0.0),
			new Point3d(0.5,0.4,0.0),
			new Point3d(1.2,0.25,0.0),
			new Point3d(1.4,0.15,0.0),
			new Point3d(1.6,0.2,0.0),
			new Point3d(2.0,0.1,0.0)
		};
		weights=new double[]
		{
			1,
			1,
			1,
			1,
			6,
			1
		};
		knots=NurbsCurve3D.getBezierUniformNurbsKnots(ctrlPoints.length,order);
		nurbsCurve3D=new NurbsCurve3D(ctrlPoints,weights,knots,order);
		double[] sizes=nurbsCurve3D.getCoordinatesY(1,0);

		SharedGroup SharedGroup1=new SharedGroup();
		SharedGroup1.addChild(new Pipeline3D(points,sizes,Appearance1));

		transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k1*unit*1.2,k1*unit,k1*unit/1.2));
		transform3D.setTranslation(new Vector3d(0,k1*unit/1.8,0));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Link(SharedGroup1));
		this.addChild(TransformGroup2);

		transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(k1*unit*1.4,k1*unit,unit/1.5));
		transform3D.setTranslation(new Vector3d(unit/1.25,k1*unit/1.45,0));
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		TransformGroup3.addChild(new Link(SharedGroup1));
		this.addChild(TransformGroup3);

		SharedGroup SharedGroup2=new SharedGroup();
		SharedGroup2.addChild(new Sphere(unit/4,Appearance1));
		
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0.5*unit,7.2*unit,0.5*unit));
		TransformGroup TransformGroup4=new TransformGroup(transform3D);
		TransformGroup4.addChild(new Link(SharedGroup2));
		this.addChild(TransformGroup4);

		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0.5*unit,7.2*unit,-0.5*unit));
		TransformGroup TransformGroup5=new TransformGroup(transform3D);
		TransformGroup5.addChild(new Link(SharedGroup2));
		this.addChild(TransformGroup5);

		transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(1.5,2.5,0.25));
		transform3D.setRotation(new AxisAngle4d(1,0,1,-Math.PI/4));
		transform3D.setTranslation(new Vector3d(1.8*unit,8*unit,-0.8*unit));
		TransformGroup TransformGroup6=new TransformGroup(transform3D);
		TransformGroup6.addChild(new Link(SharedGroup2));
		this.addChild(TransformGroup6);

		transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(1.5,2.5,0.25));
		transform3D.setRotation(new AxisAngle4d(-1,0,1,-Math.PI/4));
		transform3D.setTranslation(new Vector3d(1.8*unit,8*unit,0.8*unit));
		TransformGroup TransformGroup7=new TransformGroup(transform3D);
		TransformGroup7.addChild(new Link(SharedGroup2));
		this.addChild(TransformGroup7);
	}
}
class RotateSurface3D extends Shape3D
{
	public double angleToY(double x,double y)
	{
		double r=Math.sqrt(x*x+y*y);
		return r==0?0:y>=0?Math.asin(x/r):Math.PI-Math.asin(x/r);
	}
	public void rotateX(Vector3d v,double a)
	{
		double y=v.y;
		double z=v.z;
		v.y=y*Math.cos(a)-z*Math.sin(a);
		v.z=y*Math.sin(a)+z*Math.cos(a);
	}
	public void rotateY(Vector3d v,double a)
	{
		double z=v.z;
		double x=v.x;
		v.z=z*Math.cos(a)-x*Math.sin(a);
		v.x=z*Math.sin(a)+x*Math.cos(a);
	}
	public void rotateZ(Vector3d v,double a)
	{
		double x=v.x;
		double y=v.y;
		v.x=x*Math.cos(a)-y*Math.sin(a);
		v.y=x*Math.sin(a)+y*Math.cos(a);
	}
	public void rotate(Vector3d vector,Vector3d axis,double angle)
	{
		if(angle==0)return;
		Vector3d v=new Vector3d(vector.x,vector.y,vector.z);
		Vector3d a=new Vector3d(axis.x,axis.y,axis.z);
		double rot_Z=angleToY(a.x,a.y);
		rotateZ(a,rot_Z);
		double rot_X=-angleToY(a.z,a.y);
		rotateZ(v,rot_Z);
		rotateX(v,rot_X);
		rotateY(v,angle);
		rotateX(v,-rot_X);
		rotateZ(v,-rot_Z);
		vector.x=v.x;
		vector.y=v.y;
		vector.z=v.z;
	}
	public RotateSurface3D(Point3d[] curvePoints,Vector3d axis,double angle0,double angle1,Appearance appearance)
	{
		int n=curvePoints.length,m=50,v=0;
		double da=(angle1-angle0)/(m-1);
		Vector3d[] P=new Vector3d[n];
		for(int j=0;j<n;j++)
		{
			P[j]=new Vector3d(curvePoints[j].x,curvePoints[j].y,curvePoints[j].z);
			rotate(P[j],axis,angle0);
		}
		Point3d[] coordinates=new Point3d[m*n];
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinates[i*n+j]=new Point3d(P[j].x,P[j].y,P[j].z);
				rotate(P[j],axis,da);
			}
		}
		int[] coordinateIndices=new int[(m-1)*n*2];v=0;
		for(int i=1;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		int[] stripCounts=new int[m-1];
		for(int i=0;i<m-1;i++)stripCounts[i]=2*n;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());	
	}
}

class Pipeline3D extends Shape3D
{
	public Vector3d D(Point3d p1,Point3d p2)
	{
		return new Vector3d(p2.x-p1.x,p2.y-p1.y,p2.z-p1.z);
	}
	public Vector3d MID(Vector3d v1,Vector3d v2)
	{
		return new Vector3d((v2.x+v1.x)/2,(v2.y+v1.y)/2,(v2.z+v1.z)/2);
	}
	public double MUL(Vector3d v1,Vector3d v2)
	{
		return v1.x*v2.x+v1.y*v2.y+v1.z*v2.z;
	}
	public double angleToY(double x,double y)
	{
		double r=Math.sqrt(x*x+y*y);
		return r==0?0:y>=0?Math.asin(x/r):Math.PI-Math.asin(x/r);
	}
	double lengthOfVector(Vector3d v)
	{
		return Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
	}
	public Vector3d crossVector(Vector3d v0,Vector3d v1)
	{
		double x=v0.y*v1.z-v1.y*v0.z;
		double y=v0.z*v1.x-v1.z*v0.x;
		double z=v0.x*v1.y-v1.x*v0.y;
		return new Vector3d(x,y,z);
	}
	public double angleToVector(Vector3d v0,Vector3d v1)
	{
		double l0=lengthOfVector(v0);
		double l1=lengthOfVector(v1);
		return Math.acos((v0.x*v1.x+v0.y*v1.y+v0.z*v1.z)/(l0*l1));
	}
	public void rotateX(Vector3d v,double a)
	{
		double y=v.y;
		double z=v.z;
		v.y=y*Math.cos(a)-z*Math.sin(a);
		v.z=y*Math.sin(a)+z*Math.cos(a);
	}
	public void rotateY(Vector3d v,double a)
	{
		double z=v.z;
		double x=v.x;
		v.z=z*Math.cos(a)-x*Math.sin(a);
		v.x=z*Math.sin(a)+x*Math.cos(a);
	}
	public void rotateZ(Vector3d v,double a)
	{
		double x=v.x;
		double y=v.y;
		v.x=x*Math.cos(a)-y*Math.sin(a);
		v.y=x*Math.sin(a)+y*Math.cos(a);
	}
	public void rotate1(Vector3d vector,Vector3d axis,double angle)
	{
		if(angle==0)return;
		Vector3d v=new Vector3d(vector.x,vector.y,vector.z);
		Vector3d a=new Vector3d(axis.x,axis.y,axis.z);
		double rot_Z=angleToY(a.x,a.y);
		rotateZ(a,rot_Z);
		double rot_X=-angleToY(a.z,a.y);
		rotateZ(v,rot_Z);
		rotateX(v,rot_X);
		rotateY(v,angle);
		rotateX(v,-rot_X);
		rotateZ(v,-rot_Z);
		vector.x=v.x;
		vector.y=v.y;
		vector.z=v.z;
	}
	public void rotate(Vector3d vector,Vector3d axis,double angle)
	{
		if(angle==0)return;
		Vector3d v=new Vector3d(vector.x,vector.y,vector.z);
		double l=lengthOfVector(axis);
		Vector3d u=new Vector3d(axis.x/l,axis.y/l,axis.z/l);
		double uv=MUL(u,v);
		Vector3d n=crossVector(u,v);
		double cosA=Math.cos(angle);
		double sinA=Math.sin(angle);
		vector.x=cosA*v.x+(1-cosA)*uv*u.x+sinA*n.x;
		vector.y=cosA*v.y+(1-cosA)*uv*u.y+sinA*n.y;
		vector.z=cosA*v.z+(1-cosA)*uv*u.z+sinA*n.z;
	}
	public Pipeline3D(Point3d[] points,double[] sizes,Appearance appearance)
	{
		int m=points.length,n=50,v=0;
		Point3d[] coordinates=new Point3d[(m+2)*n];
		int[] coordinateIndices=new int[(m+1)*n*2];
		double angle=2*Math.PI/(n-1);
		Vector3d dF0=new Vector3d(0,1,0),axis=dF0,dT=dF0;
		Vector3d[] d=new Vector3d[n];
		for(int j=0;j<n;j++)
		{
			double x=Math.cos(angle*j);
			double y=0;
			double z=Math.sin(angle*j);
			d[j]=new Vector3d(x,y,z);
		}
		for(int j=0;j<n;j++)coordinates[v++]=points[0];
		for(int i=0;i<m;i++)
		{
			Point3d P=points[i];
			double R=sizes[i];
			if(i==m-1)angle=0;
			else
			{
				Vector3d dF1=D(points[i],points[i+1]);
				Vector3d dF=i==0?dF1:MID(dF0,dF1);
				angle=angleToVector(dT,dF);
				axis=crossVector(dT,dF);
				dF0=dF1;
				dT=dF;	
			}
			for(int j=0;j<n;j++)
			{
				rotate(d[j],axis,angle);
				coordinates[v++]=new Point3d(P.x+R*d[j].x,P.y+R*d[j].y,P.z+R*d[j].z);
			}
		}
		for(int j=0;j<n;j++)coordinates[v++]=points[m-1];
		v=0;
		for(int i=1;i<m+2;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=(i-1)*n+j;
				coordinateIndices[v++]=i*n+j;
			}
		}
		int[] stripCounts=new int[m+1];
		for(int i=0;i<m+1;i++)stripCounts[i]=2*n;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());	
	}
}

class NurbsCurve3D extends Shape3D
{
	Point3d[] coordinates;
	public NurbsCurve3D(Point3d[] ctrlPoints,double[] weights,double[] knots,int order)
	{
		int l=100,m=order,n=ctrlPoints.length;
		double du=1.0/(l-1);
		double[][] B=this.getParameterMatrix(knots,l,m,n);
		this.coordinates=new Point3d[l];
		for(int i=0;i<l;i++)coordinates[i]=this.getCoordinate(ctrlPoints,weights,B,i);
		int[] stripCounts=new int[]{l};
		LineStripArray LineStripArray1=new LineStripArray(l,LineStripArray.COORDINATES,stripCounts);
		LineStripArray1.setCoordinates(0,coordinates);
		this.setGeometry(LineStripArray1);
	}
	private Point3d getCoordinate(Point3d[] ctrlPoints,double[] weights,double[][] B,int k)
	{
		int n=ctrlPoints.length;
		double x=0,y=0,z=0,w=0;
		for(int i=0;i<n;i++)
		{
			x+=ctrlPoints[i].x*B[k][i]*weights[i];
			y+=ctrlPoints[i].y*B[k][i]*weights[i];
			z+=ctrlPoints[i].z*B[k][i]*weights[i];
			w+=B[k][i]*weights[i];
		}
		return new Point3d(x/w,y/w,z/w);
	}
	public Point3d[] getCoordinates()
	{
		return this.coordinates;
	}
	public double[] getCoordinatesY(double k, double b)
	{
		int l=this.coordinates.length;
		double[] coordinatesY=new double[l];
		for(int i=0;i<l;i++)coordinatesY[i]=k*this.coordinates[i].y+b;
		return coordinatesY;
	}
	private double[][] getParameterMatrix(double[] knots,int curvePointsLength,int order,int ctrlPointsLength)
	{
		double[] u=knots;
		int l=curvePointsLength;
		int m=order,n=ctrlPointsLength;
		double d=(u[n]-u[m])/(l-1);
		double[][] b=new double[l][n];
		double[][] B=new double[m+1][n+1];
		for(int k=0;k<l;k++)
		{
			double t=u[m]+k*d;
			for(int j=0;j<=n;j++)B[0][j]=isBetween(t,u[j],u[j+1])?1:0;
			for(int i=1;i<=m;i++)
			{
				for(int j=0;j<n;j++)
				{
					double du0=u[j+0+i]-u[j+0];
					double du1=u[j+1+i]-u[j+1];
					double U0=du0==0?0:(t-u[j])/du0;
					double U1=du1==0?0:(u[j+1+i]-t)/du1;
					B[i][j]=B[i-1][j]*U0+B[i-1][j+1]*U1;
				}
			}
			for(int j=0;j<n;j++)b[k][j]=B[m][j];
		}
		return b;	
	}
	public static double[] getStandardUniformNurbsKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		double du=1.0/(l-1);
		for(int i=0;i<l;i++)knots[i]=i*du;
		return knots;
	}
	public static double[] getBezierUniformNurbsKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		int c=0,k=l-2*order;
		double du=1.0/(k-1);
		for(int i=0;i<order;i++)knots[c++]=0;
		for(int i=0;i<k;i++)knots[c++]=i*du;
		for(int i=0;i<order;i++)knots[c++]=1;
		return knots;
	}
	public static double[] getSemiUniformNurbsKnots(int ctrlPointsLength,int order)
	{
		int l=ctrlPointsLength+order+1;
		double[] knots=new double[l];
		int c=0,k=l-2*(order+1);
		for(int i=0;i<=order;i++)knots[c++]=0;
		for(int i=0;i<k;i++)knots[c++]=0.5;
		for(int i=0;i<=order;i++)knots[c++]=1;
		return knots;
	}
	public  static double distance(Point3d point0,Point3d point1)
	{
		double dx=point1.x-point0.x;
		double dy=point1.y-point0.y;
		double dz=point1.z-point0.z;
		return Math.sqrt(dx*dx+dy*dy+dz*dz);
	}
	public  static double[] getRiesenfeldNurbsKnots(Point3d[] ctrlPoints,int order)
	{
		int l=ctrlPoints.length+order+1;
		double[] knots=new double[l];
		int c=0,k=l-2*order;
		double[] u=new double[k];u[0]=0;
		for(int i=1;i<k;i++)u[i]=u[i-1]+distance(ctrlPoints[i-1],ctrlPoints[i]);
		for(int i=0;i<order;i++)knots[c++]=0;
		for(int i=0;i<k;i++)knots[c++]=u[i]/u[k-1];
		for(int i=0;i<order;i++)knots[c++]=1;
		return knots;
	}
	boolean isBetween(double x,double x0,double x1)
	{
		return x==0?x0==0:x0<x&&x<=x1;
	}
}
