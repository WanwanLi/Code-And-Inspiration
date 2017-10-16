import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
public class JavaAndRayTracer
{
	public static void main(String[] args)
	{
		Frame_RayTracer Frame_RayTracer1=new Frame_RayTracer();
		Frame_RayTracer1.setSize(700,700);
		Frame_RayTracer1.setVisible(true);
	}
}
class Frame_RayTracer extends Frame
{
	public Image image1;
	public Frame_RayTracer()
	{
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		try
		{
			String viewConf="0,0,10.0, 0,0,-1, 0,1,0, 1,0,0, 1,1, 10.0,  0.5";
			String lightConf="0,1.2,0,  1.0,0.9,0.5,  0.25,  2.5, 0,0,0,0,0,0,0,0";
			int imageWidth=100, imageHeight=100, maxTime=10;
			RayTracer RayTracer1=new RayTracer(imageWidth, imageHeight);
			RayTracer1.renderImage(viewConf, lightConf, maxTime);
			MemoryImageSource MemoryImageSource1=RayTracer1.getMemoryImageSource();
			this.image1=this.createImage(MemoryImageSource1);
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void paint(Graphics g)
	{
		if(image1!=null)g.drawImage(image1,100,100,this);
	}

}
class vec3
{
	public double x,y,z;
	public vec3(double k)
	{
		this.x=k;
		this.y=k;
		this.z=k;
	}
	public vec3(double x,double y,double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public vec3(String x,String y,String z)
	{
		this.x=Double.parseDouble(x);
		this.y=Double.parseDouble(y);
		this.z=Double.parseDouble(z);
	}
	public void inc(double scale, vec3 vector)
	{
		this.x+=scale*vector.x;
		this.y+=scale*vector.y;
		this.z+=scale*vector.z;
	}
	public vec3 add(double scale, vec3 vector)
	{
		double x=this.x+scale*vector.x;
		double y=this.y+scale*vector.y;
		double z=this.z+scale*vector.z;
		return new vec3(x,y,z);
	}
	public vec3 mul(double scale, vec3 vector)
	{
		double x=this.x*scale*vector.x;
		double y=this.y*scale*vector.y;
		double z=this.z*scale*vector.z;
		return new vec3(x,y,z);
	}
	public vec3 sub(vec3 vector)
	{
		double x=this.x-vector.x;
		double y=this.y-vector.y;
		double z=this.z-vector.z;
		return new vec3(x,y,z);
	}
	public vec3 div(vec3 vector)
	{
		double x=this.x/vector.x;
		double y=this.y/vector.y;
		double z=this.z/vector.z;
		return new vec3(x,y,z);
	}
	public void scale(double scale)
	{
		this.x*=scale;
		this.y*=scale;
		this.z*=scale;
	}
	public void normalize()
	{
		double l=Math.sqrt(x*x+y*y+z*z);
		this.x/=l;
		this.y/=l;
		this.z/=l;
	}
	public void clamp(double min, double max)
	{
		this.x=Math.min(Math.max(this.x,min),max);
		this.y=Math.min(Math.max(this.y,min),max);
		this.z=Math.min(Math.max(this.z,min),max);
	}
	public double length()
	{
		return Math.sqrt(x*x+y*y+z*z);
	}
	public vec3 get()
	{
		return new vec3(x,y,z);
	}
	public vec3 inverse()
	{
		return new vec3(-x,-y,-z);
	}
	public double dot(vec3 vector)
	{
		double a=this.x*vector.x;
		double b=this.y*vector.y;
		double c=this.z*vector.z;
		return a+b+c;
	}
	public vec3 reflect(vec3 N)
	{
		vec3 L=new vec3(x,y,z);
		L.inc(-2.0*L.dot(N),N);
		return L;
	}
	public vec3 cross(vec3 vector)
	{
		double x=this.y*vector.z-vector.y*this.z;
		double y=this.z*vector.x-vector.z*this.x;
		double z=this.x*vector.y-vector.x*this.y;
		return new vec3(x,y,z);
	}
	public vec3 vertical()
	{
		if(Math.abs(this.x)<0.5) return this.cross(new vec3(1,0,0));
		else return this.cross(new vec3(0,1,0));
	}
	public vec3 random()
	{
		double u=Math.random();
		double v=Math.random();
		double r = Math.sqrt(u);
		double angle = 6.28318530*v;
		vec3 n=this.get(),d=n.vertical();
		vec3 t=n.cross(d),b=n.cross(t);
		vec3 randir=new vec3(0,0,0);
		randir.inc(r*Math.cos(angle),t);
		randir.inc(r*Math.sin(angle),b);
		randir.inc(Math.sqrt(1.0-u),n);
		randir.normalize();
		return randir;
	}
	public static vec3 min(vec3 a, vec3 b)
	{
		double x=a.x<b.x?a.x:b.x;
		double y=a.y<b.y?a.y:b.y;
		double z=a.z<b.z?a.z:b.z;
		return new vec3(x,y,z);
	}
	public static vec3 max(vec3 a, vec3 b)
	{
		double x=a.x>b.x?a.x:b.x;
		double y=a.y>b.y?a.y:b.y;
		double z=a.z>b.z?a.z:b.z;
		return new vec3(x,y,z);
	}
}
class vec16
{
	public double[] x;
	public vec16(String[] values)
	{
		this.x=new double[16];
		for(int i=0;i<16;i++)
		{
			this.x[i]=Double.parseDouble(values[i]);
		}
	}
	public vec16(double value)
	{
		this.x=new double[16];
		for(int i=0;i<16;i++)
		{
			this.x[i]=value;
		}
	}
	public vec16(double s0, double s1, double s2, double s3, double s4, double s5, double s6, double s7, 
	double s8, double s9, double sa, double sb, double sc, double sd, double se, double sf)
	{
		this.x=new double[16];
		this.x[0]=s0; this.x[1]=s1; this.x[2]=s2; this.x[3]=s3; this.x[4]=s4; this.x[5]=s5; 
		this.x[6]=s6; this.x[7]=s7; this.x[8]=s8; this.x[9]=s9; this.x[10]=sa; this.x[11]=sb; 
		this.x[12]=sc; this.x[13]=sd; this.x[14]=se; this.x[15]=sf;
	}
	public vec16(double s0, vec3 s123, vec3 s456, vec3 s789, double sa, double sb, double sc, double sd, double se, double sf)
	{
		this.x=new double[16];
		this.x[0]=s0; this.set(1,s123); this.set(4,s456); this.set(7,s789);
		this.x[10]=sa; this.x[11]=sb; this.x[12]=sc; this.x[13]=sd; this.x[14]=se; this.x[15]=sf;
	}
	public void set(int i, vec3 vector)
	{
		this.x[i+0]=vector.x;
		this.x[i+1]=vector.y;
		this.x[i+2]=vector.z;
	}
	public double s(int i)
	{
		return this.x[i];
	}
	public vec3 s(int i, int j, int k)
	{
		String s0=this.x[i]+"";
		String s1=this.x[j]+"";
		String s2=this.x[k]+"";
		return new vec3(s0,s1,s2);
	}
	public void println()
	{
		String s="";
		for(int i=0;i<16;i++)
		{
			s+=this.x[i]+", ";
		}
		System.out.println("vec16=("+s+"...)");
	}
}
class RayTracer
{
	public int width;
	public int height;
	public int[] pixels;
	public RayTracer(int imageWidth,int imageHeight)
	{
		this.width=imageWidth;
		this.height=imageHeight;
		this.pixels=new int[width*height];
	}
	public void renderImage(String viewConf, String lightConf, int maxTime)
	{
		vec16 viewInfo = new vec16(viewConf.replace(" ","").split(","));
		vec16 lightInfo = new vec16(lightConf.replace(" ","").split(","));
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				vec3 color=getColor(viewInfo, lightInfo, maxTime, i, j);
				this.pixels[(height-1-i)*width+j]=this.getPixel(color);
			}
		}
	}
	private double MaxError=0.000001;
	private double MaxDistance=10000.0;
	private double sphereRadius1=0.2;
	private double sphereRadius2=0.6;
	private double sphereRadius3=0.3;
	private vec3 cornelBoxCenter=new vec3(0.0);
	private vec3 cornelBoxSize=new vec3(3.0);
	private vec3 cornelBoxColor1=new vec3(0.625, 0.61, 0.48);
	private vec3 cornelBoxColor2=new vec3(0.14, 0.45, 0.091);
	private vec3 cornelBoxColor3=new vec3(0.63, 0.065, 0.05);
	private vec3 sphereCenter1=new vec3(-6.0,0.0,0.0);
	private vec3 sphereCenter2=new vec3(-0.4,-0.9,-0.2);
	private vec3 sphereCenter3=new vec3(1.0,-1.2,0.4);
	private vec3 sphereColor1=new vec3(1.0,1.0,0.0);
	private vec3 sphereColor2=new vec3(0.625, 0.61, 0.48);
	private vec3 sphereColor3=new vec3(0.625, 0.61, 0.48);
	private double abs(double x){return x<0?-x:x;}
	private double sign(double x){return x<0?-1:1;}
	private double viewCoord(int coord,int size,double scale)
	{
		return ((coord+0.0)/(size-1.0)-0.5)*2.0*scale;
	}
	private vec3 getViewDirection(vec16 viewInfo,int row,int col,int imageWidth,int imageHeight)
	{
		vec3 direction=new vec3(0,0,0);
		vec3 eyePosition=viewInfo.s(0,1,2);
		vec3 viewDirection=viewInfo.s(3,4,5);
		vec3 viewUpDirection=viewInfo.s(6,7,8);
		vec3 viewRightDirection=viewInfo.s(9,10,11);
		double viewHeight=viewInfo.s(12),viewWidth=viewInfo.s(13);
		double viewDistance=viewInfo.s(14),viewFocus=viewInfo.s(15);
		double upDistance=viewCoord(row,imageHeight,viewHeight);
		double rightDistance=viewCoord(col,imageWidth,viewWidth);
		direction.inc(viewDistance*viewFocus,viewDirection);
		direction.inc(upDistance,viewUpDirection);
		direction.inc(rightDistance,viewRightDirection);
		direction.normalize();
		return direction;
	}
	double intersectSphere(vec3 center,double radius,vec3 origin,vec3 direction)
	{
		vec3 connection=center.sub(origin);
		double b=connection.dot(direction);
		double c=connection.dot(connection);
		double delta=b*b-c+radius*radius;
		if(delta<0.0)return MaxDistance;
		double distance=b-Math.sqrt(delta);
		if(distance<=MaxError)return MaxDistance;
		return distance;
	}
	double intersectCube(vec3 cubeMin,vec3 cubeMax, vec3 origin,vec3 direction)
	{
		vec3 tMin=cubeMin.sub(origin).div(direction);
		vec3 tMax=cubeMax.sub(origin).div(direction);
		vec3 t1=vec3.min(tMin,tMax),t2=vec3.max(tMin,tMax);
		double tNear=Math.max(Math.max(t1.x,t1.y),t1.z);
		double tFar=Math.min(Math.min(t2.x,t2.y),t2.z);
		if(tNear>tFar)return MaxDistance;
		else return tFar;
	}
	vec3 normalCube(vec3 position,vec3 center)
	{
		vec3 normal=position.sub(center);
		normal.normalize();
		double x=normal.x,y=normal.y,z=normal.z;
		double absX=abs(x),absY=abs(y),absZ=abs(z);
		if(absX>absY&&absX>absZ){x=sign(x);y=0.0;z=0.0;}
		else if(absY>absZ){x=0.0;y=sign(y);z=0.0;}
		else {x=0.0;y=0.0;z=sign(z);}
		return new vec3(x,y,z);
	}
	vec16 intersectCornelBox(vec3 origin,vec3 direction)
	{
		vec3 min=cornelBoxCenter.add(-0.5, cornelBoxSize);
		vec3 max=cornelBoxCenter.add(0.5, cornelBoxSize);
		double distance=intersectCube(min, max,origin,direction);
		if(distance==MaxDistance)
		{
			vec16 ErrorValue=new vec16(0.0);
			ErrorValue.x[10]=MaxDistance;
			return ErrorValue;
		}
		vec3 position=origin.add(distance, direction);
		vec3 normal=normalCube(position,cornelBoxCenter).inverse();
		vec3 color=cornelBoxColor1;
		if(normal.x<-0.5)color=cornelBoxColor2;
		else if(normal.x>0.5)color=cornelBoxColor3;
		return new vec16(1.0,position,normal,color,distance,0,0,0,0,0);
	}
	private vec16 intersectScene(vec3 origin,vec3 direction)
	{
		vec3 position=new vec3(0);
		vec3 normal=new vec3(0);
		vec3 color=new vec3(0);
		double intersection=0.0;
		vec16 intersect=intersectCornelBox(origin,direction);
		double d1=intersect.s(10);
		double d2=intersectSphere(sphereCenter2,sphereRadius2,origin,direction);
		double d3=intersectSphere(sphereCenter3,sphereRadius3,origin,direction);
		double d4=MaxDistance;
		double distance=MaxDistance;
		if(d1<MaxDistance&&d1<d2&&d1<d3&&d1<d4)return intersect;
		else if(d2<MaxDistance&&d2<d3&&d2<d4)
		{
			intersection=1.0;
			position=origin.add(d2,direction);
			normal=position.sub(sphereCenter2);
			normal.normalize();
			color=sphereColor2;
			distance=d2;
		}
		else if(d3<MaxDistance&&d3<d4)
		{
			intersection=1.0;
			position=origin.add(d3,direction);
			normal=position.sub(sphereCenter3);
			normal.normalize();
			color=sphereColor3;
			distance=d3;
		}
		return new vec16(intersection,position,normal,color,distance,0,0,0,0,0);
	}
	private boolean isOnShadow(vec3 position,vec3 lightPosition)
	{
		vec3 lightDirection=lightPosition.sub(position);
		double lightDistance=lightDirection.length();
		lightDirection.normalize();
		vec16 intersection=intersectScene(position,lightDirection);
		double distance=intersection.s(10);
		if(distance>lightDistance)return false;
		return intersection.s(0)>0;
	}
	private double lightDecay(double lightDistance)
	{
		return 1.0/(lightDistance*lightDistance+MaxError);
	}
	private vec3 lightColor(vec16 lightInfo,vec3 position,vec3 normal,vec3 color)
	{
		vec3 lightPosition=lightInfo.s(0,1,2), lightColor=lightInfo.s(3,4,5);
		vec3 L=lightPosition.sub(position); double d=L.length(); L.normalize();
		vec3 N=normal;double cosLN=Math.max(L.dot(N),0.0), I=lightInfo.s(7);
		return color.mul(I*cosLN*lightDecay(d),lightColor);
	}
	private vec3 envLightColor(vec16 lightInfo,vec3 position,vec3 normal,int maxTime)
	{
		vec3 envColor=new vec3(0,0,0);
		vec3 lightDirection=lightInfo.s(0,1,2);
		for(int t=0;t<maxTime;t++)
		{
			double shadowDecay=1.0;
			vec3 direction=normal.random();
			vec16 intersection=intersectScene(position,direction);
			if(intersection.s(0)>0)
			{
				vec3 position1=intersection.s(1,2,3);
				vec3 normal1=intersection.s(4,5,6);
				vec3 color1=intersection.s(7,8,9);
				if(isOnShadow(position1,lightDirection))shadowDecay=lightInfo.s(6);
				envColor.inc(shadowDecay, lightColor(lightInfo,position1,normal1,color1));
			}
		}
		return envColor;
	}
	private vec3 getColor(vec16 viewInfo, vec16 lightInfo, int maxTime, int row, int col)
	{
		double shadowDecay=1.0;
		vec3 eyePosition=viewInfo.s(0,1,2);
		vec3 viewDirection=getViewDirection(viewInfo,row,col,width,height);
		vec3 lightDirection=lightInfo.s(0,1,2),position,normal,color,viewColor,reflectColor;
		vec16 intersection=intersectScene(eyePosition,viewDirection);
		vec3 direction=viewDirection.get(); viewColor=new vec3(0.0);
		if(intersection.s(0)>0)
		{
			position=intersection.s(1,2,3);
			normal=intersection.s(4,5,6);
			color=intersection.s(7,8,9);
			if(isOnShadow(position,lightDirection))shadowDecay=lightInfo.s(6);
			viewColor.inc(shadowDecay, lightColor(lightInfo,position,normal,color));
			vec3 envColor=envLightColor(lightInfo,position,normal,maxTime);
			viewColor.inc(shadowDecay, color.mul(1.0/maxTime, envColor));
		}
		viewColor.clamp(0,1);
		return viewColor;
	}
	private int getPixel(vec3 color)
	{
		int alpha=255;
		int red = (int)(255*color.x);
		int green = (int)(255*color.y);
		int blue = (int)(255*color.z);
		return (alpha<<24|red<<16|green<<8|blue);
	}
	public MemoryImageSource getMemoryImageSource()
	{
		MemoryImageSource MemoryImageSource1=new MemoryImageSource(width,height,pixels,0,width);
		return MemoryImageSource1;
	}
}