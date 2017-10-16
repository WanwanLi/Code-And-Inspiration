import java.applet.*;
import java.awt.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.io.*;
public class JavaAndMandelbrot3D extends Applet
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
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		Transform3D transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(0.05,0.05,0.05));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Mandelbrot3D(60f,Appearance1));
		TransformGroup1.addChild(TransformGroup2);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndMandelbrot3D(),400,400);
	}
}
class Mandelbrot3D extends Shape3D
{
	public int m=200;
	public int n=1000;
	public int h=300;
	double a;
	float x,y,z,r;
	float p0=0f;
	float q0=0f;
	float s0=0f;
	float v0=0f;
	float pMin=-2.2f;
	float pMax=0.7f;
	float qMin=-1.2f;
	float qMax=1.2f;
	float sMin=-1.2f;
	float sMax=1.2f;
	float size;
	public Mandelbrot3D(float size,Appearance appearance)
	{
		Point3f[] coordinates=new Point3f[m*n];
		float dx=2*size/(m-1);
		float dr=size/h;
		int v=0;
		for(int i=0;i<m;i++)
		{
			x=-size+i*dx;
			p0=pMin+2*i*(pMax-pMin)/(2*(m-1));
			for(int j=0;j<n;j++)
			{
				a=j*2*Math.PI/n;
				q0=s0=r=0f;
				v0=0f;
				int k=0;
				while(k<h&&Mandelbrot(p0,q0,s0,v0)==100)
				{
					k++;
					r=k*dr;
					this.getYZ();
					q0=qMin+(int)(2*(h-1)*(y+size)/(2*size))*(qMax-qMin)/(2*(h-1));
					s0=sMin+(int)(2*(h-1)*(z+size)/(2*size))*(sMax-sMin)/(2*(h-1));
				}
				coordinates[v++]=new Point3f(x,y,z);
			}
		};
		v=0;
		int[] coordinateIndices=new int[4*(m-1)*n];
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n;j++)
			{
				coordinateIndices[v++]=((i+0)%m)*n+(j+0)%n;
				coordinateIndices[v++]=((i+0)%m)*n+(j+1)%n;
				coordinateIndices[v++]=((i+1)%m)*n+(j+1)%n;
				coordinateIndices[v++]=((i+1)%m)*n+(j+0)%n;
			}
		}
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(coordinates.length,IndexedQuadArray.COORDINATES,coordinateIndices.length);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
	public int Mandelbrot(float p,float q,float s,float v)
	{
		float x0=0f,y0=0f,z0=0f,t0=0f;
		float x,y,z,t;
		int i;
		for(i=0;i<100;i++)
		{
		    	x=x0*x0-y0*y0-z0*z0-t0*t0+p;
			y=2*x0*y0+q;
			z=2*x0*z0+s;
			t=2*x0*t0+v;
			if (x*x+y*y+z*z+t*t>100)return i;		         
			x0=x;
			y0=y;
			z0=z;
			t0=t;
		}
		return i;	
	}
	private void getYZ()
	{
		y=r*(float)(Math.cos(a));
		z=r*(float)(Math.sin(a));
	}
}