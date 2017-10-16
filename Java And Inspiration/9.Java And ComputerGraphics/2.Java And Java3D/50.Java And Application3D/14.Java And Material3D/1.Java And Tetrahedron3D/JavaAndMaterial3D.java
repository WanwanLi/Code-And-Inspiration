import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.image.*;
public class JavaAndMaterial3D
{
	public static void main(String[] args)
	{
		BranchGroup BranchGroup1=new BranchGroup();
		BoundingSphere BoundingSphere1=new BoundingSphere(new Point3d(0,0,0),300);
		Background Background1=new Background();
		Background1.setApplicationBounds(BoundingSphere1);
		BranchGroup1.addChild(Background1);
		Color3f color3f=new Color3f(1f,1f,1f);
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
		MouseTranslate MouseTranslate1=new MouseTranslate(TransformGroup1);
		MouseTranslate1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseTranslate1);
		MouseZoom MouseZoom1=new MouseZoom(TransformGroup1);
		MouseZoom1.setSchedulingBounds(BoundingSphere1);
		BranchGroup1.addChild(MouseZoom1);
		int row=128,column=128,level=128;double r=40;
		ColorSpace3D colorSpace3D=new ColorSpace3D(row,column,level);
		Color3f color0=new Color3f(160.0f/256,110.0f/256,70.0f/256);
		Color3f color1=new Color3f(230.0f/256,190.0f/256,140.0f/256);
		Wood3D wood3D=new Wood3D(color0,color1,-r,r,-r,r,-r,r,row,column,level);
		Color3f color2=new Color3f(175.0f/256,205.0f/256,210.0f/256);
		Color3f color3=new Color3f(0.0f/256,8.0f/256,5.0f/256);
		Marble3D marble3D=new Marble3D(color2,color3,-r,r,-r,r,-r,r,row,column,level);
		//TransformGroup1.addChild(new Tetrahedron3D(0.5f,colorSpace3D));
		//TransformGroup1.addChild(new Tetrahedron3D(0.5f,wood3D));
		TransformGroup1.addChild(new Tetrahedron3D(0.5f,marble3D));
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class ColorSpace3D implements Material3D
{
	public int row,column,level;
	public ColorSpace3D(int row,int column,int level)
	{
		this.row=row;
		this.column=column;
		this.level=level;
	} 
	public int getRGB(int X,int Y,int Z)
	{
		double z=255.0*Z/row;
		double x=255.0*X/column;
		double y=255.0*Y/level;
		int red=(int)z,green=(int)x,blue=(int)y;
		return (red<<16)|(green<<8)|blue;
	}
	public int getRow(){return this.row;}
	public int getColumn(){return this.column;}
	public int getLevel(){return this.level;}
}
class Wood3D implements Material3D
{
	public int m=50,n=50,l=50;
	public int row,column,level;
	public Color3f color0,color1;
	public double x0,x1,y0,y1,z0,z1,dx,dy,dz;
	public Wood3D(Color3f color0,Color3f color1,double x0,double x1,double y0,double y1,double z0,double z1,int row,int column,int level)
	{
		this.m=row;
		this.n=column;
		this.l=level;
		this.row=row;
		this.column=column;
		this.level=level;
		this.color0=color0;
		this.color1=color1;
		this.x0=x0;this.x1=x1;
		this.y0=y0;this.y1=y1;
		this.z0=z0;this.z1=z1;
		this.dx=(x1-x0)/(n-1);
		this.dy=(y1-y0)/(l-1);
		this.dz=(z1-z0)/(m-1);
	}
	public Color3f interpolate(Color3f color0,Color3f color1,float k)
	{
		float red=color0.x*(1-k)+color1.x*k;
		float green=color0.y*(1-k)+color1.y*k;
		float blue=color0.z*(1-k)+color1.z*k;
		return new Color3f(red,green,blue);
	}
	public int toRGB(Color3f color)
	{
		int red=(int)(color.x*255);
		int green=(int)(color.y*255);
		int blue=(int)(color.z*255);
		return (red<<16)|(green<<8)|blue;
	}
	public int getRGB(int X,int Y,int Z)
	{
		double x=x0+X*dx;
		double y=y0+Y*dy;
		double z=z0+Z*dz;
		double a=20,b=150,A=0.2;
		double t=Math.atan(z/x);
		double r=Math.sqrt(x*x+z*z);
		double dr=A*Math.sin(a*t+y/b);
		double k=0.5*Math.cos(r+dr)+0.5;
		return toRGB(interpolate(color0,color1,(float)k));
	}
	public int getRow(){return this.row;}
	public int getColumn(){return this.column;}
	public int getLevel(){return this.level;}
}
class Marble3D implements Material3D
{
	public int[][][] grids;
	public int[][] gradientVectors;
	public int l=40,m=40,n=40;
	public int row,column,level;
	public Color3f color0,color1;
	public double x0,x1,y0,y1,z0,z1,dx,dy,dz,dX,dY,dZ;
	public Marble3D(Color3f color0,Color3f color1,double x0,double x1,double y0,double y1,double z0,double z1,int row,int column,int level)
	{
		this.row=row;
		this.column=column;
		this.level=level;
		this.color0=color0;
		this.color1=color1;
		this.x0=x0;this.x1=x1;
		this.y0=y0;this.y1=y1;
		this.z0=z0;this.z1=z1;
		this.dx=(x1-x0)/(column-1);
		this.dy=(y1-y0)/(level-1);
		this.dz=(z1-z0)/(row-1);
		this.dX=(x1-x0)/(n-1);
		this.dY=(y1-y0)/(l-1);
		this.dZ=(z1-z0)/(m-1);
		this.initGrids();
		this.initGradientVectors();
	}
	public void initGrids()
	{
		this.grids=new int[l][m][n];
		for(int k=0;k<l;k++)
		{
			for(int i=0;i<m;i++)
			{
				for(int j=0;j<n;j++)
				{
					int d=(int)(12.0*Math.random());
					this.grids[k][i][j]=d<12?d:11;
				}
			}
		}
	}
	public void initGradientVectors()
	{
		this.gradientVectors=new int[][]
		{
			{1,1,0},{-1,1,0},{1,-1,0},{-1,-1,0},
			{1,0,1},{-1,0,1},{1,0,-1},{-1,0,-1},
			{0,1,1},{0,-1,1},{0,1,-1},{0,-1,-1}
		};
	}
	public Color3f interpolate(Color3f color0,Color3f color1,float k)
	{
		float red=color0.x*(1-k)+color1.x*k;
		float green=color0.y*(1-k)+color1.y*k;
		float blue=color0.z*(1-k)+color1.z*k;
		return new Color3f(red,green,blue);
	}
	public int toRGB(Color3f color)
	{
		int red=(int)(color.x*255);
		int green=(int)(color.y*255);
		int blue=(int)(color.z*255);
		return (red<<16)|(green<<8)|blue;
	}
	public double interpolate1d(double g0,double g1,double u)
	{
		return (1.0-u)*g0+u*g1;
	}
	public double interpolate2d(double g00,double g01,double g10,double g11,double u,double v)
	{
		double g0=this.interpolate1d(g00,g01,v);
		double g1=this.interpolate1d(g10,g11,v);
		return this.interpolate1d(g0,g1,u);
	}
	public double interpolate3d(double g000,double g001,double g010,double g011,double g100,double g101,double g110,double g111,double u,double v,double w)
	{
		double g0=this.interpolate2d(g000,g001,g010,g011,u,v);
		double g1=this.interpolate2d(g100,g101,g110,g111,u,v);
		return this.interpolate1d(g0,g1,w);
	}
	public double dotGradDist(int k0,int i0,int j0,double k,double i,double j)
	{
		int d=this.grids[k0][i0][j0];
		int gradX=this.gradientVectors[d][0];
		int gradY=this.gradientVectors[d][1];
		int gradZ=this.gradientVectors[d][2];
		double distX=j-j0,distY=k-k0,distZ=i-i0;
		double dot=gradX*distX+gradY*distY+gradZ*distZ;
		return 0.5+0.5*dot/(1.414*1.732);
	}
	public int getRGB(int X,int Y,int Z)
	{
		double j=X*dx/dX;
		double k=Y*dy/dY;
		double i=Z*dz/dZ;
		int j0=(int)j,j1=j0+1;
		int k0=(int)k,k1=k0+1;
		int i0=(int)i,i1=i0+1;
		double v=j-j0,w=k-k0,u=i-i0;
		if(i1>=m)i1=m-1;if(j1>=n)j1=n-1;if(k1>=l)k1=l-1;
		double g000=this.dotGradDist(k0,i0,j0,k,i,j);
		double g001=this.dotGradDist(k0,i0,j1,k,i,j);
		double g010=this.dotGradDist(k0,i1,j0,k,i,j);
		double g011=this.dotGradDist(k0,i1,j1,k,i,j);
		double g100=this.dotGradDist(k1,i0,j0,k,i,j);
		double g101=this.dotGradDist(k1,i0,j1,k,i,j);
		double g110=this.dotGradDist(k1,i1,j0,k,i,j);
		double g111=this.dotGradDist(k1,i1,j1,k,i,j);
		double g=this.interpolate3d(g000,g001,g010,g011,g100,g101,g110,g111,u,v,w);
		return toRGB(interpolate(color0,color1,(float)g));
	}
	public int getRow(){return this.row;}
	public int getColumn(){return this.column;}
	public int getLevel(){return this.level;}
}
interface Material3D
{
	public int getRow();
	public int getColumn();
	public int getLevel();
	public int getRGB(int x,int y,int z);
}
class Tetrahedron3D extends Shape3D
{
	public Tetrahedron3D(float r,Material3D material3D)
	{
		IndexedTriangleArray IndexedTriangleArray1=new IndexedTriangleArray(4,IndexedTriangleArray.COORDINATES|IndexedTriangleArray.TEXTURE_COORDINATE_3|IndexedTriangleArray.NORMALS,12);
		IndexedTriangleArray1.setCoordinate(0,new Point3f(0,0,0));
		IndexedTriangleArray1.setCoordinate(1,new Point3f(r,0,0));
		IndexedTriangleArray1.setCoordinate(2,new Point3f(0,r,0));
		IndexedTriangleArray1.setCoordinate(3,new Point3f(0,0,r));
		IndexedTriangleArray1.setTextureCoordinate(0,0,new TexCoord3f(0f,0f,0f));
		IndexedTriangleArray1.setTextureCoordinate(0,1,new TexCoord3f(1f,0f,0f));
		IndexedTriangleArray1.setTextureCoordinate(0,2,new TexCoord3f(0f,1f,0f));
		IndexedTriangleArray1.setTextureCoordinate(0,3,new TexCoord3f(0f,0f,1f));
		IndexedTriangleArray1.setNormal(0,new Vector3f(1/1.732f,1/1.732f,1/1.732f));
		IndexedTriangleArray1.setNormal(1,new Vector3f(-1,0,0));
		IndexedTriangleArray1.setNormal(2,new Vector3f(0,-1,0));
		IndexedTriangleArray1.setNormal(3,new Vector3f(0,0,-1));
		int[] coordinateIndices=new int[]
		{
			1,2,3,
			0,3,2,
			0,1,3,
			0,2,1
		};
		int[] normalIndices=new int[]
		{
			0,0,0,
			1,1,1,
			2,2,2,
			3,3,3
		};
		IndexedTriangleArray1.setCoordinateIndices(0,coordinateIndices);
		IndexedTriangleArray1.setTextureCoordinateIndices(0,0,coordinateIndices);
		IndexedTriangleArray1.setNormalIndices(0,normalIndices);
		this.setGeometry(IndexedTriangleArray1);
		this.setAppearance(this.getTexture3DAppearance(material3D));
	}
	private Appearance getTexture3DAppearance(Material3D material3D)
	{
		int row=material3D.getRow();
		int column=material3D.getColumn();
		int level=material3D.getLevel();
		BufferedImage[] BufferedImages=new BufferedImage[level];
		for(int z=0;z<level;z++)
		{
			BufferedImages[z]=new BufferedImage(column,row,BufferedImage.TYPE_INT_ARGB);
			for(int y=0;y<row;y++)
			{
				for(int x=0;x<column;x++)
				{
					int RGB=material3D.getRGB(x,y,z);
					BufferedImages[z].setRGB(x,(column-1)-y,RGB);
				}
			}
		}
		ImageComponent3D imageComponent3D=new ImageComponent3D(ImageComponent3D.FORMAT_RGB,BufferedImages);
		Texture3D texture3D=new Texture3D(Texture3D.BASE_LEVEL,Texture3D.RGBA,imageComponent3D.getWidth(),imageComponent3D.getHeight(),imageComponent3D.getDepth());
		texture3D.setImage(0,imageComponent3D);
		Appearance appearance=new Appearance();
		appearance.setMaterial(new Material());
		appearance.setTexture(texture3D);
		TextureAttributes textureAttributes=new TextureAttributes();
		textureAttributes.setTextureMode(TextureAttributes.COMBINE);
		appearance.setTextureAttributes(textureAttributes);
		return appearance;
	}
}