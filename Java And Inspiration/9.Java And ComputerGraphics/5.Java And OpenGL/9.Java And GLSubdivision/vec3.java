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
	public double[] xyz()
	{
		return new double[]{x, y, z};
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
	public void normalize()
	{
		double l=Math.sqrt(x*x+y*y+z*z);
		this.x/=l;
		this.y/=l;
		this.z/=l;
	}
	public static vec3 random(double xMax, double yMax, double zMax)
	{
		double x = xMax*Math.random();
		double y = yMax*Math.random();
		double z = zMax*Math.random();
		return new vec3(x, y, z);
	}
}
