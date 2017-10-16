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
	public vec3(double[] v)
	{
		this.x=v[0];
		this.y=v[1];
		this.z=v[2];
	}
	public double[] xyz()
	{
		return new double[]{x, y, z};
	}
	public double[] xyz(double k)
	{
		return new double[]{k*x, k*y, k*z};
	}
	public int x(){return (int)x;}
	public int y(){return (int)y;}
	public int z(){return (int)z;}
	public void set(double x,double y,double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public vec3 add(vec3 vector)
	{
		double x=this.x+vector.x;
		double y=this.y+vector.y;
		double z=this.z+vector.z;
		return new vec3(x,y,z);
	}
	public vec3 mul(double scale)
	{
		double x=this.x*scale;
		double y=this.y*scale;
		double z=this.z*scale;
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
	public static vec3 zero(){return new vec3(0);}
	public static final vec3[] v = 
	{
		new vec3(0, 0, 0),
		new vec3(1, 0, 0),
		new vec3(0, 1, 0),
		new vec3(0, 0, 1),
		new vec3(-1, 0, 0),
		new vec3(0, -1, 0),
		new vec3(0, 0, -1),
		new vec3(0, 1, 1),
		new vec3(1, 0, 1),
		new vec3(1, 1, 0),
		new vec3(1, 1, 1)
	};
	public static final vec3 unit(int k)
	{
		switch(k)
		{
			case 000: return v[0];
			case 100: return v[1];
			case 010: return v[2];
			case 001: return v[3];
			case -100: return v[4];
			case 0-10: return v[5];
			case 00-1: return v[6];
			case 011: return v[7];
			case 101: return v[8];
			case 110: return v[9];
			default: return v[10];
		}
	}
	public static vec3 random(double xMax, double yMax, double zMax)
	{
		double x = xMax*Math.random();
		double y = yMax*Math.random();
		double z = zMax*Math.random();
		return new vec3(x, y, z);
	}
}
