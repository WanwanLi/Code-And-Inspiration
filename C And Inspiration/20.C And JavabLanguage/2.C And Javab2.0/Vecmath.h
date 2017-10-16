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
