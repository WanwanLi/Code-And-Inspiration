int*** new__Int(int l,int m,int n)
{
	int*** d=(int***)malloc(l*sizeof(int**)),i;
	for(i=0;i<l;i++)d[i]=new_Int(m,n);
	return d;
}
double*** new__Double(int l,int m,int n)
{
	double*** d=(double***)malloc(l*sizeof(double**));int i;
	for(i=0;i<l;i++)d[i]=new_Double(m,n);
	return d;
}

#define boolean int
#define true 1
#define false 0
#define Value3D struct Value3D
Value3D
{
	double* values;
	double x0,y0,z0,dx,dy,dz;
	int level,row,column;
};
Value3D* newValue3D(double x0,double dx,double y0,double dy,double z0,double dz,int level,int row,int column)
{
	Value3D* value3D=(Value3D*)malloc(sizeof(Value3D));
	int l=level,m=row,n=column;
	value3D->values=new_double(l*m*n);
	value3D->x0=x0;
	value3D->y0=y0;
	value3D->z0=z0;
	value3D->dx=dx;
	value3D->dy=dy;
	value3D->dz=dz;
	value3D->level=level;
	value3D->row=row;
	value3D->column=column;
	return value3D;
}
double xCoordinate(Value3D* value,int j)
{
	return value->x0+value->dx*j;
}
double yCoordinate(Value3D* value,int k)
{
	return value->y0+value->dy*k;
}
double zCoordinate(Value3D* value,int i)
{
	return value->z0+value->dz*i;
}
boolean vIsBetween(Value3D* value,double v,int k0,int i0,int j0,int k1,int i1,int j1)
{
	int m=value->row,n=value->column;
	boolean l0=value->values[k0*m*n+i0*n+j0]<v;
	boolean l1=v<value->values[k1*m*n+i1*n+j1];
	boolean g0=value->values[k0*m*n+i0*n+j0]>v;
	boolean g1=v>value->values[k1*m*n+i1*n+j1];
	return (l0&&l1)||(g0&&g1);
}
Point3f* getMidPoint(Value3D* value,double v,int k0,int i0,int j0,int k1,int i1,int j1)
{
	int m=value->row,n=value->column;
	double x0=xCoordinate(value,j0);
	double y0=yCoordinate(value,k0);
	double z0=zCoordinate(value,i0);
	double x1=xCoordinate(value,j1);
	double y1=yCoordinate(value,k1);
	double z1=zCoordinate(value,i1);
	double v0=value->values[k0*m*n+i0*n+j0];
	double v1=value->values[k1*m*n+i1*n+j1];
	double dx=x1-x0,dy=y1-y0,dz=z1-z0;
	double x=x0+dx*(v-v0)/(v1-v0);
	double y=y0+dy*(v-v0)/(v1-v0);
	double z=z0+dz*(v-v0)/(v1-v0);
	return newPoint3f(x,y,z);
}
int*** new_lnt(int l,int m,int n)
{
	int*** d=(int***)malloc(l*sizeof(int**)),i;
	for(i=0;i<l;i++)d[i]=new_Int(m,n);
	return d;
}
Shape3D* newIsoSurface3D(Value3D* value,double v)
{
	int m,n,l,i,j,k;
	Point3f** coordinates;
	int* coordinateIndices;
	boolean*** isNotOnIsoSurface;
	int*** upEdgeCoordinateIndices;
	int*** leftEdgeCoordinateIndices;
	int*** frontEdgeCoordinateIndices;
	int*** upCenterEdgeCoordinateIndices;
	int*** leftCenterEdgeCoordinateIndices;
	int*** frontCenterEdgeCoordinateIndices;
	l=value->level;
	m=value->row;
	n=value->column;
	isNotOnIsoSurface=new_lnt(l,m,n);
	upEdgeCoordinateIndices=new_lnt(l,m,n);
	leftEdgeCoordinateIndices=new_lnt(l,m,n);
	frontEdgeCoordinateIndices=new_lnt(l,m,n);
	upCenterEdgeCoordinateIndices=new_lnt(l,m,n);
	leftCenterEdgeCoordinateIndices=new_lnt(l,m,n);
	frontCenterEdgeCoordinateIndices=new_lnt(l,m,n);
	int getCoordinatesCount,getCoordinateIndicesCount,getCoordinates,getCoordinateIndices;
	getCoordinatesCount=0;
	{
		int c=0;
		for(k=0;k<l-1;k++)
		{	
			for(i=0;i<m-1;i++)
			{
				for(j=0;j<n-1;j++)
				{
					if(vIsBetween(value,v,k,i,j,k,i,j+1))upEdgeCoordinateIndices[k][i][j]=c++;
					else upEdgeCoordinateIndices[k][i][j]=-1;
					if(vIsBetween(value,v,k,i,j,k,i+1,j))leftEdgeCoordinateIndices[k][i][j]=c++;
					else leftEdgeCoordinateIndices[k][i][j]=-1;
					if(vIsBetween(value,v,k,i,j,k+1,i,j))frontEdgeCoordinateIndices[k][i][j]=c++;
					else frontEdgeCoordinateIndices[k][i][j]=-1;
					if((i+j+k)%2==0)
					{
						if(vIsBetween(value,v,k,i,j,k,i+1,j+1))upCenterEdgeCoordinateIndices[k][i][j]=c++;
						else upCenterEdgeCoordinateIndices[k][i][j]=-1;
						if(vIsBetween(value,v,k,i,j,k+1,i+1,j))leftCenterEdgeCoordinateIndices[k][i][j]=c++;
						else leftCenterEdgeCoordinateIndices[k][i][j]=-1;
						if(vIsBetween(value,v,k,i,j,k+1,i,j+1))frontCenterEdgeCoordinateIndices[k][i][j]=c++;
						else frontCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					else
					{
						if(vIsBetween(value,v,k,i+1,j,k,i,j+1))upCenterEdgeCoordinateIndices[k][i][j]=c++;
						else upCenterEdgeCoordinateIndices[k][i][j]=-1;
						if(vIsBetween(value,v,k+1,i,j,k,i+1,j))leftCenterEdgeCoordinateIndices[k][i][j]=c++;
						else leftCenterEdgeCoordinateIndices[k][i][j]=-1;
						if(vIsBetween(value,v,k+1,i,j,k,i,j+1))frontCenterEdgeCoordinateIndices[k][i][j]=c++;
						else frontCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
				}
			}
		}
		for(k=l-1;k<l;k++)
		{
			for(i=0;i<m-1;i++)
			{
				for(j=0;j<n-1;j++)
				{
					if(vIsBetween(value,v,k,i,j,k,i,j+1))upEdgeCoordinateIndices[k][i][j]=c++;
					else upEdgeCoordinateIndices[k][i][j]=-1;
					if(vIsBetween(value,v,k,i,j,k,i+1,j))leftEdgeCoordinateIndices[k][i][j]=c++;
					else leftEdgeCoordinateIndices[k][i][j]=-1;
					if((i+j+k)%2==0)
					{
						if(vIsBetween(value,v,k,i,j,k,i+1,j+1))upCenterEdgeCoordinateIndices[k][i][j]=c++;
						else upCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					else
					{
						if(vIsBetween(value,v,k,i+1,j,k,i,j+1))upCenterEdgeCoordinateIndices[k][i][j]=c++;
						else upCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
				}
			}
		}
		for(k=0;k<l-1;k++)
		{
			for(i=m-1;i<m;i++)
			{
				for(j=0;j<n-1;j++)
				{
					if(vIsBetween(value,v,k,i,j,k,i,j+1))upEdgeCoordinateIndices[k][i][j]=c++;
					else upEdgeCoordinateIndices[k][i][j]=-1;
					if(vIsBetween(value,v,k,i,j,k+1,i,j))frontEdgeCoordinateIndices[k][i][j]=c++;
					else frontEdgeCoordinateIndices[k][i][j]=-1;
					if((i+j+k)%2==0)
					{
						if(vIsBetween(value,v,k,i,j,k+1,i,j+1))frontCenterEdgeCoordinateIndices[k][i][j]=c++;
						else frontCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					else
					{
						if(vIsBetween(value,v,k+1,i,j,k,i,j+1))frontCenterEdgeCoordinateIndices[k][i][j]=c++;
						else frontCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
				}
			}
		}
		for(k=0;k<l-1;k++)
		{
			for(i=0;i<m-1;i++)
			{
				for(j=n-1;j<n;j++)
				{
					if(vIsBetween(value,v,k,i,j,k,i+1,j))leftEdgeCoordinateIndices[k][i][j]=c++;
					else leftEdgeCoordinateIndices[k][i][j]=-1;
					if(vIsBetween(value,v,k,i,j,k+1,i,j))frontEdgeCoordinateIndices[k][i][j]=c++;
					else frontEdgeCoordinateIndices[k][i][j]=-1;
					if((i+j+k)%2==0)
					{
						if(vIsBetween(value,v,k,i,j,k+1,i+1,j))leftCenterEdgeCoordinateIndices[k][i][j]=c++;
						else leftCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
					else
					{
						if(vIsBetween(value,v,k+1,i,j,k,i+1,j))leftCenterEdgeCoordinateIndices[k][i][j]=c++;
						else leftCenterEdgeCoordinateIndices[k][i][j]=-1;
					}
				}
			}
		}
		for(k=0;k<l-1;k++)
		{
			for(i=m-1;i<m;i++)
			{
				for(j=n-1;j<n;j++)
				{
					if(vIsBetween(value,v,k,i,j,k+1,i,j))frontEdgeCoordinateIndices[k][i][j]=c++;
					else frontEdgeCoordinateIndices[k][i][j]=-1;
				}
			}
		}
		for(k=l-1;k<l;k++)
		{
			for(i=0;i<m-1;i++)
			{
				for(j=n-1;j<n;j++)
				{
					if(vIsBetween(value,v,k,i,j,k,i+1,j))leftEdgeCoordinateIndices[k][i][j]=c++;
					else leftEdgeCoordinateIndices[k][i][j]=-1;
				}
			}
		}
		for(k=l-1;k<l;k++)
		{
			for(i=m-1;i<m;i++)
			{
				for(j=0;j<n-1;j++)
				{
					if(vIsBetween(value,v,k,i,j,k,i,j+1))upEdgeCoordinateIndices[k][i][j]=c++;
					else upEdgeCoordinateIndices[k][i][j]=-1;
				}
			}
		}
		getCoordinatesCount=c;
	}
	coordinates=new_Point3f(getCoordinatesCount);
	getCoordinateIndicesCount=0;
	{
		int c=0;
		for(k=0;k<l-1;k++)
		{
			for(i=0;i<m-1;i++)
			{
				for(j=0;j<n-1;j++)
				{
					int b=0;
					if((i+j+k)%2==0)
					{
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1)c+=12;
						else b++;
						if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1)c+=12;
						else if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i+1][j]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else b++;
						if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k+1][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=12;
						else b++;
						if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=12;
						else b++;
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=12;
						else if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=12;
						else if(upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=12;
						else b++;
					}
					else
					{
						if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else b++;
						if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&frontEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&frontEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k+1][i][j]!=-1&&upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else b++;
						if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i][j+1]!=-1&&upEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=12;
						else b++;
						if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=6;
						else if(leftEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=6;
						else if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=12;
						else if(leftEdgeCoordinateIndices[k][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)c+=12;
						else b++;
						if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)c+=6;
						else if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=6;
						else if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)c+=12;
						else b++;
					}
					isNotOnIsoSurface[k][i][j]=b==5?true:false;
				}
			}
		}
		getCoordinateIndicesCount=c;
	}
	coordinateIndices=new_int(getCoordinateIndicesCount);
	getCoordinates=0;
	{
		int c=0;
		for(k=0;k<l-1;k++)
		{
			for(i=0;i<m-1;i++)
			{
				for(j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k,i,j+1);
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k,i+1,j);
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k+1,i,j);
					if((i+j+k)%2==0)
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k,i+1,j+1);
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k+1,i+1,j);
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k+1,i,j+1);
					}
					else
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i+1,j,k,i,j+1);
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k+1,i,j,k,i+1,j);
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k+1,i,j,k,i,j+1);
					}
					
				}
			}
		}
		for(k=l-1;k<l;k++)
		{
			for(i=0;i<m-1;i++)
			{
				for(j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k,i,j+1);
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k,i+1,j);
					if((i+j+k)%2==0)
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k,i+1,j+1);
					}
					else
					{
						if(upCenterEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i+1,j,k,i,j+1);
					}
				}
			}
		}
		for(k=0;k<l-1;k++)
		{
			for(i=m-1;i<m;i++)
			{
				for(j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k,i,j+1);
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k+1,i,j);
					if((i+j+k)%2==0)
					{
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k+1,i,j+1);
					}
					else
					{
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k+1,i,j,k,i,j+1);
					}
				}
			}
		}
		for(k=0;k<l-1;k++)
		{
			for(i=0;i<m-1;i++)
			{
				for(j=n-1;j<n;j++)
				{
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k,i+1,j);
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k+1,i,j);
					if((i+j+k)%2==0)
					{
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k+1,i+1,j);
					}
					else
					{
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k+1,i,j,k,i+1,j);
					}
				}
			}
		}
		for(k=0;k<l-1;k++)
		{
			for(i=m-1;i<m;i++)
			{
				for(j=n-1;j<n;j++)
				{
					if(frontEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k+1,i,j);
				}
			}
		}
		for(k=l-1;k<l;k++)
		{
			for(i=0;i<m-1;i++)
			{
				for(j=n-1;j<n;j++)
				{
					if(leftEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k,i+1,j);
				}
			}
		}
		for(k=l-1;k<l;k++)
		{
			for(i=m-1;i<m;i++)
			{
				for(j=0;j<n-1;j++)
				{
					if(upEdgeCoordinateIndices[k][i][j]!=-1)coordinates[c++]=getMidPoint(value,v,k,i,j,k,i,j+1);
				}
			}
		}
	}
	getCoordinateIndices=0;
	{
		int c=0;
		for(k=0;k<l-1;k++)
		{
			for(i=0;i<m-1;i++)
			{
				for(j=0;j<n-1;j++)
				{
					if(isNotOnIsoSurface[k][i][j])continue;
					if((i+j+k)%2==0)
					{
						if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i+1][j]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
					}
					else
					{
						if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k][i][j]!=-1&&leftEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftEdgeCoordinateIndices[k][i][j]!=-1&&frontEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i][j]!=-1&&upEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&frontEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&frontEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j]!=-1&&upEdgeCoordinateIndices[k+1][i+1][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i+1][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k+1][i][j]!=-1&&leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftEdgeCoordinateIndices[k+1][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1)
						{
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k+1][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i][j+1]!=-1&&upEdgeCoordinateIndices[k+1][i][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upEdgeCoordinateIndices[k][i+1][j]!=-1&&leftEdgeCoordinateIndices[k][i][j+1]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftEdgeCoordinateIndices[k][i][j+1]!=-1&&frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontEdgeCoordinateIndices[k][i+1][j+1]!=-1&&upEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1)
						{
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							coordinateIndices[c++]=upEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontEdgeCoordinateIndices[k][i+1][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1)
						{
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(leftCenterEdgeCoordinateIndices[k][i][j+1]!=-1&&frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&leftCenterEdgeCoordinateIndices[k][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=leftCenterEdgeCoordinateIndices[k][i][j+1];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
						else if(frontCenterEdgeCoordinateIndices[k][i+1][j]!=-1&&upCenterEdgeCoordinateIndices[k+1][i][j]!=-1&&frontCenterEdgeCoordinateIndices[k][i][j]!=-1&&upCenterEdgeCoordinateIndices[k][i][j]!=-1)
						{
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k+1][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=upCenterEdgeCoordinateIndices[k][i][j];
							coordinateIndices[c++]=frontCenterEdgeCoordinateIndices[k][i+1][j];
							coordinateIndices[c++]=coordinateIndices[c-1];
							coordinateIndices[c++]=coordinateIndices[c-3];
							coordinateIndices[c++]=coordinateIndices[c-5];
						}
					}
				}
			}
		}
	}
	GeometryInfo* GeometryInfo1=newGeometryInfo(TRIANGLE_ARRAY);
	setCoordinates(GeometryInfo1,coordinates,getCoordinatesCount);
	setCoordinateIndices(GeometryInfo1,coordinateIndices,getCoordinateIndicesCount);
	generateNormals(GeometryInfo1);
	Shape3D* shape3D=newShape3D();
	setGeometry(shape3D,getGeometryArray(GeometryInfo1));
	return shape3D;
}
