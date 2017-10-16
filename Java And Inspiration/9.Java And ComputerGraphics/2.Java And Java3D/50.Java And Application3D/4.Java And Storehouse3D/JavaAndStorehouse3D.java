import java.io.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.imageio.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;
public class JavaAndStorehouse3D
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
		//TransformGroup1.addChild(new ImageWalls3D(3f,2f,2f,images));
		//String textureImage=dir+"texture1.jpg";
		//TransformGroup2.addChild(new Box3D(1f,0.5f,0.5f,3,5,4,textureImage));

		Transform3D transform3D=new Transform3D();
		double scale=1;
		transform3D.setScale(new Vector3d(scale,scale,scale));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		TransformGroup2.addChild(new Storehouse3D());
		TransformGroup1.addChild(new Storehouse3D());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Image3D extends Shape3D
{
	public Image3D(float length,float width,String imageName)
	{
		Point3f p00=new Point3f(-length/2,-width/2,0);
		Point3f p01=new Point3f(-length/2,+width/2,0);
		Point3f p11=new Point3f(+length/2,+width/2,0);
		Point3f p10=new Point3f(+length/2,-width/2,0);
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
		imageComponent2DAppearance.setTexture(texture2D);
		TextureAttributes TextureAttributes1=new TextureAttributes();
		TextureAttributes1.setTextureMode(TextureAttributes.COMBINE);
		imageComponent2DAppearance.setTextureAttributes(TextureAttributes1);
		if(hasMaterial)imageComponent2DAppearance.setMaterial(new Material());
		return imageComponent2DAppearance;
	}
}
class ImageWalls3D extends TransformGroup
{
	public ImageWalls3D(float length,float width,float height,String[] images)
	{
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(0,0,-width/2.0));
		TransformGroup TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Image3D(length,height,images[0]));
		this.addChild(TransformGroup1);
		transform3D.rotY(Math.PI);
		transform3D.setTranslation(new Vector3d(0,0,width/2.0));
		TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Image3D(length,height,images[1]));
		this.addChild(TransformGroup1);
		transform3D.rotY(Math.PI/2);
		transform3D.setTranslation(new Vector3d(-length/2.0,0,0));
		TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Image3D(width,height,images[2]));
		this.addChild(TransformGroup1);
		transform3D.rotY(3*Math.PI/2);
		transform3D.setTranslation(new Vector3d(length/2.0,0,0));
		TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Image3D(width,height,images[3]));
		this.addChild(TransformGroup1);
		transform3D.rotX(-Math.PI/2);
		transform3D.setTranslation(new Vector3d(0,-height/2.0,0));
		TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Image3D(length,width,images[4]));
		this.addChild(TransformGroup1);
		transform3D.rotX(Math.PI/2);
		transform3D.setTranslation(new Vector3d(0,height/2.0,0));
		TransformGroup1=new TransformGroup(transform3D);
		TransformGroup1.addChild(new Image3D(length,width,images[5]));
		this.addChild(TransformGroup1);
	}
}
class Box3D extends Shape3D
{
	public Box3D(float length,float width,float height,int row,int column,int level,String textureImage)
	{
		float l=length/column;
		float w=width/row;
		float h=height/level;
		int QuadArrayNumber=2*level*column*4+2*level*row*4+2*row*column*4;
		QuadArray QuadArray1=new QuadArray(QuadArrayNumber,QuadArray.COORDINATES|QuadArray.NORMALS|QuadArray.TEXTURE_COORDINATE_2);
		int c=0,t=0,n=0;
		TexCoord2f t00=new TexCoord2f(0f,0f);
		TexCoord2f t01=new TexCoord2f(0f,1f);
		TexCoord2f t11=new TexCoord2f(1f,1f);
		TexCoord2f t10=new TexCoord2f(1f,0f);
		for(int i=0;i<level;i++)
		{
			for(int j=0;j<column;j++)
			{
				float x0=-length/2+j*l;
				float x1=-length/2+(j+1)*l;
				float y0=height/2-i*h;
				float y1=height/2-(i+1)*h;
				float z=-width/2;
				QuadArray1.setCoordinate(c++,new Point3f(x0,y0,z));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y0,z));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y1,z));
				QuadArray1.setCoordinate(c++,new Point3f(x0,y1,z));
				QuadArray1.setTextureCoordinate(0,t++,t00);
				QuadArray1.setTextureCoordinate(0,t++,t10);
				QuadArray1.setTextureCoordinate(0,t++,t11);
				QuadArray1.setTextureCoordinate(0,t++,t01);
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			}
		}
		for(int i=0;i<level;i++)
		{
			for(int j=0;j<column;j++)
			{
				float x0=-length/2+j*l;
				float x1=-length/2+(j+1)*l;
				float y0=height/2-i*h;
				float y1=height/2-(i+1)*h;
				float z=width/2;
				QuadArray1.setCoordinate(c++,new Point3f(x0,y0,z));
				QuadArray1.setCoordinate(c++,new Point3f(x0,y1,z));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y1,z));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y0,z));
				QuadArray1.setTextureCoordinate(0,t++,t00);
				QuadArray1.setTextureCoordinate(0,t++,t01);
				QuadArray1.setTextureCoordinate(0,t++,t11);
				QuadArray1.setTextureCoordinate(0,t++,t10);
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
				QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			}
		}
		for(int i=0;i<level;i++)
		{
			for(int j=0;j<row;j++)
			{
				float x=-length/2;
				float y0=height/2-i*h;
				float y1=height/2-(i+1)*h;
				float z0=width/2-j*w;
				float z1=width/2-(j+1)*w;
				QuadArray1.setCoordinate(c++,new Point3f(x,y0,z0));
				QuadArray1.setCoordinate(c++,new Point3f(x,y0,z1));
				QuadArray1.setCoordinate(c++,new Point3f(x,y1,z1));
				QuadArray1.setCoordinate(c++,new Point3f(x,y1,z0));
				QuadArray1.setTextureCoordinate(0,t++,t00);
				QuadArray1.setTextureCoordinate(0,t++,t01);
				QuadArray1.setTextureCoordinate(0,t++,t11);
				QuadArray1.setTextureCoordinate(0,t++,t10);
				QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
				QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
				QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
				QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
			}
		}
		for(int i=0;i<level;i++)
		{
			for(int j=0;j<row;j++)
			{
				float x=length/2;
				float y0=height/2-i*h;
				float y1=height/2-(i+1)*h;
				float z0=width/2-j*w;
				float z1=width/2-(j+1)*w;
				QuadArray1.setCoordinate(c++,new Point3f(x,y0,z0));
				QuadArray1.setCoordinate(c++,new Point3f(x,y1,z0));
				QuadArray1.setCoordinate(c++,new Point3f(x,y1,z1));
				QuadArray1.setCoordinate(c++,new Point3f(x,y0,z1));
				QuadArray1.setTextureCoordinate(0,t++,t00);
				QuadArray1.setTextureCoordinate(0,t++,t10);
				QuadArray1.setTextureCoordinate(0,t++,t11);
				QuadArray1.setTextureCoordinate(0,t++,t01);
				QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
				QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
				QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
				QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
			}
		}
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				float y=-height/2;
				float x0=-length/2+j*l;
				float x1=-length/2+(j+1)*l;
				float y1=height/2-(i+1)*h;
				float z0=-width/2+i*w;
				float z1=-width/2+(i+1)*w;
				QuadArray1.setCoordinate(c++,new Point3f(x0,y,z0));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y,z0));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y,z1));
				QuadArray1.setCoordinate(c++,new Point3f(x0,y,z1));
				QuadArray1.setTextureCoordinate(0,t++,t00);
				QuadArray1.setTextureCoordinate(0,t++,t01);
				QuadArray1.setTextureCoordinate(0,t++,t11);
				QuadArray1.setTextureCoordinate(0,t++,t10);
				QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
				QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
				QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
				QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
			}
		}
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				float y=height/2;
				float x0=-length/2+j*l;
				float x1=-length/2+(j+1)*l;
				float y1=height/2-(i+1)*h;
				float z0=-width/2+i*w;
				float z1=-width/2+(i+1)*w;
				QuadArray1.setCoordinate(c++,new Point3f(x0,y,z0));
				QuadArray1.setCoordinate(c++,new Point3f(x0,y,z1));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y,z1));
				QuadArray1.setCoordinate(c++,new Point3f(x1,y,z0));
				QuadArray1.setTextureCoordinate(0,t++,t00);
				QuadArray1.setTextureCoordinate(0,t++,t10);
				QuadArray1.setTextureCoordinate(0,t++,t11);
				QuadArray1.setTextureCoordinate(0,t++,t01);
				QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
				QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
				QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
				QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
			}
		}
		this.setGeometry(QuadArray1);
		this.setAppearance(Image3D.getImageComponent2DAppearance(textureImage,false));
	}
}
class Shelf3D extends Shape3D
{
	public Shelf3D(float length,float width,float height,int column,int level,String textureImage)
	{
		float l=length/column;
		float h=height/level;
		int k=10;
		float tl=l/k;
		float th=h/k;
		int QuadArrayNumber=4*(level+1)*4+4*(column+1)*4;
		QuadArray QuadArray1=new QuadArray(QuadArrayNumber,QuadArray.COORDINATES|QuadArray.NORMALS|QuadArray.TEXTURE_COORDINATE_2);
		int c=0,t=0,n=0;
		TexCoord2f t00=new TexCoord2f(0f,0f);
		TexCoord2f t01=new TexCoord2f(0f,1f);
		TexCoord2f t11=new TexCoord2f(1f,1f);
		TexCoord2f t10=new TexCoord2f(1f,0f);
		for(int i=0;i<=level;i++)
		{
			Point3f p00=new Point3f(-length/2,height/2-i*h+th,-width/2);
			Point3f p01=new Point3f(-length/2,height/2-i*h+th,+width/2);
			Point3f p11=new Point3f(+length/2+tl,height/2-i*h+th,+width/2);
			Point3f p10=new Point3f(+length/2+tl,height/2-i*h+th,-width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
			QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
			QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
			QuadArray1.setNormal(n++,new Vector3f(0f,1f,0f));
		}
		for(int i=0;i<=level;i++)
		{
			Point3f p00=new Point3f(-length/2,height/2-i*h,-width/2);
			Point3f p01=new Point3f(-length/2,height/2-i*h,+width/2);
			Point3f p11=new Point3f(+length/2+tl,height/2-i*h,+width/2);
			Point3f p10=new Point3f(+length/2+tl,height/2-i*h,-width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
			QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
			QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
			QuadArray1.setNormal(n++,new Vector3f(0f,-1f,0f));
		}
		for(int i=0;i<=level;i++)
		{
			Point3f p00=new Point3f(-length/2,height/2-i*h+th,width/2);
			Point3f p01=new Point3f(-length/2,height/2-i*h,width/2);
			Point3f p11=new Point3f(+length/2+tl,height/2-i*h,width/2);
			Point3f p10=new Point3f(+length/2+tl,height/2-i*h+th,width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
		}
		for(int i=0;i<=level;i++)
		{
			Point3f p00=new Point3f(-length/2,height/2-i*h+th,-width/2);
			Point3f p01=new Point3f(-length/2,height/2-i*h,-width/2);
			Point3f p11=new Point3f(+length/2+tl,height/2-i*h,-width/2);
			Point3f p10=new Point3f(+length/2+tl,height/2-i*h+th,-width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
		}
		for(int j=0;j<=column;j++)
		{
			Point3f p00=new Point3f(-length/2+j*l,-height/2,-width/2);
			Point3f p01=new Point3f(-length/2+j*l,-height/2,+width/2);
			Point3f p11=new Point3f(-length/2+j*l,+height/2+th,+width/2);
			Point3f p10=new Point3f(-length/2+j*l,+height/2+th,-width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
			QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
			QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
			QuadArray1.setNormal(n++,new Vector3f(-1f,0f,0f));
		}
		for(int j=0;j<=column;j++)
		{
			Point3f p00=new Point3f(-length/2+j*l+tl,-height/2,-width/2);
			Point3f p01=new Point3f(-length/2+j*l+tl,-height/2,+width/2);
			Point3f p11=new Point3f(-length/2+j*l+tl,+height/2+th,+width/2);
			Point3f p10=new Point3f(-length/2+j*l+tl,+height/2+th,-width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
			QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
			QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
			QuadArray1.setNormal(n++,new Vector3f(1f,0f,0f));
		}
		for(int j=0;j<=column;j++)
		{
			Point3f p00=new Point3f(-length/2+j*l+0,-height/2+th,-width/2);
			Point3f p01=new Point3f(-length/2+j*l+0,+height/2+th,-width/2);
			Point3f p11=new Point3f(-length/2+j*l+tl,+height/2+th,-width/2);
			Point3f p10=new Point3f(-length/2+j*l+tl,-height/2+th,-width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,-1f));
		}
		for(int j=0;j<=column;j++)
		{
			Point3f p00=new Point3f(-length/2+j*l+0,-height/2+th,width/2);
			Point3f p01=new Point3f(-length/2+j*l+0,+height/2+th,width/2);
			Point3f p11=new Point3f(-length/2+j*l+tl,+height/2+th,width/2);
			Point3f p10=new Point3f(-length/2+j*l+tl,-height/2+th,width/2);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setTextureCoordinate(0,t++,t00);
			QuadArray1.setTextureCoordinate(0,t++,t10);
			QuadArray1.setTextureCoordinate(0,t++,t11);
			QuadArray1.setTextureCoordinate(0,t++,t01);
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
			QuadArray1.setNormal(n++,new Vector3f(0f,0f,1f));
		}
		this.setGeometry(QuadArray1);
		this.setAppearance(Image3D.getImageComponent2DAppearance(textureImage,false));
	}
}
class Storehouse3D extends TransformGroup
{
	private float wallLength=20f;
	private float wallWidth=15f;
	private float wallHeight=10f;
	private float shelfLength=3f;
	private float shelfWidth=2f;
	private float shelfHeight=5f;
	private int shelfColumn=3;
	private int shelfLevel=5;
	private String dir="images\\";
	private String[] wallImages=new String[]{dir+"wall0.jpg",dir+"wall1.jpg",dir+"wall2.jpg",dir+"wall3.jpg",dir+"wall4.jpg",dir+"wall5.jpg"};
	private String shelfImage=dir+"shelf.jpg";
	private float intervalLength=2f;
	private float intervalWidth=1.5f;
	private int maxRow=(int)(wallWidth/(shelfWidth+intervalWidth));
	private int maxColumn=(int)(wallLength/(shelfLength+intervalLength));
	public Storehouse3D()
	{
		TransformGroup TransformGroup1=new TransformGroup();
		TransformGroup1.addChild(new ImageWalls3D(wallLength,wallWidth,wallHeight,wallImages));
		this.addChild(TransformGroup1);
		SharedGroup SharedGroup1=new SharedGroup();
		SharedGroup1.addChild(new Shelf3D(shelfLength,shelfWidth,shelfHeight,3,5,shelfImage));
		for(int i=0;i<maxRow;i++)
		{
			for(int j=0;j<maxColumn;j++)
			{
				float x=-wallLength/2+3*intervalLength/2+j*(shelfLength+intervalLength);
				float y=(shelfHeight-wallHeight)/2;
				float z=-wallWidth/2+3*intervalWidth/2+i*(shelfWidth+intervalWidth);
				Transform3D transform3D=new Transform3D();
				transform3D.setTranslation(new Vector3f(x,y,z));
				TransformGroup TransformGroup2=new TransformGroup(transform3D);
				TransformGroup2.addChild(new Link(SharedGroup1));
				this.addChild(TransformGroup2);
			}
		}
	}
}
/*
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

class Histogram3D extends TransformGroup
{
	private int number;
	private double edgeX,edgeY,edgeZ;
	private double length,width,height;
	private int row,column,level;
	private double minX,maxX,minY,maxY,minZ,maxZ,scaleX,scaleY,scaleZ;
	public Histogram3D(double length,double width,double height,int row,int column,int level)
	{
		this.length=length;
		this.width=width;
		this.height=height;
		this.row=row;
		this.column=column;
		this.level=level;
		this.minX=-1;
		this.maxX=1;
		this.minY=-1;
		this.maxY=1;
		this.minZ=-1;
		this.maxZ=1;
	}
	public void addGrid(Color gridColor)
	{
		TransformGroup grid=new TransformGroup();
		TransformGroup gridCoordinates=new TransformGroup();
		Appearance Appearance1=new Appearance();
		ColoringAttributes ColoringAttributes1=new ColoringAttributes();
		ColoringAttributes1.setColor(new Color3f(gridColor));
		Appearance1.setColoringAttributes(ColoringAttributes1);
		Appearance1.setMaterial(null);
		Font3D font3D=new Font3D(new Font("Microsoft Tai Le",Font.BOLD,1),null);
		double fontSize=0.2,fontEdge=0.3;
		Transform3D Transform3D_gridCoordinate=new Transform3D();
		double intervalX=length/column;
		double intervalY=height/level;
		double intervalZ=width/row;
		double coordinateIntervalX=(maxX-minX)/column;
		double coordinateIntervalY=(maxY-minY)/level;
		double coordinateIntervalZ=(maxZ-minZ)/row;
		int v=0,n=2*(row+1+column+1+level+1)*2;
		Point3d[] coordinates=new Point3d[n];
		Color3f[] colors=new Color3f[n];
		Transform3D_gridCoordinate.rotX(-Math.PI/2);
		for(int i=0;i<=row;i++)
		{
			double x0=-0.5*length;
			double x1=0.5*length;
			double y=-0.5*height;
			double z=-0.5*width+i*intervalZ;
			coordinates[v++]=new Point3d(x0,y,z);
			coordinates[v++]=new Point3d(x1,y,z);
			double coordinateZ=minZ+i*coordinateIntervalZ;
			String coordinate=format(coordinateZ);
			Transform3D_gridCoordinate.setScale(new Vector3d(fontSize*intervalZ,fontSize*intervalZ,0));
			Transform3D_gridCoordinate.setTranslation(new Vector3d(x1+fontEdge*intervalZ,y,z+fontSize*intervalZ));
			TransformGroup gridCoordinate=new TransformGroup(Transform3D_gridCoordinate);
			gridCoordinate.addChild(new Shape3D(new Text3D(font3D,coordinate),Appearance1));
			grid.addChild(gridCoordinate);
		}
		for(int j=0;j<=column;j++)
		{
			double x=-0.5*length+j*intervalX;
			double y=-0.5*height;
			double z0=-0.5*width;
			double z1=0.5*width;
			coordinates[v++]=new Point3d(x,y,z0);
			coordinates[v++]=new Point3d(x,y,z1);
			double coordinateX=minX+j*coordinateIntervalX;
			String coordinate=format(coordinateX);
			Transform3D_gridCoordinate.setScale(new Vector3d(fontSize*intervalX,fontSize*intervalX,0));
			Transform3D_gridCoordinate.setTranslation(new Vector3d(x-fontSize*intervalX,y,z1+fontEdge*intervalX));
			TransformGroup gridCoordinate=new TransformGroup(Transform3D_gridCoordinate);
			gridCoordinate.addChild(new Shape3D(new Text3D(font3D,coordinate),Appearance1));
			grid.addChild(gridCoordinate);
		}
		Transform3D_gridCoordinate.rotY(0);
		for(int j=0;j<=column;j++)
		{
			double x=-0.5*length+j*intervalX;
			double y0=-0.5*height;
			double y1=0.5*height;
			double z=-0.5*width;
			coordinates[v++]=new Point3d(x,y0,z);
			coordinates[v++]=new Point3d(x,y1,z);
			double coordinateX=minX+j*coordinateIntervalX;
			String coordinate=format(coordinateX);
			Transform3D_gridCoordinate.setScale(new Vector3d(fontSize*intervalX,fontSize*intervalX,0));
			Transform3D_gridCoordinate.setTranslation(new Vector3d(x-fontSize*intervalX,y1+fontEdge*intervalX,z));
			TransformGroup gridCoordinate=new TransformGroup(Transform3D_gridCoordinate);
			gridCoordinate.addChild(new Shape3D(new Text3D(font3D,coordinate),Appearance1));
			grid.addChild(gridCoordinate);
		}
		for(int k=0;k<=level;k++)
		{
			double x0=-0.5*length;
			double x1=0.5*length;
			double y=-0.5*height+k*intervalY;
			double z=-0.5*width;
			coordinates[v++]=new Point3d(x0,y,z);
			coordinates[v++]=new Point3d(x1,y,z);
			double coordinateY=minY+k*coordinateIntervalY;
			String coordinate=format(coordinateY);
			Transform3D_gridCoordinate.setScale(new Vector3d(fontSize*intervalY,fontSize*intervalY,0));
			Transform3D_gridCoordinate.setTranslation(new Vector3d(x1+fontEdge*intervalY,y,z));
			TransformGroup gridCoordinate=new TransformGroup(Transform3D_gridCoordinate);
			gridCoordinate.addChild(new Shape3D(new Text3D(font3D,coordinate),Appearance1));
			grid.addChild(gridCoordinate);
		}
		Transform3D_gridCoordinate.rotY(Math.PI/2);
		for(int k=0;k<=level;k++)
		{
			double x=-0.5*length;
			double y=-0.5*height+k*intervalY;
			double z0=-0.5*width;
			double z1=0.5*width;
			coordinates[v++]=new Point3d(x,y,z0);
			coordinates[v++]=new Point3d(x,y,z1);
			double coordinateY=minY+k*coordinateIntervalY;
			String coordinate=format(coordinateY);
			Transform3D_gridCoordinate.setScale(new Vector3d(fontSize*intervalY,fontSize*intervalY,0));
			Transform3D_gridCoordinate.setTranslation(new Vector3d(x,y,z1+2.5*fontEdge*intervalY));
			TransformGroup gridCoordinate=new TransformGroup(Transform3D_gridCoordinate);
			gridCoordinate.addChild(new Shape3D(new Text3D(font3D,coordinate),Appearance1));
			grid.addChild(gridCoordinate);
		}
		for(int i=0;i<=row;i++)
		{
			double x=-0.5*length;
			double y0=-0.5*height;
			double y1=0.5*height;
			double z=-0.5*width+i*intervalZ;
			coordinates[v++]=new Point3d(x,y0,z);
			coordinates[v++]=new Point3d(x,y1,z);
			double coordinateZ=minZ+i*coordinateIntervalZ;
			String coordinate=format(coordinateZ);
			Transform3D_gridCoordinate.setScale(new Vector3d(fontSize*intervalZ,fontSize*intervalZ,0));
			Transform3D_gridCoordinate.setTranslation(new Vector3d(x,y1+fontEdge*intervalZ,z+fontSize*intervalZ));
			TransformGroup gridCoordinate=new TransformGroup(Transform3D_gridCoordinate);
			gridCoordinate.addChild(new Shape3D(new Text3D(font3D,coordinate),Appearance1));
			grid.addChild(gridCoordinate);
		}
		for(int i=0;i<n;i++)colors[i]=new Color3f(gridColor);
		Shape3D gridLines=new Shape3D();
		LineArray LineArray1=new LineArray(n,LineArray.COORDINATES|LineArray.COLOR_3);
		LineArray1.setCoordinates(0,coordinates);
		LineArray1.setColors(0,colors);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(2.0f);
		LineAttributes1.setLineAntialiasingEnable(true);
		Appearance Appearance2=new Appearance();
		Appearance2.setLineAttributes(LineAttributes1);
		gridLines.setGeometry(LineArray1);
		gridLines.setAppearance(Appearance2);
		grid.addChild(gridLines);
		grid.addChild(gridCoordinates);
		this.addChild(grid);
	}
	public void setCoordinates(double[] x,double[] y,double[] z)
	{
		int len=x.length<y.length?x.length:y.length;
		this.number=len<z.length?len:z.length;
		for(int i=0;i<number;i++)
		{
			if(x[i]>maxX)this.maxX=x[i];
			if(x[i]<minX)this.minX=x[i];
			if(y[i]>maxY)this.maxY=y[i];
			if(y[i]<minY)this.minY=y[i];
			if(z[i]>maxZ)this.maxZ=z[i];
			if(z[i]<minZ)this.minZ=z[i];
		}
		this.scaleX=length/(maxX-minX);
		this.scaleY=height/(maxY-minY);
		this.scaleZ=width/(maxZ-minZ);
	}
	private double transformToCoordinateX(double xi)
	{
		return -0.5*length+(xi-minX)*scaleX;
	}
	private double transformToCoordinateY(double yi)
	{
		return -0.5*height+(yi-minY)*scaleY;
	}
	private double transformToCoordinateZ(double zi)
	{
		return -0.5*width+(zi-minZ)*scaleZ;
	}
	public void addColumns(double[] x,double[] y,double[] z,Color columnColor)
	{
		this.setCoordinates(x,y,z);
		int n=2*number,v=0;
		Point3d[] coordinates=new Point3d[n];
		Color3f[] colors=new Color3f[n];
		for(int i=0;i<number;i++)
		{
			double X=this.transformToCoordinateX(x[i]);
			double Y0=this.transformToCoordinateY(minY);
			double Y1=this.transformToCoordinateY(y[i]);
			double Z=this.transformToCoordinateZ(z[i]);
			coordinates[v++]=new Point3d(X,Y0,Z);
			coordinates[v++]=new Point3d(X,Y1,Z);
		}
		for(int i=0;i<n;i++)colors[i]=new Color3f(columnColor);
		Shape3D columns=new Shape3D();
		LineArray LineArray1=new LineArray(n,LineArray.COORDINATES|LineArray.COLOR_3);
		LineArray1.setCoordinates(0,coordinates);
		LineArray1.setColors(0,colors);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(2.0f);
		LineAttributes1.setLineAntialiasingEnable(true);
		Appearance Appearance1=new Appearance();
		Appearance1.setLineAttributes(LineAttributes1);
		columns.setGeometry(LineArray1);
		columns.setAppearance(Appearance1);
		this.addChild(columns);
	}
	public void addPoints(double[] x,double[] y,double[] z,Color pointColor)
	{
		this.setCoordinates(x,y,z);
		int n=number,v=0;
		Point3d[] coordinates=new Point3d[n];
		Color3f[] colors=new Color3f[n];
		for(int i=0;i<n;i++)
		{
			double X=this.transformToCoordinateX(x[i]);
			double Y=this.transformToCoordinateY(y[i]);
			double Z=this.transformToCoordinateZ(z[i]);
			coordinates[i]=new Point3d(X,Y,Z);
		}
		for(int i=0;i<n;i++)colors[i]=new Color3f(pointColor);
		Shape3D points=new Shape3D();
		PointArray PointArray1=new PointArray(n,PointArray.COORDINATES|PointArray.COLOR_3);
		PointArray1.setCoordinates(0,coordinates);
		PointArray1.setColors(0,colors);
		PointAttributes PointAttributes1=new PointAttributes();
		PointAttributes1.setPointSize(10.0f);
		PointAttributes1.setPointAntialiasingEnable(true);
		Appearance Appearance1=new Appearance();
		Appearance1.setPointAttributes(PointAttributes1);
		points.setGeometry(PointArray1);
		points.setAppearance(Appearance1);
		this.addChild(points);
	}
	public void addCurve(double[] x,double[] y,double[] z,Color curveColor)
	{
		this.setCoordinates(x,y,z);
		int n=2*(number-1),v=0;
		Point3d[] coordinates=new Point3d[n];
		Color3f[] colors=new Color3f[n];
		for(int i=0;i<number-1;i++)
		{
			double X0=this.transformToCoordinateX(x[i]);
			double Y0=this.transformToCoordinateY(y[i]);
			double Z0=this.transformToCoordinateZ(z[i]);
			double X1=this.transformToCoordinateX(x[i+1]);
			double Y1=this.transformToCoordinateY(y[i+1]);
			double Z1=this.transformToCoordinateZ(z[i+1]);
			coordinates[v++]=new Point3d(X0,Y0,Z0);
			coordinates[v++]=new Point3d(X1,Y1,Z1);
		}
		for(int i=0;i<n;i++)colors[i]=new Color3f(curveColor);
		LineArray LineArray1=new LineArray(n,LineArray.COORDINATES|LineArray.COLOR_3);
		LineArray1.setCoordinates(0,coordinates);
		LineArray1.setColors(0,colors);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(2.0f);
		LineAttributes1.setLineAntialiasingEnable(true);
		Appearance Appearance1=new Appearance();
		Appearance1.setLineAttributes(LineAttributes1);
		this.addChild(new Shape3D(LineArray1,Appearance1));
	}
	public void addSurface(double[] x,double[] y,double[] z,int row,int column,Color surfaceColor)
	{
		this.setCoordinates(x,y,z);
		Point3d[] coordinates=new Point3d[row*column];
		int[] coordinateIndices=new int[(row-1)*column*2];
		int[] stripCounts=new int[row-1];
		int v=0;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				double X=this.transformToCoordinateX(x[i*column+j]);
				double Y=this.transformToCoordinateY(y[i*column+j]);
				double Z=this.transformToCoordinateZ(z[i*column+j]);
				coordinates[v++]=new Point3d(X,Y,Z);
			}
		}
		v=0;
		for(int i=1;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				coordinateIndices[v++]=i*column+j;
				coordinateIndices[v++]=(i-1)*column+j;
			}
		}
		for(int i=0;i<row-1;i++)stripCounts[i]=2*column;
		GeometryInfo GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(surfaceColor));
		Appearance Appearance1=new Appearance();
		Appearance1.setMaterial(Material1);
		this.addChild(new Shape3D(GeometryInfo1.getGeometryArray(),Appearance1));
		v=0;
		for(int i=1;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				coordinateIndices[v++]=(i-1)*column+j;
				coordinateIndices[v++]=i*column+j;
			}
		}
		GeometryInfo1=new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		GeometryInfo1.setCoordinates(coordinates);
		GeometryInfo1.setCoordinateIndices(coordinateIndices);
		GeometryInfo1.setStripCounts(stripCounts);
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.addChild(new Shape3D(GeometryInfo1.getGeometryArray(),Appearance1));
	}
	public void addFrame(double[] x,double[] y,double[] z,int row,int column,Color frameColor)
	{
		this.setCoordinates(x,y,z);
		int n=(row-1)*(column-1)*4+(row-1)*2+(column-1)*2,v=0;
		Point3d[] coordinates=new Point3d[n];
		Color3f[] colors=new Color3f[n];
		for(int i=0;i<row-1;i++)
		{
			for(int j=0;j<column-1;j++)
			{
				double X00=this.transformToCoordinateX(x[(i+0)*column+(j+0)]);
				double Y00=this.transformToCoordinateY(y[(i+0)*column+(j+0)]);
				double Z00=this.transformToCoordinateZ(z[(i+0)*column+(j+0)]);
				double X01=this.transformToCoordinateX(x[(i+0)*column+(j+1)]);
				double Y01=this.transformToCoordinateY(y[(i+0)*column+(j+1)]);
				double Z01=this.transformToCoordinateZ(z[(i+0)*column+(j+1)]);
				double X10=this.transformToCoordinateX(x[(i+1)*column+(j+0)]);
				double Y10=this.transformToCoordinateY(y[(i+1)*column+(j+0)]);
				double Z10=this.transformToCoordinateZ(z[(i+1)*column+(j+0)]);
				coordinates[v++]=new Point3d(X00,Y00,Z00);
				coordinates[v++]=new Point3d(X01,Y01,Z01);
				coordinates[v++]=new Point3d(X00,Y00,Z00);
				coordinates[v++]=new Point3d(X10,Y10,Z10);
			}
		}
		for(int i=0;i<row-1;i++)
		{
				int j=column-1;
				double X00=this.transformToCoordinateX(x[(i+0)*column+(j+0)]);
				double Y00=this.transformToCoordinateY(y[(i+0)*column+(j+0)]);
				double Z00=this.transformToCoordinateZ(z[(i+0)*column+(j+0)]);
				double X10=this.transformToCoordinateX(x[(i+1)*column+(j+0)]);
				double Y10=this.transformToCoordinateY(y[(i+1)*column+(j+0)]);
				double Z10=this.transformToCoordinateZ(z[(i+1)*column+(j+0)]);
				coordinates[v++]=new Point3d(X00,Y00,Z00);
				coordinates[v++]=new Point3d(X10,Y10,Z10);
		}
		for(int j=0;j<column-1;j++)
		{
				int i=row-1;
				double X00=this.transformToCoordinateX(x[(i+0)*column+(j+0)]);
				double Y00=this.transformToCoordinateY(y[(i+0)*column+(j+0)]);
				double Z00=this.transformToCoordinateZ(z[(i+0)*column+(j+0)]);
				double X01=this.transformToCoordinateX(x[(i+0)*column+(j+1)]);
				double Y01=this.transformToCoordinateY(y[(i+0)*column+(j+1)]);
				double Z01=this.transformToCoordinateZ(z[(i+0)*column+(j+1)]);
				coordinates[v++]=new Point3d(X00,Y00,Z00);
				coordinates[v++]=new Point3d(X01,Y01,Z01);
		}
		for(int i=0;i<n;i++)colors[i]=new Color3f(frameColor);
		LineArray LineArray1=new LineArray(n,LineArray.COORDINATES|LineArray.COLOR_3);
		LineArray1.setCoordinates(0,coordinates);
		LineArray1.setColors(0,colors);
		LineAttributes LineAttributes1=new LineAttributes();
		LineAttributes1.setLineWidth(2.0f);
		LineAttributes1.setLineAntialiasingEnable(true);
		Appearance Appearance1=new Appearance();
		Appearance1.setLineAttributes(LineAttributes1);
		this.addChild(new Shape3D(LineArray1,Appearance1));
	}
	private static String format(double x)
	{
		if(Math.abs(x)<1.0E-3)return "0.0";
		String string=x+"";
		String formatString="";
		int i=0,j=0,len=string.length();
		while(i<len&&string.charAt(i)!='.')formatString+=string.charAt(i++);
		for(;j<4&&i<len;j++,i++)formatString+=string.charAt(i);
		return formatString;
	}
}
*/