import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndCoordinate3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),100);
		Color3f color3f=new Color3f(0f,0f,0f);
		Background Background1=new Background(color3f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		color3f=new Color3f(1f,0f,0f);
		Vector3f vector3f=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,vector3f);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(18);
		TransformGroup1.setCapability(17);
		BranchGroup1.addChild(TransformGroup1);
		MouseRotate MouseRotate1=new MouseRotate(TransformGroup1);
		MouseRotate1.setTransformGroup(TransformGroup1);
		MouseRotate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseRotate1);
		MouseZoom MouseZoom1=new MouseZoom();
		MouseZoom1.setTransformGroup(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		MouseTranslate MouseTranslate1=new MouseTranslate();
		MouseTranslate1.setTransformGroup(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,1f,0f));
		Appearance1.setMaterial(Material1);
		int n=6;
		final int X=0,Y=1,Z=2;
		float[] Colors=new float[n*n*n*3];
		float[] Coordinates=new float[n*n*n*3];
		float[] Vertexes=new float[n*n*n*3*n*n*3];
		float[] Color3fs=new float[n*n*n*3*n*n*3];
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				for(int k=0;k<n;k++)
				{
					Coordinates[i*n*n*3+j*n*3+k*3+X]=0.2f*k;
					Coordinates[i*n*n*3+j*n*3+k*3+Y]=0.2f*j;
					Coordinates[i*n*n*3+j*n*3+k*3+Z]=0.2f*i;
					Colors[i*n*n*3+j*n*3+k*3+X]=0.2f*k;
					Colors[i*n*n*3+j*n*3+k*3+Y]=0.2f*j;
					Colors[i*n*n*3+j*n*3+k*3+Z]=0.2f*i;
				}
			}
		}
		int v=0;
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				int k=0;
				Vertexes[v*2*3+X]=Coordinates[i*n*n*3+j*n*3+k*3+X];
				Vertexes[v*2*3+Y]=Coordinates[i*n*n*3+j*n*3+k*3+Y];
				Vertexes[v*2*3+Z]=Coordinates[i*n*n*3+j*n*3+k*3+Z];
				Color3fs[v*2*3+X]=k*0.1f;
				Color3fs[v*2*3+Y]=j*0.1f;
				Color3fs[v*2*3+Z]=i*0.1f;
				k=n-1;
				Vertexes[(v*2+1)*3+X]=Coordinates[i*n*n*3+j*n*3+k*3+X];
				Vertexes[(v*2+1)*3+Y]=Coordinates[i*n*n*3+j*n*3+k*3+Y];
				Vertexes[(v*2+1)*3+Z]=Coordinates[i*n*n*3+j*n*3+k*3+Z];
				Color3fs[(v*2+1)*2*3+X]=k*0.1f;
				Color3fs[(v*2+1)*2*3+Y]=j*0.1f;
				Color3fs[(v*2+1)*2*3+Z]=i*0.1f;
				v++;
			}
		}
		for(int i=0;i<n;i++)
		{
			int j=0;
			for(int k=0;k<n;k++)
			{
				j=0;
				Vertexes[v*2*3+X]=Coordinates[i*n*n*3+j*n*3+k*3+X];
				Vertexes[v*2*3+Y]=Coordinates[i*n*n*3+j*n*3+k*3+Y];
				Vertexes[v*2*3+Z]=Coordinates[i*n*n*3+j*n*3+k*3+Z];
				Color3fs[v*2*3+X]=k*0.1f;
				Color3fs[v*2*3+Y]=j*0.1f;
				Color3fs[v*2*3+Z]=i*0.1f;
				j=n-1;
				Vertexes[(v*2+1)*3+X]=Coordinates[i*n*n*3+j*n*3+k*3+X];
				Vertexes[(v*2+1)*3+Y]=Coordinates[i*n*n*3+j*n*3+k*3+Y];
				Vertexes[(v*2+1)*3+Z]=Coordinates[i*n*n*3+j*n*3+k*3+Z];
				Color3fs[(v*2+1)*2*3+X]=k*0.1f;
				Color3fs[(v*2+1)*2*3+Y]=j*0.1f;
				Color3fs[(v*2+1)*2*3+Z]=i*0.1f;
				v++;
			}
		}
		int i=0;
		for(int j=0;j<n;j++)
		{
			for(int k=0;k<n;k++)
			{
				i=0;
				Vertexes[v*2*3+X]=Coordinates[i*n*n*3+j*n*3+k*3+X];
				Vertexes[v*2*3+Y]=Coordinates[i*n*n*3+j*n*3+k*3+Y];
				Vertexes[v*2*3+Z]=Coordinates[i*n*n*3+j*n*3+k*3+Z];
				Color3fs[v*2*3+X]=k*0.1f;
				Color3fs[v*2*3+Y]=j*0.1f;
				Color3fs[v*2*3+Z]=i*0.1f;
				i=n-1;
				Vertexes[(v*2+1)*3+X]=Coordinates[i*n*n*3+j*n*3+k*3+X];
				Vertexes[(v*2+1)*3+Y]=Coordinates[i*n*n*3+j*n*3+k*3+Y];
				Vertexes[(v*2+1)*3+Z]=Coordinates[i*n*n*3+j*n*3+k*3+Z];
				Color3fs[(v*2+1)*2*3+X]=k*0.1f;
				Color3fs[(v*2+1)*2*3+Y]=j*0.1f;
				Color3fs[(v*2+1)*2*3+Z]=i*0.1f;
				v++;
			}
		}
		System.out.println("v:"+v);
		int[] numberOfEachPolygon=new int[]{3,6};
		Shape3D Shape3D1=new Shape3D();
		System.out.println(PointArray.COORDINATES|PointArray.COLOR_3);
		PointArray PointArray1=new PointArray(n*n*n,5);
		PointArray1.setCoordinates(0,Coordinates);
		PointArray1.setColors(0,Colors);
		PointAttributes PointAttributes1=new PointAttributes();
		PointAttributes1.setPointSize(20.0f);
		PointAttributes1.setPointAntialiasingEnable(true);
		Appearance1.setPointAttributes(PointAttributes1);
		Shape3D1.setGeometry(PointArray1);
		Shape3D1.setAppearance(Appearance1);
		TransformGroup1.addChild(Shape3D1);
		Shape3D Shape3D2=new Shape3D();
		System.out.println(LineArray.COORDINATES|LineArray.COLOR_3);
		LineArray LineArray1=new LineArray(n*n*n*3*n*n*3,5);
		LineArray1.setCoordinates(0,Vertexes);
		LineArray1.setColors(0,Color3fs);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(2.0f);
		LineAttributes1.setLineAntialiasingEnable(true);
		Appearance1.setLineAttributes(LineAttributes1);
		Shape3D2.setGeometry(LineArray1);
		Shape3D2.setAppearance(Appearance1);
		TransformGroup1.addChild(Shape3D2);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
