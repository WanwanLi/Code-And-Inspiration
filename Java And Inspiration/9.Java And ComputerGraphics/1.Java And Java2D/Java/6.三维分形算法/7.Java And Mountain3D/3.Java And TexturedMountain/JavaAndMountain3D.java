import java.io.*;
import java.awt.*;
import java.applet.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.imageio.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.image.*;
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
		float ydim=1.8f;
		double Hurst=0.9;
		int maxDepth=8;
		Material Material1=new Material();
		Material1.setSpecularColor(new Color3f(Color.black));
		TransformGroup1.addChild(new Mountain3D("grass.jpg",xdim,ydim,zdim,Hurst,maxDepth,Material1));
		Transform3D transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3f(0f,ydim/10,0f));
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		Sphere  skySphere=new Sphere(2f,Sphere.GENERATE_NORMALS_INWARD|Sphere.GENERATE_TEXTURE_COORDS,100);
		skySphere.getAppearance().setTexture(new TextureLoader("sky.jpg",null).getTexture());
		TransformGroup2.addChild(skySphere);
		TransformGroup1.addChild(TransformGroup2);
		transform3D=new Transform3D();
		transform3D.rotX(-Math.PI/2);
		transform3D.setTranslation(new Vector3f(0f,ydim/40,0f));
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		TransformGroup3.addChild(new Image3D(xdim*2.5f,zdim*2.5f,"sea.jpg"));
		TransformGroup1.addChild(TransformGroup3);
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
	TexCoord2f[]  texCoords;
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
	private void getTextureCoordinates()
	{
		for(int i=0;i<m+1;i++)for(int j=0;j<m+1;j++)this.texCoords[i*(m+1)+j]=new TexCoord2f(i*1.0f/m,j*1.0f/m);
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
	public void filter(double[][] Filter)
	{
		int row=Filter.length;
		int column=Filter[0].length;
		if(row%2==0)row++;
		if(column%2==0)column++;
		int r=row/2,c=column/2;
		int width=m+1,height=m+1;
		Point3f[] newCoordinates=new Point3f[width*height];
		for(int i=r;i<height-r;i++)
		{
			for(int j=c;j<width-c;j++)
			{
				newCoordinates[i*width+j]=this.getFilterCoordinate(Filter,r,c,i,j);
			}
		}
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				if(newCoordinates[i*width+j]!=null)this.coordinates[i*width+j]=newCoordinates[i*width+j];
			}
		}
	}
	private Point3f  getFilterCoordinate(double[][] filter,int r,int c,int i,int j)
	{
		int width=m+1;
		double x=0,y=0,z=0,w=0;
		for(int u=i-r;u<=i+r;u++)
		{
			for(int v=j-c;v<=j+c;v++)
			{
				x+=this.coordinates[u*width+v].x*filter[u-(i-r)][v-(j-c)];
				y+=this.coordinates[u*width+v].y*filter[u-(i-r)][v-(j-c)];
				z+=this.coordinates[u*width+v].z*filter[u-(i-r)][v-(j-c)];
				w+=filter[u-(i-r)][v-(j-c)];
			}
		}
		return new Point3f((float)(x/w),(float)(y/w),(float)(z/w));
	}
	public double Gauss(double x,double y,double u1,double u2,double o1,double o2,double p)
	{
		double u=(x-u1)/o1;
		double v=(y-u2)/o2;
		double w=1-p*p;
		return Math.exp(-(u*u-2*p*u*v+v*v)/(2*w))/(2*Math.PI*o1*o2*Math.sqrt(w));
	}
	public double[][] getGaussFilter(double u1,double u2,double o1,double o2,double p,double zoom,int row,int column)
	{
		if(row%2==0)row++;
		if(column%2==0)column++;
		double maxX=zoom*o1;
		double minX=-zoom*o1;
		double maxY=zoom*o2;
		double minY=-zoom*o2;
		double dx=(maxX-minX)/(column-1);
		double dy=(maxY-minY)/(row-1);
		double[][] GaussFilter=new double[row][column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				double x=minX+j*dx;
				double y=minY+i*dy;
				GaussFilter[i][j]=Gauss(x,y,u1,u2,o1,o2,p);
			}
		}
		return GaussFilter;
	}
	public Mountain3D(String imageName,float xdim,float ydim,float zdim,double Hurst,int maxDepth,Material material)
	{
		this.MaxDepth=maxDepth;
		this.decay=(float)(1.0/Math.pow(2,Hurst));
		this.m=2<<MaxDepth;
		this.coordinates=new Point3f[(m+1)*(m+1)];
		this.texCoords=new TexCoord2f[(m+1)*(m+1)];
		this.coordinateIndices=new int[4*m*m];
		this.coordinates[0*(m+1)+0]=new Point3f(-xdim/2,0f,-zdim/2);
		this.coordinates[0*(m+1)+m]=new Point3f(xdim/2,0f,-zdim/2);
		this.coordinates[m*(m+1)+m]=new Point3f(xdim/2,0f,zdim/2);
		this.coordinates[m*(m+1)+0]=new Point3f(-xdim/2,0f,zdim/2);
		this.getCoordinates(ydim);
		this.getTextureCoordinates();
		this.getCoordinateIndices();
		this.filter(this.getGaussFilter(0,0,2,2,0,0.15,5,5));
		IndexedQuadArray IndexedQuadArray1=new IndexedQuadArray((m+1)*(m+1),IndexedQuadArray.COORDINATES|IndexedQuadArray.NORMALS|IndexedQuadArray.TEXTURE_COORDINATE_2,4*m*m);
		IndexedQuadArray1.setCoordinates(0,coordinates);
		IndexedQuadArray1.setCoordinateIndices(0,coordinateIndices);
		IndexedQuadArray1.setTextureCoordinates(0,0,texCoords);
		IndexedQuadArray1.setTextureCoordinateIndices(0,0,coordinateIndices);
		GeometryInfo GeometryInfo1=new GeometryInfo(IndexedQuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(this.getImageComponent2DAppearance(imageName,material));
	}
	public static Appearance getImageComponent2DAppearance(String imageName,Material material)
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
		imageComponent2DAppearance.setMaterial(material);
		return imageComponent2DAppearance;
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
		imageComponent2DAppearance.setTexture(texture2D);
		TextureAttributes TextureAttributes1=new TextureAttributes();
		TextureAttributes1.setTextureMode(TextureAttributes.COMBINE);
		imageComponent2DAppearance.setTextureAttributes(TextureAttributes1);
		if(hasMaterial)imageComponent2DAppearance.setMaterial(new Material());
		return imageComponent2DAppearance;
	}
}
