import java.io.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.imageio.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
public class JavaAndReliefTexture3D
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
		double radius=0.12;
		double height=0.6;
		String imageName="Image.jpg";
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
//		TransformGroup1.addChild(new Cylinder_ReliefTexture3D(radius,height,Appearance1,imageName));
		TransformGroup1.addChild(new Cylinder_ReliefTexture3D(radius,height,null,imageName));
//		TransformGroup1.addChild(new Quad_ReliefTexture3D());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Cylinder_ReliefTexture3D extends TransformGroup
{
	public Cylinder_ReliefTexture3D(double radius,double height,Appearance appearance,String imageName)
	{
		int index=0;
		int column=100;
		double imagePrecision=0.8;
		double imageThickness=0.01;
		if(appearance==null)imageThickness=3;
		double angle=2.0*Math.PI/column;
		Point3d[] coordinates=new Point3d[column*4];
		Vector3d[] normals=new Vector3d[column*4];
		TexCoord2d[] texCoords=new TexCoord2d[column*4];
		QuadArray quadArray_Bottom=new QuadArray(column*8,QuadArray.COORDINATES|QuadArray.NORMALS);
		Point3d p0C=new Point3d(0,height*0.5,0);
		Point3d p1C=new Point3d(0,-height*0.5,0);
		for(int i=0;i<column;i++)
		{
			double w0=angle*(i+0)-Math.PI;
			double w1=angle*(i+1)-Math.PI;
			double c0=(i+0.0)/column;
			double c1=(i+1.0)/column;
			double x0=radius*Math.cos(w0);
			double x1=radius*Math.cos(w1);
			double y0=height*0.5;
			double y1=-height*0.5;
			double z0=-radius*Math.sin(w0);
			double z1=-radius*Math.sin(w1);
			Point3d p00=new Point3d(x0,y0,z0);
			Point3d p10=new Point3d(x0,y1,z0);
			Point3d p11=new Point3d(x1,y1,z1);
			Point3d p01=new Point3d(x1,y0,z1);
			Vector3d v0=new Vector3d(x0,0,z0);
			Vector3d v1=new Vector3d(x1,0,z1);
			coordinates[i*4+0]=p00;
			coordinates[i*4+1]=p10;
			coordinates[i*4+2]=p11;
			coordinates[i*4+3]=p01;
			normals[i*4+0]=v0;
			normals[i*4+1]=v0;
			normals[i*4+2]=v1;
			normals[i*4+3]=v1;
			texCoords[i*4+0]=new TexCoord2d(c0,0);
			texCoords[i*4+1]=new TexCoord2d(c0,1);
			texCoords[i*4+2]=new TexCoord2d(c1,1);
			texCoords[i*4+3]=new TexCoord2d(c1,0);
			quadArray_Bottom.setCoordinate(index+0,p00);
			quadArray_Bottom.setCoordinate(index+1,p01);
			quadArray_Bottom.setCoordinate(index+2,p0C);
			quadArray_Bottom.setCoordinate(index+3,p00);
			quadArray_Bottom.setCoordinate(index+4,p11);
			quadArray_Bottom.setCoordinate(index+5,p10);
			quadArray_Bottom.setCoordinate(index+6,p1C);
			quadArray_Bottom.setCoordinate(index+7,p11);
			index+=8;
		}
		if(appearance!=null)this.addChild(new ReliefTexture3D(coordinates,normals,texCoords,appearance,imageName,imagePrecision,imageThickness));
		else this.addChild(new ReliefTexture3D(coordinates,normals,texCoords,imageName,imagePrecision,imageThickness));
		GeometryInfo GeometryInfo1=new GeometryInfo(quadArray_Bottom);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.addChild(new Shape3D(GeometryInfo1.getGeometryArray(),appearance));
	}
}
class Quad_ReliefTexture3D extends TransformGroup
{
	public Quad_ReliefTexture3D()
	{
		Point3d[] coordinates=new Point3d[]
		{
			new Point3d(-0.4,0.6,0.1),
			new Point3d(-0.4,-0.6,0.1),
			new Point3d(0.4,-0.6,-0.1),
			new Point3d(0.4,0.6,-0.1)
		};
		Vector3d[] normals=new Vector3d[]
		{
			new Vector3d(0,0,1),
			new Vector3d(0,0,1),
			new Vector3d(0,0,1),
			new Vector3d(0,0,1)
		};
		TexCoord2d[] texCoords=new TexCoord2d[]
		{
			new TexCoord2d(0,0),
			new TexCoord2d(0,1),
			new TexCoord2d(1,1),
			new TexCoord2d(1,0)
		};
		String imageName="Image.jpg";
		double imagePrecision=0.8;
		double imageThickness=0.01;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(Color.orange));
		Appearance1.setMaterial(Material1);
		this.addChild(new ReliefTexture3D(coordinates,normals,texCoords,Appearance1,imageName,imagePrecision,imageThickness));
	}
}
class ReliefTexture3D extends Shape3D
{
	private int[] pixels;
	private int imageWidth;
	private int imageHeight;
	private double imagePrecision;
	private double imageThickness;
	private double[] hue,saturation,brightness;
	public ReliefTexture3D(Point3d[] coordinates,Vector3d[] normals,TexCoord2d[] texCoords,Appearance appearance,String imageName,double imagePrecision,double imageThickness)
	{
		this.getReliefImage(imageName);
		this.getGrayPixels(1,1,1);
		this.imagePrecision=imagePrecision;
		this.imageThickness=imageThickness;
		int l=coordinates.length/4;
		int coordinatesLength=this.getCoordinatesLength(texCoords);
		QuadArray QuadArray1=new QuadArray(coordinatesLength,QuadArray.COORDINATES|QuadArray.NORMALS);
		int index=0;
		for(int i=0;i<l;i++)
		{
			int[] rowColumn=this.getTexCoordRowColumn(texCoords[i*4+0],texCoords[i*4+1],texCoords[i*4+2],texCoords[i*4+3]);
			int row=rowColumn[0],column=rowColumn[1];
			QuadArray1.setCoordinates(index,this.getQuadReliefCoordinates(coordinates[i*4+0],coordinates[i*4+1],coordinates[i*4+2],coordinates[i*4+3],normals[i*4+0],normals[i*4+1],normals[i*4+2],normals[i*4+3],texCoords[i*4+0],texCoords[i*4+1],texCoords[i*4+2],texCoords[i*4+3],row,column));
			index+=(row-1)*(column-1)*4;
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(QuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
	private Point3d[] quadReliefCoordinates;
	private Color3f[] quadReliefColors;
	public ReliefTexture3D(Point3d[] coordinates,Vector3d[] normals,TexCoord2d[] texCoords,String imageName,double imagePrecision,double imageThickness)
	{
		this.getReliefImage(imageName);
		this.getHueAndBrightness();
		this.imagePrecision=imagePrecision;
		this.imageThickness=imageThickness;
		int l=coordinates.length/4;
		int coordinatesLength=this.getCoordinatesLength(texCoords);
		QuadArray QuadArray1=new QuadArray(coordinatesLength,QuadArray.COORDINATES|IndexedQuadArray.COLOR_3);
		int index=0;
		for(int i=0;i<l;i++)
		{
			int[] rowColumn=this.getTexCoordRowColumn(texCoords[i*4+0],texCoords[i*4+1],texCoords[i*4+2],texCoords[i*4+3]);
			int row=rowColumn[0],column=rowColumn[1];
			this.getQuadReliefCoordinatesAndColors(coordinates[i*4+0],coordinates[i*4+1],coordinates[i*4+2],coordinates[i*4+3],normals[i*4+0],normals[i*4+1],normals[i*4+2],normals[i*4+3],texCoords[i*4+0],texCoords[i*4+1],texCoords[i*4+2],texCoords[i*4+3],row,column);
			QuadArray1.setCoordinates(index,quadReliefCoordinates);
			QuadArray1.setColors(index,quadReliefColors);
			index+=(row-1)*(column-1)*4;
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(QuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		Appearance appearance=new Appearance();
		appearance.setMaterial(new Material());
		this.setAppearance(appearance);
	}
	private int getCoordinatesLength(TexCoord2d[] texCoords)
	{
		int coordinatesLength=0;
		int l=texCoords.length/4;
		for(int i=0;i<l;i++)
		{
			int[] rowColumn=this.getTexCoordRowColumn(texCoords[i*4+0],texCoords[i*4+1],texCoords[i*4+2],texCoords[i*4+3]);
			coordinatesLength+=(rowColumn[0]-1)*(rowColumn[1]-1)*4;
		}
		return coordinatesLength;
	}
	private int[] getTexCoordRowColumn(TexCoord2d t00,TexCoord2d t10,TexCoord2d t11,TexCoord2d t01)
	{
		double r=imageHeight*imagePrecision*abs(t10.y-t00.y);
		double c=imageWidth*imagePrecision*abs(t01.x-t00.x);
		int row=(int)r+1;
		int column=(int)c+1;
		if(row<2)row=2;
		if(column<2)column=2;
		return new int[]{row,column};
	}
	private Point3d[] getQuadReliefCoordinates(Point3d p00,Point3d p10,Point3d p11,Point3d p01,Vector3d v00,Vector3d v10,Vector3d v11,Vector3d v01,TexCoord2d t00,TexCoord2d t10,TexCoord2d t11,TexCoord2d t01,int row,int column)
	{
		Point3d[] quadReliefCoordinates=new Point3d[(row-1)*(column-1)*4];
		int index=0;
		for(int i=0;i<row-1;i++)
		{
			for(int j=0;j<column-1;j++)
			{
				Point3d P00=this.getBilinearInterpolatedPoint(p00,p10,p11,p01,row,column,i+0,j+0);
				Point3d P10=this.getBilinearInterpolatedPoint(p00,p10,p11,p01,row,column,i+1,j+0);
				Point3d P11=this.getBilinearInterpolatedPoint(p00,p10,p11,p01,row,column,i+1,j+1);
				Point3d P01=this.getBilinearInterpolatedPoint(p00,p10,p11,p01,row,column,i+0,j+1);
				Vector3d V00=this.getBilinearInterpolatedVector(v00,v10,v11,v01,row,column,i+0,j+0);
				Vector3d V10=this.getBilinearInterpolatedVector(v00,v10,v11,v01,row,column,i+1,j+0);
				Vector3d V11=this.getBilinearInterpolatedVector(v00,v10,v11,v01,row,column,i+1,j+1);
				Vector3d V01=this.getBilinearInterpolatedVector(v00,v10,v11,v01,row,column,i+0,j+1);
				TexCoord2d T00=this.getBilinearInterpolatedTexCoord(t00,t10,t11,t01,row,column,i+0,j+0);
				TexCoord2d T10=this.getBilinearInterpolatedTexCoord(t00,t10,t11,t01,row,column,i+1,j+0);
				TexCoord2d T11=this.getBilinearInterpolatedTexCoord(t00,t10,t11,t01,row,column,i+1,j+1);
				TexCoord2d T01=this.getBilinearInterpolatedTexCoord(t00,t10,t11,t01,row,column,i+0,j+1);
				quadReliefCoordinates[index++]=this.getReliefCoordinate_Gray(P00,V00,T00);
				quadReliefCoordinates[index++]=this.getReliefCoordinate_Gray(P10,V10,T10);
				quadReliefCoordinates[index++]=this.getReliefCoordinate_Gray(P11,V11,T11);
				quadReliefCoordinates[index++]=this.getReliefCoordinate_Gray(P01,V01,T01);
			}
		}
		return quadReliefCoordinates;
	}
	private void getQuadReliefCoordinatesAndColors(Point3d p00,Point3d p10,Point3d p11,Point3d p01,Vector3d v00,Vector3d v10,Vector3d v11,Vector3d v01,TexCoord2d t00,TexCoord2d t10,TexCoord2d t11,TexCoord2d t01,int row,int column)
	{
		this.quadReliefCoordinates=new Point3d[(row-1)*(column-1)*4];
		this.quadReliefColors=new Color3f[(row-1)*(column-1)*4];
		int index=0;
		for(int i=0;i<row-1;i++)
		{
			for(int j=0;j<column-1;j++)
			{
				Point3d P00=this.getBilinearInterpolatedPoint(p00,p10,p11,p01,row,column,i+0,j+0);
				Point3d P10=this.getBilinearInterpolatedPoint(p00,p10,p11,p01,row,column,i+1,j+0);
				Point3d P11=this.getBilinearInterpolatedPoint(p00,p10,p11,p01,row,column,i+1,j+1);
				Point3d P01=this.getBilinearInterpolatedPoint(p00,p10,p11,p01,row,column,i+0,j+1);
				Vector3d V00=this.getBilinearInterpolatedVector(v00,v10,v11,v01,row,column,i+0,j+0);
				Vector3d V10=this.getBilinearInterpolatedVector(v00,v10,v11,v01,row,column,i+1,j+0);
				Vector3d V11=this.getBilinearInterpolatedVector(v00,v10,v11,v01,row,column,i+1,j+1);
				Vector3d V01=this.getBilinearInterpolatedVector(v00,v10,v11,v01,row,column,i+0,j+1);
				TexCoord2d T00=this.getBilinearInterpolatedTexCoord(t00,t10,t11,t01,row,column,i+0,j+0);
				TexCoord2d T10=this.getBilinearInterpolatedTexCoord(t00,t10,t11,t01,row,column,i+1,j+0);
				TexCoord2d T11=this.getBilinearInterpolatedTexCoord(t00,t10,t11,t01,row,column,i+1,j+1);
				TexCoord2d T01=this.getBilinearInterpolatedTexCoord(t00,t10,t11,t01,row,column,i+0,j+1);
				quadReliefCoordinates[index+0]=this.getReliefCoordinate(P00,V00,T00);
				quadReliefCoordinates[index+1]=this.getReliefCoordinate(P10,V10,T10);
				quadReliefCoordinates[index+2]=this.getReliefCoordinate(P11,V11,T11);
				quadReliefCoordinates[index+3]=this.getReliefCoordinate(P01,V01,T01);
				quadReliefColors[index++]=this.getReliefColor(T00);
				quadReliefColors[index++]=this.getReliefColor(T10);
				quadReliefColors[index++]=this.getReliefColor(T11);
				quadReliefColors[index++]=this.getReliefColor(T01);
			}
		}
	}
	private Point3d getReliefCoordinate(Point3d p,Vector3d v,TexCoord2d t)
	{
		double x=t.x*imageWidth;
		double y=t.y*imageHeight;
		double b=this.getBilinearInterpolatedBrightness(x,y);
		double k=b/(255.0)*imageThickness;
		return MOV(p,MUL(unit(v),k));
	}
	private Color3f getReliefColor(TexCoord2d t)
	{
		double x=t.x*imageWidth;
		double y=t.y*imageHeight;
		double h=this.getBilinearInterpolatedHue(x,y);
		double s=this.getBilinearInterpolatedSaturation(x,y);
		double b=this.getBilinearInterpolatedBrightness(x,y);
		return ColorSpace.getRGBfromHSB(h,s,b);
	}
	private Point3d getReliefCoordinate_Gray(Point3d p,Vector3d v,TexCoord2d t)
	{
		double x=t.x*imageWidth;
		double y=t.y*imageHeight;
		double g=this.getBilinearInterpolatedGray(x,y);
		double k=g/(255.0)*imageThickness;
		return MOV(p,MUL(unit(v),k));
	}
	private Point3d getBilinearInterpolatedPoint(Point3d p00,Point3d p10,Point3d p11,Point3d p01,int row,int column,int i,int j)
	{
		double U=(i+0.0)/(row-1.0);
		double V=(j+0.0)/(column-1.0);
		Point3d pU0=ADD(MUL(p00,1-U),MUL(p10,U));
		Point3d pU1=ADD(MUL(p01,1-U),MUL(p11,U));
		Point3d pUV=ADD(MUL(pU0,1-V),MUL(pU1,V));
		return pUV;
	}
	private Vector3d getBilinearInterpolatedVector(Vector3d v00,Vector3d v10,Vector3d v11,Vector3d v01,int row,int column,int i,int j)
	{
		double U=(i+0.0)/(row-1.0);
		double V=(j+0.0)/(column-1.0);
		Vector3d vU0=ADD(MUL(v00,1-U),MUL(v10,U));
		Vector3d vU1=ADD(MUL(v01,1-U),MUL(v11,U));
		Vector3d vUV=ADD(MUL(vU0,1-V),MUL(vU1,V));
		return vUV;
	}
	private TexCoord2d getBilinearInterpolatedTexCoord(TexCoord2d t00,TexCoord2d t10,TexCoord2d t11,TexCoord2d t01,int row,int column,int i,int j)
	{
		double U=(i+0.0)/(row-1.0);
		double V=(j+0.0)/(column-1.0);
		TexCoord2d tU0=ADD(MUL(t00,1-U),MUL(t10,U));
		TexCoord2d tU1=ADD(MUL(t01,1-U),MUL(t11,U));
		TexCoord2d tUV=ADD(MUL(tU0,1-V),MUL(tU1,V));
		return tUV;
	}
	private double getBilinearInterpolatedGray(double x,double y)
	{
		int j0=(int)x;
		int j1=j0+1;
		int i0=(int)y;
		int i1=i0+1;
		if(i1>=imageHeight||j1>=imageWidth||i0>=imageHeight||j0>=imageWidth)return 0;
		int g00=this.pixels[i0*imageWidth+j0];
		int g01=this.pixels[i0*imageWidth+j1];
		int g11=this.pixels[i1*imageWidth+j1];
		int g10=this.pixels[i1*imageWidth+j0];
		double U=(y-i0)/1.0;
		double V=(x-j0)/1.0;
		double gU0=g00*(1-U)+g10*U;
		double gU1=g01*(1-U)+g11*U;
		double gUV=gU0*(1-V)+gU1*V;
		return gUV;
	}
	private double getBilinearInterpolatedHue(double x,double y)
	{
		int j0=(int)x;
		int j1=j0+1;
		int i0=(int)y;
		int i1=i0+1;
		if(i1>=imageHeight||j1>=imageWidth||i0>=imageHeight||j0>=imageWidth)return 0;
		double h00=this.hue[i0*imageWidth+j0];
		double h01=this.hue[i0*imageWidth+j1];
		double h11=this.hue[i1*imageWidth+j1];
		double h10=this.hue[i1*imageWidth+j0];
		double U=(y-i0)/1.0;
		double V=(x-j0)/1.0;
		double hU0=h00*(1-U)+h10*U;
		double hU1=h01*(1-U)+h11*U;
		double hUV=hU0*(1-V)+hU1*V;
		return hUV;
	}
	private double getBilinearInterpolatedSaturation(double x,double y)
	{
		int j0=(int)x;
		int j1=j0+1;
		int i0=(int)y;
		int i1=i0+1;
		if(i1>=imageHeight||j1>=imageWidth||i0>=imageHeight||j0>=imageWidth)return 0;
		double s00=this.saturation[i0*imageWidth+j0];
		double s01=this.saturation[i0*imageWidth+j1];
		double s11=this.saturation[i1*imageWidth+j1];
		double s10=this.saturation[i1*imageWidth+j0];
		double U=(y-i0)/1.0;
		double V=(x-j0)/1.0;
		double sU0=s00*(1-U)+s10*U;
		double sU1=s01*(1-U)+s11*U;
		double sUV=sU0*(1-V)+sU1*V;
		return sUV;
	}
	private double getBilinearInterpolatedBrightness(double x,double y)
	{
		int j0=(int)x;
		int j1=j0+1;
		int i0=(int)y;
		int i1=i0+1;
		if(i1>=imageHeight||j1>=imageWidth||i0>=imageHeight||j0>=imageWidth)return 0;
		double b00=this.brightness[i0*imageWidth+j0];
		double b01=this.brightness[i0*imageWidth+j1];
		double b11=this.brightness[i1*imageWidth+j1];
		double b10=this.brightness[i1*imageWidth+j0];
		double U=(y-i0)/1.0;
		double V=(x-j0)/1.0;
		double bU0=b00*(1-U)+b10*U;
		double bU1=b01*(1-U)+b11*U;
		double bUV=bU0*(1-V)+bU1*V;
		return bUV;
	}
	private Point3d MUL(Point3d p,double k)
	{
		return new Point3d(p.x*k,p.y*k,p.z*k);
	}
	private Point3d ADD(Point3d p0,Point3d p1)
	{
		return new Point3d(p0.x+p1.x,p0.y+p1.y,p0.z+p1.z);
	}
	private Point3d MOV(Point3d p,Vector3d v)
	{
		return new Point3d(p.x+v.x,p.y+v.y,p.z+v.z);
	}
	private Vector3d MUL(Vector3d v,double k)
	{
		return new Vector3d(v.x*k,v.y*k,v.z*k);
	}
	private Vector3d ADD(Vector3d v0,Vector3d v1)
	{
		return new Vector3d(v0.x+v1.x,v0.y+v1.y,v0.z+v1.z);
	}
	private Vector3d unit(Vector3d v)
	{
		double l=Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
		return new Vector3d(v.x/l,v.y/l,v.z/l);
	}
	private TexCoord2d MUL(TexCoord2d t,double k)
	{
		return new TexCoord2d(t.x*k,t.y*k);
	}
	private TexCoord2d ADD(TexCoord2d t0,TexCoord2d t1)
	{
		return new TexCoord2d(t0.x+t1.x,t0.y+t1.y);
	}
	private void getReliefImage(String imageName)
	{
		try
		{
			Frame Frame1=new Frame();
			Image Image1=Toolkit.getDefaultToolkit().getImage(imageName);
			MediaTracker MediaTracker1=new MediaTracker(Frame1);
			MediaTracker1.addImage(Image1,0);
			MediaTracker1.waitForID(0);
			this.imageWidth=Image1.getWidth(Frame1);
			this.imageHeight=Image1.getHeight(Frame1);
			this.pixels=new int[imageWidth*imageHeight];
			PixelGrabber PixelGrabber1=new PixelGrabber(Image1,0,0,imageWidth,imageHeight,pixels,0,imageWidth);
			PixelGrabber1.grabPixels();
		}
		catch(Exception e){System.err.println(e);}
	}
	private void getGrayPixels(double r,double g,double b)
	{
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{

				int alpha=this.getAlpha(pixels[i*imageWidth+j]);
				int red=this.getRed(pixels[i*imageWidth+j]);
				int green=this.getGreen(pixels[i*imageWidth+j]);
				int blue=this.getBlue(pixels[i*imageWidth+j]);
				int gray=(int)((r*red+g*green+b*blue)/(r+g+b));
				this.pixels[i*imageWidth+j]=gray;
			}
		}
	}
	private void getHueAndBrightness()
	{
		this.hue=new double[imageHeight*imageWidth];
		this.saturation=new double[imageHeight*imageWidth];
		this.brightness=new double[imageHeight*imageWidth];
		for(int i=0;i<imageHeight;i++)
		{
			for(int j=0;j<imageWidth;j++)
			{
				int alpha=this.getAlpha(pixels[i*imageWidth+j]);
				int red=this.getRed(pixels[i*imageWidth+j]);
				int green=this.getGreen(pixels[i*imageWidth+j]);
				int blue=this.getBlue(pixels[i*imageWidth+j]);
				double[] HSB=ColorSpace.getHSBfromRGB(red,green,blue);
				this.hue[i*imageWidth+j]=HSB[0];
				this.saturation[i*imageWidth+j]=HSB[1];
				this.brightness[i*imageWidth+j]=HSB[2];
			}
		}
	}
	private int getAlpha(int pixel)
	{
		return pixel>>24;
	}
	private int getRed(int pixel)
	{
		return (pixel>>16)&0xff;
	}
	private int getGreen(int pixel)
	{
		return (pixel>>8)&0xff;
	}
	private int getBlue(int pixel)
	{
		return pixel&0xff;
	}
	private double abs(double x)
	{
		return (x<0?-x:x);
	}
	private int round(double x)
	{
		int x0=(int)x;
		double dx=x-x0;
		return (dx>0.4?(x0+1):x0);
	}
}
class TexCoord2d
{
	public double x;
	public double y;
	public TexCoord2d(double x,double y)
	{
		this.x=x;
		this.y=y;
	}
}
class ColorSpace
{
	public static double[] getHSBfromRGB(int red,int green,int blue)
	{
		double[] HSB=new double[3];
		if(red==green&&green==blue)
		{
			HSB[0]=-1;
			HSB[1]=0;
			HSB[2]=(red+0.0)/255;
			return HSB;
		}
		double hue=0,saturation=0,brightness=0;
		int high=max(red,green,blue);
		int low=min(red,green,blue);
		int range=high-low;
		if(red==high)hue=(green-blue+0.0)/range+0;
		else if(green==high)hue=(blue-red+0.0)/range+2;
		else if(blue==high)hue=(red-green+0.0)/range+4;
		if(hue<0)hue+=6;
		hue/=6;
		saturation=(range+0.0)/high;
		brightness=(high+0.0)/255;
		HSB[0]=hue;
		HSB[1]=saturation;
		HSB[2]=brightness;
		return HSB;
	}
	public static Color3f getRGBfromHSB(double hue,double saturation,double brightness)
	{
		int[] RGB=new int[3];
		if(hue==-1)return new Color3f((float)brightness,(float)brightness,(float)brightness);
		double red=0,green=0,blue=0;
		double range=saturation*brightness;
		double high=brightness;
		double low=brightness-range;
		double Hue=(6.0*hue)%6.0;
		int index=(int)Hue;
		double mid_low=(index%2==0?(Hue-index)*range:(index+1-Hue)*range);
		double mid=low+mid_low;
		switch(index)
		{
			case 0:red=high;green=mid;blue=low;break;
			case 1:red=mid;green=high;blue=low;break;
			case 2:red=low;green=high;blue=mid;break;
			case 3:red=low;green=mid;blue=high;break;
			case 4:red=mid;green=low;blue=high;break;
			case 5:red=high;green=low;blue=mid;break;
		}	
		return new Color3f((float)red,(float)green,(float)blue);
	}
	private static int max(int x,int y,int z)
	{
		int m=(z>y?z:y);
		return (x>m?x:m);
	}
	private static int min(int x,int y,int z)
	{
		int m=(z<y?z:y);
		return (x<m?x:m);
	}
}