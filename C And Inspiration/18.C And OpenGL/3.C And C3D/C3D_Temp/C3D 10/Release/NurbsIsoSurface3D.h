double getValue(double*** ctrlValues,int ctrlValuesLength,int ctrlValues0Length,int ctrlValues00Length,double*** weights,double** U,double** V,double** W,int u,int v,int w);
double getValue_null(double*** ctrlValues,int ctrlValuesLength,int ctrlValues0Length,int ctrlValues00Length,double** U,double** V,double** W,int u,int v,int w);
Shape3D* newNurbsIsoSurface3D(Point3f* point0,Point3f* point1,double value,double*** ctrlValues,int ctrlValuesLength,int ctrlValues0Length,int ctrlValues00Length,double*** weights,double* uKnots,double* vKnots,double* wKnots,int uOrder,int vOrder,int wOrder,int uStep,int vStep,int wStep)
{
	int h=wStep,r=uStep,c=vStep,v=0,l=ctrlValuesLength,m=ctrlValues0Length,n=ctrlValues00Length,i,j,k;
	double x0=point0->x,y0=point0->y,z0=point0->z,x1=point1->x,y1=point1->y,z1=point1->z;
	double dx=(x1-x0)/(c-1),dy=(y1-y0)/(h-1),dz=(z1-z0)/(r-1);
	double** U=getParameterMatrix(uKnots,r,uOrder,m);
	double** V=getParameterMatrix(vKnots,c,vOrder,n);
	double** W=getParameterMatrix(wKnots,h,wOrder,l);
	Value3D* value3D=newValue3D(x0,dx,y0,dy,z0,dz,h,r,c);
	if(weights==null)for(k=0;k<h;k++)for(i=0;i<r;i++)for(j=0;j<c;j++)value3D->values[k*r*c+i*c+j]=getValue_null(ctrlValues,l,m,n,U,V,W,i,j,k);
	else for(k=0;k<h;k++)for(i=0;i<r;i++)for(j=0;j<c;j++)value3D->values[k*r*c+i*c+j]=getValue(ctrlValues,l,m,n,weights,U,V,W,i,j,k);
	return newIsoSurface3D(value3D,value);
}
double getValue(double*** ctrlValues,int ctrlValuesLength,int ctrlValues0Length,int ctrlValues00Length,double*** weights,double** U,double** V,double** W,int u,int v,int w)
{
	int l=ctrlValuesLength;
	int m=ctrlValues0Length;
	int n=ctrlValues00Length,i,j,k;
	double value=0,weight=0;
	for(k=0;k<l;k++)
	{
		for(i=0;i<m;i++)
		{
			for(j=0;j<n;j++)
			{
				value+=ctrlValues[k][i][j]*U[u][i]*V[v][j]*W[w][k]*weights[k][i][j];
				weight+=U[u][i]*V[v][j]*W[w][k]*weights[k][i][j];
			}
		}
	}
	return value/weight;
}
double getValue_null(double*** ctrlValues,int ctrlValuesLength,int ctrlValues0Length,int ctrlValues00Length,double** U,double** V,double** W,int u,int v,int w)
{
	int l=ctrlValuesLength;
	int m=ctrlValues0Length;
	int n=ctrlValues00Length,i,j,k;
	double value=0;
	for(k=0;k<l;k++)
	{
		for(i=0;i<m;i++)
		{
			for(j=0;j<n;j++)
			{
				value+=ctrlValues[k][i][j]*U[u][i]*V[v][j]*W[w][k];
			}
		}
	}
	return value;
}
