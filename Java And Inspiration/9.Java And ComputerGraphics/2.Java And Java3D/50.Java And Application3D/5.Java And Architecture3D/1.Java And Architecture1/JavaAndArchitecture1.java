import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
public class JavaAndArchitecture1
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
		TransformGroup1.addChild(new Architecture1());
		BranchGroup1.compile();
		SimpleUniverse SimpleUniverse1=new SimpleUniverse();
		SimpleUniverse1.getViewingPlatform().setNominalViewingTransform();
		SimpleUniverse1.addBranchGraph(BranchGroup1);
	}
}
class Architecture1 extends TransformGroup
{
	private double length=1.01,width=0.71,height=0.32,thick=0.03,dx=-0.5,dy=-0.22,dz=-0.35;
	public Architecture1()
	{
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(1f,1f,1f));
		Appearance1.setMaterial(Material1);
		Appearance Appearance2=new Appearance();
		Material Material2=new Material();
		Material2.setDiffuseColor(new Color3f(0.3f,0.3f,0.3f));
		Material2.setSpecularColor(new Color3f(0f,0f,0f));
		Appearance2.setMaterial(Material2);
		Point3d[] eastCoord=new Point3d[]
		{
			new Point3d(length+dx,0+dy,0+dz),
			new Point3d(length+dx,height+dy,0+dz),
			new Point3d(length+dx,height+dy,0.26+dz),
			new Point3d(length+dx,0+dy,0.26+dz),

			new Point3d(length+dx,height-0.07+dy,0.26+dz),
			new Point3d(length+dx,height+dy,0.26+dz),
			new Point3d(length+dx,height+dy,0.26+0.11+dz),
			new Point3d(length+dx,height-0.07+dy,0.26+0.11+dz),

			new Point3d(length+dx,0+dy,0.71-0.34+dz),
			new Point3d(length+dx,height+dy,0.71-0.34+dz),
			new Point3d(length+dx,height+dy,0.71+dz),
			new Point3d(length+dx,0+dy,0.71+dz),

			new Point3d(length+dx,height+dy,width+dz),
			new Point3d(length+dx,height+dy,0+dz),
			new Point3d(length+dx,height+0.12+dy,0.07+dz),
			new Point3d(length+dx,height+dy,width+dz)

		};
		QuadArrayExtrusion3D eastWall=new QuadArrayExtrusion3D(eastCoord,new Vector3d(-thick,0,0),Appearance1);
		this.addChild(eastWall);
		Point3d[] westCoord=new Point3d[]
		{
			new Point3d(0+dx,height+dy,0+dz),
			new Point3d(0+dx,0+dy,0+dz),
			new Point3d(0+dx,0+dy,width+dz),
			new Point3d(0+dx,height+dy,width+dz),

			new Point3d(0+dx,height+dy,width+dz),
			new Point3d(0+dx,height+0.12+dy,0.07+dz),
			new Point3d(0+dx,height+dy,0+dz),
			new Point3d(0+dx,height+dy,width+dz)
		};
		QuadArrayExtrusion3D westWall=new QuadArrayExtrusion3D(westCoord,new Vector3d(thick,0,0),Appearance1);
		this.addChild(westWall);
		Point3d[] northOutCoord=new Point3d[]
		{
			new Point3d(0+dx,0+dy,0+dz),
			new Point3d(0+dx,height+dy,0+dz),
			new Point3d(0.165+0.048+0.25+dx,height+dy,0+dz),
			new Point3d(0.165+0.048+0.25+dx,0+dy,0+dz),

			new Point3d(0.165+0.048+0.25+dx,0+dy,0+dz),
			new Point3d(0.165+0.048+0.25+dx,height+dy,0+dz),
			new Point3d(0.165+0.048+0.25+0.11+dx,height+dy,0+dz),
			new Point3d(0.165+0.048+0.25+0.11+dx,0+dy,0+dz),

			new Point3d(0.165+0.048+0.25+0.11+dx,0+dy,0+dz),
			new Point3d(0.165+0.048+0.25+0.11+dx,height+dy,0+dz),
			new Point3d(length+dx,height+dy,0+dz),
			new Point3d(length+dx,0+dy,0+dz)
		};
		Point3d[] northInCoord=new Point3d[]
		{
			new Point3d(0.165+dx,0.11+dy,0+dz),
			new Point3d(0.165+dx,0.11+0.07+dy,0+dz),
			new Point3d(0.165+0.04+dx,0.11+0.07+dy,0+dz),
			new Point3d(0.165+0.04+dx,0.11+dy,0+dz),

			new Point3d(0.165+0.048+0.25+dx,0.11+dy,0+dz),
			new Point3d(0.165+0.048+0.25+dx,height-0.159+dy,0+dz),
			new Point3d(0.165+0.048+0.25+0.11+dx,height-0.159+dy,0+dz),
			new Point3d(0.165+0.048+0.25+0.11+dx,0.11+dy,0+dz),

			new Point3d(length-0.055-0.16+dx,0+dy,0+dz),
			new Point3d(length-0.055-0.16+dx,0.21+dy,0+dz),
			new Point3d(length-0.055+dx,0.21+dy,0+dz),
			new Point3d(length-0.055+dx,0+dy,0+dz)
		};
		QuadArrayWindowsExtrusion3D northWall=new QuadArrayWindowsExtrusion3D(northOutCoord,northInCoord,new Vector3d(0,0,thick),Appearance1);
		this.addChild(northWall);
		Point3d[] northBoardCoord=new Point3d[]
		{
			new Point3d(0+dx,height+dy,0+dz),
			new Point3d(0+dx,height+0.12+dy,0.07+dz),
			new Point3d(length+dx,height+0.12+dy,0.07+dz),
			new Point3d(length+dx,height+dy,0+dz)
		};
		QuadArrayExtrusion3D northBoard=new QuadArrayExtrusion3D(northBoardCoord,new Vector3d(0,-thick*(0.07/0.12),thick),Appearance1);
		this.addChild(northBoard);
		Point3d[] southOutCoord=new Point3d[]
		{
			new Point3d(0+dx,0+dy,width+dz),
			new Point3d(0.35+dx,0+dy,width+dz),
			new Point3d(0.35+dx,height+dy,width+dz),
			new Point3d(0+dx,height+dy,width+dz),

			new Point3d(0.35+dx,0+dy,width+dz),
			new Point3d(0.35+0.36+dx,0+dy,width+dz),
			new Point3d(0.35+0.36+dx,height+dy,width+dz),
			new Point3d(0.35+dx,height+dy,width+dz),

			new Point3d(length+dx,0+dy,width+dz),
			new Point3d(length+dx,height+dy,width+dz),
			new Point3d(length-0.3+dx,height+dy,width+dz),
			new Point3d(length-0.3+dx,0+dy,width+dz)
		};
		Point3d[] southInCoord=new Point3d[]
		{
			new Point3d(0.085+dx,0.08+dy,width+dz),
			new Point3d(0.085+0.18+dx,0.08+dy,width+dz),
			new Point3d(0.085+0.18+dx,0.08+0.18+dy,width+dz),
			new Point3d(0.085+dx,0.08+0.18+dy,width+dz),

			new Point3d(0.35+dx,0+dy,width+dz),
			new Point3d(0.35+0.36+dx,0+dy,width+dz),
			new Point3d(0.35+0.36+dx,0.25+dy,width+dz),
			new Point3d(0.35+dx,0.25+dy,width+dz),

			new Point3d(length-0.12+dx,0.09+dy,width+dz),
			new Point3d(length-0.12+dx,0.09+0.14+dy,width+dz),
			new Point3d(length-0.12-0.11+dx,0.09+0.14+dy,width+dz),
			new Point3d(length-0.12-0.11+dx,0.09+dy,width+dz)
		};
		QuadArrayWindowsExtrusion3D southWall=new QuadArrayWindowsExtrusion3D(southOutCoord,southInCoord,new Vector3d(0,0,-thick),Appearance1);
		this.addChild(southWall);
		this.addChild(new CircularWindowExtrusion3D(0.09,thick,10,new Point3d(0.085+0.09+dx,0.08+0.09+dy,width+dz),Appearance1));
		double sinW=0.12/0.651153;
		double cosW=(width-0.07)/0.651153;
		Point3d[] roofOutCoord=new Point3d[]
		{
			new Point3d(0+dx,height+dy,width+dz),
			new Point3d(length+dx,height+dy,width+dz),
			new Point3d(length+dx,height+0.12+dy,0.07+dz),
			new Point3d(0+dx,height+0.12+dy,0.07+dz)
		};
		Point3d[] roofInCoord=new Point3d[]
		{
			new Point3d(length-0.28-0.36+dx,height+0.335751*sinW+dy,width-0.335751*cosW+dz),
			new Point3d(length-0.28+dx,height+0.335751*sinW+dy,width-0.335751*cosW+dz),
			new Point3d(length-0.28+dx,height+(0.335751+0.13837)*sinW+dy,width-(0.335751+0.13837)*cosW+dz),
			new Point3d(length-0.28-0.36+dx,height+(0.335751+0.13837)*sinW+dy,width-(0.335751+0.13837)*cosW+dz)
		};
		QuadArrayWindowsExtrusion3D roof=new QuadArrayWindowsExtrusion3D(roofOutCoord,roofInCoord,new Vector3d(0,-thick,0),Appearance1);
		this.addChild(roof);
		double floorLength=3,floorWidth=2,floorHeight=0.001;
		double leftFloorLength=(floorLength-length)/2,topFloorWidth=(floorWidth-width)/2;
		Point3d[] floorCoord=new Point3d[]
		{
			new Point3d(-leftFloorLength+dx,floorHeight/2+dy,-topFloorWidth+dz),
			new Point3d(-leftFloorLength+dx,floorHeight/2+dy,-topFloorWidth+floorWidth+dz),
			new Point3d(-leftFloorLength+floorLength+dx,floorHeight/2+dy,-topFloorWidth+floorWidth+dz),
			new Point3d(-leftFloorLength+floorLength+dx,floorHeight/2+dy,-topFloorWidth+dz)
		};
		QuadArrayExtrusion3D floor=new QuadArrayExtrusion3D(floorCoord,new Vector3d(0,-floorHeight,0),Appearance2);
		this.addChild(floor);
	}
}
class QuadArrayWindowsExtrusion3D extends Shape3D
{
	public QuadArrayWindowsExtrusion3D(Point3d[] outSideQuadArrayCoordinates,Point3d[] inSideQuadArrayCoordinates,Vector3d extrusionVector,Appearance appearance)
	{
		int c=0,l=outSideQuadArrayCoordinates.length;
		double dx=extrusionVector.x;
		double dy=extrusionVector.y;
		double dz=extrusionVector.z;
		QuadArray QuadArray1=new QuadArray(l*16,QuadArray.COORDINATES);
		for(int i=0;i<l;i+=4)
		{
			Point3d op00=outSideQuadArrayCoordinates[i+0];
			Point3d op01=outSideQuadArrayCoordinates[i+1];
			Point3d op02=outSideQuadArrayCoordinates[i+2];
			Point3d op03=outSideQuadArrayCoordinates[i+3];
			Point3d ip00=inSideQuadArrayCoordinates[i+0];
			Point3d ip01=inSideQuadArrayCoordinates[i+1];
			Point3d ip02=inSideQuadArrayCoordinates[i+2];
			Point3d ip03=inSideQuadArrayCoordinates[i+3];
			Point3d op10=new Point3d(op00.x+dx,op00.y+dy,op00.z+dz);
			Point3d op11=new Point3d(op01.x+dx,op01.y+dy,op01.z+dz);
			Point3d op12=new Point3d(op02.x+dx,op02.y+dy,op02.z+dz);
			Point3d op13=new Point3d(op03.x+dx,op03.y+dy,op03.z+dz);
			Point3d ip10=new Point3d(ip00.x+dx,ip00.y+dy,ip00.z+dz);
			Point3d ip11=new Point3d(ip01.x+dx,ip01.y+dy,ip01.z+dz);
			Point3d ip12=new Point3d(ip02.x+dx,ip02.y+dy,ip02.z+dz);
			Point3d ip13=new Point3d(ip03.x+dx,ip03.y+dy,ip03.z+dz);
			QuadArray1.setCoordinate(c++,op00);
			QuadArray1.setCoordinate(c++,op01);
			QuadArray1.setCoordinate(c++,ip01);
			QuadArray1.setCoordinate(c++,ip00);
			QuadArray1.setCoordinate(c++,op01);
			QuadArray1.setCoordinate(c++,op02);
			QuadArray1.setCoordinate(c++,ip02);
			QuadArray1.setCoordinate(c++,ip01);
			QuadArray1.setCoordinate(c++,op02);
			QuadArray1.setCoordinate(c++,op03);
			QuadArray1.setCoordinate(c++,ip03);
			QuadArray1.setCoordinate(c++,ip02);
			QuadArray1.setCoordinate(c++,op03);
			QuadArray1.setCoordinate(c++,op00);
			QuadArray1.setCoordinate(c++,ip00);
			QuadArray1.setCoordinate(c++,ip03);
			QuadArray1.setCoordinate(c++,op13);
			QuadArray1.setCoordinate(c++,op12);
			QuadArray1.setCoordinate(c++,ip12);
			QuadArray1.setCoordinate(c++,ip13);
			QuadArray1.setCoordinate(c++,op12);
			QuadArray1.setCoordinate(c++,op11);
			QuadArray1.setCoordinate(c++,ip11);
			QuadArray1.setCoordinate(c++,ip12);
			QuadArray1.setCoordinate(c++,op11);
			QuadArray1.setCoordinate(c++,op10);
			QuadArray1.setCoordinate(c++,ip10);
			QuadArray1.setCoordinate(c++,ip11);
			QuadArray1.setCoordinate(c++,op10);
			QuadArray1.setCoordinate(c++,op13);
			QuadArray1.setCoordinate(c++,ip13);
			QuadArray1.setCoordinate(c++,ip10);
			QuadArray1.setCoordinate(c++,op00);
			QuadArray1.setCoordinate(c++,op10);
			QuadArray1.setCoordinate(c++,op11);
			QuadArray1.setCoordinate(c++,op01);
			QuadArray1.setCoordinate(c++,op01);
			QuadArray1.setCoordinate(c++,op11);
			QuadArray1.setCoordinate(c++,op12);
			QuadArray1.setCoordinate(c++,op02);
			QuadArray1.setCoordinate(c++,op02);
			QuadArray1.setCoordinate(c++,op12);
			QuadArray1.setCoordinate(c++,op13);
			QuadArray1.setCoordinate(c++,op03);
			QuadArray1.setCoordinate(c++,op03);
			QuadArray1.setCoordinate(c++,op13);
			QuadArray1.setCoordinate(c++,op10);
			QuadArray1.setCoordinate(c++,op00);
			QuadArray1.setCoordinate(c++,ip00);
			QuadArray1.setCoordinate(c++,ip01);
			QuadArray1.setCoordinate(c++,ip11);
			QuadArray1.setCoordinate(c++,ip10);
			QuadArray1.setCoordinate(c++,ip01);
			QuadArray1.setCoordinate(c++,ip02);
			QuadArray1.setCoordinate(c++,ip12);
			QuadArray1.setCoordinate(c++,ip11);
			QuadArray1.setCoordinate(c++,ip02);
			QuadArray1.setCoordinate(c++,ip03);
			QuadArray1.setCoordinate(c++,ip13);
			QuadArray1.setCoordinate(c++,ip12);
			QuadArray1.setCoordinate(c++,ip03);
			QuadArray1.setCoordinate(c++,ip00);
			QuadArray1.setCoordinate(c++,ip10);
			QuadArray1.setCoordinate(c++,ip13);
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(QuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}
class QuadArrayExtrusion3D extends Shape3D
{
	public QuadArrayExtrusion3D(Point3d[] quadArrayCoordinates,Vector3d extrusionVector,Appearance appearance)
	{
		int c=0,l=quadArrayCoordinates.length;
		double dx=extrusionVector.x;
		double dy=extrusionVector.y;
		double dz=extrusionVector.z;
		QuadArray QuadArray1=new QuadArray(l*6,QuadArray.COORDINATES);
		for(int i=0;i<l;i+=4)
		{
			Point3d p00=quadArrayCoordinates[i+0];
			Point3d p01=quadArrayCoordinates[i+1];
			Point3d p02=quadArrayCoordinates[i+2];
			Point3d p03=quadArrayCoordinates[i+3];
			Point3d p10=new Point3d(p00.x+dx,p00.y+dy,p00.z+dz);
			Point3d p11=new Point3d(p01.x+dx,p01.y+dy,p01.z+dz);
			Point3d p12=new Point3d(p02.x+dx,p02.y+dy,p02.z+dz);
			Point3d p13=new Point3d(p03.x+dx,p03.y+dy,p03.z+dz);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p02);
			QuadArray1.setCoordinate(c++,p03);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p00);
			QuadArray1.setCoordinate(c++,p03);
			QuadArray1.setCoordinate(c++,p13);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p01);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p12);
			QuadArray1.setCoordinate(c++,p02);
			QuadArray1.setCoordinate(c++,p13);
			QuadArray1.setCoordinate(c++,p12);
			QuadArray1.setCoordinate(c++,p11);
			QuadArray1.setCoordinate(c++,p10);
			QuadArray1.setCoordinate(c++,p02);
			QuadArray1.setCoordinate(c++,p12);
			QuadArray1.setCoordinate(c++,p13);
			QuadArray1.setCoordinate(c++,p03);
		}
		GeometryInfo GeometryInfo1=new GeometryInfo(QuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}
class CircularWindowExtrusion3D extends Shape3D
{
	public CircularWindowExtrusion3D(double r,double t,int n,Point3d center,Appearance appearance)
	{
		int c=0;
		double a=0.5*Math.PI/n;
		double dx=center.x,dy=center.y,dz=center.z;
		QuadArray QuadArray1=new QuadArray(4*n*12+4*4,QuadArray.COORDINATES);
		Point3d[] p02=new Point3d[]
		{
			new Point3d(r+dx,r+dy,0+dz),
			new Point3d(-r+dx,r+dy,0+dz),
			new Point3d(-r+dx,-r+dy,0+dz),
			new Point3d(r+dx,-r+dy,0+dz)
		};
		Point3d[] p12=new Point3d[]
		{
			new Point3d(r+dx,r+dy,-t+dz),
			new Point3d(-r+dx,r+dy,-t+dz),
			new Point3d(-r+dx,-r+dy,-t+dz),
			new Point3d(r+dx,-r+dy,-t+dz)
		};
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<n;j++)
			{
				double w=j*a+i*Math.PI/2;
				Point3d p00=new Point3d(r*Math.cos(w)+dx,r*Math.sin(w)+dy,0+dz);
				Point3d p10=new Point3d(r*Math.cos(w)+dx,r*Math.sin(w)+dy,-t+dz);
				w+=a;
				Point3d p01=new Point3d(r*Math.cos(w)+dx,r*Math.sin(w)+dy,0+dz);
				Point3d p11=new Point3d(r*Math.cos(w)+dx,r*Math.sin(w)+dy,-t+dz);
				QuadArray1.setCoordinate(c++,p00);
				QuadArray1.setCoordinate(c++,p02[i]);
				QuadArray1.setCoordinate(c++,p01);
				QuadArray1.setCoordinate(c++,p00);
				QuadArray1.setCoordinate(c++,p10);
				QuadArray1.setCoordinate(c++,p11);
				QuadArray1.setCoordinate(c++,p12[i]);
				QuadArray1.setCoordinate(c++,p10);
				QuadArray1.setCoordinate(c++,p00);
				QuadArray1.setCoordinate(c++,p01);
				QuadArray1.setCoordinate(c++,p11);
				QuadArray1.setCoordinate(c++,p10);
			}
		}
		QuadArray1.setCoordinate(c++,p02[0]);
		QuadArray1.setCoordinate(c++,p12[0]);
		QuadArray1.setCoordinate(c++,p12[1]);
		QuadArray1.setCoordinate(c++,p02[1]);
		QuadArray1.setCoordinate(c++,p02[1]);
		QuadArray1.setCoordinate(c++,p12[1]);
		QuadArray1.setCoordinate(c++,p12[2]);
		QuadArray1.setCoordinate(c++,p02[2]);
		QuadArray1.setCoordinate(c++,p02[2]);
		QuadArray1.setCoordinate(c++,p12[2]);
		QuadArray1.setCoordinate(c++,p12[3]);
		QuadArray1.setCoordinate(c++,p02[3]);
		QuadArray1.setCoordinate(c++,p02[3]);
		QuadArray1.setCoordinate(c++,p12[3]);
		QuadArray1.setCoordinate(c++,p12[0]);
		QuadArray1.setCoordinate(c++,p02[0]);
		GeometryInfo GeometryInfo1=new GeometryInfo(QuadArray1);
		NormalGenerator NormalGenerator1=new NormalGenerator();
		NormalGenerator1.generateNormals(GeometryInfo1);
		this.setGeometry(GeometryInfo1.getGeometryArray());
		this.setAppearance(appearance);
	}
}