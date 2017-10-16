import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndCoonsSurface3D
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
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,1f,1f));
		Appearance1.setMaterial(Material1);
		int m=100,n=100;
		Point3d[] pU0=new Point3d[m],pU1=new Point3d[m];
		Point3d[] p0V=new Point3d[n],p1V=new Point3d[n];
		double A=0.1,r=0.4,wU=2*Math.PI/(m-1),wV=Math.PI/(n-1);
		for(int i=0;i<m;i++)
		{
			double dr=2.0*r/(m-1);
			double y=A*Math.sin(wU*i);
			pU0[i]=new Point3d(-r,y,-r+i*dr);
			pU1[i]=new Point3d(r,-y,-r+i*dr);
		}
		for(int j=0;j<n;j++)
		{
			double dr=2.0*r/(n-1);
			double y=A*Math.sin(wV*j);
			p0V[j]=new Point3d(-r+j*dr,y,-r);
			p1V[j]=new Point3d(-r+j*dr,-y,r);
		}
		CoonsSurface3D CoonsSurface3D=new CoonsSurface3D(pU0,pU1,p0V,p1V);
		CoonsSurface3D.setAppearance(Appearance1);
		TransformGroup1.addChild(CoonsSurface3D);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
	
}
class CoonsSurface3D extends Shape3D
{
	public CoonsSurface3D(Point3d[] pointsU0,Point3d[] pointsU1,Point3d[] points0V,Point3d[] points1V)
	{
		int r=pointsU0.length,c=points0V.length,v=0;
		Point3d[] coordinates=new Point3d[r*c];
		int[] coordinateIndices=new int[(r-1)*(c-1)*4];
		for(int i=0;i<r;i++)for(int j=0;j<c;j++)coordinates[i*c+j]=this.getCoordinate(pointsU0,pointsU1,points0V,points1V,i,j);
		for(int i=0;i<r-1;i++)
		{
			for(int j=0;j<c-1;j++)
			{
				coordinateIndices[v++]=(i+0)*c+(j+0);
				coordinateIndices[v++]=(i+1)*c+(j+0);
				coordinateIndices[v++]=(i+1)*c+(j+1);
				coordinateIndices[v++]=(i+0)*c+(j+1);
			}
		}
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(coordinates.length,IndexedQuadArray.COORDINATES|IndexedQuadArray.NORMALS,coordinateIndices.length);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
	Point3d midPoint(Point3d p0,Point3d p1,double u)
	{
		double x=(1-u)*p0.x+u*p1.x;
		double y=(1-u)*p0.y+u*p1.y;
		double z=(1-u)*p0.z+u*p1.z;
		return new Point3d(x,y,z);
	}
	private Point3d getCoordinate(Point3d[] pU0,Point3d[] pU1,Point3d[] p0V,Point3d[] p1V,int i,int j)
	{
		int m=pU0.length,n=p0V.length;
		double u=i*1.0/(m-1),v=j*1.0/(n-1);
		Point3d q=midPoint(p0V[j],p1V[j],u);
		Point3d r=midPoint(pU0[i],pU1[i],v);
		Point3d p00=p0V[0],p01=p0V[n-1];
		Point3d p10=p1V[0],p11=p1V[n-1];
		Point3d sU0=midPoint(p00,p10,u);
		Point3d sU1=midPoint(p01,p11,u);
		Point3d s=midPoint(sU0,sU1,v);
		double x=q.x+r.x-s.x;
		double y=q.y+r.y-s.y;
		double z=q.z+r.z-s.z;
		return new Point3d(x,y,z);
	}
}
