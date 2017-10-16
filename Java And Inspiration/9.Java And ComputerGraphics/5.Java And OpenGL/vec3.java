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
	public vec3 random(double xMax, double yMax, double zMax)
	{
		double x = xMax*Math.random();
		double y = yMax*Math.random();
		double z = zMax*Math.random();
		return new vec3(x, y, z);
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
