import java.awt.*;
import java.awt.image.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;
public class JavaAndCup3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background(0f,0f,0f);
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
		Vector3f lightDirection=new Vector3f(0f,0f,-1f);
		DirectionalLight DirectionalLight1=new DirectionalLight(color3f,lightDirection);
		DirectionalLight1.setInfluencingBounds(BoundingSphere1);
		BranchGroup1.addChild(DirectionalLight1);
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
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
		TransformGroup1.addChild(new Cup3D(0.3f,0.2f,0.5f,new Color(255,200,0),new Color(0,255,255)));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Cup3D extends TransformGroup
{
	public Cup3D(float R,float r,float H,Color cupColor,Color stringColor)
	{
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(cupColor));
		Appearance cupAppearance=new Appearance();
		cupAppearance.setMaterial(Material1);
		this.addChild(this.getCup3D_OutSide(R,r,H,cupColor,stringColor));
		this.addChild(this.getCup3D_InSide(R,r,H,cupAppearance));
		this.addChild(new Cylinder(r,r/10,cupAppearance));
	}
	public Shape3D getCup3D_OutSide(float R,float r,float H,Color cupColor,Color stringColor)
	{
		int m=2<<7;
		QuadArray QuadArray1=new QuadArray(m*4,QuadArray.COORDINATES|QuadArray.NORMALS|QuadArray.TEXTURE_COORDINATE_2);
		Point3f p0=new Point3f(R,H,0f);
		Point3f p1=new Point3f(r,0f,0f);
		Vector3f normal=new Vector3f(H,r-R,0f);
		TexCoord2f t0=new TexCoord2f(0f,0f);
		TexCoord2f t1=new TexCoord2f(0f,1f);
		Transform3D Transform3D_rotY=new Transform3D();
		Transform3D_rotY.rotY(2*Math.PI/m);
		for(int j=0;j<m;j++)
		{
			QuadArray1.setCoordinate(j*4+0,p0);
			QuadArray1.setCoordinate(j*4+1,p1);
			QuadArray1.setNormal(j*4+0,normal);
			QuadArray1.setNormal(j*4+1,normal);
			QuadArray1.setTextureCoordinate(0,j*4+0,t1);
			QuadArray1.setTextureCoordinate(0,j*4+1,t0);
			Transform3D_rotY.transform(p0);
			Transform3D_rotY.transform(p1);
			Transform3D_rotY.transform(normal);
			t0=new TexCoord2f((j+1)*1f/m,0f);
			t1=new TexCoord2f((j+1)*1f/m,1f);
			QuadArray1.setCoordinate(j*4+2,p1);
			QuadArray1.setCoordinate(j*4+3,p0);
			QuadArray1.setNormal(j*4+2,normal);
			QuadArray1.setNormal(j*4+3,normal);
			QuadArray1.setTextureCoordinate(0,j*4+2,t0);
			QuadArray1.setTextureCoordinate(0,j*4+3,t1);
		}
		return new Shape3D(QuadArray1,getImageComponent2DAppearance(cupColor,stringColor));
	}
	private Appearance getImageComponent2DAppearance(Color cupColor,Color stringColor)
	{
		Appearance imageComponent2DAppearance=new Appearance();
		int imageWidth=2<<9,imageHeight=2<<7;
		BufferedImage BufferedImage1=new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_ARGB);
		Graphics g=BufferedImage1.getGraphics();
		g.setColor(cupColor);
		g.fillRect(0,0,imageWidth,imageHeight);
		g.setFont(new Font("Microsoft Tai Le",Font.BOLD,100));
		g.setColor(stringColor);
		g.drawString("Java3D",0,100);
		ImageComponent2D imageComponent2D=new ImageComponent2D(ImageComponent2D.FORMAT_RGBA,BufferedImage1);
		Texture2D texture2D=new Texture2D(Texture.BASE_LEVEL,Texture.RGBA,imageComponent2D.getWidth(),imageComponent2D.getHeight());
		texture2D.setImage(0,imageComponent2D);
		texture2D.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		imageComponent2DAppearance.setTexture(texture2D);
		TextureAttributes TextureAttributes1=new TextureAttributes();
		TextureAttributes1.setTextureMode(TextureAttributes.COMBINE);
		imageComponent2DAppearance.setTextureAttributes(TextureAttributes1);
		imageComponent2DAppearance.setMaterial(new Material());
		return imageComponent2DAppearance;
	}
	public Shape3D getCup3D_InSide(float R,float r,float H,Appearance cupAppearance)
	{
		int m=128;
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray(m*4,IndexedQuadArray.COORDINATES,m*4);
		double w=2*Math.PI/(m-1);
		for(int j=0;j<m;j++)
		{
			Point3f[] coordinates=new Point3f[]
			{
				new Point3f((float)(R*Math.cos((j+0)*w)),H,(float)(R*Math.sin((j+0)*w))),
				new Point3f((float)(r*Math.cos((j+0)*w)),0,(float)(r*Math.sin((j+0)*w))),
				new Point3f((float)(r*Math.cos((j+1)*w)),0,(float)(r*Math.sin((j+1)*w))),
				new Point3f((float)(R*Math.cos((j+1)*w)),H,(float)(R*Math.sin((j+1)*w)))

			};
			int[] coordinateIndices=new int[]{j*4+0,j*4+1,j*4+2,j*4+3};
			IndexedQuadArray1.setCoordinates(j*4,coordinates);
			IndexedQuadArray1.setCoordinateIndices(j*4,coordinateIndices);
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		(new NormalGenerator()).generateNormals(GeometryInfo1);
		return new Shape3D(GeometryInfo1.getGeometryArray(),cupAppearance);
	}
}