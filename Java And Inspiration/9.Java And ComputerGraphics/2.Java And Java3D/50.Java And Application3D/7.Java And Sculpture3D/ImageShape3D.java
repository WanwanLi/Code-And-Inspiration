class ImageShape3D
{
	private int imageHeight;
	private int imageWidth;
	private double[] depth;
	private double[] logDepth;
	private double[] brightness;
	private double albedo,x,y,f,a,b,c;
	private double minDepth,maxDepth;
	public ImageShape3D(double albedo, double[] brightness, int imageWidth, int imageHeight)
	{
		this.albedo = albedo;
		this.brightness = brightness;
		this.imageWidth=imageWidth;
		this.imageHeight=imageHeight;
		this.depth = new double[imageWidth*imageHeight];
		this.logDepth = new double[imageWidth*imageHeight];
	}
	public void setLightPosition(double a, double b, double c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}
	public void setCameraParameters(double x, double y, double f) 
	{
		this.x = x;
		this.y = y;
		this.f = f;
	}
	public double[][] getDepthMap(double scale, double maxDepth, int maxTime) 
	{
		this.initialiseLaxFriederichs(maxDepth);
		for(int t=0;t<maxTime;t++)
		{
			this.maxDepth = Double.MIN_VALUE;
			this.minDepth = Double.MAX_VALUE;
			this.iterateLaxFriederichs(-1,-1);
			this.iterateLaxFriederichs(-1,1);
			this.iterateLaxFriederichs(1,-1);
			this.iterateLaxFriederichs(1,1);
			this.updateDepth();
			System.out.println("Iteration: " +t+" times.");
		}
		double[][] depthMap  = new double[imageHeight][imageWidth];
		for (int i = 0; i < imageHeight; i++) 
		{ 
			for (int j = 0; j < imageWidth; j++) 
			{
				depthMap[i][j] = (this.depth[i*imageWidth+j]-this.minDepth)*scale;
			}
		}
		return depthMap ;
	}
	private void initialiseLaxFriederichs(double maxDepth)
	{
		for (int i = 0; i < imageHeight; i++) 
		{ 
			for (int j = 0; j < imageWidth; j++) 
			{
				int k = i*imageWidth+j;
				if(this.brightness[k]<5.0)this.brightness[k]=5.0; 
				if (isOnImageEdge(i,j)) 
				{
					double depth=255*albedo/(f*f*brightness[k]);
					this.logDepth[k] = Math.log(Math.sqrt(depth));
				}
				else this.logDepth[k] = Math.log(maxDepth);
			}
		}
	}
	private void updateDepth()
	{
		for (int i = 0; i < imageHeight; i++) 
		{ 
			for (int j = 0; j < imageWidth; j++) 
			{
				this.depth[i*imageWidth +j] = Math.exp(this.logDepth[i*imageWidth+j]);
				this.maxDepth = Math.max(this.maxDepth, this.depth[i*imageWidth+j]);
				this.minDepth = Math.min(this.minDepth, this.depth[i*imageWidth+j]);
			}
		}
	}
	private void iterateLaxFriederichs(int dj, int di)
	{
		int i0 = di > 0 ? 1 : imageHeight -2;
		int i1 = di > 0 ? imageHeight -2 : 1;
		int j0 = dj > 0 ? 1 : imageWidth -2;
		int j1 = dj > 0 ? imageWidth -2 : 1;
		for (int i =i0; i!=i1; i += di) 
		{
			for (int j =j0; j!=j1; j += dj) 
			{
				int kC = (i+0) * imageWidth + (j+0);
				int kE = (i+0) * imageWidth + (j+1);
				int kW = (i+0) * imageWidth + (j-1);
				int kS = (i+1) * imageWidth + (j+0);
				int kN = (i-1) * imageWidth + (j+0);

				double x = j - this.x, y = i - this.y;
				double ux0 = logDepth[kC] - logDepth[kW], ux1 = logDepth[kE] - logDepth[kC];
				double uy0 = logDepth[kC] - logDepth[kN], uy1 = logDepth[kS] - logDepth[kC];

				double I = brightness[kC]*Math.sqrt((f+c)*(f+c) + (x+a)*(x+a) + (y+b)*(y+b))*((f+c)*(f+c) + (x+a)*(x+a) + (y+b)*(y+b))/((f + c)*albedo*100); 
				double uxa = Math.abs(I*(ux0*((f+c)*(f+c) + (x+a)*(x+a)) + uy0*(x+a)*(y+b) + (x+a))/Math.sqrt((f+c)*(f+c)*(ux0*ux0 + uy0*uy0) + (ux0*(x+a) + uy0*(y+b) + 1)*(ux0*(x+a) + uy0*(y+b) + 1)));
				double uxb = Math.abs(I*(ux1*((f+c)*(f+c) + (x+a)*(x+a)) + uy1*(x+a)*(y+b) + (x+a))/Math.sqrt((f+c)*(f+c)*(ux1*ux1 + uy1*uy1) + (ux1*(x+a) + uy1*(y+b) + 1)*(ux1*(x+a) + uy1*(y+b) + 1)));
				double uya = Math.abs(I*(uy0*((f+c)*(f+c) + (y+b)*(y+b)) + ux0*(x+a)*(y+b) + (y+b))/Math.sqrt((f+c)*(f+c)*(ux0*ux0 + uy0*uy0) + (ux0*(x+a) + uy0*(y+b) + 1)*(ux0*(x+a) + uy0*(y+b) + 1)));
				double uyb = Math.abs(I*(uy1*((f+c)*(f+c) + (y+b)*(y+b)) + ux1*(x+a)*(y+b) + (y+b))/Math.sqrt((f+c)*(f+c)*(ux1*ux1 + uy1*uy1) + (ux1*(x+a) + uy1*(y+b) + 1)*(ux1*(x+a) + uy1*(y+b) + 1)));

				double ux = Math.max(uxa, uxb), uy =  Math.max(uya, uyb);
				double dx = (this.logDepth[kE] - this.logDepth[kW])/2.0;
				double dy = (this.logDepth[kS] - this.logDepth[kN])/2.0;
				double sx = (this.logDepth[kE] + this.logDepth[kW])/2.0;
				double sy = (this.logDepth[kS] + this.logDepth[kN])/2.0;

				double H = (1/(255*albedo))*brightness[kC]*Math.sqrt((f+c)*(f+c)*(dx*dx + dy*dy) + (dx*(x+a) + dy*(y+b) +1)*(dx*(x+a) + dy*(y+b) +1))*Math.sqrt((f+c)*(f+c) + (x+a)*(x+a) + (y+b)*(y+b))*((f+c)*(f+c) + (x+a)*(x+a) + (y+b)*(y+b))/(f+c);
				double a = -1/(ux+uy), b = 1/(ux+uy)*(ux*sx + uy*sy - H);
				double newLogDepth=this.fitExponentialTerm(this.logDepth[kC], a, b, -2, 5);
				this.logDepth[kC] = Math.min(newLogDepth, this.logDepth[kC]);
			}
		}
		this.checkColumnBoundaryCondition();
		this.checkRowBoundaryCondition();
	}
	private double fitExponentialTerm(double x0, double a, double b, double c, int time)
	{
		double x=x0,exp;
		for(int t=0;t<time;t++)
		{
			exp=Math.exp(c*x);
			x-=(a*exp + x- b)/(c*a*exp+1);
		}
		return x;
	}
	private void checkColumnBoundaryCondition()
	{
		for (int i = 0; i < imageHeight; i++) 
		{
			int k0 = i*imageWidth, k1 = i*imageWidth + imageWidth-1;
			this.logDepth[k0] = Math.min(Math.max(2*this.logDepth[k0 +1] - this.logDepth[k0+2],this.logDepth[k0+2]),this.logDepth[k0]);
			this.logDepth[k1] = Math.min(Math.max(2*this.logDepth[k1 -1] - this.logDepth[k1-2],this.logDepth[k1-2]),this.logDepth[k1]); 
		}
	}
	private void checkRowBoundaryCondition()
	{
		for (int j = 0; j < imageWidth; j++) 
		{
			int k0 = j, k1 = (imageHeight-1)*imageWidth + j;
			this.logDepth[k0] = Math.min(Math.max(2*this.logDepth[k0 + imageWidth] - this.logDepth[k0 + 2*imageWidth],this.logDepth[k0 + 2*imageWidth]),this.logDepth[k0]);
			this.logDepth[k1] = Math.min(Math.max(2*this.logDepth[k1 - imageWidth] - this.logDepth[k1 - 2*imageWidth],this.logDepth[k1 - 2*imageWidth]),this.logDepth[k1]);
		}
	}
	private boolean isOnImageEdge(int i, int j)
	{
		return (i == 0) || (i == (imageHeight-1)) || (j == 0) || (j == imageWidth -1);
	}
}
