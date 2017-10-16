import java.applet.*;
import java.awt.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.io.*;
public class JavaAndJulia3D extends Applet
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
		float size=0.5f;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);	
		TransformGroup1.addChild(new Julia3D(10f,Appearance1));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndJulia3D(),400,400);
	}
}
class Julia3D extends Shape3D
{
	public int m=400;
	public int n=600;
	Point3f center;
	double a;
	float x,y,z,r;
	float size;
	float p=-1f;
	float q=0.2f;
	float s=0f;
	float v=0f;
	float t=0f;
	public Julia3D(float size,Appearance appearance)
	{

		center=new Point3f(0f,0f,0f);
		float x0,x1;
		float dr=size/5;
		float dy=size/n;
		Point3f[] coordinates=new Point3f[m*n];
		x=-size;
		float dx=size/10;
		while(dx>size/500)
		{
			boolean rootIsfound=false;
			while(!rootIsfound)
			{
				x+=dx;
				for(int j=0;j<n;j++)
				{
					y=j*dy;
					if(Julia(x,y,z,t)==100){rootIsfound=true;break;}
					y=-j*dy;
					if(Julia(x,y,z,t)==100){rootIsfound=true;break;}
				}
			}
			x-=dx;
			dx/=2;
		}
		x0=x+2*dx;
		x=size;
		dx=size/10;
		while(dx>size/500)
		{
			boolean rootIsfound=false;
			while(!rootIsfound)
			{
				x-=dx;
				for(int j=0;j<n;j++)
				{
					y=j*dy;
					if(Julia(x,y,z,t)==100){rootIsfound=true;break;}
					y=-j*dy;
					if(Julia(x,y,z,t)==100){rootIsfound=true;break;}
				}
			}
			x+=dx;
			dx/=2;
		}
		x1=x-2*dx;
		dx=(x1-x0)/(m-1);
		this.size=size;
		y=z=0f;
		int v=0;
		for(int i=0;i<m;i++)
		{
			a=0.0;
			x=x0+i*dx;
			y=z=r=0f;
			for(int j=0;j<n;j++)
			{
				y=j*dy;
				if(Julia(x,y,z,t)==100)break;
				y=-j*dy;
				if(Julia(x,y,z,t)==100)break;
			}
			this.center=new Point3f(x,y,z);
			for(int j=0;j<n;j++)
			{
				r=0;
				a=j*2*Math.PI/n;
				this.getYZ();
				dr=size/5f;
				while(dr>size/500f)
				{
					while(this.isInBoundary()&&Julia(x,y,z,t)==100){r+=dr;this.getYZ();}
					r-=dr;
					this.getYZ();
					dr/=2;
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
	private boolean isInBoundary()
	{
		return (y>-size&&y<size&&z>-size&&z<size);
	}
	public int Julia(float x0,float y0,float z0,float t0)
	{
		float x;
		float y;
		float z;
		float t;
		int i;
		for(i=0;i<100;i++)
		{
		    	x=x0*x0-y0*y0-z0*z0-t0*t0+p;
			y=2*x0*y0+q;
			z=2*x0*z0+s;
			t=2*x0*t0+v;
			if (x*x+y*y+z*z+t*t>4)return i;		         
			x0=x;
			y0=y;
			z0=z;
			t0=t;
		}
		return i;	
	}
	private void getYZ()
	{
		y=center.y+r*(float)(Math.cos(a));
		z=center.z+r*(float)(Math.sin(a));
	}
}