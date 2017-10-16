class vec4
{
	public double x,y,z,w;
	public vec4(double k)
	{
		this.x=k;
		this.y=k;
		this.z=k;
		this.w=k;
	}
	public vec4(double x, double y, double z, double w)
	{
		this.x=x;
		this.y=y;
		this.z=z;
		this.w=w;
	}
	public void set(double x, double y, double z, double w)
	{
		this.x=x;
		this.y=y;
		this.z=z;
		this.w=w;
	}
	public static vec4 random(double xMax, double yMax, double zMax, double wMax)
	{
		double x = xMax*Math.random();
		double y = yMax*Math.random();
		double z = zMax*Math.random();
		double w = wMax*Math.random();
		return new vec4(x, y, z, w);
	}
	public vec4 blend(vec4 dest)
	{
		double x, y, z, w = dest.w;
		x = dest.x*w + this.x*(1-w);
		y = dest.y*w + this.y*(1-w);
		z = dest.z*w + this.z*(1-w);
		return new vec4(x, y, z, 1);
	}
	public int x(){return (int)x;}
	public int y(){return (int)y;}
	public int z(){return (int)z;}
	public int w(){return (int)w;}
}