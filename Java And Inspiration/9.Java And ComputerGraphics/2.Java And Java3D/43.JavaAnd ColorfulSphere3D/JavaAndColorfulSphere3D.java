import java.io.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.imageio.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.image.*;
public class JavaAndColorfulSphere3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),100);
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
		TransformGroup1.addChild(new ColorfulSphere3D("Image.jpg",0.5f));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class ColorfulSphere3D extends Shape3D
{
	public ColorfulSphere3D(String imageName,float r)
	{
		int m=40,n=80,v=0,c=0,t=0;
		Point3d[] coordinates=new Point3d[m*n];
		Color3f[] colors=new Color3f[m*n];
	//	TexCoord2f[] textureCoordinates=new TexCoord2f[m*n];
		Point2f[] textureCoordinates=new Point2f[m*n];
		int[] coordinateIndices=new int[(m-1)*(n-1)*4];
		for(int i=0;i<m;i++)
		{
			double a=Math.PI*i/(m-1);
			for(int j=0;j<n;j++)
			{
				double w=2.0*Math.PI*j/(n-1);
				double x=r*Math.cos(a);
				double y=r*Math.sin(a)*Math.sin(w);
				double z=r*Math.sin(a)*Math.cos(w);
				coordinates[v++]=new Point3d(x,y,z);
				float[] RGB=function(i+0.0,j+0.0);
				colors[c++]=new Color3f(RGB[0],RGB[1],RGB[2]);
				textureCoordinates[t++]=new Point2f((i+0.0f)/(m-1),(j+0.0f)/(n-1));
			}
		}
		v=0;
		for(int i=0;i<m-1;i++)
		{
			for(int j=0;j<n-1;j++)
			{
				coordinateIndices[v++]=(i+0)*n+(j+0);
				coordinateIndices[v++]=(i+0)*n+(j+1);
				coordinateIndices[v++]=(i+1)*n+(j+1);
				coordinateIndices[v++]=(i+1)*n+(j+0);
			}
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.QUAD_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setColors(colors);
		GeometryInfo1.setColorIndices(coordinateIndices);
		GeometryInfo1.setTextureCoordinates(textureCoordinates);
		GeometryInfo1.setTextureCoordinateIndices(coordinateIndices);
		new NormalGenerator().generateNormals(GeometryInfo1);
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(new Material());
		if(imageName!=null)this.setAppearance(Image3D.getImageComponent2DAppearance(imageName,true));
		else this.setAppearance(Appearance1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
	}
	double minR=Double.MAX_VALUE;
	double minG=Double.MAX_VALUE;
	double minB=Double.MAX_VALUE;
	double maxR=Double.MIN_VALUE;
	double maxG=Double.MIN_VALUE;
	double maxB=Double.MIN_VALUE;
	private float[] function(double x,double y)
	{
		double r=x+y;
		double g=x*y;
		double b=x-y;
		if(r<minR)minR=r;
		if(g<minG)minG=g;
		if(b<minB)minB=b;
		if(r>maxR)maxR=r;
		if(g>maxG)maxG=g;
		if(b>maxB)maxB=b;
		float R=(float)((r-minR)/(maxR-minR));
		float G=(float)((g-minG)/(maxG-minG));
		float B=(float)((b-minB)/(maxB-minB));
		return new float[]{R,G,B};
	}
}

class Image3D extends Shape3D
{
	public Image3D(double width,double height,String imageName)
	{
		Point3d p00=new Point3d(-width/2,-height/2,0);
		Point3d p01=new Point3d(-width/2,+height/2,0);
		Point3d p11=new Point3d(+width/2,+height/2,0);
		Point3d p10=new Point3d(+width/2,-height/2,0);
		TexCoord2f t00=new TexCoord2f(0f,0f);
		TexCoord2f t01=new TexCoord2f(0f,1f);
		TexCoord2f t11=new TexCoord2f(1f,1f);
		TexCoord2f t10=new TexCoord2f(1f,0f);
		Vector3f normal=new Vector3f(0f,0f,1f);
		QuadArray QuadArray1=new QuadArray(4,QuadArray.COORDINATES|QuadArray.NORMALS|QuadArray.TEXTURE_COORDINATE_2);
		QuadArray1.setCoordinate(3,p00);
		QuadArray1.setCoordinate(2,p01);
		QuadArray1.setCoordinate(1,p11);
		QuadArray1.setCoordinate(0,p10);
		QuadArray1.setTextureCoordinate(0,3,t00);
		QuadArray1.setTextureCoordinate(0,2,t01);
		QuadArray1.setTextureCoordinate(0,1,t11);
		QuadArray1.setTextureCoordinate(0,0,t10);
		QuadArray1.setNormal(3,normal);
		QuadArray1.setNormal(2,normal);
		QuadArray1.setNormal(1,normal);
		QuadArray1.setNormal(0,normal);
		this.setGeometry(QuadArray1);
		this.setAppearance(this.getImageComponent2DAppearance(imageName,false));
	}
	public static Appearance getImageComponent2DAppearance(String imageName,boolean hasMaterial)
	{
		Appearance imageComponent2DAppearance=new Appearance();
		int imageWidth=1024,imageHeight=1024;
		BufferedImage BufferedImage1=new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_ARGB);
		BufferedImage BufferedImage2=null;
		try{BufferedImage2=ImageIO.read(new File(imageName));}catch(Exception e){}
		double scaleX=(imageWidth+0.0)/BufferedImage2.getWidth();
		double scaleY=(imageHeight+0.0)/BufferedImage2.getHeight();
		AffineTransform AffineTransform1=new AffineTransform();
		AffineTransform1.setToScale(scaleX,scaleY);
		AffineTransformOp AffineTransformOp1=new AffineTransformOp(AffineTransform1,AffineTransformOp.TYPE_BILINEAR);
		BufferedImage BufferedImage3=AffineTransformOp1.filter(BufferedImage2,null);
		ImageComponent2D imageComponent2D=new ImageComponent2D(ImageComponent2D.FORMAT_RGBA,BufferedImage3);
		Texture2D texture2D=new Texture2D(Texture.BASE_LEVEL,Texture.RGBA,imageComponent2D.getWidth(),imageComponent2D.getHeight());
		texture2D.setImage(0,imageComponent2D);
		texture2D.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		texture2D.setMinFilter(Texture.BASE_LEVEL_LINEAR);
		imageComponent2DAppearance.setTexture(texture2D);
		TextureAttributes TextureAttributes1=new TextureAttributes();
		TextureAttributes1.setTextureMode(TextureAttributes.COMBINE);
		imageComponent2DAppearance.setTextureAttributes(TextureAttributes1);
		if(hasMaterial)imageComponent2DAppearance.setMaterial(new Material());
		return imageComponent2DAppearance;
	}
}