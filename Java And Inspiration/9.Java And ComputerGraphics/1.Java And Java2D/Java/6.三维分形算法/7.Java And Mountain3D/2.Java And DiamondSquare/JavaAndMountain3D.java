import java.applet.*;
import java.awt.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndMountain3D extends Applet
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
		float xdim=2f;
		float zdim=1.5f;
		float ydim=1.2f;
		double Hurst=0.9;
		int maxDepth=8;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.green));
		Material1.setSpecularColor(new Color3f(Color.black));
		Appearance1.setMaterial(Material1);
		TransformGroup1.addChild(new Mountain3D(xdim,ydim,zdim,Hurst,maxDepth,Appearance1));
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,-ydim/10,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		Appearance1=new Appearance();
		Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.blue));
		Appearance1.setMaterial(Material1);
		TransformGroup2.addChild(new Box(xdim/1.5f,ydim/8,zdim/1.5f,Appearance1));
		TransformGroup1.addChild(TransformGroup2);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse(canvas3D);
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	public static void main(String[] args)
	{
		new MainFrame(new JavaAndMountain3D(),400,400);
	}
}
class Mountain3D extends Shape3D
{
	int MaxDepth;
	Point3f[] coordinates;
	int[] coordinateIndices;
	float decay=0.5f;
	float Abs(float f){return (f<0?-f:f);}
	int m=1;
	private Point3f getRandomDisplacementMidPoint3f(Point3f p0,Point3f p1,Point3f p2,Point3f p3,float size)
	{
		return new Point3f((p0.x+p1.x+p2.x+p3.x)/4,Abs((p0.y+p1.y+p2.y+p3.y)/4+size*(float)(Math.random()-0.5)),(p0.z+p1.z+p2.z+p3.z)/4);
	}
	private Point3f getMiddlePoint3f(Point3f p0,Point3f p1)
	{
		return new Point3f((p0.x+p1.x)/2,(p0.y+p1.y)/2,(p0.z+p1.z)/2);
	}
	private void getDiamondCoordinates(int start_i,int start_j,int width,float size)
	{
		Point3f p00=this.coordinates[(start_i+0)*(m+1)+(start_j+0)];
		Point3f p01=this.coordinates[(start_i+0)*(m+1)+(start_j+width)];
		Point3f p11=this.coordinates[(start_i+width)*(m+1)+(start_j+width)];
		Point3f p10=this.coordinates[(start_i+width)*(m+1)+(start_j+0)];
		this.coordinates[(start_i+width/2)*(m+1)+(start_j+width/2)]=this.getRandomDisplacementMidPoint3f(p00,p01,p11,p10,size);
	}
	private void getSquareCoordinates(int center_i,int center_j,int width,float size)
	{
		Point3f pC=this.coordinates[center_i*(m+1)+center_j];
		Point3f p0=this.coordinates[(center_i-width/2)*(m+1)+(center_j-width/2)];
		Point3f p1=this.coordinates[(center_i-width/2)*(m+1)+(center_j+width/2)];
		Point3f p2=this.coordinates[(center_i+width/2)*(m+1)+(center_j+width/2)];
		Point3f p3=this.coordinates[(center_i+width/2)*(m+1)+(center_j-width/2)];
		if(center_i-width<0)this.coordinates[(center_i-width/2)*(m+1)+center_j]=this.getMiddlePoint3f(p0,p1);
		else
		{
			Point3f p01=this.coordinates[(center_i-width)*(m+1)+center_j];
			this.coordinates[(center_i-width/2)*(m+1)+center_j]=this.getRandomDisplacementMidPoint3f(p0,p01,p1,pC,size);
		}
		if(center_j+width>m)this.coordinates[center_i*(m+1)+(center_j+width/2)]=this.getMiddlePoint3f(p1,p2);
		else
		{
			Point3f p12=this.coordinates[center_i*(m+1)+(center_j+width)];
			this.coordinates[center_i*(m+1)+(center_j+width/2)]=this.getRandomDisplacementMidPoint3f(p1,p12,p2,pC,size);
		}
		if(center_i+width>m)this.coordinates[(center_i+width/2)*(m+1)+center_j]=this.getMiddlePoint3f(p2,p3);
		else
		{
			Point3f p23=this.coordinates[(center_i+width)*(m+1)+center_j];
			this.coordinates[(center_i+width/2)*(m+1)+center_j]=this.getRandomDisplacementMidPoint3f(p2,p23,p3,pC,size);
		}
		if(center_j-width<0)this.coordinates[center_i*(m+1)+(center_j-width/2)]=this.getMiddlePoint3f(p3,p0);
		else
		{
			Point3f p30=this.coordinates[center_i*(m+1)+(center_j-width)];
			this.coordinates[center_i*(m+1)+(center_j-width/2)]=this.getRandomDisplacementMidPoint3f(p3,p30,p0,pC,size);
		}
	}
	private void getCoordinates(float size)
	{
		int width=m;
		while(width>0)
		{
			for(int i=0;i<m;i+=width)for(int j=0;j<m;j+=width)this.getDiamondCoordinates(i,j,width,size);	
			for(int i=0;i<m;i+=width)for(int j=0;j<m;j+=width)this.getSquareCoordinates(i+width/2,j+width/2,width,size);
			size*=decay;
			width/=2;
		}
	}
	private void getCoordinateIndices()
	{
		int v=0;
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<m;j++)
			{
				this.coordinateIndices[v++]=(i+0)*(m+1)+(j+0);
				this.coordinateIndices[v++]=(i+1)*(m+1)+(j+0);
				this.coordinateIndices[v++]=(i+1)*(m+1)+(j+1);
				this.coordinateIndices[v++]=(i+0)*(m+1)+(j+1);
			}
		}
	}
	public Mountain3D(float xdim,float ydim,float zdim,double Hurst,int maxDepth,Appearance appearance)
	{
		this.MaxDepth=maxDepth;
		this.decay=(float)(1.0/Math.pow(2,Hurst));
		this.m=2<<MaxDepth;
		this.coordinates=new Point3f[(m+1)*(m+1)];
		this.coordinateIndices=new int[4*m*m];
		this.coordinates[0*(m+1)+0]=new Point3f(-xdim/2,0f,-zdim/2);
		this.coordinates[0*(m+1)+m]=new Point3f(xdim/2,0f,-zdim/2);
		this.coordinates[m*(m+1)+m]=new Point3f(xdim/2,0f,zdim/2);
		this.coordinates[m*(m+1)+0]=new Point3f(-xdim/2,0f,zdim/2);
		this.getCoordinates(ydim);
		this.getCoordinateIndices();
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray((m+1)*(m+1),1,4*m*m);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}