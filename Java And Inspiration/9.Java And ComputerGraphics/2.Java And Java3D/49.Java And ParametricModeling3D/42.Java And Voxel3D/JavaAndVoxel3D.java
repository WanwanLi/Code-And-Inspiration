import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndVoxel3D
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
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		PointAttributes PointAttributes1=new PointAttributes();
		PointAttributes1.setPointSize(1.0f);
		PointAttributes1.setPointAntialiasingEnable(true);
		Appearance Appearance1=new Appearance();
		Appearance1.setPointAttributes(PointAttributes1);
		Transform3D Transform3D=new Transform3D();
		Transform3D.setScale(new Vector3d(0.2,0.2,0.2));
		TransformGroup1.setTransform(Transform3D);
//		Voxel3D Function_Sphere3D=new Voxel3D(new Function_Sphere(),-1.2,1.2,-1.2,1.2,-1.2,1.2,Appearance1);
		double r=2.4;
		boolean b=true;
		Voxel3D Function_Sphere3D=new Voxel3D(new Function_Sphere(),-r,r,-r,r,-r,0);
	//	Voxel3D Function_Sphere3D=new Voxel3D(new Function_Sphere(),-r,r,-r,r,-r,r,Appearance1);
	//	Voxel3D Function_Sphere3D=new Voxel3D(new Function_Sphere(),2,true,-r,r,-r,r,-r,r,Appearance1);
		TransformGroup1.addChild(Function_Sphere3D);
		Appearance Appearance2=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,0f));
		Appearance2.setMaterial(Material1);
		int l=10;double[] v=new double[l];for(int i=0;i<l;i++)v[i]=(i-l/2)*0.2;
	//	TransformGroup1.addChild(new ContourSurface3D(Function_Sphere3D,v,Appearance2));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Function_Sphere implements Function
{
	public double value(double x,double y,double z)
	{
		double a=1.59,b=0.6,c=0.8;
		return x*x/(a*a)-y*y/(b*b)+z*z/(c*c);
	}
}
interface Function
{
	double value(double x,double y,double z);
}

