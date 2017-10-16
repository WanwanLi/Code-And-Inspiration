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