Point3f* getCoordinate_CtrlSurface(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** B,int k);
void inverse(double** a,int length);
Shape3D* newNurbsCtrlSurface3D(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** weights,int uOrder,int vOrder,int uStep,int vStep)
{
	int m=ctrlPointsLength,n=ctrlPoints0Length,i,j,u,v;
	double* uKnots=getBezierUniformNurbsKnots(m,uOrder);
	double* vKnots=getBezierUniformNurbsKnots(n,vOrder);
	double** U=getParameterMatrix(uKnots,m,uOrder,m);
	double** V=getParameterMatrix(vKnots,n,vOrder,n);
	double** B=new_Double(m*n,m*n);
	for(u=0;u<m;u++)
	{
		for(v=0;v<n;v++)
		{
			for(i=0;i<m;i++)
			{
				for(j=0;j<n;j++)
				{
					B[u*n+v][i*n+j]=U[u][i]*V[v][j];
				}
			}
		}
	}
	inverse(B,m*n);
	Point3f*** newCtrlPoints=new__Point3f(m,n);
	for(i=0;i<m;i++)for(j=0;j<n;j++)newCtrlPoints[i][j]=getCoordinate_CtrlSurface(ctrlPoints,m,n,B,i*n+j);
	return newNurbsSurface3D(newCtrlPoints,m,n,weights,uKnots,vKnots,uOrder,vOrder,uStep,vStep);
}
Point3f* getCoordinate_CtrlSurface(Point3f*** ctrlPoints,int ctrlPointsLength,int ctrlPoints0Length,double** B,int k)
{
	int m=ctrlPointsLength;
	int n=ctrlPoints0Length,i,j;
	double x=0,y=0,z=0;
	for(i=0;i<m;i++)
	{
		for(j=0;j<n;j++)
		{
			x+=ctrlPoints[i][j]->x*B[k][i*n+j];
			y+=ctrlPoints[i][j]->y*B[k][i*n+j];
			z+=ctrlPoints[i][j]->z*B[k][i*n+j];
		}
	}
	return newPoint3f(x,y,z);
}
void inverse(double** a,int length)
{
	int n=length,k,i,j;
	double** AE=new_Double(n,n+n);
	for(i=0;i<n;i++)
	{
		for(j=0;j<n;j++)AE[i][j]=a[i][j];
		AE[i][n+i]=1;
	}
	for(k=0;k<n;k++)
	{
		for(i=0;i<n;i++)
		{
			if(i==k)continue;
			double M=AE[i][k]/AE[k][k];
			for(j=k;j<n+n;j++)AE[i][j]-=M*AE[k][j];
		}
	}
	for(i=0;i<n;i++)
	{
		double M=AE[i][i];
		for(j=0;j<n+n;j++)AE[i][j]/=M;
	}
	for(i=0;i<n;i++)for(j=0;j<n;j++)a[i][j]=AE[i][n+j];
}
