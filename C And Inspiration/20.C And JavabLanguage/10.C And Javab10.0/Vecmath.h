#define Math_PI 3.1415926
double cot(double x)
{
	return 1.0/tan(x);
}
double sec(double x)
{
	return 1.0/cos(x);
}
double csc(double x)
{
	return 1.0/sin(x);
}
double acot(double x)
{
	return atan(1.0/x);
}
double asec(double x)
{
	return acos(1.0/x);
}
double acsc(double x)
{
	return asin(1.0/x);
}
double Log(double a,double x)
{
	return log(x)/log(a);
}
double random()
{
	int r=rand();
	double d=(r+90.0)/(32767+90);
	return d;
}
int factorial(int n)
{
	int fact=1,i;
	for(i=2;i<=n;i++)fact*=i;
	return fact;
}
int permutation(int m,int n)
{
	int perm=1,i;
	for(i=0;i<n;i++)perm*=(m-i);
	return perm;
}
int combination(int m,int n)
{
	return permutation(m,n)/factorial(n);
}
double square(double x)
{
	double d=x/(Math_PI*2);
	double f=d-(int)d;
	return f<=0.5?1:-1;
}
double unitSawtooth(double x,double k)
{
	if(k==1)return x;
	if(x<=-1||x>=1)return 0.0;
	if(x>k)return 1-(x-k)/(1-k);
	if(x<-k)return (-x-k)/(1-k)-1;
	return x/k;
}
double sawtooth(double x,double k)
{
	double d=x/(Math_PI*2);
	double f=d-(int)d;
	return -unitSawtooth(2*(f-0.5),k);
}
double sh(double x)
{
	return (exp(x)-exp(-x))/2;
}
double ch(double x)
{
	return (exp(x)+exp(-x))/2;
}
double th(double x)
{
	return sh(x)/ch(x);
}
double cth(double x)
{
	return ch(x)/sh(x);
}
double arsh(double x)
{
	return log(x+sqrt(x*x+1));
}
double arch(double x)
{
	return log(x+sqrt(x*x-1));
}
double arth(double x)
{
	return 0.5*log((1+x)/(1-x));
}
double gauss(double x,double x0,double d)
{
	double u=(x-x0)/d;
	double k=sqrt(2*Math_PI)*d;
	return exp(-u*u/2)/k;
}
double taylor(double x,double x0,double* df,int n)
{
	double y=0,dx=1;int i;
	for(i=0;i<n;i++)
	{
		y+=df[i]*dx/factorial(i);
		dx*=(x-x0);
	}
	return y;
}
#define Vector struct Vector
Vector
{
	double x;
	double y;
	double z;
};
Vector* newVector(double x,double y,double z)
{
	Vector* vector=(Vector*)(malloc(sizeof(Vector)));
	vector->x=x;
	vector->y=y;
	vector->z=z;
	return vector;
}
Vector** newVectors(int length)
{
	Vector** vectors=(Vector**)(malloc(length*sizeof(Vector*)));
	int i;for(i=0;i<length;i++)vectors[i]=null;
	return vectors;
}
void addVector(Vector* v0,Vector* v1)
{
	v0->x+=v1->x;
	v0->y+=v1->y;
	v0->z+=v1->z;
}
void subVector(Vector* v0,Vector* v1)
{
	v0->x-=v1->x;
	v0->y-=v1->y;
	v0->z-=v1->z;
}
double mulVector(Vector* v0,Vector* v1)
{
	return v0->x*v1->x+v0->y*v1->y+v0->z*v1->z;
}
void mulDouble(Vector* v,double d)
{
	v->x*=d;
	v->y*=d;
	v->z*=d;
}
double lengthOfVector(Vector* v)
{
	return sqrt(mulVector(v,v));
}
void setVector(Vector* v,double x,double y,double z)
{
	v->x=x;
	v->y=y;
	v->z=z;
}
void crossVector(Vector* v0,Vector* v1)
{
	double x=v0->y*v1->z-v1->y*v0->z;
	double y=v0->z*v1->x-v1->z*v0->x;
	double z=v0->x*v1->y-v1->x*v0->y;
	setVector(v0,x,y,z);
}
void projectVector(Vector* v0,Vector* v1)
{
	double l0=lengthOfVector(v0);
	double l1=lengthOfVector(v1);
	double cosA=mulVector(v0,v1)/(l0*l1);
	double k=l0*cosA/l1;
	double x=v1->x*k;
	double y=v1->y*k;
	double z=v1->z*k;
	setVector(v0,x,y,z);
}
double angleToVector(Vector* v0,Vector* v1)
{
	double l0=lengthOfVector(v0);
	double l1=lengthOfVector(v1);
	return acos(mulVector(v0,v1)/(l0*l1));
}
boolean equalVectors(Vector* v0,Vector* v1)
{
	return v0->x==v1->x&&v0->y==v1->y&&v0->z==v1->z;
}
Vector* scanVector()
{
	Vector* v=newVector(0,0,0);
	scanf("<%f,%f,%f>\n",&v->x,&v->y,&v->z);
	return v;
}
void printVector(Vector* v)
{
	printf("<%f,%f,%f>\n",v->x,v->y,v->z);
}
String vtoa(Vector* v)
{
	String a=newChar(256);
	sprintf(a,"<%f,%f,%f>",v->x,v->y,v->z);
	return a;
}
void pushVector(List* stack,Vector* v)
{
	if(v==null)
	{
		pushDouble(stack,0);
		pushDouble(stack,0);
		pushDouble(stack,0);
		return;
	}
	pushDouble(stack,v->x);
	pushDouble(stack,v->y);
	pushDouble(stack,v->z);
}
Vector* popVector(List* stack)
{
	double x=popDouble(stack);
	double y=popDouble(stack);
	double z=popDouble(stack);
	return newVector(x,y,z);
}
void enQueueVector(List* queue,Vector* v)
{
	enQueueDouble(queue,v->x);
	enQueueDouble(queue,v->y);
	enQueueDouble(queue,v->z);
}
Vector* deQueueVector(List* queue)
{
	double x=deQueueDouble(queue);
	double y=deQueueDouble(queue);
	double z=deQueueDouble(queue);
	return newVector(x,y,z);
}
