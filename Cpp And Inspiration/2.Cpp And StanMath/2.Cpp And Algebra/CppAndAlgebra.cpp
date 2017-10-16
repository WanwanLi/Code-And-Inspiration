#include <iostream>
#include <Eigen/Dense>
using namespace std;
using namespace Eigen;

const int size=30; 
char screen[size][size];
void clearScreen()
{
	for(int i=0; i<size; i++)
	{
		for(int j=0; j<size; j++)screen[i][j]=' ';
	}
	for(int i=0; i<size; i++)screen[i][0]='|';
	for(int j=0; j<size; j++)screen[size-1][j]='_';
}
void drawPoint(double x, double y, double scaleX, double scaleY, char point)
{
	int X=(int)(x*scaleX+0.5), Y=(int)(y*scaleY+0.5);
	if(0<X&&X<size-1&&0<Y&&Y<size-1)screen[size-1-Y][X]=point;
}
void drawLine(const Vector2d& args, const Vector2d& scale, char point)
{
	double a=args(0), b=args(1);
	for(int x=0; x<size; x++)
	{
		drawPoint(x, a*x/scale(0)+b, 1, scale(1), point);
	}
}
void drawCubicCurve(const Vector4d& args, const Vector2d& scale, char point)
{
	double a=args(0), b=args(1);
	double c=args(2), d=args(3);
	for(int x=0; x<size; x++)
	{
		double t=(x+0.0)/scale(0);
		double y=a*t*t*t+b*t*t+c*t+d;
		drawPoint(x, y, 1, scale(1), point);
	}
}
void drawPoints(const VectorXd& x, const VectorXd& y,  const Vector2d& scale, char point)
{
	for(int i=0; i<x.size(); i++)drawPoint(x[i], y[i], scale(0), scale(1), point);
}
void printScreen()
{
	for(int i=0; i<size; i++)
	{
		for(int j=0; j<size; j++)
		{
			cout<<screen[i][j];
			cout<<(i==size-1?"_":" ");
		}
		cout<<endl;
	}
}
void testAlgebra1()
{
	cout<<"Solve: Ax=b"<<endl;
	Matrix3d A; A<<1,2,3,  4,5,6,  7,8,10;
	cout<<"A="<<endl<<A<<endl;
	Vector3d b; b<<3, 3, 4;
	cout<<"b="<<endl<<b<<endl;
	Vector3d x=A.colPivHouseholderQr().solve(b);
	cout<<"x="<<endl<<x<<endl;
	cout<<"A*x="<<endl<<A*x<<endl;
	double normL2=(A*x-b).norm();
	cout<<"normL2(Ax-b)="<<normL2<<endl;
	cout<<"error="<<normL2/b.norm()<<endl;
	ColPivHouseholderQR<Matrix3d> QR(A);
	cout<<"QR(A).solve(b)="<<endl<<QR.solve(b)<<endl;
	cout<<"A.ldlt().solve(b)="<<endl<<A.ldlt().solve(b)<<endl;
}
void testAlgebra2()
{
	Matrix3d A; A<<1,2,3,  4,5,6,  7,8,10;
	cout<<"A="<<endl<<A<<endl;
	cout<<"A.inverse()="<<endl<<A.inverse()<<endl;
	cout<<"A.determinant()="<<A.determinant()<<endl;
	cout<<"A*A.inverse()="<<endl<<A*A.inverse()<<endl;
	SelfAdjointEigenSolver<Matrix3d> eigen(A);
	if(eigen.info()!=Success)abort();
	cout<<"eigenvalues="<<endl<<
	eigen.eigenvalues()<<endl;
	cout<<"eigenvectors="<<endl<<
	eigen.eigenvectors()<<endl;
}
void testAlgebra3()
{
	Matrix2d A; A << 2,1, 2, 0.9999999999;
	cout<<"A="<<endl<<A<<endl;
	FullPivLU<Matrix2d> LU(A);
	cout<<"A.rank="<<LU.rank()<<endl;
	LU.setThreshold(1e-5);
	cout<<"A.rank(th:1e-5)="<<LU.rank()<<endl;
	cout<<"A.kernel="<<endl<<LU.kernel()<<endl;
	cout<<"A.image="<<endl<<LU.image(A)<<endl;
}
void testAlgebra4()
{
	MatrixXd A=MatrixXd::Random(3, 2);
	VectorXd b=VectorXd::Random(3);
	cout<<"A="<<endl<<A<<endl;
	cout<<"b="<<endl<<b<<endl;
	cout<<"least-squares solution is: x="<<endl;
	cout<<A.jacobiSvd(ComputeThinU|ComputeThinV).solve(b)<<endl;
}
void testAlgebra5()
{
	cout<<"Line Fitting(least-square):"<<endl;
	cout<<"Find best (a, b) that : a.x+b=y"<<endl;
	VectorXd x(5); x<<2, 6, 20, 30, 40;
	VectorXd y(5); y<<20, 18, 10, 6, 2;
	cout<<"Line sample points are:"<<endl;
	cout<<"x="<<endl<<x<<endl;  cout<<"y="<<endl<<y<<endl;
	cout<<"The least-square matrix equation is: Ax=b, x=(a, b)"<<endl;	
	MatrixXd A(x.size(), 2); A.col(0)=x; for(int i=0; i<A.rows(); i++)A(i, 1)=1.0;
	VectorXd b=y; cout<<"A="<<endl<<A<<endl; cout<<"b="<<endl<<b<<endl;
	Vector2d solution=A.jacobiSvd(ComputeThinU|ComputeThinV).solve(b);
	cout<<"least-squares solution is: x=(a, b)="<<endl<<solution<<endl;
	clearScreen(); Vector2d scale; scale<<0.5, 1.0;
	drawLine(solution, scale, '.');
	drawPoints(x, y, scale, 'X'); printScreen();
}
void testAlgebra6()
{
	cout<<"Cubic Curve Fitting(least-square):"<<endl;
	cout<<"Find best (a, b, c, d) that : a.x^3+b.x^2+c.x+d=y"<<endl;
	VectorXd x(6); x<<1, 2, 3, 4, 5, 6;
	VectorXd y(6); y<<2.1, 3.5, 4.2, 3.1, 4.4, 6.8;
	cout<<"Cubic curve sample points are:"<<endl;
	cout<<"x="<<endl<<x<<endl; cout<<"y="<<endl<<y<<endl;
	cout<<"The least-square matrix equation is: Ax=b, x=(a, b, c, d)"<<endl;	
	MatrixXd A(x.size(), 4); 
	for(int i=0; i<A.rows(); i++)
	{
		A(i, 0)=x(i)*x(i)*x(i);
		A(i, 1)=x(i)*x(i);
		A(i, 2)=x(i);
		A(i, 3)=1;
	}
	VectorXd b=y; cout<<"A="<<endl<<A<<endl; cout<<"b="<<endl<<b<<endl;
	Vector4d solution=A.jacobiSvd(ComputeThinU|ComputeThinV).solve(b);
	cout<<"least-squares solution is: x=(a, b, c, d)="<<endl<<solution<<endl;
	clearScreen(); Vector2d scale; scale<<3.0, 3.0;
	drawCubicCurve(solution, scale, '.');
	drawPoints(x, y, scale, 'X'); printScreen();
}
MatrixXd transform2d(const Matrix3d& M, const VectorXd& X, const VectorXd& Y)
{
	MatrixXd P=MatrixXd(X.size(), 2);
	for(int i=0; i<P.rows(); i++)
	{
		Vector3d V(X(i), Y(i), 1);
		double x=M.row(0)*V;
		double y=M.row(1)*V;
		double z=M.row(2)*V;
		P.row(i)=Vector2d(x/z, y/z);
	}
	return P;
}
void testAlgebra7()
{
	cout<<"2D Affine Transform Matrix Fitting(least-square):"<<endl;
	cout<<"Find best affine transform matrix H that :"<<endl;
	cout<<"	x2=[h1 h2 h3]x1	"<<endl;
	cout<<"	y2=[h4 h5 h6]y1	"<<endl;
	cout<<"	 1=[ 0  0  1] 1	"<<endl;
	cout<<"So we have linear equation: Ah=b, where h=(h1, .., h6)"<<endl;
	cout<<"h1.x1+h2.y1+h3=x2"<<endl;
	cout<<"h4.x1+h5.y1+h6=y2"<<endl;
	cout<<"where A[x]=[x1 y1 1  0  0  0]"<<endl;
	cout<<"where A[y]=[ 0   0 0 x1 y1 1]"<<endl;
	VectorXd x1(6); x1<<1, 2, 3, 4, 5, 6;
	VectorXd y1(6); y1<<2.1, 3.5, 4.2, 3.1, 4.4, 6.8;
	Matrix3d H=Matrix3d::Random(); 
	H.row(2)=Vector3d(0.0, 0.0, 1.0);
	MatrixXd M=transform2d(H, x1, y1);
	VectorXd x2=M.col(0), y2=M.col(1);
	cout<<"Original sample points (x1, y1) are:"<<endl;
	cout<<"x1="<<endl<<x1<<endl; 
	cout<<"y1="<<endl<<y1<<endl;
	cout<<"Affine Transform matrix is:"<<endl;
	cout<<"H="<<endl<<H<<endl;
	cout<<"Transformed sample points (x2, y2) are:"<<endl;
	cout<<"x2="<<endl<<x2<<endl; 
	cout<<"y2="<<endl<<y2<<endl;
	cout<<"The least-square matrix equation is: Ah=b, h=(h1, ..., h6)"<<endl;	
	MatrixXd A(x1.size()*2, 6); VectorXd b(x1.size()*2); 
	for(int i=0; i<x1.size(); i++)
	{
		VectorXd X(6); X<<x1(i),y1(i),1, 0,0,0; A.row(i*2+0)=X; b(i*2+0)=x2(i);
		VectorXd Y(6); Y<<0,0,0, x1(i),y1(i),1; A.row(i*2+1)=Y; b(i*2+1)=y2(i);
	}
	cout<<"A="<<endl<<A<<endl; cout<<"b="<<endl<<b<<endl; 
	VectorXd h=A.jacobiSvd(ComputeThinU|ComputeThinV).solve(b);
	MatrixXd S=Map<MatrixXd>(h.data(), 3, 2).transpose(); Matrix3d T; 
	T.row(0)=S.row(0); T.row(1)=S.row(1); T.row(2)=Vector3d(0.0, 0.0, 1.0);
	cout<<"The least-squares solution of Ah=b, h=(h1, ..., h6) is:"<<endl<<T<<endl;
	cout<<"Using solution to transform sample points:"<<endl<<transform2d(T, x1, y1)<<endl;
}
void testAlgebra8()
{
	cout<<"2D Homography Matrix Fitting(least-square):"<<endl;
	cout<<"Find best homography matrix H that :"<<endl;
	cout<<"	x\'=[h1 h2 h3]x1	"<<endl;
	cout<<"	y\'=[h4 h5 h6]y1	"<<endl;
	cout<<"	z\'=[h7 h8 h9] 1	"<<endl;
	cout<<"However, 2D point (x2, y2)=(x\'/z\', y\'/z\') :"<<endl;
	cout<<"So we have (x2.z\', y2.z\')=(x\', y\') that is :"<<endl;
	cout<<"x2.(h7.x1+h8.y1+h9)=h1.x1+h2.y1+h3"<<endl;
	cout<<"y2.(h7.x1+h8.y1+h9)=h4.x1+h5.y1+h6"<<endl;
	cout<<"So we have linear equation for PH=0 :"<<endl;
	cout<<"h1.x1+h2.y1+h3-x2.x1.h7-x2.y1.h8-x2.h9=0"<<endl;
	cout<<"h4.x1+h5.y1+h6-y2.x1.h7-y2.y1.h8-y2.h9=0"<<endl;
	cout<<"where P[x]=[x1 y1 1  0  0  0 -x2.x1 -x2.y1 -x2]"<<endl;
	cout<<"where P[y]=[ 0   0 0 x1 y1 1 -y2.x1 -y2.y1 -y2]"<<endl;
	cout<<"where H=[h1 h2 h3 h4 h5 h6 h7 h8 h9]"<<endl;
	VectorXd x1(6); x1<<1, 2, 3, 4, 5, 6;
	VectorXd y1(6); y1<<2.1, 3.5, 4.2, 3.1, 4.4, 6.8;
	Matrix3d H=Matrix3d::Random();
	MatrixXd M=transform2d(H, x1, y1);
	VectorXd x2=M.col(0), y2=M.col(1);
	cout<<"Original sample points (x1, y1) are:"<<endl;
	cout<<"x1="<<endl<<x1<<endl; 
	cout<<"y1="<<endl<<y1<<endl;
	cout<<"Transform matrix is:"<<endl;
	cout<<"H="<<endl<<H<<endl;
	cout<<"Transformed sample points (x2, y2) are:"<<endl;
	cout<<"x2="<<endl<<x2<<endl; 
	cout<<"y2="<<endl<<y2<<endl;
	cout<<"The least-square matrix equation is: PH=0, H=(h1, ..., h9)"<<endl;	
	MatrixXd P(x1.size()*2, 9); 
	for(int i=0; i<x1.size(); i++)
	{
		VectorXd X(9); X<<x1(i),y1(i),1, 0,0,0, -x2(i)*x1(i),-x2(i)*y1(i),-x2(i); P.row(i*2+0)=X;
		VectorXd Y(9); Y<<0,0,0, x1(i),y1(i),1, -y2(i)*x1(i),-y2(i)*y1(i),-y2(i); P.row(i*2+1)=Y;
	}
	cout<<"P="<<endl<<P<<endl; 
	VectorXd solution=-P.fullPivLu().kernel();
	Matrix3d S=Map<Matrix3d>(solution.data()).transpose();
	cout<<"The least-squares solution of PH=0, H=(h1, ..., h9)="<<endl<<S<<endl;
	cout<<"Using solution to transform sample points:"<<endl<<transform2d(S, x1, y1)<<endl;
}
int main()
{
	testAlgebra1();
	testAlgebra2();
	testAlgebra3();
	testAlgebra4();
	testAlgebra5();
	testAlgebra6();
	testAlgebra7();
	testAlgebra8();
}
