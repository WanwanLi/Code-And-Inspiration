import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;
public class JavaAndShadow3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f lightDirection=new Vector3f(1f,-1f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,lightDirection);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TransformGroup1.addChild(DirectionalLight1);
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
		Material1.setDiffuseColor(new Color3f(0f,1f,0f));
		Appearance1.setMaterial(Material1);
		Dodecahedron Dodecahedron1=new Dodecahedron(0.25f,Appearance1);
		TransformGroup1.addChild(Dodecahedron1);
		GeometryArray GeometryArray1=(GeometryArray)Dodecahedron1.getGeometry();
		double planeHeight=-0.5;
		TransformGroup TransformGroup2=new TransformGroup();
		TransformGroup2.addChild(new Shadow3D(GeometryArray1,lightDirection,planeHeight));
		TransformGroup1.addChild(TransformGroup2);
		TransformGroup TransformGroup3=new TransformGroup();
		TransformGroup3.addChild(new Floor(3,2.4,planeHeight+planeHeight*0.01,true,"Floor.jpg"));
		TransformGroup1.addChild(TransformGroup3);
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}

class Shadow3D extends Shape3D
{
	public Shadow3D(GeometryArray geometryArray,Vector3f lightDirection,double planeHeight)
	{
		GeometryInfo GeometryInfo1=new GeometryInfo(geometryArray);
		GeometryInfo1.convertToIndexedTriangles();
		IndexedTriangleArray IndexedTriangleArray1=(IndexedTriangleArray)GeometryInfo1.getIndexedGeometryArray();
		int vertexCount=IndexedTriangleArray1.getVertexCount();
		int indexCount=IndexedTriangleArray1.getIndexCount();
		IndexedTriangleArray IndexedTriangleArray_Shadow=new IndexedTriangleArray(vertexCount,GeometryArray.COORDINATES,indexCount);
		Point3d coordinate=new Point3d();
		int[] coordinateIndices=new int[indexCount];
		for(int i=0;i<vertexCount;i++)
		{
			IndexedTriangleArray1.getCoordinate(i,coordinate);
			coordinate=this.getProjectionCoordinate(coordinate,lightDirection,planeHeight);
			IndexedTriangleArray_Shadow.setCoordinate(i,coordinate);
		}
		IndexedTriangleArray1.getCoordinateIndices(0,coordinateIndices);
		IndexedTriangleArray_Shadow.setCoordinateIndices(0,coordinateIndices);
		this.setGeometry(IndexedTriangleArray_Shadow);
		this.setAppearance(this.getShadowAppearance());
	}
	Appearance getShadowAppearance()
	{
		Appearance Appearance_Shadow=new Appearance();
		ColoringAttributes ColoringAttributes1=new ColoringAttributes(0.1f,0.1f,0.1f,ColoringAttributes.FASTEST);
		Appearance_Shadow.setColoringAttributes(ColoringAttributes1);
		TransparencyAttributes TransparencyAttributes1=new TransparencyAttributes(TransparencyAttributes.BLENDED,0.55f);
		Appearance_Shadow.setTransparencyAttributes(TransparencyAttributes1);
		PolygonAttributes PolygonAttributes1=new PolygonAttributes();
		PolygonAttributes1.setCullFace(PolygonAttributes.CULL_NONE);
		Appearance_Shadow.setPolygonAttributes(PolygonAttributes1);
		return Appearance_Shadow;
	}
	Point3d getProjectionCoordinate(Point3d coordinate,Vector3f lightDirection,double planeHeight)
	{
		double a=(planeHeight-coordinate.y)/lightDirection.y;
		double x=coordinate.x+a*lightDirection.x;
		double y=coordinate.y+a*lightDirection.y;
		double z=coordinate.z+a*lightDirection.z;
		Point3d projectionCoordinate=new Point3d(x,y,z);
		return projectionCoordinate;
	}
}
class Dodecahedron extends Shape3D
{
	public Dodecahedron(float r,Appearance appearance)
	{
		System.out.println(GeometryInfo.POLYGON_ARRAY);
		GeometryInfo GeometryInfo1=new GeometryInfo(5);
		float f=0.5f*(float)(Math.sqrt(5)+1);
		Point3f[] coordinates=new Point3f[]
		{
			new Point3f(r,r,r),
			new Point3f(0f,r/f,r*f),
			new Point3f(r*f,0f,r/f),
			new Point3f(r/f,r*f,0f),

			new Point3f(-r,r,r),
			new Point3f(0f,-r/f,r*f),
			new Point3f(r,-r,r),
			new Point3f(r*f,0f,-r/f),

			new Point3f(r,r,-r),
			new Point3f(-r/f,r*f,0f),
			new Point3f(-r*f,0f,r/f),
			new Point3f(-r,-r,r),

			new Point3f(r/f,-r*f,0f),
			new Point3f(r,-r,-r),
			new Point3f(0f,r/f,-r*f),
			new Point3f(-r,r,-r),

			new Point3f(-r/f,-r*f,0f),
			new Point3f(-r*f,0f,-r/f),
			new Point3f(0f,-r/f,-r*f),
			new Point3f(-r,-r,-r)
		};
		int[] coordinateIndices=new int[]
		{
			0,1,5,6,2,
			0,2,7,8,3,
			0,3,9,4,1,
			1,4,10,11,5,

			2,6,12,13,7,
			3,8,14,15,9,
			5,11,16,12,6,
			7,13,18,14,8,

			9,15,17,10,4,
			19,16,11,10,17,
			19,17,15,14,18,
			19,18,13,12,16
		};
		int[] stripCounts=new int[12];
		for(int i=0;i<12;i++)stripCounts[i]=5;
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}
class Floor extends Shape3D
{
	public Floor(double length,double width,double height,boolean normalIsUp,String pictureName)
	{
		Point3d[] coordinates=new Point3d[]
		{
			new Point3d(-length/2,height,-width/2),
			new Point3d(-length/2,height,+width/2),
			new Point3d(+length/2,height,+width/2),
			new Point3d(+length/2,height,-width/2)
		};
		int[] coordinateIndices=new int[4];
		if(normalIsUp)coordinateIndices=new int[]{0,1,2,3};
		else coordinateIndices=new int[]{3,2,1,0};
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(4,IndexedQuadArray.COORDINATES|IndexedQuadArray.TEXTURE_COORDINATE_2,4);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		this.setGeometry(IndexedQuadArray1);
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(new Material());
		TexCoord2f[] textureCoordinates=new TexCoord2f[]
		{
			new TexCoord2f(0f,1f),
			new TexCoord2f(0f,0f),
			new TexCoord2f(1f,0f),
			new TexCoord2f(1f,1f)
		};
		int[] textureCoordinateIndices=new int[]{0,1,2,3};
		IndexedQuadArray1.setTextureCoordinates(0,0,textureCoordinates);
		IndexedQuadArray1.setTextureCoordinateIndices(0,0,textureCoordinateIndices);
		TextureLoader TextureLoader1=new TextureLoader(pictureName,null);
		Appearance1.setTexture(TextureLoader1.getTexture());
		TextureAttributes TextureAttributes1=new TextureAttributes();
		TextureAttributes1.setTextureMode(TextureAttributes.COMBINE);
		Appearance1.setTextureAttributes(TextureAttributes1);
		this.setAppearance(Appearance1);
	}
}