import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
public class JavaAndSolarBattery
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
		Vector3f vector3f=new Vector3f(0f,0.3f,-1f);
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
		this.addChild(this.getNorthBoardSolarBattery());
		this.addChild(this.getSouthWallSolarBattery());
		this.addChild(this.getRoofSolarBattery());
		this.addChild(this.getWestWallSolarBattery());
		this.addChild(this.getNorthWallSolarBattery());
	}
	TransformGroup getNorthBoardSolarBattery()
	{
		TransformGroup northBoardSolarBattery=new TransformGroup();
		for(int i=0;i<8;i++)
		{
			double x=i*(0.11+0.015)+0.11/2+0.015;
			double y=height+0.06;
			double z=0.07/2;
			double rotX=Math.atan(0.07/0.12);
			double rotY=Math.PI;
			double rotZ=Math.PI/2;
			northBoardSolarBattery.addChild(new SolarBattery("C1",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		}
		return northBoardSolarBattery;
	}
	TransformGroup getSouthWallSolarBattery()
	{
		TransformGroup southWallSolarBattery=new TransformGroup();
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<5;j++)
			{
				double x=j*(0.0615)+0.0615/2+0;
				double y=height-i*(0.0180)-0.0180/2;
				double z=width;
				double rotX=0;
				double rotY=0;
				double rotZ=0;
				southWallSolarBattery.addChild(new SolarBattery("C7",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
			}
		}
		for(int j=0;j<20;j++)
		{
			double x=j*(0.0180)+0.0180/2+0.35;
			double y=height-0.0615/2;
			double z=width;
			double rotX=0;
			double rotY=0;
			double rotZ=Math.PI/2;
			southWallSolarBattery.addChild(new SolarBattery("C7",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));	
		}
		for(int i=0;i<10;i++)
		{
			double x=0.35+0.36+0.0615/2;
			double y=height-0.07-i*0.0180-0.0180/2;
			double z=width;
			double rotX=0;
			double rotY=0;
			double rotZ=0;
			southWallSolarBattery.addChild(new SolarBattery("C7",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		}
		southWallSolarBattery.addChild(new SolarBattery("C2",new Point3d(0+0.0711/2+dx,0+0.132/2+dy,width+dz),0,0,Math.PI/2));
		southWallSolarBattery.addChild(new SolarBattery("C2",new Point3d(0+0.0711/2+dx,0.132+0.132/2+dy,width+dz),0,0,Math.PI/2));
		southWallSolarBattery.addChild(new SolarBattery("C2",new Point3d(0+0.0711+0.132/2+dx,0+0.0711/2+dy,width+dz),0,0,0));
		southWallSolarBattery.addChild(new SolarBattery("C2",new Point3d(0+0.0711+0.132+0.132/2+dx,0+0.0711/2+dy,width+dz),0,0,0));
		southWallSolarBattery.addChild(new SolarBattery("C2",new Point3d(0.35-0.0711/2+dx,0.132+0.0711/2+dy,width+dz),0,0,Math.PI/2));
		southWallSolarBattery.addChild(new SolarBattery("C2",new Point3d(length-0.132/2+dx,0+0.0711/2+dy,width+dz),0,0,0));
		southWallSolarBattery.addChild(new SolarBattery("C2",new Point3d(length-0.132/2+dx,height-0.0711/2+dy,width+dz),0,0,0));
		southWallSolarBattery.addChild(new SolarBattery("C2",new Point3d(length-0.132-0.132/2+dx,height-0.0711/2+dy,width+dz),0,0,0));
		southWallSolarBattery.addChild(new SolarBattery("C2",new Point3d(length-0.0711/2+dx,height-0.0711-0.132/2+dy,width+dz),0,0,Math.PI/2));
		return southWallSolarBattery;
	}
	TransformGroup getNorthWallSolarBattery()
	{
		TransformGroup northWallSolarBattery=new TransformGroup();
		for(int i=0;i<3;i++)
		{
			double x=i*0.0458+0.0458/2;
			double y=height-0.04/2;
			double z=0;
			double rotX=0;
			double rotY=Math.PI;
			double rotZ=0;
			northWallSolarBattery.addChild(new SolarBattery("SN7",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		}
		double x=3*0.0458+0.02/2;
		double y=height-0.0365/2;
		double z=0;
		double rotX=0;
		double rotY=Math.PI;
		double rotZ=0;
		northWallSolarBattery.addChild(new SolarBattery("SN1",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		x=3*0.0458+0.02+0.02/2;
		northWallSolarBattery.addChild(new SolarBattery("SN3",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		x=3*0.0458+0.02+0.02+0.0205/2;
		northWallSolarBattery.addChild(new SolarBattery("SN12",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		x=3*0.0458+0.02+0.02+0.0205+0.02/2;
		y=height-0.0365/2;
		northWallSolarBattery.addChild(new SolarBattery("SN4",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		return northWallSolarBattery;
	}
	TransformGroup getWestWallSolarBattery()
	{
		TransformGroup westWallSolarBattery=new TransformGroup();
		for(int i=0;i<9;i++)
		{
			double x=0;
			double y=0.1645/2;
			double z=i*0.072+0.072/2;
			double rotX=0;
			double rotY=-Math.PI/2;
			double rotZ=Math.PI/2;
			westWallSolarBattery.addChild(new SolarBattery("C11",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		}
		for(int i=0;i<6;i++)
		{
			double x=0;
			double y=0.1645+0.14/2;
			double z=i*0.11+0.11/2;
			double rotX=0;
			double rotY=-Math.PI/2;
			double rotZ=Math.PI/2;
			westWallSolarBattery.addChild(new SolarBattery("C5",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		}
		double x=0;
		double y=0.1645+0.14+0.072/2;
		double z=width-0.297-0.1645/2;
		double rotX=0;
		double rotY=-Math.PI/2;
		double rotZ=0;
		westWallSolarBattery.addChild(new SolarBattery("C11",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		y=0.1645+0.14+0.11/2;
		z=width-0.297-0.1645-0.015-0.14/2;
		westWallSolarBattery.addChild(new SolarBattery("C5",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		return westWallSolarBattery;
	}
	TransformGroup getRoofSolarBattery()
	{
		double sinW=0.12/0.651153;
		double cosW=(width-0.07)/0.651153;
		TransformGroup roofSolarBattery=new TransformGroup();
		for(int i=0;i<9;i++)
		{
			double x=length-0.28-0.36+i*0.0355+0.0355/2;
			double y=height+(0.651153-0.092/2)*sinW;
			double z=width-(0.651153-0.092/2)*cosW;
			double rotX=Math.asin(sinW)-Math.PI/2;
			double rotY=0;
			double rotZ=Math.PI/2;
			roofSolarBattery.addChild(new SolarBattery("C9",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
			y=height+(0.651153-0.092-0.0818/2)*sinW;
			z=width-(0.651153-0.092-0.0818/2)*cosW;
			roofSolarBattery.addChild(new SolarBattery("C10",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		}
		for(int i=0;i<4;i++)
		{
			double x=length-0.28-0.36+i*0.0712+0.0712/2;
			double y=height+(0.3357-0.1645/2)*sinW;
			double z=width-(0.3357-0.1645/2)*cosW;
			double rotX=Math.asin(sinW)-Math.PI/2;
			double rotY=0;
			double rotZ=Math.PI/2;
			roofSolarBattery.addChild(new SolarBattery("C11",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
			y=height+(0.3357-0.1645-0.1645/2)*sinW;
			z=width-(0.3357-0.1645-0.1645/2)*cosW;
			roofSolarBattery.addChild(new SolarBattery("C11",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		}
		for(int i=0;i<9;i++)
		{
			double x=length-0.132/2;
			double y=height+(0.6511-i*0.0711-0.0711/2)*sinW;
			double z=width-(0.6511-i*0.0711-0.0711/2)*cosW;
			double rotX=Math.asin(sinW)-Math.PI/2;
			double rotY=0;
			double rotZ=0;
			roofSolarBattery.addChild(new SolarBattery("C2",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
			x=length-0.132-0.132/2;
			roofSolarBattery.addChild(new SolarBattery("C2",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		}
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				double x=j*0.11+0.11/2;
				double y=height+(0.651153-i*0.14-0.14/2)*sinW;
				double z=width-(0.651153-i*0.14-0.14/2)*cosW;
				double rotX=Math.asin(sinW)-Math.PI/2;
				double rotY=0;
				double rotZ=Math.PI/2;
				roofSolarBattery.addChild(new SolarBattery("C5",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
			}
		}
		for(int i=0;i<3;i++)
		{
			double x=0.1645/2;
			double y=height+(i*0.072+0.072/2)*sinW;
			double z=width-(i*0.072+0.072/2)*cosW;
			double rotX=Math.asin(sinW)-Math.PI/2;
			double rotY=0;
			double rotZ=0;
			roofSolarBattery.addChild(new SolarBattery("C11",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
			x=0.1645+0.1645/2;
			roofSolarBattery.addChild(new SolarBattery("C11",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		}
		double x=length-0.28-0.36+4*0.0712+0.0712/2;
		double y=height+(0.3357-0.1645/2)*sinW;
		double z=width-(0.3357-0.1645/2)*cosW;
		double rotX=Math.asin(sinW)-Math.PI/2;
		double rotY=0;
		double rotZ=Math.PI/2;
		roofSolarBattery.addChild(new SolarBattery("C11",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		x=length-0.28-0.36+4*0.0712+0.0355/2;
		y=height+(0.3357-0.1645-0.0818/2)*sinW;
		z=width-(0.3357-0.1645-0.0818/2)*cosW;
		roofSolarBattery.addChild(new SolarBattery("C10",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		y=height+(0.3357-0.1645-0.0818-0.0818/2)*sinW;
		z=width-(0.3357-0.1645-0.0818-0.0818/2)*cosW;
		roofSolarBattery.addChild(new SolarBattery("C10",new Point3d(x+dx,y+dy,z+dz),rotX,rotY,rotZ));
		return roofSolarBattery;
	}
}
class SolarBattery extends TransformGroup
{
	public SolarBattery(String type,Point3d center,double rotX,double rotY,double rotZ)
	{
		double length=0.13,height=0.11,thick=0.0015;
		Appearance Appearance1=new Appearance();
		Material Material1=new Material();
		Material1.setDiffuseColor(new Color3f(0f,0f,0f));
		Appearance1.setMaterial(Material1);
		Appearance Appearance2=new Appearance();
		Material Material2=new Material();
		Material2.setDiffuseColor(new Color3f(0.3f,0.3f,0.3f));
		Appearance2.setMaterial(Material2);
		Font3D font3D=new Font3D(new Font("Microsoft Tai Le",Font.BOLD,1),new FontExtrusion());
		TransformGroup TransformGroup1=new TransformGroup();
		this.addChild(TransformGroup1);
		if(type.equals("C1"))
		{
			length=0.13;
			height=0.11;
			thick=0.0015;
		}
		else if(type.equals("C2"))
		{
			length=0.132;
			height=0.0711;
			thick=0.0020;
		}
		else if(type.equals("C5"))
		{
			length=0.14;
			height=0.11;
			thick=0.0025;
		}
		else if(type.equals("C7"))
		{
			length=0.0615;
			height=0.0180;
			thick=0.00167;
		}
		else if(type.equals("C9"))
		{
			length=0.092;
			height=0.0355;
			thick=0.00167;
		}
		else if(type.equals("C10"))
		{
			length=0.0818;
			height=0.0355;
			thick=0.00167;
		}
		else if(type.equals("C11"))
		{
			length=0.1645;
			height=0.0712;
			thick=0.0027;
		}
		else if(type.equals("SN1")||type.equals("SN3")||type.equals("SN4"))
		{
			length=0.02;
			height=0.0365;
			thick=0.0395;
		}
		else if(type.equals("SN7"))
		{
			length=0.0458;
			height=0.04;
			thick=0.047;
		}
		else if(type.equals("SN12"))
		{
			length=0.0205;
			height=0.0365;
			thick=0.0425;
		}
		Point3d[] batteryCoord=new Point3d[]
		{
			new Point3d(-length/2,-height/2,thick/2),
			new Point3d(length/2,-height/2,thick/2),
			new Point3d(length/2,height/2,thick/2),
			new Point3d(-length/2,height/2,thick/2)
		};
		Point3d[] batteryInCoord=new Point3d[]
		{
			new Point3d(-length/2+length/30,-height/2+height/30,thick),
			new Point3d(length/2-length/30,-height/2+height/30,thick),
			new Point3d(length/2-length/30,height/2-height/30,thick),
			new Point3d(-length/2+length/30,height/2-height/30,thick)
		};
		Point3d[] batteryOutCoord=new Point3d[]
		{
			new Point3d(-length/2,-height/2,thick),
			new Point3d(length/2,-height/2,thick),
			new Point3d(length/2,height/2,thick),
			new Point3d(-length/2,height/2,thick)
		};
		Transform3D transform3D=new Transform3D();
		transform3D.rotX(rotX);
		TransformGroup1=new TransformGroup(transform3D);
		transform3D.rotY(rotY);
		TransformGroup TransformGroup2=new TransformGroup(transform3D);
		transform3D.rotZ(rotZ);
		TransformGroup TransformGroup3=new TransformGroup(transform3D);
		TransformGroup3.addChild(new QuadArrayExtrusion3D(batteryCoord,new Vector3d(0,0,-thick),Appearance2));
		TransformGroup3.addChild(new QuadArrayWindowsExtrusion3D(batteryOutCoord,batteryInCoord,new Vector3d(0,0,-thick),Appearance1));
		transform3D=new Transform3D();
		transform3D.setScale(new Vector3d(length/3,height/2,thick));
		transform3D.setTranslation(new Vector3d(-length/4,-height/4,thick));
		TransformGroup TransformGroup4=new TransformGroup(transform3D);
		TransformGroup4.setTransform(transform3D);
		TransformGroup1.addChild(TransformGroup2);
		TransformGroup2.addChild(TransformGroup3);
		TransformGroup3.addChild(TransformGroup4);
		TransformGroup4.addChild(new Shape3D(new Text3D(font3D,type),Appearance1));
		transform3D=new Transform3D();
		transform3D.setTranslation(new Vector3d(center.x,center.y,center.z));
		this.setTransform(transform3D);
		this.addChild(TransformGroup1);
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