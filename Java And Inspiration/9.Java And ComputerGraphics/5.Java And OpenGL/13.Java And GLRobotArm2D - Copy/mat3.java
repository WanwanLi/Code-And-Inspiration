class mat3
{
	public double[][] v;
	public mat3()
	{
		this.v = new double[3][3]; this.clear();
	}
	public mat3(double[][] m)
	{
		this.v = new double[3][3];
		for (int i = 0; i<3; i++)for (int j = 0; j<3; j++)this.v[i][j]=m[i][j];
	}
	public void set(double[][] m)
	{
		for (int i = 0; i<3; i++)for (int j = 0; j<3; j++)this.v[i][j]=m[i][j];
	}
	public void clear()
	{
		for (int i = 0; i<3; i++)for (int j = 0; j<3; j++)this.v[i][j]=0;
	}
	public void identity()
	{
		this.clear(); for (int i = 0; i<3; i++)this.v[i][i] = 1;
	}
	public void mul(mat3 m)
	{
		mat3 t = new mat3();
		for (int i = 0; i<3; i++) 
		{
			for (int j = 0; j<3; j++) 
			{
				for (int k = 0; k<3; k++) 
				{
					t.v[i][j] += this.v[i][k]*m.v[k][j];
				}
			}
		}
		this.v = t.v;
	}
	public void translate(double x, double y)
	{
		mat3 m = new mat3();
		m.identity();
		m.v[0][2] = x; 
		m.v[1][2] = y;
		this.mul(m);
	}
	public void rotate(double a)
	{
		mat3 m = new mat3();
		m.identity();
		m.v[0][0] = Math.cos(a);
		m.v[0][1] = -Math.sin(a);
		m.v[1][0] = Math.sin(a);
		m.v[1][1] = Math.cos(a);
		this.mul(m);
	}
	public void scale(double x, double y)
	{
		mat3 m = new mat3();
		m.identity();
		m.v[0][0] = x; 
		m.v[1][1] = y;
		this.mul(m);
	}
	public vec3 mul(vec3 v)
	{
		double[] k = v.xyz();
		double[] s = {0, 0, 0};
		for (int i = 0; i<3; i++) 
		{
			for (int j = 0; j<3; j++) 
			{
				s[i] += this.v[i][j]*k[j];
			}
		}
		return new vec3(s);
	}
}