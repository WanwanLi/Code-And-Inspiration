import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.io.*;
public class JavaAndObject3D
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
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,0.8f,0f));
		Appearance1.setMaterial(Material1);

		Point3d[] ctrlPoints=
		{
/*
                                        new Point3d(0.00000000000000044,0.0,0),
                                        new Point3d(0.00000000000000036,0.004999999999999893,0),
                                        new Point3d(0.000000000000003,0.0,0),
                                        new Point3d(0.1349999999999999,0.0,0),
                                        new Point3d(0.13,0.030000000000000027,0),
                                        new Point3d(0.12499999999999989,0.03499999999999992,0),
                                        new Point3d(0.10999999999999999,0.1200000000000001,0),
                                        new Point3d(0.10499999999999987,0.125,0),
                                        new Point3d(0.09999999999999998,0.15999999999999992,0),
                                        new Point3d(0.09500000000000008,0.16500000000000004,0),
                                        new Point3d(0.08999999999999997,0.18999999999999995,0),
                                        new Point3d(0.08500000000000008,0.19500000000000006,0),
                                        new Point3d(0.039999999999999925,0.21999999999999997,0),
                                        new Point3d(0.05499999999999994,0.2649999999999999,0),
                                        new Point3d(0.04999999999999993,0.27,0),
                                        new Point3d(0.03500000000000003,0.30499999999999994,0),
                                        new Point3d(0.020000000000000018,0.48,0),
                                        new Point3d(0.025000000000000022,0.4850000000000001,0),
                                        new Point3d(0.030000000000000027,0.51,0),
                                        new Point3d(0.03500000000000003,0.5150000000000001,0),
                                        new Point3d(0.04999999999999993,0.5800000000000001,0),
                                        new Point3d(0.05499999999999994,0.585,0),
                                        new Point3d(0.05999999999999994,0.6100000000000001,0),
                                        new Point3d(0.06499999999999995,0.615,0),
                                        new Point3d(0.06999999999999995,0.6400000000000001,0),
                                        new Point3d(0.07499999999999996,0.645,0),
                                        new Point3d(0.08999999999999997,0.7,0),
                                        new Point3d(0.09500000000000008,0.7050000000000001,0),
                                        new Point3d(0.09999999999999998,0.7150000000000001,0),
                                        new Point3d(0.16000000000000003,0.73,0),
                                        new Point3d(0.16499999999999992,0.7350000000000001,0),
                                        new Point3d(0.18000000000000005,0.74,0),
                                        new Point3d(0.20499999999999996,0.78,0),
                                        new Point3d(0.21000000000000008,0.7849999999999999,0),
                                        new Point3d(0.23999999999999988,0.7949999999999999,0),
                                        new Point3d(0.23999999999999988,0.8200000000000001,0),
                                        new Point3d(0.245,0.825,0),
                                        new Point3d(0.23999999999999988,0.8599999999999999,0),
                                        new Point3d(0.00000000000000005,0.855,0)


                                        new Point3d(0.0,0.0,0),
                                        new Point3d(0.05500000000000005,0.0950000000000002,0),
                                        new Point3d(0.06000000000000005,0.10000000000000009,0),
                                        new Point3d(0.07000000000000006,0.15000000000000013,0),
                                        new Point3d(0.06500000000000006,0.15500000000000003,0),
                                        new Point3d(0.04500000000000004,0.2400000000000002,0),
                                        new Point3d(0.050000000000000044,0.2450000000000001,0),
                                        new Point3d(0.05500000000000005,0.27500000000000013,0),
                                        new Point3d(0.07000000000000006,0.29000000000000004,0),
                                        new Point3d(0.07500000000000007,0.30000000000000004,0),
                                        new Point3d(0.08499999999999996,0.31000000000000005,0),
                                        new Point3d(0.08000000000000007,0.31500000000000017,0),
                                        new Point3d(0.07500000000000007,0.33000000000000007,0),
                                        new Point3d(0.05500000000000005,0.3400000000000001,0),
                                        new Point3d(0.050000000000000044,0.3450000000000002,0),
                                        new Point3d(0.05500000000000005,0.6150000000000002,0),
                                        new Point3d(0.06000000000000005,0.6200000000000001,0),
                                        new Point3d(0.06500000000000006,0.6500000000000001,0),
                                        new Point3d(0.07000000000000006,0.655,0),
                                        new Point3d(0.06500000000000006,0.6600000000000001,0),
                                        new Point3d(0.07000000000000006,0.665,0),
                                        new Point3d(0.08499999999999996,0.7200000000000002,0),
                                        new Point3d(0.125,0.7650000000000001,0),
                                        new Point3d(0.19999999999999996,0.7950000000000002,0),
                                        new Point3d(0.22999999999999998,0.8450000000000002,0),
                                        new Point3d(0.2350000000000001,0.8800000000000001,0),
                                        new Point3d(0.24,0.885,0),
                                        new Point3d(0.24,0.8950000000000002,0),
                                        new Point3d(0.0,0.8950000000000002,0)
*/

                                        new Point3d(0.000,0.04000000000000133,0),
                                        new Point3d(0.1250000000000001,0.06000000000000005,0),
                                        new Point3d(0.1449999999999999,0.125,0),
                                        new Point3d(0.1449999999999999,0.18500000000000005,0),
                                        new Point3d(0.09499999999999997,0.2250000000000001,0),
                                        new Point3d(0.05500000000000005,0.2450000000000001,0),
                                        new Point3d(0.06500000000000006,0.26,0),
                                        new Point3d(0.07999999999999996,0.26,0),
                                        new Point3d(0.08499999999999996,0.2650000000000001,0),
                                        new Point3d(0.09499999999999997,0.2650000000000001,0),
                                        new Point3d(0.08999999999999997,0.29499999999999993,0),
                                        new Point3d(0.06500000000000006,0.30499999999999994,0),
                                        new Point3d(0.05500000000000005,0.3400000000000001,0),
                                        new Point3d(0.06000000000000005,0.44999999999999996,0),
                                        new Point3d(0.06500000000000006,0.45500000000000007,0),
                                        new Point3d(0.07999999999999996,0.48,0),
                                        new Point3d(0.08499999999999996,0.4850000000000001,0),
                                        new Point3d(0.14,0.51,0),
                                        new Point3d(0.17000000000000004,0.5150000000000001,0),
                                        new Point3d(0.17499999999999993,0.52,0),
                                        new Point3d(0.20000000000000007,0.5350000000000001,0),
                                        new Point3d(0.21000000000000008,0.5700000000000001,0),
                                        new Point3d(0.22000000000000008,0.5900000000000001,0),
                                        new Point3d(0.245,0.615,0),
                                        new Point3d(0.2500000000000001,0.6200000000000001,0),
                                        new Point3d(0.245,0.6300000000000001,0),
                                        new Point3d(0.21000000000000008,0.635,0),
                                        new Point3d(0.20499999999999996,0.6300000000000001,0),
                                        new Point3d(0.0,0.635,0)

		};
	//	int order=2;double[] knots=BSpline3D.getBezierUniformNurbsKnots(ctrlPoints.length,order);
		int order=2;double[] knots=BSpline3D.getRiesenfeldNurbsKnots(ctrlPoints,order);

		BSpline3D bSpline3D=new BSpline3D(ctrlPoints,knots,order,30);
		Point3d[] points=bSpline3D.getCoordinates();
		Vector3d axis=new Vector3d(0,1,0);
		double w0=0,w1=2*Math.PI;
		RotateSurface3D RotateSurface3D=new RotateSurface3D(points,axis,w0,w1,15,Appearance1);
		TransformGroup1.addChild(RotateSurface3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static Point3d F(double t)
	{
		double w=100;
		double x=t*Math.sin(w*t)*(Math.PI/30);
		double y=t*0.01*w;
		double z=0.5*t*Math.cos(t*0.5*w);
		return new Point3d(x,y,z);
	}
	public static double G(double x,double x0,double d)
	{
		double u=(x-x0)/d;
		double k=Math.sqrt(2*Math.PI)*d;
		return Math.exp(-u*u/2)/k;
	}
	public static double G(double x,double y,double u1,double u2,double o1,double o2,double p)
	{
		double u=(x-u1)/o1;
		double v=(y-u2)/o2;
		double w=1-p*p;
		return Math.exp(-(u*u-2*p*u*v+v*v)/(2*w))/(2*Math.PI*o1*o2*Math.sqrt(w));
	}
	public Point3d F1(double t)
	{
		double w=2;
		return new Point3d(t,Math.sin(t*w),Math.cos(t*w));
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
	public RotateSurface3D(Point3d[] curvePoints,Vector3d axis,double angle0,double angle1,int slices,Appearance appearance)
	{
		int n=curvePoints.length,m=slices,v=0;
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
		try
		{
			this.writeGeometryInfo(GeometryInfo1,"Object3D");
		}
		catch(Exception e){}
		this.setAppearance(appearance);
		this.setGeometry(GeometryInfo1.getGeometryArray());	
	}
	public void writeGeometryInfo(GeometryInfo geometryInfo,String fileName) throws Exception
	{
		PrintWriter printWriter=new PrintWriter(fileName+".obj");
		Point3f[] coordinates=geometryInfo.getCoordinates();
		printWriter.println("# coordinates");
		for(int i=0;i<coordinates.length;i++)
		{
			printWriter.println("v "+coordinates[i].x+" "+coordinates[i].y+" "+coordinates[i].z);
		}
		printWriter.println();

		printWriter.println("# texCoords");
		printWriter.println("vt 0 0 0");
		printWriter.println();

		printWriter.println("# normals");
		Vector3f[] normals=geometryInfo.getNormals();
		for(int i=0;i<normals.length;i++)
		{
			printWriter.println("vn "+normals[i].x+" "+normals[i].y+" "+normals[i].z);
		}
		printWriter.println();

		printWriter.println("# indices");
		int[] coordinateIndices=geometryInfo.getCoordinateIndices();
		int[] normalIndices=geometryInfo.getNormalIndices();
		for(int i=0;i<coordinateIndices.length;i+=3)
		{
			printWriter.print("f");
			printWriter.print(" "+(coordinateIndices[i+0]+1)+"/1/"+(normalIndices[i+0]+1));
			printWriter.print(" "+(coordinateIndices[i+1]+1)+"/1/"+(normalIndices[i+1]+1));
			printWriter.print(" "+(coordinateIndices[i+2]+1)+"/1/"+(normalIndices[i+2]+1));
			printWriter.println();
		}
		printWriter.println();
		printWriter.close();
	}
	public void printGeometryInfo(GeometryInfo geometryInfo,String fileName)
	{
		Point3f[] coordinates=geometryInfo.getCoordinates();
		System.out.println("# coordinates");
		for(int i=0;i<coordinates.length;i++)
		{
			System.out.println("v "+coordinates[i].x+" "+coordinates[i].y+" "+coordinates[i].z);
		}
		System.out.println();

		System.out.println("# texCoords");
		System.out.println("vt 0 0 0");
		System.out.println();

		System.out.println("# normals");
		Vector3f[] normals=geometryInfo.getNormals();
		for(int i=0;i<normals.length;i++)
		{
			System.out.println("vn "+normals[i].x+" "+normals[i].y+" "+normals[i].z);
		}
		System.out.println();

		System.out.println("# indices");
		int[] coordinateIndices=geometryInfo.getCoordinateIndices();
		int[] normalIndices=geometryInfo.getNormalIndices();
		for(int i=0;i<coordinateIndices.length;i+=3)
		{
			System.out.print("f");
			System.out.print(" "+(coordinateIndices[i+0]+1)+"/1/"+(normalIndices[i+0]+1));
			System.out.print(" "+(coordinateIndices[i+1]+1)+"/1/"+(normalIndices[i+1]+1));
			System.out.print(" "+(coordinateIndices[i+2]+1)+"/1/"+(normalIndices[i+2]+1));
			System.out.println();
		}
		System.out.println();
	}
}
class BSpline3D
{
	Point3d[] ctrlPoints;
	double[] knots;
	int order,curveLength;
	public BSpline3D(Point3d[] ctrlPoints,double[] knots,int order,int curveLength)
	{
		this.ctrlPoints=ctrlPoints;;
		this.knots=knots;
		this.order=order;
		this.curveLength=curveLength;
	}
	public Point3d[] getCoordinates()
	{
		int l=curveLength,m=order,n=ctrlPoints.length;
		double du=1.0/(l-1);
		double[][] B=this.getParameterMatrix(knots,l,m,n);
		Point3d[] coordinates=new Point3d[l];
		for(int i=0;i<l;i++)coordinates[i]=this.getCoordinate(ctrlPoints,B,i);
		return coordinates;
	}
	private Point3d getCoordinate(Point3d[] ctrlPoints,double[][] B,int k)
	{
		int n=ctrlPoints.length;
		double x=0,y=0,z=0;
		for(int i=0;i<n;i++)
		{
			x+=ctrlPoints[i].x*B[k][i];
			y+=ctrlPoints[i].y*B[k][i];
			z+=ctrlPoints[i].z*B[k][i];
		}
		return new Point3d(x,y,z);
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