class Voxel3D extends Shape3D
{
	//public int m=100,n=100,l=100;
	public int m=50,n=50,l=50;
	public Point3d[] coordinates=new Point3d[l*m*n];
	public Color3f[] colors=new Color3f[l*m*n];
	public double[] values=new double[l*m*n];
	public double maxValue=-Double.MAX_VALUE;
	public double minValue=Double.MAX_VALUE;
	public int[] coordinateIndices;
	public Voxel3D(Function function,double x0,double x1,double y0,double y1,double z0,double z1)
	{
		double dx=(x1-x0)/(n-1);
		double dy=(y1-y0)/(l-1);
		double dz=(z1-z0)/(m-1);
		for(int k=0;k<l;k++)
		{
			double y=y0+k*dy;
			for(int i=0;i<m;i++)
			{
				double z=z0+i*dz;
				for(int j=0;j<n;j++)
				{
					double x=x0+j*dx;
					this.coordinates[k*m*n+i*n+j]=new Point3d(x,y,z);
					this.values[k*m*n+i*n+j]=function.value(x,y,z);
					if(values[k*m*n+i*n+j]>maxValue)maxValue=values[k*m*n+i*n+j];
					if(values[k*m*n+i*n+j]<minValue)minValue=values[k*m*n+i*n+j];
				}
			}
		}
		int c=0;
		this.coordinateIndices=new int[6*(m-1)*(n-1)*4];
		for(int k=0;k<1;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+1);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+1);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+0);
				}
			}
		}
		for(int k=l-1;k<l;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+1);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+1);
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=0;i<1;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+1);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+1);
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=m-1;i<m;i++)
			{
				for(int j=0;j<n-1;j++)
				{
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+1);
					this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+1);
					this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+0);
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=0;j<1;j++)
				{
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+0);
					this.coordinateIndices[c++]=(k+1)*m*n+(i+1)*n+(j+0);
					this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+0);
				}
			}
		}
		for(int k=0;k<l-1;k++)
		{
			for(int i=0;i<m-1;i++)
			{
				for(int j=n-1;j<n;j++)
				{
					this.coordinateIndices[c++]=(k+0)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+1)*m*n+(i+0)*n+(j+0);
					this.coordinateIndices[c++]=(k+1)*m*n+(i+1)*n+(j+0);
					this.coordinateIndices[c++]=(k+0)*m*n+(i+1)*n+(j+0);
				}
			}
		}
		for(int k=0;k<l;k++)for(int i=0;i<m;i++)for(int j=0;j<n;j++)this.colors[k*m*n+i*n+j]=this.getColor(i,j,k);
		this.setGeometry(this.getIndexedQuadArray());
	}
	public Voxel3D(Function function,double x0,double x1,double y0,double y1,double z0,double z1,Appearance appearance)
	{
		double dx=(x1-x0)/(n-1);
		double dy=(y1-y0)/(l-1);
		double dz=(z1-z0)/(m-1);
		for(int k=0;k<l;k++)
		{
			double y=y0+k*dy;
			for(int i=0;i<m;i++)
			{
				double z=z0+i*dz;
				for(int j=0;j<n;j++)
				{
					double x=x0+j*dx;
					this.coordinates[k*m*n+i*n+j]=new Point3d(x,y,z);
					this.values[k*m*n+i*n+j]=function.value(x,y,z);
					if(values[k*m*n+i*n+j]>maxValue)maxValue=values[k*m*n+i*n+j];
					if(values[k*m*n+i*n+j]<minValue)minValue=values[k*m*n+i*n+j];
				}
			}
		}
		for(int k=0;k<l;k++)for(int i=0;i<m;i++)for(int j=0;j<n;j++)this.colors[k*m*n+i*n+j]=this.getColor(i,j,k);
		this.setGeometry(this.getPointArray());
		this.setAppearance(appearance);
	}
	public Voxel3D(Function function,double v,boolean b,double x0,double x1,double y0,double y1,double z0,double z1,Appearance appearance)
	{
		double dx=(x1-x0)/(n-1);
		double dy=(y1-y0)/(l-1);
		double dz=(z1-z0)/(m-1);
		int length=0;
		for(int k=0;k<l;k++)
		{
			double y=y0+k*dy;
			for(int i=0;i<m;i++)
			{
				double z=z0+i*dz;
				for(int j=0;j<n;j++)
				{
					double x=x0+j*dx;
					this.values[k*m*n+i*n+j]=function.value(x,y,z);
					if(isRoot(i,j,k,v,b))
					{
						length++;
						if(values[k*m*n+i*n+j]>maxValue)maxValue=values[k*m*n+i*n+j];
						if(values[k*m*n+i*n+j]<minValue)minValue=values[k*m*n+i*n+j];
					}
				}
			}
		}
		Point3d[] coordinates=new Point3d[length];
		Color3f[] colors=new Color3f[length];
		length=0;
		for(int k=0;k<l;k++)
		{
			double y=y0+k*dy;
			for(int i=0;i<m;i++)
			{
				double z=z0+i*dz;
				for(int j=0;j<n;j++)
				{
					double x=x0+j*dx;
					if(isRoot(i,j,k,v,b))
					{
						coordinates[length]=new Point3d(x,y,z);
						colors[length++]=this.getColor(i,j,k);
					}
				}
			}
		}
		PointArray PointArray1=new PointArray(length,PointArray.COORDINATES|PointArray.COLOR_3);
		PointArray1.setCoordinates(0,coordinates);
		PointArray1.setColors(0,colors);
		this.setGeometry(PointArray1);
		this.setAppearance(appearance);
	}
	private Color3f getColor(int i,int j,int k)
	{
		double red=0,green=0,blue=0;
		double hue=(values[k*m*n+i*n+j]-minValue)/(maxValue-minValue);
		double H=6*hue%6;
		int n=(int)H;
		double h=H-n;
		double m=n%2==0?h:1-h;
		switch(n)
		{
			case 0:red=1;green=m;blue=0;break;
			case 1:red=m;green=1;blue=0;break;
			case 2:red=0;green=1;blue=m;break;
			case 3:red=0;green=m;blue=1;break;
			case 4:red=m;green=0;blue=1;break;
			case 5:red=1;green=0;blue=m;break;
		}
		return new Color3f((float)red,(float)green,(float)blue);
	}
	private boolean isRoot(int i,int j,int k,double v,boolean b)
	{
		if(values[k*m*n+i*n+j]>=v)return b?true:false;
		else return b?false:true;
	}
	private GeometryArray getPointArray()
	{
		PointArray PointArray1=new PointArray(coordinates.length,PointArray.COORDINATES|PointArray.COLOR_3);
		PointArray1.setCoordinates(0,coordinates);
		PointArray1.setColors(0,colors);
		return PointArray1;
	}
	private GeometryArray getIndexedQuadArray()
	{
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(coordinates.length,IndexedQuadArray.COORDINATES|PointArray.COLOR_3,coordinateIndices.length);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		IndexedQuadArray1.setColors(0,colors);
		IndexedQuadArray1.setColorIndices(0,coordinateIndices);
		return IndexedQuadArray1;
	}
}

